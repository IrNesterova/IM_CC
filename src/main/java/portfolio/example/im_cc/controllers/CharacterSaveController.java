package portfolio.example.im_cc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterSave;
import portfolio.example.im_cc.repositories.CharacterSaveRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/character")
public class CharacterSaveController {

    // No I, O, 0, 1 — avoids ambiguity when reading the code aloud
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LEN = 6;
    private static final SecureRandom RNG = new SecureRandom();

    @Autowired
    private CharacterSaveRepository repo;

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> create(@RequestBody String json) {
        String code;
        int attempts = 0;
        do {
            code = generateCode();
            if (++attempts > 30) throw new RuntimeException("Could not generate unique save code");
        } while (repo.findBySaveCode(code).isPresent());

        CharacterSave cs = new CharacterSave();
        cs.setSaveCode(code);
        cs.setData(json);
        cs.setCreatedAt(LocalDateTime.now());
        cs.setUpdatedAt(LocalDateTime.now());
        repo.save(cs);
        return ResponseEntity.ok(Map.of("code", code));
    }

    @PutMapping("/save/{code}")
    public ResponseEntity<Map<String, String>> update(@PathVariable String code,
                                                      @RequestBody String json) {
        return repo.findBySaveCode(code.toUpperCase())
                .map(cs -> {
                    cs.setData(json);
                    cs.setUpdatedAt(LocalDateTime.now());
                    repo.save(cs);
                    return ResponseEntity.ok(Map.of("code", cs.getSaveCode()));
                })
                .orElseGet(() -> ResponseEntity.notFound().<Map<String, String>>build());
    }

    @GetMapping("/load/{code}")
    public ResponseEntity<String> load(@PathVariable String code) {
        return repo.findBySaveCode(code.toUpperCase())
                .map(cs -> ResponseEntity.ok(cs.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LEN);
        for (int i = 0; i < CODE_LEN; i++) sb.append(CHARS.charAt(RNG.nextInt(CHARS.length())));
        return sb.toString();
    }
}
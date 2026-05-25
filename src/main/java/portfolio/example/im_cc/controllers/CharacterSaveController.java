package portfolio.example.im_cc.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.example.im_cc.models.CharacterCreationModel;
import portfolio.example.im_cc.models.CharacterSave;
import portfolio.example.im_cc.repositories.CharacterSaveRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/character")
public class CharacterSaveController {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LEN = 6;
    private static final SecureRandom RNG = new SecureRandom();

    @Autowired private CharacterSaveRepository repo;
    @Autowired private ObjectMapper objectMapper;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> save(
            @RequestBody String editsJson,
            HttpSession httpSession) {

        String combined = buildCombined(httpSession, editsJson);
        String existingCode = (String) httpSession.getAttribute("currentSaveCode");

        CharacterSave cs = null;
        if (existingCode != null) {
            cs = repo.findBySaveCode(existingCode).orElse(null);
        }

        if (cs == null) {
            String code;
            int attempts = 0;
            do {
                code = generateCode();
                if (++attempts > 30) throw new RuntimeException("Could not generate unique save code");
            } while (repo.findBySaveCode(code).isPresent());
            cs = new CharacterSave();
            cs.setSaveCode(code);
            cs.setCreatedAt(LocalDateTime.now());
        }

        cs.setData(combined);
        cs.setUpdatedAt(LocalDateTime.now());
        repo.save(cs);

        httpSession.setAttribute("currentSaveCode", cs.getSaveCode());
        return ResponseEntity.ok(Map.of("code", cs.getSaveCode()));
    }

    @GetMapping("/load/{code}")
    @ResponseBody
    public ResponseEntity<String> load(@PathVariable String code) {
        return repo.findBySaveCode(code.toUpperCase())
                .map(cs -> ResponseEntity.ok(cs.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Restores the full character (session + summary edits) and redirects to summary
    @GetMapping("/restore/{code}")
    public String restore(@PathVariable String code, HttpSession httpSession) {
        return repo.findBySaveCode(code.toUpperCase())
                .map(cs -> {
                    try {
                        tools.jackson.databind.JsonNode root = objectMapper.readTree(cs.getData());
                        JsonNode ccmNode   = root.path("ccm");
                        JsonNode editsNode = root.path("edits");

                        if (!ccmNode.isMissingNode()) {
                            CharacterCreationModel ccm =
                                objectMapper.treeToValue(ccmNode, CharacterCreationModel.class);
                            httpSession.setAttribute("characterCreation", ccm);
                        }

                        String edits = !editsNode.isMissingNode()
                                ? editsNode.toString()
                                : (ccmNode.isMissingNode() ? cs.getData() : null);

                        if (edits != null) {
                            httpSession.setAttribute("pendingSummaryEdits", edits);
                        }
                        httpSession.setAttribute("currentSaveCode",  cs.getSaveCode());
                        httpSession.setAttribute("pendingSaveCode",  cs.getSaveCode());
                    } catch (Exception e) {
                        httpSession.setAttribute("pendingSummaryEdits", cs.getData());
                        httpSession.setAttribute("currentSaveCode",  cs.getSaveCode());
                        httpSession.setAttribute("pendingSaveCode",  cs.getSaveCode());
                    }
                    return "redirect:/summary";
                })
                .orElse("redirect:/?loadError=1");
    }

    private String buildCombined(HttpSession httpSession, String editsJson) {
        CharacterCreationModel ccm =
                (CharacterCreationModel) httpSession.getAttribute("characterCreation");
        if (ccm == null) return editsJson;
        try {
            return "{\"ccm\":" + objectMapper.writeValueAsString(ccm) + ",\"edits\":" + editsJson + "}";
        } catch (Exception e) {
            return editsJson;
        }
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LEN);
        for (int i = 0; i < CODE_LEN; i++) sb.append(CHARS.charAt(RNG.nextInt(CHARS.length())));
        return sb.toString();
    }
}
package portfolio.example.im_cc.models;


import java.util.HashMap;
import java.util.Map;

public class CharacterCreationModel {
    private Long originId;
    private Long factionId;
    private Map<String, String> characteristics = new HashMap<>();
    private Map<Long, Integer> factionSkillAdvances = new HashMap<>();
    private Map<Long, Long> factionChoices = new HashMap<>();


    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Map<Long, Integer> getFactionSkillAdvances() {
        return factionSkillAdvances;
    }

    public void setFactionSkillAdvances(Map<Long, Integer> factionSkillAdvances) {
        this.factionSkillAdvances = factionSkillAdvances;
    }

    public Map<Long, Long> getFactionChoices() {
        return factionChoices;
    }

    public void setFactionChoices(Map<Long, Long> factionChoices) {
        this.factionChoices = factionChoices;
    }

    public Long getFactionId() {
        return factionId;
    }

    public void setFactionId(Long factionId) {
        this.factionId = factionId;
    }
}

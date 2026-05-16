package portfolio.example.im_cc.models;


import java.util.HashMap;
import java.util.Map;

public class CharacterCreationModel {
    private Long originId;
    private Map<String, String> characteristics = new HashMap<>();


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
}

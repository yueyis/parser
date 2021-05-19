package parser.fix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class FixField {
    private static final String DUMMY_TAG_VALUE = "*";

    private String tag = "";
    private String value = "";
    private String name = "";
    private String desc = "";
    private List<String> labeList = new ArrayList<>();
    private String msgCategory;
    // mapping from value to desc
    private HashMap<String, String> valueMeaning = new HashMap<>();

    public FixField() {
    }

    public FixField(String tag) {
        this.tag = tag;
    }

    public FixField(FixField field) {
        this.tag = field.getTag();
        this.value = field.getValue();
        this.name = field.getName();
        this.desc = field.getDesc();
        this.labeList = field.getLabeList();
        this.msgCategory = field.getMsgCategory();
        this.valueMeaning = field.getValueMeaning();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;

        List<String> headerTag = Arrays.asList("8", "9", "35", "34", "43", "49", "52", "56", "122");
        List<String> trailerTag = Arrays.asList("10");
        if (headerTag.stream().anyMatch(str -> str.trim().equals(tag))) {
            this.msgCategory = "header";
        } else if (trailerTag.stream().anyMatch(str -> str.trim().equals(tag))) {
            this.msgCategory = "trailer";
        } else {
            this.msgCategory = "body";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.updateDesc();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getLabeList() {
        return labeList;
    }

    public void setLabeList(List<String> labeList) {
        this.labeList = labeList;
    }

    public void updateDesc() {
        if (valueMeaning.containsKey(this.value)) {
            this.desc = valueMeaning.get(this.value);
        } else if (valueMeaning.containsKey(DUMMY_TAG_VALUE)) {
            this.desc = valueMeaning.get(DUMMY_TAG_VALUE);
        }
    }

    public String getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(String msgCategory) {
        this.msgCategory = msgCategory;
    }

    public void addMmap(String value, String desc) {
        this.valueMeaning.put(value, desc);
    }

    public HashMap<String, String> getValueMeaning() {
        return valueMeaning;
    }

    public void setValueMeaning(HashMap<String, String> valueMeaning) {
        this.valueMeaning = valueMeaning;
    }

}

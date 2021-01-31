package parser.fix;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class FixField {

    private String tag;
    private String value;
    private String name = "";
    private String desc = "";
    private String msgCategory;


    public String getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(String msgCategory) {
        this.msgCategory = msgCategory;
    }

    public HashMap<String, String> getMmap() {
        return mmap;
    }

    public void setMmap(HashMap<String, String> mmap) {
        this.mmap = mmap;
    }

    // mapping from value to desc
    private HashMap<String, String> mmap = new HashMap<String, String>();

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

    public void addMmap(String value, String desc) {
        this.mmap.put(value, desc);
    }

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
        this.msgCategory = field.getMsgCategory();
        this.mmap = field.getMmap();
    }

    public void updateDesc() {
        if (mmap.containsKey(this.value)) {
            this.desc = mmap.get(this.value);
        }
    }


}

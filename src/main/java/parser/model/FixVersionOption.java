package parser.model;

public class FixVersionOption {

    private String version;
    private String desc;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FixVersionOption(String version, String desc) {
        this.version = version;
        this.desc = desc;
    }
}

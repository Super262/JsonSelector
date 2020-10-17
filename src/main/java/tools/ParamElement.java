package tools;
public class ParamElement {
    private final ParamType type;
    private String name = null;
    private int startIndex = 0, endIndex = Integer.MAX_VALUE;
    private Object value = null;
    private final boolean forAllName;

    public ParamElement(ParamType type) {
        this.type = type;
        forAllName = true;
    }

    public ParamElement(ParamType type, String name) {
        this.type = type;
        this.name = name;
        forAllName = false;
    }

    public ParamElement(ParamType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
        forAllName = false;
    }

    public ParamElement(ParamType type, String name, int startIndex) {
        this.type = type;
        this.name = name;
        this.startIndex = startIndex;
        forAllName = false;
    }

    public ParamElement(ParamType type, String name, int startIndex, int endIndex) {
        this.type = type;
        this.name = name;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        forAllName = false;
    }

    public ParamType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public Object getValue() {
        return value;
    }

    public boolean isForAllName(){
        return forAllName;
    }
}

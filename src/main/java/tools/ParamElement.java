package tools;
public class ParamElement {

    @SuppressWarnings("unused")
    private final ParamType type;

    @SuppressWarnings("unused")
    private String name = null;

    @SuppressWarnings("unused")
    private int startIndex = 0, endIndex = Integer.MAX_VALUE;

    @SuppressWarnings("unused")
    private Object value = null;

    @SuppressWarnings("unused")
    private final boolean forAllName;

    @SuppressWarnings("unused")
    public ParamElement(ParamType type) {
        this.type = type;
        forAllName = true;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String name) {
        this.type = type;
        this.name = name;
        forAllName = false;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
        forAllName = false;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String name, int startIndex) {
        this.type = type;
        this.name = name;
        this.startIndex = startIndex;
        forAllName = false;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String name, int startIndex, int endIndex) {
        this.type = type;
        this.name = name;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        forAllName = false;
    }

    @SuppressWarnings("unused")
    public ParamType getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public int getStartIndex() {
        return startIndex;
    }

    @SuppressWarnings("unused")
    public int getEndIndex() {
        return endIndex;
    }

    @SuppressWarnings("unused")
    public Object getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    public boolean isForAllName(){
        return forAllName;
    }
}

package tools;

import com.google.gson.JsonPrimitive;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class ParamElement {

    @SuppressWarnings("unused")
    private final ParamType type;

    @SuppressWarnings("unused")
    private String key = null;

    @SuppressWarnings("unused")
    private int startIndex = 0, endIndex = Integer.MAX_VALUE;

    @SuppressWarnings("unused")
    private JsonPrimitive value = null;

    @SuppressWarnings("unused")
    private final boolean forAllKey, forAllValue;

    @SuppressWarnings("unused")
    public ParamElement(ParamType type) {
        this.type = type;
        forAllKey = true;
        forAllValue = true;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String key) {
        this.type = type;
        this.key = key;
        forAllKey = false;
        forAllValue = true;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String key, Object value) {
        this.type = type;
        this.key = key;
        forAllKey = false;
        forAllValue = false;
        if(value instanceof Boolean){
            this.value = new JsonPrimitive((Boolean) value);
        }
        if(value instanceof Number){
            this.value = new JsonPrimitive((Number) value);
        }
        if(value instanceof String){
            this.value = new JsonPrimitive((String) value);
        }
        if(value instanceof Character){
            this.value = new JsonPrimitive((Character) value);
        }
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String key, int startIndex) {
        this.type = type;
        this.key = key;
        this.startIndex = startIndex;
        forAllKey = false;
        forAllValue = true;
    }

    @SuppressWarnings("unused")
    public ParamElement(ParamType type, String key, int startIndex, int endIndex) {
        this.type = type;
        this.key = key;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        forAllKey = false;
        forAllValue = true;
    }

    @SuppressWarnings("unused")
    public ParamType getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getKey() {
        return key;
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
    public boolean isForAllKey(){
        return forAllKey;
    }

    @SuppressWarnings("unused")
    public boolean isForAllValue(){
        return forAllValue;
    }
}

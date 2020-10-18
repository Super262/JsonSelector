import com.google.gson.*;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class JsonSelector {

    private final SearchTree jsonTree;

    public JsonSelector(String rawJsonStr) {
        jsonTree = new SearchTree(rawJsonStr);
    }

    public JsonSelector addObjectSign(){
        jsonTree.addParam(new ParamElement(ParamType.OBJECT));
        return this;
    }

    public JsonSelector addObjectSign(String key){
        jsonTree.addParam(new ParamElement(ParamType.OBJECT, key));
        return this;
    }

    public JsonSelector addArraySign(){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY));
        return this;
    }

    public JsonSelector addArraySign(int start){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY, start));
        return this;
    }

    public JsonSelector addArraySign(int start, int end){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY, start, end));
        return this;
    }

    public JsonSelector addArraySign(String key){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY, key));
        return this;
    }

    public JsonSelector addArraySign(String key, int start){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY, key, start));
        return this;
    }

    public JsonSelector addArraySign(String key, int start, int end){
        jsonTree.addParam(new ParamElement(ParamType.ARRAY, key, start, end));
        return this;
    }

    public JsonSelector addPrimitiveSign(){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE));
        return this;
    }

    public JsonSelector addPrimitiveSign(String key){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE, key));
        return this;
    }

    public JsonSelector addIntegerSign(Integer i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,i));
        return this;
    }

    public JsonSelector addIntegerSign(String key, Integer i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,key,i));
        return this;
    }

    public JsonSelector addBooleanSign(Boolean i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,i));
        return this;
    }

    public JsonSelector addBooleanSign(String key, Boolean i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,key,i));
        return this;
    }

    public JsonSelector addStringSign(String i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,i));
        return this;
    }

    public JsonSelector addStringSign(String key, String i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,key,i));
        return this;
    }

    public JsonSelector addCharacterSign(Character i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,i));
        return this;
    }

    public JsonSelector addCharacterSign(String key, Character i){
        jsonTree.addParam(new ParamElement(ParamType.PRIMITIVE,key,i));
        return this;
    }

    public  JsonSelector setPrevSignAsTarget(){
        if(jsonTree.getParamsSize() > 0){
            jsonTree.setTarget(jsonTree.getParamsSize() - 1);
        }
        return this;
    }

    public ArrayList<String> getSelectedJsonString(){
        final ArrayList<String> result = new ArrayList<>();
        if(jsonTree.getParamsSize() != 0 && jsonTree.getTarget() != -1){
            for(JsonElement ele : jsonTree.query()){
                result.add(ele.toString());
            }
        }
        return result;
    }

    public ArrayList<JsonElement> getSelectedJsonElement(){
        return jsonTree.query();
    }


    private static class SearchTree {

        private final JsonElement jsonTree;

        private final ArrayList<ParamElement> paramsForSearch;

        private int target = -1;

        @SuppressWarnings("unused")
        private SearchTree(String rawJsonStr) {
            final Gson mainGson = new Gson();
            jsonTree = mainGson.fromJson(rawJsonStr, JsonElement.class);
            paramsForSearch = new ArrayList<>();
        }

        @SuppressWarnings("unused")
        private void addParam(ParamElement param){
            paramsForSearch.add(param);
        }

        private int getParamsSize(){
            return paramsForSearch.size();
        }

        @SuppressWarnings("unused")
        private SearchTree removeAllParams(){
            paramsForSearch.clear();
            return this;
        }

        @SuppressWarnings("unused")
        private void setTarget(int target){
            this.target = target;
        }

        @SuppressWarnings("unused")
        private int getTarget(){
            return target;
        }


        @SuppressWarnings("unused")
        private ArrayList<JsonElement> query() {
            return searchRecursively(null,jsonTree,0, target);
        }

        @SuppressWarnings("unused")
        private ArrayList<JsonElement> searchRecursively(String firstKey, JsonElement firstNode, int firstIndex, int targetIndex) {
            final ArrayList<JsonElement> result = new ArrayList<>();
            if(firstIndex < paramsForSearch.size()){
                ParamElement firstParam = paramsForSearch.get(firstIndex);
                if (isSameType(firstNode, firstParam) && (firstParam.isForAllKey() || firstParam.getKey().equals(firstKey))) {
                    if (firstNode.isJsonObject()) {
                        final JsonObject jsonObject = firstNode.getAsJsonObject();
                        if (firstIndex < targetIndex) {
                            for (String nextkey : jsonObject.keySet()) {
                                ArrayList<JsonElement> temp = searchRecursively(nextkey, jsonObject.get(nextkey), firstIndex + 1, targetIndex);
                                result.addAll(temp);
                            }
                        } else if (firstIndex == targetIndex){
                            if(targetIndex == paramsForSearch.size() - 1){
                                result.add(jsonObject);
                            }
                            else{
                                final ArrayList<JsonElement> validResult = new ArrayList<>();
                                for (String nextkey : jsonObject.keySet()) {
                                    ArrayList<JsonElement> temp = searchRecursively(nextkey, jsonObject.get(nextkey), firstIndex + 1, paramsForSearch.size() - 1);
                                    validResult.addAll(temp);
                                }
                                if(!validResult.isEmpty()){
                                    result.add(jsonObject);
                                }
                            }
                        }

                    } else if (firstNode.isJsonArray()) {
                        final JsonArray tempJsonArray = firstNode.getAsJsonArray();
                        final JsonArray jsonArray = new JsonArray();
                        int arrayLen = tempJsonArray.size();
                        arrayLen = firstParam.getEndIndex() > arrayLen ? arrayLen : firstParam.getEndIndex();
                        for (int i = firstParam.getStartIndex(); i < arrayLen; ++i) {
                            jsonArray.add(tempJsonArray.get(i));
                        }
                        if (firstIndex < targetIndex) {
                            for (JsonElement ele : jsonArray) {
                                result.addAll(searchRecursively(null, ele, firstIndex + 1, targetIndex));
                            }
                        }
                        else if (firstIndex == targetIndex){
                            if(targetIndex == paramsForSearch.size() - 1){
                                result.add(jsonArray);
                            }
                            else{
                                final ArrayList<JsonElement> validResult = new ArrayList<>();
                                for (JsonElement ele : jsonArray) {
                                    validResult.addAll(searchRecursively(null, ele, targetIndex + 1, paramsForSearch.size() - 1));
                                }
                                if(!validResult.isEmpty()){
                                    result.add(jsonArray);
                                }
                            }
                        }

                    } else if (firstNode.isJsonPrimitive()) {
                        final JsonPrimitive jsonPrimitive = firstNode.getAsJsonPrimitive();
                        if (firstIndex == paramsForSearch.size() - 1 && (firstParam.isForAllValue() || jsonPrimitive.equals(firstParam.getValue()))) {
                            result.add(jsonPrimitive);
                        }

                    } else {
                        final JsonNull jsonNull = firstNode.getAsJsonNull();
                        if (firstIndex == paramsForSearch.size() - 1) {
                            result.add(jsonNull);
                        }
                    }

                }
            }
            return result;
        }

        @SuppressWarnings("unused")
        private boolean isSameType(JsonElement node, ParamElement element) {
            if (node == null || element == null) {
                return false;
            }
            if (node.isJsonObject() && element.getType() == ParamType.OBJECT) {
                return true;
            }
            if (node.isJsonArray() && element.getType() == ParamType.ARRAY) {
                return true;
            }
            if (node.isJsonPrimitive() && element.getType() == ParamType.PRIMITIVE) {
                return true;
            }
            return node.isJsonNull() && element.getType() == ParamType.NULL;
        }
    }

    private static class ParamElement {

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


        // For JsonElements without JsonPrimitives
        @SuppressWarnings("unused")
        private ParamElement(ParamType type) {
            this.type = type;
            forAllKey = true;
            forAllValue = true;
        }

        @SuppressWarnings("unused")
        private ParamElement(ParamType type, String key) {
            this.type = type;
            this.key = key;
            forAllKey = false;
            forAllValue = true;
        }

        // For JsonArray
        @SuppressWarnings("unused")
        private ParamElement(ParamType type, int startIndex) {
            this.type = type;
            this.startIndex = startIndex;
            forAllKey = true;
            forAllValue = true;
        }

        @SuppressWarnings("unused")
        private ParamElement(ParamType type, int startIndex, int endIndex) {
            this.type = type;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            forAllKey = true;
            forAllValue = true;
        }

        @SuppressWarnings("unused")
        private ParamElement(ParamType type, String key, int startIndex) {
            this.type = type;
            this.key = key;
            this.startIndex = startIndex;
            forAllKey = false;
            forAllValue = true;
        }

        @SuppressWarnings("unused")
        private ParamElement(ParamType type, String key, int startIndex, int endIndex) {
            this.type = type;
            this.key = key;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            forAllKey = false;
            forAllValue = true;
        }

        // For JsonPrimitive
        @SuppressWarnings("unused")
        private ParamElement(ParamType type, Object value) {
            this.type = type;
            forAllKey = true;
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
        private ParamElement(ParamType type, String key, Object value) {
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
        private ParamType getType() {
            return type;
        }

        @SuppressWarnings("unused")
        private String getKey() {
            return key;
        }

        @SuppressWarnings("unused")
        private int getStartIndex() {
            return startIndex;
        }

        @SuppressWarnings("unused")
        private int getEndIndex() {
            return endIndex;
        }

        @SuppressWarnings("unused")
        private Object getValue() {
            return value;
        }

        @SuppressWarnings("unused")
        private boolean isForAllKey(){
            return forAllKey;
        }

        @SuppressWarnings("unused")
        private boolean isForAllValue(){
            return forAllValue;
        }
    }

    private enum ParamType {
        ARRAY,
        PRIMITIVE,
        OBJECT,
        NULL
    }
}

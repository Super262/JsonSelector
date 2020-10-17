package tools;

import com.google.gson.*;

import java.util.ArrayList;

public class JsonTree {

    @SuppressWarnings("unused")
    private final JsonElement rootNode;

    @SuppressWarnings("unused")
    private final ArrayList<ParamElement> params;

    @SuppressWarnings("unused")
    public JsonTree(String rawJsonStr) {
        final Gson mainGson = new Gson();
        rootNode = mainGson.fromJson(rawJsonStr, JsonElement.class);
        params = new ArrayList<>();

        //
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.OBJECT, "q2"));
        params.add(new ParamElement(ParamType.ARRAY));
    }


    @SuppressWarnings("unused")
    public ArrayList<String> query() {
        return searchRecursively(null, rootNode, 0, 1);
    }

    @SuppressWarnings("unused")
    private ArrayList<String> searchRecursively(String realKey, JsonElement startNode, int searchStartIndex, int targetIndex) {
        ParamElement startParam = params.get(searchStartIndex);
        final ArrayList<String> result = new ArrayList<>();
        if (isSameType(startNode, startParam) && (startParam.isForAllKey() || startParam.getKey().equals(realKey))) {

            if (startNode.isJsonObject()) {
                final JsonObject startObject = startNode.getAsJsonObject();
                if (searchStartIndex < params.size() - 1) {
                    for (String nextkey : startObject.keySet()) {
                        result.addAll(searchRecursively(nextkey, startObject.get(nextkey), searchStartIndex + 1, targetIndex));
                    }
                } else {
                    result.add(startObject.toString());
                }

            } else if (startNode.isJsonArray()) {
                final JsonArray tempStartArray = startNode.getAsJsonArray();
                final JsonArray startArray = new JsonArray();
                int arrayLen = tempStartArray.size();
                arrayLen = startParam.getEndIndex() > arrayLen ? arrayLen : startParam.getEndIndex();
                for(int i = startParam.getStartIndex(); i < arrayLen; ++i){
                    startArray.add(tempStartArray.get(i));
                }
                if(searchStartIndex < params.size() - 1){
                    for(JsonElement ele : startArray){
                        result.addAll(searchRecursively(null, ele, searchStartIndex + 1, targetIndex));
                    }
                }
                else{
                    result.add(startArray.toString());
                }

            } else if (startNode.isJsonPrimitive()) {
                final JsonPrimitive startPrimitive = startNode.getAsJsonPrimitive();
                if(searchStartIndex == params.size() - 1 && (startParam.isForAllValue() || startPrimitive.equals(startParam.getValue()))){
                    result.add(startPrimitive.toString());
                }

            } else {
                final JsonNull startNull = startNode.getAsJsonNull();
                if(searchStartIndex == params.size() - 1){
                    result.add(startNull.toString());
                }
            }

        }
        return result;
    }


    private boolean isSameType(JsonElement node, ParamElement element){
        if(node == null || element == null){
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

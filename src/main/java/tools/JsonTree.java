package tools;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class JsonTree {

    @SuppressWarnings("unused")
    private final JsonObject rootNode;

    @SuppressWarnings("unused")
    private final ArrayList<ParamElement> params;

    @SuppressWarnings("unused")
    public JsonTree(String rawJsonStr) {
        final Gson mainGson = new Gson();
        rootNode = mainGson.toJsonTree(rawJsonStr).getAsJsonObject();
        params = new ArrayList<>();
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.OBJECT, "quiz"));
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.OBJECT));
        params.add(new ParamElement(ParamType.ARRAY, "options"));
    }


    @SuppressWarnings("unused")
    public ArrayList<String> query() throws Exception {
        return searchRecursively(rootNode, 0, 4);
    }

    @SuppressWarnings("unused")
    private ArrayList<String> searchRecursively(JsonElement startNode, int searchStartIndex, int targetIndex) {
        ParamElement rootParam = params.get(searchStartIndex);
        if (startNode.isJsonObject() && rootParam.getType() != ParamType.OBJECT) {
            return new ArrayList<>();
        }
        if (startNode.isJsonArray() && rootParam.getType() != ParamType.ARRAY) {
            return new ArrayList<>();
        }
        if (startNode.isJsonPrimitive() && rootParam.getType() != ParamType.PRIMITIVE) {
            return new ArrayList<>();
        }
        if (startNode.isJsonNull() && rootParam.getType() != ParamType.NULL) {
            return new ArrayList<>();
        }
        if (searchStartIndex >= targetIndex) {
            final ArrayList<String> result = new ArrayList<>();
            result.add(rootNode.toString());
            return result;
        } else {
            if (rootParam.isForAllName()) {
                final ArrayList<String> result = new ArrayList<>();
                if(startNode.isJsonObject()){
                    final JsonObject startObject = startNode.getAsJsonObject();
                    for(String key : startObject.keySet()){
                        final ArrayList<String> tempResult = searchRecursively(startObject.get(key), searchStartIndex + 1, targetIndex);
                        if(!tempResult.isEmpty()){
                            result.addAll(tempResult);
                        }
                    }
                }
                else if(startNode.isJsonArray()){
                    final JsonArray startArray = startNode.getAsJsonArray();
                    for(JsonElement ele : startArray){
                        final ArrayList<String> tempResult = searchRecursively(ele, searchStartIndex + 1, targetIndex);
                        if(!tempResult.isEmpty()){
                            result.addAll(tempResult);
                        }
                    }
                }

                // for null and primitive, return empty array if target doesn't equal with start.

                return result;

            } else {
                if (startNode.isJsonObject()) {
                    JsonElement nextNode = startNode.getAsJsonObject().get(rootParam.getName());
                    if (nextNode == null) {
                        return new ArrayList<>();
                    } else {
                        return searchRecursively(nextNode, searchStartIndex + 1, targetIndex);
                    }
                } else {
                    return new ArrayList<>();
                }
            }
        }
    }
}

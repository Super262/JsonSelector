import tools.Analyzer;
import tools.JsonTree;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/Users/fengwei/IdeaProjects/JsonAnalyzer/example.json"));
        String content = new String(encoded, StandardCharsets.UTF_8);
        JsonTree tree = new JsonTree(content);
        ArrayList<String> res = tree.query();
        for(String item : res){
            System.out.println(item);
        }
    }
}

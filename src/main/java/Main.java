import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/Users/fengwei/IdeaProjects/JsonAnalyzer/example.json"));
        String content = new String(encoded, StandardCharsets.UTF_8);
        JsonSelector jsonSelector = new JsonSelector(content).addObjectSign().addObjectSign().addObjectSign().setPrevSignAsTarget().addObjectSign();
        for(String s : jsonSelector.getSelectedJsonString()){
            System.out.println(s);
        }
    }
}

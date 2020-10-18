import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/Users/fengwei/IdeaProjects/JsonSelector/data.json"));
        String content = new String(encoded, StandardCharsets.UTF_8);

        Selector selector = new Selector(content).addObjectSign().addArraySign().addObjectSign().addStringSign("title").setThisSignAsTarget();
        int size = 0;
        for(String s : selector.getSelectedJsonString()){
            System.out.println(s);
            ++size;
        }
        System.out.println("Size: " + size);
    }
}
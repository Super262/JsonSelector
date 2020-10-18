import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/Users/fengwei/IdeaProjects/JsonSelector/example.json"));
        String content = new String(encoded, StandardCharsets.UTF_8);
        System.out.println("Path: *(Object) -> *(Object) -> *(Object, TARGET) -> *(Object) -> *(Null)");
        Selector selector = new Selector(content).addObjectSign().addObjectSign().addObjectSign().setPrevSignAsTarget().addObjectSign().addNullSign();
        for(String s : selector.getSelectedJsonString()){
            System.out.println(s);
        }
    }
}
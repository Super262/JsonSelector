import tools.Analyzer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/Users/fengwei/IdeaProjects/JsonAnalyzer/example.json"));
        String content = new String(encoded, StandardCharsets.UTF_8);
        Analyzer analyzer = new Analyzer(content);
        System.out.println(analyzer.getJsonData());
    }
}

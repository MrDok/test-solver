package excell;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khekk on 06.12.16
 */
public class HtmlHelper {

    public Map<String, String> getQuestions(String filepath) throws IOException {
        File file = new File(filepath);

        return getQuestions(file);
    }

    public Map<String, String> getQuestions(File file) throws IOException {
        Elements elements = Jsoup.parse(file,
                Charset.defaultCharset().name())
                .select("div.question-result");
        Map<String, String> map = new HashMap<>();
        elements.forEach(element -> {
            String sb = element.select("div.question-text").first().ownText() + "\n\n" +
                    Jsoup.parse(element.select("code.java").html()).text().replace("{", "{\n").replace(";", ";\n").replace("}", "}\n");
            map.put(sb, element.select("p.correct").text());
        });
        return map;
    }
}

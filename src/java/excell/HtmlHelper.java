package excell;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * Created by khekk on 06.12.16
 */
class HtmlHelper {

    public Map<String, String> getQuestions(String filepath) throws IOException {
        File file = new File(filepath);

        return getQuestions(file);
    }

    Map<String, String> getQuestions(File file) throws IOException {
        Elements elements = Jsoup.parse(file,
                Charset.defaultCharset().name())
                .select("div.question-result");
        Map<String, String> map = new HashMap<>();
        elements.forEach(element -> {
            String question = element.select("div.question-text").first().ownText() + "\n\n" +
                    Jsoup.parse(element.select("code.java").html()).text().replace("{", "{\n").replace(";", ";\n").replace("}", "}\n");
            String answers = element.select("p.correct").stream()
                    .map(Element::ownText)
                    .collect(joining("\nОтвет: ", "Ответ: ", "."));
            map.put(question, answers);
        });
        return map;
    }
}

package excell;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.joining;

/**
 * Created by khekk on 06.12.16
 */
class HtmlHelper {

    Set<Question> getQuestions(File file) {
        Elements elements;
        try {
            elements = Jsoup.parse(file,
                    Charset.defaultCharset().name())
                    .select("div.question-result");
        } catch (IOException e) {
            return null;
        }
        Set<Question> result = new HashSet<>();
        elements.forEach(element -> {
            String question = element.select("div.question-text").first().ownText();
            String code = element.select("code.java").text().replace("{", "{\n").replace(";", ";\n").replace("}", "}\n").replace("\u00a0"," ");
            String answers = element.select("p.correct").stream()
                    .map(Element::ownText)
                    .collect(joining("\nОтвет: ", "Ответ: ", "."));
            result.add(new Question(question, code, answers));
        });
        return result;
    }
}

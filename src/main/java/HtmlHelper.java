import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.stream.Collectors.joining;

/**
 * Created by khekk on 06.12.16
 */
class HtmlHelper {

    Set<Question> getQuestions(File file) {

        try {
            return Jsoup.parse(file,
                    defaultCharset().name())
                    .select("div.question-result").stream()
                    .map(element -> new Question(
                            element.select("div.question-text").first().ownText(),
                            element.select("code").text().replace("<code class=\"java\">", "").replace("</code>", ""),
                            element.select("p.correct").stream()
                                    .map(Element::ownText)
                                    .collect(joining(";\nОтвет: ", "Ответ: ", ".")), allAnswers, answerDescription))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}

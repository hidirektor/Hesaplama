package me.t3sl4.hydraulic.utils.general.Highlighter;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyleSpan;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlSyntaxHighlighter {

    // Anahtarlar için regex (YAML anahtarları genellikle ":" ile biter)
    private static final String KEY_PATTERN = "^\\s*([a-zA-Z0-9_\\-]+)(?=\\s*:)";

    // Stringler için regex (Çift tırnakla tanımlanan stringler)
    private static final String STRING_PATTERN = "\"([^\"]*)\"";

    // Değerler için regex (YAML'deki ":" işaretinden sonra gelen değerler)
    private static final String VALUE_PATTERN = "(?<=:\\s*)([^\\n]+)";

    public static StyleSpans<Collection<String>> getStyleSpans(String text) {
        Matcher matcher = Pattern.compile(KEY_PATTERN).matcher(text);
        StyleSpansBuilder<Collection<String>> styleSpansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            // Calculate the length of the span and add it with the style
            styleSpansBuilder.add(new StyleSpan<>(Collections.singleton("yaml-key"), matcher.end() - matcher.start()));
        }

        matcher = Pattern.compile(STRING_PATTERN).matcher(text);
        while (matcher.find()) {
            // Calculate the length of the span and add it with the style
            styleSpansBuilder.add(new StyleSpan<>(Collections.singleton("yaml-string"), matcher.end() - matcher.start()));
        }

        matcher = Pattern.compile(VALUE_PATTERN).matcher(text);
        while (matcher.find()) {
            // Calculate the length of the span and add it with the style
            styleSpansBuilder.add(new StyleSpan<>(Collections.singleton("yaml-value"), matcher.end() - matcher.start()));
        }

        return styleSpansBuilder.create();
    }
}
package me.t3sl4.hydraulic.utils.general.Highlighter;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonSyntaxHighlighter {

    // JSON Regex Tanımları
    private static final Pattern JSON_PATTERN = Pattern.compile(
            "(?<KEYVALUE>[a-zA-Z0-9_-öşçığüÖŞÇİĞÜ]+(?:\\s*:\\s*\"[^\"]*\"|\\s*:\\s*'[^']*'|\\s*:\\s*[^\\s#]+)*)" +                            // Anahtarlar (Çift tırnak içinde)
                    "|(?<STRUCTURAL>[\\[\\]{}:,])"                          // Yapısal elemanlar (virgül, köşeli parantez, süslü parantez, iki nokta)
    );

    public static StyleSpans<Collection<String>> getStyleSpans(String text) {
        Matcher matcher = JSON_PATTERN.matcher(text);
        int lastMatchEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYVALUE") != null ? "json-key" :
                            matcher.group("STRUCTURAL") != null ? "json-brace" :
                                    null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastMatchEnd);

            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastMatchEnd = matcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastMatchEnd);

        return spansBuilder.create();
    }
}
package me.t3sl4.hydraulic.utils.general.Highlighter;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlSyntaxHighlighter {

    private static final Pattern YAML_PATTERN = Pattern.compile(
            "(?<STRUCTURAL>[\\[\\]{}:,'\\\"']|':)(?=\\s|\\b|$|[İÖÇığüşçıİĞÜÖŞÇ])" + // Yapısal elemanlar (virgül, köşeli parantez, süslü parantez, iki nokta)
            "|(?<KEYVALUE>\\s*(?=#[^\\s])|[a-zA-Z0-9öşçığüÖŞÇİĞÜ()._\\-\\/Ø\\+½]+(?:\\s*)|(?<=\\d)\\\"(?!\\s*'))" +  // Key-Value (Anahtarlar ve Değerler)
                    "|(?<COMMENT>#\\s?.*$)",  // Yorumlar
            Pattern.MULTILINE
    );

    public static StyleSpans<Collection<String>> getStyleSpans(String text) {
        Matcher matcher = YAML_PATTERN.matcher(text);
        int lastEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass =
                    matcher.group("STRUCTURAL") != null ? "yaml-structural" :
                            matcher.group("KEYVALUE") != null ? "yaml-key" :
                                    matcher.group("COMMENT") != null ? "yaml-comment" :
                                            "yaml-blank";

            assert styleClass != null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastEnd);

            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastEnd);

        return spansBuilder.create();
    }
}
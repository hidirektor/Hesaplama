package me.t3sl4.hydraulic.utils.general.Highlighter;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyleSpan;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonSyntaxHighlighter {

    // Anahtarlar için regex (JSON anahtarları çift tırnak içinde olmalı)
    private static final String KEY_PATTERN = "\"([^\"]+)\"(?=\\s*:)";

    // Stringler için regex (JSON stringleri çift tırnak içinde olabilir, kaçış karakterlerini de dikkate alır)
    private static final String STRING_PATTERN = "\"([^\"]|\\\\\")*\"";

    // Sayılar için regex (JSON sayıları, tam sayılar veya ondalıklı sayılar olabilir)
    private static final String NUMBER_PATTERN = "(\\b\\d+(\\.\\d+)?\\b)";

    public static StyleSpans<Collection<String>> getStyleSpans(String text) {
        StyleSpansBuilder<Collection<String>> styleSpansBuilder = new StyleSpansBuilder<>();

        // Add patterns to the builder using the helper method
        addPatternToStyleSpans(styleSpansBuilder, KEY_PATTERN, "json-key", text);
        addPatternToStyleSpans(styleSpansBuilder, STRING_PATTERN, "json-string", text);
        addPatternToStyleSpans(styleSpansBuilder, NUMBER_PATTERN, "json-number", text);

        return styleSpansBuilder.create();
    }

    // Helper method to add the matching pattern to the builder
    private static void addPatternToStyleSpans(StyleSpansBuilder<Collection<String>> builder, String pattern, String style, String text) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            // Create a StyleSpan with the corresponding style and length
            // Use matcher.start() and matcher.end() to define the span
            StyleSpan<Collection<String>> styleSpan = new StyleSpan<>(Collections.singleton(style), matcher.end() - matcher.start());
            // Add the StyleSpan to the builder, passing the start and length directly
            builder.add(styleSpan);
        }
    }
}

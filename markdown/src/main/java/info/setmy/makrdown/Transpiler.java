package info.setmy.makrdown;

import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Transpiler {

    private final static Transpiler transpiler = new Transpiler();

    public static Transpiler getInstance() {
        return transpiler;
    }

    public void transpile(String fileIn, String fileOut) {
        try {

            MutableDataSet options = new MutableDataSet();

            options.set(Parser.EXTENSIONS, List.of(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                AbbreviationExtension.create(),
                AutolinkExtension.create(),
                EmojiExtension.create(),
                AttributesExtension.create(),
                TypographicExtension.create(),
                GitLabExtension.create() // lisab GitHub/GitLab-stiili blockquote jne
            ));

            Parser parser = Parser.builder(options).build();
            Node document = parser.parse(Files.readString(Path.of(fileIn)));

            HtmlRenderer renderer = HtmlRenderer.builder(options).build();
            String html = renderer.render(document);

            String fullHtml = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Markdown Output</title>
                        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.11.1/build/styles/default.min.css">
                        <script src="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.11.1/build/highlight.min.js"></script>
                        <script src="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.11.1/build/languages/go.min.js"></script>
                        <script src="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.11.1/build/languages/java.min.js"></script>
                        <script>hljs.highlightAll();</script>
                    <style>
                        body { font-family: sans-serif; line-height: 1.6; margin: 2em auto; max-width: 900px; }
                        code, pre { background-color: #f4f4f4; padding: 0.2em; border-radius: 4px; }
                        table { border-collapse: collapse; width: 100%%; }
                        th, td { border: 1px solid #ccc; padding: 0.5em; text-align: left; }
                    </style>
                </head>
                <body>
                %s
                </body>
                </html>
                """.formatted(html);

            Files.writeString(Path.of(fileOut), fullHtml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

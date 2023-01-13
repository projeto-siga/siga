package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import br.gov.jfrj.siga.base.util.FreemarkerIndent;

public class ModeloMarkdown {

    public static String markdownToFreemarker(String input) {

        String mdWithCommandsInFreemarker = processCommands(input, (cmd) -> {
            String comando = cmd.get("comando").getAsString();
            if (comando == null)
                return "COMANDO_DESCONHECIDO";
            StringBuilder sb = new StringBuilder();
            sb.append("[@");
            sb.append(comando);
            for (Entry<String, JsonElement> entry : cmd.entrySet()) {
                if (entry.getKey().equals("comando"))
                    continue;
                sb.append(" ");
                sb.append(entry.getKey());
                sb.append("='");
                sb.append(entry.getValue().getAsString());
                sb.append("'");
            }
            sb.append(" /]");
            return sb.toString();
        });

        List<String> lftl = new ArrayList<>();
        String txtWithPlaceholders = FreemarkerIndent.convertFtl2Html(mdWithCommandsInFreemarker, lftl);

        StringBuilder sb = new StringBuilder();

        sb.append("[@entrevista]");
        String PREFIX_CAMPO = "[@campo ";
        for (String s : lftl) {
            if (s.startsWith(PREFIX_CAMPO)) {
                sb.append("\n  ");
                sb.append(s);
            }
        }
        sb.append("\n[/@entrevista]");

        // Change "campo" to "valor"
        for (int i = 0; i < lftl.size(); i++) {
            if (lftl.get(i).startsWith(PREFIX_CAMPO)) {
                lftl.set(i, "[@valor " + lftl.get(i).substring(PREFIX_CAMPO.length()));
            }
        }

        String ftlInMarkdown = FreemarkerIndent.convertHtml2Ftl(txtWithPlaceholders, lftl);

        String ftlInHtml = markdownToHtml(ftlInMarkdown);

        sb.append("\n\n[@documento]\n");
        sb.append(ftlInHtml);
        sb.append("[/@documento]");

        return sb.toString();
    }

    private static String markdownToHtml(String input) {
        // Convert markdown to html
        //
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        // options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).indentSize(2).build();
        // You can re-use parser and renderer instances
        Node document = parser.parse(input);
        String html = renderer.render(document);
        return html;
    }

    private static JsonObject interpretCommand(String command) {
        Gson gson = new Gson();
        Pattern pattern = Pattern.compile("\\{(\\w+)(?: ([^\\}]+))\\}|\\{([a-zA-Z0-9\\$_\\[\\]]+)\\}");
        Matcher m = pattern.matcher(command);
        if (m.find()) {
            MatchResult matches = m.toMatchResult();
            if (matches.group(1) != null) {
                String ss = "{comando:\"" + matches.group(1) + "\", " +
                        matches.group(2) + '}';
                ss = ss.replaceAll("([{, ])([a-z]+):\"", "$1\"$2\":\"");
                return gson.fromJson(ss, JsonObject.class);
            }

            // Variables
            if (matches.group(3) != null) {
                JsonObject obj = new JsonObject();
                obj.addProperty("comando", "campo");
                obj.addProperty("var", matches.group(3));
                return obj;
            }

        } else {
            if (command.charAt(1) == '"') {
                return gson.fromJson(command, JsonObject.class);
            } else {
                JsonObject obj = new JsonObject();
                obj.addProperty("comando", "escrever");
                obj.addProperty("valor", command.substring(2, command.length() - 2));
                return obj;
            }
        }
        return null;
    }

    public interface ProcessCommandFunction {
        String modifyCommand(JsonObject cmd);
    }

    private static Integer readCommand(String block, int startIndex) {
        int currPos = startIndex;
        int openBrackets = 0;
        boolean stillSearching = true;
        char waitForChar = '\0';

        while (stillSearching && currPos <= block.length()) {
            char currChar = block.charAt(currPos);

            if (waitForChar == '\0') {
                switch (currChar) {
                    case '{':
                        openBrackets++;
                        break;
                    case '}':
                        openBrackets--;
                        break;
                    case '"':
                    case '\'':
                        waitForChar = currChar;
                        break;
                }
            } else {
                if (currChar == waitForChar) {
                    if (waitForChar == '"' || waitForChar == '\'') {
                        if (block.charAt(currPos - 1) != '\\')
                            waitForChar = '\0';
                    } else {
                        waitForChar = '\0';
                    }
                } else if (currChar == '*') {
                    if (block.charAt(currPos + 1) == '/')
                        waitForChar = '\0';
                }
            }

            currPos++;
            if (openBrackets == 0) {
                stillSearching = false;
                return currPos;
            }
        }
        return null;
    }

    // Replace everything inside {} by the return of a call to func(string
    // found)
    //
    private static String processCommands(String template, ProcessCommandFunction func) {
        int pointer = 0;
        String s = "";
        int start;

        while (-1 != (start = template.indexOf("{", pointer))) {
            s += template.substring(pointer, start);
            Integer end = readCommand(template, start);
            if (end == null) {
                break;
            }
            String command = template.substring(start, end);
            JsonObject cmd = interpretCommand(command);
            String r = func.modifyCommand(cmd);
            if (r != null)
                s += r;

            pointer = end;
        }
        s += template.substring(pointer, template.length());
        return s;
    }
}

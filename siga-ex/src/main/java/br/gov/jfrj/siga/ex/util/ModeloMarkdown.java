package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import br.gov.jfrj.siga.base.util.FreemarkerIndent;
import br.gov.jfrj.siga.base.util.Utils;

public class ModeloMarkdown {

    public static String markdownToFreemarker(String input) {

        String mdWithCommandsInFreemarker = processCommands(input, (cmd) -> {
            String comando = cmd.command;
            if (comando == null)
                return "COMANDO_DESCONHECIDO";
            StringBuilder sb = new StringBuilder();
            sb.append("[@");
            sb.append(comando);
            sb.append(" ");
            sb.append(cmd.params);
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

    private static class Command {
        String command;
        String expr;
        String var;
        String params;

        public Command(String s) {
            Pattern pattern = Pattern.compile(
                    "\\{(?<command>(?>campo |escrever |se |/se))?(?<expr>.*?)(?<params>\\s*[a-zA-Z0-9_]+\\s*=\\s*[^=].*)?\\}$");
            Matcher m = pattern.matcher(s);
            if (m.find()) {
                command = Utils.sorn(m.group("command"));
                expr = Utils.sorn(m.group("expr"));
                params = Utils.sorn(m.group("params"));
            }

            if (command == null && expr != null && params == null) {
                if (expr.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                    // A single variable identifier should be mapped to a "campo" command
                    command = "campo";
                    var = expr;
                    params = "var='" + this.var + "'";
                } else {
                    // A single expression should be mapped to a "escrever" command
                    command = "escrever";
                    params = "valor=" + expr;
                }
            }

        }

        public Command(String command, String var, String params) {
            this.command = command;
            this.var = var;
            this.params = params;
        }

        public Command(String command, String params) {
            this.command = command;
            this.var = extractVar(params);
            this.params = params;
        }

        private String extractExpression(String s) {
            Pattern pattern = Pattern.compile("^(\\s*+.*?\\s*)(?>$|\\s+[a-zA-Z0-9_]+\\s*=\\s*[^=])");
            Matcher m = pattern.matcher(s);
            if (m.find()) {
                MatchResult matches = m.toMatchResult();
                if (matches.group(1) != null) {
                    return matches.group(1);
                }
            }
            return null;
        }

        private String extractVar(String s) {
            Pattern pattern = Pattern.compile("(?>^|\\s+)var\\s*=\\s*(.*?)(?>$|\\s+[a-zA-Z0-9_]+\\s*=\\s*[^=])");
            Matcher m = pattern.matcher(s);
            if (m.find()) {
                MatchResult matches = m.toMatchResult();
                if (matches.group(1) != null) {
                    return matches.group(1);
                }
            }
            return null;
        }
    }

    private static Command interpretCommand(String command) {
        Gson gson = new Gson();
        Pattern pattern = Pattern.compile("\\{(\\w+)(?: ([^\\}]+))\\}|\\{([a-zA-Z0-9\\$_\\[\\]]+)\\}");
        Matcher m = pattern.matcher(command);
        if (m.find()) {
            MatchResult matches = m.toMatchResult();
            if (matches.group(1) != null) {
                Command cmd = new Command(matches.group(1), matches.group(2));
//                String ss = "{comando:\"" + matches.group(1) + "\", " +
//                        matches.group(2) + '}';
//                ss = ss.replaceAll("([{, ])([a-z]+):\"", "$1\"$2\":\"");
//                return gson.fromJson(ss, JsonObject.class);
                return cmd;
            }

            // Variables
            if (matches.group(3) != null) {
//                JsonObject obj = new JsonObject();
//                obj.addProperty("comando", "campo");
//                obj.addProperty("var", matches.group(3));
//                return obj;
                Command cmd = new Command("campo", matches.group(3), "var='" + matches.group(3) + "'");
                return cmd;
            }

        } else {
            Command cmd = new Command("escrever", null, "valor='" + command.substring(2, command.length() - 2) + "'");
        }
        return null;
    }

    public interface ProcessCommandFunction {
        String modifyCommand(Command cmd);
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
            Command cmd = interpretCommand(command);
            String r = func.modifyCommand(cmd);
            if (r != null)
                s += r;

            pointer = end;
        }
        s += template.substring(pointer, template.length());
        return s;
    }
}

package br.gov.jfrj.siga.util;

public class FreemarkerMarker {
	private String template;
	private int pos = 0;
	private int len = 0;

	public FreemarkerMarker(String template) {
		super();
		this.template = template;
		this.len = this.template.length();
	}

	private boolean match(char c, int i) {
		if (pos + i >= len)
			return false;
		return template.charAt(pos + i) == c;
	}

	public String run() {
		StringBuffer sb = new StringBuffer();
		int state = 0;
		while (pos < len) {
			if (match('[', 0)) {
				if (match('#', 1) && match('-', 2) && match('-', 3)) {
					int start = pos;
					skipComment();
					sb.append("{{fm}}");
					sb.append(template.substring(start, pos));
					sb.append("{{/fm}}");
					continue;
				}
				if ((match('#', 1) || match('@', 1))
						|| (match('/', 1) && (match('#', 2) || match('@', 2)))) {
					int start = pos;
					skipCommand();
					sb.append("{{fm}}");
					sb.append(template.substring(start, pos));
					sb.append("{{/fm}}");
					continue;
				}
			}
			sb.append(template.charAt(pos));
			pos++;
		}
		return sb.toString();
	}

	private void skipComment() {
		while (pos < len) {
			if (match('-', 0) && match('-', 1) && match(']', 2)) {
				pos += 3;
				return;
			}
			pos++;
		}
	}

	private void skipCommand() {
		pos++;
		while (pos < len) {
			if (match('"', 0)) {
				skipStringDoubleQuote();
			}

			if (match('\'', 0)) {
				skipStringSingleQuote();
			}

			if (match('[', 0)) {
				skipBrackets();
			}

			if (match(']', 0)) {
				pos += 1;
				return;
			}
			if (match('/', 0) && match(']', 1)) {
				pos += 2;
				return;
			}
			pos++;
		}
	}

	private void skipStringDoubleQuote() {
		pos++;
		while (pos < len) {
			if (match('"', 0)) {
				pos += 1;
				return;
			}
			if (match('\\', 0) && match('"', 1)) {
				pos += 2;
			}
			pos++;
		}
	}

	private void skipStringSingleQuote() {
		pos++;
		while (pos < len) {
			if (match('\'', 0)) {
				pos += 1;
				return;
			}
			if (match('\\', 0) && match('\'', 1)) {
				pos += 2;
			}
			pos++;
		}
	}

	private void skipBrackets() {
		pos++;
		while (pos < len) {
			if (match('[', 0)) {
				skipBrackets();
			}

			if (match(']', 0)) {
				pos += 1;
				return;
			}
			pos++;
		}
	}

}

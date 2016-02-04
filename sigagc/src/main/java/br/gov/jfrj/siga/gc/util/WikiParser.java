package br.gov.jfrj.siga.gc.util;

/*
 * Copyright 2007-2009 Yaroslav Stavnichiy, yarosla@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Latest version of this software can be obtained from:
 *
 *     http://t4-wiki-parser.googlecode.com/
 *
 * If you make use of this code, I'd appreciate hearing about it.
 * Comments, suggestions, and bug reports welcome: yarosla@gmail.com
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * WikiParser.renderXHTML() is the main method of this class. It takes wiki-text
 * and returns XHTML.
 * 
 * WikiParser's behavior can be customized by overriding appendXxx() methods,
 * which should make integration of this class into any wiki/blog/forum software
 * easy and painless.
 * 
 * @author Yaroslav Stavnichiy (yarosla@gmail.com)
 * 
 */
public class WikiParser {

	private int wikiLength;
	private char wikiChars[];
	protected StringBuilder sb = new StringBuilder();
	protected StringBuilder toc = new StringBuilder();
	protected int tocLevel = 0;
	private HashSet<String> tocAnchorIds = new HashSet<String>();
	private String wikiText;
	private int pos = 0;
	private int listLevel = -1;
	private static final int MAX_LIST_LEVELS = 100;
	private char listLevels[] = new char[MAX_LIST_LEVELS + 1]; // max number of
																// levels
																// allowed
	private boolean blockquoteBR = false;
	private boolean inTable = false;
	private int mediawikiTableLevel = 0;

	protected int HEADING_LEVEL_SHIFT = 1; // make =h2, ==h3, ...
	protected String HEADING_ID_PREFIX = null;

	private static enum ContextType {
		PARAGRAPH, LIST_ITEM, TABLE_CELL, HEADER, NOWIKI_BLOCK
	};

	private static final String[] ESCAPED_INLINE_SEQUENCES = { "{{{", "{{",
			"}}}", "**", "//", "__", "##", "\\\\", "[[", "<<<", "~", "--", "|" };

	private static final String LIST_CHARS = "*-#>:!";
	private static final String[] LIST_OPEN = { "<ul><li>", "<ul><li>",
			"<ol><li>", "<blockquote>", "<div class='indent'>",
			"<div class='center'>" };
	private static final String[] LIST_CLOSE = { "</li></ul>\n",
			"</li></ul>\n", "</li></ol>\n", "</blockquote>\n", "</div>\n",
			"</div>\n" };

	private static final String FORMAT_CHARS = "*/_#&";
	private static final String[] FORMAT_DELIM = { "**", "//", "__", "##", "&&" };
	private static final String[] FORMAT_TAG_OPEN = { "<strong>", "<em>",
			"<span class=\"underline\">", "<tt>", "<mark>" };
	private static final String[] FORMAT_TAG_CLOSE = { "</strong>", "</em>",
			"</span>", "</tt>", "</mark>" };

	public static String renderXHTML(String wikiText) {
		return new WikiParser(wikiText).toString();
	}

	protected void parse(String wikiText) {
		wikiText = preprocessWikiText(wikiText);

		this.wikiText = wikiText;
		wikiLength = this.wikiText.length();
		wikiChars = new char[wikiLength];
		this.wikiText.getChars(0, wikiLength, wikiChars, 0);

		while (parseBlock())
			;

		closeListsAndTables();

		while (mediawikiTableLevel-- > 0)
			sb.append("</td></tr></table>\n");

		completeTOC();
	}

	protected WikiParser() {
		// for use by subclasses only
		// subclasses should call parse() to complete construction
	}

	protected WikiParser(String wikiText) {
		parse(wikiText);
	}

	public String toString() {
		return sb.toString();
	}

	private void closeListsAndTables() {
		// close unclosed lists
		while (listLevel >= 0) {
			sb.append(LIST_CLOSE[LIST_CHARS.indexOf(listLevels[listLevel--])]);
		}
		if (inTable) {
			sb.append("</table>\n");
			inTable = false;
		}
	}

	private boolean parseBlock() {
		for (; pos < wikiLength && wikiChars[pos] <= ' '
				&& wikiChars[pos] != '\n'; pos++)
			; // skip whitespace
		if (pos >= wikiLength)
			return false;

		char c = wikiChars[pos];

		if (c == '\n') { // blank line => end of list/table; no other meaning
			closeListsAndTables();
			pos++;
			return true;
		}

		if (c == '|') { // table
			if (mediawikiTableLevel > 0) {
				int pp = pos + 1;
				if (pp < wikiLength) {
					boolean newRow = false, endTable = false;
					if (wikiChars[pp] == '-') { // mediawiki-table new row
						newRow = true;
						pp++;
					} else if (wikiChars[pp] == '}') { // mediawiki-table end
														// table
						endTable = true;
						pp++;
					}
					for (; pp < wikiLength
							&& (wikiChars[pp] == ' ' || wikiChars[pp] == '\t'); pp++)
						; // skip spaces
					if (pp == wikiLength || wikiChars[pp] == '\n') { // nothing
																		// else
																		// on
																		// the
																		// line
																		// =>
																		// it's
																		// mediawiki-table
																		// markup
						closeListsAndTables(); // close lists if any
						sb.append(newRow ? "</td></tr>\n<tr><td>"
								: (endTable ? "</td></tr></table>\n"
										: "</td>\n<td>"));
						if (endTable)
							mediawikiTableLevel--;
						pos = pp + 1;
						return pp < wikiLength;
					}
				}
			}

			if (!inTable) {
				closeListsAndTables(); // close lists if any
				sb.append("<table border=\"1\">");
				inTable = true;
			}
			pos = parseTableRow(pos + 1);
			return true;
		} else {
			if (inTable) {
				sb.append("</table>\n");
				inTable = false;
			}
		}

		if (listLevel >= 0 || LIST_CHARS.indexOf(c) >= 0) { // lists
			int lc;
			// count list level
			for (lc = 0; lc <= listLevel && pos + lc < wikiLength
					&& wikiChars[pos + lc] == listLevels[lc]; lc++)
				;

			if (lc <= listLevel) { // end list block(s)
				do {
					sb.append(LIST_CLOSE[LIST_CHARS
							.indexOf(listLevels[listLevel--])]);
				} while (lc <= listLevel);
				// list(s) closed => retry from the same position
				blockquoteBR = true;
				return true;
			} else {
				if (pos + lc >= wikiLength)
					return false;
				char cc = wikiChars[pos + lc];
				int listType = LIST_CHARS.indexOf(cc);
				if (listType >= 0 && pos + lc + 1 < wikiLength
						&& wikiChars[pos + lc + 1] != cc
						&& listLevel < MAX_LIST_LEVELS) { // new list block
					sb.append(LIST_OPEN[listType]);
					listLevels[++listLevel] = cc;
					blockquoteBR = true;
					pos = parseListItem(pos + lc + 1);
					return true;
				} else if (listLevel >= 0) { // list item - same level
					if (listLevels[listLevel] == '>'
							|| listLevels[listLevel] == ':')
						sb.append('\n');
					else if (listLevels[listLevel] == '!')
						sb.append("</div>\n<div class='center'>");
					else
						sb.append("</li>\n<li>");
					pos = parseListItem(pos + lc);
					return true;
				}
			}
		}

		if (c == '=') { // heading
			int hc;
			// count heading level
			for (hc = 1; hc < 6 && pos + hc < wikiLength
					&& wikiChars[pos + hc] == '='; hc++)
				;
			if (pos + hc >= wikiLength)
				return false;
			int p;
			for (p = pos + hc; p < wikiLength
					&& (wikiChars[p] == ' ' || wikiChars[p] == '\t'); p++)
				; // skip spaces
			String tagName = "h" + (hc + HEADING_LEVEL_SHIFT);
			sb.append("<" + tagName + " id=''>"); // real id to be inserted
													// after parsing this item
			int hStart = sb.length();
			pos = parseItem(p, wikiText.substring(pos, pos + hc),
					ContextType.HEADER);
			String hText = sb.substring(hStart, sb.length());
			sb.append("</" + tagName + ">\n");
			String anchorId = generateTOCAnchorId(hc, hText);
			sb.insert(hStart - 2, anchorId);
			appendTOCItem(hc, anchorId, hText);
			return true;
		} else if (c == '{') { // nowiki-block?
			if (pos + 2 < wikiLength && wikiChars[pos + 1] == '{'
					&& wikiChars[pos + 2] == '{') {
				int startNowiki = pos + 3;
				int endNowiki = findEndOfNowiki(startNowiki);
				int endPos = endNowiki + 3;
				if (wikiText.lastIndexOf('\n', endNowiki) >= startNowiki) { // block
																			// <pre>
					if (wikiChars[startNowiki] == '\n')
						startNowiki++; // skip the very first '\n'
					if (wikiChars[endNowiki - 1] == '\n')
						endNowiki--; // omit the very last '\n'
					sb.append("<pre>");
					appendNowiki(wikiText.substring(startNowiki, endNowiki));
					sb.append("</pre>\n");
					pos = endPos;
					return true;
				}
				// else inline <nowiki> - proceed to regular paragraph handling
			} else if (pos + 1 < wikiLength && wikiChars[pos + 1] == '|') { // mediawiki-table?
				int pp;
				for (pp = pos + 2; pp < wikiLength
						&& (wikiChars[pp] == ' ' || wikiChars[pp] == '\t'); pp++)
					; // skip spaces
				if (pp == wikiLength || wikiChars[pp] == '\n') { // yes, it's
																	// start of
																	// a table
					sb.append("<table border=\"1\"><tr><td>");
					mediawikiTableLevel++;
					pos = pp + 1;
					return pp < wikiLength;
				}
			}
		} else if (c == '-' && wikiText.startsWith("----", pos)) {
			int p;
			for (p = pos + 4; p < wikiLength
					&& (wikiChars[p] == ' ' || wikiChars[p] == '\t'); p++)
				; // skip spaces
			if (p == wikiLength || wikiChars[p] == '\n') {
				sb.append("\n<hr/>\n");
				pos = p;
				return true;
			}
		} else if (c == '~') { // block-level escaping: '*' '-' '#' '>' ':' '!'
								// '|' '='
			if (pos + 1 < wikiLength) {
				char nc = wikiChars[pos + 1];
				if (nc == '>' || nc == ':' || nc == '-' || nc == '|'
						|| nc == '=' || nc == '!') { // can't be inline markup
					pos++; // skip '~' and proceed to regular paragraph handling
					c = nc;
				} else if (nc == '*' || nc == '#') { // might be inline markup
														// so need to double
														// check
					char nnc = pos + 2 < wikiLength ? wikiChars[pos + 2] : 0;
					if (nnc != nc) {
						pos++; // skip '~' and proceed to regular paragraph
								// handling
						c = nc;
					}
					// otherwise escaping will be done at line level
				} else if (nc == '{') { // might be inline {{{ markup so need to
										// double check
					char nnc = pos + 2 < wikiLength ? wikiChars[pos + 2] : 0;
					if (nnc == '|') { // mediawiki-table?
						pos++; // skip '~' and proceed to regular paragraph
								// handling
						c = nc;
					}
					// otherwise escaping will be done at line level
				}
			}
		}

		{ // paragraph handling
			sb.append("<p>");
			pos = parseItem(pos, null, ContextType.PARAGRAPH);
			sb.append("</p>\n");
			return true;
		}
	}

	/**
	 * Finds first closing '}}}' for nowiki block or span. Skips escaped
	 * sequences: '~}}}'.
	 * 
	 * @param startBlock
	 *            points to first char after '{{{'
	 * @return position of first '}' in closing '}}}'
	 */
	private int findEndOfNowiki(int startBlock) {
		// NOTE: this method could step back one char from startBlock position
		int endBlock = startBlock - 3;
		do {
			endBlock = wikiText.indexOf("}}}", endBlock + 3);
			if (endBlock < 0)
				return wikiLength; // no matching '}}}' found
			while (endBlock + 3 < wikiLength && wikiChars[endBlock + 3] == '}')
				endBlock++; // shift to end of sequence of more than 3x'}' (eg.
							// '}}}}}')
		} while (wikiChars[endBlock - 1] == '~');
		return endBlock;
	}

	/**
	 * Greedy version of findEndOfNowiki(). It finds the last possible closing
	 * '}}}' before next opening '{{{'. Also uses escapes '~{{{' and '~}}}'.
	 * 
	 * @param startBlock
	 *            points to first char after '{{{'
	 * @return position of first '}' in closing '}}}'
	 */
	@SuppressWarnings("unused")
	private int findEndOfNowikiGreedy(int startBlock) {
		// NOTE: this method could step back one char from startBlock position
		int nextBlock = startBlock - 3;
		do {
			do {
				nextBlock = wikiText.indexOf("{{{", nextBlock + 3);
			} while (nextBlock > 0 && wikiChars[nextBlock - 1] == '~');
			if (nextBlock < 0)
				nextBlock = wikiLength;
			int endBlock = wikiText.lastIndexOf("}}}", nextBlock);
			if (endBlock >= startBlock && wikiChars[endBlock - 1] != '~')
				return endBlock;
		} while (nextBlock < wikiLength);
		return wikiLength;
	}

	/**
	 * @param start
	 *            points to first char after pipe '|'
	 * @return
	 */
	private int parseTableRow(int start) {
		if (start >= wikiLength)
			return wikiLength;

		sb.append("<tr>");
		boolean endOfRow = false;
		do {
			int colspan = 0;
			while (start + colspan < wikiLength
					&& wikiChars[start + colspan] == '|')
				colspan++;
			start += colspan;
			colspan++;
			boolean th = start < wikiLength && wikiChars[start] == '=';
			start += (th ? 1 : 0);
			while (start < wikiLength && wikiChars[start] <= ' '
					&& wikiChars[start] != '\n')
				start++; // trim whitespace from the start

			if (start >= wikiLength || wikiChars[start] == '\n') { // skip last
																	// empty
																	// column
				start++; // eat '\n'
				break;
			}

			sb.append(th ? "<th" : "<td");
			if (colspan > 1)
				sb.append(" colspan=\"" + colspan + "\"");
			sb.append('>');
			try {
				parseItemThrow(start, null, ContextType.TABLE_CELL);
			} catch (EndOfSubContextException e) { // end of cell
				start = e.position;
				if (start >= wikiLength)
					endOfRow = true;
				else if (wikiChars[start] == '\n') {
					start++; // eat '\n'
					endOfRow = true;
				}
			} catch (EndOfContextException e) {
				start = e.position;
				endOfRow = true;
			}
			sb.append(th ? "</th>" : "</td>");
		} while (!endOfRow/* && start<wikiLength && wikiChars[start]!='\n' */);
		sb.append("</tr>\n");
		return start;
	}

	/**
	 * Same as parseItem(); blank line adds &lt;br/&gt;&lt;br/&gt;
	 * 
	 * @param start
	 */
	private int parseListItem(int start) {
		while (start < wikiLength && wikiChars[start] <= ' '
				&& wikiChars[start] != '\n')
			start++; // skip spaces
		int end = parseItem(start, null, ContextType.LIST_ITEM);
		if ((listLevels[listLevel] == '>' || listLevels[listLevel] == ':')
				&& wikiText.substring(start, end).trim().length() == 0) { // empty
																			// line
																			// within
																			// blockquote/div
			if (!blockquoteBR) {
				sb.append("<br/><br/>");
				blockquoteBR = true;
			}
		} else {
			blockquoteBR = false;
		}
		return end;
	}

	/**
	 * @param p
	 *            points to first slash in suspected URI (scheme://etc)
	 * @param start
	 *            points to beginning of parsed item
	 * @param end
	 *            points to end of parsed item
	 * 
	 * @return array of two integer offsets [begin_uri, end_uri] if matched,
	 *         null otherwise
	 */
	private int[] checkURI(int p, int start, int end) {
		if (p > start && wikiChars[p - 1] == ':') { // "://" found
			int pb = p - 1;
			while (pb > start && isLatinLetterOrDigit(wikiChars[pb - 1]))
				pb--;
			int pe = p + 2;
			while (pe < end && isUrlChar(wikiChars[pe]))
				pe++;
			URI uri = null;
			do {
				while (pe > p + 2 && ",.;:?!%)".indexOf(wikiChars[pe - 1]) >= 0)
					pe--; // don't want these chars at the end of URI
				try { // verify URL syntax
					uri = new URI(wikiText.substring(pb, pe));
				} catch (URISyntaxException e) {
					pe--; // try chopping from the end
				}
			} while (uri == null && pe > p + 2);
			if (uri != null && uri.isAbsolute() && !uri.isOpaque()) {
				int offs[] = { pb, pe };
				return offs;
			}
		}
		return null;
	}

	private int parseItem(int start, String delimiter, ContextType context) {
		try {
			return parseItemThrow(start, delimiter, context);
		} catch (EndOfContextException e) {
			return e.position;
		}
	}

	private int parseItemThrow(int start, String delimiter, ContextType context)
			throws EndOfContextException {
		StringBuilder tb = new StringBuilder();

		boolean specialCaseDelimiterHandling = "//".equals(delimiter);
		int p = start;
		int end = wikiLength;

		try {
			nextChar: while (true) {
				if (p >= end)
					throw new EndOfContextException(end); // break;

				if (delimiter != null && wikiText.startsWith(delimiter, p)) {
					if (!specialCaseDelimiterHandling
							|| checkURI(p, start, end) == null) {
						p += delimiter.length();
						return p;
					}
				}

				char c = wikiChars[p];
				boolean atLineStart = false;

				// context-defined break test
				if (c == '\n') {
					if (context == ContextType.HEADER
							|| context == ContextType.TABLE_CELL) {
						p++;
						throw new EndOfContextException(p);
					}
					if (p + 1 < end && wikiChars[p + 1] == '\n') { // blank line
																	// delimits
																	// everything
						p++; // eat one '\n' and leave another one unparsed so
								// parseBlock() can close all lists
						throw new EndOfContextException(p);
					}
					for (p++; p < end && wikiChars[p] <= ' '
							&& wikiChars[p] != '\n'; p++)
						; // skip whitespace
					if (p >= end)
						throw new EndOfContextException(p); // end of text
															// reached

					c = wikiChars[p];
					atLineStart = true;

					if (c == '-' && wikiText.startsWith("----", p)) { // check
																		// for
																		// ----
																		// <hr>
						int pp;
						for (pp = p + 4; pp < end
								&& (wikiChars[pp] == ' ' || wikiChars[pp] == '\t'); pp++)
							; // skip spaces
						if (pp == end || wikiChars[pp] == '\n')
							throw new EndOfContextException(p); // yes, it's
																// <hr>
					}

					if (LIST_CHARS.indexOf(c) >= 0) { // start of list item?
						if (FORMAT_CHARS.indexOf(c) < 0)
							throw new EndOfContextException(p);
						// here we have a list char, which also happen to be a
						// format char
						if (p + 1 < end && wikiChars[p + 1] != c)
							throw new EndOfContextException(p); // format chars
																// go in pairs
						if (/* context==ContextType.LIST_ITEM */listLevel >= 0
								&& c == listLevels[0]) {
							// c matches current list's first level, so it must
							// be new list item
							throw new EndOfContextException(p);
						}
						// otherwise it must be just formatting sequence => no
						// break of context
					} else if (c == '=') { // header
						throw new EndOfContextException(p);
					} else if (c == '|') { // table or mediawiki-table
						throw new EndOfContextException(p);
					} else if (c == '{') { // mediawiki-table?
						if (p + 1 < end && wikiChars[p + 1] == '|') {
							int pp;
							for (pp = p + 2; pp < end
									&& (wikiChars[pp] == ' ' || wikiChars[pp] == '\t'); pp++)
								; // skip spaces
							if (pp == end || wikiChars[pp] == '\n')
								throw new EndOfContextException(p); // yes, it's
																	// start of
																	// a table
						}
					}

					// if none matched add '\n' to text buffer
					tb.append('\n');
					// p and c already shifted past the '\n' and whitespace
					// after, so go on
				} else if (c == '|') {
					if (context == ContextType.TABLE_CELL) {
						p++;
						throw new EndOfSubContextException(p);
					}
				}

				int formatType;

				if (c == '{') {
					if (p + 1 < end && wikiChars[p + 1] == '{') {
						if (p + 2 < end && wikiChars[p + 2] == '{') { // inline
																		// or
																		// block
																		// <nowiki>
							appendText(tb.toString());
							tb.delete(0, tb.length()); // flush text buffer
							int startNowiki = p + 3;
							int endNowiki = findEndOfNowiki(startNowiki);
							p = endNowiki + 3;
							if (wikiText.lastIndexOf('\n', endNowiki) >= startNowiki) { // block
																						// <pre>
								if (wikiChars[startNowiki] == '\n')
									startNowiki++; // skip the very first '\n'
								if (wikiChars[endNowiki - 1] == '\n')
									endNowiki--; // omit the very last '\n'
								if (context == ContextType.PARAGRAPH)
									sb.append("</p>"); // break the paragraph
														// because XHTML does
														// not allow <pre>
														// children of <p>
								sb.append("<pre>");
								appendNowiki(wikiText.substring(startNowiki,
										endNowiki));
								sb.append("</pre>\n");
								if (context == ContextType.PARAGRAPH)
									sb.append("<p>"); // continue the paragraph
								// if (context==ContextType.NOWIKI_BLOCK) return
								// p; // in this context return immediately
								// after nowiki
							} else { // inline <nowiki>
								appendNowiki(wikiText.substring(startNowiki,
										endNowiki));
							}
							continue;
						} else if (p + 2 < end) { // {{image}}
							int endImg = wikiText.indexOf("}}", p + 2);
							if (endImg >= 0 && endImg < end) {
								appendText(tb.toString());
								tb.delete(0, tb.length()); // flush text buffer
								appendImage(wikiText.substring(p + 2, endImg));
								p = endImg + 2;
								continue;
							}
						}
					}
				} else if (c == '[') {
					if (p + 1 < end && wikiChars[p + 1] == '[') { // [[link]]
						int endLink = wikiText.indexOf("]]", p + 2);
						if (endLink >= 0 && endLink < end) {
							appendText(tb.toString());
							tb.delete(0, tb.length()); // flush text buffer
							appendLink(wikiText.substring(p + 2, endLink));
							p = endLink + 2;
							continue;
						}
					}
				} else if (c == '\\') {
					if (p + 1 < end && wikiChars[p + 1] == '\\') { // \\ = <br/>
						appendText(tb.toString());
						tb.delete(0, tb.length()); // flush text buffer
						sb.append("<br/>");
						p += 2;
						continue;
					}
				} else if (c == '<') {
					if (p + 1 < end && wikiChars[p + 1] == '<') {
						if (p + 2 < end && wikiChars[p + 2] == '<') { // <<<macro>>>
							int endMacro = wikiText.indexOf(">>>", p + 3);
							if (endMacro >= 0 && endMacro < end) {
								appendText(tb.toString());
								tb.delete(0, tb.length()); // flush text buffer
								appendMacro(wikiText.substring(p + 3, endMacro));
								p = endMacro + 3;
								continue;
							}
						}
					}
				} else if ((formatType = FORMAT_CHARS.indexOf(c)) >= 0) {
					if (p + 1 < end && wikiChars[p + 1] == c) {
						appendText(tb.toString());
						tb.delete(0, tb.length()); // flush text buffer
						if (c == '/') { // special case for "//" - check if it
										// is part of URL (scheme://etc)
							int[] uriOffs = checkURI(p, start, end);
							if (uriOffs != null) {
								int pb = uriOffs[0], pe = uriOffs[1];
								if (pb > start && wikiChars[pb - 1] == '~') {
									sb.delete(sb.length() - (p - pb + 1),
											sb.length()); // roll back URL +
															// tilde
									sb.append(escapeHTML(wikiText.substring(pb,
											pe)));
								} else {
									sb.delete(sb.length() - (p - pb),
											sb.length()); // roll back URL
									appendLink(wikiText.substring(pb, pe));
								}
								p = pe;
								continue;
							}
						}
						sb.append(FORMAT_TAG_OPEN[formatType]);
						try {
							p = parseItemThrow(p + 2, FORMAT_DELIM[formatType],
									context);
						} finally {
							sb.append(FORMAT_TAG_CLOSE[formatType]);
						}
						continue;
					}
				} else if (c == '~') { // escape
					// most start line escapes are dealt with in parseBlock()
					if (atLineStart) {
						// same as block-level escaping: '*' '-' '#' '>' ':' '|'
						// '='
						if (p + 1 < end) {
							char nc = wikiChars[p + 1];
							if (nc == '>' || nc == ':' || nc == '-'
									|| nc == '|' || nc == '=' || nc == '!') { // can't
																				// be
																				// inline
																				// markup
								tb.append(nc);
								p += 2; // skip '~' and nc
								continue nextChar;
							} else if (nc == '*' || nc == '#') { // might be
																	// inline
																	// markup so
																	// need to
																	// double
																	// check
								char nnc = p + 2 < end ? wikiChars[p + 2] : 0;
								if (nnc != nc) {
									tb.append(nc);
									p += 2; // skip '~' and nc
									continue nextChar;
								}
								// otherwise escaping will be done at line level
							} else if (nc == '{') { // might be inline {{{
													// markup so need to double
													// check
								char nnc = p + 2 < end ? wikiChars[p + 2] : 0;
								if (nnc == '|') { // mediawiki-table?
									tb.append(nc);
									tb.append(nnc);
									p += 3; // skip '~', nc and nnc
									continue nextChar;
								}
								// otherwise escaping will be done as usual at
								// line level
							}
						}
					}
					for (String e : ESCAPED_INLINE_SEQUENCES) {
						if (wikiText.startsWith(e, p + 1)) {
							tb.append(e);
							p += 1 + e.length();
							continue nextChar;
						}
					}
				} else if (c == '-') { // ' -- ' => &ndash;
					if (p + 2 < end && wikiChars[p + 1] == '-'
							&& wikiChars[p + 2] == ' ' && p > start
							&& wikiChars[p - 1] == ' ') {
						// appendText(tb.toString()); tb.delete(0, tb.length());
						// // flush text buffer
						// sb.append("&ndash; ");
						tb.append("&ndash; "); // &ndash; = "\u2013 "
						p += 3;
						continue;
					}
				}
				tb.append(c);
				p++;
			}
		} finally {
			appendText(tb.toString());
			tb.delete(0, tb.length()); // flush text buffer
		}
	}

	protected void appendMacro(String text) {
		if ("TOC".equals(text)) {
			sb.append("<<<TOC>>>"); // put TOC placeholder for replacing it
									// later with real TOC
		} else {
			sb.append("&lt;&lt;&lt;Macro:");
			sb.append(escapeHTML(unescapeHTML(text)));
			sb.append("&gt;&gt;&gt;");
		}
	}

	protected void appendLink(String text) {
		String[] link = split(text, '|');
		URI uri = null;
		try { // validate URI
			uri = new URI(link[0].trim());
		} catch (URISyntaxException e) {
		}
		//if (uri != null && uri.isAbsolute() && !uri.isOpaque()) {
		if (uri != null){
			String[] path = split(uri.getPath().toString(),'/');
			if(uri.isAbsolute()|| path.length > 1) {
				sb.append("<a href=\"" + escapeHTML(uri.toString())
						+ "\" target=\"_blank\" rel=\"nofollow\">");
				sb.append(escapeHTML(unescapeHTML(link.length >= 2
						&& !isEmpty(link[1].trim()) ? link[1] : link[0])));
				sb.append("</a>");
			} 
			else {
				sb.append("<a href=\"/sigagc/app/baixar?id=" + escapeHTML(uri.toString())
						+ "\" target=\"_blank\" title=\"Internal link\">");
				sb.append(escapeHTML(unescapeHTML(link.length >= 2
						&& !isEmpty(link[1].trim()) ? link[1] : link[0])));
				sb.append("</a>");
			}
		}
	}

	protected void appendImage(String text) {
		String[] link = split(text, '|');
		URI uri = null;
		try { // validate URI
			uri = new URI(link[0].trim());
		} catch (URISyntaxException e) {
		}
		String alt = escapeHTML(unescapeHTML(link.length >= 2
				&& !isEmpty(link[1].trim()) ? link[1] : link[0]));
		if (uri != null && uri.isAbsolute() && !uri.isOpaque()) {
			sb.append("<img src=\"" + escapeHTML(uri.toString()) + "\" alt=\""
					+ alt + "\" title=\"" + alt + "\" />");
		} else {
			sb.append("<img src=\"/sigagc/app/baixar?id=" + escapeHTML(uri.toString()) + "\" alt=\""
					+ alt + "\" title=\"" + alt + "\" />");
		}
	}

	protected void appendText(String text) {
		sb.append(escapeHTML(unescapeHTML(text)));
	}

	protected String generateTOCAnchorId(int hLevel, String text) {
		int i = 0;
		String id = (HEADING_ID_PREFIX != null ? HEADING_ID_PREFIX : "H"
				+ hLevel + "_")
				+ translit(text.replaceAll("<.+?>", "")).trim()
						.replaceAll("\\s+", "_")
						.replaceAll("[^a-zA-Z0-9_-]", "");
		while (tocAnchorIds.contains(id)) { // avoid duplicates
			i++;
			id = text + "_" + i;
		}
		tocAnchorIds.add(id);
		return id;
	}

	protected void appendTOCItem(int level, String anchorId, String text) {
		if (level > tocLevel) {
			while (level > tocLevel) {
				toc.append("<ul><li>");
				tocLevel++;
			}
		} else {
			while (level < tocLevel) {
				toc.append("</li></ul>");
				tocLevel--;
			}
			toc.append("</li>\n<li>");
		}
		toc.append("<a href='#" + anchorId + "'>" + text + "</a>");
	}

	protected void completeTOC() {
		while (0 < tocLevel) {
			toc.append("</li></ul>");
			tocLevel--;
		}
		int idx;
		String tocDiv = "<div class='toc'>" + toc.toString() + "</div>";
		while ((idx = sb.indexOf("<<<TOC>>>")) >= 0) {
			sb.replace(idx, idx + 9, tocDiv);
		}
	}

	protected void appendNowiki(String text) {
		sb.append(escapeHTML(replaceString(replaceString(text, "~{{{", "{{{"),
				"~}}}", "}}}")));
	}

	private static class EndOfContextException extends Exception {
		private static final long serialVersionUID = 1L;
		int position;

		public EndOfContextException(int position) {
			super();
			this.position = position;
		}
	}

	private static class EndOfSubContextException extends EndOfContextException {
		private static final long serialVersionUID = 1L;

		public EndOfSubContextException(int position) {
			super(position);
		}
	}

	public static boolean isUrlChar(char c) {
		// From MediaWiki: "._\\/~%-+&#?!=()@"
		// From http://www.ietf.org/rfc/rfc2396.txt :
		// reserved: ";/?:@&=+$,"
		// unreserved: "-_.!~*'()"
		// delim: "%#"
		if (isLatinLetterOrDigit(c))
			return true;
		return "/?@&=+,-_.!~()%#;:$*".indexOf(c) >= 0; // I excluded '\''
	}

	public static boolean isLatinLetterOrDigit(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c >= '0' && c <= '9');
	}

	/**
	 * Filters text so there are no '\r' chars in it ("\r\n" -&gt; "\n"; then
	 * "\r" -&gt; "\n"). Most importantly makes all blank lines (lines with only
	 * spaces) exactly like this: "\n\n". WikiParser relies on that.
	 * 
	 * @param text
	 * @return filtered text
	 */
	public static String preprocessWikiText(String text) {
		if (text == null)
			return "";
		text = text.trim();
		int length = text.length();
		char[] chars = new char[length];
		text.getChars(0, length, chars, 0);
		StringBuilder sb = new StringBuilder();
		boolean blankLine = true;
		StringBuilder spaces = new StringBuilder();
		for (int p = 0; p < length; p++) {
			char c = chars[p];
			if (c == '\r') { // "\r\n" -> "\n"; then "\r" -> "\n"
				if (p + 1 < length && chars[p + 1] == '\n')
					p++;
				sb.append('\n');
				spaces.delete(0, spaces.length()); // discard spaces if there is
													// nothing else on the line
				blankLine = true;
			} else if (c == '\n') {
				sb.append(c);
				spaces.delete(0, spaces.length()); // discard spaces if there is
													// nothing else on the line
				blankLine = true;
			} else if (blankLine) {
				if (c <= ' '/* && c!='\n' */) {
					spaces.append(c);
				} else {
					sb.append(spaces);
					blankLine = false;
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String escapeHTML(String s) {
		if (s == null)
			return "";
		StringBuffer sb = new StringBuffer(s.length() + 100);
		int length = s.length();

		for (int i = 0; i < length; i++) {
			char ch = s.charAt(i);

			if ('<' == ch) {
				sb.append("&lt;");
			} else if ('>' == ch) {
				sb.append("&gt;");
			} else if ('&' == ch) {
				sb.append("&amp;");
			} else if ('\'' == ch) {
				sb.append("&#39;");
			} else if ('"' == ch) {
				sb.append("&quot;");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private static HashMap<String, Character> entities = null;

	private static synchronized HashMap<String, Character> getHtmlEntities() {
		if (entities == null) {
			entities = new HashMap<String, Character>();
			entities.put("lt", '<');
			entities.put("gt", '>');
			entities.put("amp", '&');
			entities.put("quot", '"');
			entities.put("apos", '\'');
			entities.put("nbsp", '\u00A0');
			entities.put("shy", '\u00AD');
			entities.put("copy", '\u00A9');
			entities.put("reg", '\u00AE');
			entities.put("trade", '\u2122');
			entities.put("mdash", '\u2014');
			entities.put("ndash", '\u2013');
			entities.put("ldquo", '\u201C');
			entities.put("rdquo", '\u201D');
			entities.put("euro", '\u20AC');
			entities.put("middot", '\u00B7');
			entities.put("bull", '\u2022');
			entities.put("laquo", '\u00AB');
			entities.put("raquo", '\u00BB');
		}
		return entities;
	}

	public static String unescapeHTML(String value) {
		if (value == null)
			return null;
		if (value.indexOf('&') < 0)
			return value;
		HashMap<String, Character> ent = getHtmlEntities();
		StringBuffer sb = new StringBuffer();
		final int length = value.length();
		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			if (c == '&') {
				char ce = 0;
				int i1 = value.indexOf(';', i + 1);
				if (i1 > i && i1 - i <= 12) {
					if (value.charAt(i + 1) == '#') {
						if (value.charAt(i + 2) == 'x') {
							ce = (char) atoi(value.substring(i + 3, i1), 16);
						} else {
							ce = (char) atoi(value.substring(i + 2, i1));
						}
					} else {
						synchronized (ent) {
							Character ceObj = ent.get(value
									.substring(i + 1, i1));
							ce = ceObj == null ? 0 : ceObj.charValue();
						}
					}
				}
				if (ce > 0) {
					sb.append(ce);
					i = i1;
				} else
					sb.append(c);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	static public int atoi(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable ex) {
			return 0;
		}
	}

	static public int atoi(String s, int base) {
		try {
			return Integer.parseInt(s, base);
		} catch (Throwable ex) {
			return 0;
		}
	}

	public static String replaceString(String str, String from, String to) {
		StringBuffer buf = new StringBuffer();
		int flen = from.length();
		int i1 = 0, i2 = 0;
		while ((i2 = str.indexOf(from, i1)) >= 0) {
			buf.append(str.substring(i1, i2));
			buf.append(to);
			i1 = i2 + flen;
		}
		buf.append(str.substring(i1));
		return buf.toString();
	}

	public static String[] split(String s, char separator) {
		// this is meant to be faster than String.split() when separator is not
		// regexp
		if (s == null)
			return null;
		ArrayList<String> parts = new ArrayList<String>();
		int beginIndex = 0, endIndex;
		while ((endIndex = s.indexOf(separator, beginIndex)) >= 0) {
			parts.add(s.substring(beginIndex, endIndex));
			beginIndex = endIndex + 1;
		}
		parts.add(s.substring(beginIndex));
		String[] a = new String[parts.size()];
		return parts.toArray(a);
	}

	private static final String translitTable = "àaábâvãgädåe¸eæzhçzèiéyêkëlìmínîoïpðrñsòtóuôfõhöts÷chøshùschüûyúýeþyuÿyaÀAÁBÂVÃGÄDÅE¨EÆZHÇZÈIÉYÊKËLÌMÍNÎOÏPÐRÑSÒTÓUÔFÕHÖTS×CHØSHÙSCHÜÛYÚÝEÞYUßYA";
	/**
	 * Translates all non-basic-latin-letters characters into latin ones for use
	 * in URLs etc. Here is the implementation for cyrillic (Russian) alphabet.
	 * Unknown characters are omitted.
	 * 
	 * @param s
	 *            string to be translated
	 * @return translated string
	 */
	public static String translit(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder(s.length() + 100);
		final int length = s.length();
		final int translitTableLength = translitTable.length();

		for (int i = 0; i < length; i++) {
			char ch = s.charAt(i);
			// System.err.println("ch="+(int)ch);

			if ((ch >= 'à' && ch <= 'ÿ') || (ch >= 'À' && ch <= 'ß')
					|| ch == '¸' || ch == '¨') {
				int idx = translitTable.indexOf(ch);
				char c;
				if (idx >= 0) {
					for (idx++; idx < translitTableLength; idx++) {
						c = translitTable.charAt(idx);
						if ((c >= 'à' && c <= 'ÿ') || (c >= 'À' && c <= 'ß')
								|| c == '¸' || c == '¨')
							break;
						sb.append(c);
					}
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public static String emptyToNull(String s) {
		return "".equals(s) ? null : s;
	}

	public static String noNull(String s) {
		return s == null ? "" : s;
	}

	public static String noNull(String s, String val) {
		return s == null ? val : s;
	}

	public static boolean isEmpty(String s) {
		return (s == null || s.length() == 0);
	}
}

package br.gov.jfrj.siga.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StackSimplier {
	private boolean useDefaultExcludeClassPattern = true;
	private static ClassNamePatternMatcher DEFAULT_EXCLUDE_MATCHER = new ClassNamePatternMatcher(
			Arrays.asList("^sun", "^java", "^com.sun"));
	private ClassNamePatternMatcher excludeMatcher;
	private ClassNamePatternMatcher includeMatcher;

	public String simplify(Throwable exception) {
		StringBuilder buff = new StringBuilder();
		buff.append(exception.getClass().getName()).append(": ").append(exception.getMessage())
				.append(System.lineSeparator());
		StackTraceElement[] traces = exception.getStackTrace();
		filterAndAppend(buff, traces);

		while (exception.getCause() != null) {
			Throwable cause = exception.getCause();
			buff.append("Caused by: ").append(cause.getClass().getName()).append(": ").append(cause.getMessage())
					.append(System.lineSeparator());
			filterAndAppend(buff, cause.getStackTrace());
			exception = exception.getCause();
		}
		return buff.toString();
	}

	private void filterAndAppend(StringBuilder buff, StackTraceElement[] traces) {
		boolean firstIncluded = false;
		for (StackTraceElement stackTrace : traces) {
			if (!firstIncluded) {
				firstIncluded = true;
				buff.append("\tat ").append(stackTrace).append("\n");
			} else if (macthIncludeTrace(stackTrace)) {
				buff.append("\tat ").append(stackTrace).append("\n");
			}
		}
	}

	private boolean macthIncludeTrace(StackTraceElement stackTrace) {
		if (matchIncludeClass(stackTrace.getClassName())) {
			return true;
		}
		if (matchExcludeClass(stackTrace.getClassName())) {
			return false;
		}
		return true;
	}

	private boolean matchIncludeClass(String className) {
		return includeMatcher != null && includeMatcher.match(className);
	}

	private boolean matchExcludeClass(String className) {
		if (useDefaultExcludeClassPattern && DEFAULT_EXCLUDE_MATCHER.match(className))
			return true;
		if (excludeMatcher != null && excludeMatcher.match(className))
			return true;
		return false;
	}

	public void setUseDefaultExcludeClassPattern(boolean useDefaultExcludeClassPattern) {
		this.useDefaultExcludeClassPattern = useDefaultExcludeClassPattern;
	}

	public void setExcludeClassPatterns(String... patterns) {
		excludeMatcher = new ClassNamePatternMatcher(Arrays.asList(patterns));
	}

	public void setExcludeClassPatterns(List<String> patterns) {
		excludeMatcher = new ClassNamePatternMatcher(patterns);
	}

	public boolean isUseDefaultExcludeClassPattern() {
		return useDefaultExcludeClassPattern;
	}

	public void setIncludeClassPatterns(String... patterns) {
		includeMatcher = new ClassNamePatternMatcher(Arrays.asList(patterns));
	}

	public void setIncludeClassPatterns(List<String> patterns) {
		includeMatcher = new ClassNamePatternMatcher(patterns);
	}

	public List<String> getExcludePatterns() {
		if (excludeMatcher == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(excludeMatcher.patterns);
	}

	public List<String> getIncludePatterns() {
		if (includeMatcher == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(includeMatcher.patterns);
	}

	private static class ClassNamePatternMatcher {
		private List<String> patterns;
		private Pattern pattern;

		public ClassNamePatternMatcher(List<String> patterns) {
			this.patterns = new ArrayList<>(patterns);
			StringBuilder sb = new StringBuilder();
			for (String pat : patterns) {
				if (sb.length() > 0)
					sb.append("|");
				sb.append(pat.replace(".", "\\."));

			}
			pattern = Pattern.compile(sb.toString());
		}

		boolean match(String className) {
			return pattern.matcher(className).find();
		}
	}
}
package util;

import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import edu.emory.mathcs.backport.java.util.Arrays;

public class FieldNameExclusionEstrategy implements ExclusionStrategy {

	private List<String> names;
	private Boolean in;

	@SuppressWarnings("unchecked")
	private FieldNameExclusionEstrategy(Boolean in, String... names) {
		this.in = in;
		this.names = Arrays.asList(names);
	}

	public static FieldNameExclusionEstrategy in(String... names) {
		return new FieldNameExclusionEstrategy(Boolean.TRUE, names);
	}

	public static FieldNameExclusionEstrategy notIn(String... names) {
		return new FieldNameExclusionEstrategy(Boolean.FALSE, names);
	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		return Boolean.FALSE;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes fieldAttributes) {
		return in ? !names.contains(fieldAttributes.getName()) : names.contains(fieldAttributes.getName());
	}
}
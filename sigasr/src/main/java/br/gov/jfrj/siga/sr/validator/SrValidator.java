package br.gov.jfrj.siga.sr.validator;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;

@RequestScoped
public class SrValidator {
	
	private List<SrError> errors = new ArrayList<SrError>();
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SrValidator() {

	}

	public void addError(String key, String value){
		if(!"".equals(key) || !"".equals(value))
			errors.add(new SrError(key, value));
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public List<SrError> getErros() {
		return errors;
	}
}

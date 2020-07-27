package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.serialization.Deserializer;
import br.com.caelum.vraptor.serialization.Deserializes;

@Deserializes("text/vnd.graphviz")
public class VizDeserializer implements Deserializer {

	@Override
	public Object[] deserialize(InputStream is, ControllerMethod method) {
		List<Object> l = new ArrayList<>();
		try {
			String dot = IOUtils.toString(is);
			l.add(dot);
			return l.toArray();
		} catch (IOException e) {
			return null;
		}
	}

}

package br.gov.jfrj.siga.vraptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import br.com.caelum.vraptor.interceptor.DefaultTypeNameExtractor;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilderImpl;
import br.com.caelum.vraptor.serialization.xstream.XStreamConverters;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@PrototypeScoped
@Component
public class SigaXStreamBuilder extends XStreamBuilderImpl {

	public SigaXStreamBuilder(XStreamConverters converters,
			TypeNameExtractor extractor) {
		super(converters, extractor);
	}

	@Override
	public XStream jsonInstance() {
		XStream xstream = super.jsonInstance();
		xstream.aliasSystemAttribute(null, "class");
		xstream.aliasSystemAttribute(null, "resolves-to");

		xstream.registerConverter(new MapConverter(xstream.getMapper()) {

			public boolean canConvert(Class clazz) {
				return IncludedParameters.class.isAssignableFrom(clazz);
			}

			public void marshal(Object source, HierarchicalStreamWriter writer,
					MarshallingContext context) {
				Map map = (Map) ((IncludedParameters) source).getMap();
				for (Iterator iterator = map.entrySet().iterator(); iterator
						.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					if (entry.getValue() == null)
						continue;
					String entryName = entry.getKey().toString();
					ExtendedHierarchicalStreamWriterHelper.startNode(writer,
							entryName, entry.getValue().getClass());

					// writeItem(entry.getKey(), context, writer);
					// writeItem(entry.getValue(), context, writer);
					// if (Collection.class.isAssignableFrom(entry.getValue()
					// .getClass())) {

					// } else
					context.convertAnother(entry.getValue());

					writer.endNode();
				}
			}

		});
		return xstream;
	}

	@Override
	public XStream xmlInstance() {
		XStream xstream = super.xmlInstance();
		xstream.aliasSystemAttribute(null, "class");
		xstream.aliasSystemAttribute(null, "resolves-to");
		return xstream;
	}

	public static XStreamBuilder cleanInstance(Converter... converters) {
		return new SigaXStreamBuilder(new XStreamConverters(
				Arrays.asList(converters),
				Collections.<SingleValueConverter> emptyList()),
				new DefaultTypeNameExtractor());
	}
}
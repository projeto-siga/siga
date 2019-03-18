import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.mapping.Collection;

public class Diagram {
	private Map<String, Element> l = new TreeMap<String, Element>();
	FontRenderContext frc = new FontRenderContext(null, false, true);
	Font fontClass = new Font("Dialog", Font.BOLD, 13);
	Font fontAttr = new Font("Dialog", Font.PLAIN, 12);
	private boolean fMergeWithAbstractClass = false;

	public boolean isfMergeWithAbstractClass() {
		return fMergeWithAbstractClass;
	}

	public void setfMergeWithAbstractClass(boolean fMergeWithAbstractClass) {
		this.fMergeWithAbstractClass = fMergeWithAbstractClass;
	}

	private class Element {
		private String name;
		private Class clazz;
		private ArrayList<Field> attrsPojo = new ArrayList<Field>();
		private ArrayList<Field> attrs = new ArrayList<Field>();
		private ArrayList<Method> methods = new ArrayList<Method>();
		private double cx;
		private double cy;
		private Class superclass;
		private Boolean fIncludeAttributes;
		private Boolean fIncludeMethods;

		public Element(Class clazz, Boolean fIncludeAttributes,
				Boolean fIncludeMethods) {
			this.clazz = clazz;
			this.fIncludeAttributes = fIncludeAttributes;
			this.fIncludeMethods = fIncludeMethods;
			merge(clazz);
		}

		public void merge(Class clazz) {
			for (Field f : clazz.getDeclaredFields()) {
				attrs.add(f);
			}
			for (Method m : clazz.getDeclaredMethods()) {
				methods.add(m);
			}
		}

		private void preprocess() {
			Field f[] = getAttributes();
			for (int i = 0; i < f.length; i++) {
				String name = f[i].getName();
				boolean hasGetter = false;
				boolean hasSetter = false;
				Method setter = null;
				Method getter = null;
				String nameGetter = "get" + name.substring(0, 1).toUpperCase()
						+ name.substring(1);
				String nameSetter = "set" + name.substring(0, 1).toUpperCase()
						+ name.substring(1);

				Method m[] = getMethods();
				for (int j = 0; j < m.length; j++) {
					if (m[j].getName().equals(nameGetter)
							&& m[j].getReturnType().getName()
									.equals(f[i].getType().getName())
							&& m[j].getParameterTypes().length == 0) {
						hasGetter = true;
						getter = m[j];
					} else if (m[j].getName().equals(nameSetter)
							&& m[j].getParameterTypes().length == 1
							&& m[j].getParameterTypes()[0].getName().equals(
									f[i].getType().getName())
							&& m[j].getReturnType().getName().equals("void")) {
						hasSetter = true;
						setter = m[j];
					}
				}
				if (hasGetter && hasSetter) {
					attrsPojo.add(f[i]);
					attrs.remove(f[i]);
					methods.remove(getter);
					methods.remove(setter);
				}
			}
		}

		public Field[] getAttributes() {
			ArrayList<Field> a = new ArrayList<Field>();
			a.addAll(attrsPojo);
			a.addAll(attrs);
			return a.toArray(new Field[a.size()]);
		}

		public Method[] getMethods() {
			ArrayList<Method> a = new ArrayList<Method>();
			a.addAll(methods);
			return a.toArray(new Method[a.size()]);
		}

		public Rectangle2D getRectangle(boolean fIncludeAttributes,
				boolean fIncludeMethods) {
			Rectangle2D rResult = new Rectangle2D.Double();

			double fWidth = 0.0;
			double fHeight = 0.0;

			{
				// Class name
				Rectangle2D r = fontClass.getStringBounds(
						clazz.getSimpleName(), frc);
				fWidth = r.getWidth();
				fHeight = r.getHeight() * 1.60;

				// Extra space
				Rectangle2D rX = fontAttr.getStringBounds("W", frc);

				// Attributes
				if (fIncludeAttributes) {
					Field f[] = getAttributes();
					for (int i = 0; i < f.length; i++) {
						Rectangle2D rA = fontAttr.getStringBounds(
								getSignature(f[i]), frc);
						double dA = rA.getWidth();
						if (dA > fWidth)
							fWidth = dA;
						fHeight += (rX.getHeight()) * 0.975;
					}
					if (f.length != 0)
						fHeight += (rX.getHeight()) * 0.5;
				}

				// Methods
				if (fIncludeMethods) {
					Method m[] = getMethods();
					for (int i = 0; i < m.length; i++) {
						Rectangle2D rA = fontAttr.getStringBounds(
								getSignature(m[i]), frc);
						double dA = rA.getWidth();
						if (dA > fWidth)
							fWidth = dA;
						fHeight += (rX.getHeight()) * 0.975;
					}
					if (m.length != 0)
						fHeight += (rX.getHeight()) * 0.5;
				}

				fWidth += rX.getWidth();
			}

			rResult.setRect(0, 0, fWidth, fHeight);

			return rResult;
		}

		private boolean isPojo(Field f) {
			return attrsPojo.contains(f);
		}

		private String getSignature(Field field) {
			return field.getName() + ": " + field.getType().getSimpleName();
		}

		private String getSignature(Method method) {
			String parameters = "";
			for (Class c : method.getParameterTypes()) {
				if (parameters.length() != 0)
					parameters += ", ";
				parameters += c.getSimpleName();
			}
			return method.getName() + "(" + parameters + "): "
					+ method.getReturnType().getSimpleName();
		}

	}

	public void addClass(Class clazz, boolean fIncludeSuperclasses) {
		addClass(clazz, fIncludeSuperclasses, null, null);
	}

	public void addClass(Class clazz, boolean fIncludeSuperclasses,
			Boolean fIncludeAttributes, Boolean fIncludeMethods) {
		Element e = new Element(clazz, fIncludeAttributes, fIncludeMethods);
		l.put(clazz.getName(), e);

		if (isfMergeWithAbstractClass()) {
			if (clazz.getSuperclass().getSimpleName().startsWith("Abstract")) {
				e.merge(clazz.getSuperclass());
				e.superclass = clazz.getSuperclass();
				clazz = clazz.getSuperclass();
			}
		}

		if (fIncludeSuperclasses) {
			while (clazz.getSuperclass() != null) {
				clazz = clazz.getSuperclass();
				if (clazz.equals(Object.class))
					break;
				if (!l.containsKey(clazz.getName()))
					l.put(clazz.getName(), new Element(clazz,
							e.fIncludeAttributes, e.fIncludeMethods));
			}
		}
	}

	private void preprocess() {
		for (Element e : l.values()) {
			e.preprocess();
		}
	}

	public Element getSuperclass(Element e) {
		if (e.superclass != null)
			return l.get(e.superclass.getSuperclass().getName());
		return l.get(e.clazz.getSuperclass().getName());
	}

	public void createGraphML(String filename, boolean fIncludeAttributes,
			boolean fIncludeMethods) throws Exception {

		preprocess();

		Writer writer = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		File file = new File(filename);
		writer = new BufferedWriter(new FileWriter(file));

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		writer.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">");
		writer.write("<!--Created by yFiles for Java 2.8-->");
		writer.write("<key for=\"graphml\" id=\"d0\" yfiles.type=\"resources\"/>");
		writer.write("<key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>");
		writer.write("<key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>");
		writer.write("<key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>");
		writer.write("<key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d4\"/>");
		writer.write("<key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>");
		writer.write("<key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>");
		writer.write("<key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d7\">");
		writer.write("<default/>");
		writer.write("</key>");
		writer.write("<key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d8\"/>");
		writer.write("<key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>");
		writer.write("<key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>");
		writer.write("<graph edgedefault=\"directed\" id=\"G\">");

		// Class nodes
		//
		for (Element e : l.values()) {
			Rectangle2D r = e.getRectangle(
					e.fIncludeAttributes != null ? e.fIncludeAttributes
							: fIncludeAttributes,
					e.fIncludeMethods != null ? e.fIncludeMethods
							: fIncludeMethods);

			writer.write("<node id=\"" + e.clazz.getName() + "\">");
			writer.write("<data key=\"d4\"/>");
			writer.write("<data key=\"d5\"><![CDATA[UMLClass]]></data>");
			writer.write("<data key=\"d6\">");
			writer.write("<y:UMLClassNode>");
			writer.write("<y:Geometry height=\"" + r.getHeight()
					+ "\" width=\"" + r.getWidth()
					+ "\" x=\"-202.0\" y=\"-314.0\"/>");
			writer.write("<y:Fill color=\"#FFCC00\" transparent=\"false\"/>");
			writer.write("<y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>");
			writer.write("<y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"13\" fontStyle=\"bold\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"19.92626953125\" modelName=\"internal\" modelPosition=\"t\" textColor=\"#000000\" visible=\"true\" width=\"91.40087890625\" x=\"47.799560546875\" y=\"3.0\">");
			writer.write(e.clazz.getSimpleName());
			writer.write("</y:NodeLabel>");
			writer.write("<y:UML clipContent=\"true\" constraint=\"\" omitDetails=\"false\" stereotype=\"\" use3DEffect=\"false\">");
			writer.write("<y:AttributeLabel>");

			// Fields
			//
			if (e.fIncludeAttributes != null ? e.fIncludeAttributes
					: fIncludeAttributes) {
				Field f[] = e.getAttributes();
				for (int i = 0; i < f.length; i++) {
					if (i > 0)
						writer.write("\n");
					writer.write(e.getSignature(f[i]));
				}
			}

			writer.write("</y:AttributeLabel>");
			writer.write("<y:MethodLabel>");

			// Methods
			//
			if (e.fIncludeMethods != null ? e.fIncludeMethods : fIncludeMethods) {
				Method m[] = e.getMethods();
				for (int i = 0; i < m.length; i++) {
					if (i > 0)
						writer.write("\n");
					writer.write(e.getSignature(m[i]));
				}
			}

			writer.write("</y:MethodLabel>");
			writer.write("</y:UML>");
			writer.write("</y:UMLClassNode>");
			writer.write("</data>");
			writer.write("</node>");
		}

		// Edges to superclasses
		//
		for (Element e : l.values()) {
			if (getSuperclass(e) == null)
				continue;

			writer.write("<edge id=\"" + e.clazz.getName() + "-to-parent-"
					+ getSuperclass(e).clazz.getName() + "\" source=\""
					+ e.clazz.getName() + "\" target=\""
					+ getSuperclass(e).clazz.getName() + "\">");
			writer.write("<data key=\"d8\"/>");
			writer.write("<data key=\"d9\"><![CDATA[UMLinherits]]></data>");
			writer.write("<data key=\"d10\">");
			writer.write("<y:PolyLineEdge>");
			writer.write("<y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>");
			writer.write("<y:LineStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>");
			writer.write("<y:Arrows source=\"none\" target=\"white_delta\"/>");
			writer.write("<y:EdgeLabel alignment=\"center\" distance=\"2.0\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" hasText=\"false\" height=\"4.0\" modelName=\"six_pos\" modelPosition=\"tail\" preferredPlacement=\"anywhere\" ratio=\"0.5\" textColor=\"#000000\" visible=\"true\" width=\"4.0\" x=\"-22.154090025655535\" y=\"26.585013892351867\"/>");
			writer.write("<y:BendStyle smoothed=\"false\"/>");
			writer.write("</y:PolyLineEdge>");
			writer.write("</data>");
			writer.write("</edge>");
		}

		// Edges to field objects
		//
		for (Element e : l.values()) {
			Field f[] = e.getAttributes();
			for (int i = 0; i < f.length; i++) {
				Class destination = null;
				boolean fCollection = false;
				if (Set.class.isAssignableFrom(f[i].getType())) {
					Type gt = f[i].getGenericType();
					if (gt instanceof ParameterizedType) {
						ParameterizedType t = (ParameterizedType) gt;
						Type[] lt = t.getActualTypeArguments();
						for (Type tt : lt) {
							if (l.containsKey(((Class) tt).getName())) {
								destination = (Class) tt;
								fCollection = true;
							}
						}
					}
				} else {
					destination = f[i].getType();
				}

				if (destination == null
						|| !l.containsKey(destination.getName()))
					continue;

				writer.write("<edge id=\"" + e.clazz.getName() + "-to-field-"
						+ f[i].getName() + "\" source=\"" + e.clazz.getName()
						+ "\" target=\"" + destination.getName() + "\">");
				writer.write("<data key=\"d8\"/>");
				writer.write("<data key=\"d9\"><![CDATA[UMLhas]]></data>");
				writer.write("<data key=\"d10\">");
				writer.write("<y:PolyLineEdge>");
				writer.write("<y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>");
				writer.write("<y:LineStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>");

				if (fCollection)
					writer.write("<y:Arrows source=\"circle\" target=\"none\"/>");
				else
					writer.write("<y:Arrows source=\"transparent_circle\" target=\"none\"/>");

				writer.write("<y:EdgeLabel alignment=\"center\" distance=\"2.0\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" hasText=\"false\" height=\"4.0\" modelName=\"six_pos\" modelPosition=\"tail\" preferredPlacement=\"anywhere\" ratio=\"0.5\" textColor=\"#000000\" visible=\"true\" width=\"4.0\" x=\"-22.154090025655535\" y=\"26.585013892351867\"/>");
				writer.write("<y:BendStyle smoothed=\"false\"/>");
				writer.write("</y:PolyLineEdge>");
				writer.write("</data>");
				writer.write("</edge>");
			}
		}

		writer.write("</graph>");
		writer.write("<data key=\"d0\">");
		writer.write("<y:Resources/>");
		writer.write("</data>");
		writer.write("</graphml>");

		writer.close();

	}
}

package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.lang.reflect.*;

/**
 * AnnotationIntrospector that allows fields to inherit the namespace of the parent by default.
 *
 * This is really a convenience feature modified from example code in this discussion:
 * https://github.com/FasterXML/jackson-dataformat-xml/issues/18.
 *
 * Imagine class like this:
 * <pre>{@code
 * @JacksonXmlRootElement(localName="point", namespace="http://www.opengis.net/waterml/2.0")
 * class MyPpoint {
 *   String myValue;	//Will be in no namespace by default
 * }
 * }</pre>
 *
 * Without this introspector, the generated xml would look like this (assuming xml2 prefix
 * for waterml/2.0) :
 * <pre>{@code
 * <wml2:point>
 *   <myValue xmlns=""/>
 * </point>
 * }</pre>
 *
 * myValue is not assumed to be in the same namespace as point, so its namespace is explicitly
 * set to empty.  This could be fixed by assigning a namespace via the JacksonXmlProperty
 * annotation to each field, however, what a pain!
 *
 * Instead, this Introspector assumes that unannotated fields are in the namespace of the
 * declaring class.
 */
public class InheritNamespaceAnnotationIntrospector extends JacksonXmlAnnotationIntrospector {
	private static final long serialVersionUID = 1L;

	private String getNameSpace(Class c) {
		JacksonXmlRootElement classRootElement = (JacksonXmlRootElement) c.getAnnotation(JacksonXmlRootElement.class);
		if (classRootElement != null) {
			return classRootElement.namespace();
		} else {
			return null;
		}
	}

//		@Override
//		public PropertyName findRootName(AnnotatedClass ac)
//		{
//			String namespace = getNameSpace(ac.getAnnotated());
//			if (ac.getAnnotated() != null)
//				return new PropertyName(ac.getAnnotated().getSimpleName(), namespace);
//			else
//				return super.findRootName(ac);
//		}

	@Override
	public PropertyName findNameForSerialization(Annotated a)
	{
		AnnotatedElement ae = a.getAnnotated();
		if (Field.class.isInstance(ae)) {
			Field f = (Field)ae;
			String namespace =  getNameSpace(f.getDeclaringClass());
			if (namespace != null)
				return PropertyName.construct(f.getName(), namespace);
		}

		return super.findNameForSerialization(a);
	}

	@Override
	public String findNamespace(Annotated ann)
	{
		AnnotatedElement ae = ann.getAnnotated();
		if (Method.class.isInstance(ae)) {
			Method m = (Method)ae;
			String namespace = getNameSpace(m.getDeclaringClass());
			if (namespace != null)
				return namespace;

		}
		return super.findNamespace(ann);
	}
}

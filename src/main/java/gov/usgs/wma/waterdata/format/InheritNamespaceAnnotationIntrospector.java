package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

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


	//It seems possible that this method may need to be overriden similar to hoow
	//findNamespace is.  So far my test example doesn't require it.
//	@Override
//	public PropertyName findNameForSerialization(Annotated ann) {
//	}

	/**
	 * This override allows the class' namespace to be used when there is no JacksonXmlProperty on a field or method.
	 * @param ann
	 * @return
	 */
	@Override
	public String findNamespace(Annotated ann) {

		JacksonXmlProperty prop = (JacksonXmlProperty)this.findAnnotation(ann, JacksonXmlProperty.class);
		if (prop != null) {

			return prop.namespace();

		} else {

			AnnotatedElement ae = ann.getAnnotated();
			String namespace = getNameSpace(getDeclaringClass(ae));	//NS from declaring class
			return namespace;

		}

	}

	protected Class<?> getDeclaringClass(AnnotatedElement ae) {
		if (Method.class.isInstance(ae)) {
			Method m = (Method) ae;
			return m.getDeclaringClass();
		} else if (Field.class.isInstance(ae)) {
			Field f = (Field)ae;
			return f.getDeclaringClass();
		} else if (Class.class.isInstance(ae)) {
			Class c = (Class)ae;
			return c.getDeclaringClass();	//Nested class
		} else {
			return null;	//who knows??
		}
	}

	protected <A extends Annotation> A findAnnotation(Annotated ann, Class<A> annoClass) {
		return ann.getAnnotation(annoClass);
	}

}

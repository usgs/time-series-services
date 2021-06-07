package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.lang.reflect.*;

/**
 * AnnotationIntrospector that allows fields to inherit the namespace of the parent by default.
 */
public class InheritNamespaceAnnotationInspector extends JacksonXmlAnnotationIntrospector {
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

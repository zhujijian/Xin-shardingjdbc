package com.snowalker.shardingjdbc.snowalker.demo.util.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.Introspector;
import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/***
 * XML工具类
 */
public class XmlUtils {
    private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);
    private static final String UTF_8 = "UTF-8";

    // 私有构造方法
    private XmlUtils() {
    }

    /** 对象转换为xml字符串 */
    public static <T> String marshal(T obj) {
        if (obj == null) {
            return null;
        }

        String xml = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            JAXB.marshal(obj, os);
            xml = os.toString(UTF_8);
        } catch (Exception e) {
            logger.error("marshal()-", e);
        }

        return xml;
    }

    /** 对象转换为xml字符串 */
    public static <T> String marshal(T obj, boolean format, boolean fragment) {
        if (obj == null) {
            return null;
        }

        String xml = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            props.put(Marshaller.JAXB_FRAGMENT, fragment);
            _marshal(obj, os, props);
            xml = os.toString(UTF_8);
        } catch (Exception e) {
            logger.error("marshal()-", e);
        }

        return xml;
    }

    /** xml字符串转换为对象 */
    public static <T> T unmarshal(String xml, Class<T> clazz) {

        if (StringUtils.isBlank(xml)) {
            return null;
        }

        T obj = null;
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(UTF_8))) {
            obj = JAXB.unmarshal(is, clazz);
        } catch (Exception e) {
            logger.error("unmarshal()-", e);
        }

        return obj;

    }

    /** xml字符串转换为对象 */
    public static <T> T unmarshal(String xml, Class<T> clazz, boolean validate) {

        if (StringUtils.isBlank(xml)) {
            return null;
        }

        T obj = null;
        try {
            obj = _unmarshal(xml, clazz, validate);
        } catch (Exception e) {
            logger.error("unmarshal()-", e);
        }

        return obj;

    }

    //==========内部实现参考javax.xml.bind.JAXB===============
    private static final class Cache {
        final Class<?> type;
        final JAXBContext context;

        public Cache(Class<?> type) throws JAXBException {
            this.type = type;
            this.context = JAXBContext.newInstance(type);
        }
    }
    private static WeakReference<Cache> cache;
    private static <T> JAXBContext getContext(Class<T> type) throws JAXBException {
        WeakReference<Cache> c = cache;
        if (c != null) {
            Cache d = c.get();
            if (d != null && d.type == type)
                return d.context;
        }

        Cache d = new Cache(type);
        cache = new WeakReference<Cache>(d);

        return d.context;
    }

    private static <T> T _unmarshal(String xml, Class<T> clazz, boolean validate) {
        try {
            JAXBContext context = getContext(clazz);
            Unmarshaller u = context.createUnmarshaller();

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", validate);
            spf.setFeature("http://xml.org/sax/features/validation", validate);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            try (ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));) {
                InputSource inputSource = new InputSource(bais);
                SAXSource source = new SAXSource(xmlReader, inputSource);
                JAXBElement<T> item = u.unmarshal(source, clazz);
                return item.getValue();
            }
        } catch (Exception e) {
            throw new DataBindingException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void _marshal(Object jaxbObject, Object xml, Map<String, Object> props) {
        try {
            JAXBContext context;

            if (jaxbObject instanceof JAXBElement) {
                context = getContext(((JAXBElement<?>) jaxbObject).getDeclaredType());
            } else {
                Class<?> clazz = jaxbObject.getClass();
                XmlRootElement r = clazz.getAnnotation(XmlRootElement.class);
                context = getContext(clazz);
                if (r == null) {
                    jaxbObject = new JAXBElement(new QName(inferName(clazz)), clazz, jaxbObject);
                }
            }

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            Set<Entry<String, Object>> entrySet = props.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                m.setProperty(entry.getKey(), entry.getValue());
            }
            m.marshal(jaxbObject, toResult(xml));
        } catch (Exception e) {
            throw new DataBindingException(e);
        }
    }

    private static String inferName(Class<?> clazz) {
        return Introspector.decapitalize(clazz.getSimpleName());
    }

    private static Result toResult(Object xml) throws IOException {
        if (xml == null)
            throw new IllegalArgumentException("no XML is given");

        if (xml instanceof String) {
            try {
                xml = new URI((String) xml);
            } catch (URISyntaxException e) {
                xml = new File((String) xml);
            }
        }
        if (xml instanceof File) {
            File file = (File) xml;
            return new StreamResult(file);
        }
        if (xml instanceof URI) {
            URI uri = (URI) xml;
            xml = uri.toURL();
        }
        if (xml instanceof URL) {
            URL url = (URL) xml;
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(false);
            con.connect();
            return new StreamResult(con.getOutputStream());
        }
        if (xml instanceof OutputStream) {
            OutputStream os = (OutputStream) xml;
            return new StreamResult(os);
        }
        if (xml instanceof Writer) {
            Writer w = (Writer) xml;
            return new StreamResult(w);
        }
        if (xml instanceof Result) {
            return (Result) xml;
        }
        throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
    }

}

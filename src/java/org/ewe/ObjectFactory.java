
package org.ewe;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.ewe package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Response_QNAME = new QName("http://ewe.org/", "response");
    private final static QName _ExecSharcnet_QNAME = new QName("http://ewe.org/", "execSharcnet");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.ewe
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExecSharcnet }
     * 
     */
    public ExecSharcnet createExecSharcnet() {
        return new ExecSharcnet();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ewe.org/", name = "response")
    public JAXBElement<Response> createResponse(Response value) {
        return new JAXBElement<Response>(_Response_QNAME, Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecSharcnet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ewe.org/", name = "execSharcnet")
    public JAXBElement<ExecSharcnet> createExecSharcnet(ExecSharcnet value) {
        return new JAXBElement<ExecSharcnet>(_ExecSharcnet_QNAME, ExecSharcnet.class, null, value);
    }

}

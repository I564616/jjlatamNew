
package com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws package. 
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

    private final static QName _ReceiveElectronicNotaFiscalFromHybrisWrapper_QNAME = new QName("http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", "receiveElectronicNotaFiscalFromHybrisWrapper");
    private final static QName _ReceiveElectronicNotaFiscalFromHybrisWrapperResponse_QNAME = new QName("http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", "receiveElectronicNotaFiscalFromHybrisWrapperResponse");
    private final static QName _BTBControlAreaCustomName05_QNAME = new QName("", "CustomName05");
    private final static QName _BTBControlAreaCustomName04_QNAME = new QName("", "CustomName04");
    private final static QName _BTBControlAreaCustomValue05_QNAME = new QName("", "CustomValue05");
    private final static QName _BTBControlAreaCustomValue04_QNAME = new QName("", "CustomValue04");
    private final static QName _BTBControlAreaCustomValue03_QNAME = new QName("", "CustomValue03");
    private final static QName _BTBControlAreaCustomName02_QNAME = new QName("", "CustomName02");
    private final static QName _BTBControlAreaCustomValue02_QNAME = new QName("", "CustomValue02");
    private final static QName _BTBControlAreaCustomName03_QNAME = new QName("", "CustomName03");
    private final static QName _BTBControlAreaCustomValue01_QNAME = new QName("", "CustomValue01");
    private final static QName _BTBControlAreaCustomName01_QNAME = new QName("", "CustomName01");
    private final static QName _ElectronicNotaFiscalResponseResponse_QNAME = new QName("", "Response");
    private final static QName _ResponseError_QNAME = new QName("", "error");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReceiveElectronicNotaFiscalFromHybrisWrapperResponse }
     * 
     */
   /* public ReceiveElectronicNotaFiscalFromHybrisWrapperResponse createReceiveElectronicNotaFiscalFromHybrisWrapperResponse() {
        return new ReceiveElectronicNotaFiscalFromHybrisWrapperResponse();
    }*/

    /**
     * Create an instance of {@link ReceiveElectronicNotaFiscalFromHybrisWrapper }
     * 
     */
    public ReceiveElectronicNotaFiscalFromHybrisWrapper createReceiveElectronicNotaFiscalFromHybrisWrapper() {
        return new ReceiveElectronicNotaFiscalFromHybrisWrapper();
    }

    /**
     * Create an instance of {@link NFERequestData }
     * 
     */
    public NFERequestData createNFERequestData() {
        return new NFERequestData();
    }

    /**
     * Create an instance of {@link ElectronicNotaFiscalResponse }
     * 
     */
    public ElectronicNotaFiscalResponse createElectronicNotaFiscalResponse() {
        return new ElectronicNotaFiscalResponse();
    }

    /**
     * Create an instance of {@link ElectronicNotaFiscalRequest }
     * 
     */
    public ElectronicNotaFiscalRequest createElectronicNotaFiscalRequest() {
        return new ElectronicNotaFiscalRequest();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link BTBControlArea }
     * 
     */
    public BTBControlArea createBTBControlArea() {
        return new BTBControlArea();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveElectronicNotaFiscalFromHybrisWrapper }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", name = "receiveElectronicNotaFiscalFromHybrisWrapper")
    public JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapper> createReceiveElectronicNotaFiscalFromHybrisWrapper(ReceiveElectronicNotaFiscalFromHybrisWrapper value) {
        return new JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapper>(_ReceiveElectronicNotaFiscalFromHybrisWrapper_QNAME, ReceiveElectronicNotaFiscalFromHybrisWrapper.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveElectronicNotaFiscalFromHybrisWrapperResponse }{@code >}}
     * 
     */
 /*   @XmlElementDecl(namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", name = "receiveElectronicNotaFiscalFromHybrisWrapperResponse")
    public JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapperResponse> createReceiveElectronicNotaFiscalFromHybrisWrapperResponse(ReceiveElectronicNotaFiscalFromHybrisWrapperResponse value) {
        return new JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapperResponse>(_ReceiveElectronicNotaFiscalFromHybrisWrapperResponse_QNAME, ReceiveElectronicNotaFiscalFromHybrisWrapperResponse.class, null, value);
    }*/

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomName05", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName05(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName05_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomName04", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName04(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName04_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomValue05", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue05(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue05_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomValue04", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue04(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue04_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomValue03", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue03(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue03_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomName02", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName02(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName02_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomValue02", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue02(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue02_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomName03", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName03(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName03_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomValue01", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue01(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue01_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CustomName01", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName01(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName01_QNAME, String.class, BTBControlArea.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Response", scope = ElectronicNotaFiscalResponse.class)
    public JAXBElement<Response> createElectronicNotaFiscalResponseResponse(Response value) {
        return new JAXBElement<Response>(_ElectronicNotaFiscalResponseResponse_QNAME, Response.class, ElectronicNotaFiscalResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "error", scope = Response.class)
    public JAXBElement<String> createResponseError(String value) {
        return new JAXBElement<String>(_ResponseError_QNAME, String.class, Response.class, value);
    }

}

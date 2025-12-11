
package com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse package. 
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

    private final static QName _ProcessSNVerificationRequest_QNAME = new QName("http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse", "processSNVerificationRequest");
    private final static QName _ProcessSNVerificationRequestResponse_QNAME = new QName("http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse", "processSNVerificationRequestResponse");
    private final static QName _SerialNoRequestLocationName_QNAME = new QName("", "locationName");
    private final static QName _SerialNoRequestLocationProvince_QNAME = new QName("", "locationProvince");
    private final static QName _SerialNoRequestLocationCountry_QNAME = new QName("", "locationCountry");
    private final static QName _SerialNoRequestLocationStreet_QNAME = new QName("", "locationStreet");
    private final static QName _SerialNoRequestLocationZip_QNAME = new QName("", "locationZip");
    private final static QName _SerialNoRequestLocationLatitude_QNAME = new QName("", "locationLatitude");
    private final static QName _SerialNoRequestLocationLongitude_QNAME = new QName("", "locationLongitude");
    private final static QName _SerialNoRequestLocationState_QNAME = new QName("", "locationState");
    private final static QName _SerialNoRequestLocationCity_QNAME = new QName("", "locationCity");
    private final static QName _SerialNoResponseShipToAdress_QNAME = new QName("", "shipToAdress");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessSNVerificationRequest }
     * 
     */
    public ProcessSNVerificationRequest createProcessSNVerificationRequest() {
        return new ProcessSNVerificationRequest();
    }

    /**
     * Create an instance of {@link ProcessSNVerificationRequestResponse }
     * 
     */
    public ProcessSNVerificationRequestResponse createProcessSNVerificationRequestResponse() {
        return new ProcessSNVerificationRequestResponse();
    }

    /**
     * Create an instance of {@link SerialNoResponse }
     * 
     */
    public SerialNoResponse createSerialNoResponse() {
        return new SerialNoResponse();
    }

    /**
     * Create an instance of {@link SerialNoRequest }
     * 
     */
    public SerialNoRequest createSerialNoRequest() {
        return new SerialNoRequest();
    }

    /**
     * Create an instance of {@link ShipToAdress }
     * 
     */
    public ShipToAdress createShipToAdress() {
        return new ShipToAdress();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessSNVerificationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse", name = "processSNVerificationRequest")
    public JAXBElement<ProcessSNVerificationRequest> createProcessSNVerificationRequest(ProcessSNVerificationRequest value) {
        return new JAXBElement<ProcessSNVerificationRequest>(_ProcessSNVerificationRequest_QNAME, ProcessSNVerificationRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessSNVerificationRequestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://itsusral00725.jnj.com/PU023_ATLAS_SN_Verification_v1.webservices:SNVerificationRequestResponse", name = "processSNVerificationRequestResponse")
    public JAXBElement<ProcessSNVerificationRequestResponse> createProcessSNVerificationRequestResponse(ProcessSNVerificationRequestResponse value) {
        return new JAXBElement<ProcessSNVerificationRequestResponse>(_ProcessSNVerificationRequestResponse_QNAME, ProcessSNVerificationRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationName", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationName(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationName_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationProvince", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationProvince(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationProvince_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationCountry", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationCountry(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationCountry_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationStreet", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationStreet(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationStreet_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationZip", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationZip(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationZip_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationLatitude", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationLatitude(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationLatitude_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationLongitude", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationLongitude(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationLongitude_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationState", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationState(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationState_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "locationCity", scope = SerialNoRequest.class)
    public JAXBElement<String> createSerialNoRequestLocationCity(String value) {
        return new JAXBElement<String>(_SerialNoRequestLocationCity_QNAME, String.class, SerialNoRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipToAdress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "shipToAdress", scope = SerialNoResponse.class)
    public JAXBElement<ShipToAdress> createSerialNoResponseShipToAdress(ShipToAdress value) {
        return new JAXBElement<ShipToAdress>(_SerialNoResponseShipToAdress_QNAME, ShipToAdress.class, SerialNoResponse.class, value);
    }

}

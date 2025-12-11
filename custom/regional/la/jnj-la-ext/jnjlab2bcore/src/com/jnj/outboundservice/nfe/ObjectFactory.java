
package com.jnj.outboundservice.nfe;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jnj.outboundservice.nfe package. 
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
    private final static QName _NfeProc_QNAME = new QName("http://www.portalfiscal.inf.br/nfe", "nfeProc");
    private final static QName _Signature_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private final static QName _BTBControlAreaCustomName05_QNAME = new QName("", "CustomName05");
    private final static QName _BTBControlAreaCustomValue01_QNAME = new QName("", "CustomValue01");
    private final static QName _BTBControlAreaCustomName04_QNAME = new QName("", "CustomName04");
    private final static QName _BTBControlAreaCustomValue03_QNAME = new QName("", "CustomValue03");
    private final static QName _BTBControlAreaCustomValue02_QNAME = new QName("", "CustomValue02");
    private final static QName _BTBControlAreaCustomValue05_QNAME = new QName("", "CustomValue05");
    private final static QName _BTBControlAreaCustomValue04_QNAME = new QName("", "CustomValue04");
    private final static QName _BTBControlAreaCustomName01_QNAME = new QName("", "CustomName01");
    private final static QName _BTBControlAreaCustomName03_QNAME = new QName("", "CustomName03");
    private final static QName _BTBControlAreaCustomName02_QNAME = new QName("", "CustomName02");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jnj.outboundservice.nfe
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReceiveElectronicNotaFiscalFromHybrisWrapper }
     * 
     */
    public ReceiveElectronicNotaFiscalFromHybrisWrapper createReceiveElectronicNotaFiscalFromHybrisWrapper() {
        return new ReceiveElectronicNotaFiscalFromHybrisWrapper();
    }

    /**
     * Create an instance of {@link ReceiveElectronicNotaFiscalFromHybrisWrapperResponse }
     * 
     */
    public ReceiveElectronicNotaFiscalFromHybrisWrapperResponse createReceiveElectronicNotaFiscalFromHybrisWrapperResponse() {
        return new ReceiveElectronicNotaFiscalFromHybrisWrapperResponse();
    }

    /**
     * Create an instance of {@link BTBControlArea }
     * 
     */
    public BTBControlArea createBTBControlArea() {
        return new BTBControlArea();
    }

    /**
     * Create an instance of {@link ElectronicNotaFiscalRequest }
     * 
     */
    public ElectronicNotaFiscalRequest createElectronicNotaFiscalRequest() {
        return new ElectronicNotaFiscalRequest();
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
     * Create an instance of {@link TNfeProc }
     * 
     */
    public TNfeProc createTNfeProc() {
        return new TNfeProc();
    }

    /**
     * Create an instance of {@link IPI }
     * 
     */
    public IPI createIPI() {
        return new IPI();
    }

    /**
     * Create an instance of {@link Det }
     * 
     */
    public Det createDet() {
        return new Det();
    }

    /**
     * Create an instance of {@link ICMS20 }
     * 
     */
    public ICMS20 createICMS20() {
        return new ICMS20();
    }

    /**
     * Create an instance of {@link PISAliq }
     * 
     */
    public PISAliq createPISAliq() {
        return new PISAliq();
    }

    /**
     * Create an instance of {@link InfAdic }
     * 
     */
    public InfAdic createInfAdic() {
        return new InfAdic();
    }

    /**
     * Create an instance of {@link ICMS00 }
     * 
     */
    public ICMS00 createICMS00() {
        return new ICMS00();
    }

    /**
     * Create an instance of {@link ICMSSN201 }
     * 
     */
    public ICMSSN201 createICMSSN201() {
        return new ICMSSN201();
    }

    /**
     * Create an instance of {@link ICMSSN202 }
     * 
     */
    public ICMSSN202 createICMSSN202() {
        return new ICMSSN202();
    }

    /**
     * Create an instance of {@link TEnderEmi }
     * 
     */
    public TEnderEmi createTEnderEmi() {
        return new TEnderEmi();
    }

    /**
     * Create an instance of {@link ICMS10 }
     * 
     */
    public ICMS10 createICMS10() {
        return new ICMS10();
    }

    /**
     * Create an instance of {@link Compra }
     * 
     */
    public Compra createCompra() {
        return new Compra();
    }

    /**
     * Create an instance of {@link PISOutr }
     * 
     */
    public PISOutr createPISOutr() {
        return new PISOutr();
    }

    /**
     * Create an instance of {@link PISNT }
     * 
     */
    public PISNT createPISNT() {
        return new PISNT();
    }

    /**
     * Create an instance of {@link ObsFisco }
     * 
     */
    public ObsFisco createObsFisco() {
        return new ObsFisco();
    }

    /**
     * Create an instance of {@link ICMSUFDest }
     * 
     */
    public ICMSUFDest createICMSUFDest() {
        return new ICMSUFDest();
    }

    /**
     * Create an instance of {@link COFINSOutr }
     * 
     */
    public COFINSOutr createCOFINSOutr() {
        return new COFINSOutr();
    }

    /**
     * Create an instance of {@link Deduc }
     * 
     */
    public Deduc createDeduc() {
        return new Deduc();
    }

    /**
     * Create an instance of {@link RefNFP }
     * 
     */
    public RefNFP createRefNFP() {
        return new RefNFP();
    }

    /**
     * Create an instance of {@link ICMS }
     * 
     */
    public ICMS createICMS() {
        return new ICMS();
    }

    /**
     * Create an instance of {@link VeicProd }
     * 
     */
    public VeicProd createVeicProd() {
        return new VeicProd();
    }

    /**
     * Create an instance of {@link ISSQN }
     * 
     */
    public ISSQN createISSQN() {
        return new ISSQN();
    }

    /**
     * Create an instance of {@link COFINSNT }
     * 
     */
    public COFINSNT createCOFINSNT() {
        return new COFINSNT();
    }

    /**
     * Create an instance of {@link Encerrante }
     * 
     */
    public Encerrante createEncerrante() {
        return new Encerrante();
    }

    /**
     * Create an instance of {@link ICMSSN102 }
     * 
     */
    public ICMSSN102 createICMSSN102() {
        return new ICMSSN102();
    }

    /**
     * Create an instance of {@link ICMSSN900 }
     * 
     */
    public ICMSSN900 createICMSSN900() {
        return new ICMSSN900();
    }

    /**
     * Create an instance of {@link ICMSSN101 }
     * 
     */
    public ICMSSN101 createICMSSN101() {
        return new ICMSSN101();
    }

    /**
     * Create an instance of {@link DI }
     * 
     */
    public DI createDI() {
        return new DI();
    }

    /**
     * Create an instance of {@link Dest }
     * 
     */
    public Dest createDest() {
        return new Dest();
    }

    /**
     * Create an instance of {@link TVeiculo }
     * 
     */
    public TVeiculo createTVeiculo() {
        return new TVeiculo();
    }

    /**
     * Create an instance of {@link Pag }
     * 
     */
    public Pag createPag() {
        return new Pag();
    }

    /**
     * Create an instance of {@link ImpostoDevol }
     * 
     */
    public ImpostoDevol createImpostoDevol() {
        return new ImpostoDevol();
    }

    /**
     * Create an instance of {@link ICMS60 }
     * 
     */
    public ICMS60 createICMS60() {
        return new ICMS60();
    }

    /**
     * Create an instance of {@link InfNFeSupl }
     * 
     */
    public InfNFeSupl createInfNFeSupl() {
        return new InfNFeSupl();
    }

    /**
     * Create an instance of {@link ICMSTot }
     * 
     */
    public ICMSTot createICMSTot() {
        return new ICMSTot();
    }

    /**
     * Create an instance of {@link Adi }
     * 
     */
    public Adi createAdi() {
        return new Adi();
    }

    /**
     * Create an instance of {@link ExportInd }
     * 
     */
    public ExportInd createExportInd() {
        return new ExportInd();
    }

    /**
     * Create an instance of {@link RetTransp }
     * 
     */
    public RetTransp createRetTransp() {
        return new RetTransp();
    }

    /**
     * Create an instance of {@link Ide }
     * 
     */
    public Ide createIde() {
        return new Ide();
    }

    /**
     * Create an instance of {@link ForDia }
     * 
     */
    public ForDia createForDia() {
        return new ForDia();
    }

    /**
     * Create an instance of {@link Transp }
     * 
     */
    public Transp createTransp() {
        return new Transp();
    }

    /**
     * Create an instance of {@link ICMS51 }
     * 
     */
    public ICMS51 createICMS51() {
        return new ICMS51();
    }

    /**
     * Create an instance of {@link Cana }
     * 
     */
    public Cana createCana() {
        return new Cana();
    }

    /**
     * Create an instance of {@link ICMSST }
     * 
     */
    public ICMSST createICMSST() {
        return new ICMSST();
    }

    /**
     * Create an instance of {@link TLocal }
     * 
     */
    public TLocal createTLocal() {
        return new TLocal();
    }

    /**
     * Create an instance of {@link ProcRef }
     * 
     */
    public ProcRef createProcRef() {
        return new ProcRef();
    }

    /**
     * Create an instance of {@link Vol }
     * 
     */
    public Vol createVol() {
        return new Vol();
    }

    /**
     * Create an instance of {@link Exporta }
     * 
     */
    public Exporta createExporta() {
        return new Exporta();
    }

    /**
     * Create an instance of {@link DetExport }
     * 
     */
    public DetExport createDetExport() {
        return new DetExport();
    }

    /**
     * Create an instance of {@link ICMS40 }
     * 
     */
    public ICMS40 createICMS40() {
        return new ICMS40();
    }

    /**
     * Create an instance of {@link Avulsa }
     * 
     */
    public Avulsa createAvulsa() {
        return new Avulsa();
    }

    /**
     * Create an instance of {@link COFINS }
     * 
     */
    public COFINS createCOFINS() {
        return new COFINS();
    }

    /**
     * Create an instance of {@link ICMS30 }
     * 
     */
    public ICMS30 createICMS30() {
        return new ICMS30();
    }

    /**
     * Create an instance of {@link TNFe }
     * 
     */
    public TNFe createTNFe() {
        return new TNFe();
    }

    /**
     * Create an instance of {@link COFINSST }
     * 
     */
    public COFINSST createCOFINSST() {
        return new COFINSST();
    }

    /**
     * Create an instance of {@link TIpi }
     * 
     */
    public TIpi createTIpi() {
        return new TIpi();
    }

    /**
     * Create an instance of {@link InfProt }
     * 
     */
    public InfProt createInfProt() {
        return new InfProt();
    }

    /**
     * Create an instance of {@link Rastro }
     * 
     */
    public Rastro createRastro() {
        return new Rastro();
    }

    /**
     * Create an instance of {@link Fat }
     * 
     */
    public Fat createFat() {
        return new Fat();
    }

    /**
     * Create an instance of {@link AutXML }
     * 
     */
    public AutXML createAutXML() {
        return new AutXML();
    }

    /**
     * Create an instance of {@link II }
     * 
     */
    public II createII() {
        return new II();
    }

    /**
     * Create an instance of {@link Comb }
     * 
     */
    public Comb createComb() {
        return new Comb();
    }

    /**
     * Create an instance of {@link ICMS90 }
     * 
     */
    public ICMS90 createICMS90() {
        return new ICMS90();
    }

    /**
     * Create an instance of {@link RetTrib }
     * 
     */
    public RetTrib createRetTrib() {
        return new RetTrib();
    }

    /**
     * Create an instance of {@link Card }
     * 
     */
    public Card createCard() {
        return new Card();
    }

    /**
     * Create an instance of {@link TEndereco }
     * 
     */
    public TEndereco createTEndereco() {
        return new TEndereco();
    }

    /**
     * Create an instance of {@link PISQtde }
     * 
     */
    public PISQtde createPISQtde() {
        return new PISQtde();
    }

    /**
     * Create an instance of {@link ISSQNtot }
     * 
     */
    public ISSQNtot createISSQNtot() {
        return new ISSQNtot();
    }

    /**
     * Create an instance of {@link InfNFe }
     * 
     */
    public InfNFe createInfNFe() {
        return new InfNFe();
    }

    /**
     * Create an instance of {@link COFINSQtde }
     * 
     */
    public COFINSQtde createCOFINSQtde() {
        return new COFINSQtde();
    }

    /**
     * Create an instance of {@link TProtNFe }
     * 
     */
    public TProtNFe createTProtNFe() {
        return new TProtNFe();
    }

    /**
     * Create an instance of {@link IPINT }
     * 
     */
    public IPINT createIPINT() {
        return new IPINT();
    }

    /**
     * Create an instance of {@link Prod }
     * 
     */
    public Prod createProd() {
        return new Prod();
    }

    /**
     * Create an instance of {@link IPITrib }
     * 
     */
    public IPITrib createIPITrib() {
        return new IPITrib();
    }

    /**
     * Create an instance of {@link ICMSSN500 }
     * 
     */
    public ICMSSN500 createICMSSN500() {
        return new ICMSSN500();
    }

    /**
     * Create an instance of {@link CIDE }
     * 
     */
    public CIDE createCIDE() {
        return new CIDE();
    }

    /**
     * Create an instance of {@link PIS }
     * 
     */
    public PIS createPIS() {
        return new PIS();
    }

    /**
     * Create an instance of {@link ICMS70 }
     * 
     */
    public ICMS70 createICMS70() {
        return new ICMS70();
    }

    /**
     * Create an instance of {@link Lacres }
     * 
     */
    public Lacres createLacres() {
        return new Lacres();
    }

    /**
     * Create an instance of {@link Cobr }
     * 
     */
    public Cobr createCobr() {
        return new Cobr();
    }

    /**
     * Create an instance of {@link Arma }
     * 
     */
    public Arma createArma() {
        return new Arma();
    }

    /**
     * Create an instance of {@link RefECF }
     * 
     */
    public RefECF createRefECF() {
        return new RefECF();
    }

    /**
     * Create an instance of {@link NFref }
     * 
     */
    public NFref createNFref() {
        return new NFref();
    }

    /**
     * Create an instance of {@link COFINSAliq }
     * 
     */
    public COFINSAliq createCOFINSAliq() {
        return new COFINSAliq();
    }

    /**
     * Create an instance of {@link ICMSPart }
     * 
     */
    public ICMSPart createICMSPart() {
        return new ICMSPart();
    }

    /**
     * Create an instance of {@link Transporta }
     * 
     */
    public Transporta createTransporta() {
        return new Transporta();
    }

    /**
     * Create an instance of {@link Med }
     * 
     */
    public Med createMed() {
        return new Med();
    }

    /**
     * Create an instance of {@link ObsCont }
     * 
     */
    public ObsCont createObsCont() {
        return new ObsCont();
    }

    /**
     * Create an instance of {@link PISST }
     * 
     */
    public PISST createPISST() {
        return new PISST();
    }

    /**
     * Create an instance of {@link Total }
     * 
     */
    public Total createTotal() {
        return new Total();
    }

    /**
     * Create an instance of {@link Imposto }
     * 
     */
    public Imposto createImposto() {
        return new Imposto();
    }

    /**
     * Create an instance of {@link DetPag }
     * 
     */
    public DetPag createDetPag() {
        return new DetPag();
    }

    /**
     * Create an instance of {@link Emit }
     * 
     */
    public Emit createEmit() {
        return new Emit();
    }

    /**
     * Create an instance of {@link RefNF }
     * 
     */
    public RefNF createRefNF() {
        return new RefNF();
    }

    /**
     * Create an instance of {@link Dup }
     * 
     */
    public Dup createDup() {
        return new Dup();
    }

    /**
     * Create an instance of {@link SignatureType }
     * 
     */
    public SignatureType createSignatureType() {
        return new SignatureType();
    }

    /**
     * Create an instance of {@link X509DataType }
     * 
     */
    public X509DataType createX509DataType() {
        return new X509DataType();
    }

    /**
     * Create an instance of {@link CanonicalizationMethod }
     * 
     */
    public CanonicalizationMethod createCanonicalizationMethod() {
        return new CanonicalizationMethod();
    }

    /**
     * Create an instance of {@link SignedInfoType }
     * 
     */
    public SignedInfoType createSignedInfoType() {
        return new SignedInfoType();
    }

    /**
     * Create an instance of {@link DigestMethod }
     * 
     */
    public DigestMethod createDigestMethod() {
        return new DigestMethod();
    }

    /**
     * Create an instance of {@link TransformType }
     * 
     */
    public TransformType createTransformType() {
        return new TransformType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link KeyInfoType }
     * 
     */
    public KeyInfoType createKeyInfoType() {
        return new KeyInfoType();
    }

    /**
     * Create an instance of {@link SignatureMethod }
     * 
     */
    public SignatureMethod createSignatureMethod() {
        return new SignatureMethod();
    }

    /**
     * Create an instance of {@link TransformsType }
     * 
     */
    public TransformsType createTransformsType() {
        return new TransformsType();
    }

    /**
     * Create an instance of {@link SignatureValueType }
     * 
     */
    public SignatureValueType createSignatureValueType() {
        return new SignatureValueType();
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
    @XmlElementDecl(namespace = "http://itsusmpl00082.jnj.com/SG910_BtB_IN0501_ElectronicNotaFiscal_Hybris_Source_v1.webservices:receiveElectronicNotaFiscalWS", name = "receiveElectronicNotaFiscalFromHybrisWrapperResponse")
    public JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapperResponse> createReceiveElectronicNotaFiscalFromHybrisWrapperResponse(ReceiveElectronicNotaFiscalFromHybrisWrapperResponse value) {
        return new JAXBElement<ReceiveElectronicNotaFiscalFromHybrisWrapperResponse>(_ReceiveElectronicNotaFiscalFromHybrisWrapperResponse_QNAME, ReceiveElectronicNotaFiscalFromHybrisWrapperResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TNfeProc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.portalfiscal.inf.br/nfe", name = "nfeProc")
    public JAXBElement<TNfeProc> createNfeProc(TNfeProc value) {
        return new JAXBElement<TNfeProc>(_NfeProc_QNAME, TNfeProc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Signature")
    public JAXBElement<SignatureType> createSignature(SignatureType value) {
        return new JAXBElement<SignatureType>(_Signature_QNAME, SignatureType.class, null, value);
    }

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
    @XmlElementDecl(namespace = "", name = "CustomValue01", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue01(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue01_QNAME, String.class, BTBControlArea.class, value);
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
    @XmlElementDecl(namespace = "", name = "CustomValue03", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomValue03(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomValue03_QNAME, String.class, BTBControlArea.class, value);
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
    @XmlElementDecl(namespace = "", name = "CustomName01", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName01(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName01_QNAME, String.class, BTBControlArea.class, value);
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
    @XmlElementDecl(namespace = "", name = "CustomName02", scope = BTBControlArea.class)
    public JAXBElement<String> createBTBControlAreaCustomName02(String value) {
        return new JAXBElement<String>(_BTBControlAreaCustomName02_QNAME, String.class, BTBControlArea.class, value);
    }

}

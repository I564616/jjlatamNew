package com.jnj.b2b.jnjglobalordertemplate.download;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.download.JnjGTOrderDetailPdfView.MyPageEvents;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTShippingTrckInfoData;
import com.jnj.facades.data.SurgeryInfoData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Class responsible to create PDF view for Order detail.
 *
 * @author Accenture
 *
 */
public class JnjGTOrderDetailPdfView extends AbstractPdfView
{
	//sid
	private static final String ORDER_DETAIL_VM_PATH = "order.detail.pdf.vm.path";
	private static final String DATE_FORMAT="vm.date.format";
	private static final String ORDER_DETAIL_PDF_VM = "orderDetailPdf.vm";
	private static final String NEW_LINE_CAHARACTER = "\n";
	private static final Logger LOG = Logger.getLogger(JnjGTOrderDetailPdfView.class);
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	private static final int MARGIN = 32;
	//sid

	
	
	@Override
	protected Document newDocument()
	{
		
		return new Document(PageSize.A3.rotate());
	} 
 
	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		final String METHOD_NAME = "buildPdfDocument()";
		final String siteName = (String) arg0.get(Jnjb2bCoreConstants.SITE_NAME);
		final JnjGTOrderData orderDetails = (JnjGTOrderData) arg0.get("orderData");
		String siteLogoPath = (String) arg0.get("siteLogoPath");
		final boolean isMddSite = (siteName != null && Jnjb2bCoreConstants.MDD.equals(siteName)) ? true : false;
		final String htmlStringForPDF = generateVMForOrder(orderDetails, isMddSite, siteLogoPath);
		String finalFileName = orderDetails.getOrderNumber()!=null?"Order_"+orderDetails.getOrderNumber() + ".pdf" : "Order.pdf";
		arg4.setHeader("Content-Disposition", "attachment; filename="+finalFileName);
		// page number add start here
				 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
				 pdfWriter.setPageEvent(events);   
		       events.onOpenDocument(pdfWriter, document);
			   
			   //page number add end here
		try
		{
			final File file = File.createTempFile("certificate", ".pdf");
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			final HTMLWorker htmlWorker = new HTMLWorker(document);
			htmlWorker.parse(new StringReader(htmlStringForPDF));
			document.close();
		}
		catch (final Exception exception)
		{
			LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: " + exception);
		}
		
	}
	
	
	/**
	 * This method fetches the VM template for the CPSIA PDF download and returns HTML String
	 * 
	 * @param jnjNACpsiaData
	 * @return HTML String
	 */
	protected String generateVMForOrder(final JnjGTOrderData jnjGTOrderData, final boolean isMddSite, final String siteLogoPath)
	{ 
		final String METHOD_NAME = "generateVMForOrder";
		final String emptyField = new String();

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOG.info("Getting the vm file..");

		final String path = getClass().getResource(Config.getParameter(ORDER_DETAIL_VM_PATH)).getPath();
		LOG.info("Found Order Detail PDF Path as:  " + path);

		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf("/")));
		LOG.info(Logging.HYPHEN + METHOD_NAME + "file.resource.loader.path :  " + path.substring(0, path.lastIndexOf("/")));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Initializing Velocity Engine");
		velocityEngine.init();

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Retrieving template from Velocity Engine");
		final Template template = velocityEngine.getTemplate(ORDER_DETAIL_PDF_VM);

		final VelocityContext context = new VelocityContext();
		final DateTool date = new DateTool();
		final String dateformat=Config.getParameter(DATE_FORMAT);
		populateAndUpdatePdfData(jnjGTOrderData, context);
		context.put("dateformat", dateformat);
		context.put("date", date);
		context.put("JnjGTOrderData", jnjGTOrderData);
		context.put("isMddSite", isMddSite);
		context.put("siteLogoPath", siteLogoPath);

		final StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return String.valueOf(writer);
	}

	protected void populateAndUpdatePdfData(final JnjGTOrderData jnjGTOrderData, final VelocityContext context)
	{
		if (null != jnjGTOrderData.getBillingAddress())
		{
			if (StringUtils.isNotEmpty(jnjGTOrderData.getBillingAddress().getPostalCode()))
			{
				String billingAddressPostalCode = jnjGTOrderData.getBillingAddress().getPostalCode();
				if (billingAddressPostalCode.length() == 9)
				{
					jnjGTOrderData.getBillingAddress().setPostalCode(
							StringUtils.substring(billingAddressPostalCode, 0, 5) + "-"
									+ StringUtils.substring(billingAddressPostalCode, 5, 9));
				}
			}
		}

		if (null != jnjGTOrderData.getDeliveryAddress())
		{
			if (StringUtils.isNotEmpty(jnjGTOrderData.getDeliveryAddress().getPostalCode()))
			{
				String deliveryAddressPostalCode = jnjGTOrderData.getDeliveryAddress().getPostalCode();
				if (deliveryAddressPostalCode.length() == 9)
				{
					jnjGTOrderData.getDeliveryAddress().setPostalCode(
							StringUtils.substring(deliveryAddressPostalCode, 0, 5) + "-"
									+ StringUtils.substring(deliveryAddressPostalCode, 5, 9));
				}
			}
		}

		List<String> trackingKeyList = new ArrayList<>();
		List<String> packingdetailsKeyList = new ArrayList<>();
		List<Date> shipDateList = new ArrayList<>();
		List<Date> deliveryDateList = new ArrayList<>();

		int carrierSize = jnjGTOrderData.getCarrier().size();
		int billOfLaddingSize = jnjGTOrderData.getBillOfLading().size();


		if (!jnjGTOrderData.getTrackingIdList().isEmpty())
		{
			trackingKeyList.addAll(jnjGTOrderData.getTrackingIdList().keySet());
		}
 
		if (!jnjGTOrderData.getPackingListDetails().isEmpty())
		{
			packingdetailsKeyList.addAll(jnjGTOrderData.getPackingListDetails().keySet());
		}
		if (!jnjGTOrderData.getShippingTrackingInfo().isEmpty())
		{
			final Collection<Set<JnjGTShippingTrckInfoData>> shippingTrckInfoList = jnjGTOrderData.getShippingTrackingInfo()
					.values();
			for (final Set<JnjGTShippingTrckInfoData> shippingTrckInfoSet : shippingTrckInfoList)
			{
				for(JnjGTShippingTrckInfoData shippingTrckInfo:shippingTrckInfoSet)
				{
				shipDateList.add(((JnjGTShippingTrckInfoData) shippingTrckInfo).getShipDate());
				deliveryDateList.add(((JnjGTShippingTrckInfoData) shippingTrckInfo).getDeliveryDate());
				}
			}

		}
		int deliveryInfoIndex = Math.max(Math.max(carrierSize, billOfLaddingSize),
				Math.max(packingdetailsKeyList.size(), trackingKeyList.size()));
		List<String> listOfNumbers = new ArrayList<>();
		for (int i = 0; i < deliveryInfoIndex; i++)
		{
			listOfNumbers.add(String.valueOf(i + 1));
		}

		context.put("listOfNumbers", listOfNumbers);
		context.put("trackingKeyList", trackingKeyList);
		context.put("packingdetailsKeyList", packingdetailsKeyList);
		context.put("shipDateList", shipDateList);
		context.put("deliveryDateList", deliveryDateList);

		if (!StringUtils.isEmpty(jnjGTOrderData.getReasonCode()))
		{
			context.put("reasonCodeValue", getJnjCommonFacadeUtil().getMessageForCode(jnjGTOrderData.getReasonCode(), null, ""));
		}

		if (StringUtils.equals(jnjGTOrderData.getOrderType(), "ZDEL"))
		{
			if (null != jnjGTOrderData.getSurgeryInfo())
			{
				populateSurgeryData(jnjGTOrderData.getSurgeryInfo(), context);
			}

		}
	}

	/**
	 * 
	 */
	protected void populateSurgeryData(final SurgeryInfoData surgeryInfoData, final VelocityContext context)
	{
		if (!StringUtils.isEmpty(surgeryInfoData.getZone()))
		{
			context.put("surgeryInfoZone",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.zone." + surgeryInfoData.getZone(), null, ""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getPathology()))
		{
			context.put(
					"surgeryInfoPathology",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.pathology." + surgeryInfoData.getPathology(), null,
							""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getLevelsInstrumented()))
		{
			context.put(
					"surgeryInfoLevelsInstrumented",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.levelsInstrumented." + surgeryInfoData.getLevelsInstrumented(), null, ""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getInterbodyFusion()))
		{
			context.put(
					"surgeryInfoInterbodyFusion",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.interbodyFusion." + surgeryInfoData.getInterbodyFusion(), null, ""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getInterbody()))
		{
			context.put(
					"surgeryInfoInterbody",
					getJnjCommonFacadeUtil().getMessageForCode("surgeryInfoPopup.interbody." + surgeryInfoData.getInterbody(), null,
							""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getSurgicalApproach()))
		{
			context.put(
					"surgeryInfoSurgicalApproach",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.surgicalApproach." + surgeryInfoData.getSurgicalApproach(), null, ""));
		}

		if (!StringUtils.isEmpty(surgeryInfoData.getOrthobiologics()))
		{
			context.put(
					"surgeryInfoOrthobiologics",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.orthobiologics." + surgeryInfoData.getOrthobiologics(), null, ""));
		}
 
		if (!StringUtils.isEmpty(surgeryInfoData.getSurgerySpecialty()))
		{
			context.put(
					"surgeryInfoSurgerySpecialty",
					getJnjCommonFacadeUtil().getMessageForCode(
							"surgeryInfoPopup.surgerySpeciality." + surgeryInfoData.getSurgerySpecialty(), null, ""));
		}

	}
	
	
	

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
	{
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
	{
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}
	
	   
	  

public static class MyPageEvents extends PdfPageEventHelper {   
	 	
    private MessageSourceAccessor messageSourceAccessor;   

  
    private PdfContentByte cb;   

    
    private PdfTemplate template;   

     
    private BaseFont bf = null;   
       
    public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {   
        this.messageSourceAccessor = messageSourceAccessor;   
    }

     
    public void onOpenDocument(PdfWriter writer, Document document) {   
        try {   
            bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );   
            cb = writer.getDirectContent();   
            template = cb.createTemplate(50, 50);   
        } catch (DocumentException de) {   
        } catch (IOException ioe) {}   
    } 

     
    public void onEndPage(PdfWriter writer, Document document) {   
        int pageN = writer.getPageNumber()-1;   
        String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " " +   
            messageSourceAccessor.getMessage("of", "of") + " ";   
        float  len = bf.getWidthPoint( text, 8 );   
        cb.beginText();   
        cb.setFontAndSize(bf, 8);   

        cb.setTextMatrix(MARGIN, 16);   
        cb.showText(text);   
        cb.endText();   

        cb.addTemplate(template, MARGIN + len, 16);   
        cb.beginText();   
        cb.setFontAndSize(bf, 8);   

        cb.endText();   
    }  

   
    public void onCloseDocument(PdfWriter writer, Document document) {   
        template.beginText();   
        template.setFontAndSize(bf, 8);   
        template.showText(String.valueOf( writer.getPageNumber() - 2 ));   
        template.endText();   
    }  
}  
	
}

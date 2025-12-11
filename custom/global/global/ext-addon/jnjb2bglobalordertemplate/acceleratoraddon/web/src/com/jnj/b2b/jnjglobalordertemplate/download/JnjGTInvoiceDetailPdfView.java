package com.jnj.b2b.jnjglobalordertemplate.download;

import de.hybris.platform.core.GenericSearchConstants.LOG;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTInvoiceOrderData;
import com.jnj.facades.data.JnjGTOrderData;
import java.util.List;
import de.hybris.platform.util.Config;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.commons.lang3.StringUtils;
import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import org.springframework.context.support.MessageSourceAccessor;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import java.io.IOException;

public class JnjGTInvoiceDetailPdfView extends AbstractPdfView
{
	private static final String INVOICE_DETAIL_VM_PATH = "invoice.detail.pdf.vm.path";

	private static final String INVOICE_DETAIL_PDF_VM = "invoiceDetailPdf.vm";

	private static final String NEW_LINE_CAHARACTER = "\n";

	private static final Logger LOG = Logger.getLogger(JnjGTInvoiceDetailPdfView.class);
	
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;
	private static final int MARGIN = 32;
	
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
	{
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
	{
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}
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
		
		arg4.setHeader("Content-Disposition", "attachment; filename=InvoiceDetailResult.pdf");

		final JnjGTInvoiceOrderData invoiceData = (JnjGTInvoiceOrderData) arg0.get("orderInvoice");
		final JnjGTOrderData orderDetails = (JnjGTOrderData) arg0.get("orderData");
		final String siteName = (String) arg0.get(Jnjb2bCoreConstants.SITE_NAME);
		String siteLogoPath = (String) arg0.get("siteLogoPath");
		
		// page number add start here
		 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
		 pdfWriter.setPageEvent(events);   
		 events.onOpenDocument(pdfWriter, document);
	   
	   //page number add end here
		
		final boolean isMddSite = (siteName != null && Jnjb2bCoreConstants.MDD.equals(siteName)) ? true : false;

		final String htmlStringForPDF = generateVMForOrder(orderDetails, invoiceData, isMddSite, siteLogoPath);

		String finalFileName = "Invoice " + invoiceData.getInvoiceNumber() + ".pdf";
		arg4.setHeader("Content-Disposition", "attachment; filename=" + finalFileName);

		try
		{
			final File file = File.createTempFile("invoice", ".pdf");
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
	

	protected String generateVMForOrder(final JnjGTOrderData jnjGTOrderData, final JnjGTInvoiceOrderData invoiceData,
			final boolean isMddSite, final String siteLogoPath)
	{
		final String METHOD_NAME = "generateVMForOrder";
		final String emptyField = new String();

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOG.info("Getting the vm file..");

		final String path = getClass().getResource(Config.getParameter(INVOICE_DETAIL_VM_PATH)).getPath();
		LOG.info("Found Order Detail PDF Path as:  " + path);

		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf("/")));
		LOG.info(Logging.HYPHEN + METHOD_NAME + "file.resource.loader.path :  " + path.substring(0, path.lastIndexOf("/")));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Initializing Velocity Engine");
		velocityEngine.init();

		LOG.info(Logging.HYPHEN + METHOD_NAME + "Retrieving template from Velocity Engine");
		final Template template = velocityEngine.getTemplate(INVOICE_DETAIL_PDF_VM);

		// CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "VM File fetched :: " + template, LOG);

		final VelocityContext context = new VelocityContext();
		final DateTool date = new DateTool();

		populateAndUpdatePdfData(jnjGTOrderData, context);

		context.put("date", date);
		context.put("orderData", jnjGTOrderData);
		context.put("invoiceData", invoiceData);
		context.put("isMddSite", isMddSite);
		context.put("siteLogoPath", siteLogoPath);
		// CommonUtil.logDebugMessage("Values have been set in the context!");

		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		// CommonUtil.logDebugMessage("VM File merged with string!");

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

	 
	

	protected static class MyPageEvents extends PdfPageEventHelper {   
	 	
		protected MessageSourceAccessor messageSourceAccessor;   


		protected PdfContentByte cb;   

 
		protected PdfTemplate template;   

    
		protected BaseFont bf = null;   
       
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

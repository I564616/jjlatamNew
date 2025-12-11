package com.jnj.b2b.jnjglobalordertemplate.download;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderHistoryData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Image;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Font;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
/**
 * Class responsible to create PDF view for Order History.
 * 
 * @author Accenture
 * 
 */
public class JnjGTOrderHistoryPdfView extends AbstractPdfView
{
	
	private static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	private static final int MARGIN = 32;
	private static final Logger LOG = Logger.getLogger(JnjGTOrderHistoryPdfView.class);
	   
	
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	
	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}
	
	
	private static final String BILL_TO_ACC_NUMBER = "orderhistory.billto.accountnumber";
	private static final String BILL_TO_ADDRESS = "orderhistory.billto.address";
	private static final String SOLD_TO_ACC_NUMBER = "orderhistory.soldto.accountnumber";
	private static final String SOLD_TO_ADDRESS = "orderhistory.soldto.address";
	private static final String SHIP_TO_ACC_NUMBER = "orderhistory.shipto.accountnumber";
	private static final String SHIP_TO_ACC_NAME = "orderhistory.shipto.accountname";
	private static final String CUSTOMER_PO = "orderhistory.customer.po";
	private static final String ORDER_NUMBER = "orderhistory.order.number";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	private static final String ORDER_DATE = "orderhistory.order.date";
	private static final String ORDER_STATUS = "orderhistory.order.status";
	private static final String ORDER_TOTAL = "orderhistory.order.total";
	private static final String ORDER_CHANNEL = "orderhistory.order.channel";
	private static final String DELIVERY_DATE = "orderhistory.order.deliverydate";
	private static final String ORDER_TYPE = "orderhistory.order.ordertype";
	public static final String ORDER_HISTORY = "OrderHistorySearchResult";

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		//for page number added
		 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
		 pdfWriter.setPageEvent(events);   
       events.onOpenDocument(pdfWriter, document);  
   		final String fileName = Jnjb2bglobalordertemplateControllerConstants.JnjGTExcelPdfViewLabels.OrderHistory.ORDER_HISTORY+".pdf";
		arg4.setHeader("Content-Disposition", "attachment; filename="+fileName);
		final SearchPageData<JnjGTOrderHistoryData> searchPageData = (SearchPageData<JnjGTOrderHistoryData>) map
				.get("searchPageData");
		
		final List<JnjGTOrderHistoryData> results = searchPageData.getResults();
		final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
		final boolean isMddSite = (currentSite != null && Jnjb2bCoreConstants.MDD.equals(currentSite)) ? true : false;
		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");
		//Modified by Archana for AAOL-5513
		final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat dateFormatRequestedDeliveryDate= new SimpleDateFormat(Jnjb2bCoreConstants.REQUESTDELIVERYDATE_FORMAT);
		//image adding start 
	      	final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
	      	final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
	      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
	      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
	      	/*Image jnjConnectLogo = null;
	      	Image jnjConnectLogo2 = null;
	      	PdfPCell cell1 = null;
	      	PdfPCell cell2 = null;*/
	      	PdfPTable table = new PdfPTable(2); // 3 columns.
	      	table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	      	table.setTotalWidth(822F);
	      	table.setLockedWidth(true);
	      	table.setSpacingAfter(30f);
	      	
	      	if ( jnjConnectLogoByteArray != null){
	      		/*jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
	      		cell1 = new PdfPCell(jnjConnectLogo, false);
	      		cell1.setBorder(Rectangle.NO_BORDER);*/
	      		table.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
	      	}
	      	
	      	if ( jnjConnectLogoByteArray2 != null){
	      		/*jnjConnectLogo2 = Image.getInstance(jnjConnectLogoByteArray2);
	      		cell2 = new PdfPCell(jnjConnectLogo2, false);
	      		cell2.setBorder(Rectangle.NO_BORDER);*/
	      		table.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
	      	}
	      	/*table.addCell(cell1);
	      	table.addCell(cell2);*/
	      	//image adding end
		
		final PdfPTable headerTable = new PdfPTable(1);
		headerTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		headerTable.setTotalWidth(822F);
		headerTable.setLockedWidth(true);
		headerTable.setSpacingAfter(50f);

		if (resultLimitExceeded.booleanValue())
		{
			document.add(new Paragraph(RESULTS_EXCEEDED_MESSAGE));
			document.add(new Paragraph("\n"));
		}

		final PdfPCell headerCell = new PdfPCell(new Phrase("ORDER HISTORY SEARCHED RESULTS"));
		headerCell.setColspan(11);
		headerTable.addCell(headerCell);

		final PdfPTable orderTable = new PdfPTable(11);
		orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		orderTable.setTotalWidth(822F);
		orderTable.setLockedWidth(true);
		orderTable.setSpacingAfter(736f);

		if (isMddSite)
		{
			orderTable.addCell(messageSource.getMessage(BILL_TO_ACC_NUMBER, null, i18nService.getCurrentLocale()));
			orderTable.addCell(messageSource.getMessage(BILL_TO_ADDRESS, null, i18nService.getCurrentLocale()));
		}
		else
		{
			orderTable.addCell(messageSource.getMessage(SOLD_TO_ACC_NUMBER, null, i18nService.getCurrentLocale()));
			orderTable.addCell(messageSource.getMessage(SOLD_TO_ADDRESS, null, i18nService.getCurrentLocale()));
		}
		orderTable.addCell(messageSource.getMessage(SHIP_TO_ACC_NUMBER, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(SHIP_TO_ACC_NAME, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(CUSTOMER_PO, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(ORDER_NUMBER, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(ORDER_DATE, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(ORDER_STATUS, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(ORDER_TOTAL, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(ORDER_CHANNEL, null, i18nService.getCurrentLocale()));
		orderTable.addCell(messageSource.getMessage(DELIVERY_DATE, null, i18nService.getCurrentLocale()));

		final String emptyField = new String();
		if (results != null && !results.isEmpty())
		{
			for (final JnjGTOrderHistoryData data : results)
			{
				orderTable.addCell(data.getBillToNumber());
				orderTable.addCell(data.getBillToName());
				orderTable.addCell(data.getShipToNumber());
				orderTable.addCell(data.getShipToName());
				orderTable.addCell(data.getPurchaseOrderNumber());
				orderTable.addCell(data.getSapOrderNumber());
				orderTable.addCell((data.getPlaced() != null) ? dateFormat.format(data.getPlaced()).toString() : emptyField);//Modified by Archana for AAOL-5513
				orderTable.addCell((data.getStatus() != null) ? data.getStatusDisplay() : emptyField);
				orderTable.addCell((data.getTotal() != null) ? data.getTotal().getFormattedValue() : emptyField);
				orderTable.addCell(Config.getParameter(Jnjb2bglobalordertemplateControllerConstants.JnjGTExcelPdfViewLabels.OrderDetail.ORDER_CHANNEL
							+ (StringUtils.isNotEmpty(data.getChannel()) ? data.getChannel() : "other")));
				//LOG.info("delivery date: "+data.getDeliveryDate());
				//LOG.info("delivery date parsed: "+dateFormat.parse(data.getDeliveryDate()));
				orderTable.addCell((data.getDeliveryDate() != null) ? dateFormat.format(dateFormatRequestedDeliveryDate.parse(data.getDeliveryDate())).toString() : emptyField);
					
			}
		}
		document.add(table);
		document.add(headerTable);
		document.add(Chunk.NEWLINE);
		document.add(orderTable);
	}
	
	
   private static class MyPageEvents extends PdfPageEventHelper {   
  	 	
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
           int pageN = writer.getPageNumber();   
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
           template.showText(String.valueOf( writer.getPageNumber() - 1 ));   
           template.endText();   
       }  
   }  
   /**
    * @param path
    * @param align
    * @return
    * @throws Exception
    */
   public static PdfPCell createImageCell(byte[] path, int align) throws Exception {
   		Image img = Image.getInstance(path);
       PdfPCell cell = new PdfPCell(img, false);
       labelImageCellStyle(cell,align);
       return cell;
   } 
   /**
    * @param cell
    * @param align
    */
   public static void labelImageCellStyle(PdfPCell cell, int align){
     // alignment
     	if(align ==2){
     		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
     	}else{
     		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
     	}
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBorder(Rectangle.NO_BORDER);
     }
}

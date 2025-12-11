/**
 *
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTCutReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTCutReportOrderData;
import com.jnj.facades.data.JnjGTCutReportOrderEntryData;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;


/**
 * @author ujjwal.negi
 *
 */
public class JnjGTCutReportPdfView extends AbstractPdfView
{
	private static final Logger LOG = Logger.getLogger(JnjGTCutReportPdfView.class);
	private static final String CUTORDER_RESPONSE_DATA_LIST = "cutReportOrders";
	private static final String CUTREPORT_FORM_NAME = "jnjGTCutReportForm";

	protected static final int MARGIN = 32;
	private static final String sheetName = "CUT REPORT RESULT";
	private static final String sheetName1 = "CUT REPORT SEARCH CRITERIA";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}
//Modified by Archana for AAOL-5513
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	
	/**
	 * @return the jnjCommonUtil
	 */
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * @param jnjCommonUtil the jnjCommonUtil to set
	 */
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
	//Modified by Archana for AAOL-5513
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.servlet.view.document.AbstractPdfView#buildPdfDocument(java.util.Map,
	 * com.lowagie.text.Document, com.lowagie.text.pdf.PdfWriter, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		try
		{
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 arg2.setPageEvent(events);   
	       events.onOpenDocument(arg2, arg1);  
			
			arg4.setHeader("Content-Disposition", "attachment; filename=Cut_Report.pdf");
			/*final Map<String, JnjGTCutReportOrderData> jnjGTCutReportResponseDataList = (Map<String, JnjGTCutReportOrderData>) arg0
					.get(CUTORDER_RESPONSE_DATA_LIST);*/
			final List<JnjGTCutReportOrderData> jnjGTCutReportResponseDataList = (List<JnjGTCutReportOrderData>) arg0
					.get(CUTORDER_RESPONSE_DATA_LIST);
			final JnjGTCutReportForm searchCriteria = (JnjGTCutReportForm) arg0.get(CUTREPORT_FORM_NAME);
			final String emptyField = new String();
			final String accountsSelectedName = (String) arg0.get("accountreportname");
			final SimpleDateFormat dateFormat2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
			//image adding start 
	      	final InputStream jnjConnectLogoIS = (InputStream) arg0.get("jnjConnectLogoURL");
	      	final InputStream jnjConnectLogoIS2 = (InputStream) arg0.get("jnjConnectLogoURL2");
	      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
	      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
	      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
	      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	      	imageTable.setWidthPercentage(100);
	      	imageTable.setLockedWidth(false);
	      	imageTable.setSpacingAfter(30f);
	      	
	      	if ( jnjConnectLogoByteArray != null){
	      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
	      	}
	      	if ( jnjConnectLogoByteArray2 != null){
	      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
	      	}
	      	
	    	final PdfPTable searchCriteriaTable = new PdfPTable(7);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setWidthPercentage(100);
			searchCriteriaTable.setLockedWidth(false);
			searchCriteriaTable.setSpacingAfter(10f);
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
			final Date date = new Date();
			
			final PdfPCell headerCell = new PdfPCell();
			headerCell.setColspan(7);
		 	headerCell.setBorderWidthBottom(10f);
	       	headerCell.setBorderWidthTop(5f);
	       	headerCell.setBackgroundColor(Color.WHITE); 
	       	headerCell.setBorder(Rectangle.NO_BORDER);
	       	headerCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa"))));
			searchCriteriaTable.addCell(headerCell);
			
			searchCriteriaTable.addCell(createLabelCell("Download Date"));
			searchCriteriaTable.addCell(createLabelCell("Account Name"));
			searchCriteriaTable.addCell(createLabelCell("Accounts"));
			searchCriteriaTable.addCell(createLabelCell("Purchase Order No"));
			searchCriteriaTable.addCell(createLabelCell("PO Start date"));
			searchCriteriaTable.addCell(createLabelCell("PO End Date"));
			
			searchCriteriaTable.addCell(createLabelCell("Search By"));
			
			searchCriteriaTable.addCell(createValueCell(dateFormat.format(date)));
			searchCriteriaTable.addCell(createValueCell(accountsSelectedName != null ? accountsSelectedName : emptyField));
			searchCriteriaTable.addCell(createValueCell(searchCriteria.getAccountIds()));
			searchCriteriaTable.addCell(createValueCell(searchCriteria.getPoNumber()));
			searchCriteriaTable.addCell(createValueCell(dateFormat2.format(searchCriteria.getPostartDate())));//Modified by Archana for AAOL-5513
			searchCriteriaTable.addCell(createValueCell(dateFormat2.format(searchCriteria.getPoendDate())));//Modified by Archana for AAOL-5513
			
			searchCriteriaTable.addCell(createValueCell(searchCriteria.getProductCode() != null? searchCriteria.getProductCode() : emptyField));
	      	
	    

			PdfPTable cutOrderTable = new PdfPTable(11);
			setTableProperties(cutOrderTable);
			/* cutOrderTable.setSpacingAfter(736f); */
			
/*			cutOrderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			cutOrderTable.setTotalWidth(822F);
			cutOrderTable.setLockedWidth(true);
			cutOrderTable.setSpacingAfter(30f);*/
			cutOrderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			cutOrderTable.setWidthPercentage(100);
			cutOrderTable.setLockedWidth(false);
			cutOrderTable.setSpacingAfter(736f);
			
			final PdfPCell headerResultsCell = new PdfPCell();
			headerResultsCell.setColspan(11);
			headerResultsCell.setBorderWidthBottom(10f);
		 	headerResultsCell.setBorderWidthTop(5f);
	       	headerResultsCell.setBackgroundColor(Color.WHITE); 
	       	headerResultsCell.setBorder(Rectangle.NO_BORDER);
	    	headerResultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa"))));
	    	
			
	    	cutOrderTable.addCell(headerResultsCell);
			
			cutOrderTable.addCell(createLabelCell("Account Number"));
			cutOrderTable.addCell(createLabelCell("Product Name"));
			cutOrderTable.addCell(createLabelCell("Product Code"));
			cutOrderTable.addCell(createLabelCell("GTIN / UPC"));
			cutOrderTable.addCell(createLabelCell("Po Number"));
			cutOrderTable.addCell(createLabelCell("Order Number"));
			cutOrderTable.addCell(createLabelCell("Order Date"));
			cutOrderTable.addCell(createLabelCell("Cut Reason"));
			cutOrderTable.addCell(createLabelCell("Cut Quantity"));
			cutOrderTable.addCell(createLabelCell("Order Quantity"));
			cutOrderTable.addCell(createLabelCell("Next Available Date"));
			
			
					if (null != jnjGTCutReportResponseDataList)
			{
				for (final JnjGTCutReportOrderData jnjGTCutReportOrderData : jnjGTCutReportResponseDataList)
				{
					for(final JnjGTCutReportOrderEntryData jnjGTCutReportOrderEntryData :jnjGTCutReportOrderData.getCutReportEntries() ){
			
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderData.getAccountNumber() == null ? emptyField : 
							jnjGTCutReportOrderData.getAccountNumber().replaceAll("^0+", StringUtils.EMPTY)));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getProductName() == null ? emptyField : jnjGTCutReportOrderEntryData.getProductName()));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getProductCode() == null ? emptyField :jnjGTCutReportOrderEntryData.getProductCode().replaceAll("^0+", StringUtils.EMPTY)));
					
					//GTIN UPC
					
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getGtin()==null ? jnjGTCutReportOrderEntryData.getUpc()==null ? emptyField:
						
								"/"+jnjGTCutReportOrderEntryData.getUpc() : jnjGTCutReportOrderEntryData.getUpc()==null ? 
										"/"+jnjGTCutReportOrderEntryData.getUpc():
									jnjGTCutReportOrderEntryData.getUpc()+"/"+jnjGTCutReportOrderEntryData.getUpc()));
					
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderData.getPONumber() == null ? emptyField :
							jnjGTCutReportOrderData.getPONumber()));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderData.getOrderNumber() == null ? emptyField : 
							jnjGTCutReportOrderData.getOrderNumber()));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderData.getOrderDate() == null ? emptyField : 
						dateFormat2.format(jnjGTCutReportOrderData.getOrderDate().toString())));//Modified by Archana for AAOL-5513
					/*cutOrderTable.addCell(jnjGTCutReportOrderData.getOperatingCompany() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTCutReportOrderData.getOperatingCompany())));
					cutOrderTable.addCell(jnjGTCutReportOrderData.getShipToName() == null ? pdfCell : new PdfPCell(new Phrase(
							jnjGTCutReportOrderData.getShipToName())));*/
					
					/*cutOrderTable.addCell(jnjGTCutReportOrderEntryData.getOrderLine() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTCutReportOrderEntryData.getOrderLine())));*/
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getCutReason() == null ? emptyField : jnjGTCutReportOrderEntryData.getCutReason()));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getCutQuantity() == null ? emptyField :jnjGTCutReportOrderEntryData.getCutQuantity()));
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getOrderQuantity() == null ? emptyField :jnjGTCutReportOrderEntryData.getOrderQuantity()));
					
					cutOrderTable.addCell(createValueCell(jnjGTCutReportOrderEntryData.getAvailabilityDate() == null ? emptyField : 
						dateFormat2.format(jnjGTCutReportOrderEntryData.getAvailabilityDate().toString())));//Modified by Archana for AAOL-5513
					
					
					/*
					cutOrderTable.addCell(jnjGTCutReportOrderEntryData.getUnitOfMeasure() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTCutReportOrderEntryData.getUnitOfMeasure())));
					
					cutOrderTable.addCell(jnjGTCutReportOrderEntryData.getItemPrice() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTCutReportOrderEntryData.getItemPrice().getFormattedValue())));
					cutOrderTable.addCell(jnjGTCutReportOrderEntryData.getExtendedPrice() == null ? pdfCell : new PdfPCell(
							new Phrase(jnjGTCutReportOrderEntryData.getExtendedPrice().getFormattedValue())));
					*/
					
					
					
					
					arg1.add(imageTable);
					arg1.add(searchCriteriaTable);
					arg1.add(Chunk.NEWLINE);
					arg1.add(cutOrderTable);
					
				
					
					

					/*PdfPTable cutOrderEntryTable = new PdfPTable(9);
					setTableProperties(cutOrderEntryTable);
					cutOrderEntryTable.setSpacingAfter(736f);
					cutOrderEntryTable.addCell("Order Line");
					cutOrderEntryTable.addCell("Cut Reason");
					cutOrderEntryTable.addCell("Cut Quantity");
					cutOrderEntryTable.addCell("Order Quantity");
					cutOrderEntryTable.addCell("Unit");
					cutOrderEntryTable.addCell("Product Code");
					cutOrderEntryTable.addCell("Product Name");
					cutOrderEntryTable.addCell("Item Price");
					cutOrderEntryTable.addCell("Extended Price");
					arg1.add(cutOrderEntryTable);
					final List<JnjGTCutReportOrderEntryData> jnjGTCutReportOrderEntryDataList = (List<JnjGTCutReportOrderEntryData>) arg0
							.get(jnjGTCutReportOrderData.getOrderNumber());
					for (final JnjGTCutReportOrderEntryData jnjGTCutReportOrderEntryData : jnjGTCutReportOrderEntryDataList)
					{
						cutOrderEntryTable = new PdfPTable(9);
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getOrderLine() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getOrderLine())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getCutReason() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getCutReason())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getCutQuantity() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getCutQuantity())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getOrderQuantity() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getOrderQuantity())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getUnitOfMeasure() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getUnitOfMeasure())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getProductCode() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getProductCode().replaceAll("^0+", StringUtils.EMPTY))));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getProductName() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getProductName())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getItemPrice() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getItemPrice().toString())));
						cutOrderEntryTable.addCell(jnjGTCutReportOrderEntryData.getExtendedPrice() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCutReportOrderEntryData.getExtendedPrice().toString())));

					}
					arg1.add(cutOrderEntryTable);*/
					}
				}
			}
			//arg1.add(cutOrderTable);
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating PDF - " + exception.getMessage());
		}
	}

	/**
	 * @param cutOrderTable
	 */
	protected void setTableProperties(final PdfPTable table)
	{
		table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		table.setTotalWidth(822F);
		table.setLockedWidth(true);
	}

	//~ Inner Classes ----------------------------------------------------------   

	private static class MyPageEvents extends PdfPageEventHelper {   
		 	
		protected MessageSourceAccessor messageSourceAccessor;   

	    // This is the PdfContentByte object of the writer   
		protected PdfContentByte cb;   

	    // We will put the final number of pages in a template   
		protected PdfTemplate template;   

	    // This is the BaseFont we are going to use for the header / footer   
		protected BaseFont bf = null;   
	       
	    public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {   
	        this.messageSourceAccessor = messageSourceAccessor;   
	    }

	    // we override the onOpenDocument method   
	    public void onOpenDocument(PdfWriter writer, Document document) {   
	        try {   
	            bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );   
	            cb = writer.getDirectContent();   
	            template = cb.createTemplate(50, 50);   
	        } catch (DocumentException de) {   
	        } catch (IOException ioe) {}   
	    } 

	    // we override the onEndPage method   
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

	    // we override the onCloseDocument method   
	    public void onCloseDocument(PdfWriter writer, Document document) {   
	        template.beginText();   
	        template.setFontAndSize(bf, 8);   
	        template.showText(String.valueOf( writer.getPageNumber() - 1 ));   
	        template.endText();   
	    }  
	}
	protected PdfPCell createLabelCell(String text){
	      // font
	      Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.GRAY);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));//.toUpperCase()
	      // set style
	      labelCellStyle(cell);
	      return cell;
	  }
	protected PdfPCell createValueCell(String text){
	      // font
	      Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));
	      // set style
	      valueCellStyle(cell);
	      return cell;
	  }
	public void labelCellStyle(PdfPCell cell){
	    // alignment
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    // padding
	    cell.setPaddingLeft(3f);
	    cell.setPaddingTop(0f);
	    // background color
	    cell.setBackgroundColor(Color.LIGHT_GRAY);
	    // border
	    cell.setBorder(0);
	    cell.setBorderWidthBottom(1);
	    cell.setBorderColorBottom(Color.GRAY);
	    // height
	    cell.setMinimumHeight(18f);
	}
	public void valueCellStyle(PdfPCell cell){
		   // alignment
		   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		   cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		   // padding
		   cell.setPaddingTop(0f);
		   cell.setPaddingBottom(5f);
		   // border
		   cell.setBorder(0);
		   cell.setBorderWidthBottom(0.5f);
		   // height
		   cell.setMinimumHeight(18f);
		}
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

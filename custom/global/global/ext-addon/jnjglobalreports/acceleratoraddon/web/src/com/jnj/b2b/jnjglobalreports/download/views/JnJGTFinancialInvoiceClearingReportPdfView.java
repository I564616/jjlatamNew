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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView; 

import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceDueReportDueForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
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

public class JnJGTFinancialInvoiceClearingReportPdfView extends AbstractPdfView{


		private static final Logger LOG = Logger.getLogger(JnJGTFinancialInvoiceClearingReportPdfView.class);
		private static final String FINANCIAL_RESPONSE_DATA_LIST = "invoiceClearingResponse";
		private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
		protected static final int MARGIN = 32;
		private static final String FINANCIAL_INVOICE_DUE_FORM_NAME = "JnjGTInvoiceClearingForm";
		//private static final String FINANCIAL_INVOICE_FORM_NAME = "JnjGlobalFinancialAnalysisReportForm";
		private static final String sheetName = "FINANCIAL_INVOICE REPORT RESULT";
		private static final String sheetName1 = "FINANCIAL_INVOICE REPORT SEARCH CRITERIA";
		private static final String TEXT_ONE = "Please note: Prices are excluding VAT. Availability and Prices might be subject to change.";
		@Override
		protected Document newDocument()
		{
			return new Document(PageSize.A4.rotate());
		}
//Modified by Archana for AAOL-5513
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
		@Autowired
		protected I18NService i18nService;
		private MessageSource messageSource;
		
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
		/**
		 * This method generates the PDF doc
		 */
		@Override
		protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
				final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
		{
			try
			{
				//for page number added
				 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
				 arg2.setPageEvent(events);   
		       events.onOpenDocument(arg2, arg1);  
		       
				arg4.setHeader("Content-Disposition", "attachment; filename=Invoice_Clearing.pdf");
				final List<JnjGTInvoiceClearingReportResponseData> jnjGTInvoicePastDueReportResponseDataList = (List<JnjGTInvoiceClearingReportResponseData>) arg0
						.get(FINANCIAL_RESPONSE_DATA_LIST);
				/*final List<jnjGTInvoicePastDueReportResponseData> jnjGTBackorderReportResponseDataList = (List<jnjGTInvoicePastDueReportResponseData>) arg0
						.get(FINANCIAL_RESPONSE_DATA_LIST);*/
				final JnjGTInvoiceDueReportDueForm searchCriteria = (JnjGTInvoiceDueReportDueForm) arg0.get(FINANCIAL_INVOICE_DUE_FORM_NAME);
				/*final JnjGlobalFinancialAnalysisReportForm searchCriteria = (JnjGlobalFinancialAnalysisReportForm) arg0.get(FINANCIAL_INVOICE_FORM_NAME);*/
				final SimpleDateFormat dateFormat1 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());//Modified by Archana for AAOL-5513
				String emptyField = new String();
				final String accountsSelectedName = (String) arg0.get("accountreportname");
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
		      	//image adding end
		      	
				final PdfPTable searchCriteriaTable = new PdfPTable(6);
				searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				searchCriteriaTable.setWidthPercentage(100);
				searchCriteriaTable.setLockedWidth(false);
				searchCriteriaTable.setSpacingAfter(10f);
				
				final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
				final Date date = new Date();
				
				final PdfPCell headerCell = new PdfPCell();
				headerCell.setColspan(6);
			 	headerCell.setBorderWidthBottom(10f);
		       	headerCell.setBorderWidthTop(5f);
		       	headerCell.setBackgroundColor(Color.WHITE); 
		       	headerCell.setBorder(Rectangle.NO_BORDER);
		       	//headerCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
				searchCriteriaTable.addCell(headerCell);
				
				/*searchCriteriaTable.addCell(createLabelCell("Download Date"));
				searchCriteriaTable.addCell(createLabelCell("Account Name"));
				searchCriteriaTable.addCell(createLabelCell("Accounts"));
				searchCriteriaTable.addCell(createLabelCell("Start date"));
				searchCriteriaTable.addCell(createLabelCell("End Date"));
				searchCriteriaTable.addCell(createLabelCell("Search By"));*/
				
				/*searchCriteriaTable.addCell(createValueCell(dateFormat.format(date)));
				searchCriteriaTable.addCell(createValueCell(accountsSelectedName != null ? accountsSelectedName : emptyField));
				searchCriteriaTable.addCell(createValueCell(searchCriteria.getAccountIds()));
				searchCriteriaTable.addCell(createValueCell(searchCriteria.getStartDate()));
				searchCriteriaTable.addCell(createValueCell(searchCriteria.getEndDate()));*/
				//searchCriteriaTable.addCell(createValueCell(searchCriteria.getProductCode() != null? searchCriteria.getProductCode() : emptyField));
				 
				final PdfPTable displayTextTable = new PdfPTable(13);
				displayTextTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				displayTextTable.setWidthPercentage(100);
				displayTextTable.setLockedWidth(false);
				displayTextTable.setSpacingAfter(10f);
				
				final PdfPCell txtCellDisplayCell = new PdfPCell();
				txtCellDisplayCell.setColspan(10);
				txtCellDisplayCell.setBackgroundColor(Color.WHITE); 
				txtCellDisplayCell.setBorder(Rectangle.NO_BORDER);
				txtCellDisplayCell.setPhrase(new Phrase(TEXT_ONE,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
				displayTextTable.addCell(txtCellDisplayCell);
				
				final PdfPTable invoicePastDueTable = new PdfPTable(12);
				invoicePastDueTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				invoicePastDueTable.setWidthPercentage(100);
				invoicePastDueTable.setLockedWidth(false);
				invoicePastDueTable.setSpacingAfter(736f);
				
				final PdfPCell headerResultsCell = new PdfPCell();
				headerResultsCell.setColspan(10);
				headerResultsCell.setBorderWidthBottom(10f);
			 	headerResultsCell.setBorderWidthTop(5f);
		       	headerResultsCell.setBackgroundColor(Color.WHITE); 
		       	headerResultsCell.setBorder(Rectangle.NO_BORDER);
		    	headerResultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
		    	
		    	invoicePastDueTable.addCell(createLabelCell("Invoice No"));
				invoicePastDueTable.addCell(createLabelCell("Invoice Date"));
				invoicePastDueTable.addCell(createLabelCell("Sold To Account"));
				invoicePastDueTable.addCell(createLabelCell("Sold to Name"));
				invoicePastDueTable.addCell(createLabelCell("Receipt Number"));
				invoicePastDueTable.addCell(createLabelCell("Status"));
				invoicePastDueTable.addCell(createLabelCell("Payment Date"));
				invoicePastDueTable.addCell(createLabelCell("Total Amount"));
				invoicePastDueTable.addCell(createLabelCell("Open Amount"));
				invoicePastDueTable.addCell(createLabelCell("Currency"));
				invoicePastDueTable.addCell(createLabelCell("Sales Document Number"));
				invoicePastDueTable.addCell(createLabelCell("Customer PO Number"));

				if (null != jnjGTInvoicePastDueReportResponseDataList)
				{
					double totals = 0;
					for (final JnjGTInvoiceClearingReportResponseData jnjGTInvoicePastDueReportResponseData : jnjGTInvoicePastDueReportResponseDataList)
					{
						emptyField="";
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getInvoiceNum() != null ? jnjGTInvoicePastDueReportResponseData.getInvoiceNum() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getBillingDate() != null ? jnjGTInvoicePastDueReportResponseData.getBillingDate() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getSoldToAccNum() != null ? jnjGTInvoicePastDueReportResponseData.getSoldToAccNum() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getSoldToAccName() != null ? jnjGTInvoicePastDueReportResponseData.getSoldToAccName() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getReceiptNumber() != null ? jnjGTInvoicePastDueReportResponseData.getReceiptNumber() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getStatus() != null ? jnjGTInvoicePastDueReportResponseData.getStatus() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getPaymentDate() != null ?dateFormat1.format( jnjGTInvoicePastDueReportResponseData.getPaymentDate() ):emptyField));//Modified by Archana for AAOL-5513
						invoicePastDueTable.addCell(createValueCell((Double)jnjGTInvoicePastDueReportResponseData.getInvoiceTotalAmount() != null ? Double.toString(jnjGTInvoicePastDueReportResponseData.getInvoiceTotalAmount()) :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getOpenAmount() != null ? jnjGTInvoicePastDueReportResponseData.getOpenAmount() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getCurrency() != null ? jnjGTInvoicePastDueReportResponseData.getCurrency() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getOrderNum() != null ? jnjGTInvoicePastDueReportResponseData.getOrderNum() :emptyField));
						invoicePastDueTable.addCell(createValueCell(jnjGTInvoicePastDueReportResponseData.getCustomerPoNum() != null ? jnjGTInvoicePastDueReportResponseData.getCustomerPoNum() :emptyField));
						
					}
					
						
				}
				arg1.add(imageTable);
				arg1.add(searchCriteriaTable);
				arg1.add(Chunk.NEWLINE);
				arg1.add(displayTextTable);
				arg1.add(Chunk.NEWLINE);
				arg1.add(invoicePastDueTable);
			}
			catch (final Exception exception)
			{
				LOG.error("Error while creating PDF - " + exception.getMessage());
			}
		}
		
		//create cells
	protected PdfPCell createLabelCell(String text){
	      // font
	      Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.GRAY);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));//.toUpperCase()
	      // set style
	      labelCellStyle(cell);
	      return cell;
	  }

	  // create cells
	protected PdfPCell createValueCell(String text){
	      // font
	      Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text,font));
	      // set style
	      valueCellStyle(cell);
	      return cell;
	  }

	 public void headerCellStyle(PdfPCell cell){
	    // alignment
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    // padding
	   cell.setPaddingTop(0f);
	   cell.setPaddingBottom(7f);
	   // background color
	   cell.setBackgroundColor(new Color(0,121,182));
	   // border
	   cell.setBorder(0);
	   cell.setBorderWidthBottom(2f);

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

package com.jnj.b2b.jnjglobalreports.download.views;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.facades.data.JnjGTFinancePurchaseOrderReportResponseData;
import com.jnj.facades.data.JnjGTOADeliveryListReportResponseData;
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

public class JnJGTFnancialInvoiceReportPdfView extends AbstractPdfView{


		private static final Logger LOG = Logger.getLogger(JnJGTFnancialInvoiceReportPdfView.class);
		private static final String FINANCIAL_RESPONSE_DATA_LIST = "jnjGTFinancialAnalysisOrderReportResponseDataMap";
		protected static final int MARGIN = 32;
		private static final String FINANCIAL_INVOICE_FORM_NAME = "JnjGlobalFinancialAnalysisReportForm";
		private static final String sheetName = "FINANCIAL_INVOICE REPORT RESULT";
		private static final String sheetName1 = "FINANCIAL_INVOICE REPORT SEARCH CRITERIA";
		@Override
		protected Document newDocument()
		{
			return new Document(PageSize.A4.rotate());
		}

		/**
		 * This method generates the PDF doc
		 */
		@Override
		protected void buildPdfDocument(final Map<String, Object> map, final Document arg1, final PdfWriter arg2,
				final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
		{
			try
			{
				//for page number added
				 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
				 arg2.setPageEvent(events);   
		       events.onOpenDocument(arg2, arg1);  
		       
				arg4.setHeader("Content-Disposition", "attachment; filename=Financial_invoice_Report.pdf");
			
				
				final JnjGlobalFinancialAnalysisReportForm formData = (JnjGlobalFinancialAnalysisReportForm) map
						.get(FINANCIAL_INVOICE_FORM_NAME);
				final TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> dataMap = (TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData>) map
						.get(FINANCIAL_RESPONSE_DATA_LIST);
				
				//image adding start 
		      	final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
		      	final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
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
		      	
				final PdfPTable searchCriteriaTable = new PdfPTable(9);
				searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				searchCriteriaTable.setWidthPercentage(100);
				searchCriteriaTable.setLockedWidth(false);
				searchCriteriaTable.setSpacingAfter(10f);
				
				final PdfPCell headerCell = new PdfPCell();
				headerCell.setColspan(9);
			 	headerCell.setBorderWidthBottom(10f);
		       	headerCell.setBorderWidthTop(5f);
		       	headerCell.setBackgroundColor(Color.WHITE); 
		       	headerCell.setBorder(Rectangle.NO_BORDER);
		       	headerCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
				searchCriteriaTable.addCell(headerCell);
				
				searchCriteriaTable.addCell("Account");
				searchCriteriaTable.addCell("Start Date");
				searchCriteriaTable.addCell("End Date");
				searchCriteriaTable.addCell("Status");
				searchCriteriaTable.addCell("Customer PO Number");
				searchCriteriaTable.addCell("Sales Document Number");
				searchCriteriaTable.addCell("Invoice Number");
				searchCriteriaTable.addCell("Franchise Desc");
				searchCriteriaTable.addCell("Order Type");
				
				
				if (null != formData) {
					searchCriteriaTable.addCell(formData.getAccountIds());
					searchCriteriaTable.addCell(formData.getStartDate());
					searchCriteriaTable.addCell(formData.getEndDate());
					searchCriteriaTable.addCell(formData.getFinancialStatus());
					searchCriteriaTable.addCell(formData.getCustomerPONumber());
					searchCriteriaTable.addCell(formData.getSalesDocumentNumber());
					searchCriteriaTable.addCell(formData.getInvoiceNumber());
					searchCriteriaTable.addCell(formData.getFranchiseDesc());
					searchCriteriaTable.addCell(formData.getOrderType());
				}
				
				final PdfPTable displayTextTable = new PdfPTable(12);
				displayTextTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				displayTextTable.setWidthPercentage(100);
				displayTextTable.setLockedWidth(false);
				displayTextTable.setSpacingAfter(10f);
				
				final PdfPTable financialInvoiceTable = new PdfPTable(12);
				financialInvoiceTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				financialInvoiceTable.setTotalWidth(822F);
				financialInvoiceTable.setLockedWidth(true);
				financialInvoiceTable.setSpacingAfter(736f);
				
				final PdfPCell resultsCell = new PdfPCell();			
				resultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
				resultsCell.setColspan(12);
				
				financialInvoiceTable.addCell(resultsCell);
				financialInvoiceTable.addCell("Order Type");
				financialInvoiceTable.addCell("Invoice No");
				financialInvoiceTable.addCell("Line Item");
				financialInvoiceTable.addCell("Customer PO No");
				financialInvoiceTable.addCell("Product Code");
				financialInvoiceTable.addCell("Product Description");
				financialInvoiceTable.addCell("Invoiced Qty");
				financialInvoiceTable.addCell("Status");
				financialInvoiceTable.addCell("Total Price");
				financialInvoiceTable.addCell("Paid Amount");
				financialInvoiceTable.addCell("Open Amount");
				financialInvoiceTable.addCell("Currency");

				final PdfPCell pdfCell = new PdfPCell();
				pdfCell.setFixedHeight(50f);
				
				if (null != dataMap)
				{
					Collection<JnjGTFinancePurchaseOrderReportResponseData> listJnjGTFinancePurchaseOrderReportResponseData= dataMap.values();
					for (final JnjGTFinancePurchaseOrderReportResponseData jnjGTFinancePurchaseOrderReportResponseData : listJnjGTFinancePurchaseOrderReportResponseData)
					{
					financialInvoiceTable
							.addCell(jnjGTFinancePurchaseOrderReportResponseData
									.getOrderType() == null ?  pdfCell : new PdfPCell(
											new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getOrderType())));
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getInvoiceNumber() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getInvoiceNumber())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getLineItem() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getLineItem())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getCustomerPONumber() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getCustomerPONumber())));
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getProductCode() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getProductCode())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getProductDescription() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getProductDescription())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getInvoiceQty() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getInvoiceQty())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getStatus() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getStatus())));
					
					/*financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getTotalPrice() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getTotalPrice())));*/
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getOpenAmount() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getOpenAmount())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getPaidAmount() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getPaidAmount())));
					
					financialInvoiceTable
					.addCell(jnjGTFinancePurchaseOrderReportResponseData
							.getCurrency() == null ?  pdfCell : new PdfPCell(
									new Phrase(jnjGTFinancePurchaseOrderReportResponseData.getCurrency())));
					
					}
					
					financialInvoiceTable.addCell(pdfCell);
					financialInvoiceTable.addCell(pdfCell);
					financialInvoiceTable.addCell(pdfCell);
					financialInvoiceTable.addCell(pdfCell);
					financialInvoiceTable.addCell(pdfCell);
					financialInvoiceTable.addCell(pdfCell);
						
				}
				arg1.add(imageTable);
				arg1.add(searchCriteriaTable);
				//arg1.add(Chunk.NEWLINE);
				//arg1.add(displayTextTable);
				//arg1.add(Chunk.NEWLINE);
				arg1.add(financialInvoiceTable);
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

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
import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialSummaryReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;
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

public class JnJGTFinancialSummaryReportPdfView extends AbstractPdfView{
	
	private static final Logger LOG = Logger.getLogger(JnJGTFinancialInvoiceDueReportPdfView.class);
	
	private static final String FINANCIAL_SUMMARY_ACCOUNT_AGINING_DATA_LIST = "accountAgingReportDatasList";
	private static final String FINANCIAL_BALANCE_SUMMARY_DATA_LIST = "balanceSummaryReportDatasList";
	private static final String FINANCIAL_PAYMENT_SUMMARY_DATA_LIST = "paymentSummaryReportDatasList";
	private static final String FINANCIAL_CREDIT_SUMMARY_DATA_LIST = "creditSummaryReportDatasList";
	protected static final int MARGIN = 32;
	private static final String FINANCIAL_SUMMARY_FORM_NAME = "JnjGlobalFinancialSummaryReportForm";
	private static final String sheetName = "FINANCIAL_SUMMARY REPORT RESULT";
	private static final String sheetName1 = "FINANCIAL_SUMMARY REPORT SEARCH CRITERIA";
	private static final String TEXT_ONE = "";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}
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
	}//Modified by Archana for AAOL-5513
	/**
	 * This method generates the PDF doc
	 */
	@SuppressWarnings("unchecked")
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
	       
			arg4.setHeader("Content-Disposition", "attachment; filename = Financial_Summary_Report.pdf");
			final List<JnjGTFinancialAccountAgingReportData> accountAgingReportDatasList = (List<JnjGTFinancialAccountAgingReportData>) arg0
					.get(FINANCIAL_SUMMARY_ACCOUNT_AGINING_DATA_LIST);
			final List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportDatasList = (List<JnjGTFinancialBalanceSummaryReportData>) arg0
					.get(FINANCIAL_BALANCE_SUMMARY_DATA_LIST);
			final List<JnjGTFinancialPaymentSummaryReportData>  paymentSummaryReportDatasList = (List<JnjGTFinancialPaymentSummaryReportData>) arg0
					.get(FINANCIAL_PAYMENT_SUMMARY_DATA_LIST);
			final List<JnjGTFinancialCreditSummaryReportData> creditSummaryReportDatasList = (List<JnjGTFinancialCreditSummaryReportData>) arg0
					.get(FINANCIAL_CREDIT_SUMMARY_DATA_LIST);
			
			final JnjGlobalFinancialSummaryReportForm financialSummaryForm = (JnjGlobalFinancialSummaryReportForm) arg0.get(FINANCIAL_SUMMARY_FORM_NAME);
			//final SimpleDateFormat dateFormatter = new SimpleDateFormat(messageSource.getMessage(DATE_FORMAT, null, i18nService.getCurrentLocale()));//Modified by Archana for AAOL-5513
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
	      	
			final PdfPTable searchCriteriaTable = new PdfPTable(8);
			searchCriteriaTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			searchCriteriaTable.setWidthPercentage(100);
			searchCriteriaTable.setLockedWidth(false);
			searchCriteriaTable.setSpacingAfter(10f);
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
			
			final PdfPCell headerCell = new PdfPCell();
			headerCell.setColspan(8);
		 	headerCell.setBorderWidthBottom(10f);
	       	headerCell.setBorderWidthTop(5f);
	       	headerCell.setBackgroundColor(Color.WHITE); 
	       	headerCell.setBorder(Rectangle.NO_BORDER);
	       	//headerCell.setPhrase(new Phrase(sheetName1,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			searchCriteriaTable.addCell(headerCell);
			
			searchCriteriaTable.addCell("Account Aging");
			searchCriteriaTable.addCell(financialSummaryForm.getAccountAgingPayerID());
			searchCriteriaTable.addCell("AR Balance Summary");
			searchCriteriaTable.addCell(financialSummaryForm.getBalanceSummaryPayerID());
			searchCriteriaTable.addCell("Payment Summary");
			searchCriteriaTable.addCell(financialSummaryForm.getPaymentSummaryPayerID());
			searchCriteriaTable.addCell("Credit Summary");
			searchCriteriaTable.addCell(financialSummaryForm.getCreditSummaryPayerID());
			
			final PdfPTable displayTextTable = new PdfPTable(4);
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
			
			final PdfPTable financialSummaryTable = new PdfPTable(4);
			financialSummaryTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			financialSummaryTable.setWidthPercentage(100);
			financialSummaryTable.setLockedWidth(false);
			financialSummaryTable.setSpacingAfter(736f);
			
			final PdfPTable displayTextTable1 = new PdfPTable(4);
			displayTextTable1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			displayTextTable1.setWidthPercentage(100);
			displayTextTable1.setLockedWidth(false);
			displayTextTable1.setSpacingAfter(10f);
			
			final PdfPCell txtCellDisplayCell1 = new PdfPCell();
			txtCellDisplayCell1.setColspan(10);
			txtCellDisplayCell1.setBackgroundColor(Color.WHITE); 
			txtCellDisplayCell1.setBorder(Rectangle.NO_BORDER);
			txtCellDisplayCell1.setPhrase(new Phrase(TEXT_ONE,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			displayTextTable1.addCell(txtCellDisplayCell1);
			
			final PdfPTable financialSummaryTable1 = new PdfPTable(4);
			financialSummaryTable1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			financialSummaryTable1.setWidthPercentage(100);
			financialSummaryTable1.setLockedWidth(false);
			financialSummaryTable1.setSpacingAfter(736f);
			
			final PdfPTable displayTextTable2 = new PdfPTable(10);
			displayTextTable2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			displayTextTable2.setWidthPercentage(100);
			displayTextTable2.setLockedWidth(false);
			displayTextTable2.setSpacingAfter(10f);
			
			final PdfPCell txtCellDisplayCell2 = new PdfPCell();
			txtCellDisplayCell2.setColspan(10);
			txtCellDisplayCell2.setBackgroundColor(Color.WHITE); 
			txtCellDisplayCell2.setBorder(Rectangle.NO_BORDER);
			txtCellDisplayCell2.setPhrase(new Phrase(TEXT_ONE,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			displayTextTable2.addCell(txtCellDisplayCell2);
			
			final PdfPTable financialSummaryTable2 = new PdfPTable(10);
			financialSummaryTable2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			financialSummaryTable2.setWidthPercentage(100);
			financialSummaryTable2.setLockedWidth(false);
			financialSummaryTable2.setSpacingAfter(736f);
			
			final PdfPTable displayTextTable3 = new PdfPTable(8);
			displayTextTable3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			displayTextTable3.setWidthPercentage(100);
			displayTextTable3.setLockedWidth(false);
			displayTextTable3.setSpacingAfter(10f);
			
			final PdfPCell txtCellDisplayCell3 = new PdfPCell();
			txtCellDisplayCell3.setColspan(10);
			txtCellDisplayCell3.setBackgroundColor(Color.WHITE); 
			txtCellDisplayCell3.setBorder(Rectangle.NO_BORDER);
			txtCellDisplayCell3.setPhrase(new Phrase(TEXT_ONE,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
			displayTextTable3.addCell(txtCellDisplayCell3);
			
			final PdfPTable financialSummaryTable3 = new PdfPTable(8);
			financialSummaryTable3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			financialSummaryTable3.setWidthPercentage(100);
			financialSummaryTable3.setLockedWidth(false);
			financialSummaryTable3.setSpacingAfter(736f);
			
			final PdfPCell headerResultsCell = new PdfPCell();
			headerResultsCell.setColspan(10);
			headerResultsCell.setBorderWidthBottom(10f);
		 	headerResultsCell.setBorderWidthTop(5f);
	       	headerResultsCell.setBackgroundColor(Color.WHITE); 
	       	headerResultsCell.setBorder(Rectangle.NO_BORDER);
	    	headerResultsCell.setPhrase(new Phrase(sheetName,new Font(Font.HELVETICA, 14, Font.BOLD,Color.decode("#0a8caa") )));
	    	
	    	final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);
	    	
			if (null != accountAgingReportDatasList)
			{
				financialSummaryTable.addCell(createLabelCell("Account Aging"));
		    	financialSummaryTable.addCell(createLabelCell("Days in Arrears"));
		    	financialSummaryTable.addCell(createLabelCell("Due Item"));
		    	financialSummaryTable.addCell(createLabelCell("Not Due"));
				for (final JnjGTFinancialAccountAgingReportData jnjGTFinancialAccountAgingReportData : accountAgingReportDatasList)
				{
					financialSummaryTable.addCell("");
					financialSummaryTable.addCell(jnjGTFinancialAccountAgingReportData.getDaysinArrears() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialAccountAgingReportData.getDaysinArrears())));
					financialSummaryTable.addCell(jnjGTFinancialAccountAgingReportData.getDueItem() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialAccountAgingReportData.getDueItem())));
					financialSummaryTable.addCell(jnjGTFinancialAccountAgingReportData.getNotDue() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialAccountAgingReportData.getNotDue())));
				}
			}
			
			if (null != balanceSummaryReportDatasList)
			{
				financialSummaryTable1.addCell(createLabelCell("AR Balance Summary"));
		    	financialSummaryTable1.addCell(createLabelCell("Due Date"));
		    	financialSummaryTable1.addCell(createLabelCell("Open Amount"));
		    	financialSummaryTable1.addCell(createLabelCell("Total"));
				for (final JnjGTFinancialBalanceSummaryReportData jnjGTFinancialBalanceSummaryReportData : balanceSummaryReportDatasList)
				{
					financialSummaryTable.addCell("");
					financialSummaryTable.addCell(jnjGTFinancialBalanceSummaryReportData.getDueDate() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialBalanceSummaryReportData.getDueDate())));
					financialSummaryTable.addCell(jnjGTFinancialBalanceSummaryReportData.getAmountPaid() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialBalanceSummaryReportData.getAmountPaid())));
					financialSummaryTable.addCell(jnjGTFinancialBalanceSummaryReportData.getTotal() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialBalanceSummaryReportData.getTotal())));
				}
			}
			
			
			if (null != paymentSummaryReportDatasList)
			{
				financialSummaryTable2.addCell(createLabelCell("Payment Summary"));
				financialSummaryTable2.addCell(createLabelCell("Amount Invoiced MTD"));
				financialSummaryTable2.addCell(createLabelCell("Net Amount Paid MTD"));
				financialSummaryTable2.addCell(createLabelCell("Amount Invoiced Prior Month"));
				financialSummaryTable2.addCell(createLabelCell("Net Amount Paid Prior Month"));
				financialSummaryTable2.addCell(createLabelCell("Amount Invoiced This Year"));
				financialSummaryTable2.addCell(createLabelCell("Net Amount Paid This Year"));
				financialSummaryTable2.addCell(createLabelCell("Amount Invoiced Prior Year"));
				financialSummaryTable2.addCell(createLabelCell("Net Amount Paid Prior Year"));
				financialSummaryTable2.addCell(createLabelCell("Last Payment Amount"));
				for (final JnjGTFinancialPaymentSummaryReportData jnjGTFinancialPaymentSummaryReportData : paymentSummaryReportDatasList)
				{
					financialSummaryTable.addCell("");
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedMTD() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedMTD())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidMTD() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidMTD())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorMonth() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorMonth())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorMonth() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorMonth())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPriorMonth() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPriorMonth())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getAmountInvoiceThisYear() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getAmountInvoiceThisYear())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidThisYear() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidThisYear())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorYear() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorYear())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPrioryear() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPrioryear())));
					financialSummaryTable.addCell(jnjGTFinancialPaymentSummaryReportData.getLastPaymentAmount() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialPaymentSummaryReportData.getLastPaymentAmount())));
					
				}
			}
			
			
			if (null != creditSummaryReportDatasList)
			{
				financialSummaryTable3.addCell(createLabelCell("Credit Summary"));
				financialSummaryTable3.addCell(createLabelCell("Open Order Value"));
				financialSummaryTable3.addCell(createLabelCell("Open Delivery Value"));
		    	financialSummaryTable3.addCell(createLabelCell("Amount Due"));
		    	financialSummaryTable3.addCell(createLabelCell("Credit Used"));
		    	financialSummaryTable3.addCell(createLabelCell("Credit Limit"));
		    	financialSummaryTable3.addCell(createLabelCell("Over/Under Value"));
		    	financialSummaryTable3.addCell(createLabelCell("Credit Limit Used (%)"));
				for (final JnjGTFinancialCreditSummaryReportData jnjGTFinancialCreditSummaryReportData : creditSummaryReportDatasList)
				{
					financialSummaryTable.addCell("");
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getOpenOrderValue() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getOpenOrderValue())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getOpenDeliveryValue() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getOpenDeliveryValue())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getAmountDue() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getAmountDue())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getCreditUsed() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getCreditUsed())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getCreditLimit() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getCreditLimit())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getOverUnderValue() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getOverUnderValue())));
					financialSummaryTable.addCell(jnjGTFinancialCreditSummaryReportData.getCreditLimitUsed() == null ? pdfCell : new PdfPCell(new Phrase(jnjGTFinancialCreditSummaryReportData.getCreditLimitUsed())));
				}
			}
			arg1.add(imageTable);
			arg1.add(searchCriteriaTable);
			//arg1.add(Chunk.NEWLINE);
			arg1.add(displayTextTable);
			arg1.add(displayTextTable1);
			arg1.add(displayTextTable2);
			arg1.add(displayTextTable3);
			//arg1.add(Chunk.NEWLINE);
			
			arg1.add(financialSummaryTable);
			arg1.add(financialSummaryTable1);
			arg1.add(financialSummaryTable2);
			arg1.add(financialSummaryTable3);
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

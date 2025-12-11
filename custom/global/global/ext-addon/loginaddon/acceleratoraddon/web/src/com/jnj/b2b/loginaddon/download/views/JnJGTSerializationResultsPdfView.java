package com.jnj.b2b.loginaddon.download.views;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSerialResponseData;
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
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author Cognizant
 * 
 * This class creates pdf view for serialization search results
 *
 */
public class JnJGTSerializationResultsPdfView extends AbstractPdfView {

	@Autowired
	protected I18NService i18nService;
	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	private MessageSource messageSource;
	
	private static final Logger LOG = Logger.getLogger(JnJGTSerializationResultsPdfView.class);
	private static final String SERIALIZATION_RESULTS_NAME = "SerializationResults";
	protected static final int MARGIN = 32;
	
	private static final String DOWNLOAD_DATE = "serialization.download.downloadDate";
	private static final String DOWNLOAD_TIME = "serialization.download.downloadTime";
	private static final String SEARCH_CRITERIA = "serialization.download.searchCeriteria";
	private static final String GTIN = "serialization.gtin";
	private static final String SERIAL_NUMBER = "serialization.serialNumber";
	private static final String BATCH_NUMBER = "serialization.batchNumber";
	private static final String EXPIRY_YEAR = "serialization.expiryYear";
	private static final String EXPIRY_MONTH = "serialization.expiryMonth";
	private static final String EXPIRY_DAY = "serialization.expiryDay";
	private static final String SERIAL_NUMBER_RESULT = "serialization.result.serialNumber";
	private static final String STATUS = "serialization.result.status";
	private static final String REASON = "serialization.result.reason";
	private static final String RESULTS = "serialization.result";
	
		
		@Override
		protected Document newDocument()
		{
			return new Document(PageSize.A4.rotate());
		}

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
				
				arg4.setHeader("Content-Disposition", "attachment; filename=Product_Serial_details.pdf");
				JnjGTSerialResponseData searchResults = (JnjGTSerialResponseData) arg0.get(SERIALIZATION_RESULTS_NAME); 
				
				
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
		      	
		      	
		      	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String date = simpleDateFormat.format(new Date());
				SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss z");
				String time = simpleTimeFormat.format(new Date());
				Font font = new Font(Font.HELVETICA, 10, Font.BOLD);
				Phrase DownloadDateHeaderPhrase = new Phrase(messageSource.getMessage(DOWNLOAD_DATE, null, i18nService.getCurrentLocale())+"		 :   ",font);
				Phrase DownloadDateHeaderValuePhrase = new Phrase(date + "\n");
				Phrase DownloadTimeHeaderPhrase = new Phrase(messageSource.getMessage(DOWNLOAD_TIME, null, i18nService.getCurrentLocale())+"		:   ",font );
				Phrase DownloadTimeHeaderValuePhrase = new Phrase(time + "\n"  + "\n");
				
				//Phrase searchCriteriaHeaderPhrase = new Phrase(messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale())+" :"  + "\n",font );
				Chunk searchCriteriaHeaderPhrase = new Chunk(messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale())+" :"  + "\n",font );
				searchCriteriaHeaderPhrase.setUnderline(0.1f, -2f);
				
				Phrase searchCriteriaGTINFieldPhrase = new Phrase(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale())+"                 :  "	 ,font);
				Phrase searchCriteriaGTINValuePhrase = new Phrase(searchResults.getGtin().trim()  + "\n");
				Phrase searchCriteriaSerialNumberFieldPhrase = new Phrase(messageSource.getMessage(SERIAL_NUMBER, null, i18nService.getCurrentLocale())+"  :  "	,font);
				Phrase searchCriteriaSerialNumberValuePhrase = new Phrase(searchResults.getInputSerialNumber().trim()+ "\n" );
				Phrase searchCriteriaBatchNumberFieldPhrase = new Phrase(messageSource.getMessage(BATCH_NUMBER, null, i18nService.getCurrentLocale())+"  : 	",font);
				Phrase searchCriteriaBatchNumberValuePhrase = new Phrase(searchResults.getBatchNumber().trim() + "\n" );
				Phrase searchCriteriaExpiryDayFieldPhrase = new Phrase(messageSource.getMessage(EXPIRY_DAY, null, i18nService.getCurrentLocale())+"        :  ",font);
				Phrase searchCriteriaExpiryDayValuePhrase = new Phrase(searchResults.getExpiryDay() + "\n" );
				Phrase searchCriteriaExpiryMonthFieldPhrase = new Phrase(messageSource.getMessage(EXPIRY_MONTH, null, i18nService.getCurrentLocale())+"    :  ",font );
				Phrase searchCriteriaExpiryMonthValuePhrase = new Phrase(searchResults.getExpiryMonth() + "\n");
				Phrase searchCriteriaExpiryYearFieldPhrase = new Phrase(messageSource.getMessage(EXPIRY_YEAR, null, i18nService.getCurrentLocale())+"       :  ",font);
				Phrase searchCriteriaExpiryYearValuePhrase = new Phrase(searchResults.getExpiryYear() + "\n" );
				
			
				Chunk searchResultsHeaderPhrase = new Chunk(messageSource.getMessage(RESULTS, null, i18nService.getCurrentLocale())+" :"  + "\n",font );
				searchResultsHeaderPhrase.setUnderline(0.1f, -2f);
				Phrase searchResultsSerialNumberFieldPhrase = new Phrase(messageSource.getMessage(SERIAL_NUMBER_RESULT, null, i18nService.getCurrentLocale())+"  :  ",font);
				Phrase searchResultsSerialNumberValuePhrase = new Phrase(searchResults.getSerialNumber()+"\n" );
				Phrase searchResultsStatusFieldPhrase = new Phrase(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale())+"            :  ",font);
				Phrase searchResultsStatusValuePhrase = new Phrase(searchResults.getStatus()+"\n" );
				//Changes for AAOl-6197
				Phrase searchResultsReasonValuePhrase = null;
				Phrase searchResultsReasonFieldPhrase = null;
				if(!searchResults.getStatus().equalsIgnoreCase(Jnjb2bCoreConstants.Serialization.KNOWN)){
					if(null!=searchResults.getReason()){						
						searchResultsReasonFieldPhrase =new Phrase(messageSource.getMessage(REASON, null, i18nService.getCurrentLocale())+"             :  ",font);
						
						searchResultsReasonValuePhrase =new Phrase( searchResults.getReason()+"\n" );
					}
				}			
				
				arg1.add(imageTable);
				arg1.add(Chunk.NEWLINE);
				arg1.add(Chunk.NEWLINE);
				arg1.add(DownloadDateHeaderPhrase);
				arg1.add(DownloadDateHeaderValuePhrase);
				arg1.add(DownloadTimeHeaderPhrase);
				arg1.add(DownloadTimeHeaderValuePhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchCriteriaHeaderPhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchCriteriaGTINFieldPhrase);
				arg1.add(searchCriteriaGTINValuePhrase);
				arg1.add(searchCriteriaSerialNumberFieldPhrase);
				arg1.add(searchCriteriaSerialNumberValuePhrase);
				arg1.add(searchCriteriaBatchNumberFieldPhrase);
				arg1.add(searchCriteriaBatchNumberValuePhrase);
				arg1.add(searchCriteriaExpiryDayFieldPhrase);
				arg1.add(searchCriteriaExpiryDayValuePhrase);
				arg1.add(searchCriteriaExpiryMonthFieldPhrase);
				arg1.add(searchCriteriaExpiryMonthValuePhrase);
				arg1.add(searchCriteriaExpiryYearFieldPhrase);
				arg1.add(searchCriteriaExpiryYearValuePhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchResultsHeaderPhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchResultsSerialNumberFieldPhrase);
				arg1.add(searchResultsSerialNumberValuePhrase);
				arg1.add(searchResultsStatusFieldPhrase);
				arg1.add(searchResultsStatusValuePhrase);
				if(null !=searchResultsReasonFieldPhrase && null!=searchResultsReasonValuePhrase){
					arg1.add(searchResultsReasonFieldPhrase);
					arg1.add(searchResultsReasonValuePhrase);
				}
				
				
			}
			catch (final Exception exception)
			{
				LOG.error("Error while creating PDF - " + exception.getMessage());
			}
		}
		
	// create cells
	protected PdfPCell createLabelCell(String text) {
		// font
		Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.GRAY);
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, font));// .toUpperCase()
		// set style
		labelCellStyle(cell);
		return cell;
	}

	// create cells
	protected PdfPCell createValueCell(String text) {
		// font
		Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		// set style
		valueCellStyle(cell);
		return cell;
	}

	public void headerCellStyle(PdfPCell cell) {
		// alignment
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// padding
		cell.setPaddingTop(0f);
		cell.setPaddingBottom(7f);
		// background color
		cell.setBackgroundColor(new Color(0, 121, 182));
		// border
		cell.setBorder(0);
		cell.setBorderWidthBottom(2f);

	}

	public void labelCellStyle(PdfPCell cell) {
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

	public void valueCellStyle(PdfPCell cell) {
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

	// ~ Inner Classes
	// ----------------------------------------------------------

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
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
						BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			} catch (DocumentException de) {
			} catch (IOException ioe) {
			}
		}

		// we override the onEndPage method
		public void onEndPage(PdfWriter writer, Document document) {
			int pageN = writer.getPageNumber();
			String text = messageSourceAccessor.getMessage("Page", "Page")
					+ " " + pageN + " "
					+ messageSourceAccessor.getMessage("of", "of") + " ";
			float len = bf.getWidthPoint(text, 8);
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
			template.showText(String.valueOf(writer.getPageNumber() - 1));
			template.endText();
		}
	}

	
	/**
	 * @param path
	 * @param align
	 * @return
	 * @throws Exception
	 */
	public static PdfPCell createImageCell(byte[] path, int align)
			throws Exception {
		Image img = Image.getInstance(path);
		PdfPCell cell = new PdfPCell(img, false);
		labelImageCellStyle(cell, align);
		return cell;
	}

	/**
	 * @param cell
	 * @param align
	 */
	public static void labelImageCellStyle(PdfPCell cell, int align) {
		// alignment
		if (align == 2) {
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		} else {
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		}
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorder(Rectangle.NO_BORDER);
	}

}


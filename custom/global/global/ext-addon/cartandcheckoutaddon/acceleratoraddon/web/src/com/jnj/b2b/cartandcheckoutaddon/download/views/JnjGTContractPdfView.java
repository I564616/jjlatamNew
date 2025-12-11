package com.jnj.b2b.cartandcheckoutaddon.download.views;


import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.facades.data.JnjContractData;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.lowagie.text.Chunk;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPageEventHelper;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * 
 * 
 */
public class JnjGTContractPdfView extends AbstractPdfView {

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

	protected static final Logger LOG = Logger.getLogger(JnjGTContractPdfView.class);
	protected static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	protected static final int MARGIN = 32;
	
	private static final String CONTRACT_CONTRACTNUMBER = "contract.number.header";
	private static final String CONTRACT_TENDERNUMBER = "contract.tendernumber.header";
	private static final String CONTRACT_INDIRECTCUSTOMER = "contract.indirectcustomer.number";
	private static final String CONTRACT_EXPIRATIONDATE = "contract.expiration.date";
	private static final String CONTRACT_CONTRACTTYPE = "contract.type";
	public static final String CONTRACT_LIST_FILENAME = "MyContracts";
	//Modified by Archana for AAOL-5513
	public static final String DATE_FORMAT = "date.dateformat";
	public static final String CONTRACT_LIST_TITLE = "MyContract List";
	public static final String CONTRACT_DETAIL_FILENAME = "contract.detail.name";
	public static final String CONTRACT_DETAIL_TITLE = "contract.details.header";
	
	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	protected Document newDocument()	{
		return new Document(PageSize.A4.rotate());
	}
	
	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter writer,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception	{
		try {
			//for page number added
			 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
			 writer.setPageEvent(events);   
	       events.onOpenDocument(writer, document);  
			//Modified by Archana for AAOL-5513
      	final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
      	final String fileName = messageSource.getMessage(CONTRACT_LIST_FILENAME, null, i18nService.getCurrentLocale()) + ".pdf";
      	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
      	final List<JnjContractData> results = (List<JnjContractData>) map.get("contractList");
      	//image adding start 
      	final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
      	final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
      	/*Image jnjConnectLogo = null;
      	Image jnjConnectLogo2 = null;
      	PdfPCell cell1 = null;
      	PdfPCell cell2 = null;*/
      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      	imageTable.setWidthPercentage(100);
      	imageTable.setLockedWidth(false);
      	imageTable.setSpacingAfter(30f);
      	
      	if ( jnjConnectLogoByteArray != null){
      		/*jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
      		cell1 = new PdfPCell(jnjConnectLogo, false);
      		cell1.setBorder(Rectangle.NO_BORDER);*/
      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray,Element.ALIGN_LEFT));
      	}
      	
      	if ( jnjConnectLogoByteArray2 != null){
      		/*jnjConnectLogo2 = Image.getInstance(jnjConnectLogoByteArray2);
      		cell2 = new PdfPCell(jnjConnectLogo2, false);
      		cell2.setBorder(Rectangle.NO_BORDER);*/
      		imageTable.addCell(createImageCell(jnjConnectLogoByteArray2,Element.ALIGN_RIGHT));
      	}
      	/*imageTable.addCell(cell1);
      	imageTable.addCell(cell2);*/
      	//image adding end
      	final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
      	final boolean isMddSite = (currentSite != null && Jnjb2bCoreConstants.MDD.equals(currentSite)) ? true : false;
      	//Header part start
      	final PdfPTable headerTable = new PdfPTable(1);
      	headerTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      	headerTable.setWidthPercentage(100);
      	headerTable.setLockedWidth(false);
       	headerTable.setSpacingAfter(10f);
       	
       	final PdfPCell headerCell = new PdfPCell();
       	headerCell.setColspan(5);
       	headerCell.setBorderWidthBottom(10f);
       	headerCell.setBorderWidthTop(5f);
       	headerCell.setBackgroundColor(Color.WHITE); 
       	headerCell.setBorder(Rectangle.NO_BORDER);
       	headerCell.setPhrase(new Phrase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_LIST_TITLE, 
       			new Font(Font.HELVETICA, 14, Font.BOLD,Color.CYAN)));
         headerTable.addCell(headerCell);
      	//Header part end
         //contract list construct start
         //creating contract column header
      	String[] headers = new String[] {
      			
      			 messageSource.getMessage(CONTRACT_CONTRACTNUMBER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_TENDERNUMBER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_EXPIRATIONDATE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_CONTRACTTYPE, null, i18nService.getCurrentLocale()),
				 
      			};
      	
      	final PdfPTable orderTable = new PdfPTable(headers.length);
      	orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      	orderTable.setWidthPercentage(100);
      	orderTable.setLockedWidth(false);
      	orderTable.setSpacingAfter(736f);
      	
      	for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            orderTable.addCell(createLabelCell(header.toUpperCase()));
        }
      	orderTable.completeRow();
      
      	final String emptyField = new String();
      	if (results != null && !results.isEmpty()) {
      		//PdfPCell dataCell = new PdfPCell();
      		for (final JnjContractData data : results) {
      			orderTable.addCell(createValueCell(data.getEccContractNum()));
      			orderTable.addCell(createValueCell(data.getTenderNum()));
      			orderTable.addCell(createValueCell(data.getIndirectCustomer()));
      			orderTable.addCell(createValueCell((data.getEndDate() != null) ? dateFormat.format(data.getEndDate()).toString() : emptyField));
      			orderTable.addCell(createValueCell(data.getContractOrderReason()));
      			orderTable.completeRow();
      		}
      	}
      	
      	document.add(imageTable);
      	document.add(headerTable);
      	document.add(Chunk.NEWLINE);
      	document.add(orderTable);
      }
      catch (final Exception e) {
      	e.printStackTrace();
      	LOG.error("error while setting table - " + e.getMessage());
      }
	}
	
		//create cells
	protected PdfPCell createLabelCell(String text){
	      // font
	      Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.GRAY);
	      // create cell
	      PdfPCell cell = new PdfPCell(new Phrase(text.toUpperCase(),font));
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

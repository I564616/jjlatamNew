package com.jnj.b2b.browseandsearch.download.views;


import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.core.util.JnJCommonUtil;
/*import com.jnj.core.constants.Jnjb2bcoreConstants;*/
import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.facades.data.JnjGTProductData;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * Class responsible to create PDF view for Search Result page.
 * 
 * @author Accenture
 * 
 */
public class JnjProductSearchResultPdfView extends AbstractPdfView
{
	protected static final String RESULT_LIMIT_EXCEEDED_MESSAGE = "More than 1,000 products matched your search results and the first 1,000 have been included in this file. Please refer to the site FAQ for instruction on how to download a full product catalog.";
	/**
	 * Private instance of <code>JnjCommonFacadeUtil</code>
	 */
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	protected static final int MARGIN = 32;
	protected static final String sheetName = "Product-Catalog(MDD)";
	
	private static final String DISCLAIMER = "Product.search.disclaimer";
	private static final String RESULTS = "Product.search.category.results";
	private static final String DOWNLOAD = "Product.search.download";
	private static final String ACC_NUMBER = "Product.search.account.number";
	private static final String PRODUCT_CODE = "Product.search.product.code";
	private static final String GTIN = "Product.search.product.gtin";
	private static final String PRODUCT_DESC = "Product.search.product.description";
	private static final String PRICE = "Product.search.price";
	private static final String UPC = "Product.search.upc";
	private static final String MEASURE = "Product.search.unit.measure";
	private static final String SHIP_NAME = "Product.search.ship.name";
	
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

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		arg4.setHeader("Content-Disposition", "attachment; filename=ProductSearchResult.pdf");
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = (ProductSearchPageData<SearchStateData, ProductData>) map
				.get("searchPageData");
		final List<ProductData> results = searchPageData.getResults();
		final Boolean isMddSite = (map.get("isMddSite") != null) ? (Boolean) map.get("isMddSite") : Boolean.FALSE;
		final String currentAccount = (String) map.get("currentAccount");
		final String currentAccountName = (String) map.get("currentAccountName");
		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");

		final boolean displayPrice = (searchPageData.getPagination() != null && searchPageData.getPagination()
				.getTotalNumberOfResults() > 200) ? false : true;
		final String pricingDisclaimer = displayPrice ? getJnjCommonFacadeUtil().getMessageFromImpex(
				Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.PRICING_DISCLAIMER_MSG) : getJnjCommonFacadeUtil()
				.getMessageFromImpex(Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.NO_PRICING_DISCLAIMER_MSG);

		final boolean isSearchResDownload = (map.get(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG) != null) ? (Boolean) map
				.get(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG) : false;

		final Font boldFont = new Font(Font.BOLD);

		if (resultLimitExceeded.booleanValue())
		{
			document.add(new Paragraph(RESULT_LIMIT_EXCEEDED_MESSAGE));
			document.add(new Paragraph("\n"));
		}

		final PdfPTable productsTable = new PdfPTable(4);
		productsTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		productsTable.setTotalWidth(822F);
		productsTable.setLockedWidth(true);
		productsTable.setSpacingAfter(30f);

		final Date currentDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final String searchResultText = (isSearchResDownload) ? searchPageData.getFreeTextSearch() : searchPageData
				.getCategoryCode();
		//JJEPIC-551
		final PdfPTable searchResultTable = new PdfPTable((displayPrice ? 8 : 7));
		searchResultTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		searchResultTable.setTotalWidth(822F);
		searchResultTable.setLockedWidth(true);
		searchResultTable.setSpacingAfter(30f);

		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()), boldFont)));
		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()), boldFont)));
		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale()), boldFont)));
		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()), boldFont)));
		if (displayPrice)
		{
			searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(PRICE, null, i18nService.getCurrentLocale()), boldFont)));
		}
		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(UPC, null, i18nService.getCurrentLocale()), boldFont)));
		searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(MEASURE, null, i18nService.getCurrentLocale()), boldFont)));
		// page number add start here
				 MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());   
				 pdfWriter.setPageEvent(events);   
		                 events.onOpenDocument(pdfWriter, document);
			   
			   //page number add end here
		
		//image adding start 
	      	final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
	      	final InputStream jnjConnectLogoIS2 = (InputStream) map.get("jnjConnectLogoURL2");
	      	byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
	      	byte[] jnjConnectLogoByteArray2 = IOUtils.toByteArray(jnjConnectLogoIS2);
	      	Image jnjConnectLogo = null;
	      	Image jnjConnectLogo2 = null;
	      	PdfPCell cell1 = null;
	      	PdfPCell cell2 = null;
	      	PdfPTable imageTable = new PdfPTable(2); // 3 columns.
	      	imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
	      	imageTable.setTotalWidth(822F);
	      	imageTable.setLockedWidth(true);
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
		
		//JJEPIC-551
	
			searchResultTable.addCell(new PdfPCell(new Phrase(messageSource.getMessage(SHIP_NAME, null, i18nService.getCurrentLocale()), boldFont)));
		

		final String emptyField = new String();
		for (final ProductData productData : results)
		{
			final JnjGTProductData data = (JnjGTProductData) productData;

			searchResultTable.addCell(currentAccount);
			searchResultTable.addCell(data.getBaseMaterialNumber());
			searchResultTable.addCell(data.getGtin());
			searchResultTable.addCell(data.getDescription());
			if (displayPrice)
			{
				searchResultTable.addCell(data.getListPrice());
			}
			searchResultTable.addCell(data.getUpc());
			searchResultTable.addCell((data.getDeliveryUnit() != null) ? data.getDeliveryUnit() : emptyField + "("
					+ ((data.getNumerator() != null) ? data.getNumerator() : emptyField) + " "
					+ ((data.getSalesUnit() != null) ? data.getSalesUnit() : emptyField) + ")");

			//TODO [AR]: Clarification required for 'Ship To name' value, blank until received.			
			
			//JJEPIC-551
			
			searchResultTable.addCell(currentAccountName);
				//searchResultTable.addCell((data.getStatus() != null) ? data.getStatus().toString() : emptyField);
			 
		}
		document.add(imageTable);
		final Paragraph header = new Paragraph("PRODUCT SEARCH RESULTS", boldFont);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph(header));
		//document.add(new Paragraph("\n"));
		//document.add(new Paragraph("\n"));
		document.add(new Paragraph("Results for:\t" + searchResultText));
		document.add(new Paragraph("Download Date:\t " + sdf.format(currentDate)));
		document.add(new Paragraph("*" + pricingDisclaimer));
		document.add(new Paragraph("\n"));

		//document.add(productsTable);
		
		document.add(searchResultTable);
	}

	/**
	 * @return the jnjCommonFacadeUtil
	 */
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
	{
		return jnjCommonFacadeUtil;
	}

	/**
	 * @param jnjCommonFacadeUtil
	 *           the jnjCommonFacadeUtil to set
	 */
	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
	{
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}
	 
	 

private static class MyPageEvents extends PdfPageEventHelper {   
	 	
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

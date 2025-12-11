/**
 *
 */
package com.jnj.la.browseandsearch.download.views;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTProductData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import org.apache.commons.io.IOUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JnjLatamProductSearchResultPdfView extends AbstractPdfView {

	/**
	 * Private instance of <code>JnjCommonFacadeUtil</code>
	 */
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	protected static final int MARGIN = 32;

	@Override
	protected Document newDocument() {
		return new Document(PageSize.A4.rotate());
	}

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception {
		arg4.setHeader("Content-Disposition", "attachment; filename=ProductSearchResult.pdf");
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = (ProductSearchPageData<SearchStateData, ProductData>) map
				.get("searchPageData");
		final String RESULT_LIMIT_EXCEEDED_MESSAGE = getJnjCommonFacadeUtil()
				.getMessageFromImpex("search.result.limit.exceededmessage");
		final List<ProductData> results = searchPageData.getResults();
		final Boolean isMddSite = (map.get("isMddSite") != null) ? (Boolean) map.get("isMddSite") : Boolean.FALSE;
		final String currentAccount = (String) map.get("currentAccount");
		final String currentAccountName = (String) map.get("currentAccountName");
		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");

		final boolean displayPrice = (searchPageData.getPagination() != null
				&& searchPageData.getPagination().getTotalNumberOfResults() > 200) ? false : true;
		final String pricingDisclaimer = displayPrice
				? getJnjCommonFacadeUtil().getMessageFromImpex(
						Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.PRICING_DISCLAIMER_MSG)
				: getJnjCommonFacadeUtil().getMessageFromImpex(
						Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.NO_PRICING_DISCLAIMER_MSG);

		final boolean isSearchResDownload = (map
				.get(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG) != null)
						? (Boolean) map.get(Jnjb2bbrowseandsearchConstants.Product.SEARCH_RES_DOWNLOAD_FLAG) : false;

		final Font boldFont = new Font(Font.BOLD);

		if (resultLimitExceeded.booleanValue()) {
			document.add(new Paragraph(RESULT_LIMIT_EXCEEDED_MESSAGE));
			document.add(new Paragraph("\n"));
		}

		final PdfPTable productsTable = new PdfPTable(4);
		productsTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		productsTable.setTotalWidth(822F);
		productsTable.setLockedWidth(true);
		productsTable.setSpacingAfter(30f);

		final Date currentDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String searchResultText = (isSearchResDownload) ? searchPageData.getFreeTextSearch()
				: searchPageData.getCategoryCode();

		final PdfPTable searchResultTable = new PdfPTable(4);
		searchResultTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		searchResultTable.setTotalWidth(822F);
		searchResultTable.setLockedWidth(true);
		searchResultTable.setSpacingAfter(30f);

		searchResultTable.addCell(new PdfPCell(new Phrase(
				getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.accountnumber"), boldFont)));
		searchResultTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("cart.review.productCodes"), boldFont)));
		searchResultTable.addCell(new PdfPCell(
				new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.pdfuom"), boldFont)));
		// page number add start here
		final MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
		pdfWriter.setPageEvent(events);
		events.onOpenDocument(pdfWriter, document);

		// page number add end here

		// image adding start
		final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
		final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
		Image jnjConnectLogo = null;
		PdfPCell cell1 = null;
		final PdfPTable imageTable = new PdfPTable(1); // 3 columns.
		imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		imageTable.setTotalWidth(822F);
		imageTable.setLockedWidth(true);
		imageTable.setSpacingAfter(30f);

		if (jnjConnectLogoByteArray != null) {
			jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
			cell1 = new PdfPCell(jnjConnectLogo, false);
			cell1.setBorder(Rectangle.NO_BORDER);
		}

		imageTable.addCell(cell1);
		// image adding end

		searchResultTable.addCell(new PdfPCell(new Phrase(
				getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.shiptoname"), boldFont)));

		final String emptyField = new String();
		for (final ProductData productData : results) {
			final JnjGTProductData data = (JnjGTProductData) productData;

			searchResultTable.addCell(currentAccount);
			searchResultTable.addCell(data.getBaseMaterialNumber());
			searchResultTable.addCell((data.getDeliveryUnit() != null) ? data.getDeliveryUnit()
					: emptyField + "(" + ((data.getNumerator() != null) ? data.getNumerator() : emptyField) + " "
							+ ((data.getSalesUnit() != null) ? data.getSalesUnit() : emptyField) + ")");

			searchResultTable.addCell(currentAccountName);

		}
		document.add(imageTable);
		final Paragraph header = new Paragraph(
				getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.pdfheader"), boldFont);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph(header));
		document.add(
				new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.results.header")
						+ searchResultText));
		document.add(new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.downloaddate")
				+ sdf.format(currentDate)));
		document.add(new Paragraph("*" + pricingDisclaimer));
		document.add(new Paragraph("\n"));
		document.add(searchResultTable);
	}

	/**
	 * @return the jnjCommonFacadeUtil
	 */
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	/**
	 * @param jnjCommonFacadeUtil
	 *            the jnjCommonFacadeUtil to set
	 */
	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

	private static class MyPageEvents extends PdfPageEventHelper {

		protected MessageSourceAccessor messageSourceAccessor;

		protected PdfContentByte cb;

		protected PdfTemplate template;

		protected BaseFont bf = null;

		public MyPageEvents(final MessageSourceAccessor messageSourceAccessor) {
			this.messageSourceAccessor = messageSourceAccessor;
		}
		@Override
		public void onOpenDocument(final PdfWriter writer, final Document document) {
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			} catch (final DocumentException de) {
			} catch (final IOException ioe) {
			}
		}
		@Override
		public void onEndPage(final PdfWriter writer, final Document document) {
			final int pageN = writer.getPageNumber();
			final String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " "
					+ messageSourceAccessor.getMessage("on", "on") + " ";
			final float len = bf.getWidthPoint(text, 8);
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

		@Override
		public void onCloseDocument(final PdfWriter writer, final Document document) {
			template.beginText();
			template.setFontAndSize(bf, 8);
			template.showText(String.valueOf(writer.getPageNumber() - 1));
			template.endText();
		}
	}

}

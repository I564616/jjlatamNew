/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTTemplateDetailsForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author plahiri1
 */


public class JnJLatamOrderTemplateDetailPdfView extends AbstractPdfView {

	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	private static final String DATA_LIST = "orderTemplateDetail";
	private static final String DATA_LIST_TEMP = "templateEditForm";
	private static final int MARGIN = 32;

	@Override
	protected Document newDocument() {
		return new Document(PageSize.A4.rotate());
	}

	/**
	 * This method generates the PDF doc
	 */
	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception {
		try {
			arg4.setHeader("Content-Disposition", "attachment; filename=Template_Details.pdf");
			final List<JnjGTOrderTemplateEntryData> orderTemplateDatas = (List<JnjGTOrderTemplateEntryData>) map
					.get(DATA_LIST);
			final JnjGTTemplateDetailsForm templateEditForm = (JnjGTTemplateDetailsForm) map.get(DATA_LIST_TEMP);
			final PdfPTable templateDetailTable = new PdfPTable(6);
			templateDetailTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			templateDetailTable.setTotalWidth(822F);
			templateDetailTable.setLockedWidth(true);
			templateDetailTable.setSpacingAfter(736f);
			templateDetailTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productName"));
			templateDetailTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productCode"));
			templateDetailTable
					.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productDescription"));
			templateDetailTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productVolume"));
			templateDetailTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productWeight"));
			templateDetailTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productQuantity"));

			final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setTotalWidth(822F);
			titleTable.setLockedWidth(true);
			titleTable.setSpacingAfter(30f);

			final PdfPCell headerCell = new PdfPCell(
					new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.pdfDetailsHeader")));
			headerCell.setColspan(11);
			titleTable.addCell(headerCell);
			final PdfPCell headerCell2 = new PdfPCell(new Phrase(templateEditForm.getTemplateName()));
			headerCell.setColspan(11);
			titleTable.addCell(headerCell2);
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
			final PdfPTable imageTable = new PdfPTable(1);
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

			final PdfPCell pdfCell = new PdfPCell();
			pdfCell.setFixedHeight(50f);

			if (null != orderTemplateDatas) {
				for (final JnjGTOrderTemplateEntryData orderTemplateData : orderTemplateDatas) {
					final String deliveryUnit = orderTemplateData.getRefVariant().getDeliveryUnit() == null
							? Jnjb2bCoreConstants.SPACE : orderTemplateData.getRefVariant().getDeliveryUnit();
					final String numerator = orderTemplateData.getRefVariant().getNumerator()
							+ Jnjb2bCoreConstants.SPACE + orderTemplateData.getRefVariant().getSalesUnit() == null
									? Jnjb2bCoreConstants.SPACE
									: orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
											+ orderTemplateData.getRefVariant().getSalesUnit();

					templateDetailTable.addCell(orderTemplateData.getRefVariant().getName() == null ? pdfCell
							: new PdfPCell(new Phrase(orderTemplateData.getRefVariant().getName())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getCode() == null ? pdfCell
							: new PdfPCell(new Phrase(orderTemplateData.getRefVariant().getCode())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getDescription() == null ? pdfCell
							: new PdfPCell(new Phrase(orderTemplateData.getRefVariant().getDescription())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getProductVolume() == null ? pdfCell
							: new PdfPCell(new Phrase(orderTemplateData.getRefVariant().getProductVolume())));
					templateDetailTable.addCell(orderTemplateData.getRefVariant().getProductWeight() == null ? pdfCell
							: new PdfPCell(new Phrase(orderTemplateData.getRefVariant().getProductWeight())));
					if (StringUtils.isEmpty(numerator)) {
						templateDetailTable.addCell(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
								+ orderTemplateData.getRefVariant().getDeliveryUnit() == null ? pdfCell
										: new PdfPCell(new Phrase(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
												+ orderTemplateData.getRefVariant().getDeliveryUnit())));
					} else {
						templateDetailTable.addCell(
								orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + deliveryUnit == null ? pdfCell
										: new PdfPCell(new Phrase(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
												+ deliveryUnit + "(" + numerator + ")")));
					}

				}
			}
			document.add(imageTable);
			document.add(titleTable);

			document.add(templateDetailTable);
		} catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage("Build PDF Document", "buildPdfDocument", "Error while creating PDF - ",
					exception, JnJLatamOrderTemplateDetailPdfView.class);
		}
	}

	private static class MyPageEvents extends PdfPageEventHelper {

		private final MessageSourceAccessor messageSourceAccessor;

		private PdfContentByte cb;

		private PdfTemplate template;

		private BaseFont bf = null;

		public MyPageEvents(final MessageSourceAccessor messageSourceAccessor) {
			this.messageSourceAccessor = messageSourceAccessor;
		}

		public void onOpenDocument(final PdfWriter writer, final Document document) {
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			} catch (final DocumentException de) {
				JnjGTCoreUtil.logErrorMessage("DocumentException", "onOpenDocument()", "Error while creating PDF - ",
						de, JnJLatamOrderTemplateDetailPdfView.class);
			} catch (final IOException ioe) {
				JnjGTCoreUtil.logErrorMessage("IOException", "onOpenDocument()", "Error while opening PDF - ", ioe,
						JnJLatamOrderTemplateDetailPdfView.class);
			}
		}

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

		public void onCloseDocument(final PdfWriter writer, final Document document) {
			template.beginText();
			template.setFontAndSize(bf, 8);
			template.showText(String.valueOf(writer.getPageNumber() - 1));
			template.endText();
		}
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

}

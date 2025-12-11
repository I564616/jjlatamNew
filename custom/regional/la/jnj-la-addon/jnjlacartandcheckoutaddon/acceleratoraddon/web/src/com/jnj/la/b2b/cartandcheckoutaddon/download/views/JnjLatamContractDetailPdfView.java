/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.download.views;

import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractPriceData;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Font;
import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractPriceData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

/**
 * @author plahiri1
 *
 */
public class JnjLatamContractDetailPdfView extends AbstractPdfView
{

	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

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

	protected static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	protected static final int MARGIN = 32;
	private static final String TEXT_PRODUCT_NAME ="text.product.name";
	private static final String CONTRACT_ITEM_PRICE="contract.itemPrice";

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	protected Document newDocument()
	{
		return new Document(PageSize.A4.rotate());
	}

	@Override
	protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		try
		{

			final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			final SimpleDateFormat dateFormatHHMMSS = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ getJnjCommonFacadeUtil().getMessageFromImpex("text.contract.pdfexcel.details") + ".pdf");

			final JnjContractData contractData = (JnjContractData) map.get("contractData");
			final List<JnjContractEntryData> jnjContractEntryDataList = (List<JnjContractEntryData>) map.get("contractEntryList");
			final Map<String, JnjContractPriceData> cntrctEntryMap = (Map<String, JnjContractPriceData>) map.get("cntrctEntryMap");
			final JnjContractPriceData cntrctPriceData = (JnjContractPriceData) map.get("cntrctPriceData");
			// page number add start here
			final MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
			pdfWriter.setPageEvent(events);
			events.onOpenDocument(pdfWriter, document);

			//page number add end here
			//image adding start
			final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
			final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
			final PdfPTable imageTable = new PdfPTable(1); // 3 columns.
			imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			imageTable.setWidthPercentage(100);
			imageTable.setLockedWidth(false);
			imageTable.setSpacingAfter(30f);

			if (jnjConnectLogoByteArray != null)
			{

				imageTable.addCell(createImageCell(jnjConnectLogoByteArray, Element.ALIGN_LEFT));
			}


			final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);
			final boolean isMddSite = (currentSite != null && Jnjb2bCoreConstants.MDD.equals(currentSite)) ? true : false;
			//title adding start
			final PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			titleTable.setWidthPercentage(100);
			titleTable.setLockedWidth(false);
			titleTable.setSpacingAfter(10f);

			final PdfPCell titleCell = new PdfPCell();
			titleCell.setColspan(6);
			titleCell.setBorderWidthBottom(10f);
			titleCell.setBorderWidthTop(5f);
			titleCell.setBackgroundColor(Color.WHITE);
			titleCell.setBorder(Rectangle.NO_BORDER);
			titleCell.setPhrase(new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.contract.pdfexcel.details"),
					new Font(Font.HELVETICA, 14, Font.BOLD, Color.CYAN)));
			titleTable.addCell(titleCell);

			//title adding end

			final String[] headers_CONTRACT_ZCQ = new String[]
			{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
				getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.unitOfMeasure"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractQty"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumed"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.balance") };

			final String[] headers_CONTRACT_ZCV = new String[]
			{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
					getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.totalValue"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumedValue"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractBalance") };

			final String[] headersContractZCI = new String[]
			{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
					getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.unitOfMeasure"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractQty"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumed"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.balance") };
			// create 6 column table
			PdfPTable orderTable = null;
			if (contractData.getDocumentType()
					.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCQ))
			{
				orderTable = new PdfPTable(headers_CONTRACT_ZCQ.length);
				orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderTable.setWidthPercentage(100);
				orderTable.setLockedWidth(false);
				orderTable.setSpacingAfter(736f);
				for (int i = 0; i < headers_CONTRACT_ZCQ.length; i++)
				{
					final String header = headers_CONTRACT_ZCQ[i];
					orderTable.addCell(createLabelCell(header.toUpperCase(), 0, 0));
				}
			}

			if (contractData.getDocumentType()
					.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV))
			{
				orderTable = new PdfPTable(headers_CONTRACT_ZCV.length);
				orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderTable.setWidthPercentage(100);
				orderTable.setLockedWidth(false);
				orderTable.setSpacingAfter(736f);
				for (int i = 0; i < headers_CONTRACT_ZCV.length; i++)
				{
					final String header = headers_CONTRACT_ZCV[i];
					orderTable.addCell(createLabelCell(header.toUpperCase(), 0, 0));
				}
			}

			if (Jnjlab2bcoreConstants.Contract.CONTRACT_ZCI.equalsIgnoreCase(contractData.getDocumentType()))
			{
				orderTable = new PdfPTable(headersContractZCI.length);
				orderTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderTable.setWidthPercentage(100);
				orderTable.setLockedWidth(false);
				orderTable.setSpacingAfter(736f);
				for (int i = 0; i < headersContractZCI.length; i++)
				{
					final String header = headersContractZCI[i];
					orderTable.addCell(createLabelCell(header.toUpperCase(), 0, 0));
				}
			}
			orderTable.completeRow();

			final String emptyField = new String();
			if (jnjContractEntryDataList != null && !jnjContractEntryDataList.isEmpty())
			{
				for (final JnjContractEntryData jnjContractEntryData : jnjContractEntryDataList)
				{
					orderTable.addCell(
							createValueCell(jnjContractEntryData.getProductCode() + "\n" + jnjContractEntryData.getProductName(), 0, 0));
					orderTable.addCell(createValueCell(jnjContractEntryData.getContractProductPrice(), 0, 0));
					if (contractData.getDocumentType()
							.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCQ))
					{
						orderTable.addCell(createValueCell(jnjContractEntryData.getProdSalesUnit() != null
								? jnjContractEntryData.getProdSalesUnit().toString() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getContractQty() != null
								? jnjContractEntryData.getContractQty().toString() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getConsumedQty() != null
								? jnjContractEntryData.getConsumedQty().toString() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getContractBalanceQty() != null
								? jnjContractEntryData.getContractBalanceQty().toString() : StringUtils.EMPTY, 0, 0));
					}

					if (contractData.getDocumentType()
							.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV))
					{
						final String prodCode = jnjContractEntryData.getProductCode();
						final JnjContractPriceData jnjContractPriceData = cntrctEntryMap.get(prodCode);
						if (jnjContractPriceData != null)
						{
							orderTable.addCell(createValueCell(jnjContractPriceData.getTotalAmount().getValue() != null
									? jnjContractPriceData.getTotalAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
							orderTable.addCell(createValueCell(jnjContractPriceData.getConsumedAmount().getValue() != null
									? jnjContractPriceData.getConsumedAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
							orderTable.addCell(createValueCell(jnjContractPriceData.getBalanceAmount().getValue() != null
									? jnjContractPriceData.getBalanceAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
						}
					}

					if (Jnjlab2bcoreConstants.Contract.CONTRACT_ZCI.equalsIgnoreCase(contractData.getDocumentType()))
					{
						orderTable.addCell(createValueCell(jnjContractEntryData.getProdSalesUnit() != null
								? jnjContractEntryData.getProdSalesUnit() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getContractQty() != null
								? jnjContractEntryData.getContractQty().toString() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getConsumedQty() != null
								? jnjContractEntryData.getConsumedQty().toString() : StringUtils.EMPTY, 0, 0));
						orderTable.addCell(createValueCell(jnjContractEntryData.getContractBalanceQty() != null
								? jnjContractEntryData.getContractBalanceQty().toString() : StringUtils.EMPTY, 0, 0));
					}
				}
			}
			final PdfPTable contractInfo = new PdfPTable(6);
			contractInfo.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			contractInfo.setWidthPercentage(100);
			contractInfo.setLockedWidth(false);
			contractInfo.setSpacingAfter(30f);
			//first row
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONTRACTNUMBER), 0, 0));
			contractInfo.addCell(createValueCell(contractData.getEccContractNum(), 5, 0));
			///second row
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_INDIRECTCUSTOMER), 0, 0));
			contractInfo.addCell(createValueCell(
					contractData.getIndirectCustomer() != null ? contractData.getIndirectCustomer() : StringUtils.EMPTY, 0, 0));
			contractInfo.addCell(
					createLabelCell(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_STATUS), 0, 0));
			contractInfo
					.addCell(createValueCell(contractData.getStatus() != null ? contractData.getStatus() : StringUtils.EMPTY, 0, 0));
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CREATION_DATE), 0, 0));
			contractInfo.addCell(createValueCell(contractData.getStartDate() != null
					? dateFormat.format(contractData.getStartDate()).toString() : StringUtils.EMPTY, 0, 0));
			///third row
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_INDIRECTCUSTOMER_NAME), 0, 0));
			contractInfo.addCell(createValueCell(
					contractData.getIndirectCustomerName() != null ? contractData.getIndirectCustomerName() : StringUtils.EMPTY, 0,
					0));
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_TENDERNUMBER), 0, 0));
			contractInfo.addCell(
					createValueCell(contractData.getTenderNum() != null ? contractData.getTenderNum() : StringUtils.EMPTY, 0, 0));
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_EXPIRATIONDATE), 0, 0));
			contractInfo.addCell(createValueCell(
					contractData.getEndDate() != null ? dateFormat.format(contractData.getEndDate()).toString() : StringUtils.EMPTY, 0,
					0));
			//fourth row
			contractInfo.addCell(createValueCell(emptyField, 2, 0));
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONTRACTTYPE), 0, 0));
			contractInfo.addCell(createValueCell(
					contractData.getContractOrderReason() != null ? contractData.getContractOrderReason() : StringUtils.EMPTY, 0, 0));
			contractInfo.addCell(createLabelCell(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_LAST_TIME_UPDATED), 0, 0));
			contractInfo.addCell(createValueCell(contractData.getLastUpdatedTime() != null
					? dateFormatHHMMSS.format(contractData.getLastUpdatedTime()).toString() : StringUtils.EMPTY, 0, 0));

			if (contractData.getDocumentType()
					.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV))
			{
				if (cntrctPriceData != null)
				{
					//fifth row
					contractInfo.addCell(createValueCell(emptyField, 4, 0));
					contractInfo.addCell(createLabelCell(
							getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_TOTAL_CONTRACT_VALUE), 0, 0));
					contractInfo.addCell(createValueCell(cntrctPriceData.getTotalAmount().getValue() != null
							? cntrctPriceData.getTotalAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
					//sixth row
					contractInfo.addCell(createValueCell(emptyField, 4, 0));
					contractInfo.addCell(createLabelCell(
							getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONSUMED_VALUE), 0, 0));
					contractInfo.addCell(createValueCell(cntrctPriceData.getConsumedAmount().getValue() != null
							? cntrctPriceData.getConsumedAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
					//seventh row
					contractInfo.addCell(createValueCell(emptyField, 4, 0));
					contractInfo.addCell(createLabelCell(
							getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_BALANCE), 0, 0));
					contractInfo.addCell(createValueCell(cntrctPriceData.getBalanceAmount().getValue() != null
							? cntrctPriceData.getBalanceAmount().getValue().toString() : StringUtils.EMPTY, 0, 0));
				}
			}
			//complete row
			contractInfo.completeRow();


			document.add(imageTable);
			document.add(titleTable);
			document.add(Chunk.NEWLINE);
			document.add(contractInfo);
			document.add(Chunk.NEWLINE);
			document.add(orderTable);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, "buildPdfDocument", "error while creating PDF", e,
					JnjLatamContractDetailPdfView.class);
		}
	}




	//create cells
	protected PdfPCell createLabelCell(final String text, final int colspan, final int rospan)
	{
		// font
		final Font font = new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK);

		// create cell
		final PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setColspan(colspan);
		cell.setBackgroundColor(Color.GRAY);
		// set style
		labelCellStyle(cell);
		return cell;
	}

	// create cells
	protected PdfPCell createValueCell(final String text, final int colspan, final int rospan)
	{
		// font
		final Font font = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
		// create cell
		final PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setColspan(colspan);
		cell.setBackgroundColor(Color.WHITE);
		// set style
		valueCellStyle(cell);
		return cell;
	}

	public void headerCellStyle(final PdfPCell cell)
	{
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

	public void labelCellStyle(final PdfPCell cell)
	{
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

	public void valueCellStyle(final PdfPCell cell)
	{
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

	protected static class MyPageEvents extends PdfPageEventHelper
	{

		protected MessageSourceAccessor messageSourceAccessor;


		protected PdfContentByte cb;

		protected PdfTemplate template;


		protected BaseFont bf = null;

		public MyPageEvents(final MessageSourceAccessor messageSourceAccessor)
		{
			this.messageSourceAccessor = messageSourceAccessor;
		}


		public void onOpenDocument(final PdfWriter writer, final Document document)
		{
			try
			{
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
			}
			catch (final DocumentException de)
			{
			}
			catch (final IOException ioe)
			{
			}
		}


		public void onEndPage(final PdfWriter writer, final Document document)
		{
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

		public void onCloseDocument(final PdfWriter writer, final Document document)
		{
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
	public static PdfPCell createImageCell(final byte[] path, final int align) throws Exception
	{
		final Image img = Image.getInstance(path);
		final PdfPCell cell = new PdfPCell(img, false);
		labelImageCellStyle(cell, align);
		return cell;
	}

	/**
	 * @param cell
	 * @param align
	 */
	public static void labelImageCellStyle(final PdfPCell cell, final int align)
	{
		// alignment
		if (align == 2)
		{
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		}
		else
		{
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		}
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorder(Rectangle.NO_BORDER);
	}
}

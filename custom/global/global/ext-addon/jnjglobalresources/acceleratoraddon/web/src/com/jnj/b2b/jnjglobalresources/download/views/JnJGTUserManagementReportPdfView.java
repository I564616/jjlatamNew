package com.jnj.b2b.jnjglobalresources.download.views;

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

import com.jnj.b2b.jnjglobalresources.form.JnjGTUserSearchForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTCommonFormIOData;
import com.jnj.facades.data.JnjGTCustomerData;
import com.lowagie.text.Anchor;
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
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;

public class JnJGTUserManagementReportPdfView extends AbstractPdfView {

	@Autowired
	protected I18NService i18nService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
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
	
	private static final Logger LOG = Logger.getLogger(JnJGTUserManagementReportPdfView.class);
	private static final String USER_MANAGEMENT_RESPONSE_DATA_LIST = "searchPageData";
	private static final String USER_MANAGEMENT_FORM_NAME = "JnjGTUserSearchForm";
	protected static final int MARGIN = 32;
	
		
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
				
				arg4.setHeader("Content-Disposition", "attachment; filename=UserManagement_Report.pdf");
				final SearchPageData<JnjGTCustomerData> jnjUserManagementReportResponseDataList = (SearchPageData<JnjGTCustomerData>) arg0
						.get(USER_MANAGEMENT_RESPONSE_DATA_LIST);
				final JnjGTUserSearchForm searchCriteria = (JnjGTUserSearchForm) arg0.get(USER_MANAGEMENT_FORM_NAME);
				
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
		      	
		      	Phrase userProfileSearchheaderPhrase = new Phrase("User Profile Search"  + "\n");
		      	
		      	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
				String date = simpleDateFormat.format(new Date());
				SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm");
				String time = simpleTimeFormat.format(new Date());
				
				Phrase DownloadDateHeaderPhrase = new Phrase("Download Date		:   "+ date + "\n");
				Phrase DownloadTimeHeaderPhrase = new Phrase("Download Time		:   "+ time + "   " +"IST" + "\n"  + "\n" );
				
				
				Phrase searchCriteriaHeaderPhrase = new Phrase("Search Criteria :"  + "\n" );
				
				Phrase searchCriteriaStatusValuePhrase = new Phrase("");
				
				Phrase searchCriteriaLastNameValuePhrase = new Phrase("Last Name		:		"	+ 	searchCriteria.getLastName()  + "\n" );
				Phrase searchCriteriaFirstNameValuePhrase = new Phrase("First Name		:	"	+	searchCriteria.getFirstName() + "\n" );
				Phrase searchCriteriaAccountNumberValuePhrase = new Phrase("Account Number		:	"	+	searchCriteria.getAccountNumber() + "\n" );
				Phrase searchCriteriaAccountNameValuePhrase = new Phrase("Account Name		:	"	+	searchCriteria.getAccountName() + "\n" );
				searchCriteriaStatusValuePhrase = new Phrase("Status		:	"	+	searchCriteria.getStatus() + "\n" );
				Phrase searchCriteriaPhoneValuePhrase = new Phrase("Phone		:	"	+	searchCriteria.getPhone() + "\n" );
				Phrase searchCriteriaEmailValuePhrase = new Phrase("Email		:	"	+	searchCriteria.getEmail() + "\n" );
				Phrase searchCriteriaRoleValuePhrase = new Phrase("");
				StringBuilder roleData1 = new StringBuilder();
				
				if(searchCriteria.getRole() != null){
					if(!(searchCriteria.getRole().equalsIgnoreCase("ALL"))) {
						if(!searchCriteria.getRole().equals("b2bcustomergroup")){
							String name = "b2busergroup." + searchCriteria.getRole() + ".name";
							String role = messageSource.getMessage(name, null, i18nService.getCurrentLocale());
							roleData1.append(role + "     ");
							searchCriteriaRoleValuePhrase = new Phrase("Role		:	"	+	roleData1.toString() + "\n" );
						}else {
							searchCriteriaRoleValuePhrase = new Phrase("");
						}
					} else {
						searchCriteriaRoleValuePhrase = new Phrase("Role		:  "	+ "All" + "\n");
					}
				}
				
				
				Phrase searchCriteriaSecotorValuePhrase = new Phrase("");
				searchCriteriaSecotorValuePhrase = new Phrase("Sector		:	"	+	searchCriteria.getSector() + "\n" );
				
				final PdfPTable userManagementTable = new PdfPTable(4);
				userManagementTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				userManagementTable.setWidthPercentage(100);
				userManagementTable.setLockedWidth(false);
				userManagementTable.setSpacingAfter(30f);
				Phrase searchResultsHeaderPhrase = new Phrase("Search Results :"  + "\n" );
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				Font font  = new Font(bf);
				searchResultsHeaderPhrase.setFont(font);
				userManagementTable.addCell("Profile Name");
				userManagementTable.addCell("Role");
				userManagementTable.addCell("Email");
				userManagementTable.addCell("status");

				final PdfPCell pdfCell = new PdfPCell();
				pdfCell.setFixedHeight(50f);
				
				List<JnjGTCustomerData> customerDataList =jnjUserManagementReportResponseDataList.getResults();

				if (null != customerDataList)
				{
					for (final JnjGTCustomerData jnjGTCustomerData : customerDataList)
					{
						String name = "";
						if(jnjGTCustomerData.getFirstName() != null){
							name = name + jnjGTCustomerData.getFirstName();
						}
						if(jnjGTCustomerData.getLastName() != null){
							name = name + "  " + jnjGTCustomerData.getLastName();
						}
						userManagementTable.addCell( new PdfPCell(
								new Phrase(name)));
						List<String> roleLists = (List<String>) jnjGTCustomerData.getRoles();
						
						StringBuilder rolesData = new StringBuilder();
						for(String roleList : roleLists){
							if(!roleList.equals("b2bcustomergroup")){
								String roleListName = "b2busergroup." + roleList + ".name";
								String role = messageSource.getMessage(roleListName, null, i18nService.getCurrentLocale());
								rolesData.append(role + "     ");
							}
						}
						userManagementTable.addCell(rolesData == null ? pdfCell : new PdfPCell(
								new Phrase(rolesData.toString())));
						userManagementTable.addCell(jnjGTCustomerData.getEmail() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCustomerData.getEmail())));
						userManagementTable.addCell(jnjGTCustomerData.getStatus() == null ? pdfCell : new PdfPCell(
								new Phrase(jnjGTCustomerData.getStatus())));
					}
				}
				arg1.add(imageTable);
				arg1.add(Chunk.NEWLINE);
				arg1.add(userProfileSearchheaderPhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(DownloadDateHeaderPhrase);
				arg1.add(DownloadTimeHeaderPhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchCriteriaHeaderPhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchCriteriaLastNameValuePhrase);
				arg1.add(searchCriteriaFirstNameValuePhrase);
				arg1.add(searchCriteriaAccountNumberValuePhrase);
				arg1.add(searchCriteriaAccountNameValuePhrase);
				arg1.add(searchCriteriaStatusValuePhrase);
				arg1.add(searchCriteriaPhoneValuePhrase);
				arg1.add(searchCriteriaEmailValuePhrase);
				arg1.add(searchCriteriaRoleValuePhrase);
				arg1.add(searchCriteriaSecotorValuePhrase);
				arg1.add(Chunk.NEWLINE);
				arg1.add(searchResultsHeaderPhrase);
				arg1.add(userManagementTable);
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
			String text = messageSourceAccessor.getMessage("page", "page")
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

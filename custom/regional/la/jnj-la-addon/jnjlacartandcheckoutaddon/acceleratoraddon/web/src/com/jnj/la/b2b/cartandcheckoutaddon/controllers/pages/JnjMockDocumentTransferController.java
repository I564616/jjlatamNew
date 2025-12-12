package com.jnj.la.b2b.cartandcheckoutaddon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.util.Config;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.FileUploadDTO;
import com.jnj.core.util.JnjFileUploadToSharedFolderUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.la.b2b.cartandcheckoutaddon.forms.JnjMockDocumentTransferForm;
import com.jnj.la.core.dto.JnjSellOutReportData;


/**
 * This controller handles the request for the Help Page.
 *
 * @author Manoj.K.Panda
 *
 */
@Controller
@Scope("tenant")
@RequestMapping("/mockdocumenttransfer")
public class JnjMockDocumentTransferController extends AbstractPageController
{

	private static final Logger LOG = Logger.getLogger(JnjMockDocumentTransferController.class);


	@Autowired
	JnjFileUploadToSharedFolderUtil jnjFileUploadToSharedFolderUtil;


	@GetMapping
	public String loadMockOrderSimulatePage(final Model model) throws CMSItemNotFoundException
	{
		return "pages/test/mockdocumenttransfer";
	}

	@PostMapping
	public String processFormOrderSimulate(@ModelAttribute final JnjMockDocumentTransferForm mockForm, final Model model)
			throws CMSItemNotFoundException
	{
		final MultipartFile file = mockForm.getFileToUpload();

		final String filePath;
		String localFilePath;
		final String remoteFilePath;
		//Setting the path according to company name
		if (mockForm.getCompany().equals(Jnjb2bCoreConstants.SellOutReports.MEDICAL))
		{
			localFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.SHARED_FOLDER_LOCATION_SELLOUT_MEDICAL);
			remoteFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.MEDICAL_REMOTE_PATH);
		}
		else
		{
			localFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.SHARED_FOLDER_LOCATION_SELLOUT_PHARMA);
			remoteFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.PHARMA_REMOTE_PATH);
		}

		final FileUploadDTO fileUploadDTO = new FileUploadDTO();
		final JnjSellOutReportData sellOutReportData = new JnjSellOutReportData();

		fileUploadDTO.setFile((MultipartFile) file);
		sellOutReportData.setFile((MultipartFile) file);

		sellOutReportData.setCompany(mockForm.getCompany());

		final String fileName = fileUploadDTO.getName();

		final String customerId = "130SadeepSoldTO";

		sellOutReportData.setCustomer(customerId);

		final Date date = new Date();

		final Timestamp timestamp = new Timestamp(date.getTime());
		final String newFileName = customerId + "_" + timestamp;
		sellOutReportData.setDocName(newFileName);
		fileUploadDTO.setRenameFileTo(file.getOriginalFilename());

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Saving file on shared folder.");
		}
		//Calling JnjFileUploadToSharedFolderUtil class method to save file to shared folder
		if (jnjFileUploadToSharedFolderUtil.uploadFileToSharedFolder(fileUploadDTO, localFilePath))
		{
			localFilePath = localFilePath + Jnjb2bCoreConstants.SellOutReports.SLASH;
			filePath = localFilePath + Jnjb2bCoreConstants.SellOutReports.SLASH + mockForm.getFileToUpload().getOriginalFilename();

			//Calling JnjSftpFileTransferUtil class method to save file to SFTP folder
			final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
			final boolean uploadStatus = jnjSftpFileTransferUtil.uploadFileToSftp(fileUploadDTO, localFilePath, remoteFilePath,
					"empenho");
			if (LOG.isDebugEnabled())
			{
				LOG.debug("If saved in shared folder, then call facade method to save on SFTP server");
			}

			model.addAttribute("msg", uploadStatus ? "TRUE" : "FALSE");


			// Delete the file from the Shared folder.
			final File f1 = new File(filePath);
			f1.delete();
		}

		return "pages/test/mockresponse";
	}




}
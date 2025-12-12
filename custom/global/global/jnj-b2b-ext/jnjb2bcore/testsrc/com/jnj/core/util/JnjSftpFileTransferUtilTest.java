/**
 * 
 */
package com.jnj.core.util;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.dto.FileUploadDTO;


/**
 * @author komal.sehgal
 * 
 */

@UnitTest
public class JnjSftpFileTransferUtilTest
{

	String sharedfolder;
	String renameFileName;
	String localFilePath;
	String remoteFilePath;
	String testFile;

	@Before
	public void setUp()
	{
		sharedfolder = "c:\\upload";
		renameFileName = "jnjSftpFileTransferUtilTestFile";
		localFilePath = "C:/upload/";
		remoteFilePath = "/aaa/";
		testFile = "C:\\Users\\komal.sehgal\\Desktop\\LoadTranslation.jpg";

	}

	@Test
	public void testFileTransferToSharedFolder()
	{

		final JnjFileUploadToSharedFolderUtil fileUpload = new JnjFileUploadToSharedFolderUtil();

		final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();


		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();

		final MockMultipartHttpServletRequest mockMultipartHttpServletRequest = request;

		final MultipartFile commonsMultipartFile = (MultipartFile) mockMultipartHttpServletRequest.getFile(testFile);

		final FileUploadDTO fileUploadDTO = new FileUploadDTO();

		fileUploadDTO.setFile(commonsMultipartFile);

		fileUpload.uploadFileToSharedFolder(fileUploadDTO, sharedfolder);

		final boolean sftpFileUploadResult = jnjSftpFileTransferUtil.uploadFileToSftp(fileUploadDTO, localFilePath, remoteFilePath,
				"empenho");

		Assert.assertTrue(sftpFileUploadResult);
	}
}

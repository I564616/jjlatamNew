package com.jnj.core.util;


import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jnj.core.dto.FileUploadDTO;


@UnitTest
public class JnjFileUploadToSharedFolderUtilTest
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
		renameFileName = "test456";
		localFilePath = "C:/upload/";
		remoteFilePath = "/aaa/";
		testFile = "C:\\Users\\komal.sehgal\\Desktop\\LoadTranslation.jpg";

	}

	@Test
	public void testFileTransferToSharedFolder()
	{

		System.out.println("in test");
		final JnjFileUploadToSharedFolderUtil fileUpload = new JnjFileUploadToSharedFolderUtil();

		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();

		final MockMultipartHttpServletRequest mockMultipartHttpServletRequest = request;

		final CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mockMultipartHttpServletRequest.getFile(testFile);

		final FileUploadDTO fileUploadDTO = new FileUploadDTO();

		fileUploadDTO.setFile(commonsMultipartFile);

		final boolean fileUploadResult = fileUpload.uploadFileToSharedFolder(fileUploadDTO, sharedfolder);

		Assert.assertTrue(fileUploadResult);
	}
}

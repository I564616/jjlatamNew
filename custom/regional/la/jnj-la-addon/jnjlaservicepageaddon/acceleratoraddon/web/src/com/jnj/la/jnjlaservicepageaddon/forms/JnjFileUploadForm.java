package com.jnj.la.jnjlaservicepageaddon.forms;

import org.springframework.web.multipart.MultipartFile;

public class JnjFileUploadForm {
	private MultipartFile[] files;

	/**
	 * @return the files
	 */
	public MultipartFile[] getFiles() {
		return files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(final MultipartFile[] files) {
		this.files = files;
	}

}

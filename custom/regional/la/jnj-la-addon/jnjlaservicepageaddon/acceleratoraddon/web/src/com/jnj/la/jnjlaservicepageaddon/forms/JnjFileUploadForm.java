package com.jnj.la.jnjlaservicepageaddon.forms;

import org.springframework.web.multipart.MultipartFile;

public class JnjFileUploadForm {
	private CommonsMultipartFile[] files;

	/**
	 * @return the files
	 */
	public CommonsMultipartFile[] getFiles() {
		return files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(final CommonsMultipartFile[] files) {
		this.files = files;
	}

}

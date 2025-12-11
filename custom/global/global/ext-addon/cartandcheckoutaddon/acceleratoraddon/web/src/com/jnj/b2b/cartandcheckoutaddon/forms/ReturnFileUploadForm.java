package com.jnj.b2b.cartandcheckoutaddon.forms;

import java.util.List;

public class ReturnFileUploadForm<MultipartFile> {
	 private List<MultipartFile> returnImageUploadFiles;
	 
	    public List<MultipartFile> getFiles() {
	        return returnImageUploadFiles;
	    }
	 
	    public void setFiles(List<MultipartFile> files) {
	        this.returnImageUploadFiles = files;
	    }

}

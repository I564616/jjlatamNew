package com.jnj.core.dto;

public class JnjCanonicalDTO {
	
	
	private String sourceColumn;
	private String targetColumn;
	private String targetModel;
	private String jobCode;
	private boolean primaryKey;
	private String transitionType;
	private String compositeType;
	private String compositeTypeUid;
	private String targetColumnFormat;
	//Added for Util Method Creation Start
	private String utilClassNamePath;
	private String utilMethodName;
	private String utilReturnType;
	private String utilMethodParam;
	//Added for Util Method Creation End
	private boolean writable;
	private boolean primaryKeyOfCustomType;
	
	public boolean isPrimaryKeyOfCustomType() {
		return primaryKeyOfCustomType;
	}
	public void setPrimaryKeyOfCustomType(boolean primaryKeyOfCustomType) {
		this.primaryKeyOfCustomType = primaryKeyOfCustomType;
	}
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	public String getTargetColumnFormat() {
		return targetColumnFormat;
	}
	public void setTargetColumnFormat(String targetColumnFormat) {
		this.targetColumnFormat = targetColumnFormat;
	}
	
	public String getSourceColumn() {
		return sourceColumn;
	}
	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}
	public String getTargetColumn() {
		return targetColumn;
	}
	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}
	public String getTargetModel() {
		return targetModel;
	}
	public void setTargetModel(String targetModel) {
		this.targetModel = targetModel;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getTransitionType() {
		return transitionType;
	}
	public void setTransitionType(String transitionType) {
		this.transitionType = transitionType;
	}
	public String getCompositeType() {
		return compositeType;
	}
	public void setCompositeType(String compositeType) {
		this.compositeType = compositeType;
	}
	public String getCompositeTypeUid() {
		return compositeTypeUid;
	}
	public void setCompositeTypeUid(String compositeTypeUid) {
		this.compositeTypeUid = compositeTypeUid;
	}
	public String getUtilClassNamePath() {
		return utilClassNamePath;
	}
	//Added for Util Method Creation Start
	public void setUtilClassNamePath(String utilClassNamePath) {
		this.utilClassNamePath = utilClassNamePath;
	}
	public String getUtilMethodName() {
		return utilMethodName;
	}
	public void setUtilMethodName(String utilMethodName) {
		this.utilMethodName = utilMethodName;
	}
	public String getUtilReturnType() {
		return utilReturnType;
	}
	public void setUtilReturnType(String utilReturnType) {
		this.utilReturnType = utilReturnType;
	}
	public String getUtilMethodParam() {
		return utilMethodParam;
	}
	public void setUtilMethodParam(String utilMethodParam) {
		this.utilMethodParam = utilMethodParam;
	}
	//Added for Util Method Creation End
	
	
	
	

}

<!-- AAOL-4937 Return Image upload option -->
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Modal -->
<c:url value="/cart/uploadFileforReturn" var="uploadFileforReturnURL" />
<form:form method="post" action="${uploadFileforReturnURL}" id="uploadFileforReturnForm" enctype="multipart/form-data" modelAttribute="returnUploadForm">
	<div class="modal  fade jnj-popup-container" id="upload-file-popup" role="dialog" data-firstLogin='true'>
			<div class="modal-dialog modalcls modal-md">
			
				<div class="modal-content popup">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal" id="select-accnt-close"><spring:message code="homePage.newReturn.close"/></button>
					  <h4 class="modal-title selectTitle"><spring:message code="dispute.order.fileUpload"/></h4>
					</div>
					<div class="modal-body" id="upload-file-row-holder">
						<div class="panel-group" style="margin-bottom:10px">
							<div class="panel panel-info">
							  <div class="panel-heading return">
								<h4 class="panel-title">
									<span class="glyphicon glyphicon-info-sign"></span>
										<spring:message code="return.upload.file.msg"/>
								</h4>
							  </div>
							</div>  
						</div>
						<div class="panel-group" id="error-msg-holder">
							<div class="panel panel-danger">
							  <div class="panel-heading return">
								<h4 class="panel-title upload-file-errors" id="upload-emptyfile-error" style="margin-top:5px;margin-bottom:5px">
									<span><span class="glyphicon glyphicon-ban-circle"></span> 
										<spring:message code="return.upload.file.blank.msg"/>
									</span>
								</h4>
								<h4 class="panel-title upload-file-errors" id="upload-file-type-error" style="margin-top:5px;margin-bottom:5px">
									<span><span class="glyphicon glyphicon-ban-circle"></span> 
										 <spring:message code="return.invalid.file.type.error"/>
									</span>
								</h4>
								<h4 class="panel-title upload-file-errors" id="upload-maxsize-error"  style="margin-top:5px;margin-bottom:5px">
									<span><span class="glyphicon glyphicon-ban-circle"></span> 
										<spring:message code="return.upload.max.size.error"/>
									</span>
								</h4> 
							  </div>
							</div>  
						</div>
						
						<div class="row upload-file-row">
							<div class="attach-file-label inline-to-block"><spring:message code="return.attach.file"/></div>
							<div class="attach-file-field inline-to-block">
								<input type="text" class="form-control uploadFileTextbox" value="" readonly="readonly">
								<!-- <div class="upload-desc">Please upload only JPEG,JPG,PNG,XLS.WORD,TIF,PDF files upto a maximum of 5 MB.</div> -->
							</div>
							<div class="float-right-to-none" style="margin-top:8px">
								<div class="inline-to-block fileUploadInputHolder" style="padding-right:15px">
									<label for="fileUploadInput-0" class="browse-btn">
										<spring:message code="return.browse.file"></spring:message>
									</label>
									<input id="fileUploadInput-0" class="fileUploadInput" style="display:none" type="file" name="files[0]"/>
								</div>
								<!-- Changes for AAOL-5807 -->
									<div class="inline-to-block upload-file-add-btn btn-disable" style="padding-right:5px"><a href="#"><span class="glyphicon glyphicon-plus"></span></a></div>
									<div class="inline-to-block upload-file-del-btn btn-disable"><a href="#"><span class="glyphicon glyphicon-minus"></span></a></div>
									<!-- <div class="add-file-link">Add File</div> -->	
								</div>
							</div>
						</div>		
						 <div class="modal-footer ftrcls">
							<a href="" class="pull-left canceltxt" data-dismiss="modal"><spring:message code="cart.common.cancel"/></a>
							<a  class="btn btnclsactive mobile-auto-btn" id="returnImageSubmitBtn"><spring:message code="return.upload.submit"/></a>
						</div>				
					</div>												
					
					
				</div>
			  
			</div>
		
</form:form>				
<!--End of Modal pop-up-->
						
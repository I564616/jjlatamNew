<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/home/webEdiOrder" var="webEdiUrl" />

<form:form name="placeOrderEdiForm" id="placeOrderEdiForm" enctype="multipart/form-data" method="POST" action="${webEdiUrl}">
	
	<div id="uploadEdiDiv" class="padding0">
		<div class="btn-file">
				<img src="${webroot}/_ui/responsive/common/images/document.png" class="img-circle " width="50" height="50" />
				<input type="file" id="uploadEdiFile" name="submitEdiOrderFile" multiple="multiple"   class="hidden-input"/>
				 <a class="col-xs-8 col-md-12 col-sm-12 col-lg-12 xplaceordermobalign boldtext dashboardtt paddingTop12">
	            	<span class="webEdiFileMessage">
		            	<p><spring:message code="home.label.from.webEdiFile" /></p>
						<p><spring:message code="home.label.webEdiFile" /></p>
	            	</span>
        		</a>
 		</div>
 		<div id="ediFilesAttached" style='display: none;'>
        	<spring:message code="home.label.webEdiFile.totalCount" />&nbsp;<div id="totalFilesAttached"></div>
        </div>
		<div id="uploadFilesList"></div>
    </div>
	 
	 <div id='uploadbtnWrapper' style='display: none;'>
		 	<input type="button" id="ediPlaceOrderfile" 
		 		value='<spring:message code="cart.common.placeOrder" />' class="primarybtn floatRight marginLeft filenamebutton2 btnclsactive" >
	  </div>
</form:form>


<div class="modal fade jnj-popup" id="ediOrderFileUploadDetailPopup" role="dialog">
    <div class="modal-dialog modalcls modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button id="ediResultMessageClose" type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
                <h4 class="modal-title"><spring:message code="homePage.webEdi.uploadHeaderMessage" /></h4>
            </div>
            <div id="ediModalBodyContent" class="modal-body">
            	<div class="row">	
             	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                     <span id="ediResultMessageContent"></span>
                 </div>
             </div>
            </div>
        </div>
    </div>
</div>    
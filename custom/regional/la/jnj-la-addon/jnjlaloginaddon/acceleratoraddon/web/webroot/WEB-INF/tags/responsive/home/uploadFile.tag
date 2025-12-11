<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/responsive/home"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
 <input type="hidden" id="showMinQtyMsg" value="${showMinQtyMsg}" />
<input type="hidden" id="test" value="${test}" />
<div >
	<c:url value="/home/homepageFileUpload" var="uploadUrl"/>
	<c:url value="/cart" var="cartUrl"/>
	<form:form name="uploadFileFormHome" id="uploadFileFormHome" enctype="multipart/form-data" method="POST" action="${uploadUrl}">

		<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 no-margin padding0">
			<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 image-upload mobile-imgTxt-view mobile-img-view">
				<label class="col-xs-12 col-md-12 col-sm-12 col-lg-12 " for="uploadmultifilehome">
					 <img src="${webroot}/_ui/responsive/common/images/upload.png" class="img-circle " style="width:50px!important;height=50px!important"/>
				</label>

			</div>
			<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-img-view" style="margin-top: -13px">
					<a href="#" class="uploadHomeLink">
					<label class="col-xs-12 col-md-12 col-sm-12 col-lg-12 " for="uploadmultifilehome">
						<p class="marginbtmplaceorder"><spring:message code="home.label.uploadfile" /></p>
						</label>
					</a>
			</div>
			<div class="templateDownloadLinks" id="uploadMessagesInstruction" >
                <cms:component uid="instructionsTemplatesLink" />
            </div>
		</div>

		<div id="uploadDiv" class="col-xs-12 col-md-12 col-sm-12 col-lg-12 no-margin padding0">
			<input id='uploadmultifilehome'  class="uploadfilehome" type='file' name="uploadmultifilehome"/>
		</div>

		<div class="row uploadedfilename" id="downLoadCartSpn" style="display:none">
			<div id="filename"></div>
			<a id="downLoadCart" class="primarybtn floatRight filenamebutton2 btnclsactive" href="${cartUrl}"><spring:message code="homePage.instruction.view.cart"/></a>
		</div>

		<div id="placeorderfileSpn" style="display:none;width:100%;text-align:center"><input type="button" class='primarybtn floatRight filenamebutton2 btnclsactive' id="placeorderfile" name="placeorderfile" value="<spring:message code='product.search.addTocartSearch'/>"/></div>
		<div id="errorDetailsSpn" style="display:none;"><input id="errorDetails" type="button" class="tertiarybtn floatRight filenamebutton2 btnclsactive" value="<spring:message code='homePage.errorDetails'/>" /></div>

		<div class="uploadContent">
			<div class="uploadMessages">
				<div class="uploadMessagesError" style="display:none">
					<p>
						<spring:message code="home.upload.file.error"/>&nbsp;
					</p>
				</div>
				<div class="uploadMessagesError uploadMessagesEmpty" style="display:none">
					<p>
						<spring:message code="home.upload.file.empty"/>&nbsp;
					</p>
				</div>
				<div class="uploadMessagesSuccess" style="display:none">
					<p>
						<spring:message code="home.upload.file.success"/>
					</p>
				</div>
			</div>
		</div>
        <div class= "registersucess uploadMessagesSuccess" style="display:none">
            <label  class="success">
                <spring:message code="home.upload.file.successOrder" />
            </label>
        </div>

	</form:form>
	<div class="modal fade jnj-popup" id="addtocartmsgpopup" role="dialog">
        <div class="modal-dialog modalcls modal-md">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="product.detail.close" /></button>
                  <h4 class="modal-title"><spring:message code="home.label.information"/></h4>
                </div>
                <div class="modal-body">
                    <p><spring:message code="home.label.errordetails" /></p>
                </div>
            </div>
        </div>
    </div>
</div>
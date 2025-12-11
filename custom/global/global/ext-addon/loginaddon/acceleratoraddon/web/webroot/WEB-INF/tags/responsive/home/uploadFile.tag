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
		 
		
		<%-- <div class="buttonContainer">
		   <div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding0">
			 <div class="image-upload padding0">
				 <label for="uploadmultifilehome">
			     		<img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/upload.png"/>
			   		<a class="col-xs-8 col-md-12 col-sm-12 col-lg-12 placeordermobalign boldtext dashboardtxt padding0 uploadfilealign">
			   		<spring:message code="home.label.uploadfile" /></a>  
						<input id='uploadmultifilehome'  class="uploadfilehome" type='file' name="uploadmultifilehome"/>
		 
		    <div id="placeorderfileSpn" style="display:none;width:100%;text-align:center"><input type="button" class='primarybtn floatRight filenamebutton2 btnclsactive' id="placeorderfile" name="placeorderfile" value="Add to Cart"/></div>
			<div id="errorDetailsSpn" style="display:none"><input id="errorDetails" type="button" class="tertiarybtn floatRight filenamebutton2 btnclsactive" value="<spring:message code='homePage.errorDetails'/>" /></div>
			
			<span id="downLoadCartSpn" style="display:none">
			<div id="filename"></div>
			<a id="downLoadCart" class="primarybtn floatRight filenamebutton2 btnclsactive" href="${cartUrl}"><spring:message code="homePage.instruction.view.cart"/></a>
			</span>
			 </label>
			 </div>
			 	<div class="templateDownloadLinks" id="uploadMessagesInstruction" >
					<!-- <p><spring:message code="home.upload.file.title"/></p>
					<br /> -->
					<cms:component uid="instructionsTemplatesLink" />
				</div>
			</div>	
					
		</div> --%>
		
		<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 no-margin padding0">
			<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 image-upload mobile-imgTxt-view mobile-img-view">
				<label class="col-xs-12 col-md-12 col-sm-12 col-lg-12 " for="uploadmultifilehome">
					 <img src="${webroot}/_ui/responsive/common/images/upload.png"/>
				</label>
				
			</div>
			<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-img-view" >
					<a href="#">
					<label class="col-xs-12 col-md-12 col-sm-12 col-lg-12 " for="uploadmultifilehome">
						<p class="marginbtmplaceorder"><spring:message code="home.label.uploadfile" /></p>
						</label>
					</a>
					
					<a href="#">
						<p class="marginbtmplaceorder templateDownloadLinks"  id="uploadMessagesInstruction" style="margin-top: 10px;"><cms:component uid="instructionsTemplatesLink" /></p>
					</a>
			</div>
		</div>
		
		<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 no-margin padding0">
			<input id='uploadmultifilehome'  class="uploadfilehome" type='file' name="uploadmultifilehome"/>
		</div>	
		
		<div class="row uploadedfilename" id="downLoadCartSpn" style="display:none">
			<div id="filename"></div>
			<!-- <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 btnclsactive filenamebutton"><spring:message code='product.search.addTocartSearch'/></div>	 -->
			<a id="downLoadCart" class="primarybtn floatRight filenamebutton2 btnclsactive" href="${cartUrl}"><spring:message code="homePage.instruction.view.cart"/></a>
		</div> 
		 
		<div id="placeorderfileSpn" style="display:none;width:100%;text-align:center"><input type="button" class='primarybtn floatRight filenamebutton2 btnclsactive' id="placeorderfile" name="placeorderfile" value="<spring:message code='product.search.addTocartSearch'/>"/></div>
		<div id="errorDetailsSpn" style="display:none;"><input id="errorDetails" type="button" class="tertiarybtn floatRight filenamebutton2 btnclsactive" value="<spring:message code='homePage.errorDetails'/>" /></div>
		
		<div class="uploadContent" style="display:none">
			<div class="uploadMessages">
			
				<div class="uploadMessagesError">
					<p>
						<spring:message code="home.upload.file.error"/>
					</p>
				</div>
				<div class="uploadMessagesError uploadMessagesEmpty">
					<p>
						<spring:message code="home.upload.file.empty"/>
					</p>
				</div>
				<div class="uploadMessagesSuccess">
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
	<!-- Modal tncpopup -->
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
	<!--End of Modal pop-up-->
	
		<%-- <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="contractPopuppage">
				<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
					<div class="modal-dialog modalcls">
						<div class="modal-content popup">
							<div class="modal-header">
							  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
							  <h4 class="modal-title selectTitle"><spring:message code="contract.page.addprod"/></h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-danger">
									<div class="panel-heading">
										<h4 class="panel-title">
										<table class="contract-popup-table">
											<tr>
											<td><div class="glyphicon glyphicon-ok"></div></td>
											<td><div class="info-text"><spring:message code="contract.page.infotext"/></div></td>														
											</tr>
										</table>
										</h4>
									</div>
								</div>													
								<div><spring:message code="contract.page.msg"/></div>
								<div class="continueques"><spring:message code="contract.page.continue"/></div>
							</div>											
							<div class="modal-footer ftrcls">
								<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart0"><spring:message code="cart.common.cancel"/></a>
								<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart0" ><spring:message code="contract.page.accept"/></button>
							</div>
						</div>
					</div>
				</div>
		</div>		
<!--  Add to cart Modal pop-up to identify  contract or non contract end --> --%>
</div>
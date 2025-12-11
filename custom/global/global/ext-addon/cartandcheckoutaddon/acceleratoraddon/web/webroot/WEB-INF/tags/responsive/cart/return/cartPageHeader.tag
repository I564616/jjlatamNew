<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="returnCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/return"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"%>
<!--Added for AAOL-4937-->
<%@ taglib prefix="returnUploadImage" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/return"%>
<c:url value="/cart/changeOrderType" var="changeOrderTypeURL" />
<form:form method="post" action="${changeOrderTypeURL}" id="changeOrderTypeForm"></form:form>
<c:url value="/checkout/single/returnConfirmationPage" var="returnConfirmationURL" />


<c:if test="${empty cartData.entries || canCheckout}">						
	<c:set value="buttonDisable" var="classForSaveTemplate"/>
	<c:set value="buttonDisable" var="classForValidate"/>					
</c:if>
<%-- <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" /> --%>
	<%--  <div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-5 col-xs-12">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-6 col-md-6 col-sm-7 col-xs-12">
			<div class="float-right-to-none">
			<div class="orderType-holder">
					<span class="orderType-label">
					<spring:message	code="cart.common.orderType" />
					</span>
					<div class="orderType-dropdown">
						<select id="changeOrderType" name="orderType">
							<option value="${cartData.orderType}"><spring:message
									code="cart.common.orderType.${cartData.orderType}"></spring:message></option>
							<c:forEach items="${orderTypes}" var="orderType">
								<c:if test="${cartData.orderType ne orderType}">
									<option value="${orderType}"><spring:message
											code="cart.common.orderType.${orderType}"></spring:message></option>
								</c:if>
							</c:forEach>

						</select>
					</div>
				</div>
				<!-- No charge Order AAOL-3392 -->
			
				<!-- No charge Order AAOL-3392 -->
				
			</div>
		</div>
	</div>  --%>
	<%-- <c:if test="${not empty validationError}">
						<div class="error">
						
						<p style="color:red">
								${validationErrorMsg}
						</p>
						
						<p style="color:red">
								<spring:message code="dropshipment.error.not.found" />
						</p>
						</div>
					</c:if> --%>
<!-- flash message for contract product -->
<div class="panel-group contract-product-show" style="margin-bottom:20px" >
	<div class="panel panel-success">
		<div class="panel-heading">
			<h4 class="panel-title">
			<spring:message code="cart.review.popup.text"/>:&nbsp;<span id="contract_product_msg"></span> 
			</h4> 
		</div>
	</div>
</div>

<input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}"/>
 <input type="hidden" name="defaultChekAddid" id = "defaultChekAddid" value="${defaultChekAddid}">
<input type="hidden" name="returnOrderPage" id = "returnOrderPage" value="true">
<!-- Added for Shipping address Display in Validation page -->



<!-- <section>
<div class="row jnj-body-padding" id="jnj-body-content">
							<div class="col-lg-12 col-md-12 mobile-no-pad"> -->
<div id="returnorderpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row ">
			<div class="col-lg-6 col-md-7 col-sm-6 col-xs-12 headingTxt content"><spring:message code="homePage.newReturn.returnAnorder"></spring:message>
				<!-- <a href="#"><span class="badge quescls">?</span></a> -->
			</div>
			<div class="col-lg-6 col-md-5 col-sm-6 col-xs-12 margintop15">
				<div class="float-right-to-none">
					<!--AAOL-4251 - Sprint 4 demofix  for UI -->
	<%-- 													<a href="#" type="button" data-toggle="modal" id="startNewButton" class="btn btnclsactive btnclsnormal"><spring:message	code="cart.quote.convertcart" /></a> 
	 --%>												
								<a href="#" id="startNewButton" class="canceltxt  return-ordr-cancel-btn"><spring:message code="homePage.newReturn.cancel" /></a>
						<button type="button" class="btn btnclsactive ${classForValidate} submitReturn"><spring:message code="cart.return.returnAction.submitReturns" /></button>
				</div>
			</div>
		</div>
	<!-- Changes for AAOL-5809 -->
	<c:if test="${isReturnImageUploaded}">
		<div class="panel-group uploadSucess">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h4 class="panel-title">
						<span><span class="glyphicon glyphicon-ok"></span> <spring:message
								code="return.image.upload.success"></spring:message></span>
					</h4>
	
				</div>
	
			</div>
		
		</div>
	</c:if>
	<div class="row table-padding">
		<div class="col-lg-12 col-md-12">
			<div class="row first-row-content">
	
				<div class="col-lg-2 col-md-2 col-sm-2 col-xs-12">
					<div class="return-label-head"><spring:message code="cart.return.orderType"></spring:message></div>
					<div class="return-head-content"><spring:message
								code="cart.common.orderType.${cartData.orderType}" /></div>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-2 col-xs-12">
					<div class="return-label-head"><spring:message code="cart.return.Account"></spring:message></div>
					<div class="return-head-content">${cartData.b2bUnitId}</div>
				</div>
				
				<div class="col-lg-8 col-md-8 col-sm-8 upload-file">
					<div class="float-right-to-none">
						<label class="btn btnclsnormal" id="uploadReturnOrderFile"> <spring:message code="cart.return.uploadFile"/></label>
					 	<a href="#uploadReturnOrder" class="instr-temp-text"></a><cms:component uid="instructionsTemplatesLinkUploadReturnOrder" />
					</div>
			   </div>
				
	<%-- 						<div class="col-lg-3 col-md-4 col-sm-5" id="instrct-template"><a href="#uploadReturnOrder" class="instr-temp-text"></a><cms:component uid="instructionsTemplatesLinkUploadReturnOrder" /></div>
	 --%>	</div>
			<div><spring:message code="${statusKey}"/></div>
			
			<div class="row subcontent2">
				<form:form id="returnOrderForm" action="${returnConfirmationURL}" method="POST">
					<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
						
						<label for="purchOrder" class="report-label"><spring:message code="cart.common.purchaseOrder"/><sup class="redStar">*</sup></label> 
						
						<div class="report-form-field">
							<input type="text" class="form-control required" data-msg-required="<spring:message code="cart.required.field"></spring:message>"
							value="${cartData.purchaseOrderNumber}" required="required"  id="purchOrder"  name="purchaseOrder"
					 maxlength="35">
					 		<div class="registerError returnOrderErrProp"></div>
					 	</div>
						
						
						<%-- <label for="purchOrder" class="product-order-label"><spring:message code="cart.common.purchaseOrder"/><sup class="redStar">*</sup>
						</label> 
						
						<input type="text" class="form-control product-order-txt required" data-msg-required="<spring:message code="cart.required.field"></spring:message>"
							value="${cartData.purchaseOrderNumber}" required="required"  id="purchOrder"  name="purchaseOrder"
					 maxlength="35">
							
						<div class="col-lg-offset-5 col-lg-7"><div class="registerError returnOrderErrProp"></div></div> --%>
					</div>
					<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
						<label for="reasonCode" class="reason-code-label"><spring:message code="cart.return.reasonCode"/><sup class="redStar">*</sup></label>
						<div class="reason-code-field">
							<select id="reasonCodeReturn"  class="reasonCodeReturnSelect required" data-msg-required="<spring:message code="cart.required.field"/>" name="reasonCodeReturn" data-width="100%" >
								<option value=""><spring:message code="cart.review.orderInfo.selectReason" />
								</option>
								<c:forEach var="reasonCode" items="${reasonCodeReturn}">
									<c:choose>
										<c:when test="${reasonCode.key eq cartData.reasonCodeReturn}">
											<option value="${reasonCode.key}" selected="selected">${reasonCode.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${reasonCode.key}">${reasonCode.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<div class="registerREError"></div>
						</div>
						
						<%-- <label for="reasonCode" class="reason-code-label"><spring:message code="cart.return.reasonCode"/>
							<!--AAOL-4251 - Sprint 4 demofix  for UI -->
									<sup class="redStar">*</sup></label>
									<div class="reason-code-dropdown">
									
										<select id="reasonCodeReturn"  class="reasonCodeReturnSelect required" data-msg-required="<spring:message code="cart.required.field"/>" name="reasonCodeReturn" data-width="100%" >
											<option value=""><spring:message code="cart.review.orderInfo.selectReason" />
											</option>
								<c:forEach var="reasonCode" items="${reasonCodeReturn}">
									<c:choose>
										<c:when test="${reasonCode.key eq cartData.reasonCodeReturn}">
											<option value="${reasonCode.key}" selected="selected">${reasonCode.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${reasonCode.key}">${reasonCode.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							
									
						</div>
						<div class="col-lg-offset-4 col-lg-8" style="padding-left:10px;"><div class="registerREError"></div></div> --%>
						
					</div>
					<div class="form-group col-lg-2 col-md-2 col-sm-6 col-xs-12" id="upload-file-link" style="padding:0px;margin-top:8px">
						<a href="#" style="margin-top:5px" data-toggle="modal" data-target="#upload-file-popup"><spring:message code="cart.return.upload.images"/></a>
					</div>
					<div class="${hiddenFields.customerRefPo}">
					<div class="form-group col-lg-6 col-md-6 col-sm-5 col-xs-12">
						<label for="customerPo" class="report-label"><spring:message code="cart.return.returnInfo.customerReferencePO" /><c:if test="${requiredFields.customerRefPo eq 'required'}"><sup class="redStar">*</sup></c:if></label> 
						
						<div class="report-form-field">
							<input type="text"
								class="form-control  ${requiredFields.customerRefPo}" id="customerPo" name="customerPo" data-msg-required="<spring:message code="cart.required.field"/>">
						
							<div class="col-lg-offset-5 col-lg-7"><div class="registerError returnOrderErrProp"></div></div>
						</div>	
					</div>
					</div>
				</form:form>
			</div>
			
			</div>
	</div>
</div>
					
	<!-- Modal -->
	<div class="modal fade jnj-popup-container" id="newReturnOrderpopup" role="dialog" data-firstLogin='true'>
		<div class="modal-dialog modalcls">
			<div class="modal-content popup">
				<div class="modal-header">
				  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="homePage.newReturn.close"/></button>
				  <h4 class="modal-title selectTitle"><spring:message code="cart.return.uploadFile"/></h4>
				</div>
				 <form:form action="cart/addToCartReturn" enctype="multipart/form-data" id="submitDelivedOrderFileForm" method="POST">	
				<div class="modal-body">		
					<div class="row surgery-info-row">
						<div class="col-lg-12">
							<input type="file"  id="uploadReturnOrderBrowseFile file-input" name="returnOrderDoc" class="filestyle returnOrderFileUpload" data-buttonText="Choose File">
							<div class="upload-info"><spring:message code="cart.return.returnInfo.upload.xls.only"></spring:message></div>
						</div>
					</div>
					
				</div>											
				<div class="modal-footer ftrcls">
					<a href="#" class="pull-left canceltxt" data-dismiss="modal"><spring:message code="homePage.newReturn.cancel"></spring:message></a>
					<button type="button" class="btn btnclsactive submitReturnOrderFile" data-dismiss="modal" id="accept-btn"><spring:message code="homePage.newReturn.ok"></spring:message></button>
				</div>
				</form:form>
			</div>
		</div>
	</div> 
								<!--End of Modal pop-up-->

<%-- <div class="modal fade jnj-popup-container"
	id="newReturnOrderErrorpopup" role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
				<h4 class="modal-title selectTitle">
					<spring:message code="homePage.errorDetails" />
				</h4>
			</div>
			<div class="globalError">
				<p>
					<spring:message code="homePage.errordetails.addfailed" />
				</p>
			</div>
			<div class="modal-body">
				<div class="row surgery-info-row">
					<div class="col-lg-12">
						<c:forEach items="${responseMap}" var="errorDetails">
							<ul>
								<li>
									<p>${errorDetails.value}</p>
								</li>
							</ul>
						</c:forEach>
					</div>
				</div>

			</div>


		</div>
	</div>
</div> --%>

<div class="modal fade jnj-popup" id="newReturnOrderErrorpopup"
					role="dialog">
	<div class="modal-dialog modalcls modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal">
					<spring:message code="cart.review.close" />
			</button>
			<h4 class="modal-title">
				<spring:message code="homePage.errorDetails" />
			</h4>
		</div>
		<form:form method="post" action="javascript:;">
			<div class="modal-body">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="panel-group">
							<div class="panel panel-danger">
								<div class="panel-heading">
									<h4 class="panel-title cart-error-msg">
										<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message
												code="homePage.errordetails.addfailed" /></span>
									</h4>
				
								</div>
								<div>
									<c:forEach items="${responseMap}" var="errorDetails">
										<ul>
											<li>
												<p>${errorDetails.value}</p>
											</li>
										</ul>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="scroll error-content" style="font-weight: bold">
						</div>
					</div>
				</div>
			</div>
		</form:form>
		</div>
	</div>
</div>
<!-- AAOL-4937 -->
<returnUploadImage:returnUploadImage/>
<!-- AAOL-4937 End -->
<home:returnOrderNavigatePopUp></home:returnOrderNavigatePopUp> 



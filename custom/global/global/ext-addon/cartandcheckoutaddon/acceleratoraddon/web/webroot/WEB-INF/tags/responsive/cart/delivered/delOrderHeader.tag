<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<c:url value="/cart/deliveredOrderFileUpload" var="DeliveredOrderFileUpload" />
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>

<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="hideSelectDropShip"/>
</c:if>
<div class="row jnjPanelbg">
	<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
		<!-- <a type="button" href="#" class="btn btnclsactive"
					data-toggle="modal" data-target="#enter-Surgery-info">Enter
					Surgery Info</a> -->
		<button type="button" class="btn btnclsactive"
			id="showSurgeryInfoPopup" data-toggle="modal">
			<spring:message code="cart.confirmation.surgeryInfo" />
		</button>

		<c:if test="${divisionData.isSpine or divisionData.isCodman}">
			<input type="hidden" id="surgeryData" value="${cartData.surgeryInfo}" />
			<div id="surgeryInfoError" style="display:none;" data-msg-required="<spring:message code="cart.required.field"/>">
			
<%-- 				<spring:message code="cart.common.surgeryInfo.enter" /> --%>
			</div>
			<div class="registerError surgeryInfo-error"></div>
		</c:if>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-3 col-xs-12 surgeon-name-holder">
		<c:if test="${divisionData.isSpine}">
			<div class="txt-label-inline surgeon-name-label valignTop">
				<span class=""><spring:message code="cart.common.surgeonName"></spring:message><span class="redStar">*</span>&nbsp;</span>
			</div>
			<div class="txt-box-inline surgeon-name-txt">
				<input type="text" class="form-control" id="surgeonName" name="surgeonName" readonly data-msg-required="<spring:message code="cart.required.field"/>"
					value="${cartData.surgeonName}" /> <a type="button" href=""
					id="selectSurgeon" data-toggle="modal"><spring:message
						code="cart.common.surgeonName.select"></spring:message></a>
						
				<div class="registerError surgeonName-error"></div>		
			</div>
			<!-- <div class="row">
				<div class="col-lg-offset-4 col-lg-8 ">
					<div class="pull-left registerError surgeonName-error"></div>
				</div>
			</div> -->
		</c:if>

		<c:if test="${divisionData.isMitek or divisionData.isCodman}">
			<div class="txt-label-inline surgeon-name-label valignTop">
				<span class=""><spring:message code="cart.common.surgeonName"></spring:message></span>
			</div>
			
			<div class="txt-box-inline surgeon-name-txt">
				<input type="text" class="form-control" id="surgeonName" readonly value="${cartData.surgeonName}" />
				 <a type="button" href="" id="selectSurgeon" data-toggle="modal">
				 <spring:message code="cart.common.surgeonName.select"></spring:message></a>
			</div>
			<div class="registerError"></div>
		</c:if>
		

	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">

		<c:if test="${divisionData.isSpine}">
			<div class="sales-rep-label">
				<spring:message code="cart.common.spinSalesRepUCN"></spring:message><span
					class="redStar">*</span>
			</div>
			<div class="sales-rep-txt">
				<select data-width="100%" class="selectpicker" id="spinSalesUCN" name="spinSalesUCN" data-msg-required="<spring:message code="cart.required.field"/>">
					<option value=""><spring:message code="cart.review.select" /></option>
					<c:forEach items="${salesRepUCN}" var="ucnValue">
						<option value="${ucnValue}" data="${ucnValue}"
							${ucnValue == cartData.spineSalesRepUCN ? 'selected="selected"' : ''}>${ucnValue}</option>
					</c:forEach>
				</select>
				<div class="registerError"></div>
			</div>
		</c:if>
		<c:if test="${divisionData.isMitek}">
			<div class="sales-rep-label">
				<label><spring:message code="cart.review.entry.specialSP"></spring:message></label><span
					class="redStar">*</span>&nbsp;
			</div>
			<div class="sales-rep-txt">
				<input type="text" class="form-control"
					value="${cartData.spineSalesRepUCN}" id="spinSalesUCN"
					name="spinSalesUCN"
					data-msg-required="<spring:message code="cart.required.field"/>" />
				<div class="registerError"></div>
			</div>
		</c:if>
	</div>
</div>

<div class="row jnjPanelbg">
	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
		<input type="hidden" id="attachDocName"
			value="${cartData.attachDocName}" /> <a type="button" id="UploadIt"
			href="" class="btn btnclsactive" data-toggle="modal"><spring:message
					code="cart.deliver.fileUpload.header"></spring:message></a>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
		<div class="checkbox checkbox-info">
			<input id="third-party-checkbox" ${cartData.thirdPartyBilling ? 'checked' : ''} class="styled" type="checkbox">
			<label for="third-party-checkbox"><spring:message
					code="cart.common.thirdparty.billing"></spring:message></label>
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 third-party-content">
		<div class="do-thirdparty-label">
			<label><spring:message code="cart.deliver.shipToAcc"></spring:message><span
				class="redStar">*</span>&nbsp;</label>
		</div>
		<div class="do-thirdparty-txt">
			<input type="text" class="form-control" value="${cartData.dropShipAccount}" id="dropShipDel"
				name="dropShip" />
			<div class="registerError"></div>
		</div>
		
		<div class="do-thirdparty-icon">
			<span class="drop-ship-account-list-icon fa fa-list changeShipAddLighboxLink ${hideSelectDropShip}" 
						data-toggle="modal" data-url="${dropShipURL}" id="selectDropShipLink"></span> <!-- data-target="#dropship-account-popup" -->
		</div>
		<div id="errorMsgDiv"></div>
		
			
	</div>
	<div class="col-md-offset-7 col-xs-offset-0 col-lg-5 col-md-5 col-sm-5 col-xs-12 distributor-po-holder third-party-content">
		<div class="do-thirdparty-label">
			<spring:message
					code="cart.deliveredOrder.distributor.po"></spring:message><span
				class="redStar">*</span>&nbsp;
		</div>
		<div class="do-thirdparty-txt">
			<input type="text" class="form-control" value="${cartData.distributorPONumber}" id="distPurOrderDel"
				name="distPurOrder" />
			<div class="registerError"></div>
		</div>
	</div>
	<c:set var="reqErrorMsg"><spring:message code="cart.required.field"/></c:set>
	<input id="thirdPartyErrorMsg" type="hidden" value="${reqErrorMsg}"/>

</div>
<!-- <div id="surgeryInfoPopupHolder"></div>
<div class="cartpage-surgeon" id="surgeonPopupHolder"></div> -->

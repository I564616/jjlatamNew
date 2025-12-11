<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>


<templateLa:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title"
	code="cart.page.title" />
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/checkout" var="checkoutUrl" />

<input type="hidden" value="${canValidateCart}" id="canValidateCart">
<input type="hidden" value="${displayBatchModeAlert}" id="displayBatchModeAlert">
	
			<a id="skip-to-content"></a>
			<!-- breadcrumb : START -->
			<div class="breadCrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<!-- breadcrumb : End -->
						
			<!-- globalMessages : START -->
			<div id="globalMessages">
				<common:globalMessages />
				<cart:cartRestoration />
				<cart:cartValidation />
			</div>
			<!-- globalMessages : END -->

			<!-- Check for add to cart by codes Errors -->
			<cart:addItemByCodeErrors cartModificationData="${cartModificationData}" />

			<div class="pageBlock shoppingCartPage stepDeliver1 standard">
					
					<h1><spring:message code="cart.review.shoppingCart"/></h1>
					<!--progressbarNav block starts-->
					<standardCart:progressBar step1="active" stepbar="1"/>
					<!--progressbarNav block ends-->
					<c:if test="${not empty customerExcludedError}">
							<div class="error">
							<p>
									<spring:message code="cart.common.excluded.customer.error" />
							</p>
						</div>
					</c:if>
					<form id="cartStep1Check" action="javascript:;" novalidate="novalidate">
						<!--Order Submission block starts-->
						<standardCart:cartPageHeader currentPage="cartPage"/>
						<c:set value="saveorderastemplate" var="disableTemplate"/>						
						<c:set value="disabled" var="disableInputBox"/>
						<!--Order Submission block ends-->
						<commonTags:cartPageActions/>
					</form>
				<!--Start: Quick Add to Cart -->
					<commonTags:quickAddToCart />
				<!--End: Quick Add to Cart -->

				<!-- Cart Entries-->
				<cart:cartEntries />

				<div class="sectionBlock buttonWrapperWithBG borDer">
					<div class="total">         	
						<p><span><spring:message code="cart.common.subTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.subTotal}" /></span></p>
					</div>		
				</div>
				<standardCart:cartPageActions/>
	</div>	
		<!-- GTR-1693 Starts Here -->
	  
	<!--<div class="hide" id="validateOrderDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><spring:message code="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock"><spring:message code="cons.cart.review.poNumber"/>
				<form method="post">
					<br>
					<div class='popupButtonWrapper txtRight'>						
						<span class="floatLeft">&nbsp;</span>
						<span><a class="tertiarybtn closePopup" href="javascript:;"><spring:message code="po.num.used.alert.close"/></a></span>
					</div>
				</form>
			</div>
		</div>
	</div> -->
  
	<div class="hide" id="validateOrderNumDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><spring:message code="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock">
			<font color=red><b><spring:message code="cart.controller.poorder.restriction"/></b></font><spring:message code="cart.controller.poorder.restriction1"/></br></br>
			<spring:message code="cart.controller.poorder.restriction2"/>
					<div class='popupButtonWrapper txtRight'>
						<span class="floatRight"><a id="validateOrderCancel"	class="tertiarybtn closePopup" href='#'><spring:message code="cart.review.cancel"/></a></span>
					</div>
			</div>
		</div>
	</div>
	
	<!-- GTR-1693 Ends Here -->
	<!-- Div elements for showing various pop up content on cart page -->
  	<standardCart:saveAsTemplateDiv/>
  	<commonTags:PONumUsedDiv/>	
	<commonTags:extendedTimeOutDiv/>
	<commonTags:changeAddressDiv/>
	<commonTags:cartErrors/>
</templateLa:page>
<commonTags:creditCartInfo/>

<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartAddTocart.js"></script>
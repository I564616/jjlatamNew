<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="home"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="quote"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/quote"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%> 
<!-- quote/cartPage.jsp -->

<template:page pageTitle="${pageTitle}">
	<div class="row jnj-body-padding" id="jnj-body-content">
		<div class="col-lg-12 col-md-12">
			<div id="AddItemsCartpage">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				<div class="row content">
					<div class="col-lg-6 col-md-6 col-sm-5 col-xs-12">
						<spring:message code="cart.quote.priceQuote" />
					</div>
					<div class="col-lg-6 col-md-6 col-sm-7 col-xs-12">
						<div class="float-right-to-none">
							<c:if test="${isEligibleForNewOrder}">
							<a href="#" type="button" data-toggle="modal" id="startNewButton" class="btn btnclsactive">
							<spring:message	code="cart.quote.convertcart" /></a> 
							</c:if>		
							<a href="#" type="button" class="btn btnclsactive validateprice no-margin-top">
							<spring:message	code="cart.quote.getPrice" /></a>
						</div>
					</div>
				</div>
				<div class="row" id="priceInquiry-nodata-error">
										<div class="col-md-12">
											<div class="panel-group">
												<div class="panel panel-danger">
													<div class="panel-heading">
													<span class="glyphicon glyphicon-ban-circlePlease"></span>
													<spring:message	code="cart.quote.product.error" />
													</div>
												</div>  
											</div>		
										</div>
									</div>
				<!-- KIT Added -->				
				 <c:if test="${not empty orthoKitProductsList}"> 
					<div class="error">
						<p>
							<spring:message code="cart.priceInquiry.orthoKit.msg1" />
							${orthoKitProductsList}
							<spring:message code="cart.priceInquiry.orthoKit.msg2" />
							<spring:message code="cart.priceInquiry.orthoKit.msg3" />
						</p>
					</div>
			 </c:if>
			 <!-- KIT Added -->
			 <div id="globalMessages">
					<common:globalMessages />
					<cart:cartRestoration />
					<cart:cartValidation />
				</div>
				<div class="mainbody-container">
					<commonTags:Addresses/>
					<commonTags:quickAddToCart addToCartLabelKey="cart.common.addToQuote" />
					<quote:cartEntries isQuoteResultPage="false" />
				</div>
					
			</div>

			<!--Accordian Ends here -->

			<!-- Start - Total Price Summary -->
			<div class="row basecontainer">
				<table class="total-summary-table">
					<tr>
						<td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
						<td class="total-summary-cost totalrps no-right-pad">--</td>
					</tr>
					<tr class="summary-bline">
						<td class="total-summary-label"><spring:message code="cart.review.entry.shipping"/></td>
						<td class="total-summary-cost">--</td>
					</tr>
					<tr class="total-price-row">
						<td class="total-summary-label"><spring:message code="cart.validate.total"/></td>
						<td class="total-summary-cost totalsum no-right-pad">
						 <sup class="supmd">--</sup> 
						</td>
					</tr>
				</table>
			</div>
			
		</div>
		<!-- End - Total Price Summary -->

		<div class="row validatebtn">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="float-right-to-none">
					<a href="#" type="button" class="btn btnclsactive validateprice">Get
						Price</a>

				</div>
			</div>
		</div>

	</div>

	<!-- Modal Shipping detail pop up-->
	<div class="modal fade jnj-popup" id="error-detail-popup" role="dialog">
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

<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="contractPopuppage">
			<!-- Modal -->
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
			<!-- Add to cart Modal pop-up to identify  contract or non contract end-->
		</div>
	<home:newOrder></home:newOrder>
	<commonTags:changeAddressDiv/> 
	<commonTags:changeBillToAddress/>

</template:page>


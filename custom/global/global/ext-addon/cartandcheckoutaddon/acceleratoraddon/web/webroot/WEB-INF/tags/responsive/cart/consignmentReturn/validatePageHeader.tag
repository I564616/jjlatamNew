<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="currentPage" required="false" type="java.lang.String" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>

<!-- replenish/validatePageHeader.tag -->
<!-- Hide select shipping if only one shipping addresses are less than 2 -->
<c:if test="${fn:length(shippingAddressess) lt 2}">
	<c:set value="strictHide" var="hideSelectShipping"/>
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>

<c:if test="${!cartData.thirdPartyBilling}">
	<c:set value="strictHide" var="hideThirdPartyFlag"/>
</c:if>

<!-- Hide drop ship selection button if no drop ship account is present -->
<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="img"/>
</c:if>

<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
	<c:set var="addOnVisibilityCss" value="disableTextbox"/>
	<c:set var="disabled" value="disabled"/>
</c:if>
<!-- AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!-- AAOL-6138 changes end -->
									<div class="row jnjPanelbg">											
											<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
												<div class="text-left">Account Number: <span>${user.currentB2BUnitID},${user.currentB2BUnitName}</span></div>												
											</div>
										</div>
										<commonTags:Addresses/>			
										<commonTags:cartErrors/>
										<commonTags:changeAddressDiv/> 
										<commonTags:changeBillToAddress/>
									<div class="row jnjPanelbg">
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="customer.po.number"></spring:message>
												<c:if test="${pOvalidation eq 'duplicate'}">
												<div style="color: #FF4500;" id="poDuplicate">
													<spring:message code="consignment.return.po.duplicate" />
												</div>
												</c:if>	
												</label>
												<div class="pull-left form-consignment-input-select">
													<input class="form-control" placeholder="${cartData.purchaseOrderNumber}" value="${cartData.purchaseOrderNumber}" disabled="disabled" id="purchOrder">
													
												</div>	
											</div>											
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 stockUserIphone">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="stock.user"></spring:message></label>
												<select class="form-control form-element form-consignment-select-large" disabled="disabled">
													<option>${cartData.stockUser}</option>											
												</select>
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 clearBoth margintop20px">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select" ><spring:message code="end.user"></spring:message></label>
												<select class="form-control form-element form-consignment-select-large" disabled="disabled">
													<option>${cartData.endUser}</option>											
												</select>
											</div>												
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select" d=><spring:message code="po.date"></spring:message></label>
												<div class="input-group form-element form-element-date">
												<!-- AAOL-6138 changes date format changed start -->
													<input id="po-date" name="toDate" value="<fmt:formatDate pattern = "${dateformat}" value = "${cartData.poDate}" />" class="date-picker form-control" type="text" disabled="disabled">
													<!-- AAOL-6138 changes date format changed end -->
													<label for="po-date" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
												</div>	
											</div>	
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="return.created.date"></spring:message></label>
												<div class="input-group form-element form-element-date">
												<!-- AAOL-6138 changes date format changed start -->
													<input id="request-date" name="toDate" value="<fmt:formatDate pattern = "${dateformat}" value = "${cartData.returnCreatedDate}" />"  class="date-picker form-control" type="text" disabled="disabled">
													<!-- AAOL-6138 changes date format changed end -->
													<label for="request-date" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
												</div>	
											</div>	
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
												<label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="shipping.instructions"></spring:message></label>
												<textarea class="form-control  form-consignment-input-select" rows="" id="comment" placeholder="${cartData.shippingInstructions}" disabled="disabled"></textarea>
											</div>											
										</div>	


	<%--
	<commonTags:customerInfoStrip/>
	 <div class="prodDeliveryInfo sectionBlock">
					<ul class="shippingAddress">
						<li>
							<div class="wordWrap">
								<div>
									<label for="purchOrder"><spring:message
											code="cart.common.purchaseOrder" /><span class="redStar"><spring:message code="cart.return.returnInfo.star"></spring:message></span></label>
								</div>
								<div>
									<span>${cartData.purchaseOrderNumber}</span>
								</div>
							</div>
							<div>
									<div>
									<c:if test="${not empty cartData.dropShipAccount}">
										<label for="dropShip"><spring:message code="cart.deliver.shipToAcc"/></label>
										</c:if>
									</div>
									<div>
										<span>${cartData.dropShipAccount}</span>
									 </div>
									</div>
									<div class=""><div class="registerError"></div></div>
						</li>
				
						<li>
							<div class="txtFont "><spring:message code="cart.common.ShipToAdd"/>
								<c:if test="${currentPage eq 'cartPage'}">							 
									<a href="javascript:;" class="changeAddressLightBoxLink ${hiddenFields.selectShipping} ${hideSelectShipping}">Change Address</a>
								</c:if>
							</div>
							<div id="deliveryAddressTag"><cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"/></div>
							<div>
								<div>
								<c:if test="${not empty cartData.attention}">
									<label for="attention"><spring:message code="cart.common.attention"/> </label>
									</c:if>
								</div>
								<div>
									<span>${cartData.attention}</span>
								</div>
							</div>     
						</li>
						<li>
							<div class="txtFont"><spring:message code="cart.common.BillingAdd"/></div>
							<div class="minHeight">
								<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />								
							</div>
						</li>

						<li class="last">
							<div>
								<span class="txtFont"><spring:message code="cart.common.paymentMethod"/></span>
							</div>
								<!-- Start  : Payment Info block for cart page -->
								<c:if test="${currentPage eq 'cartPage'}">								
										<c:set var="purOrderCheckValue" value="checked" />
										<c:set var="credutCardCheckValue" value="" />
										<c:set var="selectCardClass" value="hidden" /> 
										
										<c:if test="${cartData.paymentInfo != null}">
											<c:set var="purOrderCheckValue" value="" />
											<c:set var="credutCardCheckValue" value="checked" />
											<c:set var="selectCardClass" value="show" />
										</c:if>
			
										<div>
											<input type="radio" id="purOrder" name="radio"
												${purOrderCheckValue}> <label for="purOrder"><spring:message code="cart.review.orderInfo.purchaseOrder"/></label>
												</div>
								</c:if>	
								<!-- End  : Payment Info block for cart page -->
								
								<!-- Start  : Payment Info block for cart validation/Checkout page -->
								<c:if test="${currentPage eq 'cartValidationPage' || currentPage eq 'cartCheckoutPage' }">
					                <div><span><spring:message code="cart.review.orderInfo.purchaseOrder"/></span></div>
		                		</c:if>
		                		<!-- End : Payment Info block for cart validation/Checkout page -->
						</li>
						
					</ul>
				</div> --%>
				
				
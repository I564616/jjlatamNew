<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="currentPage" required="false" type="java.lang.String" %>
<!-- standard/cartPageHeader.tag -->
<!-- Hide select shipping if only one shipping addresses are less than 2 -->
<c:if test="${fn:length(shippingAddressess) lt 2}">
	<c:set value="strictHide" var="hideSelectShipping"/>
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>

<!-- Hide drop ship selection button if no drop ship account is present -->
<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="hideSelectDropShip"/>
</c:if>

<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
	<c:set var="addOnVisibilityCss" value="disableTextbox"/>
	<c:set var="disabled" value="disabled"/>
</c:if>

	<commonTags:customerInfoStrip showChangeTypeLink="true"/>
	
	<div class="prodDeliveryInfo sectionBlock">
					<ul class="shippingAddress">
						<li>
							<div>
								<div><label for="purchOrder"><spring:message code="cart.common.purchaseOrder"/><span
										class="redStar">*</span></label></div>
								<div><span><input id="purchOrder" maxlength="35" type="text"
										name="purchaseOrder" class="required"  ${disabled}
										data-msg-required="<spring:message code="cart.common.purchaseOrder.enter"></spring:message>"
										value="${cartData.purchaseOrderNumber}" maxlength="35"/></span></div>
								<div class=""><div class="registerError"></div></div>
							</div>
							<div class="${hiddenFields.distPurOrder} marTop55">
								<div>
									<label for="distPurOrder"><spring:message code="cart.common.disributorPO"/></label>
								</div>
								<div>
									<span><input type="text" id="stndDistPurOrder" name="distPurOrder"
									 class="distPurOrder  ${addOnVisibilityCss}"
									 data-msg-required="<spring:message code="cart.required.cartHeader.disPONumber"/>" 
									 value="${cartData.distributorPONumber}"></span>
								</div>
								<div class="">
									<div class="registerError"></div>
								</div>
							</div>
						</li>

						<li>
							<div class="txtFont "><spring:message code="cart.common.ShipToAdd"/>
							<c:if test="${(currentPage eq 'cartPage') && (indicator eq 'Distributor')}">
									<a href="javascript:;" class="changeAddressLightBoxLink ${hiddenFields.selectShipping} ${hideSelectShipping}"><spring:message code="cart.confirmation.changeAddress"/></a>
								</c:if></div>
								<div id="deliveryAddressTag" class="minHeight"><cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"/></div>
							<div>
								<div>
									<label for="attention"><spring:message code="cart.common.attention"/> </label>
								</div>
								<div>
									<span><input type="text" id="attention" name="attention"  class="${addOnVisibilityCss}"  ${disabled}
										value="${cartData.attention}"></span>
								</div>
							</div>
						</li>
						<li>
							<div class="txtFont"><spring:message code="cart.common.BillingAdd"/></div>
							  <div class="minHeight">
								<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />								
							</div>	
							<div class="${hiddenFields.enterDropShip} marTop23">
								<div>
									<label for="dropShip"><spring:message code="cart.common.dropShipAcc" /></label>
								</div>
								<div>
									<input type="text" id="dropShip" name="dropShip" class="${addOnVisibilityCss}"  ${disabled}
										data-msg-required="<spring:message code="cart.common.dropShipAcc.enter"></spring:message>"
										value="${cartData.dropShipAccount}"/> 
										<span class="changeShipAddLighboxLink ${hideSelectDropShip}" data-url="${dropShipURL}" id="selectDropShipLink">
											<img src="${themeResourcePath}/images/ship_account.gif" alt="Drop Ship">
										</span>
									<div id="errorMsgDiv"></div>
								</div>
								<div>
									<div class="registerError"></div>
								</div>
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
											<input type="radio" id="purOrder" name="radio" value="purchaseOrder"
												${purOrderCheckValue}> <label for="purOrder"><spring:message code="cart.review.orderInfo.purchaseOrder"/></label>
												<input type="radio" id="creditCard" name="radio" class="${hiddenFields.selectProvideCreditCard}" ${credutCardCheckValue} > <label class="${hiddenFields.selectProvideCreditCard}" for="creditCard"><spring:message code="cart.review.orderInfo.creditOrder"/></label>
										</div>
										<div class="${hiddenFields.selectProvideCreditCard} selectCard">
											<label for="selectCard"><spring:message code="cart.review.orderInfo.selectCard"/></label><select
												id="selectCard">
												<option value=""><spring:message code="cart.review.orderInfo.select"/></option>
												<c:forEach var="creditCardInfo" items="${creditCardsInfos}">
													<c:set var="selectedValue" value="" />
			
													<c:if test="${creditCardInfo.id == cartData.paymentInfo.id}">
														<c:set var="selectedValue" value="selected" />
													</c:if>
												<option value="${creditCardInfo.id}" ${selectedValue}>
														${creditCardInfo.cardType} - ${creditCardInfo.cardNumber} -
														${creditCardInfo.expiryMonth}/${creditCardInfo.expiryYear}</option>
												</c:forEach>
												 <c:if test="${selectedValue!='selected' && cartData.paymentInfo ne null}">													
													<option value="${cartData.paymentInfo.id}" selected>
														${cartData.paymentInfo.cardType} - ${cartData.paymentInfo.cardNumber} - 
														${cartData.paymentInfo.expiryMonth}/${cartData.paymentInfo.expiryYear}
													</option>	
												</c:if>
			
											</select> <a href="javascript:;" class="editCreditCardLightBoxLink" id="creditCardLightbox"><spring:message code="cart.review.orderInfo.addEditCard"/></a>
										</div>
								</c:if>	
								<!-- End  : Payment Info block for cart page -->								
						</li>
						
					</ul>
				</div>
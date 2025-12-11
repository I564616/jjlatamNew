<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>

<%@ attribute name="currentPage" required="false" type="java.lang.String" %>
<!-- replenish/cartPageHeader.tag -->
<!-- Hide select shipping if only one shipping addresses are less than 2 -->
<c:if test="${fn:length(shippingAddressess) lt 2}">
	<c:set value="strictHide" var="hideSelectShipping"/>
</c:if>

<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>

<!-- Hide drop ship selection button if no drop ship account is present -->
<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<%-- <c:set value="strictHide" var="hideSelectDropShip"/> --%>
</c:if>

<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
	<c:set var="addOnVisibilityCss" value="disableTextbox"/>
	<c:set var="disabled" value="disabled"/>
</c:if>
 <form:form id="replenishValidateCartForm" action="cart/updateAll" method="post" commandName="replenishValidateCartForm">
<div class="row jnjPanelbg">
	<div class="col-lg-7 col-md-7 col-sm-4 col-xs-12"> 
		<div class="txt-label-inline no-charge-reason"><spring:message code="cart.common.purchaseOrder"/><span class="redStar">*</span></div>
		<div class="txt-box-inline no-charge-select">
			<!-- <input type="text" class="form-control"> -->
			<span>
				<input id="purchOrder" type="text" name="purchaseOrder" class="form-control"  ${disabled}
										data-msg-required="<spring:message code="cart.common.purchaseOrder.enter"></spring:message>"
					value="${cartData.purchaseOrderNumber}" maxlength="35"/>
			</span>
				<div class="registerError"></div>
		</div>
	
	</div>
	<div class="col-lg-5 col-md-5 col-sm-4 col-xs-12">
			<div class="display-table-full">
								<div class="display-table-row">
									<div class="txt-label-inline dropshipAccntLabel"><spring:message code="cart.deliver.shipToAcc"/></div>
									<div class="table-cell-toblock">
										<div class="txt-box-inline  display-table-cell full-width">
											<input type="text" class="form-control" id="dropShip" value="${cartData.dropShipAccount}">
											                                            
									 	</div>
										
										<div class="txt-box-inline display-table-cell"> 
										<a href="#"  data-toggle="modal" >
										<span class="changeShipAddLighboxLink" data-url="${dropShipURL}"> 
											<button class="drop-ship-account-list-icon ${hideSelectDropShip}  fa fa-list" id="drop-ship-account-list-icon"></button>
											
											</span>
											</a>
										</div>
							    </div>		
							   <div class="display-table-row">
									<div class="txt-label-inline hidden-xs"></div>
									<div id="errorMsgDiv" class="registerError txt-box-inline" style="color: red;"></div>
								</div>	 
							</div>
						</div>
	</div>	
	</div>
 </form:form>
<%-- <commonTags:customerInfoStrip showChangeTypeLink="${not cartData.linkedWithDelivered}"/> --%>
	
	<%-- <div class="prodDeliveryInfo sectionBlock">
					<ul class="shippingAddress">
						<li>
							<div>
								<div><label for="purchOrder"><spring:message code="cart.common.purchaseOrder"/><span
										class="redStar">*</span></label></div>
								<div><span><input id="purchOrder" type="text"
										name="purchaseOrder" class="required"  ${disabled}
										data-msg-required="<spring:message code="cart.common.purchaseOrder.enter"></spring:message>"
										value="${cartData.purchaseOrderNumber}" maxlength="35"/></span></div>
								<div class=""><div class="registerError"></div></div>
							</div>
							<div class="marTop55">
									<div>
										<label for="dropShip"><spring:message code="cart.deliver.shipToAcc"></spring:message></label>
									</div>
									<div>
										<span> <input type="text" id="dropShip" name="dropShip"	value="${cartData.dropShipAccount}">
										</span>
										<span class="changeShipAddLighboxLink ${hideSelectDropShip}" data-url="${dropShipURL}" id="selectDropShipLink"> 
											<img src="${themeResourcePath}/images/ship_account.gif" alt="Drop Ship"></span>
											<div id="errorMsgDiv"></div>
										</div>
									<div class=""><div class="registerError"></div></div>				
							</div>
						</li>

						<li>
							<div class="txtFont "><spring:message code="cart.common.ShipToAdd"/></div>
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
						<li class="last">
							<div class="txtFont"><spring:message code="cart.common.BillingAdd"/></div>
							<div class="minHeight">
								<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />								
							</div>						
						</li>						
					</ul>
				</div> --%>
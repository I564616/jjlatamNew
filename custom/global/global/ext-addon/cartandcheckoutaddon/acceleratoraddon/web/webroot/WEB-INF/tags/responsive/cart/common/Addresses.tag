<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="row jnjPanelbg no-top-bottom-padding hidden-xs" id="jnj-head-panel">
	<div class="col-lg-1 col-md-1 col-sm-1 col-xs-12 toggle-link-holder">
		<a data-toggle="collapse" data-parent="#accordion" href="#addressBodycontent" class="toggle-link panel-collapsed clickontext">
		 <span id="emeo-toggle-icon" class="glyphicon glyphicon-plus"></span>
		</a>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shoppingart-panel-padding"	id="pi-shipToaddress-head">
		<span class="subhead boldtext">
		<spring:message	code="cart.shiptoAddress.header" /></span>
	</div>
	<div class="col-lg-1 col-md-1 col-sm-1 col-xs-12" id="pi-head-line">
		<div class="pi-line"></div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shoppingart-panel-padding" 	id="pi-billToaddress-head">
		<span class="subhead boldtext">
		<spring:message	code="cart.billtoAddress.header" /></span>
	</div>
</div>

<div class="row jnjPanelbg no-top-bottom-padding  panel-collapse collapse" id="addressBodycontent">
	<input type="hidden" name="makeThisAddrDefaultChk"	id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}" /> 
	<input	type="hidden" name="defaultChekAddid" id="defaultChekAddid"	value="${defaultChekAddid}">
	<div class="col-lg-1 col-md-1 col-sm-1 col-xs-12 toggle-link-holder hidden-xs"></div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shoppingart-panel-padding visible-xs" id="mobile-ship-header"></div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shoppingart-panel-padding"	id="pi-shipToaddress">
	<div class="pi-shipToaddress-drop" id="pi-shipToaddress-drop">
		<cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}" /></div>
		<div class="checkbox checkbox-info" id="shippingAddrDefaultChkDiv">
			<input id="shippingAddrDefaultChk" class="styled" type="checkbox">
			<label for="shippingAddrDefaultChk">
			 <spring:message code="cart.shipping.defaultaddress" />
			</label>
		</div>

		<c:choose>
			<c:when test="${shippingAddressess.size()>1}"><!-- AAOL-5386 -->
				<div class="shiptoAlternativeAddress">
					<a href="#" data-toggle="modal" data-target="#selectaddresspopup">
						<spring:message code="cart.shipto.alternateaddress" />
					</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="shiptoAlternativeAddress"></div>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-xs-12 visible-xs shoppingart-panel-padding"  id="mobile-bill-header"></div>
	<div class="col-lg-1 col-md-1 col-sm-1 col-xs-12 hidden-xs" id="pi-body-line">
		<div class="pi-line"></div>
	</div>

	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shoppingart-panel-padding" 	id="pi-billToaddress">
		<input type="hidden" name="makeThisAddrDefaultChangeChkForBilling"	id="makeThisAddrDefaultChangeChkForBilling" 	value="${makeThisAddrDefaultChangeChkForBilling}" /> 
		<input	type="hidden" name="defaultCheckforBillingAddid" id="defaultCheckforBillingAddid"	value="${defaultCheckforBillingAddid}">
		<div>
		<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" />
		</div>
		<div class="checkbox checkbox-info " id="billingAddrDefaultChkDiv">
			<input id="billingAddrDefaultChk" class="styled" type="checkbox">
			<label for="billingAddrDefaultChk"> <spring:message code="cart.shipping.defaultaddress" /></label>
		</div>
		<c:choose>
			<c:when test="${billingAddressess.size()>1}"><!-- AAOL-5386 -->
				<div class="shiptoAlternativeAddress">
					<a href="#" data-toggle="modal"
						data-target="#selectaddresspopupBillTO"> <spring:message
							code="cart.billto.alternateaddress" /> <!-- <a href="#">Bill-to alternative address</a> -->
					</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="shiptoAlternativeAddress"></div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
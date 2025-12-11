<!--validateEntries.tag -->
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="consignmentFillUpCart"     tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentFillUp"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>
<!-- consignmentFillUp/validateEntries.tag -->
 <div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
  <div class="row content">
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
      <spring:message code="cart.review.shoppingCart" />
    </div>
    <%-- <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
      <button type="button"
        class="btn btnclsactive pull-right validateprice shippingPage">
        <spring:message code="cart.review.shoppingCart.checkout" />
      </button>
    </div> --%>
    <div class="float-right-to-none">
      <div class="orderType-holder">
        <span class="orderType-label"><spring:message code="cart.common.orderType" /> </span> <span> <spring:message
            code="cart.common.orderType.${cartData.orderType}" />
        </span>
      </div>
      <!-- <a href="#" type="button" class="btn btnclsactive ConsignmentValidateBtn placeOrderBtn">Place Order</a> -->
      <button type="button" class="btn btnclsactive placeOrderBtnConsignment">
        <spring:message code="cart.common.placeOrder" />
      </button>
    </div>
  </div>
	<div class="mainbody-container"  id="consignmentOrderPage">
	<div class="row jnjPanelbg">
		<spring:message code="header.information.cart.number" /> <span>${user.currentB2BUnitID}, ${user.currentB2BUnitName}</span>
	</div>
	<commonTags:Addresses/>
	<consignmentFillUpCart:validatePageHeader currentPage="cartValidationPage"/>
		<c:choose>
			<c:when test="${splitCart}">
				<c:forEach items="${jnjCartDataList}" var="jnjCartData" varStatus="count">
					 <consignmentFillUpCart:multiValidateCartsEntries jnjCartData="${jnjCartData}" varRowCount="${count.count}"  /> 
				</c:forEach>
				<consignmentFillUpCart:cartTotals />
			</c:when>
			<c:otherwise>
				<consignmentFillUpCart:singleValidateCartEntry />
			</c:otherwise>
		</c:choose>
	</div>
	<!-- End - Total Price Summary -->

	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
				<%-- <button type="button" class="btn btnclsnormal checkout-clear-cart"
					id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button> --%>
				<div class="empty-btn continue-shop" >
                <cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
					<cms:component component="${feature}"/>
				</cms:pageSlot>
		   	</div>
			</div>
			<div class="float-right-to-none">
			<c:set value="saveorderastemplate" var="classForSaveTemplate" />
				<button type="button" class="btn btnclsnormal templatebtn  ${classForSaveTemplate}">
					<spring:message code="cart.review.shoppingCart.template" />
				</button>
				<button type="button" class="btn btnclsactive placeorder placeOrderBtnConsignment"><spring:message code="cart.common.placeOrder"/></button>
				<%-- <button type="button" class="btn btnclsactive shippingPage">
					<spring:message code="cart.review.shoppingCart.checkout" />
				</button> --%>
			</div>
		</div>
	</div>
</div>
   <commonTags:checkoutForm/>
   
   <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="placeOrderPopuppage">
			<!-- Modal -->
				<div class="modal fade" id="placeOrderpopup" role="dialog" data-firstLogin='true'>
					<div class="modal-dialog modalcls">
						<div class="modal-content popup">
							<div class="modal-header">
							<h4 class="modal-title selectTitle pull-left"><spring:message code="cart.review.progressBar.order"/></h4>
							  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
							</div>
							<div class="modal-body"> 
								<div class="continueques"><spring:message code="consignment.place.order.confirm.question"/></div>
							</div>											
							<div class="modal-footer ftrcls">
								<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-placeorder"><spring:message code="cart.common.cancel"/></a>
								<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-placeorder" ><spring:message code="contract.page.accept"/></button>
							</div>
						</div>
					</div>
				</div>
			<!-- Add to cart Modal pop-up to identify  contract or non contract end-->
		</div>
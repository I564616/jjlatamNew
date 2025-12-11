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
<%@ taglib prefix="returnConsignmentCart"     tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentReturn"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>
<!-- replenish/validateEntries.tag -->

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
      <button type="button" class="btn btnclsactive placeorder" data-toggle="modal" data-target="#dispute-order">
        <spring:message code="cart.common.placeOrder" />
      </button>
    </div>
  </div>
  <div class="mainbody-container" id="consignmentOrderPage">
		<%-- <commonTags:Addresses/> --%>
		<returnConsignmentCart:validatePageHeader currentPage="cartValidationPage"/> 
		<c:choose>
			<c:when test="${splitCart}">
				<c:forEach items="${jnjCartDataList}" var="jnjCartData" varStatus="count">
					<%-- <replenish:multiValidateCartsEntries jnjCartData="${jnjCartData}" varRowCount="${count.count}"  /> --%>
				</c:forEach>
				<returnConsignmentCart:cartTotals />
			</c:when>
			<c:otherwise>
				<returnConsignmentCart:singleValidateCartEntry />
			</c:otherwise>
		</c:choose>
		<!-- Table collapse for mobile device-->

		 <%-- <div class="visible-xs hidden-lg hidden-sm hidden-md">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message
								code="cart.validate.product" /></th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${splitCart}">
					<c:forEach items="${jnjCartDataList}" var="jnjCartData" varStatus="count">
						<standardCart:multiValidateCartsEntries	jnjCartData="${jnjCartData}" varRowCount="${count.count}"  />
					</c:forEach>
							<div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
								<div class="txtRight">
									<standardCart:cartTotals />
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<standardCart:singleValidateCartEntry />
							<div class="sectionBlock buttonWrapperWithBG borDer">
								<div class="txtRight">
									<standardCart:cartTotals />
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div> 

 --%>		
 
 <!--Accordian Ends here -->

		<!-- Start - Total Price Summary -->

	</div>
	<!-- End - Total Price Summary -->

	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
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
				<%-- <button type="button" class="btn btnclsactive shippingPage">
					<spring:message code="cart.review.shoppingCart.checkout" />
				</button> --%>
				<button type="button" class="btn btnclsactive placeorder" data-toggle="modal" data-target="#dispute-order"><spring:message code="cart.common.placeOrder"/></button>
			</div>
		</div>
	</div>

</div>

<!--  Changes Made for PurChase Order Pop Up -->
<%-- <div class="modal fade jnj-popup" id="validateOrderDivId-popup" role="dialog">
	<div class="modal-dialog modalcls modal-md" id="validateOrderPOpopup">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
				<h4 class="modal-title"><spring:message code="cart.review.validateOrder"/></h4>
			</div>
			<div class="modal-body">
				<spring:message code="cart.review.poNumber"/>
			</div>
			<div class="modal-footer">
			<c:url value="/cart/validate" var="orderValidateUrl" />
				<a href="#"  class="pull-left"  data-dismiss="modal">Cancel</a>
			<button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" onclick="location.href='${orderValidateUrl}'">Validate Order</button>
			</div>	
		</div>
	</div>
</div> --%>
<!--  Changes Made for PurChase Order Pop Up -->

<!-- Modal dropship account detail pop up-->
						<div class="modal fade jnj-popup-container" id="dispute-order" role="dialog" data-firstLogin='true'>
							<div class="modal-dialog modalcls">
								<div class="modal-content popup">
									<div class="modal-header">
									  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
									   <h4 class="modal-title selectTitle">Confirmation</h4>
									</div>
									<div class="modal-body"> 
										<div class="text-center "><spring:message code="order.submition.popup.msg"/> </div>
										
									</div>											
									<div style="margin-bottom:25px;">
										<div class="yesNo text-center">
										<button type="button" class="yesbtn btn btnclsnormal  placeConsignmentReturnOrderBtn" data-dismiss="modal"><spring:message code="cart.common.yes"/></button>
										<button type="button" class="nobtn btn btnclsactive" id="no-btn-placeorder" data-dismiss="modal"><spring:message code="cart.common.no"/></button>
										</div>
									</div>
								</div>
							</div>
						</div>
					
					<!-- End - Body Content -->
				
					
<commonTags:checkoutForm/>
	<%-- <c:choose>
<c:when test="${splitCart}">
<c:forEach items="${jnjCartDataList}" var="jnjCartData">
<replenish:multiValidateCartsEntries jnjCartData="${jnjCartData}"/>
</c:forEach>
<div class="sectionBlock buttonWrapperWithBG borDer">
	<div class="txtRight">
		<replenish:cartTotals/>
	</div>
</div>
</c:when>
<c:otherwise>
<replenish:singleValidateCartEntry/> 
<div class="sectionBlock buttonWrapperWithBG borDer">
	<div class="txtRight">
		<replenish:cartTotals/>
	</div>
</div>
</c:otherwise>
</c:choose> --%>
 
  
   
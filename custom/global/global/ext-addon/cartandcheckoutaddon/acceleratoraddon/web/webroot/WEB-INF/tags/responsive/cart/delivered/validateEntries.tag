<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="deliveredCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />

	<div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			<button type="button"
				class="btn btnclsactive pull-right validateprice shippingPage">
				<spring:message code="cart.review.shoppingCart.checkout" />
			</button>
		</div>
	</div>
	<div class="mainbody-container">


		<c:choose>
			<c:when test="${splitCart}">
				<c:forEach items="${jnjCartDataList}" var="jnjCartData"
					varStatus="count">
					<deliveredCart:multiValidateCartsEntries
						jnjCartData="${jnjCartData}" varRowCount="${count.count}" />
				</c:forEach>

				<deliveredCart:cartTotals />

			</c:when>
			<c:otherwise>
				<deliveredCart:singleValidateCartEntry />
			</c:otherwise>
		</c:choose>

		<!-- Table collapse for mobile device-->

		<%-- <div class="visible-xs hidden-lg hidden-sm hidden-md">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table-lines">
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
				<button type="button" class="btn btnclsnormal checkout-clear-cart"
					id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button>
				<div class="empty-btn continue-shop" >
	 <cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
												<cms:component component="${feature}"/>
											</cms:pageSlot>
											</div>
			</div>
			<div class="float-right-to-none">
				<c:set value="saveorderastemplate" var="classForSaveTemplate" />
					<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}">
						<spring:message code="cart.review.cartPageAction.saveTemplate" />
					</button>
				<button type="button" class="btn btnclsactive shippingPage">
					<spring:message code="cart.review.shoppingCart.checkout" />
				</button>
			</div>
		</div>
	</div>

</div>
<cart:saveAsTemplateDiv/>

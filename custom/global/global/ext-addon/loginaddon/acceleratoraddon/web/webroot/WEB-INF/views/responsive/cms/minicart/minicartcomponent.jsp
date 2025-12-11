<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

<!--changes start for URL modification because of mini cart logic changes and if user wants to see the mini cart then revert the changes. -->
<%-- <c:url value="/cart/miniCart/${totalDisplay}" var="refreshMiniCartUrl"/>
<c:url value="/cart/rollover/${component.uid}" var="rolloverPopupUrl"/> --%>

<c:url value="/cart/reviseOrder" var="refreshMiniCartUrl"/>
<c:url value="/cart/reviseOrder" var="rolloverPopupUrl"/>
<!--changes done for URL modification because of mini cart logic changes and if user wants to see the mini cart then revert the changes. -->
<script id="miniCartTemplate" type="text/x-jquery-tmpl">
/*<![CDATA[*/
	<dt><ycommerce:testId code="miniCart_items_label"><spring:message text="items" code="cart.items" arguments="{{= totalItems}}"/></ycommerce:testId> - </dt>
	<dd><ycommerce:testId code="miniCart_total_label">
			<c:if test="${totalDisplay == 'TOTAL'}">{{= totalPrice.formattedValue}}</c:if>
			<c:if test="${totalDisplay == 'SUBTOTAL'}">{{= subTotal.formattedValue}}</c:if>
			<c:if test="${totalDisplay == 'TOTAL_WITHOUT_DELIVERY'}">{{= totalNoDelivery.formattedValue}}</c:if>
	</ycommerce:testId></dd>
/*]]>*/
</script>

<script type="text/javascript"> // set vars
var rolloverPopupUrl = '${rolloverPopupUrl}';
var refreshMiniCartUrl = '${refreshMiniCartUrl}/?';
</script>
<c:url value="/cart/reviseOrder" var="cartUrl"/>
<input type="hidden" id="hddnCartUrl" value="${cartUrl}" />
<%-- <c:choose>
	<c:when test="${totalItems eq 0}">
		<c:set var="minicartStyle" value="shoppingcartDisabled" />
		<c:set var="minicartId" value="cart_content_disabled" />
		<c:set var="minicartHref" value="javascript:;" />
	</c:when>
	<c:otherwise> --%>
		<c:set var="minicartStyle" value="shoppingcart" />
		<c:set var="minicartId" value="cart_content" />
		<c:set var="minicartHref" value="${cartUrl}" />
	<%-- </c:otherwise> 
</c:choose>--%>
<div id="cart_header" >

	<!--AFFG-25522  -->
	 <a class="btn btn-default glyphicon glyphicon-shopping-cart"   href="${minicartHref}" id="miniCartHrefId">
	    <i class="bi bi-cart-fill"></i>
	 </a>
		    <div id="items-msg"><%-- <c:choose>
							<c:when test="${totalItems ne 0}"> --%>
								${totalItems}
							<%-- </c:when>
							<c:otherwise>
								<spring:message code="header.information.cart.empty" />
							</c:otherwise></c:choose> --%></div>
	
	<c:if test="${JNJ_SITE_NAME eq 'CONS'}">
		<div id="miniCartHeader">
			<div><spring:message code="cart.common.totalWeight"/><strong>${cartData.orderWeight}&nbsp;${cartData.orderWeightUOM}</strong></div>
			<div><spring:message code="cart.common.totalVolume"/><strong>${cartData.orderVolume}&nbsp;${cartData.orderVolumeUOM}</strong></div>	
		</div>
	</c:if>
</div>
<cart:rolloverCartPopup/>
<cart:addToCart/>
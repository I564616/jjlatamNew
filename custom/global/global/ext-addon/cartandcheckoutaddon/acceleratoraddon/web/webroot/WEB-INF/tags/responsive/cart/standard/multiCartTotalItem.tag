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
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="cartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
      	
<%-- <c:if test="${cartData.subTotal.value > 0}"> --%>
<!-- 	<p> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.subTotal"/> --%>
<!-- 		</span>  -->
<%-- 		<span class="jnjID"><format:price priceData="${cartData.subTotal}"/></span></p> --%>
<%-- </c:if> --%>
<%-- <c:if test="${cartData.totalFees.value ne '0.0'}"> --%>
<!-- 	<p class="slideDownButton"> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.fees"/> --%>
<!-- 		</span>  -->
<%-- 		<span class="jnjID"><format:price priceData="${cartData.totalFees}"/></span> --%>
<!-- 		<span class="iconExpandCollapse freightIcon" role="directionLeft" style="background-position: 0px -19px;"> </span> -->
<!-- 	</p> -->
<!-- 	<div>					 -->
<%-- 		<c:if test="${cartData.totalDropShipFee.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.dropShipFee" /> --%>
<!-- 				</span>  -->
<!-- 				<span class="jnjID"> -->
<%-- 					<format:price priceData="${cartData.totalDropShipFee}" /> --%>
<!-- 				</span> -->
<!-- 			</p> -->
<%-- 		</c:if>					 --%>
<%-- 		<c:if test="${cartData.totalminimumOrderFee.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.minimumOrderFee" /> --%>
<!-- 				</span> -->
<!-- 				<span class="jnjID"> -->
<%-- 				<format:price priceData="${cartData.totalminimumOrderFee}" /> --%>
<!-- 				</span> -->
<!-- 			</p> -->
<%-- 		</c:if> --%>
<%-- 		<c:if test="${cartData.totalFreightFees.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.freightCost"/> --%>
<!-- 				</span>  -->
<!-- 				<span class="jnjID"> -->
<%-- 			 		<format:price priceData="${cartData.totalFreightFees}"/> --%>
<!-- 				 </span> -->
<!-- 			</p> -->
<%-- 		</c:if> --%>
<!-- 	</div> -->
<%-- </c:if> --%>
<%-- <c:if test="${cartData.hsaPromotion.value > 0}"> --%>
<!-- 	<p> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.hsaPromotion"/> --%>
<!-- 		</span>  -->
<!-- 		<span class="jnjID"> -->
<%-- 			<format:price priceData="${cartData.hsaPromotion}"/> --%>
<!-- 		</span> -->
<!-- 	</p> -->
<%-- </c:if> --%>
<!-- <p> -->
<!-- 	<span> -->
<%-- 		<spring:message code="cart.validate.cartTotal.tax"/> --%>
<!-- 	</span>  -->
<%-- 	<span class="jnjID"> <format:price priceData="${cartData.totalTax}"/></span> --%>
<!-- </p> -->
<%-- <c:if test="${cartData.totalGrossPrice.value > 0}">				 --%>
<!-- 	<p class="totalSum"> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.orderTotal"/> --%>
<!-- 		</span>  -->
<!-- 		<span class="jnjID"> -->
<%-- 			<format:price priceData="${cartData.totalGrossPrice}"/> --%>
<!-- 		</span> -->
<!-- 	</p> -->
<%-- </c:if>		 --%>
<!-- </div> -->
<div class="row basecontainer">
										
												<table class="total-summary-table">
													<tr>
														<td class="total-summary-label"><spring:message code="cart.shipping.SubTotal"/></td>
														<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td> 
														
													</tr>
													<tr class="summary-bline">
														<td class="total-summary-label"><spring:message code="cart.shipping.shipping"/></td>
														<td class="total-summary-cost">--</td>
													</tr>
													<tr class="total-price-row">
														<td class="total-summary-label"><spring:message code="cart.shipping.total"/></td>
														<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${cartData.totalGrossPrice}" /></td>
														
													</tr>
												</table>
												
										</div>

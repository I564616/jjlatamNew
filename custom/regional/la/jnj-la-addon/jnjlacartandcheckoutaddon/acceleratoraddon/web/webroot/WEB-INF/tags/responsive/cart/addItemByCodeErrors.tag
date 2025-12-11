<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="cartModificationData" required="true" type="com.jnj.facades.data.JnjCartModificationData" %>

<c:set var="errorDivAdded" value="false"/>

	<c:if test="${fn:length(cartModificationData.cartModifications) gt 0}">
		<c:set var="errorCount" value="0" />
		<c:forEach items="${cartModificationData.cartModifications}" var="cartModificaion" varStatus="count">
			<c:set var="productID" value="${fn:trim(cartModificaion.entry.product.code)}"></c:set>
			<c:if test="${cartModificaion.statusCode=='Error' && productID ne ''}">
				<c:if test="${errorDivAdded=='false'}">
					<div class="error"><p><spring:message code="cart.review.productCodes"/>
					<c:set var="errorDivAdded" value="true"/>					
				</c:if>			
					${cartModificaion.entry.product.code},
					<c:set var="errorCount" value="${errorCount+1}"></c:set>				
			</c:if>
		</c:forEach>
		<c:if test="${errorCount gt 0}">
			<c:choose>
				<c:when test="${errorCount gt 1}">
					<spring:message code="cart.review.areInvalid"/>.</p></div>
				</c:when>
				<c:otherwise>
					<spring:message code="cart.review.isInvalid"/>.</p></div>
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${fn:length(cartModificationData.productsWithMaxQty) gt 0}">	
			<div class="error"><p><spring:message code="cart.review.maxQtyExceeded"/>.</p></div>			
		</c:if>
	</c:if>
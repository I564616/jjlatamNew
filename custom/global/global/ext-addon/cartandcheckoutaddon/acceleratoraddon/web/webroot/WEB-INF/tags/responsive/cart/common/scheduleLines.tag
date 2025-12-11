<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="scheduleLineList" required="false" type="java.util.List"%>
<%@ attribute name="count" required="false" type="java.lang.String"%>
<%@ attribute name="uom" required="false" type="java.lang.String"%>
<%@ attribute name="productName" required="false" type="java.lang.String"%>
<%@ attribute name="jjId" required="false" type="java.lang.String"%>
<div id="orderEntryDetail${count}" class="orderEntryDetail">
	<c:forEach var="scheduleLine" items="${scheduleLineList}">
		<div class="orderDetRow">
			<div class="floatLeft column1 paddingLeft"></div>
			<div class="floatLeft column2">
			<%-- Commented the below line as per the JJEPIC-657 --%>
			<%--<div class="prodImage"><p>${scheduleLine.lineNumber}-${fn:length(scheduleLineList)}</p></div>--%>
				<div class="orderProdDesc">
				<h4 title="${productName}">${productName}</h4>
				<p class="jnjID">J&amp;J ID#: <span class="strong">${jjId}</span></p>
				<span class="descSmall block"><spring:message code="product.detail.specification.status" />&nbsp;${scheduleLine.lineStatus}</span>
				</div>
			</div>
			<div class="floatLeft column3">
				<div class="lbox">
					<p><span class="block">${scheduleLine.quantity}</span></p>
					<p><span class="descSmall block">${uom}</span></p>
				</div>
			</div>
			<%--<div class="floatLeft column4">                
				<p><spring:message code="cart.confirmation.estimatedShipDate"/></p>
				<p><spring:message code="cart.confirmation.estimatedDeliveryDate"/></p>    
			</div>
			<div class="floatLeft column5">
				<p>
					<fmt:formatDate pattern="MM/dd/yyyy" value="${scheduleLine.shippingDate}" />
				</p>
				<p>
					<fmt:formatDate pattern="MM/dd/yyyy" value="${scheduleLine.deliveryDate}" />
				</p>
			</div> --%>
			
			
			<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
			
			<c:choose>
	<c:when test="${scheduleLine.lineStatus ne 'BACKORDERED'}">
			<div class="floatLeft column4">                
				<p><spring:message code="cart.confirmation.estimatedShipDate"/></p>
				<p><spring:message code="cart.confirmation.estimatedDeliveryDate"/></p>    
			</div>
			<div class="floatLeft column5">
				<p>
					<fmt:formatDate pattern="${dateformat}" value="${scheduleLine.shippingDate}" />
				</p>
				<p>
					<fmt:formatDate pattern="${dateformat}" value="${scheduleLine.deliveryDate}" />
				</p>
			</div>
	</c:when>
	<c:otherwise>
		<div class="floatLeft column4">                
				<p><spring:message code="cart.confirmation.materialAvailabilityDate"/></p>
			</div>
			<div class="floatLeft column5">
				<p>
					<fmt:formatDate pattern="${dateformat}" value="${scheduleLine.materialAvailabilityDate}" />
				</p>
				
			</div>
	</c:otherwise>
</c:choose> 
</div>
	</c:forEach>
</div> 
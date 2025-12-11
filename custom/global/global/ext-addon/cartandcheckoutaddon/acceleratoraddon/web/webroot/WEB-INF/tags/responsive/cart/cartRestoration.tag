<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:if test="${not empty restorationErrorMsg}">
	<div class="span-24">
		<div class="information_message negative">
				<span class="single"></span>
				<p><spring:theme code="${restorationErrorMsg}"/></p>
		</div>
	</div>
</c:if>
<c:if test="${not empty restorationData and not empty restorationData.modifications}">
	<div class="span-24">
		<div class="information_message neutral">
			<span class="single"></span>
			<p>
				<spring:theme code="basket.restoration"/>
				<c:forEach items="${restorationData.modifications}" var="modification">
					<br/>
					<c:url value="${modification.entry.product.url}" var="entryUrl"/>
					<spring:theme code="basket.restoration.${modification.statusCode}" 
						arguments="${modification.entry.product.name},${entryUrl},${modification.quantity},${modification.quantityAdded}"/>
				</c:forEach>
			</p>
		</div>
	</div>
</c:if>

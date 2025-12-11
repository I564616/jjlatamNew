<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:if test="${not empty validationData}">
	<c:set var="productLinkValidationTextDecoration" value="style=\"text-decoration: underline\""/>
	<c:forEach items="${validationData}" var="modification">
		<div class="information_message neutral">
			<span class="single"></span>
			<p>
				<c:url value="${modification.entry.product.url}" var="entryUrl"/>
				<spring:theme code="basket.validation.${modification.statusCode}"
					arguments="${modification.entry.product.name}###${entryUrl}###${modification.quantity}###
							${modification.quantityAdded}###${productLinkValidationTextDecoration}" argumentSeparator="###"/><br>
			</p>
		</div>
	</c:forEach>
</c:if>

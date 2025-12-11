<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>

<c:url value="${product.url}" var="productUrl"/>


<div id="quickview_lightbox" class="resizeableColorbox">
	<div class="span-8">
		<div class="prod_image">
			<a class="modal" href="${productUrl}">
				<product:productPrimaryImage product="${product}" format="product"/>
			</a>
			<c:url value="${product.url}/zoomImages" var="productZoomImagesUrl"/>
			<a class="modal" href="${productZoomImagesUrl}" target="_blank"><span class="details"></span></a>
		</div>
	</div>
	<div class="span-9 last">
		<div class="prod">
		
			<a href="${productUrl}">
				<h1>${product.name}</h1>
			</a>

			<product:productPricePanel product="${product}"/>

			<p>${product.summary}</p>
			<div class="bundle">
				<c:if test="${not empty product.potentialPromotions}">
					<c:choose>
						<c:when test="${not empty product.potentialPromotions[0].couldFireMessages}">
							<p>${product.potentialPromotions[0].couldFireMessages[0]}</p>
						</c:when>
						<c:otherwise>
							<p>${product.potentialPromotions[0].description}</p>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
			
			<div class="prod_review">
				<c:if test="${not empty product.reviews}">
					<span class="stars large" style="display: inherit;">
						<span style="width: <fmt:formatNumber maxFractionDigits="0" value="${product.averageRating * 24}" />px;"></span>
					</span>
					<p><fmt:formatNumber maxFractionDigits="1" value="${product.averageRating}" />/5</p>
				</c:if>
				<p class="prod_review-info">
					<c:if test="${not empty product.reviews}">
						<c:url value="${product.url}?tab=readreviews" var="productReadReviewsUrl"/>
						<a href="${productReadReviewsUrl}"><spring:theme code="review.based.on" arguments="${fn:length(product.reviews)}"/></a>
					</c:if>
				</p>
				<p class="prod_review-new">
					<c:url value="${product.url}?tab=writereview" var="productWriteReviewsUrl"/>
					<a href="${productWriteReviewsUrl}"><spring:theme code="review.write.title" /></a>
				</p>
			</div>

			<div class="quickview_lightbox-goto-product">
				<a href="${productUrl}"><spring:theme code="product.product.details.more"/></a>
			</div>

			<product:productAddToCartPanel product="${product}" allowAddToCart="${true}"/>

		</div>
	</div>
</div>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="format" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ attribute name="galleryImages" required="false" type="java.util.List" %>

<c:set value="${ycommerce:productImage(product, format)}" var="primaryImage"/>
<ul class="pgwSlideshow">

<c:if test="${product.isKitProduct eq true}">
<li class="kit-img"><c:choose>
			<c:when test="${not empty primaryImage}">
				<img src="${primaryImage.url}" alt="${product.name}" title="${product.name}" class="productdetail-img" />
			</c:when>
			<c:otherwise>
				<theme:image code="img.missingProductImage.${format}" alt="${product.name} photo unavailable"   title="${product.name}" />
				<c:if test="${product.isKitProduct eq true}">
					<img src="${webroot}/_ui/responsive/common/images/kit-img.png"	class="kit-img-thumb">
					<input type="hidden" value="${webroot}/_ui/responsive/common/images/kit-img.png" id="kit-img-src"/>
				</c:if>
				
			</c:otherwise>
		</c:choose></li>
</c:if>

<c:if test="${empty product.isKitProduct}">
<li><c:choose>
			<c:when test="${not empty primaryImage}">
				<img src="${primaryImage.url}" alt="${product.name}" title="${product.name}" class="productdetail-img" />
			</c:when>
			<c:otherwise>
				<theme:image code="img.missingProductImage.${format}" alt="${product.name} photo unavailable"  title="${product.name}" />
				
				
			</c:otherwise>
		</c:choose></li>
</c:if>


	<c:forEach items="${galleryImages}" var="container"	varStatus="varStatus">
		<li>
		<img src="${container.thumbnail.url}" data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${product.name}" title="${product.name}" />
		</li>
	</c:forEach>

</ul>

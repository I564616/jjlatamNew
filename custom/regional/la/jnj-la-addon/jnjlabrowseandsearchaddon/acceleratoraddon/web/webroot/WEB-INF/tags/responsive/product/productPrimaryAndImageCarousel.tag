<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="format" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ attribute name="galleryImages" required="false" type="java.util.List" %>
<c:set value="${ycommerce:productImage(product, format)}" var="primaryImage"/>
<ul class="pgwSlideshow">

    <li>
        <c:choose>
           <c:when test="${not empty productMainImage}">
           	 <img src="${productMainImage}" id="productMainImg" alt ="${imgAlttext}"/>
           </c:when>
           <c:otherwise>
              <theme:image code="img.missingProductImage.${format}" alt="${product.name} photo unavailable"  title="${product.name}" />
           </c:otherwise>
        </c:choose>
    </li>

    <c:forEach items="${galleryImages}" var="galleryImage" varStatus="count">
        <c:forEach var="entry" items="${galleryImage}">
          <li>
            <img src="${entry.value.url}" class="thumbnails" alt ="${imgAlttext}" id="img_${count.count}"/>
          </li>
        </c:forEach>
    </c:forEach>
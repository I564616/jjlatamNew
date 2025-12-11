<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="code" required="true" type="java.lang.String" %>
<%@ attribute name="alt" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<spring:theme code="${code}" text="/" var="imagePath"/>
<c:choose>
	<c:when test="${originalContextPath ne null}">
		<c:url value="${imagePath}" var="imageUrl" context="${originalContextPath}"/>
	</c:when>
	<c:otherwise>
		<c:url value="${imagePath}" var="imageUrl" />
	</c:otherwise>
</c:choose>

<%--<img src="/jnjb2bglobalstorefront/_ui/desktop/theme-jnj-emea/images/missing-product-72x72.jpg" alt="${alt}" title="${title}"  class="no-preview-img"/>--%>
<!-- KIT ADDED  -->
 <img src="${webroot}/_ui/responsive/common/images/img-not-available.jpg" alt="${alt}" title="${title}" class="no-preview-img productdetail-img"/> 

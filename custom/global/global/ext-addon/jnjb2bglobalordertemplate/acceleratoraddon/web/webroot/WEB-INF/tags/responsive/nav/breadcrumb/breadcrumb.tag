<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/home" var="homeUrl"/>
<a href="${homeUrl}"><spring:theme code="breadcrumb.home"/></a>
<c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="status">
	&#062;
	<c:choose>
		<c:when test="${breadcrumb.url eq '#'}">
			<a href="#" onclick="return false;"> ${breadcrumb.name}</a>
		</c:when>
		<c:otherwise>
			<c:url value="${breadcrumb.url}" var="breadcrumbUrl"/>
			<c:choose>
				<c:when test="${not status.last}">
					 <a href="${breadcrumbUrl}"> ${breadcrumb.name}</a>
				</c:when>
				<c:otherwise>
					 ${breadcrumb.name}
				</c:otherwise>
			</c:choose>						
		</c:otherwise>			
	</c:choose>
</c:forEach>

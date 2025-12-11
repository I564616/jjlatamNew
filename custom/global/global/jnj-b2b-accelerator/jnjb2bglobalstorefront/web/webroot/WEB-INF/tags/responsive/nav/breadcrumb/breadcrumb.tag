<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--Start AAOL-3813 Nav Bar Issue--%>
<c:url value="/home" var="homeUrl"/>
<ul class="breadcrumb pagNav" >
<%--Start AAOL-4661 Breadcrumb Changes Issue --%> 
     <li>
		<a href="${homeUrl}"><spring:theme code="breadcrumb.home"/></a>
	</li> 
<%--Start AAOL-4661 Breadcrumb Changes Issue --%> 
	<c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="status">
		
		<li <c:if test="${not empty breadcrumb.linkClass}">class="${breadcrumb.linkClass}"</c:if>>
			<c:choose>
				<c:when test="${breadcrumb.url eq '#'}">
					<%-- <input type="hidden" value="${breadcrumb.name}" id='BC' >  --%>
					<a href="#" onclick="return false;" <c:if test="${status.last}">class="last active"</c:if>>${breadcrumb.name} </a>
					<input type="hidden" value="${breadcrumb.name}" id='BC'> 	
				</c:when>

				<c:otherwise>
					<c:url value="${breadcrumb.url}" var="breadcrumbUrl"/>
					<a href="${breadcrumbUrl}" <c:if test="${status.last}">class="last active"</c:if>> ${breadcrumb.name} </a>
				 	<input type="hidden" value="${breadcrumb.name}" id='BC'> 	
				</c:otherwise>

			</c:choose>
		</li>

	</c:forEach>
</ul>
<%--End AAOL-3813 Nav Bar Issue--%>
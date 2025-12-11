<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- JS configuration --%>
<script type="text/javascript"> // set vars
	/*<![CDATA[*/
	<%-- Define a javascript variable to hold the content path --%>
	var ACC = { config: {} };
	ACC.config.contextPath = "${contextPath}";
	ACC.config.commonResourcePath = "${commonResourcePath}";
	ACC.config.themeResourcePath = "${themeResourcePath}";
	ACC.config.siteResourcePath = "${siteResourcePath}";
	ACC.config.rootPath = "${siteRootUrl}";
	<c:if test="${request.secure}"><c:url value="/search/autocompleteSecure"  var="autocompleteUrl"/></c:if>
	<c:if test="${not request.secure}"><c:url value="/search/autocomplete"  var="autocompleteUrl"/></c:if>
	ACC.autocompleteUrl = '${autocompleteUrl}';
	
	<c:forEach var="jsVar" items="${jsVariables}">
		<c:if test="${not empty jsVar.qualifier}" >
			ACC.${jsVar.qualifier} = '${jsVar.value}';
		</c:if>
	</c:forEach>
	
	/*]]>*/
</script>
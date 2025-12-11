<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/nav" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>


	<c:forEach items="${pageData.facets}" var="facet">
				<nav:facetNavRefinementFacet facetData="${facet}"/>
	</c:forEach>





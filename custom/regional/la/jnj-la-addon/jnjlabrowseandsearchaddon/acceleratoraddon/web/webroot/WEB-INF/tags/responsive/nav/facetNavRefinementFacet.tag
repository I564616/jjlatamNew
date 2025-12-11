<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:if test="${not empty facetData.values}">
        	<div class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey filterDivider">
        	<div class="ml-4">${facetData.name}</div>
		<ycommerce:testId code="facetNav_facet${facetData.name}_links">
				<div class="checkbox checkbox-info checkboxmargin allFacetValues"  style="${not empty facetData.topValues ? 'display:none' : ''}">
				
						<c:forEach items="${facetData.values}" var="facetValue">
								<c:if test="${facetData.multiSelect}">
									<form:form action="#" method="get">
									    <input type="hidden" name="showAllProducts" value="true"/>
										<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
										<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
										
										<input class="styled"type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''} onchange="$(this).closest('form').submit()"  />
										<label for="check1">
																					${facetValue.name}
											</label>
										<spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/>
									</form:form>
								</c:if>
								<c:if test="${not facetData.multiSelect}">
									<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
									<a href="${facetValueQueryUrl}&text=${searchPageData.freeTextSearch}">${facetValue.name}</a>
									<spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/>
								</c:if>
		          </c:forEach>
				</div>
		</ycommerce:testId>
			</div>			
</c:if>

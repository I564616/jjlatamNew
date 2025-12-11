<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<div class="search">
	<div class="headerHelp">
		<a href='<c:url value="/help"/>' class="secondarybtn">
			<spring:message code="header.information.help.link"/>
		</a>
	</div>
	<div class="searchForm">
		<form name="search_form" method="get" action="<c:url value="/search"/>">
			<ycommerce:testId code="header_search_input">
				<input id="search" class="text placeholder" type="text" name="text" maxlength="100" placeholder='<spring:message code="header.information.search.text"/>'/>
			</ycommerce:testId>
			<ycommerce:testId code="header_search_button">
				<input class="button" type="image" src="${themeResourcePath}/images/button/search.png" alt="Search" />
			</ycommerce:testId>
		</form>
	</div>
</div>
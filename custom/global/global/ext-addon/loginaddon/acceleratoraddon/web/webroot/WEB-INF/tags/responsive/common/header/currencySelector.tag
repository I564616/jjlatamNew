<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="currencies" required="true" type="java.util.Collection" %>
<%@ attribute name="currentCurrency" required="true" type="de.hybris.platform.commercefacades.storesession.data.CurrencyData" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${fn:length(currencies) > 1}">
	<c:url value="/_s/currency" var="setCurrencyActionUrl"/>
	<form:form action="${setCurrencyActionUrl}" method="post" id="currency-form">
		<spring:theme code="text.currency" var="currencyText"/>
		<label class="skip" for="currency-selector">${currencyText}</label>
		<ycommerce:testId code="header_currency_select">
			<select name="code" id="currency-selector">
				<c:forEach items="${currencies}" var="curr">
					<option value="${curr.isocode}" ${curr.isocode == currentCurrency.isocode ? 'selected="selected"' : ''}>
						${curr.symbol} ${curr.isocode}
					</option>
				</c:forEach>
			</select>
		</ycommerce:testId>
	</form:form>
</c:if>
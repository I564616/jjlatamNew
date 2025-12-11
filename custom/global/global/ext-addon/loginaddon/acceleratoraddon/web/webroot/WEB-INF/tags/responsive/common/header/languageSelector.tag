<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="languages" required="true" type="java.util.Collection" %>
<%@ attribute name="currentLanguage" required="true" type="de.hybris.platform.commercefacades.storesession.data.LanguageData" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<c:if test="${fn:length(languages) > 1}">
	<c:url value="/_s/language" var="setLanguageActionUrl"/>
	<form:form action="${wrappedContextPath}/_s/language" method="post" id="lang-form">
		<spring:message code="text.language" var="languageText"/>
		<label class="skip" for="lang-selector">${languageText}</label>
		<ycommerce:testId code="header_language_select">
		<div class="selectLangContainer">
		<label for="select_lang"><spring:message code="login.page.language.label"/></label>
			<select name="code" id="lang-selector">
				<c:forEach items="${languages}" var="lang">
					<c:choose>
						<c:when test="${lang.isocode == currentLanguage.isocode}">
							<option value="${lang.isocode}" selected="selected" lang="${lang.isocode}">
								${lang.nativeName}
							</option>
						</c:when>
						<c:otherwise>
							<option value="${lang.isocode}" lang="${lang.isocode}">
								${lang.nativeName} <%-- (${lang.name}) --%>
							</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			</div>
		</ycommerce:testId>
	</form:form>
</c:if>

								
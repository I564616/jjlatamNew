<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="languages" required="true" type="java.util.Collection" %>
<%@ attribute name="currentLanguage" required="true" type="de.hybris.platform.commercefacades.storesession.data.LanguageData" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>


	<c:url value="/_s/language" var="setLanguageActionUrl"/>
	<form:form action="${setLanguageActionUrl}" method="post" id="lang_form_${component.uid}" class="lang-form">
		<div class="control-group">
            <div class="controls">
                <ycommerce:testId code="header_language_select">
                    <label class="control-label skip stroke" for="lang_selector_${component.uid}">${languageText}
                        <messageLabel:message messageCode="profile.langString" />
                    </label>&nbsp;
                    <select name="code" id="lang_selector_${component.uid}" class="lang-selector">
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
                </ycommerce:testId>
            </div>
        </div>
	</form:form>

<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="idKey" required="true" type="java.lang.String" %>
<%@ attribute name="labelKey" required="false" type="java.lang.String" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="mandatory" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelCSS" required="false" type="java.lang.String" %>
<%@ attribute name="inputCSS" required="false" type="java.lang.String" %>
<%@ attribute name="tabindex" required="false" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ attribute name="errorMsg" required="false" type="java.lang.String" %>
<label class="${labelCSS}" for="${idKey}">
				<spring:message code="${labelKey}"/>
				<c:if test="${mandatory != null && mandatory == true}">
					<span>
						<spring:message code="login.required" var="loginrequiredText" />
					</span>
				</c:if>
				</label>
				
				<form:input cssClass="${inputCSS}" autocomplete="off" id="${idKey}" path="${path}" tabindex="${tabindex}" readonly="${readonly}" data-msg-required="${errorMsg}"/>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="idKey" required="true" type="java.lang.String" %>
<%@ attribute name="labelKey" required="true" type="java.lang.String" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="items" required="true" type="java.util.Collection" %>
<%@ attribute name="itemValue" required="false" type="java.lang.String" %>
<%@ attribute name="itemLabel" required="false" type="java.lang.String" %>
<%@ attribute name="mandatory" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelCSS" required="false" type="java.lang.String" %>
<%@ attribute name="selectCSSClass" required="false" type="java.lang.String" %>
<%@ attribute name="skipBlank" required="false" type="java.lang.Boolean" %>
<%@ attribute name="skipBlankMessageKey" required="false" type="java.lang.String" %>
<%@ attribute name="selectedValue" required="false" type="java.lang.String" %>
<%@ attribute name="tabindex" required="false" rtexprvalue="true" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ attribute name="errorMsg" required="false" type="java.lang.String" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>

<label class="${labelCSS}" for="${idKey}">
	<spring:message code="${labelKey}"/>
	<c:if test="${mandatory != null && mandatory == true}">
		<span>
			<spring:theme code="login.required" var="loginRequiredText" />
			*
		</span>
	</c:if>
</label>


<spring:message code='signup.secret.question.nothing.selected' var="nothingSelected"/>
<form:select id="${idKey}" path="${path}" cssClass="${selectCSSClass}" tabindex="${tabindex}" disabled="${disabled}"  data-msg-required="${errorMsg}" data-none-selected-text="${nothingSelected}" >
	<c:if test="${skipBlank == null || skipBlank == false}">
		<option value=""  selected="${empty selectedValue ? 'selected' : ''}"><spring:theme code='${skipBlankMessageKey}'/></option>
	</c:if>
	<form:options items="${items}" itemValue="${not empty itemValue ? itemValue :'code'}" itemLabel="${not empty itemLabel ? itemLabel :'name'}"/>
</form:select>




<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sec:authorize ifAnyGranted="ROLE_CUSTOMERGROUP">
    <li class="logged_in"><ycommerce:testId code="header_LoggedUser"><spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" htmlEscape="true"/></ycommerce:testId></li>
</sec:authorize>
<c:if test="${navigationNode.visible}">
    <c:forEach items="${navigationNode.links}" var="link">
        <cms:component component="${link}" evaluateRestriction="true" element="li" />
    </c:forEach>
</c:if>
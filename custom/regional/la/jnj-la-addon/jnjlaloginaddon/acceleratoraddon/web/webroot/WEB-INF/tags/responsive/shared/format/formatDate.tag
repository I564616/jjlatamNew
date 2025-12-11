<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ attribute name="dateToFormat" required="true" type="java.util.Date"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:choose>
    <c:when test="${'en' eq sessionLanguage}">
        <span class="txtFont">&nbsp;<fmt:formatDate value="${dateToFormat}" pattern="MM/dd/yyyy"/></span>
    </c:when>
    <c:otherwise>
        <span class="txtFont">&nbsp;<fmt:formatDate value="${dateToFormat}" pattern="dd/MM/yyyy"/></span>
    </c:otherwise>
</c:choose>
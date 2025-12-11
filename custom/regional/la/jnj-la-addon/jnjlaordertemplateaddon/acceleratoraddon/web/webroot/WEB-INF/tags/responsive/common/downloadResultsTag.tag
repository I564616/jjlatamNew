<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="useLinks" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld" %>

 <span>
	 <span class="link-txt boldtext">
	    <spring:message code="product.search.download"/>
    </span>
    <c:choose>
        <c:when test="${'true' eq useLinks}">
            <a href="#" class="tertiarybtn pdfdownloadlinks" id="excelDownload">
                <spring:message code="reports.excel.label" />
            </a>
            &nbsp;|&nbsp;
            <a href="#" class="tertiarybtn marginRight pdfdownloadlinks" id="pdfDownload">
                <spring:message code="reports.pdf.label" />
            </a>
        </c:when>
        <c:otherwise>
            <input type="submit" class="tertiarybtn pdfdownloadlinks" value="<spring:message code="reports.excel.label" />" name="downloadType"/> | <input type="submit" class="tertiarybtn marginRight pdfdownloadlinks" value="<spring:message code="reports.pdf.label" />" name="downloadType"/>
        </c:otherwise>
    </c:choose>
 </span>
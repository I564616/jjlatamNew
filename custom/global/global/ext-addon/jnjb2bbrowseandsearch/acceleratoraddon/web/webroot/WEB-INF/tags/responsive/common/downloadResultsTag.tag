<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

<strong><spring:message code="product.search.download" /></strong>
&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" class="tertiarybtn pdfdownloadlinks" value="XLS" name="downloadType" />
<span class="pipesymbol">|</span>
&nbsp;
<input type="submit" class="tertiarybtn marginRight pdfdownloadlinks" value="PDF" name="downloadType" />
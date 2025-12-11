<%@ tag trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderData" required="true"
	type="com.jnj.facades.data.JnjGTCutReportOrderData"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/desktop/nav"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/desktop/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/desktop/common/footer"%>
<%@ taglib prefix="report" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/cutReport" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<div class="reportRow">
	<div class="column1">
		<span class="showViewDetails detailTxt"><label:message
				messageCode="cutReport.header.column1.label" /></span>
	</div>
	<div class="column2">
		<span>${orderData.accountNumber}</span>
	</div>
	<div class="column3 pONumber wordWrap">
	
		<span><c:if test="${not empty orderData.PONumber && orderData.PONumber ne null}">${orderData.PONumber}</c:if> </span>
	</div>
	<div class="column4 orderNumber">
		<c:url value="/order-history/order/${orderData.hybrisOrderNumber}" var="orderDetailUrl" />
		<span><a href="${orderDetailUrl}"><c:if test="${not empty orderData.orderNumber && orderData.orderNumber ne null}">${orderData.orderNumber}</c:if></a></span>
	</div>
	<div class="column5">
		<span><c:if test="${not empty orderData.orderDate && orderData.orderDate ne null}">
		<fmt:formatDate pattern="${dateformat} value="${orderData.orderDate}"/>
		</c:if>
		
		</span>
	</div>
	<div class="column6">
		<span><c:if test="${not empty orderData.shipToName && orderData.shipToName ne null}">${orderData.shipToName}</c:if></span>
	</div>
</div>
<report:orderLineRow/>
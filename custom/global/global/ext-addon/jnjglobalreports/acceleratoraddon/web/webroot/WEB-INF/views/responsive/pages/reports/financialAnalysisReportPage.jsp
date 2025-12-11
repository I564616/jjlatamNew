<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="reports" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/common" %>
<%@ taglib prefix="financialAnalysis" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/financialAnalysisReport" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<!-- AAOL #2419 -->
<template:page pageTitle="${pageTitle}">
	<div id="Reportspage">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row content">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<spring:message code='category.financial.Analysis' />
			</div>
		</div>
		<reports:common />
    <input id="isFinancialSummary" value="true" type="hidden">
		 <c:choose>
			<c:when test="${selectedReport eq 'invoiceReport'}">
				<financialAnalysis:invoiceReport/>
			</c:when>
			<c:when test="${selectedReport eq 'invoicePastDue'}">
				<financialAnalysis:invoiceDueReport/>
			</c:when>
				<c:when test="${selectedReport eq 'financialSummary'}">
				<financialAnalysis:financialSummary/>
			</c:when>
			<c:when test="${selectedReport eq 'invoiceClearing'}">
				<financialAnalysis:invoiceClearingReport/>
			</c:when>
			
			<c:otherwise>
			</c:otherwise>
		</c:choose>

	</div>
</template:page>
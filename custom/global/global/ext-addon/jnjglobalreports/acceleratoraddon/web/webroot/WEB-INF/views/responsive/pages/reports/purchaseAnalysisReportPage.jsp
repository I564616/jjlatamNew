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
<%@ taglib prefix="reports" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/purchaseAnalysisReport" %>
<%@ taglib prefix="oaReports" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/orderAnalysisReport" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<template:page pageTitle="${pageTitle}">
				<div id="Reportspage">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
						<div class="row content">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"><spring:message code='reports.report.header' /></div>
						</div>

						<c:choose>
							<%-- Multiple PA Report Condition --%>
							<c:when test="${jnjGlobalMultiPurchaseAnalysisReportForm eq null}">
								<c:set var="pageStyle" value="singlePurchasePage" />
							</c:when>
							<%-- Single PA Report Condition --%>
							<c:otherwise>
								<c:set var="pageStyle" value="multiPurchasePage" />
							</c:otherwise>
						</c:choose>

							<%-- <div class="pageBlockDivision sectionBlock ${pageStyle}"> --%>
								<!--Left Panel Starts-->
								<c:choose>
									<%-- Multiple PA Report Condition --%>
									<c:when test="${jnjGlobalMultiPurchaseAnalysisReportForm ne null}">
										<reports:multiProductPurchaseAnalysisReport />
									</c:when>
									<c:when test="${jnjGlobalBackorderReportForm ne null}">
										<reports:backOrderAnalysisReport/>
									</c:when>
									<c:when test="${cutReportForm ne null}">
										<reports:cutAnalysisReport/>
									</c:when>
									<c:when test="${jnjGlobalOADeliveryListReportForm ne null}">
										<oaReports:deliveryList />
									</c:when>
									<%-- Single PA Report Condition --%>
									<c:otherwise>
										<reports:singleProductPurchaseAnalysisReport />
									</c:otherwise>
								</c:choose>
						<!-- 	</div> -->

					</div>
</template:page>
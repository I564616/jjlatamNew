<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row jnj-panel-report multiPurchaseReportBlock rdiCont sectionBlock">
	<div class="col-lg-12 col-md-12">
		 <div class="row jnj-panel-header">
			<div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
				<div class="amazon">
					<spring:message code='reports.account.label' />&nbsp;
						<span id="selectedAccountsText">
							<c:choose>
								<c:when test="${not empty currentAccountId}">
									${currentAccountId}
								</c:when>
								<c:otherwise>
									${accountsSelectedValue}
								</c:otherwise>
							</c:choose>
						</span>
						<c:if test="${showChangeAccountLink eq true}">
							<a id="accountSelectionLink" href="javascript:; " class="change">
								<spring:message code='reports.purchase.analysis.change' />
							</a>
						</c:if>
				</div>
				<input type="hidden" id="totalAccountsInModal"/>
			</div>
			<c:if test="${showAccounts ne true}">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="checkbox checkbox-info float-right-to-none selectchkbox">
						 <input id="check4" class="styled selectAllAccount" type="checkbox"> 
						 <label	for="check4"><spring:message code='reports.account.selection.all'/></label> 
					</div>
				</div>
			</c:if>
		</div> 
		<div class="row jnj-panel-body">
			<div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12">
				<label class="form-label form-label-select boldtext"><spring:message code='reports.report.category'/></label>	
				<select class="form-control form-element form-element-select" id="reportCategory">
					<c:forEach items="${reportCategory}" var="category">
						<c:if test="${user.financialAnalysisEnable}">
							<c:choose>
								<c:when test="${categoryUrl eq category.key}">
									<option value="${category.value}/>" selected="selected"><spring:message code='category.${category.key}.Analysis'/></option>							
								</c:when>
								<c:otherwise>
									<option value="${category.value}"><spring:message code='category.${category.key}.Analysis'/></option>	
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${not user.financialAnalysisEnable || user.financialAnalysisEnable eq null}">
							<c:choose>
								<c:when test="${categoryUrl eq category.key}">
									<option value="${category.value}" selected="selected"><spring:message code="category.${category.key}.Analysis"/></option>							
								</c:when>
								<c:otherwise>
									<c:if test="${category.key eq 'financial'}">
										<option value="${category.value}" style="display:none;"><spring:message code='category.${category.key}.Analysis'/></option>
									</c:if>	
									
									<c:if test="${category.key ne 'financial'}">
										<option value="${category.value}"><spring:message code='category.${category.key}.Analysis'/></option>
									</c:if>	
									
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>													
				</select>
			</div>
			<div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12 col-xs-12">
				<label class="form-label form-label-select boldtext"><spring:message code='reports.report.type'/></label>												
				<select class="form-control form-element form-element-select" id="reportTypes">
					<c:forEach items="${reportsType}" var="reportType">
						<c:if test="${siteName eq 'MDD'}">
							<c:choose>
								<c:when test="${inventry eq true}">
									<c:if test="${reportType.key eq 'boReport' || reportType.key eq 'inventoryReport' || 
										reportType.key eq 'consignmentReport' || reportType.key eq 'invoiceReport' || 
										reportType.key eq 'invoicePastDue' || reportType.key eq 'invoiceClearing' || reportType.key eq 'financialSummary' || reportType.key eq 'salesReport' || reportType.key eq 'deliveryList' 
										|| reportType.key eq 'singleProductReport' || reportType.key eq 'multiProductReport'}">
										<c:choose>
											<c:when test="${subReportUrl eq reportType.key}">
												<option value="${reportType.value}" selected="selected"><spring:message code='${reportType.key}.la.reportType'/></option>							
											</c:when>
											<c:otherwise>
												<option value="${reportType.value}"><spring:message code='${reportType.key}.la.reportType'/></option>	
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:when>
								<c:otherwise>
									<c:if test="${reportType.key eq 'boReport' || 
										reportType.key eq 'consignmentReport' || reportType.key eq 'invoiceReport' || 
										reportType.key eq 'invoicePastDue' || reportType.key eq 'invoiceClearing' || reportType.key eq 'financialSummary' || reportType.key eq 'salesReport' || reportType.key eq 'deliveryList' 
										|| reportType.key eq 'singleProductReport' || reportType.key eq 'multiProductReport'}">
										<c:choose>
											<c:when test="${subReportUrl eq reportType.key}">
												<option value="${reportType.value}" selected="selected"><spring:message code='${reportType.key}.la.reportType'/></option>							
											</c:when>
											<c:otherwise>
												<option value="${reportType.value}"><spring:message code='${reportType.key}.la.reportType'/></option>	
											</c:otherwise>
										</c:choose>
									</c:if>
										
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>																		
				</select>
			</div>
			<c:if test="${showCurrency eq true}">
				<div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12 col-xs-12">
						<label class="form-label currency-label-select-large"><spring:message code='reports.la.Currency'/></label>	
						<div class="currencyVal">
							<span class="curCode">${currentCurrency.isocode}</span>
							<span class="curRegion">${currentCurrency.name}</span>
						</div>
				</div>
			</c:if>
		</div>
	</div>
</div>
<div id="changeAccountPopupContainer"></div>

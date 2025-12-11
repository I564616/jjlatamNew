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
								<c:forEach var="accountIds" items="${currentAccountId}">
									${accountIds}
									</c:forEach>
								</c:when>
								<c:otherwise>
								<c:forEach var="accountIds" items="${accountsSelectedValue}">
									${accountIds}
									</c:forEach>
									
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

	</div>
</div>
<div id="changeAccountPopupContainer"></div>
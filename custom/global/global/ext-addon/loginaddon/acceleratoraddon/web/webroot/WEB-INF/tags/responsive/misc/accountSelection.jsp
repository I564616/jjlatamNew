<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/responsive/company"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:choose>
	<c:when test="${fetchAll ne true}">
		<div id="changeAccountPopupContainer" style="display: none;">
			<input type="hidden" id="totalResultsAccountSelection" value="${accountPaginationData.totalNumberOfResults}" />
			<div class="lightboxtemplate changeAccountBasketPopUp">
				<h2><spring:message code='reports.account.selection.header'/></h2>
				<div class="ttBlock">
					<ul>
						<li>
							<div><label for="searchAccount"><spring:message code='reports.account.selection.info'/></label></div>
							<div><span><spring:message code='reports.account.selection.selected'/>&nbsp;:&nbsp;</span><span class="selectedAccountsText">0</span></div>
							<div>
								<input type="text" placeholder="<spring:message code='account.change.searchText'/>" value="${accountSearchTerm}" id="selectAccountNoAjax" class="placeholder changeContractSearch" style="margin-right: 5px;">
								<input type="button" class="secondarybtn searchSelectAccountBtn" value="<spring:message code='reports.search.label'/>" id="searchSelectAccountBtn" style="margin-right: 5px;"/>
								<c:choose>
									<c:when test="${accountSearchTerm eq null || empty accountSearchTerm}">
										<input type="button" class="tertiarybtn clearSelectAccountBtn" value="Clear" id="clearSelectAccountBtn" disabled="disabled"/>
									</c:when>
									<c:otherwise>
										<input type="button" class="primarybtn clearSelectAccountBtn" value="Clear" id="clearSelectAccountBtn"/>
									</c:otherwise>
								</c:choose>
							</div>
						</li>
					</ul>	
				</div>
				<div id="selectAccountResultWindow">
					<company:selectAccounts/>
					<div class="popupButtonWrapper txtRight marTop10" >
						<span class="floatLeft">
							<input type="button" class="tertiarybtn accountSelectionCancel" value="<spring:message code='reports.account.selection.cancel' />" />
						</span>
						<span><input type="button" value="<spring:message code='reports.account.selection.ok'/>" id="accountSelectionOk" class="primarybtn"></span>
					</div>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>{"totalNumberOfResults":"${accountPaginationData.totalNumberOfResults}","accounts":"<c:forEach items="${accountList}" var="accountsObj" varStatus="status">${accountsObj.key}<c:if test="${not status.last}">,</c:if></c:forEach>"}
	</c:otherwise>
</c:choose>
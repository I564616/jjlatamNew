<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="isAdminUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isFirstTimeLogin" required="false" type="java.lang.Boolean" %>

<div id="changeAccountPopupContainer" style="display: none;">
	<input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
	<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
	<input id="currentPage" value="${currentPage}" type="hidden" />
	<div class="lightboxtemplate changeAccountBasketPopUp">
		<h2><spring:message code="account.change.title"/></h2>
		<input type="text" placeholder="<spring:message code='account.change.searchText'/>" value="${accountSearchTerm}" id="changeAccountNoAjax" class="placeholder changeContractSearch" style="margin-right: 5px;"/>
		<input type="button" class="secondarybtn searchSelectAccountBtn" value="<spring:message code='reports.search.label'/>" id="searchSelectAccountBtn" style="margin-right: 5px;"/>
		<c:choose>
			<c:when test="${accountSearchTerm eq null || empty accountSearchTerm}">
				<input type="button" class="tertiarybtn clearSelectAccountBtn" value="<spring:message code='changeAccount.clear'/>" id="clearSelectAccountBtn" disabled="disabled"/>
			</c:when>
			<c:otherwise>
				<input type="button" class="primarybtn clearSelectAccountBtn" value="<spring:message code='changeAccount.clear'/>" id="clearSelectAccountBtn"/>
			</c:otherwise>
		</c:choose>
		<div id="changeAccountPopup" class="changeAccountContent changeAccountColorBox">
			<c:if test="${isFirstTimeLogin ne null}">
				<ul class="accountListPopUpUL">
					<%-- Iterating over the accounts map --%>
					<c:forEach items="${accountList}" var="accountsObj" varStatus="count">
						<li id="accountListPopUp" class="accountListPopUp ${(count.count % 2 eq 0) ? 'even' : 'odd'}">
							<a class="changeAccountUnit" href="javascript:;">
								<input id="accountUID" value="${accountsObj.key}" type="hidden" />
								<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
								<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
								<%-- ACCOUNT UID --%>
								${accountsObj.key}
								<br />
								<%-- ACCOUNT NAME --%>
								${fn:split(accountsObj.value, '_')[1]}
								<c:if test="${fn:split(accountsObj.value, '_')[2] ne ' '}">
									<br />
									<%-- ACCOUNT CITY / STATE --%>
									${fn:split(accountsObj.value, '_')[2]}
								</c:if>
								<br />
								<span class="hidden">
									<%-- HIDDEN ACCOUNT GLN : to facilitate filter --%>
									${fn:split(accountsObj.value, '_')[0]}
								</span>
							</a>
						</li>
					</c:forEach>
					<c:if test="${empty accountList}">
						<center class="marTop20">
							<p><spring:message code="search.no.results"/></p>
						</center>
					</c:if>
				</ul>
			</c:if>
		</div>
		<a style="visibility:hidden;display: block;text-align: center;" href="javascript:;" class="loadMoreAccounts"><spring:message code='cart.common.loadMore'/></a>
	</div>
</div>
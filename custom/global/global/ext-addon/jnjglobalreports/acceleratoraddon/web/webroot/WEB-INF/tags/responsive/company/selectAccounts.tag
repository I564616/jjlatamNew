<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/desktop/template" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
<input id="currentPage" value="${currentPage}" type="hidden" />

<div class="row checbox-btn-row" id="selectAllDiv">
	<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12 for-landscape-mobile">
		<div class="checkbox checkbox-info selectchkbox">
			<%-- <input id="contract-select-all" class="styled contract-thead-chckbox"	type="checkbox">
			 <label for="contract-select-all" id="contract-head-chck-label">Select All</label> --%>
			 
			 <input type="checkbox" class = "accountSelectionAll styled contract-thead-chckbox" id="selectAllAccount" name="selectAllAccount">
			 <label for="contract-select-all"><spring:message code='reports.account.selection.all'/></label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 for-landscape-mobile">
		<div class="float-right-to-none">

			<button type="button" class="btn btnclsactive searchbtn primarybtn accountSelectionOk" id="accountSelectionOk"><spring:message code='reports.account.selection.ok'/></button>
		</div>
	</div>
</div>	
<div class="modalbody contract-detail-desktab modal-account-yscroll">
<c:forEach items="${accountList}" var="accountsObj" varStatus="count">
	<div class="accountsMargin">
		<div class="checkbox checkbox-info selectchkbox display-table-cell">
			<!-- <input id="contract-check-1" class="styled contract-tcell-chckbox"	type="checkbox"> -->
			<input type="checkbox" name="accounts" class="accountSelection styled contract-tcell-chckbox" id="accountSelection_${count.count}" value="${accountsObj.key}" data="${accountsObj.value}"/>
			 <label for="contract-check-1"></label>
			 
		</div>
		<div class="contract-check-content display-table-cell changeAccountUnit" for="accountSelection_${count.count}" id="${count.count}">
		<input id="accountUID" value="${accountsObj.key}" type="hidden" />
		<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
		<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
		
			${accountsObj.key}
			<div>${fn:split(accountsObj.value, '_')[1]}
								<c:if test="${fn:split(accountsObj.value, '_')[2] ne ' '}">${fn:split(accountsObj.value, '_')[2]}
								</c:if></div>
				<span class="hidden"> <%-- HIDDEN ACCOUNT GLN : to facilitate filter --%>
					${fn:split(accountsObj.value, '_')[0]}
				</span>
			</div>
			<c:if test="${empty accountList}">
					<center class="marTop20">
						<p><spring:message code="reports.noresults.label"/></p>
					</center>
				</c:if>
	</div>
	
</c:forEach>	
</div>
<a style="visibility:hidden;display: block;text-align: center;" href="javascript:;" class="loadMoreAccounts"><spring:message code="reports.loadmore.label"/></a>
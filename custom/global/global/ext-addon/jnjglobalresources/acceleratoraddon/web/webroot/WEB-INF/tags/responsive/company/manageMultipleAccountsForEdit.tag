<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>

<div class="sectionBlock body">
	<%-- <input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
	<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
	<input id="currentPage" value="${currentPage}" type="hidden" /> --%>
	  <div class="overFlowContainer">	 
		<div class="accountSelectorText boxshadow" id="account-holder-usm">
			<!-- <ul> -->
				<%-- Iterating over the accounts map --%>
				<c:forEach items="${allAccountSelectionList}" var="accountsObj" varStatus="count">
					<!-- <li> --> <!-- id="accountListPopUp" class="accountListPopUp" -->
						<div class="${count.index %2 == 0 ? 'even' : 'odd'} grpAccount grpAccountEdit grpAccountEdit_${accountsObj.key}" id="grpAccount_${count.count}" style="padding:10px 10px 0px 10px;display:none">
							<label for="multiAccountSelection_${count.count}" class="changeAccountUnit" id="${count.count}">
								<input id="accountUID" value="${accountsObj.key}" type="hidden" />
								<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
								<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
								
								<div class="checkbox checkbox-info checkboxmargin" style="display:none;">
									<input type="checkbox"  class="multiAccountSelection multiAccountSelection_${accountsObj.key}" id="multiAccountSelection_${count.count}" value="${accountsObj.key}" data="${accountsObj.value}"/><label for="multiAccountSelection_${count.count}"></label>
								</div>
								<%-- <input type="checkbox"  class="multiAccountSelection" id="multiAccountSelection_${count.count}" value="${accountsObj.key}" data="${accountsObj.value}"/> --%>
								<%-- ACCOUNT UID --%>
								<p style="display: table-cell;"><span class="accountGroupText">${accountsObj.key}</span>
								<br />
								<%-- ACCOUNT NAME --%>
								${fn:split(accountsObj.value, '_')[1]}
								<c:if test="${fn:split(accountsObj.value, '_')[2] ne ' '}">
								<br />
								<%-- ACCOUNT CITY / STATE --%>
								${fn:split(accountsObj.value, '_')[2]}
								</c:if>
								</p>
								<span class="hidden">
									<%-- HIDDEN ACCOUNT GLN : to facilitate filter --%>
									${fn:split(accountsObj.value, '_')[0]}
								</span>
							</label>
						</div>
					<!-- </li> -->
				</c:forEach>
				<c:if test="${empty allAccountSelectionList}">
					<center class="marTop20">
						<p><spring:message code="reports.noresults.label"/></p>
					</center>
				</c:if>
			<!-- </ul> -->
		</div>
  </div>  
</div>
<%-- <a style="visibility:hidden;display: block;text-align: center;" href="javascript:;" class="loadMoreAccounts"><spring:message code="reports.loadmore.label"/></a> --%>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
<input id="currentPage" value="${currentPage}" type="hidden" />
<input id="currentAccount" value="${currentAccountId}" type="hidden" />
<div class="modal fade jnj-popup" id="changeAcntPopup" role="dialog">
    <div class="modal-dialog modalExtendWidth modalcls ">
    <form:form method="post" id="selectAccountForm" action="javascript:;">
    <input type="hidden" id="totalResultsAccountSelection" value="${accountPaginationData.totalNumberOfResults}" />
        <!-- Modal content-->
        <div class="modal-content" id="ContractDetailsPage">
            <div class="modal-header">
              <h4 class="modal-title"><spring:message code="orderHistorySelectAccPopUp.selectAccOrderHistory"/></h4>
              <a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="la.popup.close"/></a>
            </div>
            <div class="modal-body">
                <div class="form-group searchArea">
                    <div class="search-txt-box">
                        <input type="text" name="searchAccount" id="orderHisChangeAccountNoAjax"  value="${accountSearchTerm}" class="required changeContractSearch form-control searchBox"  placeholder="<spring:message code='change.account.search'/>"/>
                         <button class=" searchBtn" id="orderHisSearchSelectAccountBtn" style="border: none;background: none"><i class="bi bi-search" style="
    -webkit-text-stroke: 1px;
"></i></button>
                    </div>
                    <button class="btn btnclsactive searchbtn  primarybtn pull-right tertiarybtn clearSelectAccountBtnOrderHistory padding-30px" id="clearSelectAccountBtnOrderHistory">
                        <spring:message code="popup.clear"/>
                    </button>
                 </div>
            </div>
            <hr>
            <div class="row checbox-btn-row-history">
                <div class="col-lg-8 col-md-8 col-sm-8 col-xs-12 for-landscape-mobile">
                    <div class="checkbox checkbox-info selectchkbox">
                        <input name="selectAllAccount" id="selectAllAccount" class="styled contract-thead-chckbox" type="checkbox">
                    <label for="selectAllAccount"  id="contract-head-chck-label"><spring:message code="orderHistorySelectAccPopUp.selectAllAvailable"/></label>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 for-landscape-mobile">
                    <div class="float-right-to-none">
                         <button type="submit" class="btn btnclsactive searchbtn pull-right padding-30px accountsSubmitlatam" >
                            <spring:message code='popup.send'/>
                         </button>
                    </div>
                </div>
            </div>

            <div class="modalbody contract-detail-desktab modal-account-yscroll">
                <c:forEach items="${accounts}" var="accountsObj" varStatus="count">
                <div class="accountsMargin">
                    <div class="checkbox checkbox-info selectchkbox display-table-cell">
                    <c:set value="${(currentAccountId eq accountsObj.key) ? 'checked' : ''}" var="selectedAccount" />
                    <input type="checkbox" class="accountSelection" id="accountSelection_${count.count}" value="${accountsObj.key}" data="${accountsObj.value}"  ${selectedAccount}/>
                        <label for="contract-check-2"></label>
                    </div>
                    <div class="contract-check-content display-table-cell accountListPopUp changeAccountUnit"  for="accountSelection_${count.count}"  id="${count.count}" >
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
                    </div>
                </div>
            </c:forEach>
            </div>
            <div class="modal-footer ftrcls d-flex justify-content-start" style="border:none !important">
                <div id="select-accnt-error-order-history" style="color:#b41601;float:left"><spring:message code="account.change.title.select"/></div>
            </div>
            <div class="btn-mobile padding25">
                <button type="button" class="btn btnclsnormal slctaccount reset " data-bs-dismiss="modal"><spring:message code='reports.account.selection.cancel' /></button>
                <button type="submit" class="btn btnclsactive searchbtn pull-right padding-30px accountsSubmitlatam" style="padding-right:20px;padding-left:20px;"><spring:message code='popup.send'/></button>
            </div>
        </div>
        </form:form>
    </div>
</div>
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
<%@ taglib prefix="removeleadZeros" uri="/WEB-INF/tld/removeLeadingZeros.tld"%>
<input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
<input id="currentPage" value="${currentPage}" type="hidden" />
<input id="currentAccount" value="${currentAccountId}" type="hidden" />
<div class="modal fade jnj-popup" id="changeAcntPopup" role="dialog">

			<div class="modal-dialog modalcls modal-md">
			<form:form method="post" id="selectAccountForm" action="javascript:;">
			<input type="hidden" id="totalResultsAccountSelection" value="${accountPaginationData.totalNumberOfResults}" />
				<!-- Modal content-->
				<div class="modal-content" id="ContractDetailsPage">
					<div class="modal-header">
					  <h4 class="modal-title"><spring:message code="account.change.title.ModifyAccount"/></h4>
					  <a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="popup.close"/></a>
					</div>
					
					<div class="modal-body">
						<div class="form-group searchArea">
							<input type="text" name="searchAccount" id="orderHisChangeAccountNoAjax"  value="${accountSearchTerm}" class="required changeContractSearch form-control searchBox"  placeholder="Search for an account"/>
							<i class="bi bi-search searchglyph "  id="orderHisSearchSelectAccountBtn"></i>
						 </div>	
						 <div class="row" style="padding-bottom:10px">
						 	<div class="col-lg-12">
								<button type="button" class="btn btnclsactive searchbtn pull-right clearbtn primarybtn  tertiarybtn clearSelectAccountBtn" id="clearSelectAccountBtn"><spring:message code="popup.clear"/></button>
							 </div>
						 </div>	
						 
					</div>
					<hr>
					<%-- <div class="row checbox-btn-row-usm">
						<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12 for-landscape-mobile">
							<div class="checkbox checkbox-info selectchkbox">
								<input id="contract-select-all" class="styled contract-thead-chckbox"	type="checkbox">
								 <label for="contract-select-all" id="contract-head-chck-label">Select All</label>
								 
								 <input type="checkbox" class ="usm-select-all accountSelectionAll styled contract-thead-chckbox" id="selectAllAccount" name="selectAllAccount">
								 <label for="selectAllAccount"><spring:message code='reports.account.selection.all'/></label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 for-landscape-mobile">
							<div class="float-right-to-none">
					
								<button type="button" class="btn btnclsactive searchbtn primarybtn" id="accountSelectionOk"><spring:message code='reports.account.selection.ok'/></button>
							</div>
						</div>
					</div>  --%>
					<div class="modalbody contract-detail-desktab" style="overflow-y: auto;max-height: 300px;">
						
						<c:forEach items="${accounts}" var="accountsObj" varStatus="count">
						<div class="accountsMargin">
							<div class="checkbox checkbox-info selectchkbox display-table-cell">
							<c:set value="${(currentAccountId eq accountsObj.key) ? 'checked' : ''}" var="selectedAccount" />
							<input type="checkbox" class="accountSelection" id="accountSelection_${count.count}" value="${accountsObj.key}" data="${accountsObj.value}"  ${selectedAccount}/>
								
								<!-- <input id="contract-check-2" class="styled contract-tcell-chckbox" type="checkbox"> -->
								<label for="contract-check-2"></label>
							</div>
							<div class="contract-check-content display-table-cell accountListPopUp changeAccountUnit"  for="accountSelection_${count.count}"  id="${count.count}" >
						
									
										<input id="accountUID" value="${accountsObj.key}" type="hidden" />
										<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
										<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
										<%-- ACCOUNT UID --%>
										<%-- <removeleadZeros:removeLeadingZeros value="${accountsObj.key}"/> --%>
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
						<div class="modal-footer ftrcls" style="border-top:none">
													<div id="select-accnt-error-order-history" style="color:#b41601;float:left"><spring:message code="account.change.title.select"/></div> 
												</div>
					<div class="btn-mobile padding25">
						<button type="button" class="btn btnclsnormal slctaccount reset  width20percent" data-bs-dismiss="modal"><spring:message code='reports.account.selection.cancel' /></button>	
						<button type="submit" class="btn btnclsactive searchbtn pull-right width20percent" id="accountsSubmitCUM">OK</button>
																			
					</div>
				</div>
				</form:form>
			</div>
		</div>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/jnjglobalreports/responsive/company"%>
<!-- Modal change account orderhistory popup -->
<c:choose>
	<c:when test="${fetchAll ne true}">
		<div class="modal fade jnj-popup" id="changeAcntPopup" role="dialog">
			<div class="modal-dialog modalcls modal-md">
			<input type="hidden" id="totalResultsAccountSelection" value="${accountPaginationData.totalNumberOfResults}" />
				<!-- Modal content-->
				<div class="modal-content" id="ContractDetailsPage">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='popup.close' /></button>
					  <h4 class="modal-title"><spring:message code='reports.account.selection.header'/></h4>
					</div>
					<div class="modal-body">
						<div class="form-group searchArea">
							<!--  <input type="text" class="form-control searchBox" placeholder="Search for an account" id="changeAccountNoAjax"> -->
							 <input type="text" placeholder="<spring:message code='account.change.searchText'/>" value="${accountSearchTerm}" id="selectAccountNoAjax" class="placeholder changeContractSearch form-control searchBox-inventory" style="margin-right: 5px;">
							  <!-- //Mobile/Tablet defect #20, 21, 22 -->
							  <button class="glyphicon glyphicon-search searchSelectAccountBtn" id="searchSelectAccountBtn"></button> 
							
						<%--	<c:choose>
									<c:when test="${accountSearchTerm eq null || empty accountSearchTerm}">
										<input type="button" class="tertiarybtn clearSelectAccountBtn btn btnclsactive searchbtn pull-right clearbtn" value="Clear" id="clearSelectAccountBtn" disabled="disabled"/>
									</c:when>
									<c:otherwise>
										<input type="button" class="primarybtn clearSelectAccountBtn btn btnclsactive searchbtn pull-right clearbtn" value="Clear" id="clearSelectAccountBtn"/>
									</c:otherwise>
								</c:choose>--%>
						 </div>	
					</div>
					<hr>
					<input type="hidden" id="invAccountList" value="${accountList}" />
					<company:selectAccounts/>
					
					<!-- <div class="btn-mobile padding25"> -->
					<div class="row modal-footer no-margin-padding">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div id="Errormassage" style="color: rgb(180, 22, 1); float: left; display: block;margin-bottom:10px"></div>
						</div>	
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="float-left-to-none">
								<button type="button" class="btn btnclsnormal reset   tertiarybtn accountSelectionCancel"><spring:message code='reports.account.selection.cancel' /></button>
							</div>	
							<div class="float-right-to-none">
							<!-- AAOL-4714 -->	
								<button type="button" class="btn btnclsactive searchbtn primarybtn accountSelectionOk" id="accountSelectionOk"><spring:message code='reports.account.selection.ok'/></button>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>
		</c:when>
	<c:otherwise>{"totalNumberOfResults":"${accountPaginationData.totalNumberOfResults}","accounts":"<c:forEach items="${accountList}" var="accountsObj" varStatus="status">${accountsObj.key}<c:if test="${not status.last}">,</c:if></c:forEach>"}
	</c:otherwise>
</c:choose>
	<!--End of Modal change account orderhistory popup-->
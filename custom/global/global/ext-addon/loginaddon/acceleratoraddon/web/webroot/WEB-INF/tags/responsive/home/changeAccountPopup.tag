<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="isAdminUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isFirstTimeLogin" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="selectaccountpage" class="changeAccountPopupContainer">
<input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
	<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
	<input id="currentPage" value="${currentPage}" type="hidden" />

	<input type="hidden" value="" id="currViewScreenName" />

		<!-- Modal -->
	<div class="modal fade" id="selectaccountpopup" role="dialog" data-firstLogin="${firstTimeLogin}">
		<div class="modal-dialog modalcls changeAccntModal">
		
			<!-- Modal content-->
			<div class="modal-content changeAccntModalContent">
				<div class="modal-header" style="border-bottom:1px solid #e5e5e5">
				   <button type="button" class="close clsBtn" data-dismiss="modal" id="select-accnt-close" ><spring:message code="account.change.popup.close"/></button>
				  <h4 class="modal-title selectTitle"><spring:message code="account.change.title"/></h4>
				</div>
				<div class="form-group searchArea accSearch">
						<div class="search-txt-box">
							<input type="text" class="form-control searchBox"  placeholder="Search for an account" id="changeAccountNoAjax">
							<button class="glyphicon glyphicon-search searchBtn pull-right" id="searchSelectAccountBtnHeader" style="border: none;background: none"/></button> 
						</div>	 
						 <button type="button" class="btn btnclsactive searchbtn pull-right clearbtn tertiarybtn clearSelectAccountBtn" id="clearSelectAccountBtn">
						 <spring:message code="popup.clear"></spring:message></button>
						 
						<!-- <span class="glyphicon glyphicon-search searchBtn pull-right" id="searchSelectAccountBtn"></span> -->
				</div>
								<div  id="lodCircleID" class="loadcircle" style='text-align: center'><img src="/store/_ui/responsive/common/images/ajax-loader.gif"></div>
				<div class="list-group listclass accountListArea">
					<div id="changeAccountPopup" class="changeAccountContent changeAccountColorBox accountListYArea">
						<c:if test="${isFirstTimeLogin ne null}">
							<ul class="accountListPopUpUL">
								
								<c:forEach items="${accountList}" var="accountsObj" varStatus="count">
									<li id="accountListPopUp" class="accountAddress accountListPopUp ${(count.count % 2 eq 0) ? 'even' : 'odd'}"> 
										<a class="changeAccountUnit list-group-item anchorcls" href="javascript:;" style="margin-bottom:0px;padding-bottom:0px">
															<input class="accountUID" value="${accountsObj.key}" type="hidden" />
											<input id="accountUID" value="${accountsObj.key}" type="hidden" />
											<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
											<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
											
											<div class="list-group-item-heading code">${accountsObj.key}</div>															
											
										</a>
										<div class="list-group-item-text descTxt">${fn:split(accountsObj.value, '_')[1]}
											<c:if test="${fn:split(accountsObj.value, '_')[2] ne ' '}">
												
	<!-- 																		ACCOUNT CITY / STATE -->
												<div>	${fn:split(accountsObj.value, '_')[2]}</div>
												</c:if><span class="hidden">
													HIDDEN ACCOUNT GLN : to facilitate filter
													${fn:split(accountsObj.value, '_')[0]}
												</span>
											</div>
																</li> 
															</c:forEach>
															<c:if test="${empty accountList}">
																<center class="marTop20" style="margin-top:50px">
																	<p><spring:message code="reports.search.noresults"/></p>
																</center>
															</c:if>
														</ul>
									</c:if>
								</div>
												</div>
												<div class="modal-footer ftrcls">
														<c:choose>
									<c:when test="${isSuperUSer}">
										<div class="row" style="visibility:visible;display: block;" >
											<div class="col-lg-12 col-md-12 text-center">
		<!-- 										<a href="#"><span class="glyphicon glyphicon-cloud-download"></span>&nbsp;Load More</a> -->
												<a href="javascript:;" class="loadMoreAccounts" style="visibility:visible;display: block;" >
													<span class="glyphicon glyphicon-cloud-download"></span>&nbsp;
														<spring:message code="accounts.load.more"></spring:message></a>
											</div>
										</div>
										
									</c:when>
									<c:otherwise>
										<a style="visibility:hidden;display: block;text-align: center;" href="javascript:;" class="loadMoreAccounts"><spring:message code="accounts.load.more"></spring:message></a>
										<div class="row" style="visibility:visible;display: block;" >
										</div>
									</c:otherwise>
								</c:choose>
							<div id="select-accnt-error" style="color:#b41601;display:inline-block;float:left"><spring:message code="account.change.title.select"/></div> 

					<%-- <button type="button" class="btn btnclsactive  pull-right" id="change-select-btn" style="margin-top:10px"><spring:message code="account.change.title.text"/></button> --%>

					<button type="button" class="btn btnclsactive  pull-right" id="change-select-btn" style="margin-top:10px"><spring:message code="account.change.title.text"/></button>

				</div>
			</div>
								  
		</div>
	</div>
														
								
</div>
<input type="hidden" value="" id="currAccScreenName" />

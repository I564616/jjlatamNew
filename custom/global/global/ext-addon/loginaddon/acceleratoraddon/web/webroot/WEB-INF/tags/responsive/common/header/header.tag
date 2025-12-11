<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="header"  tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/nav" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>
<%@ attribute name="isAdminUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isFirstTimeLogin" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="chatlive" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/genesys" %>
<%
    request.setAttribute("isAdminUser","${adminUser}" ); 
    request.setAttribute("isFirstTimeLogin", true);
%>
<chatlive:chatWidget/>
<script>
var hjid_Id = ACC.config.hjid;
	 (function(h,o,t,j,a,r){
	        h.hj=h.hj||function(){(h.hj.q=h.hj.q||[]).push(arguments)};
	        h._hjSettings={hjid:hjid_Id,hjsv:5};
	        a=o.getElementsByTagName('head')[0];
	        r=o.createElement('script');r.async=1;
	        r.src=t+h._hjSettings.hjid+j+h._hjSettings.hjsv;
	        a.appendChild(r);
	    })
	    (window,document,'//static.hotjar.com/c/hotjar-','.js?sv=');
</script>
<input type="hidden" value="GT" id="datepickerformat" /><!-- Modified by Archana for AAOL-5513 -->
<input type="hidden" name="browserCompatible"
		value='<spring:message code="browser.compatible.header"/>..<a  href="javascript:void(0)" id=buorgcloseid><spring:message code="browser.compatible.dismiss"/><a/>'
		id="browserCompatible" /> <input type="hidden"
		name="browserCompatibletest" value='true' id="browserCompatibletest" />
	
<button id="hamburg-btn" type="button" class = "navbar-toggle pull-left" data-toggle = "collapse" data-target ="#jnj-menu">								
							 <span class ="bi bi-list"></span>
							 <span class ="bi bi-list"></span>
							 <span class ="bi bi-list"></span>
						</button>
				    <div class="row" id="jnj-header">
    <div class="col-lg-12 shadow-header" style="
    height: fit-content;
" >
  <div class="row" style="height: fit-content;padding-bottom: 20px;justify-content: start;align-items: center;">
	    <div class="col-lg-7 col-md-7 col-9 col-3 no-padding" id="jnj-logo-holder">
		    <cms:pageSlot position="SiteLogo" var="logo" limit="1">
			    <cms:component component="${logo}" />
			</cms:pageSlot>
		</div>
		<c:if test="${((empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)) && (placeOrderGroupFlag eq true || not isSerialUser)}" >
				<div class="padding-0-tablet col-lg-1 col-md-1 col-3 col-2 pull-right order-sm-2 order-xs-2 order-lg-3 order-md-3">
					<div id="cart-btn-holder" class="pull-right">
			            <cms:pageSlot position="MiniCart" var="cart" limit="1">
	                        <cms:component component="${cart}"/>
                        </cms:pageSlot>
					</div>
				</div>
				<header:searchBox />

		</c:if>
		</div>
	</div>
</div>

<div class="row" style="bottom: 0px;position: fixed !important;right: 0px !important;margin-right: 50px!important;z-index: 100;">
   <div class="buttonTable liveChatpopup_hn">
      <table style="height: 45px; border-color: #030303; border-radius: 10px; background-color: #288297; width: 250px;border-collapse: collapse;
               border-spacing: 0;
               opacity: .7;
               border-bottom-left-radius: 0em !important;
               border-bottom-right-radius: 0em !important;    "
            role="button">
         <tbody>
         <tr id="webChatButton" onclick="customPlugin.command('WebChat.open',getAdvancedConfig());">
            <td style="padding-left: 20px; padding-top: 20px;" colspan="1"
               rowspan="2"><input type="image"
                              src="${webroot}/_ui/responsive/common/images/chatBtn.png"
                              alt=""
                              width="35" height="27" class="page-1" role="button">
            </td>
            <td style="padding-top: 10px !important; float: left !important;"
               colspan="3" rowspan="2"><span class="livechat"><strong><span
                  style="color: #ffffff;"><span
                  style="float: left; padding-top: 0px !important; font-size: 36px; font-weight: 700; width: 146px; height: 49px;"> live chat</span></span></strong></span>

            </td>
         </tr>
         </tbody>
      </table>
   </div>
</div>

<input type="hidden" value="${adminUser}" id="isAdminUserHddn" />
<%-- <account:changeAccountPopup isAdminUser="${adminUser}" isFirstTimeLogin="${firstTimeLogin}" /> --%>
	<input type="hidden" value="" id="hddnTempAccountList" autocomplete="off" />
		<input type="hidden" value="" id="hddnTempAccountList1" autocomplete="off" />
	<input type="hidden" value="" id="currAccScreenName" />
	<input type="hidden" id="isUserLoggedIn"  value="${user.uid}" />
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
										<spring:message code="popup.clear"></spring:message>
									</button>
								<!-- <span class="glyphicon glyphicon-search searchBtn pull-right" id="searchSelectAccountBtn"></span> -->
						</div>
						<div class="list-group listclass accountListArea">
							<div id="changeAccountPopup" class="changeAccountContent changeAccountColorBox accountListYArea">
							   <c:if test="${isFirstTimeLogin ne null}">
								<ul class="accountListPopUpUL">
									<%-- Iterating over the accounts map --%>
									<c:forEach items="${accountList}" var="accountsObj" varStatus="count">
										 <li id="accountListPopUp" class="accountAddress accountListPopUp ${(count.count % 2 eq 0) ? 'even' : 'odd'}"> 
										<a class="changeAccountUnit list-group-item anchorcls" href="javascript:;" style="margin-bottom:0px;padding-bottom:0px">
																	<input class="accountUID" value="${accountsObj.key}" type="hidden" />
													<input id="accountUID" value="${accountsObj.key}" type="hidden" />
												<input id="accountName" value="${fn:split(accountsObj.value, '_')[1]}" type="hidden" />
												<input id="accountGLN" value="${fn:split(accountsObj.value, '_')[0]}" type="hidden" />
												<%-- ACCOUNT UID --%>
											<div class="list-group-item-heading code">${accountsObj.key}</div>															
												
												<%-- ACCOUNT NAME --%>															
												
											</a>
											<div class="list-group-item-text descTxt">${fn:split(accountsObj.value, '_')[1]}
								
															<c:if test="${fn:split(accountsObj.value, '_')[2] ne ' '}">
															
																<%-- ACCOUNT CITY / STATE --%>
										<div>	${fn:split(accountsObj.value, '_')[2]}</div>
										</c:if><span class="hidden">
																<%-- HIDDEN ACCOUNT GLN : to facilitate filter --%>
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
								<!--End of Modal pop-up-->
								
					
								
							</div>	
								<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
							<div class="col-lg-12 col-md-12" id="sessionTimer-page">
								<div class="modal fade" id="sessionTimeout-popup" role="dialog" data-firstLogin='true'>
									<div class="modal-dialog modalcls-timeout">                                                                                                                                                           
										<div class="modal-content popup">
											<div class="modal-header">
												<%-- <button type="button" class="close clsBtn" data-dismiss="modal" id="select-accnt-close"><spring:message code="account.change.popup.close"/></button> --%>
												<h4 class="modal-title selectTitle"><spring:message code="session.timeout.title"/></h4>
											</div>
											<div class="modal-body">                                                                                                                                                                                                 
												<div class="row" id="quick-cart-contentholder">
													<div class="col-lg-12">                                                                                                                                                                                                                                        
														<div><spring:message code="session.timeout.infotext"/></div>
														<span class="info-text" id="sessionInfoText1"></span><span> <spring:message code="session.timeout.min"/> </span><span  class="info-text" id="sessionInfoText2"></span>    
														<div><spring:message code="session.timeout.msg1"/></div>
														<div><spring:message code="session.timeout.msg2"/></div>
													</div>
												</div>                                                                                                                                                                                   
											</div>                                                                                                                                                                   
											<div class="modal-footer ftrcls">
												<button type="button" class="btn btnclsnormal pull-left" data-dismiss="modal" id="logoff-btn"><spring:message code="user.session.logoff"/></button>                                                                                                                                                                                              
												<button type="button" class="btn btnclsactive" data-dismiss="modal" id="logIn-btn"><spring:message code="user.session.continue"/></button>
											</div>
										</div>                                                                                                                                                     
									</div>
								</div>
							</div>						
						<!-- Added By Vijay for Loading ....Display -->
						<div id="laodingcircle" style="display:none">
					       <div class="modal-backdrop in">
					        <!-- Modal content-->
					          <div class="row panelforcircle">
					           <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="loadcircle"><img src="${webroot}/_ui/responsive/common/images/ajax-loader.gif"></div>
					           </div>
					          </div>
					         </div>        
						</div>
<!-- Validate Quantity Popup start-->
<div class="modal fade jnj-popup-container" id="newQuantitypopup"	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="quantity.check.close"/></button>
				<h4 class="modal-title selectTitle"><spring:message code="quantity.check.title"/></h4>
			</div>
			<div class="modal-body">
				<div><spring:message code="quantity.check.msg"/></div>
			</div>
			<div class="modal-footer ftrcls">
				<button type="button" class="btn btnclsnormal pull-left" id="back-btn"><spring:message code="quantity.check.btnBack"/></button>
				<button type="button" class="btn btnclsactive"  	id="continue-btn"><spring:message code="quantity.check.btnContinue"/></button>
			</div>
		</div>
	</div>
</div>
<!--Validate Quantity Popup end-->
<div id="privacypolicypopuopholder"></div>
<div id="legalnoticepopupholder"></div>
<div id="termsandconditionsholder"></div>
<div id="contactuspopupholder"></div>
<div id="updateprivacypopupholder"></div>
<!-- product details popup tag -->
<div id="product-details-popupholder"></div>
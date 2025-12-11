<%-- <%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%> --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%-- <c:set var="scrLocation" value='${commonResourcePath}/js/browserCompatible.js' /> --%>

<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%-- <%@ taglib prefix="account"	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"%> --%>



<div class="row" id="usr-details-menu-row">
	<div class="col-lg-12 col-xs-12" id="Menu">
		<nav class="" role="navigation">
			<div class="menu-closed" id="jnj-menu">
				<!-- Start - Userdetails -->

				<div class="row" id="usr-details-row">
					<div class="col-lg-12 col-md-12 col-xs-12  no-padding-menu"	id="usr-details">
						<div class="usr-name-padding">

							<div class="row">
								<div id="usr-name" class="col-lg-12">
								<sec:authorize	ifAnyGranted="ROLE_CUSTOMERGROUP">
									<ycommerce:testId code="header_LoggedUser">${user.firstName}&nbsp;${user.lastName}</ycommerce:testId>
							    </sec:authorize></div>
							</div>
							<div class="row">
								<div id="usr-actions" class="col-lg-12">
									<!--my  profile tag and action here  -->
									<a href="<c:url value='/my-account/personalInformation'/>" class="usr-action-link">
									 <i class="fa fa-user" aria-hidden="true"></i>
									  <span	class="usr-action-txt"><spring:message code="header.information.profile.link"/></span>
									</a>
									<!--Logout tag and action here  -->
									 <span class="vertical-separator">|</span>
									 <ycommerce:testId code="header_signOut">
									  <a href="<c:url value='/logout'/>" class="usr-action-link-signout"> 
									  <i class="fa fa-power-off" aria-hidden="true"></i>
									   <span class="usr-action-txt"><spring:message code="header.information.signout"/></span>
									</a>
									</ycommerce:testId>
								</div>
							</div>
						</div>
						<div id="horizant-divider-holder">
							<div class="row">
								<div class="col-lg-12">
									<div id="divider"></div>
								</div>
							</div>
						</div>
						<div class="usr-address-padding">
						
						<!-- account id and name start here -->
						<!-- Added if condition for serialization -->
						<c:if test="${not isSerialUser }">
							<div class="row">
								<div id="usr-address" class="col-lg-12">
									<c:choose>
										<c:when test="${accountUID ne null}">
								${accountUID}
							</c:when>
										<c:otherwise>
							${user.currentB2BUnitID}
								<%-- ${user.currentB2BUnitID} --%>
										</c:otherwise>
									</c:choose>
									,
									<c:choose>
										<c:when test="${accountName ne null}">
											<c:set var="dispAccountName" value="${accountName}" />
										</c:when>
										<c:otherwise>
											<c:set var="dispAccountName"
												value="${user.currentB2BUnitName} " />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${fn:length(dispAccountName)>45 }">
											<c:set var="dispAccountNameSubString"
												value="${fn:substring(dispAccountName, 0, 45)}..." />
											<label title="${dispAccountName}" class="label labelLong">${dispAccountNameSubString}</label>
										</c:when>
										<c:otherwise>
											<c:out value="${dispAccountName}"></c:out>
										</c:otherwise>
									</c:choose>
									<c:if
										test="((accountGLN ne 'null' && not empty accountGLN) || not empty user.currentB2BUnitGLN )}">
										<li><spring:message code="header.information.account.gln" />&nbsp;
											<span> <c:choose>
													<c:when
														test="${accountGLN ne 'null' && not empty accountGLN}">
									${accountGLN}
								</c:when>
													<c:otherwise>
									${user.currentB2BUnitGLN}
								</c:otherwise>
												</c:choose>
										</span></li>
									</c:if>
								</div>
							</div>
							</c:if>
								<!-- change account start here  -->
							<!-- Added if condition for serialization -->
								<c:if test="${not isSerialUser }">
								   <div class="row">	
								  
								   <c:if test="${showChangeAccountLink eq true}"> 
								<div class="changeAccount col-lg-12" id="change-account">
								
			                	 <a href="" class="usr-action-link change-accnt-link" data-bs-toggle="modal" id = "selectact">
			                	 <i class="fa fa-exchange" aria-hidden="true"></i>
			                	 <spring:message code="header.information.account.change.link"/>
			                	 </a>
			                      </div>
			                        <input type="hidden" value="${adminUser}" id="isAdminUserHddn" />
	                             <%-- 	<account:changeAccountPopup isAdminUser="${adminUser}" isFirstTimeLogin="${firstTimeLogin}" /> --%>
								
								</c:if>	
								
						        </div>  
						        </c:if>
										
									<!-- change account end here  -->
						</div>
					</div>
				</div>
				<!-- End - Userdetails -->
				<!-- Start - Menu --> 
				<!-- AAOL 4344 surabhi -->
				<div class="row">
					<div class="col-lg-12 col-md-12 col-xs-12 no-padding-menu">
						<ul class="navbar-nav" id="jnj-menu-list">
							<cms:pageSlot position="TopNavigation" var="component">
								<li>
									<div>
										<table style="width: 100%;">
											<tr>
												<td>
													<div>
														<c:choose>
                                                            <c:when test="${(pharmaCommercialUserGroupFlag eq true || mddCommercialUserGroupFlag eq true) && (component.uid ne 'TemplatesBarComponent')}">
                                                               <cms:component component="${component}" />
                                                            </c:when>
                                                            <c:when test="${(empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)}">
                                                               <cms:component component="${component}" />
                                                            </c:when>
                                                            <c:otherwise>
                                                            </c:otherwise>
                                                       </c:choose>
													</div>
												</td>
											</tr>
										</table>
									</div>
								</li>
							</cms:pageSlot>
						</ul>
					</div>
				</div>
				
				<!-- End - Menu -->
				<!-- Report -->
				
				
				<!-- Report end -->
			</div>
		</nav>
	</div>
</div>

<%-- <div class="col-lg-12 col-md-12" id="sessionTimer-page">
	<input type="hidden" id="isUserLoggedIn"  value="${user.uid}" />
	<div class="modal fade" id="sessionTimeout-popup" role="dialog" data-firstLogin='true'>
		<div class="modal-dialog modalcls-timeout">                                                                                                                                                           
			<div class="modal-content popup">
				<div class="modal-header">
					<button type="button" class="close clsBtn" data-dismiss="modal" id="select-accnt-close"><spring:message code="account.change.popup.close"/></button>
					<h4 class="modal-title selectTitle"><spring:message code="session.timeout.title"/></h4>
				</div>
				<div class="modal-body">                                                                                                                                                                                                 
					<div class="row" id="quick-cart-contentholder">
						<div class="col-lg-12">                                                                                                                                                                                                                                        
							<div><spring:message code="session.timeout.infotext"/></div>
							<span class="info-text" id="sessionInfoText1"></span><span> minutes as of </span><span  class="info-text" id="sessionInfoText2"></span>    
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
</div> --%>
 


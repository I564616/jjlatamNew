<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>


<template:page pageTitle="${pageTitle}">
 <div class="orderstatuspage">	
<input type="hidden" id="updatedLegalPolicy"
		value="${updatedLegalPolicy}"  />
	<input type="hidden" id="survey" value="${survey}" />

 <%--  <cms:pageSlot position="BroadCast" var="feature" element="div">
	<cms:component component="${feature}"/>
</cms:pageSlot>  --%>
	
	<c:forEach items="${broadCastData}" var="broadCastData">
								<c:if test="${broadCastData.type=='ALERT'}">
									<div class="panel-group home-panel-alert">
										<div class="alertBroadcast broadcastMessageContainer panel panel-danger">
											<div class="panel-heading">
												<div class="panel-title">	
													<div class="row">
														<div class=" col-lg-11" >
															<span class="glyphicon glyphicon-ban-circle"></span>
															<span class="broadcastMessage broadcastMessageContent">
																${broadCastData.content}
																<input type="hidden" class ="broadCastId"  value="${broadCastData.id}"/>	
															</span>
														</div>
														<input type="hidden" class="broadcastMessageHidden"/>
														<a href="javascript:void(0)" data-target="#os-pls-read-popup" data-toggle="modal" class="moreBroadCastBtn moreBroadCastMessageTxt pull-right col-lg-1"><spring:message code="home.label.more"/></a>
													</div>
												</div>
											</div>
										</div>										
									</div>
								</c:if>
								<c:if test="${broadCastData.type=='NOTIFICATION'}">
									<div class="panel-grouphome-panel-alert">
										<div class="alertBroadcast broadcastMessageContainer panel panel-warning">
											<div class="panel-heading">
												<div class="panel-title">	
													<div class="row">
														<div class=" col-lg-11" >
															<span class="glyphicon glyphicon-exclamation-sign"></span>
															<span class="broadcastMessage broadcastMessageContent">
																
																	${broadCastData.content}
																	<input type="hidden" class ="broadCastId"  value="${broadCastData.id}"/>
															</span>		
														</div>	
														<input type="hidden" class="broadcastMessageHidden"/>	
														<a href="javascript:void(0)" data-target="#os-pls-read-popup" data-toggle="modal" class="moreBroadCastBtn moreBroadCastMessageTxt col-lg-1"><spring:message code="home.label.more"/></a>
													</div>
												</div>
											</div>
										</div>
									</div>		
															
								</c:if>
								<c:if test="${broadCastData.type=='INFORMATION'}">
									<div class="panel-group home-panel-alert">
										<div class="successBroadcast broadcastMessageContainer panel panel-success">
											<div class="panel-heading">
												<div class="panel-title">
													<div class="row">
														<div class="col-lg-11">
															<span class="glyphicon glyphicon-ok"></span>
															<span class="broadcastMessage broadcastMessageContent">
																${broadCastData.content}
																<input type="hidden" class ="broadCastId"  value="${broadCastData.id}"/>
															</span>
														</div>
														<input type="hidden" class="broadcastMessageHidden"/>
														<a href="javascript:void(0)" data-target="#os-pls-read-popup" data-toggle="modal" class="moreBroadCastBtn moreBroadCastMessageTxt col-lg-1"><spring:message code="home.label.more"/></a>
													</div>
												</div>
											</div>
										</div>
									</div>
								</c:if>
							</c:forEach>
	 
	
	                                <div id="tables">
										<home:orderHistory></home:orderHistory>
									  <div class="col-lg-8 col-md-8 col-sm-12 col-xs-12 placeOrder whitebg placeorderrightborder" >
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 placeordertext"><spring:message code="cart.common.placeOrder"/></div>
																				
										<%-- <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered borderforplaceorder1">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px"><a href="#"> <img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/cart.png" class="img-circle  quickAddtoCartClick" alt="buildorder" width="40" height="40"></div></a>
											<div class="col-xs-8 col-md-6 col-sm-6 col-lg-6 placeordermobalign buildanordertext boldtext dashboardtxt fullwidth managetextalign padding0">
											<a href="#" class="quickAddtoCartClick">
											<p class="marginbtmplaceorder"><spring:message code="text.company.managePermissions.assignName.quickAddToCart"/></p>
											<p><spring:message code="text.company.managePermissions.assignName.quickAddToCart1"/></p>
											</a>
											</div>
										</div> --%>
										
										<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 centered borderforplaceorder">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#"> 
													<img src="${webroot}/_ui/responsive/common/images/cart.png" class="img-circle  quickAddtoCartClick" alt="buildorder" width="40" height="40">
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#" class="quickAddtoCartClick"><spring:message code="text.company.managePermissions.assignName.quickAddToCart"/>&nbsp;<spring:message code="text.company.managePermissions.assignName.quickAddToCart1"/></a>
											</div>
										</div>
										
										
										<!-- <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 centered borderforplaceorder" style="padding-bottom:0"> -->
										<div class=" col-lg-3 col-md-3 col-sm-3 col-xs-12 centered">
											<home:uploadFile></home:uploadFile>
										</div>
									<%-- 	<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered borderforplaceorder">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px"><img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/document.png" class="img-circle " alt="ordertemplate" width="40" height="40"></div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 placeordermobalign boldtext dashboardtxt padding0"><a href="#"><p class="marginbtmplaceorder"><spring:message code="home.label.orderfrom" /></p> <p><spring:message code="home.label.template" /></p></a></div>
										</div> --%>
										
										<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 centered borderforplaceorder">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#" id="orderFromTemplate">
													<img src="${webroot}/_ui/responsive/common/images/document.png" class="img-circle " alt="ordertemplate" width="40" height="40">
												</a>
											</div>
										<c:url value="/templates" var="templatesUrl"/>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a  href="${templatesUrl}" id="orderFromTemplate"><spring:message code="home.label.orderfrom" />&nbsp;<spring:message code="home.label.template" /></a>
											</div>
										</div>
										
										<c:choose>
											 <c:when test="${showContractLink}">
									     <%-- <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered" >
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px home_Contract_Link" ><img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/contract.png" class="img-circle" alt="addcontract" width="40" height="40"></div>
											<div class="col-xs-8 col-md-6 col-sm-6 col-lg-6 placeordermobalign buildanordertext boldtext dashboardtxt padding0 home_Contract_Link" ><p class="marginbtmplaceorder" ><spring:message code="home.label.addfrom" /> </p> <p><spring:message code="home.label.acontract" /> </p></div>
											
										</div> --%>
										<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 centered borderforplaceorder">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#" class="home_Contract_Link">
													<img src="${webroot}/_ui/responsive/common/images/contract.png" class="img-circle" alt="addcontract" width="40" height="40">
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#" class="home_Contract_Link"><spring:message code="home.label.addfrom" />&nbsp;<spring:message code="home.label.acontract" /></a>
											</div>
										</div>
									    </c:when>
									    <c:otherwise>
									       <%--  <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered">
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px"><img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/contract.png" class="img-circle opaque " alt="addcontract" width="40" height="40"></div>
											<div class="col-xs-8 col-md-6 col-sm-6 col-lg-6 placeordermobalign buildanordertext boldtext dashboardtxt padding0 opaque"><a href="#"><p class="marginbtmplaceorder"><spring:message code="home.label.addfrom" /> </p> <p><spring:message code="home.label.acontract" /> </p></a></div>
											</div> --%>
											
											<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 centered borderforplaceorder">
												<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
													<a href="#">
														<img src="${webroot}/_ui/responsive/common/images/contract.png" class="img-circle opaque " alt="addcontract" width="40" height="40">
													</a>
												</div>
												<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
													<a href="#"><spring:message code="home.label.addfrom" />&nbsp; <spring:message code="home.label.acontract" /></a>
												</div>
											</div>
									    </c:otherwise>
    									</c:choose>
										
										
									  </div>
									  <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
											<div  id="contractPopuppage">
												<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
													<div class="modal-dialog modalcls">
														<div class="modal-content popup">
															<div class="modal-header">
															  <button type="button" class="close clsBtn "  id="contractCancelBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
															  <h4 class="modal-title selectTitle"><spring:message code="contract.page.addprod"/></h4>
															</div>
															<div class="modal-body">
																<div class="panel panel-danger">
																	<div class="panel-heading">
																		<h4 class="panel-title">
																		<table class="contract-popup-table">
																			<tr>
																			<td><div class="glyphicon glyphicon-ok"></div></td>
																			<td><div class="info-text"><spring:message code="contract.page.infotext"/></div></td>														
																			</tr>
																		</table>
																		</h4>
																	</div>
																</div>													
																<div><spring:message code="contract.page.msg"/></div>
																<div class="continueques"><spring:message code="contract.page.continue"/></div>
															</div>											
															<div class="modal-footer ftrcls">
																<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart0"><spring:message code="cart.common.cancel"/></a>
																<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart0" ><spring:message code="contract.page.accept"/></button>
															</div>
														</div>
													</div>
												</div>
										</div>		
										<!--  Add to cart Modal pop-up to identify  contract or non contract end -->
										
											<!-- Modal pop up for file upload error start -->
										<div class="modal fade jnj-popup" id="error-detail-popup" role="dialog">
											<div class="modal-dialog modalcls modal-md">
												<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
													<h4 class="modal-title"><spring:message code="home.label.errordetails"/></h4>
												</div>
												<div class="modal-body">
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
															<div class="panel-group">
																<div class="panel panel-danger">
																	<div class="panel-heading">
																		<h4 class="panel-title cart-error-msg">
																			<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message
																					code="homePage.errordetails.addfailed" /></span>
																		</h4>
																	</div>
																</div>
															</div>
														</div>
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<span id="uploadErrorMsg"></span>
														</div>
													</div>
												</div>
												</div>
											</div>
										</div>
										<!-- Modal pop up for file upload error end -->
									 
									  <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 manageorder whitebg">
									  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 placeordertext text-left"><spring:message code="home.label.manage" /></div>	
									  
							   	<%-- 	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 centered borderforplaceorder1" >
										<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 ">
													
											<p class="price-icon-holder">
											     <span class="price-icon-round"><span class="price-icon" id="requestPriceQuote">${currencySymbol}</span></span>
											</p>
											 <div class="col-xs-8 col-md-6 col-sm-6 col-lg-12 placeordermobalign manageorderstyle boldtext priceQuoteTxt dashboardtxt fullwidth managetextalign" >
												<a href="#" id="requestPriceQuote"><p class="marginbtm0"><spring:message code="home.label.priceInquiry" /></a>
											</div>
										</div>
									</div>	 --%>
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 centered borderforplaceorder1" >	
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#">
													<span class="price-icon-round"><span class="price-icon requestPriceQuote" id="requestPriceQuote">${currencySymbol}</span></span>
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#"  id="requestPriceQuote" class="requestPriceQuote"><spring:message code="home.label.priceInquiry" /></a>
											</div>
										</div> 
										
										
										<c:choose>
										<c:when test="${isEligibleForOrderReturn}">
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 centered borderforplaceorder1" >	
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#" id="startReturnButton" >
													<img src="${webroot}/_ui/responsive/common/images/start.png" class="img-circle startReturnButton" alt="startreturn" width="40" height="40">
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#" id="startReturnButton" class="startReturnButton"><spring:message code="home.label.startcaps" />&nbsp;<spring:message code="home.label.areturn" /></a>
											</div>
										</div> 
										</c:when>
										<c:otherwise>
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 centered borderforplaceorder1" >	
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#">
													<img src="${webroot}/_ui/responsive/common/images/start.png" class="img-circle opaque " alt="startreturn" width="40" height="40">
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#"><spring:message code="home.label.startcaps" />&nbsp;<spring:message code="home.label.areturn" /></a>
											</div>
										</div> 
										</c:otherwise>
										</c:choose>
									
										
										<%-- <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 font8pixel centered borderforplaceorder1" >
											<div class="col-xs-4 col-md-12 col-sm-8 col-lg-12 padding10px pull-right"><img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/start.png" class="img-circle " alt="startreturn" width="40" height="40"></div>
											<div class="col-xs-8 col-md-6 col-sm-6 col-lg-12 placeordermobalign floatnone boldtext dashboardtxt fullwidth managetextalign" ><a href="#"><p class="marginbtm0"><spring:message code="home.label.startcaps" /></p><p><spring:message code="home.label.areturn" /> </p></a></div>
										</div> --%>
										<%-- <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 font8pixel centered" >
											<div class="col-xs-4 col-md-12 col-sm-8 col-lg-12 padding10px pull-right"><img src="/jnjb2bglobalstorefront/_ui/responsive/common/images/dispute.png" class="img-circle " alt="disputeorder" width="40" height="40"></div>
											<div class="col-xs-8 col-md-6 col-sm-6 col-lg-12 placeordermobalign disputeorder floatnone boldtext dashboardtxt fullwidth managetextalign"><a href="#"><p class="marginbtm0"><spring:message code="home.label.dispute" /></p> <p><spring:message code="home.label.anorder" /></p></a></div>
										</div> --%>
											
										
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 centered borderforplaceorder1" >	
											<div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 mobile-imgTxt-view mobile-img-view">
												<a href="#" data-toggle="modal" data-target="#dispute-order" class="dispute-popup-link">
													<img src="${webroot}/_ui/responsive/common/images/dispute.png" class="img-circle " alt="disputeorder" width="40" height="40">
												</a>
											</div>
											<div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 boldtext mobile-imgTxt-view mobile-txt-view">
												<a href="#" data-toggle="modal" data-target="#dispute-order" id="" class="dispute-popup-link"><spring:message code="home.label.dispute" />&nbsp;<spring:message code="home.label.anorder" /></a>
											</div>
										</div> 
									 </div>
					
			</div>
			<!-- please read - Modal -->
			<cms:pageSlot position="BroadCastContent" var="feature" element="div">
					<cms:component component="${feature}"/>
			</cms:pageSlot>
			
			
			<cms:pageSlot position="BroadCastInformationContent" var="feature" element="div">
					<cms:component component="${feature}"/>
			</cms:pageSlot>
					
	
</div>	

     <div class="modal fade jnj-popup" id="os-pls-read-popup" role="dialog">
      <div class="modal-dialog modalcls modal-sm">
       <!-- Modal content-->
       <div class="modal-content popup">
        <div class="modal-header">
          <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
         <!--  <h4 class="modal-title">Items in Order</h4> -->
        </div>
        <div class="modal-body">
         <div class="os-more-popup-header"></div>
         <div class="os-more-popup-content"></div>
        </div>
       </div>
        
      </div>
     </div>
     <!--End of Modal pop-up-->
	
<home:disputeOrderHome></home:disputeOrderHome>									
<home:newQuote></home:newQuote>
<home:newReturn></home:newReturn>
<home:addQuickToCart></home:addQuickToCart>
<!--AAOL-6377-->
<div id="replacement-line-item-holder" style="display: none;"></div>
</template:page>
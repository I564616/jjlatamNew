<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="contactUs" tagdir="/WEB-INF/tags/responsive/contactus" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<template:page pageTitle="${pageTitle}">
		
			<div  id="templatedetailpage">
				<div class="row">
					<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
						<div class="row content">
							<div class="col-lg-8 col-md-8 col-sm-6 col-xs-12">${templateEditForm.templateName}</div>
							<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
						<!-- 	<input type="button" value="Add All to Cart" class="primarybtn templateDetailAddToCart"/> -->
								<div class="float-right-to-none">
									<input type="button" style="" id="errorMultiCartTmplt" class="tertiarybtn homeCartErrors btn btnclsnormal" value="<spring:message code='homePage.errorDetails' />" />
									<button class="btn btnclsactive addtocart primarybtn templateDetailAddToCart"><spring:message code="template.detail.addToCart"/></button>
								</div>
						
								
							</div>
						</div>
						<div class="error hide" id="addToCartErrorLayer"><p></p></div>   
					</div>
				</div>
				
				
				<div class="row">
					<div class="col-lg-12 col-md-12">
						<div class="table-padding">
							<div class="row editDeletepanel">
							<c:url value="/templates/templateDetail/deleteTemplate/${templateId}" var="templateEditUrl"/>
							<form:form action="${templateEditUrl}" commandName="templateEditForm" id="templateEditForm" method="POST">
						    <span class="textBlack templateNumber" style="display:none" >${templateId}</span><form:input path="templateNumber" id="hddnTemplateNumber" type="hidden"/>
						    <span style="display:none"><input type="text" name="templateName" id="templateName" value="${templateEditForm.templateName}"/></span>
								 <c:if test="${user.uid eq templateEditForm.author}">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 tabletbtnwidth">
									<button type="button"	class="btn btnclsnormal pull-left editTemplatebtn"><spring:message code="template.detail.editTemplate"/></button>
								
									<button type="button"	class="btn btnclsnormal pull-left deleteTemplatebtn tertiarybtn deleteTemplate"><spring:message code="template.detail.deleteTemplate"/></button>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6 tabletShareTxt">
									<div class="checkbox checkbox-info checkboxmargin pull-right styled">
									 <form:checkbox name="shareWithAccountUsers" id="shareWithAccountUsers" path="shareWithAccountUsers" class=" styled tertiarybtn saveTemplate"/>
									 <label for="shareWithAccountUsers"><spring:message code="template.detail.share.with.users"/></label>																		
									</div>
								</div>
								</c:if>
								<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6 downloadbuttons">
									<div class="pull-right downloadlinks downloadbuttons sectionBlock ">
										<strong><spring:message code="reports.download.label" /></strong>
										<a href="#" class="tertiarybtn excel"> &nbsp;<spring:message code="reports.excel.label" /> </a>
										<span class="pipesymbol">|</span> &nbsp;<a href="#" class="tertiarybtn pdf"><spring:message code="reports.pdf.label" /></a>
									</div>
								</div>
								</form:form>
							</div>
							<div class="hidden-xs">
								<table id="ordersTable"	class="table table-bordered table-striped sorting-table">
									<thead>
										<tr>
											<th class="no-sort"></th>
											<th class="no-sort"></th>
											<th class="no-sort"></th>
										</tr>
									</thead>
									<tbody>
									<c:forEach items="${orderTemplateDetail}" var="productEntry"	varStatus="count">
									<c:url value="${productEntry.refVariant.url}" var="productUrl"/>
									<tr>
									<td class="verticalmiddle">
												<c:choose>
												<c:when test="${!productEntry.refVariant.isProdViewable}">
												<product:productPrimaryImage product="${productEntry.refVariant}" format="cartIcon" />
												</c:when>
												<c:otherwise>
												<a href="${productUrl}"><product:productPrimaryImage product="${productEntry.refVariant}" format="cartIcon" /></a>
												</c:otherwise>				
												</c:choose>
									</td>
											<td class="text-left verticalmiddle">
											<span class="Tabsubtxt">
												<input type="hidden" value="${productEntry.refVariant.saleableInd && productEntry.salesRepDivisionCompatible}"  class="saleableInd" id="${productEntry.refVariant.code}" qty="${productEntry.qty}" />
												<p class="firstline text-capitalize boldtext">
													<c:choose>
													<c:when test="${!productEntry.refVariant.isProdViewable}">
											            ${productEntry.refVariant.name}
									             	</c:when>
														<c:otherwise>
															<a href="${productUrl}">
																${productEntry.refVariant.name} </a>
														</c:otherwise>
													</c:choose>
												</p>
												<p class="secondline boldtext">${productEntry.refVariant.baseMaterialNumber}</p>
												<p class="thirdline pull-left">${productEntry.refVariant.description}</p>
												<c:if test="${productEntry.refVariant.productVolume ne null}">
													<span class="descSmall">
													<spring:message	code="template.detail.volume" />
														${productEntry.refVariant.productVolume}</span>
												</c:if> <c:if	test="${productEntry.refVariant.productWeight ne null}">
													<span class="descSmall"><spring:message	code="template.detail.weight" />
														${productEntry.refVariant.productWeight}</span>
												</c:if>

										       </span>
										  <p style="margin-left: 11px;"><spring:message code="product.search.uom"/>&nbsp;&nbsp;${productEntry.refVariant.deliveryUnit}<c:if test="${productEntry.refVariant.numerator ne null && productEntry.refVariant.salesUnit ne null}">(${productEntry.refVariant.numerator}&nbsp;${productEntry.refVariant.salesUnit})</c:if>
										</p>
										       
										</td>
											<td class="paddingright40px">
												<div class="row">
													<form:form class="form-inline" role="form" >
														<div class="form-group pull-right ">
															<label for="email"><spring:message code="product.detail.addToCart.quantity"/></label> 
															<input type="text"	value="${productEntry.qty}" class="form-control txtWidth " disabled="disabled">
														</div>
													</form:form>
												</div>
												<!-- <div class="row viewprice">
													<div class="pull-right">
														<a href="">Remove</a>
													</div>
												</div> -->

											</td>

										</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
							<!-- Table collapse for mobile device-->

							<div class="visible-xs hidden-lg hidden-sm hidden-md">
								<table id="ordersTablemobile"
									class="table table-bordered table-striped sorting-table bordernone">
									<thead class="hidden-xs">
										<tr>
											<th class="text-left no-sort"></th>
											<th class="text-left no-sort"></th>
										</tr>
									</thead>
									<tbody>
									<c:forEach items="${orderTemplateDetail}" var="productEntry"	varStatus="count">
									<tr>
									<td style="padding: 25px !important">
											 <c:choose>
												<c:when test="${!productEntry.refVariant.isProdViewable}">
													<product:productPrimaryImage product="${productEntry.refVariant}" format="cartIcon" />
												</c:when>
												<c:otherwise>
													<a href="${productUrl}"><product:productPrimaryImage product="${productEntry.refVariant}" format="cartIcon" /></a>
												</c:otherwise>
											</c:choose>
											<%--  <input type="hidden" value="${productEntry.refVariant.saleableInd && productEntry.salesRepDivisionCompatible}"	class="saleableInd" id="${productEntry.refVariant.code}"qty="${productEntry.qty}" /> --%>
										<p class="firstline text-capitalize boldtext">
														<c:choose>
															<c:when test="${!productEntry.refVariant.isProdViewable}">
															${productEntry.refVariant.name}
															</c:when>
															<c:otherwise>
																<a href="${productUrl}">
																	${productEntry.refVariant.name} </a>
															</c:otherwise>
														</c:choose>
										
										</p>
										<p class="secondline boldtext">${productEntry.refVariant.baseMaterialNumber}</p>
										
										<div class="Tabsubtxt text-left verticalmiddle">
													<h6>${productEntry.refVariant.description}</h6>
													<c:if	test="${productEntry.refVariant.productVolume ne null}">
													<p class="thirdline pull-left">
													
														<strong><spring:message	code="template.detail.volume" />:</strong>${productEntry.refVariant.productVolume}
													</p>
													
													</c:if>
													
													 <c:if	test="${productEntry.refVariant.productWeight ne null}">
														<p class="thirdline pull-left marginleft30px">
															<strong>
															<spring:message code="template.detail.weight" />:
															 </strong>
															${productEntry.refVariant.productWeight}
														</p>
													</c:if>
													
													<p class="thirdline pull-left marginleft30px unitcase">
														<strong></strong>${productEntry.refVariant.deliveryUnit}
														<c:if	test="${productEntry.refVariant.numerator ne null && productEntry.refVariant.salesUnit ne null}">(${productEntry.refVariant.numerator}&nbsp;${productEntry.refVariant.salesUnit})</c:if>
													</p>
												</div>
										<div style="width: 30%; float: left; margin-top: 5px;">
													<form class="form-inline margintop5px" role="form">
														<div class="form-group ">
															<label for="email"></label>
															 <input type="text" value="${productEntry.qty}" class="form-control txtWidth" disabled="disabled">
														</div>
													</form>
												</div>
												<!-- <div class="viewprice">
													<div class="pull-left ">
														<a href="">Remove</a>
													</div>
												</div> -->
										</td>
									  <td class="hidden"></td> <!-- dont remove this td otherwise it will not load -->
									</tr>
									</c:forEach>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="row basecontent">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<button type="button"
									class="btn btnclsactive pull-right addtocart templateDetailAddToCart"><spring:message code="template.detail.addToCart"/></button>
									
							</div>
						</div>
						<!--Accordian Ends here -->
					</div>
				</div>
				<!-- Modal contactus -->
											<div class="modal fade jnj-popup" id="confirmationPopup" role="dialog">
												<div class="modal-dialog modalcls modal-md">
													<!-- Modal content-->
													<div class="modal-content" id="Confirmationext">
														
																			
																						
													
													</div>
												</div>
											</div>
										<!--End of Modal pop-up-->
			<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
		<div  id="contractPopuppage">
			<!-- Modal -->
			<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
				<div class="modal-dialog modalcls">
					<div class="modal-content popup">
						<div class="modal-header">
							<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
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
						<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart"><spring:message code="cart.common.cancel"/></a>
						<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart" ><spring:message code="contract.page.accept"/></button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Add to cart Modal pop-up to identify  contract or non contract end-->	
	</div>
		
		<!-- Error Details Popup Start-->

<div class="modal fade jnj-popup" id="error-detail-popup"
			role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close" /></button>
						<h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
					</div>
					<form method="post" action="javascript:;">
						<div class="modal-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="panel-group">
										<div class="panel panel-danger">
											  <div class="panel-heading">
												<h4 class="panel-title cart-error-msg">
												<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="homePage.errordetails.addfailed" /></span>
												</h4>
											  </div>
										</div>  
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="scroll error-content" style="font-weight:bold">
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		
		
		<!-- Error Details Popup End-->
	<!-- AAOL-6378 -->
	<div id="replacement-line-item-holder" style="display: none;"></div>		
		
</template:page>


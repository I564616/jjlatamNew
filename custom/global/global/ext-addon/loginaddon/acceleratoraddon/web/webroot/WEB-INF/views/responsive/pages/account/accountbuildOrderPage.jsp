<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>


<template:page pageTitle="${pageTitle}">
		<div id="buildorderpage">
		
		<ul class="breadcrumb">
			<li><a href="${webroot}/en/mdd-deCMSsite/home"><spring:message code="account.buildorder.home"/></a></li>
			<li class="active"><spring:message code="account.buildorder.header"/></li>
		</ul>
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content"><spring:message code="account.buildorder.header"/> 		
			</div>
			 <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 addToCartBtnHide margintop15 paddingright40px">
				<button type="submit" class="btn btnclsactive pull-right addtoCart"><spring:message code="account.buildorder.addtocart"/></button>
				<a href="home/buildorder/resetBuildOrder"  class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="account.buildorder.cancel"/></a>
			</div> 
		</div>

		<div class="row table-padding" id="productQuantityForm">
			<div class="col-lg-12 col-md-12">
			<input type="button" id="errorQuickCart" class="tertiarybtn homeCartErrors left" style="display:none;" value="<spring:message code='homePage.errorDetails' />"/>
             <div class="registerError" style="color: red;"></div>
		<!-- START DEFECT 32656 -->
		<div class="registersucess"></div>
				<div class="row subcontent1">
					<div class="col-lg-12 col-md-12 col-xs-12"><spring:message code="account.buildorder.enterproducts"/></div>
				</div>

				<div class="row subcontent2">
					<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
						<label for="usr" class="subtxt" data-msg-required="<spring:message code="cart.common.validNumber"></spring:message>" ${disableInputBox}><spring:message code="acc.build.order.prodnum"/> </label> <input
							type="text" id="productCode" class="form-control boxprop"
							id="usr">
					</div>
					
					
					
					<div
						class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12 quantitymbox">
						<label for="usr" class="subtxt "><spring:message code="account.buildorder.quantity"/></label> <input
							type="number" id="qty" class="form-control boxprop quantitybox numeric " id="usr" min="1">
					</div>
					<div class="cartbtn col-lg-3 col-md-3 col-sm-3 col-xs-12 ">
						<button type="button" id="addToOrder" 	class="btn btnclsnormal pull-right "><spring:message code="account.buildorder.addtoorder"/></button>
					</div>
				</div>


				<div class="row">
			
				<input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="10"/>
					<c:if test="${not empty buildorder.lineItems}">
						<table id="ordersTable"
							class="table table-bordered table-striped hidden-xs only-sort-table build-order-table desk-build-order-table">
							<thead>
								<tr>
									<th class="text-left"><spring:theme
											code="text.account.buildorder.productCode"
											text="PRODUCT NUMBER" /></th>
									<th class="text-left no-sort"><spring:theme
											code="text.account.buildorder.customerProductCode"
											text="PRODUCT NAME" /></th>
									<th class="no-sort"><spring:theme
											code="text.account.buildorder.quantity" text="QUANTITY" />
								
									</th>
											
											
							
									<th class="no-sort"></th>
								</tr>
							</thead>
							<tbody class="tabdata">

								 <c:forEach items="${buildorder.lineItems}" var="entry"
									varStatus="status">
									
									 <tr>
										 
									 <td class="text-left">${entry.value.product.code}
									 <input type="hidden" value="${entry.value.product.code}" class="form-control txtWidth" id="productId${status.index}" readonly="readonly">
									  </td> 
										<td class="text-left">${entry.value.product.name}</td>
										<td><input type="text" class="form-control txtWidth " id="quantity${status.index}" value="${entry.value.quantity}" entryNumber="${entry.value.quantity}">
									
											</td>

										<td><a name="${entry.value.product.code}"
											class="button_neg_cls removeProd" value="remove" style="cursor:pointer"><spring:message code="account.buildorder.delete"/></a>
										</td>
									</tr>  
								</c:forEach> 
							</tbody>
						</table>


<!-- <MOBILE DEVICE -->

<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
									<c:if test="${not empty buildorder.lineItems}">
											<table id="ordersTable" class="table table-bordered table-striped tabsize desk-build-order-table">
												<thead>
													<tr>
														<th class="text-left"><spring:theme code="text.account.buildorder.productCode" text="PRODUCT NUMBER" /></th>
													</tr>
												</thead>
												<tbody>
												 <c:forEach items="${buildorder.lineItems}" var="entry" varStatus="status">
													<tr>
														<td class="panel-title text-left">
															<a data-toggle="collapse" data-parent="#accordion" href="#collapse${status.count}" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> ${entry.value.product.code}</a>
															<div id="collapse${status.count}" class="panel-collapse collapse">
																<div class="panel-body details">
																	<p><spring:theme code="text.account.buildorder.customerProductCode" text="PRODUCT NAME" /></p>
																	<p>${entry.value.product.name}<br><spring:message code="acc.build.order.unit"/></p>
																	<p><spring:theme code="text.account.buildorder.quantity" text="QUANTITY" /></p>
																	<input type="text" class="form-control txtWidth" id="quantity${status.index}" value="${entry.value.quantity}" entryNumber="${entry.value.quantity}">
																	<a name="${entry.value.product.code}" class="button_neg_cls removeProd" value="remove" style="cursor:pointer"><spring:message code="account.buildorder.delete"/></a>
																</div>
															</div>
														</td>
													</tr>
													</c:forEach>
												</tbody>
											</table>
											</c:if>
										</div>	

<!-- <MOBILE DEVICE -->
					</c:if>
				</div>
			</div>
		</div>
		<div class="row subcontent3">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 addToCartBtnHide paddingright40px">
				<button type="submit" class="btn btnclsactive pull-right addtoCart"><spring:message code="account.buildorder.addtocart"/></button>
				<a href="home/buildorder/resetBuildOrder"  class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="account.buildorder.cancel"/></a>
			</div>
		</div>
		
					<div class="modal fade jnj-popup" id="items-in-order-popup" role="dialog">
							<div class="modal-dialog modalcls">
								<!-- Modal content-->
								<div class="modal-content popup">
									<div class="modal-header">
									  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.buildorder.close"/></button>
									  <h4 class="modal-title"><spring:message code="account.buildorder.popup.header"/></h4>
									</div>
									<div class="modal-body">
										<div class="build-order-content"><spring:message code="account.buildorder.popup.message"/></div>	
										<div class="build-order-content"><spring:message code="account.buildorder.popup.text"/></div>	
									</div>
									<div class="modal-footer ftrcls">
										<a class="btn btnclsnormal leavepage" id="leave-page-btn" href="#"><spring:message code="account.buildorder.popup.leavepage"/></a>
										<a class="btn btnclsactive" id="stay-page-btn" data-dismiss="modal" href="javascript:void(0)"><spring:message code="account.buildorder.popup.stayonpage"/></a>
									</div>
								</div>
							  
							</div>
						</div>

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
								<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart0"><spring:message code="cart.common.cancel"/></a>
								<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart0" ><spring:message code="contract.page.accept"/></button>
							</div>
						</div>
					</div>
				</div>
			<!-- Add to cart Modal pop-up to identify  contract or non contract end-->
		</div>						
</div>

</template:page>





<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<template:page pageTitle="${pageTitle}">
		<div class=" mobile-no-pad">
			<div id="buildorderpage">
				<div class="createTemplate">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
					<div class="row ">
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 headingTxt content"><spring:message code="template.create.new.template" /></div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margintop15">
							<a href="#"><button type="button" class="btn btnclsactive pull-right createNewTemplate" ><spring:message code="template.create.template" /></button></a>
							<a href="#" class="canceltxt pull-right build-ordr-cancel-btn" onclick="history.go(-1);"><spring:message code="template.create.cancel" /></a>
						</div>											
					</div>
					<div class="marTop10 marBott10" id="editTemplatezeroMsg" style="display: none">
						<div class="error">
							<spring:message code="template.create.zero.quantity.product" />
						</div>
					</div>
					<div class="panel-group" id="createTemplateMsg" style="display: none">
						<div
						class="alertBroadcast broadcastMessageContainer panel panel-danger">
						<div class="panel-heading">
							<div class="panel-title">
								<div class="row">
									<div class=" col-lg-11">
										<span class="glyphicon glyphicon-ban-circle"></span>
										<spring:message code="template.create.not.valid.product"/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
              	<div class="panel-group" id="noProdTemplateMsg"
					style="display: none">
					<div
						class="alertBroadcast broadcastMessageContainer panel panel-danger">
						<div class="panel-heading">
							<div class="panel-title">
								<div class="row">
									<div class=" col-lg-11">
										<span class="glyphicon glyphicon-ban-circle"></span>
										<spring:message code="no.product.template.added" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
					<div class="row table-padding">
						<div class="col-lg-12 col-md-12">
							<div class="row subcontent1">
								<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 form-group">
									<label for="usr" class="subtxt"><spring:message code="template.create.template.name" /> <span	class="redStar">*</span></label>
										<input type="text" class="form-control boxprop" id="templateName">
										<div  id="noTemplateNameMsg"style="display:none ;font-size: 15px;">
		                                	<div class="error"><spring:message code="no.product.template.name"/></div>
		                                   </div>
								</div>
								<div class="col-lg-7 col-md-7 col-sm-7 col-xs-11">
									<div class="checkbox checkbox-info pull-right" style="font-size:14px;padding-top:8px;padding-right: 13px;">
										<input id="check4" class="styled" type="checkbox">
										<label for="check4"><spring:message code="template.create.share.with.other.users"/></label>
									</div>
								</div>
							</div>
							<div class="row subcontent2">
									<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
										<label for="usr" class="subtxt"><spring:message code="template.create.product.number"/></label>
										<input type="text" class="form-control boxprop" id="productNumber">
									</div>
									<div class="form-group col-lg-3 col-md-3 col-sm-3 col-xs-12 quantitymbox">
										<label for="usr" class="subtxt"><spring:message code="template.create.quantity"/></label>
										<input type="text" class="form-control boxprop quantitybox" id="QtyQuantity">
									</div>
									<div class="cartbtn col-lg-4 col-md-4 col-sm-4 col-xs-12">
										<button type="button" class="btn btnclsnormal pull-right" id="createAddToTemplate"><spring:message code="template.create.addto.template"/></button>
									</div>
									
							</div>
							<div class="row">
								<table id="createOrdersTable-desktop" class="table table-bordered table-striped hidden-xs desk-create-template-table-desktop">
									<thead>
									  <tr>
										<th class="text-left text-uppercase"><spring:message code="template.create.product.number"/></th>
										<th class="text-left no-sort text-uppercase"><spring:message code="template.create.product.name"/></th>
										<th class="no-sort text-uppercase" style="text-align:center !important"><spring:message code="template.create.quantity"/></th>
										<th class="no-sort sorting_disabled" rowspan="1" colspan="1" aria-label="" style="width: 36px;"></th>
									
									  </tr>
									</thead>
									<tbody class="tabdata">
									</tbody>
								</table>
							</div>	
						</div>
					</div>
				
			<!-- Table collapse for mobile device-->
					<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
						<table id="createOrdersTable-mobile" class="table table-bordered table-striped tabsize desk-create-template-table-mobile">
							<thead>
								<tr>
									<th class="text-left"><spring:message code="template.create.product.number"/></th>
								</tr>
							</thead>
							<tbody>
							
								<%-- <tr>
									<td class="panel-title text-left">
										<a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
											<span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
											M542901
										</a>
										<div id="collapse1" class="panel-collapse collapse">
											<div class="panel-body details">
												<div class="mob-view-label uppercase"><spring:message code="template.create.product.name"/></div>
												<div class="mob-view-label uppercase"><spring:message code="template.create.quantity"/></div>
												<!-- <p>#0210 Rechargeable Battery<br>Unit: Case (1 each)</p>
												<p>QUANTITY</p> -->
												<input type="text" class="form-control txtWidth">
												<a href="#">Delete</a>
											</div>
										</div>
									</td>
								</tr> --%>
								<!-- <tr>
									<td class="panel-title text-left">
										<a data-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> CLR222US</a>
										<div id="collapse2" class="panel-collapse collapse">
											<div class="panel-body details">
												<p>PRODUCT NAME</p>
												<p>Derma Bond TM Prine OTM<br>Unit: Case(1 each)</p>
												<p>QUANTITY</p>
												<input type="text" class="form-control txtWidth">
												<a href="#">Delete</a>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="panel-title text-left">
										<a data-toggle="collapse" data-parent="#accordion" href="#collapse3" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> HARH45-03</a>
										<div id="collapse3" class="panel-collapse collapse">
											<div class="panel-body details">
												<p>PRODUCT NAME</p>
												<p>Harmonic ACE Plus 7 w/ ADV Hemostasis<br>Unit: Case (1 each)</p>
												<p>QUANTITY</p>
												<input type="text" class="form-control txtWidth">
												<a href="#">Delete</a>
											</div>
										</div>
									</td>
								</tr> -->
										
							</tbody>
						</table>
					</div>	
					
			<!--Accordian Ends here -->
		
					<div class="row subcontent3">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<a href="#"><button type="button" class="btn btnclsactive pull-right createNewTemplate" ><spring:message code="template.create.template" /></button></a>
							<a href="#" class="canceltxt pull-right build-ordr-cancel-btn" onclick="history.go(-1);"><spring:message code="template.create.cancel" /></a>
						</div>
					</div>
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
					<div class="build-order-content"><spring:message code="account.CreateTemplate.popup.message"/></div>	
				</div>
				<div class="modal-footer ftrcls">
					<a class="btn btnclsnormal leavepage" id="leave-page-btn" href="#"><spring:message code="account.buildorder.popup.leavepage"/></a>
					<button class="btn btnclsactive" id="stay-page-btn" data-dismiss="modal" ><spring:message code="account.buildorder.popup.stayonpage"/></button>
				</div>
			</div>
		  
		</div>
	</div>
			</div>
</template:page>
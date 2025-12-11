<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>


<templateLa:page pageTitle="${pageTitle}">
    <c:set var="val1"><spring:message code="template.create.delete"/></c:set>
    <input id="delete_latam" type="hidden" value="${val1}"/>
    <c:set var="val2"><spring:message code="product.search.uom"/></c:set>
    <input id="unit_latam" type="hidden" value="${val2}"/>
        <div class=" mobile-no-pad">
			<div id="buildorderpage">
				<div class="createTemplate">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
						<div class="row ">
							<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 headingTxt content">
							    <spring:message code="template.create.new.template" />
							</div>
							<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margintop15">
							    <a href="#">
							        <button type="button" class="btn btnclsactive pull-right" disabled="disabled"
							        id="createNewTemplate">
							            <spring:message code="template.create.template" />
							        </button>
							    </a>
							    <a href="#" class="canceltxt pull-right build-ordr-cancel-btn-latam" data-bs-toggle="modal"
							        data-bs-target="#items-in-template-popup">
							        <spring:message code="template.create.cancel" />
							    </a>
							</div>
						</div>
						<div class="marTop10 marBott10" id="createTemplateMsg" style="display: none;">
						    <laCommon:genericMessage messageCode="template.create.not.valid.product" icon="ban-circle" panelClass="danger" />
						</div>
						<div class="marTop10 marBott10" id="emptyProductCodeMsg" style="display: none;">
							<laCommon:genericMessage messageCode="template.create.empty.product" icon="ban-circle" panelClass="danger" />
						</div>
						<div class="marTop10 marBott10" id="validQuantityMsg" style="display: none;">
							<laCommon:genericMessage messageCode="template.create.not.valid.quantity" icon="ban-circle" panelClass="danger" />
						</div>
							
						<div class="row table-padding">
							<div class="col-lg-12 col-md-12">
								<div class="row subcontent1">
									<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 form-group display-table">
										<label for="usr" class="subtxt table-cell">
										    <spring:message code="template.create.template.name" />
										</label>
										<input type="text" class="form-control box-width table-cell" id="templateName">
											<div class="error subtxt" id="noTemplateNameMsg" style="display:none">
										        <spring:message code="template.create.templatename.error"/>
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
									<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12 display-table">
										<label for="usr" class="subtxt table-cell"><spring:message code="template.create.product.number"/></label>
										<input type="text" class="form-control box-width table-cell" id="productNumber">
									</div>
									<div class="form-group col-lg-3 col-md-3 col-sm-3 col-xs-12 quantitymbox display-table">
										<label for="usr" class="subtxt table-cell">
										    <spring:message code="template.create.quantity"/>
										</label>
										<input type="text" class="form-control Qbox-width table-cell quantitybox" id="QtyQuantity">
									</div>
									<div class="cartbtn col-lg-4 col-md-4 col-sm-4 col-xs-12">
										<button type="button" class="btn btnclsnormal pull-right" id="laCreateAddToTemplate">
										    <spring:message code="template.create.addto.template"/>
										</button>
									</div>
								</div>
								<div class="row d-none d-sm-block" >
									<input type="hidden" value="desktop" class="currViewScreenName"/>
									<table id="createOrdersTable-desktop" class="table table-bordered table-striped hidden-xs desk-create-template-table-desktop">
										<thead>
											<tr>
											    <th class="text-left text-uppercase"><spring:message code="template.create.product.number"/></th>
												<th class="text-left no-sort text-uppercase"><spring:message code="template.create.product.name"/></th>
												<th class="no-sort text-uppercase" style="text-align:center !important"><spring:message code="template.create.quantity"/></th>
												<th class="no-sort sorting_disabled" rowspan="1" colspan="1" aria-label="" style="width: 36px;"></th>
											</tr>
										</thead>
									    <tbody class="tabdata"></tbody>
									</table>
								</div>
							</div>
						</div>
									
						<!-- Table collapse for mobile device-->
					    <div class="Subcontainer d-none d-xs-block">
					    	 <input type="hidden" value="mobile" class="currViewScreenName"/>
						    <table id="createOrdersTable-mobile" class="table table-bordered table-striped tabsize desk-create-template-table-mobile">
							    <thead>
								    <tr>
									    <th class="text-left"><spring:message code="template.create.product.number"/></th>
								    </tr>
							    </thead>
							    <tbody>
							    </tbody>
						    </table>
					    </div>

						<!--Accordian Ends here -->
						<div class="row subcontent3">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<a href="#">
								    <button type="button" class="btn btnclsactive pull-right" disabled="disabled" id="createNewTemplate2">
								        <spring:message code="template.create.template" />
								    </button>
								</a>
				    			<a href="#" class="canceltxt pull-right build-ordr-cancel-btn-latam" data-bs-toggle="modal" data-bs-target="#items-in-template-popup"><spring:message code="template.create.cancel" /></a>
							</div>
						</div>
					</div>
				</div>
			<div class="modal fade jnj-popup" id="items-in-template-popup" role="dialog">
			    <div class="modal-dialog modalcls">
				    <!-- Modal content-->
					<div class="modal-content popup">
						<div class="modal-header">
						    <h4 class="modal-title">
						        <spring:message code="template.cancel.popupheader"/>
						    </h4>
						    <a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></a>
						</div>
					<div class="modal-body">
						<div class="build-order-content">
						    <spring:message code="template.cancel.popupmsg"/>
						</div>
					</div>
					<div class="modal-footer ftrcls">
						<a class="btn btnclsnormal leavepage" id="leave-page-btn-template" href="#">
						    <spring:message code="template.cancel.popup.leavepage"/>
						</a>
						<a class="btn btnclsactive" id="stay-page-btn" data-bs-dismiss="modal" href="javascript:void(0)">
						    <spring:message code="template.cancel.popup.stayonpage"/>
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</templateLa:page>
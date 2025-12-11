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
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <c:set var="val1"><spring:message code="template.create.delete"/></c:set>
    <input id="delete_latam" type="hidden" value="${val1}"/>
    <c:set var="val2">
        <spring:message code="product.search.uom"/>
    </c:set>
    <input id="unit_latam" type="hidden" value="${val2}"/>
	<div class="col-lg-12 col-md-12 mobile-no-pad">
		<div id="buildorderpage">
			<div class="createTemplate">
				<c:url value="/templates/templateDetail/deleteTemplate/${templateId}" var="templateEditUrl"/>
				<form:form action="${templateEditUrl}" modelAttribute="templateEditForm" id="templateEditForm" method="POST">
				    <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
				        <div class="row ">
					        <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 headingTxt content">
							    <spring:message code="template.edit.page.label"/>
							</div>
							<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margintop15">
								<a href="#"><button type="button" class="btn btnclsactive pull-right" id="lasaveEditedTemplate">
							            <spring:message code="template.edit.save.template" />
							        </button>
							    </a>
								<a href="#" class="canceltxt pull-right build-ordr-cancel-btn" data-bs-toggle="modal" data-bs-target="#items-in-edittemplate-popup">
								    <spring:message code="template.create.cancel" />
								</a>
							</div>
						</div>
						<div class="marTop10 marBott10" id="editTemplateMsg"style="display:none">
							<laCommon:genericMessage messageCode="template.create.not.valid.product" icon="ban-circle" panelClass="danger" />
						</div>
		                <div class="marTop10 marBott10" id="validQuantityMsg"style="display:none">
		                    <laCommon:genericMessage messageCode="template.create.not.valid.quantity" icon="ban-circle" panelClass="danger" />
		                </div>
						<form:input path="templateNumber" id="hddnEditTemplateNumber" type="hidden" value="${templateId}"/>
							<div class="row table-padding">
								<div class="col-lg-12 col-md-12">
									<div class="row subcontent1">
										<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 form-group ">
											<label for="templateName" class="subtxt">
											    <spring:message code="template.detail.template.name"/>
											</label>
											<input type="text" class="form-control boxprop" name="templateName" id="templateName"
											    value="${templateEditForm.templateName}"/>
											<div class="error subtxt" id="noTemplateNameMsg" style="display:none">
												<spring:message code="template.create.templatename.error"/>
											</div>
										</div>
										<div class="col-lg-7 col-md-7 col-sm-7 col-xs-11">
											<div class="checkbox checkbox-info pull-right" style="font-size:14px;padding-top:8px;padding-right: 13px;">
												<c:url value="/templates/templateDetail/deleteTemplate/${templateId}" var="templateEditUrl"/>
							                        <form:checkbox name="shareWithAccountUsers" id="shareWithAccountUsers" path="shareWithAccountUsers"
							                              class=" styled tertiarybtn saveTemplate"/>
												<label for="check4"><spring:message code="template.create.share.with.other.users" /></label>
											</div>
										</div>
									</div>
									<div class="row subcontent2">
										<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
											<label for="usr" class="subtxt">
											    <spring:message code="template.create.product.number"/>
											</label>
										<div>
									        <input type="text" class="form-control boxprop" style="margin-left:0px" id="editProductNumber">
										</div>
									</div>
									<div class="form-group col-lg-3 col-md-3 col-sm-3 col-xs-12 quantitymbox">
										<label for="usr" class="subtxt"><spring:message code="template.create.quantity"/></label>
									<div >
										 <input type="text" class="form-control boxprop quantitybox" style="margin-left:0px" id="editQuantity">
									</div>
								</div>

								<div class="cartbtn col-lg-4 col-md-4 col-sm-4 col-xs-12" style="padding-top:17px">
									<button type="button" class="btn btnclsnormal pull-right" id="laEditAddToTemplate">
								        <spring:message code="template.create.addto.template"/>
									</button>
								</div>
							</div>
							<div class="row d-none d-sm-block">
								<input type="hidden" value="desktop" class="currViewScreenName"/>
								<table id="editOrdersTable-desktop" class="table table-bordered table-striped  only-sort-table desk-edit-template-table">
									<thead>
									    <tr>
									        <th class="text-left text-uppercase"><spring:message code="template.create.product.number"/></th>
											    <th class="text-left no-sort text-uppercase"><spring:message code="template.create.product.name"/></th>
												<th class="no-sort text-uppercase"><spring:message code="template.create.quantity"/></th>
												<th class="no-sort"></th>
										</tr>
									</thead>
									<tbody class="tabdata">
										<c:forEach items="${orderTemplateDetail}" var="productEntry" varStatus="count">
							                <c:url value="${productEntry.productUrl}" var="productUrl"/>
										    <tr id='${productEntry.refVariant.baseMaterialNumber}'>
												<td class="text-left">
												    <span class="editProductCode" id="${productEntry.catalogId}">
													    ${productEntry.catalogId}
													</span>
												</td>
												<td class="text-left">${productEntry.refVariant.name}</td>
												<td>
												    <input type="text" class="form-control txtWidth" id="prodQnty" value="${productEntry.qty}">
												        <p >
												            <spring:message code="product.search.uom"/>:
												            &nbsp;${productEntry.refVariant.deliveryUnit}
												            <c:if test="${productEntry.refVariant.numerator ne null && productEntry.refVariant.salesUnit ne null}">
													            (${productEntry.refVariant.numerator}&nbsp;${productEntry.refVariant.baseUnitCode})
													        </c:if>
									                    </p>
									                </td>
												<td>
												    <a href="#" class="deleteTemplateProd"><spring:message code="template.create.delete"/></a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form:form>

                <!-- Table collapse for mobile device-->
                <div class="Subcontainer d-block d-sm-none">
                	<input type="hidden" value="desktop" class="currViewScreenName"/>
                    <table id="editOrdersTable-mobile" class="table table-bordered table-striped tabsize desk-create-template-table-mobile">
                        <thead>
                            <tr>
                                <th class="text-left"><spring:message code="template.create.product.number"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderTemplateDetail}" var="productEntry" varStatus="count">
                                <c:url value="${productEntry.productUrl}" var="productUrl"/>
                                <tr id='${productEntry.catalogId}'>
                                    <td class="panel-title text-left">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${productEntry.catalogId}" class="ref_no toggle-link panel-collapsed">
                                            <span class="glyphicon glyphicon-plus editProductCode" id="${productEntry.catalogId}">${productEntry.catalogId}</span>
                                        </a>
                                        <div id="collapse${productEntry.catalogId}" class="panel-collapse collapse">
                                            <div class="panel-body details">
                                                <p><spring:message code="template.create.product.name"/></p>
                                                <p>${productEntry.refVariant.name}<br>
                                                <spring:message code="product.search.uom"/>&nbsp;${productEntry.refVariant.deliveryUnit}
                                                <c:if test="${productEntry.refVariant.numerator ne null && productEntry.refVariant.salesUnit ne null}">
                                                    (${productEntry.refVariant.numerator}&nbsp;${productEntry.refVariant.baseUnitCode})
                                                </c:if>
                                                </p>
                                                <p><spring:message code="template.create.quantity"/></p>
                                                <input type="text" class="form-control txtWidth numeric-only" id="prodQnty" value="${productEntry.qty}">
                                                    <a href="#" class="deleteTemplateProd">
                                                        <spring:message code="template.create.delete"/>
                                                    </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!--Accordian Ends here -->
                <div class="row subcontent3">
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <a href="#">
                            <button type="button" class="btn btnclsactive pull-right" id="lasaveEditedTemplate">
                                <spring:message code="template.edit.save.template" />
                            </button>
                        </a>
                        <a href="#" class="canceltxt pull-right build-ordr-cancel-btn" data-bs-toggle="modal" data-bs-target="#items-in-edittemplate-popup">
                            <spring:message code="template.create.cancel" />
                        </a>
                    </div>
                </div>
            </div>
        </div>
	    <div class="modal fade jnj-popup" id="items-in-edittemplate-popup" role="dialog">
		    <div class="modal-dialog modalcls">
	            <!-- Modal content-->
		        <div class="modal-content popup">
			        <div class="modal-header">
					    <h4 class="modal-title">
					        <spring:message code="template.edit.cancel.popupheader"/>
					    </h4>
					     <a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></a>
				    </div>
				    <div class="modal-body">
					    <div class="build-order-content"><spring:message code="template.edit.cancel.popupmsg"/></div>
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
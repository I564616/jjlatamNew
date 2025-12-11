<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="templateLa"
	tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="lacommon"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>

<c:set value="${fn:toUpperCase(countryCode)}" var="currentCountry"/>

<templateLa:page pageTitle="${pageTitle}">

	<div id="paymentpage">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row">
			<div class="col-xs-12 col-sm-3 col-md-3 col-lg-3 headingTxt content">
				<spring:message code="cart.review.Payment" />
			</div>
			<div class="col-xs-12 col-sm-9 col-md-9 col-lg-9">
				<div class="btn-group pull-right">
					<ul id="breadcrumbs-one">
						<li><a href="shipping">1. Shipping</a></li>
						<li><a href="paymentContinue"><strong>2. </strong>Payment</a></li>
						<li><a href="orderReview">3. Review</a></li>
					</ul>
				</div>
	        </div>
		</div>

		<div class="addresspane">
			<div class="row shipping-row-padding">
				<div
					class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ship-address-pane">
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
							<strong><spring:message code="cart.common.orderType" /></strong>&nbsp;
							<spring:message
								code="cart.common.orderType.${cartData.orderType}" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
							<strong><spring:message
									code="header.information.account.number" /></strong>
							${cartData.b2bUnitId}
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Table collapse for mobile device-->
		<form:form action="reviewOrder" enctype="multipart/form-data"
			id="submitReviewOrderForm" method="POST">
			<input type="hidden" value="${isContractCart}" id="isContractFlag" />
			<div class="Subcontainer ">
				<table id="ordersTable"
					class="table table-bordered table-striped tabsize">
					<tbody>
						<c:if test="${cartData.isContractCart}">
							<tr>
								<td class="panel-title text-left">
									<div class="lightboxtemplate" id="uploadDeliveredOrder">
										<h2>
											<spring:message code="cart.deliver.fileUpload.header" />
										</h2>
										<div>
											<spring:message code="cart.deliver.contract.fileUpload" />
											<br />
											<br />
											<div class="btn-file btnclsactive">
												<input name="deliveredOrderDoc" id="uploadBrowseFile"
													type="file" multiple class="hidden-input" />
												<spring:message code='text.sellout.browsefile' />
											</div>
											<div id="empechoFilesList"></div>
											<div class="registerError invalidFileError"
												style="display: none; margin: 0;">
												<label class="error"> <spring:message
														code="cart.deliver.fileUpload.error" />
												</label>
											</div>
											<div class="registerError removeFileError"
												style="display: none; margin: 0;">
												<label class="error"> <label:message
														messageCode="cart.delivered.order.remove.file.error" />
												</label>
											</div>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td class="panel-title text-left"><a data-toggle="collapse"
									data-parent="#accordion" href="#collapse1"
									class="ref_no toggle-link panel-collapsed"> <!-- <span class="glyphicon glyphicon-plus"></span> -->
										<spring:message code="cart.payment.paywithPurchageOrder" />
								</a>
								<c:if test="${placeOrderResComUserGrpFlag eq false}" >
								<div class="panel-body details">
								   <div>
									   <label><spring:message
											   code="cart.payment.purchageorder" /> &nbsp;</label> <input
										   id="purchOrder" type="text"
										   class="form-control textboxstyle ${disabled} value="${cartData.purchaseOrderNumber}">
								   </div>
							   </div>
						       </c:if>
								<c:if test="${placeOrderResComUserGrpFlag eq true }">
                                    <div class="row panel-body details">
                                        <div class="row col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                            <label style=" margin-left: -15px;" ><spring:message
                                                code="cart.payment.purchageorder" /> &nbsp;</label> </div>
                                            <div class="col-lg-9 col-md-9 col-sm-6 col-xs-12">
                                            <input id="purchOrder" type="text"
                                                class="form-control textboxstyle ${disabled} value="${cartData.purchaseOrderNumber}" style="margin-left: -20px; width: 350px">
                                            </div>
                                        </div>
                                        <div class="row col-lg-6 col-md-6 col-sm-6 col-xs-12" style="margin-top: -22px;margin-left: 20px">
                                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                                <label style="width: 435px;"><spring:message code="cart.payment.complementary.info.label.${currentCountry}" /> &nbsp;
                                                    <sup class="star" style="margin-left: -4px;">*</sup>
                                                </label>
                                            </div>
                                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                                <input id="complementaryInfoOrder" type="text" class="form-control textboxstyle ${disabled} value="complementaryInfo"  style="width:390px;">
                                            </div>
											<div id="complementaryError" style=" display: none; margin-top: 5px;" class="error errorYourInfo marginGap30  col-lg-12 col-md-12 col-sm-12 col-xs-12">
												<spring:message code="cart.payment.complementaryinfoerror" />
											</div>
											<div id="complementarymendatoryError" style=" display: none; margin-top: 5px;" class="error errorYourInfo marginGap30  col-lg-12 col-md-12 col-sm-12 col-xs-12">
												<spring:message code="cart.payment.complementarymandatoryError" />
											</div>
                                        </div>
                                        
                                    </div>
                                </c:if>
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<div class="Subcontainer ">
				<table id="ordersTable"
					class="table table-bordered table-striped tabsize">
					<tbody>
					<c:if test="${cartData.isContractCart eq false}">
						<c:if
							test="${cartData.orderType eq 'ZOR' && b2bUnitFileUploadFlag eq true}">
							<tr>
								<td class="panel-title text-left">
									<div class="lightboxtemplate" id="uploadDeliveredOrder">
										<input type="hidden" id="fileType" value="${fileTypes}">
										<h2>
											<spring:message code="cart.deliver.fileUpload.header" />
										</h2>
										<div>
											<spring:message code="cart.deliver.paymentpage.fileUpload" />
											<br />
											<br />
											<div class="btn-file btnclsactive">
												<input name="deliveredOrderDoc" id="uploadBrowseFile1"
													type="file" multiple class="hidden-input" />
												<spring:message code='text.sellout.browsefile' />
											</div>
											<div id="empechoFilesList1"></div>
											<div class="registerError invalidFileError1"
												style="display: none; margin: 0;">
												<label class="error"> <spring:message
														code="stdorder.payment.fileUpload.error" />
												</label>
											</div>
											<div class="registerError removeFileError"
												style="display: none; margin: 0;">
												<label class="error"> <label:message
														messageCode="cart.delivered.order.remove.file.error" />
												</label>
											</div>
										</div>
									</div>
								</td>
							</tr>
						</c:if>
					</c:if>
						<c:if test="${cartData.isContractCart ne true}">
						<tr>
							<td class="panel-title text-left"><a data-toggle="collapse"
								data-parent="#accordion" href="#collapse1"
								class="ref_no toggle-link panel-collapsed"> <!-- <span class="glyphicon glyphicon-plus"></span> -->
									<spring:message code="cart.payment.paywithPurchageOrder" />
							</a>
						
								<div class="panel-body details">
								   <div>
									   <label><spring:message
											   code="cart.payment.purchageorder" /> &nbsp;</label> <input
										   id="purchOrder" type="text"
										   class="form-control textboxstyle ${disabled} value="${cartData.purchaseOrderNumber}">
								   </div>
							   </div>
						   
							
							</td>
						</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<!--Accordian Ends here -->

			<div class="row subcontent3">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
					<button type="submit"
						class="btn btnclsactive pull-right text-uppercase revieworder">
						<spring:message code="cart.review.shoppingCart.checkout" />
					</button>
				</div>
			</div>
			<standardCart:reviewEntries />
			<div class="d-block d-sm-none">
				<table id="datatab-mobile"
					class="table table-bordered table-striped sorting-table">
					<thead>
						<tr>
							<th class="no-sort text-left"><spring:message
									code="cart.payment.product" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${cartData.entries}" var="entry"
							varStatus="count">
							<tr>
								<td class="text-left"><standardCart:productDescriptionBeforeValidation
										entry="${entry}" showRemoveLink="false" showStatus="false" />
									<p>
										<spring:message code="cart.payment.quantityQty" />
									</p> ${entry.quantity}&nbsp;${entry.product.salesUnit}
									<p>${entry.basePrice}</p>
									<p></p>
									<p>${entry.totalPrice}</p>
									<p></p></td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>

			<div class="row subcontent3">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
					<button type="submit"
						class="btn btnclsactive pull-right text-uppercase revieworder">
						<spring:message code="cart.review.shoppingCart.checkout" />
					</button>
				</div>
			</div>
			<!-- End - Total Price Summary -->
		</form:form>
	</div>
</templateLa:page>

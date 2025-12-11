<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<c:url value="/order-history/invoiceSubmitDisputeItemInq" var="url" />
<form:form commandName="disputeItemInquiryForm"
	id="disputeItemInquiryForm" name="disputeItemInquiryForm" method="POST"
	action="${url}">
	<input type="hidden" value="${disputeItemInquiryForm.accountNumber}"
		name="accountNumber">
	<input type="hidden"
		value="${disputeItemInquiryForm.purchaseOrderNumber}"
		name="purchaseOrderNumber">
	<input type="hidden" value="${disputeItemInquiryForm.orderCode}"
		name="orderCode" />
	<input type="hidden"
		value="${disputeItemInquiryForm.disputeInvoiceNumber}"
		name="disputeInvoiceNumber" />
	<div class="col-lg-12 col-md-12 mobile-no-pad disputeOrder">
		<div id="help-page">
			<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content">
					<spring:message code="disputeItem.heading.text" />
					<div class="heading-note" style="font-size: 18px !important">
						<spring:message code="dispute.order.select.appropriate" />
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
					<c:url value="/order-history/order/invoiceDetail/${orderData.code}"
						var="invoiceDetailUrl" />
					<input type="button"
						class="primarybtn btn btnclsactive pull-right naSubmitDisputeItem"
						value="Submit Dispute" /> <a href="${invoiceDetailUrl}"
						class="canceltxt pull-right build-ordr-cancel-btn"><spring:message
							code="dispute.order.cancel" /></a>
					<div class="req-text">
						(<span style="color: #b41601;">*</span>
						<spring:message code="dispute.Invoice.required.text" />
					</div>
				</div>
			</div>
			<div class="boxshadow">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 "
						id="accordion">
						<div class="help-accordian panel disputeItemInvoice ">
							<div class="help-accordian-header">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapse1"
									class="ref_no toggle-link panel-collapsed table-cell"> <span
									class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
									<span class="table-cell"><spring:message
											code="disputeItem.text.heading" /></span>
								</a>
							</div>
							<div class="help-accordian-body panel-collapse collapse"
								id="collapse1">
								<div class="hidden-xs">
									<table id=""
										class="table table-bordered table-striped dispute-an-item-table table-disbaled">
										<thead>
											<tr>
												<th class="invoice-dispute-head-chk"></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.product" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.qty" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.disputedPrice" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.correctPrice" /><span class="star">*</span></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.contactNumber" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.invoiceNumber" /></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${invoice}" var="entry" varStatus="status">
												<tr>
													<td>
														<div
															class="checkbox checkbox-info invoice-dispute-checkbox">
															<input id="prodCheck_desktop_${status.index}"
																index="${status.index}" name="prodCheck"
																class="styled prodCheck" type="checkbox"> <label
																for="acc1-check1"></label>
														</div>
													</td>
													<td class="text-left prodCode "
														id="prodCode_desktop_${status.index}" name="prodCode"
														index="${status.index}" data="${entry.product.code }">${entry.product.code }</td>
													<td class="text-left prodQty "
														id="prodQty_desktop_${status.index}" name="prodQty"
														index="${status.index}"
														data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
														<div>${entry.qty}</div>
														<div>${entry.product.deliveryUnit}(${entry.product.numerator}
															&nbsp; ${entry.product.salesUnit})</div>
													</td>
													<td class="text-left disputedPrice "
														id="disputedPrice_desktop_${status.index}"
														name="disputedPrice" index="${status.index}"
														data="${entry.totalPrice.value}">${entry.totalPrice.value}
													</td>
													<td class="text-left "><input type="text"
														class="form-control invoiceDisputeTxtField correctPrice inputTxtInvoice"
														id="correctPrice_desktop_${status.index}"
														name="correctPrice" index="${status.index}" value=""
														disabled>
														<div style="display: none; color: #B41601;"
															class="emptyCorrectPriceError invoicedisputeerror"
															id="emptyCorrectPriceError_desktop_${status.index}">
															<spring:message
																code="dispute.order.overshipped.CorrectPrice.errormessage" />
														</div></td>
													<td class="text-left contactNumber"
														id="contactNumber_desktop_${status.index}"
														name="contactNumber" index="${status.index}"
														data="${entry.contractNum}">${entry.contractNum}</td>
													<td class="text-left invoiceNumber"
														id="invoiceNumber_desktop_${status.index}"
														name="invoiceNumber" index="${status.index}"
														data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}
													</td>


												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="productCodeValidationError" style="display: none;">
										<div class="registerError">
											<spring:message
												code="dispute.order.overshipped.product.errormessage" />
										</div>
									</div>
								</div>
								<!-- Mobile for Pricing dispute Start -->

								<div class="row visible-xs">
									<div class="col-xs-12">
										<table id=""
											class="table table-bordered table-striped dispute-an-item-mobi dispute-an-item-table">
											<tbody>
												<c:forEach items="${invoice}" var="entry" varStatus="status">
													<tr>
														<td class="dispute-chckbox-cell">
															<div
																class="checkbox checkbox-info invoice-dispute-mobichkbox">
																<input id="prodCheck_mobile_${status.index}"
																	index="${status.index}" name="prodCheck"
																	class="styled prodCheck" type="checkbox"> <label
																	for="acc1-mob-check1"></label>
															</div>
														</td>
														<td class="text-left"><a
															href="#collapse-tab-${status.index}"
															data-toggle="collapse" data-parent="#accordion1"
															class="toggle-link-sub panel-collapsed"> <span
																class="glyphicon glyphicon-plus"></span> <span><spring:message
																		code="dispute.order.overshipped.product" /></span>
														</a>
															<div class="text-left prdctCodemb prodCode "
																id="prodCode_mobile_${status.index}" name="prodCode"
																index="${status.index}" data="${entry.product.code }">${entry.product.code }</div>
															<div id="collapse-tab-${status.index}"
																class="panel-collapse collapse sub-inputs">
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message code="dispute.order.overshipped.qty" />
																	</p>
																	<div class="text-left prodQty "
																		id="prodQty_mobile_${status.index}" name="prodQty"
																		index="${status.index}"
																		data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
																		<div>${entry.qty}</div>
																		<div>${entry.product.deliveryUnit}(${entry.product.numerator}
																			&nbsp; ${entry.product.salesUnit})</div>
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message code="dispute.order.disputedPrice" />
																	</p>
																	<div class="text-left disputedPrice "
																		id="disputedPrice_mobile_${status.index}"
																		name="disputedPrice" index="${status.index}"
																		data="${entry.totalPrice.value}">${entry.totalPrice.value}
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message code="dispute.order.correctPrice" />
																		<span class="star">*</span>
																	</p>
																	<div class="text-left ">
																		<input type="text"
																			class="form-control invoiceDisputeTxtField invoiceDisputeMobieTxt correctPrice inputTxtInvoice"
																			id="correctPrice_mobile_${status.index}"
																			name="correctPrice" index="${status.index}" value=""
																			disabled>
																		<div style="display: none; color: #B41601;"
																			class="emptyCorrectPriceError invoicedisputeerror"
																			id="emptyCorrectPriceError_mobile_${status.index}">
																			<spring:message
																				code="dispute.order.overshipped.CorrectPrice.errormessage" />
																		</div>
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message code="dispute.order.contactNumber" />
																	</p>
																	<div class="text-left contactNumber"
																		id="contactNumber_mobile_${status.index}"
																		name="contactNumber" index="${status.index}"
																		data="${entry.contractNum}">${entry.contractNum}</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.invoiceNumber" />
																	</p>
																	<div class="text-left invoiceNumber"
																		id="invoiceNumber_mobile_${status.index}"
																		name="invoiceNumber" index="${status.index}"
																		data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}
																	</div>
																</div>
															</div></td>
													</tr>

												</c:forEach>
											</tbody>
										</table>
										<div class="productCodeValidationError" style="display: none;">
											<div class="registerError">
												<spring:message
													code="dispute.order.overshipped.product.errormessage" />
											</div>
										</div>
									</div>
								</div>

								<!-- Mobile for Pricing dispute End -->
							</div>
						</div>


						<div class="help-accordian  panel disputeItemInvoice ">
							<div class="help-accordian-header">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapse2"
									class="ref_no toggle-link panel-collapsed table-cell"> <span
									class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
									<span class="table-cell"><spring:message
											code="disputeItem.Shortshipped.text" /></span>
								</a>
							</div>
							<div class="help-accordian-body panel-collapse collapse"
								id="collapse2">
								<div class="hidden-xs">
									<table id=""
										class="table table-bordered table-striped dispute-an-item-table table-disbaled">
										<thead>
											<tr>
												<th class="invoice-dispute-head-chk"></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.product" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.qtyreceived" /><span
													class="star">*</span></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.qtyordered" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.invoiceNumber" /></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${invoice}" var="entry" varStatus="status">
												<tr>
													<td>
														<div
															class="checkbox checkbox-info invoice-dispute-checkbox">
															<input id="prodCheckShort_desktop_${status.index}"
																index="${status.index}" name="prodCheckshort"
																class="styled prodCheckShort" type="checkbox"> <label
																for="acc2-check1"></label>
														</div>
													</td>
													<td class="text-left prodCodeShort"
														id="prodCodeShort_desktop_${status.index}"
														index="${status.index}" name="prodCodeShort"
														data="${entry.product.code }">${entry.product.code }</td>
													<td class="text-left "><input type="text"
														class="form-control invoiceDisputeQtyTxtField prodQtyReceived inputTxtInvoice"
														id="prodQtyReceived_desktop_${status.index}"
														index="${status.index}" name="prodQtyReceived"
														disabled="true">
														<div style="display: none; color: #B41601;"
															class="emptyQtyreceivedError invoicedisputeerror"
															id="emptyQtyreceivedError_desktop_${status.index}">
															<spring:message
																code="dispute.order.overshipped.qtyreceived.errormessage" />
														</div></td>
													<td class="text-left prodQtyOrdered"
														id="prodQtyOrdered_desktop_${status.index}"
														index="${status.index}" name="prodQtyOrdered"
														data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
														<div>${entry.qty}</div>
														<div>${entry.product.deliveryUnit}(${entry.product.numerator}
															&nbsp; ${entry.product.salesUnit})</div>
													</td>
													<td class="text-left invoiceNumber"
														id="invoiceNumberShort_desktop_${status.index}"
														index="${status.index}" name="invoiceNumberShort"
														data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}
													</td>

												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="productCodeValidationError" style="display: none;">
										<div class="registerError">
											<spring:message
												code="dispute.order.overshipped.product.errormessage" />
										</div>
									</div>
								</div>

								<!-- Mobile for Short Shipped Start -->
								<div class="row visible-xs">
									<div class="col-xs-12">
										<table id=""
											class="table table-bordered table-striped dispute-an-item-mobi dispute-an-item-table">
											<tbody>
												<c:forEach items="${invoice}" var="entry" varStatus="status">
													<tr>
														<td class="dispute-chckbox-cell">
															<div
																class="checkbox checkbox-info invoice-dispute-mobichkbox">
																<input id="prodCheckShort_mobile_${status.index}"
																	index="${status.index}" name="prodCheckshort"
																	class="styled prodCheckShort" type="checkbox">
																<label for="acc2-mob-check1"></label>
															</div>
														</td>
														<td class="text-left"><a
															href="#acc2-collapse-tab-${status.index}"
															data-toggle="collapse" data-parent="#accordion2"
															class="toggle-link-sub panel-collapsed"> <span
																class="glyphicon glyphicon-plus"></span> <span><spring:message
																		code="dispute.order.overshipped.product" /></span>
														</a>
															<div class="text-left prdctCodemb prodCodeShort"
																id="prodCodeShort_mobile_${status.index}"
																index="${status.index}" name="prodCodeShort"
																data="${entry.product.code }">${entry.product.code }</div>
															<div id="acc2-collapse-tab-${status.index}"
																class="panel-collapse collapse sub-inputs">
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.qtyreceived" />
																		<span class="star">*</span>
																	</p>
																	<input type="text"
																		class="form-control invoiceDisputeMobieTxt prodQtyReceived"
																		id="prodQtyReceived_mobile_${status.index}"
																		index="${status.index}" name="prodQtyReceived"
																		disabled="true">
																	<div style="display: none; color: #B41601;"
																		class="emptyQtyreceivedError invoicedisputeerror"
																		id="emptyQtyreceivedError_mobile_${status.index}">
																		<spring:message
																			code="dispute.order.overshipped.qtyreceived.errormessage" />
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.qtyordered" />
																	</p>
																	<div class="text-left prodQtyOrdered"
																		id="prodQtyOrdered_mobile_${status.index}"
																		index="${status.index}" name="prodQtyOrdered"
																		data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
																		<div>${entry.qty}</div>
																		<div>${entry.product.deliveryUnit}(${entry.product.numerator}
																			&nbsp; ${entry.product.salesUnit})</div>
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.invoiceNumber" />
																	</p>
																	<div class="text-left invoiceNumber"
																		id="invoiceNumberShort_mobile_${status.index}"
																		index="${status.index}" name="invoiceNumberShort"
																		data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}</div>
																</div>
															</div></td>
													</tr>

												</c:forEach>
											</tbody>
										</table>
										<div class="productCodeValidationError" style="display: none;">
											<div class="registerError">
												<spring:message
													code="dispute.order.overshipped.product.errormessage" />
											</div>
										</div>
									</div>
								</div>

								<div class="dispute-panel">
									<div class="row">
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<spring:message
												code="disputeItem.Shortshipped.replacementNeeded" />
										</div>
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<div class="radio radio-info radio-inline">
												<form:radiobutton path="replacementRequired"
													id="replacementRequiredYes" value="true" checked="" />
												<label for="dispute-yes">Yes</label>
											</div>

											<div class="radio radio-info radio-inline">
												<form:radiobutton path="replacementRequired"
													id="replacementRequiredNo" value="false" checked="" />
												<label for="dispute-no">No</label>
											</div>
										</div>
									</div>
									<div class="row margintop15">
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<spring:message code="disputeItem.Shortshipped.replacedPO" />
										</div>
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<span class="textBlack replacedPo" hidden="true">${disputeItemInquiryForm.purchaseOrderNumber}</span>
										</div>
									</div>
								</div>
								<!-- Mobile for Short Shipped End -->
							</div>
						</div>

						<div class="help-accordian  panel disputeItemInvoice ">
							<div class="help-accordian-header">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapse3" class="ref_no toggle-link panel-collapsed"><span
									class="glyphicon glyphicon-plus help-accordian-icon"></span><spring:message code="dispute.order.overshipped.header" />
								</a>
							</div>
							<div class="help-accordian-body panel-collapse collapse"
								id="collapse3">
								<div class="hidden-xs">
									<table id=""
										class="table table-bordered table-striped dispute-an-item-table table-disbaled">
										<thead>
											<tr>
												<th class="invoice-dispute-head-chk"></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.product" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.qtyreceived" /><span
													class="star">*</span></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.qtyordered" /></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.lot" /><span class="star">*</span></th>
												<th class="no-sort text-left text-uppercase"><spring:message
														code="dispute.order.overshipped.invoiceNumber" /></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${invoice}" var="entry" varStatus="status">
												<tr>
													<td>
														<div
															class="checkbox checkbox-info invoice-dispute-checkbox">
															<input type="checkbox"
																id="prodCheckOver_desktop_${status.index}"
																index="${status.index}" name="prodCheckOver"
																class="styled prodCheckOver""> <label
																for="acc3-check1"></label>
														</div>
													</td>
													<td class="text-left prodCodeOver"
														id="prodCodeOver_desktop_${status.index}"
														index="${status.index}" name="prodCodeOver"
														data="${entry.product.code }">${entry.product.code }</td>
													<td class="text-left"><input type="text"
														class="form-control invoiceDisputeQtyTxtField prodQtyReceivedOver inputTxtInvoice"
														id="prodQtyReceivedOver_desktop_${status.index}"
														index="${status.index}" name="prodQtyReceivedOver"
														disabled="true">
														<div style="display: none; color: #B41601;"
															class="emptyQtyReceivedOverError invoicedisputeerror"
															id="emptyQtyReceivedOverError_desktop_${status.index}">
															<spring:message
																code="dispute.order.overshipped.qtyReceivedOver.errormessage" />
														</div></td>
													<td class="text-left prodQtyOrderedOver"
														id="prodQtyOrderedOver_desktop_${status.index}"
														index="${status.index}" name="prodQtyOrderedOver"
														data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
														<div>${entry.qty}</div>
														<div>${entry.product.deliveryUnit}(${entry.product.numerator}
															&nbsp; ${entry.product.salesUnit})</div>
													</td>
													<td class="text-left"><input type="text"
														class="form-control invoiceDisputeQtyTxtField lotNumbers inputTxtInvoice"
														id="lotNumbers_desktop_${status.index}"
														index="${status.index}" name="lotNumbers" disabled="true">
														<div style="display: none; color: #B41601;"
															class="emptylotNumbersError invoicedisputeerror"
															id="emptylotNumbersError_desktop_${status.index}">
															<spring:message
																code="dispute.order.overshipped.lotNumbers.errormessage" />
														</div></td>
													<td class="text-left invoiceNumberOver"
														id="invoiceNumberOver_desktop_${status.index}"
														index="${status.index}" name="invoiceNumberOver"
														data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}
													</td>

												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="productCodeValidationError" style="display: none;">
										<div class="registerError">
											<spring:message
												code="dispute.order.overshipped.product.errormessage" />
										</div>
									</div>
								</div>
								<!-- Mobile for Over Shipped Start -->

								<div class="row visible-xs">
									<div class="col-xs-12">
										<table id=""
											class="table table-bordered table-striped dispute-an-item-mobi dispute-an-item-table">
											<tbody>
												<c:forEach items="${invoice}" var="entry" varStatus="status">
													<tr>
														<td class="dispute-chckbox-cell">
															<div
																class="checkbox checkbox-info invoice-dispute-mobichkbox">
																<input type="checkbox"
																	id="prodCheckOver_mobile_${status.index}"
																	index="${status.index}" name="prodCheckOver"
																	class="styled prodCheckOver""> <label
																	for="acc3-mob-check1"></label>
															</div>
														</td>
														<td class="text-left"><a
															href="#acc3-collapse-tab-${status.index}"
															data-toggle="collapse" data-parent="#accordion3"
															class="toggle-link-sub panel-collapsed"> <span
																class="glyphicon glyphicon-plus"></span> <span><spring:message
																		code="dispute.order.overshipped.product" /></span><span
																class="star">*</span>
														</a>
															<div class="text-left prdctCodemb prodCodeOver"
																id="prodCodeOver_mobile_${status.index}"
																index="${status.index}" name="prodCodeOver"
																data="${entry.product.code }">${entry.product.code }</div>
															<div id="acc3-collapse-tab-${status.index}"
																class="panel-collapse collapse sub-inputs">
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.qtyreceived" />
																		<span class="star">*</span>
																	</p>
																	<input type="text"
																		class="form-control invoiceDisputeMobieTxt invoiceDisputeQtyTxtField prodQtyReceivedOver inputTxtInvoice"
																		id="prodQtyReceivedOver_mobile_${status.index}"
																		index="${status.index}" name="prodQtyReceivedOver"
																		disabled="true">
																	<div style="display: none; color: #B41601;"
																		class="emptyQtyReceivedOverError invoicedisputeerror"
																		id="emptyQtyReceivedOverError_mobile_${status.index}">
																		<spring:message
																			code="dispute.order.overshipped.qtyReceivedOver.errormessage" />
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.qtyordered" />
																	</p>
																	<div class="text-left prodQtyOrderedOver"
																		id="prodQtyOrderedOver_mobile_${status.index}"
																		index="${status.index}" name="prodQtyOrderedOver"
																		data="${entry.qty}${entry.product.deliveryUnit}(${entry.product.numerator} &nbsp; ${entry.product.salesUnit})">
																		<div>${entry.qty}</div>
																		<div>${entry.product.deliveryUnit}(${entry.product.numerator}
																			&nbsp; ${entry.product.salesUnit})</div>
																	</div>
																</div>
																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message code="dispute.order.overshipped.lot" />
																		<span class="star">*</span>
																	</p>
																	<input type="text"
																		class="form-control invoiceDisputeMobieTxt invoiceDisputeQtyTxtField lotNumbers inputTxtInvoice"
																		id="lotNumbers_mobile_${status.index}"
																		index="${status.index}" name="lotNumbers"
																		disabled="true">
																	<div style="display: none; color: #B41601;"
																		class="emptylotNumbersError invoicedisputeerror"
																		id="emptylotNumbersError_mobile_${status.index}">
																		<spring:message
																			code="dispute.order.overshipped.lotNumbers.errormessage" />
																	</div>
																</div>

																<div class="dispute-row">
																	<p class="text-uppercase">
																		<spring:message
																			code="dispute.order.overshipped.invoiceNumber" />
																	</p>
																	<div class="text-left invoiceNumberOver"
																		id="invoiceNumberOver_mobile_${status.index}"
																		index="${status.index}" name="invoiceNumberOver"
																		data="${disputeItemInquiryForm.disputeInvoiceNumber}">${disputeItemInquiryForm.disputeInvoiceNumber}
																	</div>
																</div>
															</div></td>
													</tr>

												</c:forEach>
											</tbody>
										</table>
										<div class="productCodeValidationError" style="display: none;">
											<div class="registerError">
												<spring:message
													code="dispute.order.overshipped.product.errormessage" />
											</div>
										</div>
									</div>
								</div>
								<!-- Mobile for Over Shipped End -->
								<div class="dispute-panel">
									<div class="row">
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<spring:message code="disputeItem.OverShipment.shipped" />
										</div>
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<div class="radio radio-info radio-inline">
												<form:radiobutton path="keepProductsShipped"
													id="productShipmentYesDL" value="true" />
												<label for="dispute-yes">Yes</label>
											</div>

											<div class="radio radio-info radio-inline">
												<form:radiobutton path="keepProductsShipped"
													id="productShipmentNoDL" value="false" />
												<label for="dispute-no">No</label>
											</div>
										</div>


									</div>
									<div class="row margintop15">
										<div
											class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style">
											<spring:message code="disputeItem.OverShipment.ponumber" />
										</div>
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
											<form:input path="newPurchaseOrderNumber"
												class="form-control margintop inputTxt" id="poNumber"
												readonly="true" />
										</div>
										<div class="poNumberValidationError error form-label-style"
											style="display: none;">
											<div class="registerError">
												<spring:message
													code="dispute.order.overshipped.newpo.errormessage.question" />
											</div>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop15">
					<input type="button"
						class="primarybtn btn btnclsactive pull-right naSubmitDisputeItem"
						value="Submit Dispute" /> <a href="${invoiceDetailUrl}"
						class="canceltxt pull-right build-ordr-cancel-btn"><spring:message
							code="dispute.order.cancel" /></a>
				</div>
			</div>
		</div>
	</div>
</form:form>

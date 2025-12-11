<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="laCommon"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<spring:message code='signup.email.regex.error' var="emailRegexError" />
	<div class="row jnj-body-padding" id="jnj-body-content">
		<div class="col-lg-12 col-md-12" id="ServicesPage">
			<div id="consignmentIssuePage">
				<input type="hidden" value="${success}" id="isFormSendSuccessful" />

				<div id="breadcrumb" class="breadcrumb">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
				<div class="row">
					<div class="margintop40 content">
						<spring:message code="misc.services.consignmentIssue" />
					</div>
					<div class="laudotoppanel">
						<div class="req-text">
							(<span style="color: #b41601;">*</span>)
							<spring:message code="text.downloadlaudo.required" />
						</div>
					</div>
				</div>
				<div id="successMessage" style="display: none;">
					<laCommon:genericMessage messageCode="misc.indirectPayer.success"
						icon="ok" panelClass="success" />
				</div>
				<div id="errorMessage" style="display: none;">
					<laCommon:genericMessage messageCode="misc.indirectPayer.failure"
						icon="ban-circle" panelClass="danger" />
				</div>
				<div class="row table-padding paddingbottom25px paddingleft10px">
					<div class="col-lg-12 col-md-12">
						<div class="row paddingbottom25px">
							<div class="col-lg-12 col-md-12 col-xs-12">

								<spring:message code="misc.services.consignmentIssueDesc" />
								<a class='privacypolicypopup_hn jnj-blue' title='Privacy Policy'
									href='#'><spring:message
										code="text.misc.privacy.policy" /></a><spring:message code="misc.services.consignmentIssueDesc2" />

							</div>
						</div>
						<input type="hidden" value="${noBlankError}"
							id="errorMessageConsignment" />
						<form:form id="consignmentIssueValidate" action="consignmentIssue"
							method="post" name="consignmentIssueValidate"
							modelAttribute="jnjConsignmentIssueForm">
							<div class="consignmentIssueGlobalError registerError"
								style="display: none;"></div>
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
									<label for="CName"> <spring:message
											code="misc.services.customerName" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<form:input path="customerName" type="text" id="CName"
										name="CName" class="required form-control validationevent"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div
										class="registerError errorWidthForms errorconsignment errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
									<label for="hospital"> <spring:message
											code="misc.services.hospital" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<form:input path="hospital" type="text" id="hospital"
										name="hospital" class="required form-control validationevent"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="ContactName"> <spring:message
											code="misc.services.contactName" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="contactName" type="text" id="ContactName"
										name="ContactName" class="required form-control validationevent"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="Cemail"> <spring:message
											code="misc.services.contactEmail" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="contactEmail" type="text" id="Cemail"
										name="Cemail" class="required form-control email"
										data-msg-required="${noBlankError}"
										data-msg-email="${emailRegexError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="Cphone"> <spring:message
											code="misc.services.contactPhone" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="contactPhone" type="text" id="Cphone"
										name="Cphone" class="required form-control phone numeric"
										maxlength="15" data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="soldTo"> <spring:message
											code="misc.services.soldTo" /> <spring:message
											code="misc.services.colon" /><sup class="star">*</sup></label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="soldTo" type="text" id="soldto" name="soldto"
										class="required form-control validationevent"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="shipto">
									    <spring:message code="misc.services.address" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="shipTo" type="text" id="shipto" name="shipto"
										class="required form-control addressvalidation"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>

                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="residentialQuarter">
									    <spring:message code="misc.services.residentialQuarter" />
									    <spring:message code="misc.services.colon" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="residentialQuarter" type="text" id="residentialQuarter"
									    name="residentialQuarter"
										class="required form-control validationevent" data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>

                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="city">
									    <spring:message code="misc.services.city" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="city" type="text" id="city"
									    name="city" class="required form-control validationevent" data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>

                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="state">
									    <spring:message code="misc.services.state" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
                                    <select class="required form-control" id="state" name="state" path="state"
                                        data-msg-required="${noBlankError}">
									    <option value="">
									        <spring:message code="misc.services.selectOne" />
									    </option>
									    <c:forEach items="${currentSiteCountryRegionList}" var="countryRegionItem" >
										    <option value="${countryRegionItem.name}"> ${countryRegionItem.name}</option>
									    </c:forEach>
								    </select>
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>



                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="zipCode">
									    <spring:message code="misc.services.zip"/>
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="zipCode" type="text" id="zipCode"
									    name="zipCode" class="required form-control idtypevalidation" data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>

							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="datepickerform0">
									    <spring:message code="misc.services.date" />
									    <spring:message code="misc.services.colon" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 select-date-box input-group">
									<form:input path="date" id="datepickerform0"
										class="required date-picker form-control date-picker-body"
										type="text" data-msg-required="${noBlankError}"
										placeholder="${selectDatePlacehodler}" />
									<label for="datepickerform0" class="input-group-addon btn">
									    <i class="bi bi-calendar3"></i>
									</label>
									<input type="hidden" id="currentLocale" style="display: none;"
									    value="${currentLocale}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>




							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="POnum">
									    <spring:message code="misc.services.poNumber" />
									    <spring:message code="misc.services.colon" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="poNumber" type="text" id="POnum" name="POnum"
										class="required form-control idtypevalidation"
										data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="billdet">
									    <spring:message code="misc.services.replenishmentOrFillUpDoc" /></label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="replenishmentOrFillUpDoc" type="text"
										id="billdet" name="billdet" class="form-control validationevent" />
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="Nfe"> <spring:message
											code="misc.services.replenishmentOrFillUpNFE" /></label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="replenishmentOrFillUpNFE" type="text"
										id="Nfe" class="form-control validationevent" name="Nfe" />
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="patient">
									    <spring:message code="misc.services.patient" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="patient" type="text" id="patient"
										name="patient" class="required form-control validationevent"
										data-msg-required="${noBlankError}"/>
								</div>
								<div class="cell">
                                    <div class="registerError errorWidthForms errorconsignment"></div>
                            	</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="doctor">
									    <spring:message	code="misc.services.doctor" />
									    <sup class="star">*</sup>
								    </label>
								</div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:input path="doctor" type="text" id="doctor" name="doctor"
										class="required form-control validationevent" data-msg-required="${noBlankError}" />
								</div>
								<div class="cell">
								    <div class="registerError errorWidthForms errorconsignment"></div>
                               	</div>
							</div>

                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="healthPlan">
									    <spring:message	code="misc.services.healthPlan" />
									    <spring:message code="misc.services.colon" />
								    </label>
								</div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<form:input path="healthPlan" type="text" id="healthPlan" name="healthPlan"
										class="form-control validationevent"/>
								</div>
							</div>

                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="datepickerform1">
									    <spring:message code="misc.services.surgery.date" />
									    <spring:message code="misc.services.colon" />
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 select-date-box input-group">
									<form:input path="surgeryDate" id="datepickerform1"
										class="required date-picker form-control date-picker-body"
										type="text" data-msg-required="${noBlankError}"
										placeholder="${selectDatePlacehodler}" />
									<label for="datepickerform1" class="input-group-addon btn">
									    <i class="bi bi-calendar3"></i>
									</label>
									<input type="hidden" id="currentLocale" style="display: none;"
									    value="${currentLocale}" />
								</div>
								<div class="cell">
									<div class="registerError errorWidthForms errorconsignment"></div>
								</div>
							</div>



                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="billingOrReplacement">
									    <spring:message code="misc.services.billing"/>/
									    <spring:message code="misc.services.replacement"/>
									    <spring:message code="misc.services.colon"/>
									    <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">

                                    <select class="required form-control" id="billingOrReplacement" name="billingOrReplacement"
                                        data-msg-required="${noBlankError}">
                                        <option value="">
									        <spring:message code="misc.services.selectOne"/>
									    </option>
                                        <option value="<spring:message code="misc.services.billing"/>">
										    <spring:message code="misc.services.billing"/>
										</option>
										<option value="<spring:message code="misc.services.replacement"/>">
										    <spring:message code="misc.services.replacement"/>
										</option>
										<option value="<spring:message code="misc.services.billingAndReplacement"/>">
										    <spring:message code="misc.services.billingAndReplacement"/>
										</option>
								    </select>
								</div>
                                <div class="registerError errorWidthForms errorconsignment"></div>
							</div>




                            <div class="row margintop20px">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
									<label for="observation">
									    <spring:message	code="misc.services.observation" />
									    <spring:message code="misc.services.colon" />
								    </label>
								</div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
									<form:textarea path="observation" type="text" id="observation" name="observation"
										class="form-control validationevent" rows="10" cols="50"/>
								</div>
							</div>




							<div id="group_0">
								<div class="row margintop20px">
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<table id="consignmentTable"
											class="table table-bordered table-striped">
											<thead>
												<tr>
													<th class="no-sort hcolumn1">
													    <spring:message code="misc.services.item" />*
													</th>
													<th class="no-sort hcolumn2">
													    <spring:message code="misc.services.qty" />*
													</th>
													<th class="no-sort hcolumn3">
													    <spring:message code="misc.services.uom" />*
													</th>
													<th class="no-sort hcolumn4">
													    <spring:message code="misc.services.batchNumber" />*
													</th>
												</tr>
											</thead>
											<tbody class="consignmentIssueRow even">
												<tr>
													<td>
														<div class="column1">
															<form:input path="item[0]" type="text"
																class="form-control autowidth validationevent"/>
														</div>
													</td>
													<td>
														<div class="column2">
															<form:input path="qty[0]" type="text"
																class="form-control autowidth numeric"/>
														</div>
													</td>
													<td>
														<div class="column3">
															<form:input path="uom[0]" type="text"
																class="form-control autowidth validationevent" />
														</div>
													</td>
													<td>
														<div class="column4">
															<form:input path="batchNumber[0]" type="text"
																class="form-control autowidth idtypevalidation"/>
														</div>
													</td>
												</tr>

											</tbody>
										</table>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<div class="inline-block">
											<a href="#" class="table-cell addMoreLines">
											    <span class="table-cell">
											        <spring:message code="misc.services.addMoreLines" />
											    </span>
											</a>
										</div>
										<div class="inline-block removeTableData">
											<a href="#" class="table-cell removeLineslatam"
												id="removeLines_0">
												<span class="table-cell">
												    <spring:message code="misc.services.removeLines" />
												</span>
											</a>
										</div>
									</div>
								</div>
							</div>
							<div class="sectionBlock" id="itemerrorpanel"
								style="display: none">
								<div class="cell">
									<div class="registerError registerErrorLast error">
										<p>
											<spring:message code="misc.services.consignmentRegisterError" />
										</p>
									</div>
								</div>
							</div>
							<div class="sectionBlock" id="itemerrorpanel2"
								style="display: none">
								<div class="cell">
									<div class="registerError registerErrorLast error">
										<p>
											<spring:message code="misc.services.consignmentRegisterError2" />
										</p>
									</div>
								</div>
							</div>
							<div class="row margintop20px">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div
										class="checkbox checkbox-info selectchkbox content inline-element"
										style="border-bottom: 1px solid #ccc;">
										<input id="consignmentIssue-agree" class="styled"
											type="checkbox">
											<label for="consignmentIssue-agree">
											<spring:message code="services.page.contactus.help.statement" />
										</label>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<div class="row margintop20px">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<input type="button" class="btn btnclsnormal addindirectbtns"
							id="consignment_back" value="<spring:message code='misc.indirectPayer.back' />" /> <input type="button"
							id="consignmentIssueSend"
							class="primarybtn btn btnclsactive addindirectbtns pull-right"
							value="<spring:message code='misc.indirectCustomer.send' />" disabled />
					</div>
				</div>

				<div id="nanError" style="display: none;">
					<div class="cellError">
						<div class="registerError registerErrorLast error">
							<p>
								<spring:message code="misc.services.invalidNumberError" />
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</templateLa:page>
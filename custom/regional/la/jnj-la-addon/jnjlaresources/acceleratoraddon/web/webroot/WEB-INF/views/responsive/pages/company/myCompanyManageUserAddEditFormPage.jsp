<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="resource" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/company"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">

    <input type="hidden" id="noRoleMessage" value='<spring:message code="usermanagement.required.roles"/>'>
    <input type="hidden" id="noAccountMessage" value='<spring:message code="usermanagement.required.accounts"/>'>
    <input type="hidden" value="" id="hddnTempAccountList1" autocomplete="off" />

	<div class="row jnj-body-padding" id="jnj-body-content">
		<div class="col-lg-12 col-md-12 mobile-no-pad">
			<div id="emeo-usrmanagement-createusr">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content">
						<spring:message code='userSearch.breadCrumb'/>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
						<div class="float-right-to-none floating-global-btns">
							<c:url value="/resources/usermanagement" var="searchPage"/>
							<a href="${searchPage}" class="canceltxt build-ordr-cancel-btn inline-btns">
								<spring:message code='userSearch.button.cancel'/>
							</a>
							<a href="#">
								<button type="button" class="btn btnclsactive buttonMargin" id="laCreateProfileButton">
									<spring:message code='userSearch.button.submit'/>
								</button>
							</a>
							<a href="${searchPage}" type="button" class="btn btnclsnormal pull-right">
								<spring:message code='user.link.backToUserSearch'/>
							</a>
						</div>
					</div>
				</div>

				<div>
					<div class="col-lg-12 col-md-12 boxshadow whiteBg">
						<input type="hidden" id="accountsSelectedList" class="accountsSelectedList" value=""/>
						<input type="hidden" value="" id="hddnTempAccountNameList"/>
						<input type="hidden" value="" id="hddnOriginalTempAccountNameList"/>
						<c:url value="/resources/usermanagement/createUser" var="createUser"/>
						<form:form method="post" action="${createUser}" id="createNewProfileForm" modelAttribute="jnjGTB2BCustomerForm">
							<div class="row subcontent1">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 pane-title">
									<spring:message code='text.company.manageUser.button.create'/>
								</div>
							</div>

							<div class="row buttonWrapperWithBG" id="select-profile">
								<div class="col-lg-12 col-md-12 col-sm-12 ">
									<span id="select-profile-txt" class="labelText usm-create-subHead">
										<label><spring:message code='user.label.sectors'/></label>
									</span>
									<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox id="mdd" name="mdd" value="option1" path="mdd" class="profileSector checkBoxBtn sectorBox"/>
										<div class="checkLabelHolder">
											<label for="mdd" class="checkLabel">
												<spring:message code='user.form.sector.mdd'/>
											</label>
										</div>
									</div>
									<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox id="consumer" name="consumer" value="option1" path="consumer" class="profileSector checkBoxBtn sectorBox"/>
										<div class="checkLabelHolder">
											<label for="consumer" class="checkLabel">
												<spring:message code='user.form.sector.consumer'/>
											</label>
										</div>
									</div>
									<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox id="pharma" name="pharma" path="pharma" value="option1" class="profileSector checkBoxBtn sectorBox"/>
										<div class="checkLabelHolder">
											<label for="pharma" class="checkLabel">
												<spring:message code='user.form.sector.pharma'/>
											</label>
										</div>
									</div>
									<div class="" id="sectorMsg"></div>
								</div>

							</div>

							<div class="margin-top-gap" id="um-create-form-holder">
								<div class="row um-form-row um-element">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="fisrtName" class="getErrorMessage" data="<spring:message code='usermanagement.required.firstname'/>">
												<spring:message code='user.form.label.firstname'/>
												<span class="redStar">*</span>
											</label>
											<form:input type="text" name="fisrtName" id="fisrtName" class="required medium form-control" path="firstName"/>
											<div class="registerError"></div>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="lastName" class="getErrorMessage" data="<spring:message code='usermanagement.required.lastname'/>">
												<spring:message code='user.form.label.lastname'/>
												<span class="redStar">*</span>
											</label>
											<form:input type="text" name="lastName" id="lastName" class="required medium form-control" path="lastName"/>
											<div class="registerError"></div>
										</div>
									</div>
								</div>

								<div class="row um-form-row um-element">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="email" class="getErrorMessage" data="<spring:message code='usermanagement.required.emailLogin'/>">
												<spring:message code='user.form.label.emailLogin'/>
												<span class="redStar">*</span>
											</label>
											<input type="hidden" id="hiddenMsgValue" value="<spring:message code='usermanagement.alreadyExists.emailLogin'/>"/>
											<form:input type="email" name="email" id="email" class="required medium form-control" path="email" data-msg-email="The email address you entered is invalid. Please try again."/>
											<div class="registerError error"></div>
											<div class="duplicateError error"></div>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="phone" class="usm-phone-label getErrorMessage" data="<spring:message code='usermanagement.required.phone'/>">
												<spring:message code='user.form.label.PhoneNumber'/>
												<span class="redStar">*</span>
											</label>
											<div class="table-cls">
												<div class="table-row">
													<div class="usm-phone-code">
														<label for="phonePrefix" class="widthAuto">
															<form:input id="phonePrefix" class="form-control phone-only" name="phonePrefix" path="phoneNumberPrefix"/>
														</label>
													</div>
													<div class="usm-phone-number">
														<form:input id="phone" type="text" name="phone" class="required phoneFormat phoneFormateUS  form-control numbersonly removeSplChar" path="phone"/>
														<div class="registerError"></div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="row um-form-row um-element">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="language" class="getErrorMessage">
												<spring:message code='user.form.label.language'/>
												<span class="redStar">*</span>
											</label>
											<div class="language registerError"></div>
											<form:select path="language" id="language" data-width="100%" class="required selectpicker">
												<c:forEach items="${languages}" var="languages">
													<option value="${languages.isocode}">${languages.name}</option>
												</c:forEach>
											</form:select>
											<div class="registerError"></div>
										</div>
									</div>

									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="supName" class="getErrorMessage" data="<spring:message code='usermanagement.required.supname'/>">
												<spring:message code='user.form.label.supervisionName'/></label>
											<form:input type="text" name="supName" id="supName" class="medium form-control alpha-only valid" path="supervisorName"/>
											<div class="registerError"></div>
										</div>

									</div>
								</div>

								<div class="row um-form-row um-element">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="supervisorPhone" class="usm-phone-label getErrorMessage" data="<spring:message code='usermanagement.required.supphone'/>">
												<spring:message code='user.form.label.supervisionPhone'/></label>
											<div class="usm-phone-code">
												<label for="supPhonePrefix" class="widthAuto">
													<form:input id="supPhonePrefix" class="form-control phone-only" name="supPhonePrefix" path="supervisorPhonePrefix"/>
												</label>
											</div>
											<div class="usm-phone-number">
												<form:input id="supervisorPhone" type="text" name="supervisorPhone" class="phoneFormat phoneFormateUS form-control numbersonly removeSplChar" path="supervisorPhone"/>
												<div class="registerError"></div>
											</div>

										</div>

									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
										<div class="form-group">
											<label for="supervisorEmail" class="getErrorMessage" data="<spring:message code='usermanagement.required.supemail'/>">
												<spring:message code='user.form.label.supervisionEmail'/></label>
											<form:input type="email" name="supervisorEmail" id="supervisorEmail" class="form-control big" path="supervisorEmail" data-msg-email="The email address you entered is invalid. Please try again."/>
											<div class="registerError"></div>
										</div>

									</div>
								</div>

							</div>
							<div class="row um-element" id="um-roles-accounts">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel um-element-col">
									<div class="roleTxt">
										<div class="registerError error"></div>
										<label for="roles" class="getErrorMessage">
											<spring:message code='user.form.label.rolePermission'/>
											<span class="redStar">*</span>
										</label>
									</div>

									<div class="checkbox checkbox-info checkboxmargin">
										<form:checkboxes items="${rolesMap}" id="roles" path="roles" element="li type=none" name="roles" class="styled userRoleList" data-msg-required="Please select at least one Role!"/>
									</div>
									<div class="registerError" id="roleMsg"></div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel">
									<div class="row">
										<div class="usm-create-subHead col-lg-4" id="account-header">
											<label for="roles" class="getErrorMessage">
												<spring:message code='user.form.accounts'/>
												<span class="redStar">*</span>
											</label>
										</div>
										<div class="col-lg-8">
											<a class="btn btnclsactive float-right-to-none" href="#" id="selectAccountUM">
												<spring:message code='user.form.addAccount'/>
											</a>
										</div>
									</div>
									<form:input type="hidden" id="hddnAccountsString" path="groups"/>
									<input type="hidden" value="${loggedUserAccountList}" id="hddnloggedUserAccountList"/>
									<company:manageMultipleAccounts/>
									<input type="hidden" value="" id="hddnloggedUserAccountList"/>
									<div class="registerError" id="accountMsg"></div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-12" id="usm-notification">
			<div class="panel-group">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code='createProfile.message'/>
					</div>
				</div>
			</div>
		</div>
		<div id="add-account-popup-holder"></div>
	</div>
</templateLa:page>

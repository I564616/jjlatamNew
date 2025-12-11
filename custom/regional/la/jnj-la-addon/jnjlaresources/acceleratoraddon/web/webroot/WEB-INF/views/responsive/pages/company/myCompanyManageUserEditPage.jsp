<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/company"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="resource" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<spring:url value="/my-company/organization-management/manage-users/create" var="manageUsersUrl" />

<templateLa:page pageTitle="${pageTitle}">
    <input type="hidden" id="noRoleMessage" value='<spring:message code="usermanagement.required.roles"/>'>
    <input type="hidden" id="noAccountMessage" value='<spring:message code="usermanagement.required.accounts"/>'>
    <input id="editUserSpecificTerr" value="${invalidTerritory}" type="hidden" />
    <input type="hidden" value="" id="hddnTempAccountList1" autocomplete="off" />

    <div class="col-lg-12 col-md-12 mobile-no-pad">
        <div id="emeo-usrmanagement-createusr">
            <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
            <div class="row">
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content">
                    <spring:message code="userSearch.breadCrumb" />
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
                    <div class="float-right-to-none floating-global-btns">
                        <c:url value="/resources/usermanagement" var="searchPage" />
                        <spring:url value="/resources/usermanagement/create" var="createUserProfile" />
                        <a href="${createUserProfile}" type="button" class="btn btnclsactive buttonMargin" id="createUserProfileUserMangement">
                            <spring:message code="text.company.manageUser.button.create" />
                        </a>
                        <a href="${searchPage}" type="button" class="btn btnclsnormal pull-right">
                            <spring:message code='user.link.backToUserSearch' />
                        </a>
                    </div>
                </div>
            </div>

            <input type="hidden" value="${hiddenAccountNamesList}" id="hddnTempAccountNameList" />
            <input type="hidden" value="${hiddenAccountNamesList}" id="hddnOriginalTempAccountNameList" />
            <input type="hidden" value="true" id="valueSetFlag" />
            <c:url value="/resources/usermanagement/editUser" var="editUser" />

            <p class="registerError errorWidthSecQ" id="editUserProfileFormError" style="margin: 0; display: none;">
                <label class="error">
                    <spring:message code="password.forgotPassword.enterPassword" />
                </label>
            </p>
            <div id="myTrainingResources">
                <form:form method="post" action="${editUser}" id="editUserProfileForm" modelAttribute="jnjGTB2BCustomerForm" class="row">

                    <div class="row myTrainingResources" id="myTrainingResourcesSuccess">
                        <div class="col-md-12">
                            <div class="panel-group">
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <span class="glyphicon glyphicon-ok"></span><span class="training-content"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row myTrainingResources" id="myTrainingResourcesFailure">
                        <div class="col-md-12">
                            <div class="panel-group">
                                <div class="panel panel-danger">
                                    <div class="panel-heading">
                                        <span class="glyphicon glyphicon-ban-circle"></span><span class="training-content"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-12 col-md-12 boxshadow whiteBg row">
                        <div class="row subcontent1">
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 pane-title">
                                <spring:message code='editProfile.breadCrumb' />
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <div class="float-right-to-none status-dropdown">
                                    <label for="status" id="status-txt">
                                        <spring:message code='user.form.label.status' />
                                    </label>
                                    <c:if test="${selfTier1 || selfTier2 || isStatusDisabled}">
                                        <c:set var="statusEnabled" value="true" />
                                    </c:if>
                                    <form:select id="status-dropdown status" class="selectpicker" path="status" disabled="${statusEnabled}">
                                        <c:forEach items="${statuses}" var="status">
                                            <option value="${status.key}" <c:if test="${fn:containsIgnoreCase(status.value, 'pending')
													|| (fn:containsIgnoreCase(status.value, 'Rejected') && isTier1User)}">disabled="disabled"</c:if>
                                                <c:if test="${(fn:containsIgnoreCase(status.value, jnjGTB2BCustomerForm.status))}"> selected="selected"</c:if>> ${status.value}
                                            </option>
                                        </c:forEach>
                                    </form:select>
                                    <form:input type="hidden" id="existingStatus" path="existingStatus" />

                                </div>
                            </div>
                        </div>
                        <div class="row password-info">
                            <div class="col-lg-4 col-md-5 col-sm-4 col-xs-12 formPanel pw-field passInfo-mobile">
                                <strong><spring:message code='user.form.label.passwordChanged' /></strong>
                                <span>: ${jnjGTB2BCustomerForm.passwordChangeDate}</span>
                            </div>
                            <div class="col-lg-4 col-md-5 col-sm-4 col-xs-12 pw-field txt-right-to-left passInfo-mobile">
                                <strong><spring:message code='user.form.label.passwordExpires' /></strong>
                                <span>: ${jnjGTB2BCustomerForm.passwordExpiryDate}</span>
                            </div>
                            <div class="col-lg-4 col-md-2 col-sm-4 col-xs-12">
                                <a href="javascript:;" class="btn btnclsactive secondarybtn float-right-to-none" type="button" id="resetPasswordButton">
                                    <spring:message code='user.form.link.resetPassword' />
                                </a>

                            </div>
                        </div>

                        <div class="row" id="select-profile">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <span id="select-profile-txt" class="usm-create-subHead"><label><spring:message code='user.label.sectors' /></label></span>
                                <div class="checkbox checkbox-info checkbox-inline">
                                    <c:if test="${selfTier2 || selfTier1|| !isTier2User}">
                                        <c:set var="sectorEnableMent" value="true" />
                                    </c:if>
                                    <form:checkbox name="medical[]" id="mdd" value="option1" class="left profileSector checkBoxBtn" path="mdd" disabled="${sectorEnableMent}" />
                                    <div class="checkLabelHolder">
                                        <label for=mdd class="getErrorMessage checkLabel" data="<spring:message code='usermanagement.required.sector'/>">
                                            <spring:message code='user.form.sector.mdd' />
                                        </label>
                                    </div>
                                </div>
                                <div class="checkbox checkbox-info checkbox-inline">
                                    <form:checkbox name="medical[]" id="consumer" value="option1" class="left profileSector checkBoxBtn" path="consumer" disabled="${sectorEnableMent}" />
                                    <div class="checkLabelHolder">
                                        <label for="consumer" class="checkLabel">
                                            <spring:message code='user.form.sector.consumer' />
                                        </label>
                                    </div>
                                </div>
                                <div class="checkbox checkbox-info checkbox-inline">
                                    <form:checkbox name="medical[]" id="pharma" value="option1" class="left profileSector checkBoxBtn" path="pharma" disabled="${sectorEnableMent}" />
                                    <div class="checkLabelHolder">
                                        <label for="pharma" class="checkLabel">
                                            <spring:message code='user.form.sector.pharma' />
                                        </label>
                                    </div>
                                </div>
                                <div class="" id="sectorMsg2"></div>
                            </div>
                        </div>

                        <div class="" id="um-create-form-holder">

                            <div class="row um-form-row">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <form:input type="hidden" id="userId" name="userId" path="uid" />
                                        <label for="fisrtName" class="getErrorMessage" data="<spring:message code='usermanagement.required.firstname'/>">
                                            <spring:message code='user.form.label.firstname' /><span class="redStar">*</span></label>
                                        <form:input type="text" name="fisrtName" id="fisrtName" disabled="${disablefield}" class="required medium form-control" path="firstName" />
                                        <div class="registerError"></div>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="lastName" class="getErrorMessage" data="<spring:message code='usermanagement.required.lastname'/>">
                                            <spring:message code='user.form.label.lastname' /><span class="redStar">*</span></label>
                                        <form:input type="text" name="lastName" id="lastName" disabled="${disablefield}" class="required medium form-control" path="lastName" />
                                        <div class="registerError"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="row um-form-row">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="emailLogin" class="getErrorMessage" data="<spring:message code='usermanagement.required.emailLogin'/>">
                                            <spring:message code='user.form.label.emailLogin' /><span class="redStar">*</span></label>
                                        <form:input type="email" name="emailLogin" id="emailLogin" disabled="true" class="required big form-control" path="email" />
                                        <form:input type="hidden" path="email" />
                                        <input type="hidden" id="hiddenMsgValue" value="<spring:message code='usermanagement.alreadyExists.emailLogin'/>" />
                                        <input type="hidden" id="existingEmail" value="${existingEmail}" />
                                        <div class="registerError"></div>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="phone" class="usm-phone-label getErrorMessage" data="<spring:message code='usermanagement.required.phone'/>">
                                            <spring:message code='user.form.label.PhoneNumber' /><span class="redStar">*</span></label>
                                        <div class="usm-phone-code">
                                            <label for="phonePrefix" class="widthAuto">
                                                <form:input type="text" id="phonePrefix" class="form-control phone-only" name="phonePrefix" path="phoneNumberPrefix" />
                                            </label>
                                        </div>
                                        <div class="usm-phone-number">
                                            <form:input id="phone" disabled="${disablefield}" type="text" name="phone" class="required phoneFormat phoneFormateUS form-control numbersonly removeSplChar" path="phone" />
                                            <div class="registerError"></div>
                                        </div>

                                    </div>
                                </div>
                            </div>

                            <div class="row um-form-row">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="language" class="getErrorMessage">
                                            <spring:message code='user.form.label.language' /><span class="redStar">*</span></label>
                                        <form:select path="language" id="language" class="selectpicker required" data-width="100%">
                                            <c:forEach items="${languages}" var="languages">
                                                <option value="${languages.isocode}" <c:if test="${jnjGTB2BCustomerForm.language eq languages.isocode}"> selected="selected"</c:if>>${languages.name}
                                                </option>
                                            </c:forEach>
                                        </form:select>
                                        <div class="registerError error"></div>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="supName" class="getErrorMessage" data="<spring:message code='usermanagement.required.supname'/>">
                                            <spring:message code='user.form.label.supervisionName' />
                                        </label>
                                        <form:input type="text" name="supName" id="supName" disabled="${disablefield}" class="medium form-control alpha-only" path="supervisorName" />
                                        <div class="registerError"></div>
                                    </div>

                                </div>
                            </div>

                            <div class="row um-form-row">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="supervisorPhone" class="getErrorMessage usm-phone-label" data="<spring:message code='usermanagement.required.supphone'/>">
                                            <spring:message code='user.form.label.supervisionPhone' />
                                        </label>
                                        <div class="usm-phone-code">
                                            <label for="supPhonePrefix" class="widthAuto">
                                                <form:input disabled="${disablefield}" type="text" id="supPhonePrefix" class="form-control phone-only" name="supPhonePrefix" path="supervisorPhonePrefix" />
                                            </label>

                                        </div>
                                        <div class="usm-phone-number">
                                            <form:input id="supervisorPhone" type="text" name="supervisorPhone" disabled="${disablefield}" class="phoneFormat phoneFormateUS form-control numbersonly removeSplChar" path="supervisorPhone" />
                                            <div class="registerError"></div>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
                                    <div class="form-group">
                                        <label for="supEmail" class="getErrorMessage" data="<spring:message code='usermanagement.required.supemail'/>">
                                            <spring:message code='user.form.label.supervisionEmail' />
                                        </label>
                                        <form:input type="email" name="supEmail" id="supEmail" disabled="${disablefield}" class="big form-control" path="supervisorEmail" data-msg-email="The email address you entered is invalid. Please try again." />
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
                                        <spring:message code='user.form.label.rolePermission' /> <span class="redStar">*</span></label>
                                </div>

                                <div class="checkbox checkbox-info checkboxmargin">
                                    <form:checkboxes items="${rolesMap}" id="roles" path="roles" element="li type=none" name="roles" class="styled userRoleList" data-msg-required="Please select at least one Role!" />
                                </div>
                                <div class="registerError" id="roleMsg"></div>
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel um-element-col">
                                <div class="row">
                                    <div class="usm-create-subHead col-lg-4" id="account-header">
                                        <label for="accounts" class="getErrorMessage" data="<spring:message code='user.form.accounts'/>">
                                            <spring:message code='user.form.accounts' /> <span class="redStar">*</span>
                                        </label>
                                    </div>
                                    <div class="col-lg-8">
                                        <a class="btn btnclsactive secondarybtn float-right-to-none" href="#" id="selectAccountEditUM">
                                            <spring:message code='user.form.addAccount' />
                                        </a>
                                    </div>
                                    <div class="registerError" id="accountMsg"></div>
                                </div>
                                <form:input type="hidden" id="hddnAccountsString" path="groups" />
                                <input type="hidden" value="${loggedUserAccountList}" id="hddnloggedUserAccountList" />
                                <company:manageMultipleAccountsForEdit />
                                <input type="hidden" id="preselectedAccounts" value="${selectedAccountsString}" />
                                <form:input type="hidden" id="hddnAccountsStringUM" path="groups" />
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
            <div class="row">
                <div class="col-md-12" id="update-cancel">
                    <a href="${searchPage}" class="canceltxt build-ordr-cancel-btn">
                        <spring:message code='userSearch.button.cancel' />
                    </a>
                    <button class="btn btnclsactive buttonMargin laUpdateProfileButton" id="updateProfileButtonTop">
                        <spring:message code='editUser.update.button' />
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div id="add-account-popup-holder"></div>
</templateLa:page>
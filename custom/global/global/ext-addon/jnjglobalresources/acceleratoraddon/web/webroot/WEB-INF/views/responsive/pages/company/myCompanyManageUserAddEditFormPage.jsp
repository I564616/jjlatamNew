<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="resource"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="company"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/company"%>
<template:page pageTitle="${pageTitle}">

	<div class="row jnj-body-padding" id="jnj-body-content">
		<div class="col-lg-12 col-md-12 mobile-no-pad">
			<div id="emeo-usrmanagement-createusr">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				<div class="row">
					<div
						class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content"><spring:message code='userSearch.breadCrumb'/></div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
						<div class="float-right-to-none floating-global-btns">
							<c:url value="/resources/usermanagement" var="searchPage" />
							<a href="${searchPage}" class="canceltxt build-ordr-cancel-btn inline-btns"><spring:message code='userSearch.button.cancel'/></a>
							<a href="#"><button type="button"
									class="btn btnclsactive buttonMargin" id="createProfileButton"><spring:message code='userSearch.button.submit'/></button></a>
							<a href="${searchPage}" type="button"
								class="btn btnclsnormal pull-right"><messageLabel:message
									messageCode='user.link.backToUserSearch' /></a>

						</div>
					</div>
				</div>

				<div class="">
					<div class="col-lg-12 col-md-12 boxshadow whiteBg">
						<input type="hidden" id="accountsSelectedList"
							class="accountsSelectedList" value="" /> <input type="hidden"
							value="" id="hddnTempAccountNameList" /> <input type="hidden"
							value="" id="hddnOriginalTempAccountNameList" />
						<c:url value="/resources/usermanagement/createUser"
							var="createUser" />
						<form:form method="post" action="${createUser}"
							id="createNewProfileForm" commandName="jnjGTB2BCustomerForm">
							<div class="row subcontent1">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 pane-title"><spring:message code='text.company.manageUser.button.create'/></div>
							</div>
							
							<div class="row buttonWrapperWithBG" id="select-profile">
									<div class="col-lg-12 col-md-12 col-sm-12 " >
										<span id="select-profile-txt" class="labelText usm-create-subHead"><label><spring:message code='user.label.sectors' /><span
									class="redStar">*</span></label></span>
										<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox  id="mdd" name="mdd" value="option1"
														path="mdd" class="profileSector checkBoxBtn sectorBox"  />
											<div class="checkLabelHolder"><label for="mdd" class="checkLabel"><spring:message
															code='user.form.sector.mdd' /></label></div>
										</div>
										<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox id="consumer" name="consumer" value="option1" path="consumer"
														class="profileSector checkBoxBtn sectorBox" />
											<div class="checkLabelHolder"><label for="consumer" class="checkLabel"><messageLabel:message
															messageCode='user.form.sector.consumer' /></label></div>
										</div>
										<div class="checkbox checkbox-info checkbox-inline">
										<form:checkbox id="pharma" name="pharma" path="pharma" value="option1"
														class="profileSector checkBoxBtn sectorBox" />
											<div class="checkLabelHolder"><label for="pharma" class="checkLabel"><spring:message
															code='user.form.sector.pharma' /></label></div>
										</div>
										<div class="" id="sectorMsg"></div>
									</div>	
													
							</div>	
							
									

							<div class="margin-top-gap" id="um-create-form-holder">
							<div class="row um-form-row um-element">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="fisrtName" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.firstname'/>"><messageLabel:message
												messageCode='user.form.label.firstname' /><span
											class="redStar">*</span></label>
										<form:input type="text" name="fisrtName" id="fisrtName"
											class="required medium form-control" path="firstName" />
										<div class="registerError"></div>
									</div>

								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="lastName" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.lastname'/>"><messageLabel:message
												messageCode='user.form.label.lastname' /><span
											class="redStar">*</span></label>
										<form:input type="text" name="lastName" id="lastName"
											class="required medium form-control" path="lastName" />
										<div class="registerError"></div>
									</div>

								</div>
							</div>
							
							<div class="row um-form-row um-element">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="email" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.emailLogin'/>"><messageLabel:message
												messageCode='user.form.label.emailLogin' /><span
											class="redStar">*</span></label> <input type="hidden"
											id="hiddenMsgValue"
											value="<spring:message code='usermanagement.alreadyExists.emailLogin'/>" />
										<form:input type="email" name="email" id="email"
											class="required medium form-control" path="email"
											data-msg-email="The email address you entered is invalid. Please try again." />
										<div class="registerError error"></div> 
										<div class="duplicateError error"></div>
									</div>

								</div>

								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="phone" class="usm-phone-label getErrorMessage" data="<spring:message code='usermanagement.required.phone'/>">
										<spring:message code='user.form.label.PhoneNumber' /><span class="redStar">*</span></label>
										
										<div class="table-cls">
		              					  <div class="table-row">	
												<div class="usm-phone-code">
													<label for="phonePrefix" class="widthAuto">
													<form:input id="phonePrefix" class="form-control phone-only" value="+1"
														name="phonePrefix" path="phoneNumberPrefix" />
													</label>	
												</div>
												<div class="usm-phone-number">
													<form:input id="phone" type="text" name="phone"
														class="required phoneFormat phoneFormateUS  form-control numbersonly removeSplChar" path="phone" />
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
										<label for="language" class="getErrorMessage"> <messageLabel:message
												messageCode='user.form.label.language' /> <span
											class="redStar">*</span></label>
										<div class="language registerError"></div>
										<form:select path="language" id="language" data-width="100%"
											class="required selectpicker">
											<c:forEach items="${languages}" var="languages">
												<option value="${languages.isocode}">
													${languages.name}</option>
											</c:forEach>
										</form:select>
										<div class="registerError"></div>
									</div>
								</div>


								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="supName" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.supname'/>"><messageLabel:message
												messageCode='user.form.label.supervisionName' /><span
											class="redStar">*</span></label>
										<form:input type="text" name="supName" id="supName"
											class="required medium form-control alpha-only valid" path="supervisorName" />
										<div class="registerError"></div>
									</div>

								</div>
							</div>
							
							<div class="row um-form-row um-element">	
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="supervisorPhone"
											class="usm-phone-label getErrorMessage"
											data="<spring:message code='usermanagement.required.supphone'/>"><messageLabel:message
												messageCode='user.form.label.supervisionPhone' /><span
											class="redStar">*</span></label>
										<div class="usm-phone-code">
											<label for="supPhonePrefix" class="widthAuto"> <form:input
													id="supPhonePrefix" class="form-control phone-only" value="+1"
													name="supPhonePrefix" path="supervisorPhonePrefix" />
											</label>
										</div>
										<div class="usm-phone-number">
											<form:input id="supervisorPhone" type="text"
												name="supervisorPhone"
												class="required phoneFormat phoneFormateUS form-control numbersonly removeSplChar"
												path="supervisorPhone" />
											<div class="registerError"></div>
										</div>
										
									</div>

								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="supervisorEmail" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.supemail'/>"><messageLabel:message
												messageCode='user.form.label.supervisionEmail' /><span
											class="redStar">*</span></label>
										<form:input type="email" name="supervisorEmail"
											id="supervisorEmail" class="form-control required big"
											path="supervisorEmail"
											data-msg-email="The email address you entered is invalid. Please try again." />
										<div class="registerError"></div>
									</div>

								</div>
							</div>	
								
							</div>
							<div class="row" id="jnj-employee">
								<div
									class="col-lg-12 col-md-12 col-sm-12 col-xs-12 usm-create-subHead formPanel um-element-col">
									<spring:message code='createUser.employee.text'/></div>
									
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label class="marLeft20 wwid" for="wwid"><messageLabel:message
												messageCode='user.form.label.wwid' /> </label>
										<form:input type="text" name="wwid" id="wwid"
											class="form-control" path="wwid" />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label class="divisionCode" for="divCode"><messageLabel:message
												messageCode='user.form.label.divisionCode' /></label>
										<form:select id="divCode" name="divCode" data-width="100%"
											path="division">
											<c:forEach var="divisionCode" items="${consumerDivisonCodes}">
												<option value="${divisionCode.key}">${divisionCode.key}</option>
											</c:forEach>
										</form:select>
									</div>
								</div>
							</div>
							<div class="row um-element" id="um-roles-accounts">
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel">
                  <div class="row">
                    <div class="usm-create-subHead col-lg-6">
                      <spring:message code='user.management.edit.franchise' />
                      <span class="star-sign">*</span>
                    </div>
                    <div class="col-lg-6">
                      <a class="btn btnclsactive secondarybtn float-right-to-none" data-target="#changeFranchisePopupForUsr" data-toggle="modal"
                        id="selectFranchiseUM"><spring:message code='user.management.edit.franchise' /></a>
                    </div>
                  </div>
                  <div class="usm-account-list scroll-y-holder" id="franchiseDiv">
                    <c:forEach var="franchise" varStatus="count" items="${allFranchise}">
                      <div class="${count.count %2 == 0 ? 'even-row' : 'odd-row'}">
                        <div class="display-table-cell">
                          <div class="">
                            <a href="#" class="usm-account-num">${franchise.name}</a>
                          </div>
                        </div>
                      </div>
                    </c:forEach>
                  </div>
                  <div style="display: none;" id="franchisePopUpDiv">
                      <company:uMFranchisePopUp />
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel">
                  <div class="row">
                    <div class="usm-create-subHead col-lg-4" id="account-header">
                      <label for="roles" class="getErrorMessage"><spring:message code='user.form.accounts' /> </label>
                    </div>
                    <div class="col-lg-8">
                      <a class="btn btnclsactive float-right-to-none" href="#" id="selectAccountUM"><spring:message code='user.form.addAccount' /></a>
                    </div>
                  </div>
                  <form:input type="hidden" id="hddnAccountsString" path="groups" />
                  <input type="hidden" value="${loggedUserAccountList}" id="hddnloggedUserAccountList" />
                  <company:manageMultipleAccounts />
                  <%-- <form:input type="hidden" id="hddnAccountsStringUM" path="groups" /> --%>
                  <!-- <div id="accnt" style="word-break:�break-word;width:300px  !important;word-break:break-all"></div> -->
                  <input type="hidden" value="" id="hddnloggedUserAccountList" />
                  <div class="registerError"></div>
                </div>
              </div>
              <div class="row um-element" id="um-roles-accounts">
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel um-element-col">
                  <div class="roleTxt">
                    <label for="roles" class="getErrorMessage"> <messageLabel:message messageCode='user.form.label.rolePermission' /> <span
                      class="redStar">*</span></label>
                  </div>
                  <div class="">
                    <form:select path="role" id="role" class="required userRoleDropDown" data-width="100%">
                      <c:forEach items="${roles}" var="role">
                        <option value="${role}">
                          <spring:message code="b2busergroup.${role}.name" />
                        </option>
                      </c:forEach>
                    </form:select>
                  </div>
                  <div class="registerError" id="roleMsg"></div>
                  <!-- No charge flag  > move to left column under Roles/ Permission dropdown(AAOL-4662) -->
                  <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                      <div class="checkbox checkbox-info">
                        <c:if test="${(!isTier2User || selfTier2 || selfTier1) && jnjGTB2BCustomerForm.role eq 'JnjGTInternalNoChargeUser'}">
                          <c:set var="noChargeFlag" value="true" />
                        </c:if>
                        <form:checkbox id="noCharge" path="noCharge" checked="${noChargeFlag}" />
                        <div id="no-charge-label">
                          <label for="noCharge" class="left marRight5"> <spring:message code='user.form.label.noChargeFlag' /></label>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="row" id="ConsignmentOrderEntryChkbox">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                      <div class="checkbox checkbox-info">
                        <form:checkbox name="consignmentEntryOrder" id="consignmentEntryOrder" class="left profileSector checkBoxBtn"
                          path="consignmentEntryOrder" />
                        <div class="checkLabelHolder">
                          <label for="consignmentEntryOrder" class="getErrorMessage checkLabel"
                            data="<spring:message code='Consignment.order.entry'/>"><spring:message code='Consignment.order.entry' /></label>
                        </div>
                      </div>
                    </div>
                  </div>
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
            <spring:message code='createProfile.message' />
          </div>
        </div>
      </div>
    </div>
    <div id="add-account-popup-holder"></div>
  </div>
</template:page>

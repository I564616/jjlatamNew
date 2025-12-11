<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="resource"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<spring:url
	value="/my-company/organization-management/manage-users/create"
	var="manageUsersUrl" />
	<template:page pageTitle="${pageTitle}">
	<div class="col-lg-12 col-md-12 " id="myTrainingResources">
	
					
		<div id="emeo-userManagement">
		<spring:url value="/resources/usermanagement" var="searchUserUrl">
		</spring:url>
		<form:form id="searchUserForm" action="${searchUserUrl}"
			commandName="searchUserForm" method="POST" autocomplete="false">
			<!-- AAOL-6460 -->
			<form:input id="downloadType" type="hidden" path="downloadType"/>
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			<div class="row content">
				<div
					class="col-lg-6 col-md-6 col-sm-6 col-xs-12 userManagementheading"><spring:message code="userSearch.breadCrumb" /></div>
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 um-create-usr-btn">
					<a href="javascript:;"><button type="button"
							class="btn btnclsactive pull-right validateprice" id="createUserProfileUserMangement">
							<spring:message code="text.company.manageUser.button.create" /></button></a>
				</div>

			</div>
			
			<c:if test="${success}">
						<div class="panel-group ">
							<div class="successBroadcast broadcastMessageContainer panel panel-success">
								<div class="panel-heading">
									<div class="panel-title">
										<div class="row">
											<span class="glyphicon glyphicon-ok"></span>
											<spring:message code="profile.changeSecurityQuestion.changesSaved" />
										</div>
									</div>
								</div>
							</div>
						</div>	
			</c:if>

			<div class="mainbody-container" id="um-search-holder">
				<div class="row um-header">
					<div
						class="col-lg-1 col-md-1 col-sm-1 col-xs-2 panel-toggle-signholder">
						<a data-toggle="collapse" data-parent="#accordion"
							href="#um-search-content"
							class="toggle-link panel-collapsed clickontext"> <span
							id="emeo-toggle-icon" class="glyphicon glyphicon-plus"></span>
						</a>
					</div>
					<div
						class="col-lg-11 col-md-11 col-sm-11 col-xs-10 panel-header-title">
						<span class="subhead boldtext"><spring:message code="userSearch.heading" /></span>
					</div>
				</div>
				<div class="panel-collapse collapse" id="um-search-content">
					<div class="row um-toggle-seracharea">
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="lastName"><spring:message code="userSearch.lastName" /></label> 
							<form:input type="text" path="lastName" class="form-control usm-formElem" name="lastName" id="lastName" />
											
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="fisrtName"><spring:message code="userSearch.firstName" /></label> 
							<form:input  type="text" name="fisrtName" path="firstName" class="form-control usm-formElem" id="fisrtName" />
										
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="acctNo"><spring:message code="userSearch.accountNumber" /></label>
							<form:input type="text" path="accountNumber" name="acctNo" class="form-control usm-formElem " id="acctNo" /> 
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="acctName"><spring:message code="userSearch.accountName" /></label> 
							<form:input type="text" path="accountName" name="acctName" class="form-control usm-formElem" id="acctName" />
									
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="status"><spring:message code="userSearch.status" /></label> 
							<form:select class="form-control usm-formElem" path="status" id="status">
											<form:option value="All">
												<spring:message code="userSearch.status.all" />
											</form:option>
											<form:option value="ACTIVE">
												<spring:message code="userSearch.status.active" />
											</form:option>
											<form:option value="DISABLED">
												<spring:message code="userSearch.status.disabled" />
											</form:option>
											<form:option value="PENDING_ACCOUNT_SETUP">
												<spring:message code="userSearch.status.pendingAccountSetUp" />
											</form:option>
											<form:option value="PENDING_SUPERVISOR_RESPONSE">
												<spring:message code="userSearch.status.pendingSupervisorResponse" />
											</form:option>
											<form:option value="PENDING_PROFILE_SETUP">
												<spring:message code="userSearch.status.pendingProfileSetUp" />
											</form:option>
											<form:option value="REJECTED">
												<spring:message code="userSearch.status.rejected" />
											</form:option>

							</form:select>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="phone"><spring:message code="userSearch.phone" /></label> 
							<form:input type="text" name="phone" id="phone" path="phone" class="form-control usm-formElem" />
											
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="email"><spring:message code="userSearch.email" /></label> 
								<form:input type="email" name="email" path="email" id="email" class="form-control usm-formElem"/>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="status"><spring:message code="userSearch.role" /></label> 
							<form:select class="form-control usm-formElem" path="role" id="role">
											<form:option value="All">
													<spring:message code="userSearch.status.all" />
											</form:option>
											<form:option value="viewOnlyBuyerGroup">
												<spring:message code="userSearch.role.viewOnly" />
											</form:option>
											<form:option value="placeOrderBuyerGroup">
												<spring:message code="userSearch.role.viewPlaceOrder" />
											</form:option>
											<form:option value="viewOnlySalesRepGroup">
												<spring:message code="userSearch.role.viewOnlySalesRep" />
											</form:option>
											<form:option value="placeOrderSalesGroup">
												<spring:message code="userSearch.role.viewPlaceOrderSalesRep" />
											</form:option>
											<form:option value="JnjGTInternalNoChargeUser">
												<spring:message code="userSearch.role.noCharge" />
											</form:option>
							</form:select>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 um-bottom-field">
							<label class="pull-left usm-label-md" for="sector" class="pull-left usm-label-md"><spring:message code="userSearch.sector" /></label>
							<form:select class="form-control usm-formElem" path="sector" id="sector" >
											<form:option selected="" value="All">
												<spring:message code="userSearch.sector.all" />
											</form:option>
											<form:option value="CONSUMER">
												<spring:message code="userSearch.sector.consumer" />
											</form:option>
											<form:option value="MDD">
												<spring:message code="userSearch.sector.medical" />
											</form:option>
											<form:option value="PHARMA">
												<spring:message code="userSearch.sector.pharma" />
											</form:option>
							</form:select>
						</div>
						
					</div>
					<div class="row um-toggle-footerarea">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="float-right-to-none">
							<c:url value="/resources/usermanagement" var="userManagementPageLink" />
								<a type="button" href="${userManagementPageLink}" id="userManagementReset" class="btn btnclsnormal um-reset-btn"><spring:message code="userSearch.reset" /></a>
								<a href="javascript:;" class="btn btnclsactive um-search-btn" id="userManagementSubmit"><spring:message code="userSearch.search" /></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="mainbody-container">
				<div class="row um-toggle-footerarea">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 um-user-count">
						<spring:message code="userSearch.result" />: ${totalResults}&nbsp;<span><spring:message code="userSearch.users" /></span>
					</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
							<div class="float-right-to-none" style="margin-top: 8px;">
								<span class="link-txt boldtext"><spring:message
										code='reports.download.label' /></span> <a href="#"
									id="userManagementExcel" class="tertiarybtn marginRight excel">
									<spring:message code='reports.excel.label' />
								</a> | <a href="#" id="userManagementPdf" class="tertiarybtn pdf">
									<spring:message code='reports.pdf.label' />
								</a>
							</div>
						</div>
					</div>
				<div class="hidden-xs">
					
					
					<table id="datatab-desktop"
						class="table table-bordered table-striped sorting-table">
						<thead>
							<tr>
								<th class="text-left"><spring:message code="userSearch.header.profileName" /></th>
								<th class="text-left"><spring:message code="userSearch.header.role" /></th>
								<th class="text-left"><spring:message code="userSearch.header.email" /></th>
								<th class="text-left"><spring:message code="userSearch.header.status" /></th>

							</tr>
						</thead>
						<tbody>
						<c:forEach items="${searchPageData.results}" var="user">
							<spring:url value="/resources/usermanagement/edit" var="viewUserUrl">
										<spring:param name="user" value="${user.uid}" />
							</spring:url>
							<tr>
								<td class="text-left">
									<span data-toggle="modal" data-target="#product-detail-popup">
										<div class="" style="width:150px;word-break:break-word">
											<a class="editUserProfileUserMangement" data="${viewUserUrl}" href="javascript:;">${user.firstName}&nbsp;${user.lastName}</a>
										</div>
									</span>
								</td>
								<td class="text-left">
									<c:forEach items="${user.roles}" var="role">
										<div>
											<c:if test="${role != 'b2bcustomergroup'}">
												<spring:message code="b2busergroup.${role}.name" />
											</c:if>
										</div>
									</c:forEach>
									&nbsp;
								</td>
								<td>
									<a href="${viewUserUrl}">${user.email}</a>
								</td>
								<td>
									<div style="word-break:�break-word;width:200px  !important;word-break:break-all">${user.status}</div>
								</td>
							</tr>
					</c:forEach>
						</tbody>
					</table>
					
					<form:input style="display:none;" hidden="true" path="showMoreCounter" id="showMoreCounterUserM" name="showMoreCounterUserM" />
						<form:input style="display:none;" hidden="true" path="searchFlagUserSearch" id="searchFlagUserSearch" name="searchFlagUserSearch" />
						<div class="clear ajaxLoad borDer">
							<span> <c:if 
									test="${fn:length(searchPageData.results) lt searchPageData.pagination.totalNumberOfResults }">
									<nav:pagination selectedShowGroup="${searchUserForm.pageSize}"
										totalResults="${searchPageData.pagination.totalNumberOfResults}"
										pageSize="${fn:length(searchPageData.results)}"
										onClickClass="showMoreUsers" />
								</c:if>
							</span>
						</div>
				</div>
				<!--Accordian Ends here -->
				<!-- Table collapse for mobile device-->
				<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
				<table id="datatab-mobile" class="table table-bordered table-striped tabsize sorting-table">
						<thead>
							<tr>
								<th class="text-left no-sort"><spring:message code="userSearch.header.profileName" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${searchPageData.results}" var="user" varStatus="Count">
							<spring:url value="/resources/usermanagement/edit" var="viewUserUrl">
											<spring:param name="user" value="${user.uid}" />
								</spring:url>
							<tr>
								<td class="panel-title text-left valign-top">
										<a data-toggle="collapse" data-parent="#accordion" href="#user-mobi-collapse${Count.count}" class="ref_no toggle-link panel-collapsed">
										<div class="table-row">
											<div class="table-cell" style="padding-right:5px"><span class="glyphicon glyphicon-plus skyBlue"></span></div>
											<div class="table-cell"><a class="editUserProfileUserMangement" data="${viewUserUrl}" href="javascript:;">${user.firstName}&nbsp;${user.lastName}</a></div>
										</div>
										</a>
									</div>
									<div id="user-mobi-collapse${Count.count}" class="panel-collapse collapse">
										<div class="panel-body details">
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px"><spring:message code="userSearch.header.role" /></div>
												<div>
													<c:forEach items="${user.roles}" var="role">
														<div>
															<c:if test="${role != 'b2bcustomergroup'}">
																<spring:message code="b2busergroup.${role}.name" />
															</c:if>
														</div>
													</c:forEach>
												</div>
											</div>
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px;"><spring:message code="userSearch.header.email" /></div>
												<div><a href="${viewUserUrl}">${user.email}</a></div>
											</div>
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px"><spring:message code="userSearch.header.status" /></div>
												<div>${user.status}</div>
											</div>
										</div>
									</div>
								</td>
							</tr>
						</c:forEach>
						</tbody>
				</table>
				</div>
				<!-- mobile ends -->
		</div>
		<!-- End - Total Price Summary -->
	</form:form>
</div>
</div>
</template:page>

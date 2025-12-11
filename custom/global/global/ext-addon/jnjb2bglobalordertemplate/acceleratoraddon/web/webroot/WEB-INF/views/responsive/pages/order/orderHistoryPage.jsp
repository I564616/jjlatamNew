<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>

<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>




<template:page pageTitle="${pageTitle}">

	<div id="Orderhistorypage">
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				<div class="row content">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<spring:message code="orderHistoryPage.heading" />
					</div>
				</div>
			</div>
		</div>
		
<jsp:useBean id="date" class="java.util.Date"/><!-- Modified by Archana for AAOL-5513 -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<fmt:formatDate pattern="${dateformat}" value="${date}" var="currentDate" />
<c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}" var="isMddSite"></c:set>
<c:url value="/order-history" var="orderHistoryFormUrl" />	
		<form:form id="orderHistoryForm" name="orderHistoryForm" method="POST"
			action="${orderHistoryFormUrl}" commandName="orderHistoryForm">
			<input style="display:none;" type="hidden"  id="searchRequest" name="searchRequest" value="${orderHistoryForm.searchRequest}" />
			<input style="display:none;" type="hidden"  id="accounts" name="accounts" value="${selectedaccounts}" />
			<input style="display:none;" type="hidden"  id="totAccountsSelected" name="totAccountsSelected" value="${totalAccountsSelected}" />
			<form:input type="hidden" id="isSelectAllAccount" path="selectAllAccount" />
			<div class="row jnj-panel mainbody-container">
				<div class="col-lg-12 col-md-12">
					<div class="row jnj-panel-header">
						<div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
							<div class="row">
								<div id="usr-address" class="amazon">
									<span id="defaultAccountSel"> 
										<c:choose>
											<c:when test="${accountUID ne null}">
												${accountUID}
											</c:when>
											<c:otherwise>
												${user.currentB2BUnitID}
											</c:otherwise>
										</c:choose>
										,
										<c:choose>
											<c:when test="${accountName ne null}">
												<c:set var="dispAccountName" value="${accountName}" />
											</c:when>
											<c:otherwise>
												<c:set var="dispAccountName"
													value="${user.currentB2BUnitName} " />
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${fn:length(dispAccountName)>45 }">
												<c:set var="dispAccountNameSubString"
													value="${fn:substring(dispAccountName, 0, 45)}..." />
												<label title="${dispAccountName}" class="label">${dispAccountNameSubString}</label>
											</c:when>
											<c:otherwise>
												<c:out value="${dispAccountName}"></c:out>
											</c:otherwise>
										</c:choose>
										<c:if  test="((accountGLN ne 'null' && not empty accountGLN) || not empty user.currentB2BUnitGLN )}">
												<li><spring:message code="header.information.account.gln" />&nbsp;
													<span> 
														<c:choose>
															<c:when  test="${accountGLN ne 'null' && not empty accountGLN}">
																${accountGLN}
															</c:when>
															<c:otherwise>
																${user.currentB2BUnitGLN}
															</c:otherwise>
														</c:choose>
													</span>
												</li>
										</c:if>
									</span>
									<span id="singleAccountSel" style="display:none;"> 
										<span class="boldtext"> <spring:message	code="orderHistoryPage.account" /></span> ${selectedaccounts} 
									</span>
									<span id="multipleAccountSel" style="display:none;"> 
										 <spring:message code="orderHistoryPage.multiAccount" />(<span id="totNoOfAccountsSelected">${totalAccountsSelected}</span> Selected) 
									</span>
									<a href="#" id="selectAccount"><spring:message	code="orderHistoryPage.change" /></a>
								</div>

							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
							<div class="checkbox checkbox-info pull-right selectchkbox">
								<input id="check4" class="styled orderHistorySelectAll" type="checkbox"> <label
									for="check4"><spring:message code="orderHistoryPage.selectallaccounts" /></label>
							</div>
						</div>
					</div>

					<div class="row jnj-panel-body">
						<div
							class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12 searchby">
							<label class="pull-left form-label form-label-select-large"><spring:message
									code="orderHistoryPage.searchBy" /></label>

							<form:select id="searchby" path="searchBy"
								class="form-control form-element form-element-select-large">
								<form:option value="">
									<spring:message code="orderHistoryPage.select" />
								</form:option>
								<form:options items="${searchOptions}" />
							</form:select>

						</div>
						<div class="col-lg-4 col-md-5 col-sm-6 col-xs-12">
						<spring:message code='orderHistoryPage.search' var="osSearch"/>
							<form:input type="text" path="searchText" title="${osSearch}"
								class="form-control rounded-textbox" placeholder="${osSearch}"
								id="searchByInput" />
						</div>
					</div>

					<div class="row jnj-panel-body">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start date-picker-start">
									<label class="pull-left form-label form-label-date boldtext"><spring:message
											code="orderHistoryPage.from" /></label>
									<div class="input-group form-element form-element-date">
										<input id="datePicker1" name="startDate"
											placeholder="<spring:message code='cart.common.selectDate' />" class="date-picker form-control"
											type="text" value="${startDate}"> <label
											for="datePicker1" class="input-group-addon btn"><span
											class="glyphicon glyphicon-calendar"></span> </label>
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12  end um-element-col date-picker-end">
									<label
										class="pull-left form-label form-label-date boldtext ipadterritory"><spring:message
											code="orderHistoryPage.to" /></label>
									<div class="input-group form-element form-element-date  um-element-col">
										<input id="datePicker2" name="endDate"
											placeholder="<spring:message code='cart.common.selectDate' />" class="date-picker form-control"
											type="text" value="${endDate}"> <label
											for="datePicker2" class="input-group-addon btn"><span
											class="glyphicon glyphicon-calendar"></span> </label>
									</div>
									<div id="orderHistoryDateError" class="registerError"></div>
									
								</div>
							</div>
						</div>

						<div
							class="form-group col-lg-8 col-md-8 col-sm-12 col-xs-12 companybrand">
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<label
										class="pull-left form-label form-label-select boldtext textheightipad"><spring:message
											code="orderHistoryPage.orderTypeheader" /></label>
											<c:if test="${isMddSite}">
									<form:select path="orderType" id="orderType"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${orderTypes}" itemLabel="name"
											itemValue="code" />
									</form:select>
									</c:if>
								</div>
								<div
									class="col-lg-6 col-md-6 col-sm-6 col-xs-12 analysisvariable">
									<label
										class="pull-left form-label form-label-select boldtext textheightipad ipadterritory"><spring:message
											code="orderHistoryPage.channelColon" /></label>

									<form:select id="channel" path="channel"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${channels}" itemLabel="name"
											itemValue="code" />
									</form:select>

								</div>
								<div
									class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px orderedfrom">
									<label
										class="pull-left form-label form-label-select boldtext textheightipad"><spring:message
											code="orderHistoryPage.orderStatus" /></label>

									<form:select id="orderStatus" path="orderStatus"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${orderStatus}" itemLabel="name"
											itemValue="code" />
									</form:select>

								</div>
								<div
									class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px linestatus">
									<label
										class="pull-left form-label form-label-select boldtext ipadterritory linestatustext"><spring:message
											code="orderHistoryPage.lineStatus" /></label>

									<form:select id="lineStatus" path="lineStatus"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${lineStatus}" itemLabel="name"
											itemValue="code" />
									</form:select>

								</div>
						<%--	<c:if test="${isDepuyUser}">
								<!-- Changes for Adding Franchise drop-down AAOL-4732 -->
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px orderedfrom">
									<label class="pull-left form-label form-label-select boldtext ipadterritory linestatustext">
										<spring:message code = "order.history.surgeon.name"/></label>												
									<form:select id="surgeonId" path="surgeonId"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${surgeonOptions}" itemLabel="name"
											itemValue="code" />
									</form:select>
								</div>
							</c:if> --%>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px linestatus pull-right">
									<label class="pull-left form-label form-label-select boldtext ipadterritory linestatustext" style="line-height:18px">
										<spring:message code="order.history.franchise.description"></spring:message></label>												
									<form:select id="franchise" path="franchise"
										class="form-control form-element form-element-select">
										<form:option value="">
											<spring:message code="orderHistoryPage.all" />
										</form:option>
										<form:options items="${franchiseOptions}" itemLabel="name"
											itemValue="code" />
									</form:select>
								</div>
								
								
							</div>
						</div>
					</div>

					<div class="row jnj-panel-footer">
						<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
							<common:downloadResultsTag />

						</div>
						<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
							<div class="pull-right btn-mobile">
								<button type="button"
									class="btn btnclsactive searchbtn pull-right"
									id="ordHistorySearch">
									<spring:message code="orderHistoryPage.search" />
								</button>
								<button type="button"
									class="btn btnclsnormal reset orderHistoryReset">
									<spring:message code="orderHistoryPage.reset" />
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>



		</form:form>
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div
					class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container orderHistory">
					<table id="datatab-desktop"
						class="table table-bordered table-striped sorting-table reports-table-desktop">
					<%-- AAOL-3680 --%>
						<thead>
							<tr>
								<th class="tab-tab-left orderNO"><spring:message
										code="orderHistoryPage.order" /></th>
								<th class="tab-text-left orderNO"><spring:message
										code="orderHistoryPage.po" /></th>
								<th class="tab-text-left"><spring:message
										code="orderHistoryPage.orderType" /></th>
								<c:choose>
									<c:when test="${isDepuyUser}">
										<th class="tab-text-left cell-pad-right patientID"><spring:message
										code ="order.history.patient.id"/></th>
										<th class="tab-text-left cell-pad-right caseDate"><spring:message
										code ="order.history.case.date"/></th>
									</c:when>
									<c:otherwise>
										<th class="tab-text-left cell-pad-right franchise"><spring:message
										code ="order.history.franchise.description"/></th>
										
									</c:otherwise>
								</c:choose>							
								<th class="tab-text-left cell-pad-right"><spring:message
										code="orderHistoryPage.stipTo" /></th>
								<th class="tab-text-left"><spring:message code="orderHistoryPage.orderDate" /></th>
								<th class="tab-text-left cell-pad-right"><spring:message
										code="orderHistoryPage.channel" /></th>
								<th class="tab-text-left"><spring:message
										code="orderHistoryPage.status" /></th>
								<th class="tab-text-right"><spring:message
										code="orderHistoryPage.total" /></th>
								
							</tr>
						</thead>
					<%-- AAOL-3680 --%>	
						<div class="">
						<c:choose>
						<c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
							<c:forEach items="${searchPageData.results}" var="order" >

								<c:url value="/order-history/order/${order.code}"
									var="orderDetailUrl" />
								<tr class="orderHistoryRow even gotoOrderHstryDetail">

									<td class="tab-text-left column1 labelText tab-col-1 paddingRight8px orderNO"><a href="${orderDetailUrl}"
										class="ordernumber "> <c:choose>
												<c:when test="${not empty order.sapOrderNumber}">
	                        			${order.sapOrderNumber}
	                        		</c:when>
												<c:otherwise>
	                        			${order.code}
	                        		</c:otherwise>
											</c:choose>
									</a> <c:if test="${order.surgeonUpdatInd}">
			                        		 <br> <a href="#"
												id="updateSurgeon" class="updateSurgeon"
												orderNumber="${order.code}"
												surgeonName="${order.surgeonName}">
												<spring:message	code="orderHistoryPage.updateSurgeon" /></a>
										</c:if>
										<c:if test="${order.surgeonUpdatInd}">
			                        		 <br> <a href="#"
												id="showSurgeryInfoOrderPopup" class="showSurgeryInfoOrderPopup"
												orderNumber="${order.code}">
												<spring:message	code="order.history.view.surgery.info" /></a>
										</c:if>
										</td>

									<td class="tab-text-left column2 wordBreak orderNO" style="500px !important"><c:choose>
											<c:when
												test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
												<a href="#" id="updatePoNumber" class="updatePoNumber"
													orderNumber="${order.code}"> <spring:message
														code="orderHistoryPage.updatePO" />
												</a>
											</c:when>
											<c:otherwise>
				            	     ${order.purchaseOrderNumber}
				             	       </c:otherwise>
										</c:choose></td>

									<td class="tab-text-left column3 text-nowrap"><c:choose>
											<c:when test="${not empty order.ordertype}">
												<span class="txtFont"><spring:message
														code="cart.common.orderType.${order.ordertype}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose></td>
									<c:choose>
										<c:when test="${isDepuyUser}">
												<td class="tab-text-left column4"><c:choose>
												<c:when test="${not empty order.patientID}">
													<span class="txtFont"><spring:message
															code="${order.patientID}" /></span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
											<td class="tab-text-left column5"><c:choose>
													<c:when test="${not empty order.caseDate}">
														<span class="txtFont"><spring:message
																code="${order.caseDate}" /></span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
										</c:when>
										<c:otherwise>
											<td class="tab-text-left column4"><c:choose>
											<c:when test="${not empty order.franchise}">
												<span class="txtFont"><spring:message
														code="${order.franchise}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
											</c:choose></td>										
										</c:otherwise>
									</c:choose>
									
									<td class="tab-text-left column4"><c:choose>
											<c:when test="${not empty order.shipToNumber}">
												<span class="txtFont">${order.shipToNumber}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose></td>

									<td class="tab-text-left column5"><c:choose>
											<c:when test="${not empty order.placed}"><!-- Modified by Archana for AAOL-5513 -->
											<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
												<fmt:formatDate pattern="${dateformat}" value="${order.placed}"
													var="orderDate" />
												<span class="txtFont">${orderDate}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose></td>

									<td class="tab-text-left column6"><c:choose>
											<c:when test="${not empty order.channel}">
												<span class="txtFont"><spring:message code="order.channel.${order.channel}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont"><spring:message
														code="order.channel.other" /></span>
											</c:otherwise>
										</c:choose></td>

									<td class="tab-text-left column7 text-nowrap"><c:choose>
											<c:when test="${not empty order.statusDisplay}">
												<span class="pendingStatus"></span><span class="home-status-txt">${order.statusDisplay}</span>
								</c:when>
											<c:otherwise>
												<span class="pendingStatus">&nbsp;</span>
											</c:otherwise>
										</c:choose></td>

									<td class="tab-text-right column8"><c:choose>
											<c:when test="${not empty order.total.formattedValue}">
												<span class="priceList">${order.total.formattedValue}</span>
											</c:when>
											<c:otherwise>
												<span class="priceList">&nbsp;</span>
											</c:otherwise>
										</c:choose></td>
									
									

								</tr>
							</c:forEach>
							</c:when>
							</c:choose>
						</div>
					</table>
				</div>
			</div>
		</div>




									<!-- Table collapse for ipad device-->




		<div
			class="visible-sm hidden-lg hidden-xs hidden-md jnj-panel-for-table mainbody-container">
			<table id="datatab-tablet"
				class="table table-bordered table-striped sorting-table bordernone mobile-table">
				<thead>
					<tr>
						<th class="orderno"><spring:message
										code="orderHistoryPage.orderNumber" /></th>
						<th class="no-sort"><spring:message
										code="orderHistoryPage.poNomber" /></th>
						<th class="no-sort"><spring:message code="orderHistoryPage.orderDate" /></th>
						<th class="no-sort" style="width: 119px;"><spring:message
										code="orderHistoryPage.status" /></th>
						<th class="no-sort"><spring:message
										code="orderHistoryPage.total" /></th>
					</tr>
				</thead>
				<tbody>
				
				<c:choose>
						<c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
							<c:forEach items="${searchPageData.results}" var="order" varStatus="count">

								<c:url value="/order-history/order/${order.code}"
									var="orderDetailUrl" />
					<tr>
						<td class="vlign-top orderno"><a data-toggle="collapse"
							data-parent="#accordion" href="#collapse${count.count}"
							class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
								class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
								<a href="${orderDetailUrl}"
										class="ordernumber"> <c:choose>
												<c:when test="${not empty order.sapOrderNumber}">
	                        			${order.sapOrderNumber}
	                        		</c:when>
												<c:otherwise>
	                        			${order.code}
	                        		</c:otherwise>
											</c:choose>
									</a>
						</a>
							<div id="collapse${count.count}" class="panel-collapse collapse">
								<div class="panel-body">
									<div class="sub-details-row">
										<p style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.stipTo" /></p>
										<p><c:choose>
											<c:when test="${not empty order.shipToNumber}">
												<span class="txtFont">${order.shipToNumber}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose></p>
									</div>

									<div class="sub-details-row">
										<p style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.orderType" /></p>
										<P><c:choose>
											<c:when test="${not empty order.ordertype}">
												<span class="txtFont"><spring:message
														code="cart.common.orderType.${order.ordertype}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose></P>
									</div>

									<div class="sub-details-row">
										<P style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.channel" /></P>
										<P><c:choose>
											<c:when test="${not empty order.channel}">
												<span class="txtFont"><spring:message code="order.channel.${order.channel}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont"><spring:message
														code="order.channel.other" /></span>
											</c:otherwise>
										</c:choose></P>
									</div>
								</div>
							</div>
							
							</td>

						<td class="vlign-top">
						<c:choose>
								<c:when
												test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
												<a href="#" id="updatePoNumber" class="updatePoNumber"
													orderNumber="${order.code}"> <spring:message
														code="orderHistoryPage.updatePO" />
												</a>
											</c:when>
											<c:otherwise>
				            	     ${order.purchaseOrderNumber}
				             	       </c:otherwise>
										</c:choose>
						</td>
						<td class="vlign-top">
						<c:choose>
											<c:when test="${not empty order.placed}"><!-- Modified by Archana for AAOL-5513 -->
											<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
												<fmt:formatDate pattern="${dateformat}" value="${order.placed}"
													var="orderDate" />
												<span class="txtFont">${orderDate}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
						</td>
						<td class="vlign-top">
						<c:choose>
											<c:when test="${not empty order.statusDisplay}">
												<span class="pendingStatus"></span><span class="home-status-txt">${order.statusDisplay}</span>
								</c:when>
											<c:otherwise>
												<span class="pendingStatus">&nbsp;</span>
											</c:otherwise>
										</c:choose>
						</td>
						<td class="vlign-top">
							<div class="sub-details-row">
								<P>
								<c:choose>
											<c:when test="${not empty order.total.formattedValue}">
												<span class="priceList">${order.total.formattedValue}</span>
											</c:when>
											<c:otherwise>
												<span class="priceList">&nbsp;</span>
											</c:otherwise>
										</c:choose>
								</P>
							</div>
						</td>
					</tr>
					</c:forEach>
							</c:when>
							</c:choose>
				</tbody>
				
			</table>
		</div>
												<!--Accordian for ipad Ends here -->

									
									
									<!-- Table collapse for mobile device -->
		
		<div
			class="visible-xs hidden-lg hidden-sm hidden-md jnj-panel-for-table mainbody-container">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table bordernone mobile-table">
				<thead>
					<tr>
						<th class="orderno"><spring:message
										code="orderHistoryPage.orderNumber" /></th>
						<th class="no-sort"><spring:message
										code="orderHistoryPage.poNomber" /></th>
					</tr>
				</thead>
				
				
				<tbody>
				
				<c:choose>
						<c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
 							<c:forEach items="${searchPageData.results}" var="ordermob"  varStatus="mob">

								<c:url value="/order-history/order/${ordermob.code}"
									var="orderDetailUrl" />
					<tr>
						<td class="vlign-top orderno"><a data-toggle="collapse"
							data-parent="#accordion" href="#collapse${ordermob.code}"
							class="toggle-link panel-collapsed skyBlue"> <span
								class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
								<a href="${orderDetailUrl}"
										class="ordernumber"> <c:choose>
												<c:when test="${not empty ordermob.sapOrderNumber}">
	                        			${ordermob.sapOrderNumber}
	                        		</c:when>
												<c:otherwise>
	                        			${ordermob.code}
	                        		</c:otherwise>
											</c:choose>
									</a>
						</a>
							<div id="collapse${ordermob.code}" class="panel-collapse collapse">
								<div class="panel-body">
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px"><spring:message code="orderHistoryPage.orderDate" /></div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.placed}"><!-- Modified by Archana for AAOL-5513 -->
											<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
												<fmt:formatDate pattern="${dateformat}" value="${ordermob.placed}"
													var="orderDate" />
												<span class="txtFont">${orderDate}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.status" /></div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.statusDisplay}">
												<span class="pendingStatus"></span><span class="home-status-txt">${ordermob.statusDisplay}</span>
								</c:when>
											<c:otherwise>
												<span class="pendingStatus">&nbsp;</span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message
										code="orderHistoryPage.stipTo" />
										</div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.shipToNumber}">
												<span class="txtFont">${ordermob.shipToNumber}</span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.orderType" /></div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.ordertype}">
												<span class="txtFont"><spring:message
														code="cart.common.orderType.${ordermob.ordertype}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.channel" /></div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.channel}">
												<span class="txtFont"><spring:message code="order.channel.${ordermob.channel}" /></span>
											</c:when>
											<c:otherwise>
												<span class="txtFont"><spring:message
														code="order.channel.other" /></span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
									<div class="sub-details-row">
										<div style="font-family: jnjlabelfont; font-size: 10px"><spring:message
										code="orderHistoryPage.total" /></div>
										<div>
										<c:choose>
											<c:when test="${not empty ordermob.total.formattedValue}">
												<span class="priceList">${ordermob.total.formattedValue}</span>
											</c:when>
											<c:otherwise>
												<span class="priceList">&nbsp;</span>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
								</div>
							</div></td>
						<td class="vlign-top">
										<c:choose>
										<c:when
												test="${empty ordermob.purchaseOrderNumber && ordermob.poNumberUpdateInd}">
												<a href="#" id="updatePoNumber" class="updatePoNumber"
													orderNumber="${ordermob.code}"> <spring:message
														code="orderHistoryPage.updatePO" />
												</a>
											</c:when>
											<c:otherwise>
				            	     ${ordermob.purchaseOrderNumber}
				             	       </c:otherwise>
										</c:choose>
					</td>
					</tr>
					</c:forEach>
					</c:when>
					</c:choose>
				</tbody>
			</table>
		</div>
		<!-- Accordian for mobile ends here -->
	</div>
	<div id="order-history-popup-holder"></div>	
</template:page>

<!-- Update PO Pop-up AAOL-3084-->
<commonTags:updatePO/>

<div class="orderHistory-surgeon" id="surgeonPopupHolder"></div>
<div class="orderHistory-surgeon" id="surgeryPopupHolder"></div>

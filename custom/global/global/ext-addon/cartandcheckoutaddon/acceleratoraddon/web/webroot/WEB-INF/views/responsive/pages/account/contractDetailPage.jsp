<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/account"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<template:page pageTitle="${pageTitle}">
<!-- fixed for 35090  start THis element append out of !DOCTYPE html so put it into page template -->
<input type="hidden" id="contractAddToCartEnable" value="${canCheckout}"/>
<input id="contractHeaderActive" value="${contractData.active}" type="hidden"/>
	<div id="ContractDetailsPage"> 
		<ul class="breadcrumb">
			<li><breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" /></li>
		</ul>
		<c:set var="totalRecords" value="${searchPageData.pagination.totalNumberOfResults}" />
			<div id="globalMessages">
				<common:globalMessages />
			</div>
		<%--  <c:url value="/my-account/contract/search" var="contractAction"></c:url> --%>
		<div class="row">
			<div class="col-xs-12 headingTxt content"><label:message messageCode="contract.myContracts"/></div>
		</div>
 
 <%--	<div id="globalMessages">
		<common:globalMessages />
	</div>
 
	<div class="row jnj-body-padding shipmentContainer" id="jnj-body-content"> --%>
				<div class="row jnj-panel mainbody-container">
					<div class="col-lg-12 col-md-12">
						<div class="row jnj-panel-header">											
							<div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
								<div class="contract-label-head"><label:message messageCode="contract.contractNumber"/></div>
								<div id="contract-num">${contractData.eccContractNum}</div>
								<input type="hidden" value="${contractData.eccContractNum}" id="hddnContract-num" />
							</div>
							<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12" id="download-link">
								<span class="pull-right"><span class="link-txt boldtext"><label:message messageCode="text.download"/></span>
								<a class="tertiarybtn contractDetailExcel" href="#" id ="contractExcelDownload" ><label:message messageCode="contract.excel.label"/></a>  
								<span class="pipesymbol">|</span> &nbsp; 
								<a class="tertiarybtn contractDetailPdf" href="#" id ="contractPdfDownload" ><label:message messageCode="contract.pdf.label"/></a></span>
							</div>	
						</div>
						<div class="row jnj-panel-body">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 contract-leftTotop-content">
								<div class="contract-content-holder">
									<div class="contract-label-head text-uppercase"><label:message messageCode="contract.indirectCustomer"/></div>
									<div class="contract-content">${contractData.indirectCustomer}</div>
								</div>
								<div class="contract-content-holder">
									<div class="text-uppercase contract-cutom-name-label"><label:message messageCode="contract.indirectCustomerName"/></div>
									<div class="contract-content">${contractData.indirectCustomerName}</div>
								</div>
							</div>
							<div class="col-lg-1 col-md-1 col-sm-1 col-xs-12" id="contract-separator"></div>
							<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 contract-rightTobottom-content">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.status"/></div>
										<div class="contract-content contract-content-active">
											<strong>
											<c:choose>
												<c:when test="${contractData.active}"><span class="active"><label:message messageCode="contract.status.active"/></span></c:when>
												<c:otherwise><span class="inactive"><label:message messageCode="contract.status.inactive"/></span></c:otherwise>
											</c:choose>
										</strong>
									</div>
									</div>
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.tenderNumber"/></div>
										<div class="contract-content">${contractData.tenderNum}</div>
									</div>
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.contractType"/></div>
										<div class="contract-content">${contractData.contractOrderReason}</div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.creationDate"/></div>
										<c:choose>
											<c:when test="${'en' eq sessionlanguage}"><!-- Modified by Archana for AAOL-5513 -->
												<div class="contract-content"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
												<fmt:formatDate value="${contractData.startDate}" pattern="dd/MM/yyyy"/>&nbsp;</div>
											</c:when>
											<c:otherwise><!-- Modified by Archana for AAOL-5513 -->
											 <div class="contract-content"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
											 <fmt:formatDate value="${contractData.startDate}" pattern="dd/MM/yyyy"/>&nbsp;</div>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.expirationDate"/></div>
										<c:choose>
											<c:when test="${'en' eq sessionlanguage}"><!-- Modified by Archana for AAOL-5513 -->
												<div class="contract-content"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
												<fmt:formatDate value="${contractData.endDate}" pattern="dd/MM/yyyy"/>&nbsp;</div>
											</c:when>
											<c:otherwise><!-- Modified by Archana for AAOL-5513 -->
											 <div class="contract-content"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
											 <fmt:formatDate value="${contractData.endDate}" pattern="dd/MM/yyyy"/>&nbsp;</div>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="contract-content-holder">
										<div class="contract-label-head text-uppercase"><label:message messageCode="contract.detail.lasttimeupdated"/></div>
										<c:choose>
											<c:when test="${'en' eq sessionlanguage}">
												<div class="contract-content"><fmt:formatDate value="${contractData.lastUpdatedTime}" pattern="dd/MM/yyyy - hh:mm:ss"  timeZone="${timezoneSet}"/>&nbsp;</div>
											</c:when>
											<c:otherwise>
											 <div class="contract-content"><fmt:formatDate value="${contractData.lastUpdatedTime}" pattern="dd/MM/yyyy - hh:mm:ss"  timeZone="${timezoneSet}"/>&nbsp;</div>
											</c:otherwise>
										</c:choose>
									</div>
									<c:if test="${contractData.documentType == 'ZCV'}">
										<div class="contract-content-holder">
											<div class="contract-label-head text-uppercase"><label:message messageCode="contract.totalValue"/>:</div>
											<div class="contract-content"><format:price priceData="${cntrctPriceData.totalAmount}" hideZeroValue="true"/>&nbsp;</div>
										</div>
										<div class="contract-content-holder">
											<div class="contract-label-head text-uppercase"><label:message messageCode="contract.consumedValue"/>:</div>
											<div class="contract-content"><format:price priceData="${cntrctPriceData.consumedAmount}"/>&nbsp;</div>
										</div>
										<div class="contract-content-holder">
											<div class="contract-label-head text-uppercase"><label:message messageCode="contract.contractBalance"/>:</div>
											<div class="contract-content"><format:price priceData="${cntrctPriceData.balanceAmount}"/>&nbsp;</div>
										</div>
									</c:if>
								</div>
							</div>	
						</div>
					</div>
				</div>	
				<!-- <div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<button type="button" class="btn btnclsactive pull-right contract-add-to-cart">ADD TO CART</button>
					</div>
				</div> -->	
				  <%-- <div class="row">
					<div class="col-lg-12 col-md-12">
						<div class="hidden-xs jnj-panel-for-table mainbody-container">	
							<table id="datatab-desktop" class="table table-bordered table-striped contract-detail-desktab">
								<thead>
									<tr>
										<th class="no-sort text-uppercase">
											<div class="checkbox checkbox-info selectchkbox">
												<input id="contract-select-all" class="styled contract-thead-chckbox" type="checkbox">
												<label for="contract-select-all"  id="contract-head-chck-label">select all</label>
											</div>
										</th>
										<th class="no-sort text-uppercase">unit price</th>
										<th class="no-sort text-uppercase contract-cnu-head">contract unit of measurement</th>
										<th class="no-sort text-uppercase">contract quantity</th>
										<th class="no-sort text-uppercase">consumed</th>
										<th class="no-sort text-uppercase">balance</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>
											<div class="display-table-row">
												<div class="checkbox checkbox-info selectchkbox display-table-cell">
													<input id="contract-check-1" class="styled contract-tcell-chckbox" type="checkbox">
													<label for="contract-check-1"></label>
												</div>
												<div class="contract-check-content display-table-cell">
													<a href="#">JNJ ID# 6902319</a>
													<div>0.8% RESOLVE C 22X3ML</div>
												</div>
											</div>
										</td>
										<td>$543.72</td>
										<td>each</td>
										<td>100</td>
										<td>18</td>
										<td>82</td>
									</tr>
									<tr>
										<td>
											<div class="display-table-row">
												<div class="checkbox checkbox-info selectchkbox display-table-cell">
													<input id="contract-check-2" class="styled contract-tcell-chckbox" type="checkbox">
													<label for="contract-check-2"></label>
												</div>
												<div class="contract-check-content display-table-cell">
													<a href="#">JNJ ID# 6902319</a>
													<div>0.8% RESOLVE C 22X3ML</div>
												</div>
											</div>
										</td>
										<td>$543.72</td>
										<td>each</td>
										<td>100</td>
										<td>18</td>
										<td>82</td>
									</tr>
									<tr>
										<td>
											<div class="display-table-row">
												<div class="checkbox checkbox-info selectchkbox display-table-cell">
													<input id="contract-check-3" class="styled contract-tcell-chckbox" type="checkbox">
													<label for="contract-check-3"></label>
												</div>
												<div class="contract-check-content display-table-cell">
													<a href="#">JNJ ID# 6902319</a>
													<div>0.8% RESOLVE C 22X3ML</div>
												</div>
											</div>
										</td>
										<td>$543.72</td>
										<td>each</td>
										<td>100</td>
										<td>18</td>
										<td>82</td>
									</tr>
									
								</tbody>
							</table>	
						</div>
					</div>
				</div>   --%>
				<!-- Table collapse for ipad device-->
                         
				<!--Accordian for ipad Ends here -->								

				<!-- Table collapse for mobile device -->
				<%-- <div class="visible-xs hidden-lg hidden-sm hidden-md jnj-panel-for-table mainbody-container">
					<table id="datatab-mobile" class="table table-bordered table-striped bordernone mobile-table contract-detail-mobi">
						<thead>
							<tr>
								<th class="no-sort text-uppercase">
									<div class="checkbox checkbox-info selectchkbox">
										<input id="contract-select-all-mobi" class="styled contract-thead-chckbox" type="checkbox">
										<label for="contract-select-all-mobi"  id="contract-head-chck-label">select all</label>
									</div>
								</th>
							 </tr>
						</thead>
						<tbody>
							<tr>
								<td class="vlign-top orderno">
									<div class="display-table-row">
										<div class="display-table-cell" style="vertical-align:top">
											<div class="checkbox checkbox-info selectchkbox display-table-cell contract-tcell-chckbox-mob">
												<input id="contract-check-mobi-1" class="styled contract-tcell-chckbox" type="checkbox">
												<label for="contract-check-mobi-1"></label>
											</div>
										</div>	
										<div class="display-table-cell" style="vertical-align:top">
											
											<a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
											<div class="display-table-row">
												<div class="display-table-cell"><span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span></div>
												<div class="display-table-cell">
																									
														<div class="contract-check-content display-table-cell">
															<a href="#">JNJ ID# 6902319</a>
															<div>0.8% RESOLVE C 22X3ML</div>
														</div>
												
												</div>	
											</div>
										</div>	
									</div>	
									<div id="collapse1" class="panel-collapse collapse">
										<div class="panel-body">
											<div class="sub-details-row">
												<div class="mob-view-label uppercase">unit price</div>
												<div>$545.72</div>
											</div>	
											
											<div class="sub-details-row">	
												<div class="mob-view-label uppercase">contract unit of measurement</div>
												<div>Lorem Ipsum</div>
											</div>	
											
											<div class="sub-details-row">
												<div class="mob-view-label uppercase">contract quantity</div>
												<div>Web</div>
											</div>
											<div class="sub-details-row">
												<div class="mob-view-label uppercase">consumed</div>
												<div>Web</div>
											</div>
											<div class="sub-details-row">
												<div class="mob-view-label uppercase">balance</div>
												<div>Web</div>
											</div>
										</div>
									</div>
								</td>
							</tr>
						</tbody>
					</table>			
				</div> --%>
				<!-- Accordian for mobile ends here -->
				<!-- <div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<button type="button" class="btn btnclsactive pull-right contract-add-to-cart-bottom">ADD TO CART</button>
					</div>
				</div> -->
				<account:contractEntries documentType="${contractData.documentType}"/>
			 </div> 
</template:page>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/order"%>

<template:page pageTitle="${pageTitle}">
<!--  AAOL-6138 changes start -->
	<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
	<!--  AAOL-6138 changes end -->
<input style="display:none;" type="hidden" id= "disputeFlag" value="${disputeFlag}" />
	<div class="">
		<div class="row">
			<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
			  <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			 </div>
			
		</div>
		<div class="row">
			<div class="col-xs-12 headingTxt content"><spring:message code="orderDetailPage.heading"/>
				</div>
					</div>
		
			<c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}" var="isMddSite"></c:set>
		<c:choose>
			<c:when test="${'ZDEL' eq orderData.orderType}">
				<order:orderDetailHeaderDel orderData="${orderData}" isMddSite="${isMddSite}" />			
			</c:when>
			<c:otherwise>
				<order:orderDetailHeader orderData="${orderData}" isMddSite="${isMddSite}" />
			
			</c:otherwise>
		</c:choose>
		
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div class="float-right-to-none marginbtm30">
					<input type="button" style="" id="errorMultiCartTmplt" class="tertiarybtn homeCartErrors btn btnclsnormal" value="<spring:message code='homePage.errorDetails' />" />
					<button id="orderDetailAddToCart1" orderCode ="${orderData.code}" class="btn btnclsactive orderDetailAddToCart" ><spring:message code="orderDetailPage.addOrdertocart"/></button>
				</div>
				<div class="error hide" id="addToCartErrorLayer"><p></p></div> 
			</div>
		</div>
						
		<div class="orderDetailsTable hidden-xs">	
			<table id="ordersTable" class="table table-bordered table-striped sorting-table ">
				<thead>
				  <tr>
									<!-- Adding for AAOL-3609 -->
										  	<th class="no-sort" style="width: 60px"><spring:message code="orderDetailPage.orderData.item"/></th>
									<!-- Adding for AAOL-3609 -->		
											
					<th class="no-sort" style="width: 120px"><spring:message code="orderDetailPage.orderData.product"/></th>
											<th class="no-sort quantity-col"><spring:message code="orderDetailPage.orderData.quantity"/></th>
					<th class="no-sort" style="width: 44px"><spring:message code="orderDetailPage.orderData.itemPrice"/></th>
					<th class="no-sort" style="width: 52px"><spring:message code="orderDetailPage.orderData.Shipping"/></th>
					<th class="no-sort" style="width: 39px"><spring:message code="orderDetailPage.orderData.expireDate"/></th>
					<th class="no-sort" style="width: 70px"><spring:message code="orderDetailPage.orderData.estDeliv"/></th>
					<th class="no-sort" style="width: 39px"><spring:message code="orderDetailPage.orderData.status"/></th>
					<th class="no-sort total-price-cell"><spring:message code="orderDetailPage.orderData.Total"/></th>
				  </tr>
				</thead>
				<c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
				  <c:choose>	
				   <c:when test="false">			
									<tbody>			
							<c:forEach items="${entry.scheduleLines}" var="scheduleLine" varStatus="schLineCounter">
								<order:orderDetailsItem entry="${entry}" isSplitOrder="true" scheduleLine="${scheduleLine}"  entryIndex="${entryCounter.count}" 
							schLineIndex="${schLineCounter.count}"   isMddSite="${isMddSite}" orderType="${orderData.orderType}" displayDisputeOption="${orderData.displayDisputeOption}"/>
						</c:forEach>
								</tbody>
					</c:when>
					<c:otherwise>
						<order:orderDetailsItem entry="${entry}" isSplitOrder="false" isMddSite="${isMddSite}" entryIndex="${entryCounter.count}"  orderType="${orderData.orderType}" 
					displayDisputeOption="${orderData.displayDisputeOption}"/>
				</c:otherwise>	
					</c:choose>	
				</c:forEach>		
		  </table>
	</div>
								
 <!-- Table collapse for mobile device-->
                                                        <c:set
			value="${not (isMddSite && fn:contains('ZDELZRE', orderData.orderType))}"
			var="displayShipping" />
		<c:set value="${(empty entry.materialEntered) ? false : true}"
			var="isMaterialEntPresent" />
		<c:set value="${(empty entry.materialNumber) ? false : true}"
			var="isMaterialNumPresent" />
      <div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
	   <table id="ordersTable" class="table table-bordered table-striped tabsize">
	   	<thead>
	      	 <tr>
	      		<th class="text-left"><spring:message code="orderDetailPage.orderData.item" /></th>
	      	</tr>
			</thead>
									
												
					<tbody>
					<c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
						<tr>
							<td class="panel-title text-left">
									<a data-toggle="collapse" data-parent="#accordion" href="#collapse${entryCounter.count}" class="ref_no toggle-link panel-collapsed">
									<span class="glyphicon glyphicon-plus"></span> ${entry.product.code}</a>
									<div id="collapse${entryCounter.count}" class="panel-collapse collapse">
									<div class="panel-body">
									<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.product" />
									</div>
									<div>
										<p>
											<c:choose>
												<c:when test="${isMaterialNumPresent}">
															<b><i><spring:message
																		code="orderDetailPage.orderData.Productmaterial" /><span
																	class="strong">${entry.materialNumber}</span></i></b>
												</c:when>
												<c:otherwise>
												
															<input type="hidden"
																value="${entry.product.saleableInd && entry.salesRepDivisionCompatible}"
														class="saleableIndicator" id="${entry.product.code}" qty="${entry.quantity}"/>
													<c:choose>
														<c:when test="${empty entry.product.url}">
															<h4 title="${entry.product.name}">${entry.product.name}</h4>
														</c:when>
														<c:otherwise>
															<h4 title="${entry.product.name}">
																<p>${entry.product.name}</p>
															</h4>
														</c:otherwise>
													</c:choose>
													<p class="jnjID">
														<spring:message code="orderDetailPage.orderData.jnjID" />
														<span class="strong">${entry.product.baseMaterialNumber}</span>
													</p>
												</c:otherwise>
											</c:choose>
										</p>
										<p>
											<c:if test="${not empty entry.contractNumber}">
												<p>
													<span class="smallFont"><spring:message
															code="orderDetailPage.orderData.contract" /></span>
													${entry.contractNumber}
												</p>
											</c:if>
										</p>
										<!-- Adding for AAOL-3648 -->
								<p>
								<c:url value="/order-history/order/disputeItem?orderCode=${orderData.code}&productCode=${entry.product.baseMaterialNumber} 
								&contractNum=${entry.contractNumber}&totalPrice=${entry.totalPrice.value}" var="disputeItemUrl" />
									<a href="${disputeItemUrl}" class="smallFont disputeItem" orderCode="${orderData.code}" productCode="${entry.product.baseMaterialNumber}" 
										contractNum="${entry.contractNumber}" totalPrice="${entry.totalPrice.value}" >
									<spring:message code="orderDetailPage.orderData.disputeThisItem" /></a>
								</p>
							<!-- Adding for AAOL-3648 -->
										<p>
											<c:if test="${not empty entry.hazmatInd}">
												<div>
													<p>
														<spring:message
															code="orderDetailPage.orderData.HazmatCode" />
														${entry.hazmatInd}
												</div>
											</c:if>
										</p>
									</div>
								</div>

								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.quantity" />
									</div>
									<div>
										<c:if test="${not isMaterialNumPresent}">
										${entry.quantity} (${entry.product.numerator} &nbsp; ${entry.product.salesUnit})
											</c:if>
									</div>
								</div>

								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.itemPrice" />
									</div>
									<div>
										<c:choose>
											<c:when test="${'ZLZ' eq orderType}">
												<span class="txtFont">-</span>
											</c:when>
											<c:when test="${not empty entry.basePrice.formattedValue}">
												${entry.basePrice.formattedValue}
													</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.Total" />
									</div>
									<div>
										<c:choose>
											<c:when test="${not empty entry.totalPrice.formattedValue}">
														${entry.totalPrice.formattedValue}
														</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.Shipping" />
									</div>
									<div>
										<c:if test="${displayShipping}">
											<c:choose>
												<c:when test="${not empty entry.shippingMethod}">
														${entry.shippingMethod}
														</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</c:if>
									</div>
								</div>
								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.estDeliv" />
									</div>
									<div>
										<c:choose>
											<c:when test="${not empty entry.expectedDeliveryDate}">
												<c:choose>
													<c:when test="${'ZLZ' eq orderType}">
														<span class="txtFont">-</span>
													</c:when>
													<c:otherwise>
														<fmt:formatDate value="${entry.expectedDeliveryDate}"
															pattern="${dateformat}" />
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<span class="txtFont">&nbsp;</span>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="sub-details-row">
									<div style="font-family: jnjlabelfont; font-size: 10px">
										<spring:message code="orderDetailPage.orderData.status" />
										</
									</div>
									<div>
										<p>${entry.status}</p>
										<p>
											<spring:message code="orderDetailPage.orderData.tracking" />
										</p>
										<p>
											<a><c:if test="${isMddSite}">
													<c:choose>
														<c:when
															test="${not empty orderData.shippingTrackingInfo }">
															<c:choose>
																<c:when
																	test="${not empty orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}">
																	<c:forEach
																		items="${orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}"
																		var="data">
																		<c:choose>
																			<c:when test="${not empty data.trackingId}">
																				<c:choose>
																					<c:when test="${data.trackingUrl eq '#'}">
																						<p class="smallFont">${data.trackingId}</p>
																					</c:when>
																					<c:otherwise>
																						<p class="smallFont">
																							<a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
																						</p>
																					</c:otherwise>
																				</c:choose>
																			</c:when>
																			<c:otherwise>
																				<p class="smallFont">
																							<label:message
																								messageCode="orderDetailPage.orderData.notAvailable" />
																				</p>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</c:when>
																<c:otherwise>
																	<p class="smallFont">
																				<label:message
																					messageCode="orderDetailPage.orderData.notAvailable" />
																	</p>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<p class="smallFont">
																		<label:message
																			messageCode="orderDetailPage.orderData.notAvailable" />
															</p>
														</c:otherwise>
													</c:choose>
												</c:if></a>
										</p>
									</div>
								</div>
							</div>
						</div></td>
					</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
	<!-- Adding for AAOL-3469 start-->	
		<div class="row basecontainer fee-toggle-container">
			<table class="total-summary-table">
				<tr>
				<c:choose>
					<c:when test="${'ZLZ' eq orderData.orderType}">
						<td class="total-summary-label" style="font-size:10px !important"><spring:message
							code="orderDetailPage.orderData.subTotal" /></td>
					</c:when>
					<c:otherwise>
						<td class="total-summary-label" style="font-size:10px !important"><spring:message
							code="orderDetailPage.orderData.subTotal" /></td>
						<td class="total-summary-cost totalrps">
					${orderData.totalNetValue.formattedValue}</td>
					</c:otherwise>
				</c:choose>	
				</tr>
			<c:if test="${isMddSite}">	
				<c:if test="${orderData.totalFees.value ne '0.0'}">
				<tr>
				
					<td class="total-summary-label" style="width: 25px;"><spring:message
							code="orderDetailPage.total.fee" /></td>
					<td class="total-summary-cost totalrps">${orderData.totalFees.formattedValue}</td>
					<td class="toggle-fee"><a data-toggle="collapse"
						class="toggle-fee-link toggle-link panel-collapsed"
						href="#fee-mobile-collpase${globalCount}"><span
							class="glyphicon glyphicon-chevron-up"></span></a></td>
				</tr>
				<tr class="fee-panel">
					<td colspan='2'>
						<table id="fee-mobile-collpase${globalCount}"
							class="fee-collpase-table total-summary-table panel-collapse collapse" style="font-size:10px !important">
							<c:if test="${orderData.totalDropShipFee.value ne '0.0'}">
							<tr>
								<td class="total-summary-label" style="width: 25px;font-size:10px !important"><spring:message code="orderDetailPage.dropship.fee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important">${orderData.totalDropShipFee.formattedValue}</td>
							</tr>
							</c:if>
							<c:if test="${orderData.totalminimumOrderFee.value ne '0.0'}">
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="orderDetailPage.minOrderqty.fee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important">${orderData.totalminimumOrderFee.formattedValue}</td>
							</tr>
							</c:if>
							<c:if test="${orderData.totalFreightFees.value ne '0.0'}">
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="orderDetailPage.orderData.freightHandlingFee"/></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important">${orderData.totalFreightFees.formattedValue}</td>
							</tr>
							</c:if> 
						</table>
					</td>
				</tr>
			</c:if> 	
			</c:if>
			<tr class="summary-bline ">
			<c:choose>
					<c:when test="${'ZLZ' eq orderData.orderType}">
					<td class="total-summary-label"><spring:message
								code="orderDetailPage.orderData.taxes" /></td>
					</c:when>
					<c:otherwise>
							<td class="total-summary-label"><spring:message
									code="orderDetailPage.orderData.taxes" /></td>
							<td class="total-summary-cost totalrps">${orderData.totalTax.formattedValue}</td>
					</c:otherwise>
			</c:choose>	
			</tr>
				<tr class="total-price-row">
					<td class="total-summary-label"><spring:message
							code="orderDetailPage.orderData.orderTotal" /></td>
					<td class="total-summary-cost totalsum">${orderData.totalPrice.formattedValue}</td>

				</tr>
			</table>

		</div>
		<!-- Adding for AAOL-3469 end-->	
		<div class="row">
		<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
			<div class="float-right-to-none marginbtm30">
					<button id="orderDetailAddToCart1" orderCode ="${orderData.code}" class="btn btnclsactive orderDetailAddToCart" ><spring:message code="orderDetailPage.addOrdertocart"/></button>
				</div>
		</div>
		</div>


		<!--Accordian Ends here -->
		<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
		<div  id="contractPopuppage">
			<!-- Modal -->
			<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
				<div class="modal-dialog modalcls">
					<div class="modal-content popup">
						<div class="modal-header">
							<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
							<h4 class="modal-title selectTitle"><spring:message code="contract.page.addprod"/></h4>
						</div>
						<div class="modal-body">
							<div class="panel panel-danger">
								<div class="panel-heading">
									<h4 class="panel-title">
										<table class="contract-popup-table">
										<tr>
											<td><div class="glyphicon glyphicon-ok"></div></td>
											<td><div class="info-text"><spring:message code="contract.page.infotext"/></div></td>															
										</tr>
										</table>
									</h4>
								</div>
							</div>													
							<div><spring:message code="contract.page.msg"/></div>
							<div class="continueques"><spring:message code="contract.page.continue"/></div>
						</div>											
						<div class="modal-footer ftrcls">
						<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart"><spring:message code="cart.common.cancel"/></a>
						<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart" ><spring:message code="contract.page.accept"/></button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Add to cart Modal pop-up to identify  contract or non contract end-->

	</div>

<!-- AAOL-3681 -->
	<div class="modal fade jnj-popup-container" id="dispute-success-popup" role="dialog" data-firstLogin='true'>
          <div class="modal-dialog modalcls">
           <div class="modal-content popup">
            <div class="modal-header">
            </div>
            <div class="modal-body">
            <div>            
            	<div class="panel-success">
		              <div class="panel-heading">
			              <h4 class="panel-title">
				              <span class="glyphicon glyphicon-ok"></span>
				              <span id="dispute-success-info"></span>
			              </h4>
		              </div>
	            </div>
	            </div>
            </div>           
           <div class="modal-footer modal-footer-style">

				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-dismiss="modal">
					<spring:message code="cart.common.ok" />
				</button>
			</div>
           </div>
          </div>
         </div>	
<!-- AAOL-3681 -->         	

</template:page>
<!-- AAOL-6377 -->
<div id="replacement-line-item-holder" style="display: none;"></div>
<div class="modal fade jnj-popup" id="error-detail-popup"
			role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close" /></button>
						<h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
					</div>
					<form:form method="post" action="javascript:;">
						<div class="modal-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="panel-group">
										<div class="panel panel-danger">
											  <div class="panel-heading">
												<h4 class="panel-title cart-error-msg">
												<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="homePage.errordetails.addfailed" /></span>
												</h4>
											  </div>
										</div>  
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="scroll error-content" style="font-weight:bold">
									</div>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
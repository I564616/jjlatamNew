<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>

<!-- Start - Product detail Modal -->
<div class="modal fade jnj-popup" id="product-detail-popup"	role="dialog">
	<div class="modal-dialog modalcls modal-lg">
		<!-- Modal content-->

		<div class="modal-content">
			<div class="modal-header">
				<div class="row close-btn-holder">
					<div class="col-lg-12">
						<button type="button" class="close clsBtn pull-right"
							data-dismiss="modal"><spring:message code='popup.close'/></button>
					</div>
				</div>
				<div class="pageBlock ${'MDD' eq JNJ_SITE_NAME ? 'prodDetailDescMd' : 'prodDetailDescCp'}"></div>
				<c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}" var="isMddSite"></c:set>
				
				
				<div class="row">
					<div class="modal-title col-lg-6">${product.displayName}</div>
					<div class="col-lg-5">
						<div>
							<label><spring:message code="product.detail.basic.productCode"/></label> ${product.baseMaterialNumber}	</div>
						<div>
								<c:choose>
									<c:when test="${isMddSite}">
										<label><spring:message code="product.detail.basic.gtin" /></label>${product.gtin}
									</c:when>
									<c:otherwise>
										<label><spring:message code="product.detail.basic.upc" /></label>${product.upc}
									</c:otherwise>
								</c:choose>
							</div>
					</div>
				</div>
			</div>
			<div class="modal-body">
				<div class="row">
					<div	class="col-lg-3 col-md-3 col-sm-4 col-xs-12 product-img-holder">
					
						<product:productPrimaryImage product="${product}" format="product" />
					</div>
					
		
					
					
					<div class="col-lg-9 col-md-9 col-sm-8 col-xs-12">
						<table id="product-details-popup-table"
							class="table table-bordered table-striped">
							<thead>
								<tr>
									<th class="text-left no-sort"><spring:message code="product.detail.specification.specification"/></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="product-popup-cell">
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.specification.franchise"/></td>
												<td class="product-details-popup-content">${product.mddSpecification.franchise}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.division"/></td>
												<td class="product-details-popup-content">${product.mddSpecification.division}</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="product-popup-cell">
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.specification.status"/></td>
												<td class="product-details-popup-content">${product.status}</td>
											</tr>
										</table>
									</td>
								</tr>
								<%-- <tr>
									<td class="product-popup-cell">
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.kitComponentName"/></td>
												<td class="product-details-popup-content"><c:forEach items="${product.mddSpecification.kitComponentNames}" var="kitName" varStatus="status">
																<span class="contentText">${kitName}</span>
																<c:if test="${!status.last}" >,</c:if>  
																		</c:forEach>
												</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.kitComponentDescription"/></td>
												<td class="product-details-popup-content"><c:forEach items="${product.mddSpecification.kitComponentDesc}" var="kitDescription" varStatus="status">
								${kitDescription}<c:if test="${!status.last}" >,</c:if>  
    </c:forEach></td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.kitComponetUnit"/></td>
												<td class="product-details-popup-content"><c:forEach items="${product.mddSpecification.kitComponentUnits}" var="kitUnit" varStatus="status">
								${kitUnit}
								<c:if test="${!status.last}" >,</c:if>  
						</c:forEach></td>
											</tr>
										</table>
									</td>
								</tr> --%>
								<tr>
									<td class="product-popup-cell">
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.eachGtin"/></td>
												<td class="product-details-popup-content">${product.mddSpecification.eachGtin}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.caseGtin"/></td>
												<td class="product-details-popup-content">${product.mddSpecification.caseGtin}</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="product-popup-cell">
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.unitOfMeasure"/></td>
												<td class="product-details-popup-content"><c:if test="${product.mddSpecification.salesUom != null}">
			<span class="contentText">${product.mddSpecification.salesUom.name}
				(${product.mddSpecification.salesUom.numerator} &nbsp;
				${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})</span>
			<p><spring:message code="browseandsearch.sellqty"/></p>
		</c:if></td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.deliveryUnitOfMeasure"/></td>
												<td class="product-details-popup-content"><c:if
			test="${product.mddSpecification.deliveryUom != null && product.mddSpecification.salesUom != null}">
			<div>
				<c:set value="${product.mddSpecification.deliveryUom}"	var="deliveryUom" />
				<c:set value="${product.mddSpecification.salesUom}" var="salesUom" />
				${deliveryUom.packagingLevelQty/salesUom.packagingLevelQty}
				${product.mddSpecification.salesUom.name}
				(${(deliveryUom.packagingLevelQty/salesUom.packagingLevelQty) * product.mddSpecification.salesUom.numerator}
				&nbsp; ${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})
			</div>
			<div><spring:message code="browseandsearch.shipqty"/></div>
		</c:if></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="product-popup-cell">
									<c:set value="${product.mddSpecification.deliveryUom}" var="deliveryUom" />
										<table class="product-popup-celldata">
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.depth"/></td>
												<td class="product-details-popup-content">${deliveryUom.depth} ${deliveryUom.depthLinearUom}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.height"/></td>
												<td class="product-details-popup-content">${deliveryUom.height} ${deliveryUom.heightLinearUom}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.width"/></td>
												<td class="product-details-popup-content">${deliveryUom.width} ${deliveryUom.widthLinearUom}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.volume"/></td>
												<td class="product-details-popup-content">${deliveryUom.volume} ${deliveryUom.volumeLinearUom}</td>
											</tr>
											<tr>
												<td class="product-details-popup-label"><spring:message code="product.detail.mddSpecification.weight"/></td>
												<td class="product-details-popup-content">${deliveryUom.weight} ${deliveryUom.weightLinearUom}</td>
											</tr>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
						<!-- KIT ADDED  -->
			        <c:set var="disableAddToCart">true</c:set>
						  <c:if test="${product.kitType == 'ORTHO'}">
   					 			<c:set var="disableAddToCart">false</c:set>
    					</c:if>
						<c:if test="${product.mddSpecification.kitData != null}">
							<table id="product-kit-table" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th colspan="3" class="headertxt no-border text-uppercase">Kit Components</th>
									<tr>
								</thead>
								<tbody>
									<c:forEach var="kitComponent" items="${product.mddSpecification.kitData}" varStatus="loop">
										<tr>
										<input type="hidden" id="kitCompCode_${loop.index}" value="${kitComponent.productCode}" index="${loop.index}" />
									    <c:url value="${kitComponent.productImageUrl}" var="productUrl"/>
											<td class="no-border valign-top">
												<div class="table-row">
													<div class="table-cell">
														<c:choose>
													<c:when test="${empty productUrl}">
														<product:productPrimaryImage product="${kitComponent}"	format="thumbnail" />
													</c:when>
													<c:otherwise>
														<a href="${productUrl}">
															<product:productPrimaryImage product="${kitComponent}"	format="thumbnail" />
														</a>	
													</c:otherwise>
													</c:choose>
													</div>
													<div class="table-cell valign-top text-left">
														<p><a href="${productUrl}" class="prod-info">${kitComponent.productName}</a></p>
														 <div class="prod-info">${kitComponent.productCode}</div>
													</div>
												</div>
											</td>
											<td class="no-border">
											<fmt:parseNumber var="qty" integerOnly="true"   type="number" value="${kitComponent.productQty}" />
												 <input type="hidden" id="kitCompQty_${loop.index}" index=${loop.index} value="${qty}" />
											${qty}
											</td>
											<td class="no-border">
											<c:if test="${disableAddToCart ne 'true'}">
												<input type="button" class=" btn btnclsactive addToCartKitProduct primarybtn ${disableAddToCart}"	value="<spring:message code="product.detail.addToCart.addToCart"/>"	id="${kitComponent.productCode}" index=${loop.index} />
												</c:if>
												
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
						<!-- KIT ADDED  -->
					</div>
				</div>
			</div>
			<div class="row close-btn-holder">
				<div class="col-lg-12 product-dtail-close-holder">
					<button type="button" class="product-dtail-close" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End - Product detail Modal -->








































<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="product"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<c:url value="/p/${product.baseMaterialNumber}/genarateBarCode" var="barcodeURL" />
<c:url value="/p/${product.baseMaterialNumber}/generateProductDetailsPDF" var="productDetailURL" /> 

<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<product:productDetailsJavascript />
	</jsp:attribute>
	<jsp:body>
	<div class="productdetailspage">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="dwnld-barcode-btns pull-right">
			<p><strong><spring:message code="product.search.download"/>:</strong> <a href="${productDetailURL}" id="dwn-btn"><spring:message code="category.pdf"/></a></p>
			<p><a href="${barcodeURL}" id="barcode-btn"><spring:message code="product.detail.generateBarCode"/></a></p>
		</div>
	 		    
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0 boxshadow"	id="productImageDetails">
				<div class="col-lg-5 col-sm-5 col-md-5 col-xs-12 productImageGallery">
					<img  src="${productMainImage}" id="productMainImg" alt ="${imgAlttext}"/>
					<!-- change displaying disclaimer message - Start  -->
						<c:choose>
							<c:when test="${not empty productMainImage}">
								<!-- Start changes for the story AEKL-973  -->
									<c:choose>
										<c:when test="${not empty galleryImages}">
											<div class="imageThumbnails col-lg-12 col-sm-12 col-md-12 col-xs-12">
											<div class="col-lg-2 col-sm-2 col-md-2 col-xs-2 backward">
												<button>&lt;</button>
											</div>
												<c:forEach items="${galleryImages}" var="galleryImage" varStatus="count">
													<c:forEach var="entry" items="${galleryImage}">
														<div class="col-lg-2 col-sm-2 col-md-2 col-xs-2 img image_${count.count}">
															<img src="${entry.value.url}" class="thumbnails" alt ="${imgAlttext}" id="img_${count.count}"/>
														</div>
													</c:forEach>
												</c:forEach>
												<div class="col-lg-2 col-sm-2 col-md-2 col-xs-2 forward right">
													<button>&gt;</button>
												</div>
											</div>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								<!-- END changes for the story AEKL-973  -->
								<p class="img_disclaimer"><spring:message code="product.detail.disclaimer.message"/></p>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<!-- change displaying disclaimer message - END  -->
					</div>
					<c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}"	var="isMddSite"></c:set>
					<div class="col-lg-7 col-sm-7 col-md-7 col-xs-12 productspanel">
						<div class="top-content">
							<div id="product_title">${product.descriptionText}<%-- ${product.name} --%></div>
							<div class="top-content-end">
								<div class="top-content-end-left left">
									<div id="product_code">
										<span class="no-bold-text"><spring:message
											code="product.detail.basic.productCode" />:</span>
											 ${product.baseMaterialNumber}
									</div>
									<div id="gtin_status"> 
										<c:choose>
											<c:when test="${isMddSite}">
												<c:if test="${not empty product.mddSpecification.caseGtin}">
													<spring:message code="product.detail.basic.gtin" />:<span>
													<c:forEach items="${product.mddSpecification.caseGtin}" var="casegtin" varStatus="status">
												</c:forEach>
												<c:out value="${product.mddSpecification.caseGtin[0]}"/>
												</span>
												</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${not empty product.upc}">
													<spring:message code="product.detail.basic.upc" />:<span>${product.upc}</span>
												</c:if>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="top-content-end-right right">
									<div id="price">
										<c:choose>
										<c:when test="${not empty product.contract}">
											${product.contract}
										</c:when>
										<c:otherwise>
											${product.price.formattedValue}							
										</c:otherwise>
									</c:choose>
									</div>
								</div>
								<div class="clear"></div>
							</div>
						</div>
						<div class="middle-content">
							  
							<form:form id="prodAddToCartForm" class="prod_list_form" action="<c:url value="/cart/add"/>" method="post"></form:form>  
							 <c:set value="9999" var="index" />
							<input type="checkbox" hidden="true" title="check"
							class="selprod" id="${product.baseMaterialNumber}"
							checked="checked" style="display: none" index="${index}" />
							<div class="addtocartButtonBox">
							
							<div class="display-table-row quantity-add-to-cart pull-left">
								<div class="display-table-cell">
									<label style="dislay: inline-block; line-height: 35px"><spring:message
											code="product.detail.addToCart.quantity" /> &nbsp;</label>
									<input class="ProdTbox cat3Input form-control" title="check"
										id="qty_${index}" type="text" value="0" name="qty">
								</div>
								<div class="display-table-cell qty-table-cell">
									<div id="sub-qty" class="qty-box"> <button>&mdash;</button> </div>
								</div>
								<div class="display-table-cell qty-table-cell">
									<div id="add-qty" class="qty-box"> <button>+</button> </div>
								</div>											
							</div>
							
							<c:if
								test="${(product.discontinued || !product.saleableInd || !product.salesRepCompatibleInd)}">
							<c:set value="disabled" var="disabled" />
							<c:set value="buttonDisable" var="disableAddToCart" />
							</c:if>
								<div class="display-table-cell pull-right">
								<input type="button" class="btnclsactive redButton primarybtn"
									value="<spring:message code="product.detail.addToCart.addToCart"/>"
									id="addToCart" />
								</div>
							</div>
							<div class="clear"></div>
						</div>
						<div class="middle-content">
							<p class="sub-head"><%-- ${product.descriptionText} --%>
						</p>
							<p class="sub-head2">${product.description}</p>
						</div>
						
						<div class="middle-content no-border">
							<p class="overview-label">
							<c:if test="${product.shortOverview != null}">
								<spring:message code="product.detail.shortOverviewName" />
							</c:if>
						</p>
							<p class="overview-desc">${product.shortOverview}
						</p>
						</div>
						<div class="bottom-content">
							<div class="franchiseImageBox pull-right">								
								 <img  src="${franchiseLogo}" alt ="${imgAlttext}"/>  									
							</div>
						</div>
							
					</div>	
				</div>	
	
	<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div id="contractPopuppage">
				<div class="modal fade" id="contractpopup" role="dialog"
					data-firstLogin='true'>
					<div class="modal-dialog modalcls">
						<div class="modal-content popup">
							<div class="modal-header">
							  <button type="button" class="close clsBtn" data-dismiss="modal">
									<spring:message code="account.change.popup.close" />
								</button>
							  <h4 class="modal-title selectTitle">
									<spring:message code="contract.page.addprod" />
								</h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-danger">
									<div class="panel-heading">
										<h4 class="panel-title">
										<table class="contract-popup-table">
											<tr>
											<td><div class="glyphicon glyphicon-ok"></div></td>
											<td><div class="info-text">
													<spring:message code="contract.page.infotext" />
												</div></td>														
											</tr>
										</table>
										</h4>
									</div>
								</div>													
								<div>
									<spring:message code="contract.page.msg" />
								</div>
								<div class="continueques">
									<spring:message code="contract.page.continue" />
								</div>
							</div>											
							<div class="modal-footer ftrcls">
								<a href="#" class="pull-left canceltxt" data-dismiss="modal"
									id="cancel-btn-addtocart"><spring:message
										code="cart.common.cancel" /></a>
								<button type="button" class="btn btnclsactive"
									data-dismiss="modal" id="accept-btn-addtocart">
									<spring:message code="contract.page.accept" />
								</button>
							</div>
						</div>
					</div>
				</div>
		</div>	
		
		<!-- KIT ADDED  -->
			<c:set var="disableAddToCart">true</c:set>
			  	<c:if test="${product.kitType == 'ORTHO'}">
					<c:set var="disableAddToCart">false</c:set>
 				</c:if>
    					
    	
    	<div
				class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0 boxshadow">
			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0">
			<c:choose>
				<c:when test="${not empty product.brandDescription}">
					<div class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab active threePdpTabs lineHeight40" id="tab_1">
						<spring:message code="product.detail.aboutTheBrandTab" /> 
					</div>
					<div class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab threePdpTabs lineHeight40" id="tab_2">
						<spring:message code="product.detail.productDetailsTab" />
					</div>
					<div class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab threePdpTabs" id="tab_3">
						<spring:message code="product.detail.additionalInformationTab" /> 
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab active twoPdpTabs" id="tab_2">
						<spring:message code="product.detail.productDetailsTab" />
					</div>
					<div class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab twoPdpTabs" id="tab_3">
						<spring:message code="product.detail.additionalInformationTab" /> 
					</div>
				</c:otherwise>
				</c:choose>
			</div>
			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 tab-description-box">
					<c:if test="${not empty product.brandDescription}">
						<div id="desc1" class="tab_description active">
								${product.brandDescription} 
						</div>
					</c:if>
					<div id="desc2" class="tab_description active">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
							<c:if test="${not empty product.classAttributeFeatureMap}">
								<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
									<table class="product-attributes">
										<thead>
											<tr>
												<th colspan="2"><spring:message code="product.detail.attributes" /></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="structuredClassification" items="${product.structuredClassifications}">
												<tr>
													<td class="sublevel" colspan="2">${structuredClassification.key}</td>
												</tr>
												<c:forEach var="rootFeature" items="${structuredClassification.value}">
													<tr>
														<td>${rootFeature.key}</td>
														<td>${rootFeature.value}</td>
													</tr>
												</c:forEach>
											</c:forEach>
										</tbody>
									</table>
                                </div>
							</c:if>
							<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
								<table class="product-specifications">
									<thead>
										<tr>
											<th colspan="2"><spring:message code="product.detail.product.specifications" /></th>
										</tr>
									</thead>
									<tbody>
									 	<c:if test="${not empty product.baseMaterialNumber}"> 
											<tr>
												<td><spring:message code="product.detail.basic.productCode" /></td>
												<td>${product.baseMaterialNumber}</td>
											</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.division}"> 
										<tr>
											<td><spring:message	code="product.detail.mddSpecification.division" /></td>
											<td>${product.mddSpecification.division}</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.kitComponentNames}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.kitComponentName"/></td>
											<td><c:forEach items="${product.mddSpecification.kitComponentNames}" var="kitName" varStatus="status">
														<span class="contentText">${kitName}</span>
															<c:if test="${!status.last}" >,</c:if>  
												</c:forEach>
											</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.kitComponentDesc}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.kitComponentDescription"/></td>
											<td><c:forEach items="${product.mddSpecification.kitComponentDesc}" var="kitName" varStatus="status">
														<span class="contentText">${kitName}</span>
															<c:if test="${!status.last}" >,</c:if>  
												</c:forEach>
											</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.kitComponentUnits}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.kitComponetUnit"/></td>
											<td><c:forEach items="${product.mddSpecification.kitComponentUnits}" var="kitName" varStatus="status">
														<span class="contentText">${kitName}</span>
															<c:if test="${!status.last}" >,</c:if>  
												</c:forEach>
											</td>
										</tr>
										</c:if>
										<!-- AEKL-3 Start-->
										<c:if test="${not empty product.mddSpecification.caseGtin}"> 
										<tr>
											<td><spring:message	code="product.detail.mddSpecification.caseGtin" /></td>
											<td>
												<c:forEach items="${product.mddSpecification.caseGtin}" var="casegtin" varStatus="status">
													${casegtin}
												<c:if test="${!status.last}">,&nbsp;</c:if>	
												</c:forEach>
											</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.eachGtin}"> 
										<tr>
											<td><spring:message	code="product.detail.mddSpecification.eachGtin" /></td>
											<td>
												<c:forEach items="${product.mddSpecification.eachGtin}" var="eachgtin" varStatus="status">
														${eachgtin}
														<c:if test="${!status.last}">,&nbsp;</c:if>
												</c:forEach>
											</td>
										</tr>
										</c:if>
										<!-- AEKL-3 Ends-->
										<c:if test="${not empty product.status}"> 
										<tr>
											<td><spring:message code="product.detail.specification.status" /></td>
											<td>${product.status}</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.salesUom}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.unitOfMeasure" /></td>
											<td>
												<c:if test="${product.mddSpecification.salesUom != null}">
														<p class="contentText">${product.mddSpecification.salesUom.name}
														(${product.mddSpecification.salesUom.numerator} &nbsp;
														${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})</p>
														<p>
															<spring:message code='popup.product.sellQty' />
														</p>
												</c:if>				
											</td>
										</tr>
										</c:if>
										<c:if test="${not empty product.mddSpecification.deliveryUom && not empty product.mddSpecification.salesUom}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.deliveryUnitOfMeasure" /></td>
										<td>
											<c:if test="${product.mddSpecification.deliveryUom != null && product.mddSpecification.salesUom != null}">
												<div>
													<c:set value="${product.mddSpecification.deliveryUom}"
																var="deliveryUom" />
													<c:set value="${product.mddSpecification.salesUom}"
																var="salesUom" />
													${deliveryUom.packagingLevelQty/salesUom.packagingLevelQty}
													${product.mddSpecification.salesUom.name}
													(${(deliveryUom.packagingLevelQty/salesUom.packagingLevelQty) * product.mddSpecification.salesUom.numerator}
													&nbsp; ${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})
												</div>
												<div>
													<spring:message code='popup.product.shipQty' />
												</div>
											</c:if>
										</td>
										</tr>
										</c:if>
										<c:if test="${not empty deliveryUom.depth}"> 
										<tr>
											<td><spring:message	code="product.detail.mddSpecification.depth" /></td>
											<td>${deliveryUom.depth} ${deliveryUom.depthLinearUom}</td>
										</tr>
										</c:if>
										<c:if test="${not empty deliveryUom.height}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.height" /></td>
											<td>${deliveryUom.height} ${deliveryUom.heightLinearUom}</td>
										</tr>
										</c:if>
										<c:if test="${not empty deliveryUom.width}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.width" /></td>
											<td>${deliveryUom.width} ${deliveryUom.widthLinearUom}</td>
										</tr>
										</c:if>
										<c:if test="${not empty deliveryUom.volume}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.volume" /></td>
											<td>${deliveryUom.volume} ${deliveryUom.volumeLinearUom}</td>
										</tr>
										</c:if>
										<c:if test="${not empty deliveryUom.weight}"> 
										<tr>
											<td><spring:message code="product.detail.mddSpecification.weight" /></td>
											<td>${deliveryUom.weight} ${deliveryUom.weightLinearUom}</td>
										</tr>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				
				<div id="desc3" class="tab_description">
					<c:forEach items="${productDocumentsList}" var="productDocuments" varStatus="count">
						<p class="additional_info_tab"> <a href="${productDocuments.urlLink}" target="_blank"> <em class="fa fa-external-link"></em>${productDocuments.name}</a></p> 
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	
	<cms:pageSlot position="RelatedProducts" var="comp" >
		<cms:component component="${comp}" />
	</cms:pageSlot>
	
	<%--Added for AFFG-1 --%>
	<input type="hidden" value="productDetailPage" id="screenName" />
	<input type="hidden" value="${product.baseMaterialNumber}" id="product_baseMaterialNumber" />
	<input type="hidden" value="${numberOfProductPerSlide}" id="numberOfProductPerSlide" />
	<div class="scrollBox"></div>
	<div class="scrollBoxMob"></div>
	<div class="row body-margin-bottom"></div>
	<!-- AAOL-6378 -->
	<div id="replacement-line-item-holder" style="display: none;"></div>
	<div id="popupBoxError"></div>
	</jsp:body>

</template:page>
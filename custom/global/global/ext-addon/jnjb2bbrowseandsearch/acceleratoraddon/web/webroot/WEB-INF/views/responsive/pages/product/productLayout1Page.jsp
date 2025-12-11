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
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<product:productDetailsJavascript />
	</jsp:attribute>
	<jsp:body>
	<div class="productdetailspage">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	  <product:productPrimaryAndImageCarousel product="${product}"	format="product" galleryImages="${galleryImages}" />	    
	<div id="product-detail-html" class="pageBlock ${'MDD' eq JNJ_SITE_NAME ? 'prodDetailDescMd' : 'prodDetailDescCp'}">
	    <c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}"	var="isMddSite"></c:set>
	   <div class="col-lg-6 col-sm-6 col-md-6 col-xs-12 productspanel">
			<div style="margin: 30px 15px">
				<div id="product_title">${product.name}</div>
					<div id="product_code">	<span><spring:message code="product.detail.basic.productCode"/></span>${product.baseMaterialNumber}</div>
					 <div id="gtin_status">
						 <c:choose>
						<c:when test="${isMddSite}">
							<span><spring:message code="product.detail.basic.gtin" /></span>${product.gtin}
						</c:when>
						<c:otherwise>
							<span><spring:message code="product.detail.basic.upc" /></span>
							${product.upc}
						</c:otherwise>
					</c:choose>
					</div>
					 <div class="pricetagTxt">
							<c:choose>
								<c:when test="${not empty product.contract}">
									${product.contract}
								</c:when>
								<c:otherwise>
									${product.price.formattedValue}							
								</c:otherwise>
							</c:choose> 
					
					</div>
					<c:if test="${(product.discontinued || !product.saleableInd || !product.salesRepCompatibleInd)}">
					<c:set value="disabled" var="disabled" />
					<c:set value="buttonDisable" var="disableAddToCart" />
					</c:if>  
					<form:form id="prodAddToCartForm" class="prod_list_form"		action="<c:url value="/cart/add"/>" method="post"></form:form>  
					 <c:set value="9999" var="index" />
					<input type="checkbox" hidden="true" title="check" class="selprod"	id="${product.baseMaterialNumber}" checked="checked" style="display: none"
					index="${index}" />
						<div class="display-table-row pull-right"
							style="margin-bottom: 30px">
							<div class="display-table-cell">
								<label style="dislay: inline-block; line-height: 35px"><spring:message code="product.detail.addToCart.quantity"/>&nbsp;</label>
								<input class="ProdTbox cat3Input form-control" type="text"	value="0" title="check" id="qty_${index}" maxlength="6"	name="qty" /> 
							</div>
							<div class="display-table-cell" style="padding-left: 20px">
								<input type="button" class="primarybtn ${disableAddToCart}"	value="<spring:message code="product.detail.addToCart.addToCart"/>"	id="addToCart" ${disabled} />
							</div>	
						</div>
				</div>					
			</div>	
	</div>
	<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="contractPopuppage">
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
		
		<!-- KIT ADDED  -->
			<c:set var="disableAddToCart">true</c:set>
						  <c:if test="${product.kitType == 'ORTHO'}">
   					 			<c:set var="disableAddToCart">false</c:set>
    					</c:if>
	<c:if test="${product.mddSpecification.kitData != null}">	
		<div  class="row boxshadow" style="margin:0px 0px 30px 0px">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt text-uppercase">Kit Components</div>	
									<c:forEach var="kitComponent" items="${product.mddSpecification.kitData}" varStatus="loop">
									<input type="hidden" id="kitCompCode_${loop.index}" value="${kitComponent.productCode}" index="${loop.index}" />
									<c:url value="${kitComponent.productImageUrl}" var="productUrl"/>
									<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
										<div class="col-lg-9 col-md-9 col-sm-6 col-xs-12 boldtxt">
											<!-- <img src="images/prouct_details.jpg" class="prod-thumb"/> -->
											<div class="display-row">
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
												
												<div class="table-cell" style=" padding-left:20px; vertical-align:middle ">
													<p><a href="${productUrl}">${kitComponent.productName}</a></p>
													 <div>${kitComponent.productCode}</div>
												 </div>
											</div>	 
										</div>											
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<div class="pull-right">
													<fmt:parseNumber var="qty" integerOnly="true"   type="number" value="${kitComponent.productQty}" />
                       							 <input type="hidden" id="kitCompQty_${loop.index}" index=${loop.index} value="${qty}" />
												<div class="ortho-qty">${qty}</div>
												<c:if test="${disableAddToCart ne 'true'}">
											<!-- <button class="btn btnclsactive">Add To Cart</button> -->
												<input type="button" class="addToCartKitProduct primarybtn ${disableAddToCart}"	value="<spring:message code="product.detail.addToCart.addToCart"/>"	id="${kitComponent.productCode}" index=${loop.index} />
												</c:if>
											</div>	
										</div>
									</div>
									</c:forEach>
									
									
		</div>
		</c:if>	
		<!-- KIT ADDED  -->
<!--  Add to cart Modal pop-up to identify  contract or non contract end -->
<c:if test="${!product.saleableInd || !product.salesRepCompatibleInd}">
								<div class="panel-group ">
										<div class="alertBroadcast broadcastMessageContainer panel panel-danger">
											<div class="panel-heading">
												<div class="panel-title">	
													<div class="row">
														<div class=" col-lg-11" >
															<span class="glyphicon glyphicon-ban-circle"></span>
															${product.errorMessage}
														</div>
													</div>
												</div>
											</div>
										</div>										
								</div>
								</c:if>
							<c:choose>
								<c:when test="${isMddSite}">
									<product:mddProductSpecifications />
								</c:when>
								<c:otherwise>
									<product:consProductSpecifications />
								</c:otherwise>
							</c:choose>								
										</div>
	<div class="row body-margin-bottom"></div>
	<!-- AAOL-6378 -->
	<div id="replacement-line-item-holder" style="display: none;"></div>
	</jsp:body>

</template:page>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="rowId" required="true" type="java.lang.String" %>
<%@ attribute name="index" required="true" type="java.lang.Integer" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product" %>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

<c:url value="${product.url}" var="productUrl"/>
<c:if test="${!product.saleableInd}">
	<c:set value="disabled" var="disableProductRow"/>
</c:if>
<c:set value="${(product.discontinued || !product.saleableInd || !product.salesRepCompatibleInd)? true : false}" var="disabledProduct"/>
<ycommerce:testId code="product_wholeProduct">
<c:set value="${ycommerce:productImage(product, 'thumbnail')}" var="primaryImage"/>
<input type="hidden" name="productCodePost" value="${product.code}"/>
<input type="hidden" name="index" value="${index}"/>
<tr disableProductRow= "${disabledProduct}">
	<td style="padding: 25px !important">
	 	<a href="${productUrl}">
	 		<product:productPrimaryImage product="${product}" format="thumbnail" />
		</a>
		<p class="firstline">
			<strong><a href="${productUrl}">${product.name}</a></strong>
		</p>
		<c:choose>
			<c:when test="${!isMddSite}">
				<p class="secondline">${product.baseMaterialNumber} </p>
			</c:when>
			<c:otherwise>
				<p class="secondline">${product.baseMaterialNumber} </p>
			</c:otherwise>
		</c:choose>	   
		<c:if test="${not empty product.launchStatus}">
			| <p><span><spring:message code="${product.launchStatus}"/></span></p>
		</c:if>
		<c:choose>
	      <c:when test="${!product.saleableInd}">
	      	|<span class="clrDisable"><label:message messageCode="category.result.discontinued"/></span>
	      </c:when>
	      <c:otherwise>
	      	<c:if test="${!isMddSite &&  not empty product.status}">
	      		|<span class="clrDisable"><label:message messageCode="${product.status}"/><c:if test="${product.status eq 'category.result.coming.soon'}">(<fmt:formatDate value="${product.firstShipEffective}" pattern="dd/MM/yyyy"/>)</c:if></span>
	      	</c:if>
	      </c:otherwise>
		</c:choose>
		    
		
		<div class="row inputAddCartRow">
	      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-6">
				<form:form class="form-inline margintop5px" role="form">
					<div class="form-group pull-left" >
						<div class="Tabsubtxt text-left verticalmiddle margin-10-left" >
								<!-- <h6>Lorem ipsum dolor sit amet, consectetur adipiscing elit,sed do eiusmod tempor incididunt ut labore</h6> -->
								<div class="thirdline margin-bottom-10"><strong>Unit:</strong>${product.deliveryUnit} (${product.numerator}&nbsp;${product.salesUnit})</div>
								<c:if test="${!isMddSite}">
								<div class="thirdline margin-bottom-10"><strong><spring:message code="cart.validate.volume"/>:${product.productVolume}</strong></div>
								<div class="thirdline margin-bottom-10"><strong><spring:message code="cart.validate.weight"/>: </strong>${product.productWeight}</div>
								<div class="thirdline margin-bottom-10"><strong><spring:message code="cart.validate.volume"/></strong>:${product.productWeight}</div>
								</c:if>
						</div>
						<div class="txt-fied-unit">
							<input type="text" value="0" class="form-control halign2 column3Input descMid txtWidth pull-left" id="qty_mobile_${index}" title="check" maxlength="6" name="qty" ${disabledProduct}/>
						</div>
						<div class="viewprice margin-10-left">
							<div class="pull-left">
								<c:choose>
										<c:when test="${isMddSite && !isinternationalAff}">
							            	<span class="priceRule" id="MobilePriceRule_${index}"></span>
											<span class="block getPricemobile" id="MobileGetgetPrice_${index}" productCode="${product.code}" productPriceMobile="${product.listPrice}" index="${index}">
												<a href="#"><spring:message code="product.search.ViewPrice"/></a>
											</span>
										</c:when>
										<c:otherwise>
											<span class="priceRule" id="MobilePriceRule_${index}">${product.listPrice}</span>
										</c:otherwise>
								</c:choose>
								<c:if test="${product.saleableInd && !product.salesRepCompatibleInd}">
									<div class="floatLeft globalError halign"><p>${product.errorMessage}</p></div>
								</c:if>
							</div>
						</div>
					</div>
				</form:form>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-6" style="box-shadow: none;">
				<form:form id="prodAddToCartForm" class="prod_list_form"	action="<c:url value="/cart/add"/>" method="post"></form:form>
				<div class="text-uppercase  margintop15px pull-right">
					<c:if test="${isEligibleForNewOrder eq true}">
					 	<c:if test="${disabledProduct eq true}">
							<input type="button" class=" primarybtn ${disabledProduct}-buttonDisable anchorwhiteText" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" /> 
					 	</c:if>
				     <c:if test="${disabledProduct eq false}">
				       <input type="button" class="addToCart primarybtn ${disabledProduct}-buttonDisable  anchorwhiteText" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" />
				     </c:if>
					</c:if>
				</div>
			</div>
		</div>
		 
		
		
			</td>
<td class="hidden"></td>
</tr>
</ycommerce:testId>
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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="${product.url}" var="productUrl"/>
<c:if test="${!product.saleableInd}">
	<c:set value="disabled" var="disableProductRow"/>
</c:if>
<c:url value="/cart/add" var="addProdToCart" />
<c:set value="${(product.discontinued || !product.saleableInd || !product.salesRepCompatibleInd)? true : false}" var="disabledProduct"/>

<ycommerce:testId code="product_wholeProduct">
	<c:set value="${ycommerce:productImage(product, 'thumbnail')}" var="primaryImage"/>
	<tr disableProductRow= "${disabledProduct}">
		<input type="hidden" class="productCodePost" name="productCodePost" value="${product.code}"/>
		<input type="hidden" name="index" value="${index}"/>

	<td class="verticalmiddle" style="padding-left:20px">
		<div class="checkbox checkbox-info">
			<input id="check${index}" class="styled catalog-prod-chck" type="checkbox">
			<label for="check${index}"></label>
		</div>
	</td>
<td class="verticalmiddle imgtdwidth">
<a href="${productUrl}">
 <product:productPrimaryImage product="${product}" format="thumbnail" />
</a>
</td>
		
		<td class="text-left verticalmiddle"><span class="Tabsubtxt">
				<p class="firstline text-capitalize">
					<strong><a href="${productUrl}">${product.name}</a></strong>
				</p>
					<c:choose>
		            	<c:when test="${!isMddSite}">
		            		<p class="secondline">${product.baseMaterialNumber}
		            	</c:when>
		            	<c:otherwise>
		            		<p class="secondline">${product.baseMaterialNumber}
		            	</c:otherwise>
	            	</c:choose>	   
                     <c:if test="${not empty product.launchStatus}">
					|<p><span><spring:message code="${product.launchStatus}"/></span></p>
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
	                </p>
	                <p class="descMid">${product.summary}</p>
	              
	                
	                
				<p class="thirdline pull-left">
					<spring:message code="product.search.uom"/>&nbsp;${product.deliveryUnit} (${product.numerator}&nbsp;${product.salesUnit})
				</p>
				
					<!-- AAOL-4150 -->
	                <c:if test="${product.obsolete}">
	                <div class="row">
	                	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	                		<div class="panel panel-danger"> 
                                 <div class="panel-heading"> 
                                       <h4 class="panel-title"> 
                                        <span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="prodcut.obsolete.error.message"/></span> 
                                       </h4> 
                                 </div> 
                               </div>  
	                	</div>
	                </div>	
	                </c:if>
					
				 <c:if test="${!isMddSite}">
		                <p class="thirdline pull-left"><spring:message code="cart.validate.volume"/>:${product.productVolume}&nbsp;
		               </p>
	               
				
				<p class="thirdline pull-left marginleft30px">	<strong><spring:message code="cart.validate.weight"/>: </strong>${product.productWeight}
				</p>
				<p class="thirdline pull-left marginleft30px unitcase">
					<strong><spring:message code="cart.validate.volume"/></strong>:${product.productWeight}
				</p>
				 </c:if>
		</span></td>
		
		
		<td class="paddingright40px" width="150px">
			<div class="viewprice pull-left">
				<div class="viewpricemargin" style="margin-left: 20px;">
				
					<form:form id="prodAddToCartForm" class="prod_list_form"	action="${addProdToCart}" method="post"></form:form>
					
					<c:choose>
					<c:when test="${isMddSite && !isinternationalAff}">
		            	<span class="priceRule" id="priceRule_${index}"></span>
						<span class="block getPrice" id="getPrice_${index}" productCode="${product.code}" productPrice="${product.listPrice}" index="${index}">
							<a  class="pricetag" href="#"><spring:message code="product.search.ViewPrice"/></a>
						</span>
					</c:when>
					<c:otherwise>
						<span  class="pricetag" class="priceRule" id="priceRule_${index}">${product.listPrice}</span>
					</c:otherwise>
				</c:choose>
				<c:if test="${product.saleableInd && !product.salesRepCompatibleInd}">
				<div class="floatLeft globalError halign"><p>${product.errorMessage}</p></div>
			</c:if>
				</div>
			</div>
			<div class="row">
				 <%-- <form:form class="form-inline" role="form" > --%>
				<input type="text" value="0" min-val="0" class="catalog-qty halign2 column3Input pull-right descMid form-control txtWidth" id="qty_desktop_${index}" title="check" maxlength="6" name="qty" ${disabledProduct}/>

			   <%-- </form:form> --%>


			</div>
			<div></div>
			<div class="row">
			<div class="pull-right viewprice">${product.deliveryUnit} (${product.numerator}&nbsp;${product.salesUnit})</div>
				<div class="text-uppercase pull-right  margintop15px" style="width:98px">
					
					   <c:if test="${disabledProduct eq true}">
					
                        <input type="button" class=" primarybtn ${disabledProduct}-buttonDisable  addToCartStyle anchorwhiteText" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" /> 
						</c:if>
						   <c:if test="${disabledProduct eq false}">
					
                        <input type="button" class="addToCart primarybtn ${disabledProduct}-buttonDisable addToCartStyle anchorwhiteText" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" /> 
						</c:if>
						
						
				</div>
			</div>
		</td>
	</tr>
     	</ycommerce:testId>
      
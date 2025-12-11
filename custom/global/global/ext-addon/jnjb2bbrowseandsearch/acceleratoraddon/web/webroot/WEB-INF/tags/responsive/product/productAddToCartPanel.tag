<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="allowAddToCart" required="true" type="java.lang.Boolean" %>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:if test="${(product.discontinued || !product.saleableInd || !product.salesRepCompatibleInd)}">
	<c:set value="disabled" var="disabled"/>
	<c:set value="buttonDisable" var="disableAddToCart"/>
</c:if>
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
<form:form id="prodAddToCartForm" class="prod_list_form"	action="<c:url value="/cart/add"/>" method="post"></form:form>
<c:set value="9999" var="index" />
<input type="checkbox" hidden="true" title="check" class="selprod"	id="${product.baseMaterialNumber}" checked="checked" style="display: none" index="${index}" />
<div class="display-table-row pull-right" style="margin-bottom: 30px">
	<div class="display-table-cell">
		<label style="dislay: inline-block; line-height: 35px"><spring:message code="product.detail.addToCart.quantity"/>	
		&nbsp;</label>
		<input class="ProdTbox cat3Input form-control" type="text" value="0"	title="check" id="qty_${index}" maxlength="6" name="qty" /> 
	
	</div>
	<div class="display-table-cell" style="padding-left: 20px">
	<input type="button" class=" primarybtn ${disableAddToCart}" value="<spring:message code="product.detail.addToCart.addToCart"/>"	id="addToCart" ${disabled} />
	</div>
</div>

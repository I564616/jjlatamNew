<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjLaOrderEntryData" required="true" %>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false" %>
<%@ attribute name="showStatus" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="${entry.product.url}" var="productUrl"/>
<c:url value="${entry.product.code}" var="productCode"/>
<div class="display-row">
    <div class="table-cell" style="vertical-align: middle;">
        <a href="javascript:;" class="laShowProductDetails imgprop" data="${productCode}">
            <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
        </a>
    </div>
    <div class="table-cell" style="padding-left: 15px;">
        <span class="Tablesubtxt">
            <p class="firstline">
                <h4 title="${entry.product.name}">
                    <a href="javascript:;" class="laShowProductDetails" data="${productCode}">${entry.product.name}</a>
                </h4>
            </p>
           <p class="secondline">
                   <spring:message code="cart.review.productDesc.jnJID"/>
                   <span class="strong">${entry.catalogId}</span>
                        <c:choose>
                           <c:when test="${(isValidatePage != null && isValidatePage eq true) && (displaySubstitutes eq true) && (not empty entry.product.productStatusCode && entry.product.productStatusCode == 'D3')}">
                             <span class="strong bg-warning p-2"><spring:message code="cart.product.obselete.discontinue.label"/></span>
                           </c:when>
                        </c:choose>
           </p>
            <p class="secondline">
                <spring:message code="product.uom"/>
                <span class="strong">
                    ${entry.product.deliveryUnit} (${entry.product.multiplicity} ${entry.product.baseUnitCode})
                </span>
            </p>
            <c:if test="${not empty entry.product.gtin}">
                <p class="secondline">
                    <spring:message code="cart.review.productDesc.gtnNumber"/>
                    ${entry.product.gtin}
                </p>
            </c:if>
            <c:if test="${not empty entry.originalOrderItem && entry.product.code ne entry.originalOrderItem}">
            	<p class="secondline">
            		<spring:message code="validatecartentry.original.itemcode"/>
            		${entry.originalOrderItem}
            	</p>
            </c:if>
        </span>
    </div>
</div>
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
<c:set value="${product.discontinue}" var="disabledProduct"/>

<ycommerce:testId code="product_wholeProduct">
    <c:set value="${ycommerce:productImage(product, 'thumbnail')}" var="primaryImage"/>
    <tr disableProductRow="${disabledProduct}">
        <input type="hidden" name="productCodePost" value="${product.code}"/>
        <input type="hidden" name="index" value="${index}"/>
        <td>
            <div style="width:35%; !important; float: left">
                <a href="${productUrl}">
                    <product:productPrimaryImage product="${product}" format="thumbnail" />
                </a>
            </div>
            <div style="width:65% !important; float:right">
                <p class="text-left">
                    <strong><a href="${productUrl}">${product.name}</a></strong>
                </p>
                <p class="text-left">
                    <c:choose>
                        <c:when test="${empty product.baseMaterialNumber}">
                            ${product.code}
                        </c:when>
                        <c:otherwise>
                            ${product.baseMaterialNumber}
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty product.launchStatus}">
                        | <p><span><spring:message code="${product.launchStatus}"/></span></p>
                </c:if>
                <c:if test="${product.discontinue}">
                    | <span class="clrDisable"><spring:message code="category.result.discontinued"/></span>
                </c:if>
                </p>
                <div class="text-left">
                    <h6>${product.summary}</h6>
                    <p class="thirdline pull-left unitcase">
                        <spring:message code="product.search.delivered.uom"/>&nbsp;
                        ${product.deliveryUnit} (${product.numerator}&nbsp;${product.baseUnit})
                    </p>
                </div>
            </div>
        </td>
        <td>
            <div>
                <form role="form" >
                 <c:choose>
                    <c:when test="${(empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)}">
                    <input type="text" value="0" class="form-control txtWidth" id="quantity${index}" maxlength="3" name="qty" ${disabledProduct}/>
                    </c:when>
                    <c:otherwise>
                     <input type="text" disabled="disabled" value="0" class="form-control txtWidth" id="quantity${index}" maxlength="3" name="qty" ${disabledProduct}/>
                    </c:otherwise>
                    </c:choose>
                </form>
            </div>
            <div>
                <c:choose>
                    <c:when test="${not disabledProduct && ((empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false))}">
                        <input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="10"/>
                        <input type="hidden" value="${product.baseMaterialNumber}" class="form-control txtWidth" id="productId${index}" readonly="readonly"/>
                        <input type="button" class="laAddToCartSearchResult primarybtn ${disabledProduct}-buttonDisable  anchorwhiteText" value="<spring:message code='product.search.addTocartSearch'/>" id="${product.baseMaterialNumber}" index="${index}" />
                    </c:when>
                    <c:otherwise>
                        <input type="button" disabled="disabled" class="addToCart primarybtn ${disabledProduct}-buttonDisable  anchorwhiteText btn btn-primary disabled" value="<spring:message code='product.search.addTocartSearch'/>" id="${product.baseMaterialNumber}" index="${index}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
    </tr>
</ycommerce:testId>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="com.jnj.facades.data.JnjLaProductData" %>
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

    <tr disableProductRow= "${disabledProduct}">
        <input type="hidden" name="productCodePost" value="${product.code}"/>
        <input type="hidden" name="index" value="${index}"/>

        <td class="verticalmiddle imgtdwidth">
            <a href="${productUrl}">
                <product:productPrimaryImage product="${product}" format="thumbnail" />
            </a>
        </td>

        <td class="text-left verticalmiddle">
            <span class="Tabsubtxt">
                <p class="firstline text-capitalize">
                    <strong><a href="${productUrl}">${product.name}</a></strong>
                </p>
                <p class="secondline">
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
                <p class="descMid">${product.summary}</p>
                <p class="thirdline pull-left">
                     <spring:message code="product.search.delivered.uom"/>&nbsp;
                     ${product.deliveryUnit} (${product.numerator}&nbsp;${product.baseUnit})
                </p>
            </span>
        </td>

        <td class="paddingright40px" width="150px">
            <div class="row">
                <form class="form-inline" role="form" >
                <c:choose>
                    <c:when test="${(empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)}">
                    <input type="text" value="0" class="halign2 column3Input pull-right descMid form-control txtWidth" id="quantity${index}" title="check" maxlength="6" name="qty" ${disabledProduct}/>
                    </c:when>
                    <c:otherwise>
                     <input type="text" disabled="disabled" value="0" class="halign2 column3Input pull-right descMid form-control txtWidth" id="quantity${index}" title="check" maxlength="6" name="qty" ${disabledProduct}/>
                    </c:otherwise>
                  </c:choose>
                    <span class="pull-right" style="margin-top:10px;margin-left:10px">${product.salesUnit}</span>
                </form>
            </div>
            <div class="row">
                <div class="pull-right">
                    <div class="viewprice" style="display:none">
                        (<spring:message code="product.detail.multiple.of"/>
                        &nbsp;${product.multiplicity})
                    </div>
                    <div class="text-uppercase margintop15px" style="width:98px">                    
                        <c:choose>
                            <c:when test="${not disabledProduct && ((empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false))}">
                                <input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="10"/>
                                <input type="hidden" value="${product.baseMaterialNumber}" class="form-control txtWidth" id="productId${index}" readonly="readonly"/>
                                <input type="button" class="laAddToCartSearchResult primarybtn ${disabledProduct}-buttonDisable  anchorwhiteText" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" />
                            </c:when>
                            <c:otherwise>
                                <input type="button" disabled="disabled" class="addToCart primarybtn ${disabledProduct}-buttonDisable  anchorwhiteText btn btn-primary disabled" value="<spring:message code="product.search.addTocartSearch"/>"  id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabledProduct}" />
                            </c:otherwise>
                        </c:choose>                       
                    </div>
                </div>
            </div>
        </td>
    </tr>
</ycommerce:testId>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjLaOrderEntryData" required="true" %>
<%@ attribute name="valueObject" type="com.jnj.facades.data.JnjOutOrderLine" required="true" %>

<tr class="noborder">
    <td class="text-right">
        <div class="display-row">
            <div class="table-cell" style="vertical-align: middle;">
                <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
            </div>
            <div class="table-cell" style="vertical-align: middle;"></div>
                <div class="table-cell" style="padding-left: 15px;">
                    <span class="Tablesubtxt">
                        <p class="secondline">
                            <spring:message code="cart.review.productDesc.jnJID"/>
                            <span class="strong"> ${valueObject.materialNumber}</span>
                        </p>
                    </span>
                </div>
            </div>
        </div>
    </td>

    <c:if test="${not empty entry.indirectCustomer}">
        <td class="valign-middle"></td>
        <td class="valign-middle"></td>
    </c:if>
    <c:if test="${empty entry.indirectCustomer}">
        <td class="valign-middle"></td>
    </c:if>

    <td>
        <div class="text-center">
            <c:if test = "${fn:contains(valueObject.materialQuantity,'.')}">
                ${fn:substringBefore(valueObject.materialQuantity, '.')} &nbsp; ${valueObject.salesUOM}
            </c:if>
            <c:if test = "${not fn:contains(valueObject.materialQuantity,'.')}">
                ${valueObject.materialQuantity} &nbsp; ${valueObject.salesUOM}
            </c:if>
        </div>
    </td>
    <td class="valign-middle">
        <span class="txt-nowrap">
            <spring:message code="cart.freeitem.message" />
        </span>
    </td>
    <td class="valign-middle"></td>
</tr>
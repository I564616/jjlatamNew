<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="standardCartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>

<c:if test="${freeGoodsMap ne null}">
    <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
	<c:if test="${not empty valueObject.itemCategory}">
		<tr class="noborder" id="freeGood${entry.product.code}">
			<td class="text-right">
			    <standardCartLa:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" />
			</td>
			<td>
	    		<div class="text-center">
					<c:url value="/cart/update" var="cartUpdateFormAction" />
					<form:form id="updateCartForm${entry.entryNumber}"
						action="${cartUpdateFormAction}" method="post"
						modelAttribute="updateQuantityForm${entry.entryNumber}">
						<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
						<input type="hidden" name="productCode"	value="${entry.product.code}" />
						<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
						<input type="hidden" size="4" value="${valueObject.materialQuantity}"
						    id="freeGoodQuantity${entry.product.code}" />
					</form:form>
				</div>
			</td>

			<c:if test="${not empty entry.indirectCustomer}">
                <td class="valign-middle"></td>
            </c:if>

			<td class="valign-middle">
                <div id="freeGoodQuantity${entry.product.code}">
                    <c:if test = "${fn:contains(valueObject.materialQuantity,'.')}">
                        ${fn:substringBefore(valueObject.materialQuantity, '.')}
                    </c:if>
                    <c:if test = "${not fn:contains(valueObject.materialQuantity, '.')}">
                        ${valueObject.materialQuantity}
                    </c:if>
                </div>
				<p class="thirdline">
					${entry.product.deliveryUnit}
				</p>
			</td>
			<td class="valign-middle">
			    <spring:message code="cart.freeitem.message" />
			</td>
		</tr>
	</c:if>
</c:if>
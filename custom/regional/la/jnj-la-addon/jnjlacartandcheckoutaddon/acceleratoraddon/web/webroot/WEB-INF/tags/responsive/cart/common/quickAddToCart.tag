<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="addToCartLabelKey" required="false" type="java.lang.String" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${empty addToCartLabelKey}">
		<c:set value='cart.common.addToCart' var='addToCartLabelKey' />
	</c:when>
	<c:otherwise>
		<c:set value="${addToCartLabelKey}" var='addToCartLabelKey'/>
	</c:otherwise>
</c:choose>

<div class="sectionBlock addToCart">
	<div class=" floatLeft ">
		<c:if test="${not empty validationError}">
			<p class="error"><spring:message code="${validationError}"/></p>
		</c:if>
	</div>
	<div class="right txtRight">
		<c:url value="/cart/addToCart" var="addToCartAction" />
		<form:form id="addToCartForm" action="${addToCartAction}"	method="post" name="quicksearchvalidate">
			<label for="productId"> <input type="text" name="productId" placeholder="Product Code or GTIN" id="productId" data-msg-required="<spring:message code="cart.common.productCode.enter"/>" ${disableInputBox}/>
			</label>
			<label for="sValue"> <input type="text" value="1" id="quantity" name="qty" class="sInput"
				data-msg-required="<spring:message code="cart.error.quantity"/>" ${disableInputBox}/>
			</label>
			<input id="addToCartCart" type="button" class="primarybtn"	value='<spring:message code="${addToCartLabelKey}"/>' ${disableInputBox}>
			<input type="hidden" id="invalidProdcutCodeMessage"	value="<spring:message code="cart.common.productCode.enter"/>" />
			<div class="registerError registerErrorSearch">
			<c:if test="${not empty productValidationErrorMsg}">	
					<c:forEach items="${productValidationErrorMsg}" var="msg">
						<label class = "error productValidationErrorMsg">
						${msg.code}
						</label>
					</c:forEach>
			</c:if>
			</div>
		</form:form>
	</div>
</div>

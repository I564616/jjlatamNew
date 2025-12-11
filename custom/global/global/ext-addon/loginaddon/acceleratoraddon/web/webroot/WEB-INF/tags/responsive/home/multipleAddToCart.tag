<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form name="mltiAddToCartForm" id="mltiAddToCartForm"
	action="javascript:;">
	<div class="productBox">
		<h3><spring:message code="homePage.multipleaddtoCart.enterproductcode" /></h3>
		<div class="productCode">
			<div>
				<label for="prodCode" class="col"><spring:message code="homePage.multipleaddtoCart.code" /></label>
			</div>
			<div>
				<textarea col="5" rows="5" id="prodCode"></textarea>
			</div>
			<div>
				<input type="button" id="errorMultiCart" class="tertiarybtn homeCartErrors" style="display: none" value="<spring:message code='homePage.errorDetails' />" />
				<input type="submit" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />" class="primarybtn right">
			</div>
					<!-- START DEFECT 32656 -->
			<div class="registersucess"></div>
		</div>
	</div>
</form:form>
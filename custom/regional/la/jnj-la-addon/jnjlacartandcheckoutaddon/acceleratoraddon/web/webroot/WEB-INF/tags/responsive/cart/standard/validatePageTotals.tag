<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<div class="total marLeft224 conTotal">         	
			<p><span><spring:message code="cart.validate.cartTotal.subTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.subTotal}" /></span></p>		
			
			<p><span><spring:message code="cart.validate.cartTotal.tax"/></span> <span class="jnjID"> <format:price priceData="${cartData.totalTax}"/></span></p>
			<p class="totalSum"><span><spring:message code="cart.validate.cartTotal.orderTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.totalPrice}"/></span></p>
</div>
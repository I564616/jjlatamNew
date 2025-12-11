<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<input type="hidden" id="cordisError" name="cordisError" value='<spring:message code="cart.noCharge.orderInfo.cordisHouseAccount.enter"/>'/>
<input type="hidden" id="globalError" name="globalError" value='<spring:message code="cart.error.global.message"/>'/>
<input type="hidden" id="POError" name="POError" value='<spring:message code="cart.error.DistPO"/>'/>
<input type="hidden" id="DropShipError" name="DropShipError" value='<spring:message code="cart.error.dropShip"/>'/>
<input type="hidden" id="DistPurchaseError" name="DistPurchaseError" value='<spring:message code="cart.error.distributedPurchase"/>'/>
<input type="hidden" id="SurgeonError" name="SurgeonError" value='<spring:message code="cart.error.surgeon"/>'/>
<input type="hidden" id="ReasonCodeError" name="ReasonCodeError" value="<spring:message code="cart.error.reasonCode"/>"/>

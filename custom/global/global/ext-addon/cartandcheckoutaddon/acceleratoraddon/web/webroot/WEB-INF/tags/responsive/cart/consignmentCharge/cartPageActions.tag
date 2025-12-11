<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url value="/home" var="homePageUrl" />
<c:set value="saveorderastemplate" var="classForSaveTemplate" />
<!-- consignmentFillUp/cartPageActions.tag -->
<!-- Disable checkout buttons if cart is empty or user does  not have rights of checkout -->
<c:if test="${empty cartData.entries || canCheckout}">
  <c:set value="linkDisable" var="classForSaveTemplate" />
  <c:set value="buttonDisable" var="classForValidate" />
</c:if>
<%-- <div class="sectionBlock buttonWrapperWithBG continueShopping">
<ul>
	<li><a href="${homePageUrl}"><spring:message code="cart.review.cartPageAction.continue"/></a></li>
	<li class="center"><a class="${classForSaveTemplate}" href="javascript:;"><spring:message code="cart.review.cartPageAction.saveTemplate"/></a></li>
	<li class="mar0"><input	class="secondarybtn floatRight cartStep1Saveupdate ${classForValidate}" value="Validate Order" type="button"></li>
</ul>
</div> --%>
<div class="row">
  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
    <div class="float-left-to-none">
      <button type="button" class="btn btnclsnormal checkout-clear-cart" id="RemoveCartData">
        <spring:message code="cart.payment.clearCart" />
      </button>
      <div class="empty-btn continue-btn">
        <cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
          <cms:component component="${feature}" />
        </cms:pageSlot>
      </div>
    </div>
    <div class="float-right-to-none">
    <form:form action="cart/fetchBatchSerial" method="GET">
    <button type="submit" class="btn btnclsnormal" id="fetchBatchSerial" style="display: inline-block;">
        <%-- <spring:message code="cart.review.cartPageAction.saveTemplate"/> --%><spring:message code="consignment.Charge.fetch.batch.serial" />
      </button>
      <c:set value="saveorderastemplate" var="classForSaveTemplate" />
      <button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}">
        <spring:message code="cart.review.cartPageAction.saveTemplate" />
      </button>
      <button type="button" class="btn btnclsactive consignmentChargeCartValidate">
        <spring:message code="cart.review.progressBar.validate" />
      </button>
    </form:form>
    </div>
  </div>
</div>

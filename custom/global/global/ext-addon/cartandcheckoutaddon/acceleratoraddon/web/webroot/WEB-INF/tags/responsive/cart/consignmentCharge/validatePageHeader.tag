<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="currentPage" required="false" type="java.lang.String"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<!-- consignmentCharge/validatePageHeader.tag -->
<!-- Hide select shipping if only one shipping addresses are less than 2 -->
<c:if test="${fn:length(shippingAddressess) lt 2}">
  <c:set value="strictHide" var="hideSelectShipping" />
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL" />
<c:if test="${!cartData.thirdPartyBilling}">
  <c:set value="strictHide" var="hideThirdPartyFlag" />
</c:if>
<!-- Hide drop ship selection button if no drop ship account is present -->
<c:if test="${fn:length(dropShipAccounts) eq 0}">
  <c:set value="strictHide" var="img" />
</c:if>
<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
  <c:set var="addOnVisibilityCss" value="disableTextbox" />
  <c:set var="disabled" value="disabled" />
</c:if>
<!-- AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!-- AAOL-6138 changes end -->
<div class="row jnjPanelbg">
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.customer.po.num" /><span
      class="redStar">*</span> <c:if test="${pOvalidation eq 'duplicate'}">
        <div style="color: #FF4500;" id="poDuplicate">
          <spring:message code="consignment.return.po.duplicate" />
        </div>
      </c:if> </label>
    <div class="pull-left form-consignment-input-select">
      <span>${cartData.purchaseOrderNumber}</span>
    </div>
    <!-- <div class="registerError" id="customerPONoError"></div> -->
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.stock.user" /><span class="redStar">*</span></label><span>${cartData.stockUser}</span>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 clearBoth margintop20px">
    <span> <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.end.user" /><span
        class="redStar">*</span></label><span>${cartData.endUser}</span>
    </span>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.po.date" /><span class="redStar">*</span></label>
    <div class="input-group form-element form-element-date">
       <!-- AAOL-6138 changes date format changed start -->
      <span> <fmt:formatDate pattern="${dateformat}" value="${cartData.poDate}" />
       <!-- AAOL-6138 changes date format changed end -->
    </div>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.requestedDeliveryDate" /><span
      class="redStar">*</span></label>
    <div class="input-group form-element form-element-date">
      <!-- AAOL-6138 changes date format changed start -->
      <span> <fmt:formatDate pattern="${dateformat}" value="${cartData.expectedShipDate}" />
      <!-- AAOL-6138 changes date format changed end -->
      </span>
    </div>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="cart.common.shipping.instructions" /></label> <span>${cartData.shippingInstructions}</span>
  </div>
</div>

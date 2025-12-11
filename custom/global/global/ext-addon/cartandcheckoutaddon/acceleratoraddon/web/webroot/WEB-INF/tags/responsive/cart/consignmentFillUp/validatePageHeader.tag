<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="currentPage" required="false" type="java.lang.String"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<!-- consignmentFillUp/validatePageHeader.tag -->
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
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="customer.po.number" /><span class="redStar">*</span>
      <c:if test="${pOvalidation eq 'duplicate'}">
        <div style="color: #FF4500;" id="poDuplicate" class="pull-left">
          <spring:message code="consignment.return.po.duplicate" />
        </div>
      </c:if> </label>
    <div class="pull-left form-consignment-input-select">
      <span> <span>${cartData.purchaseOrderNumber}</span>
      </span>
    </div>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="stock.user" /><span class="redStar">*</span></label> <span>
      <span>${cartData.stockUser}</span>
    </span>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 clearBoth margintop20px">
    <span> <label class="pull-left boldtext form-consignment-label-select"><spring:message code="end.user" /><span class="redStar">*</span></label>
      <span> <span>${cartData.endUser}</span>
    </span>
    </span>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="po.date" /><span class="redStar">*</span></label>
    <div class="input-group form-element form-element-date">
    <!-- AAOL-6138 changes date format changed start -->
      <span> <fmt:formatDate pattern="${dateformat}" value="${cartData.poDate}" />
       <!-- AAOL-6138 changes date format changed end -->
      </span>
    </div>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="requested.delivery.date" /><span class="redStar">*</span></label>
    <div class="input-group form-element form-element-date">
    <!-- AAOL-6138 changes date format changed start -->
      <span> <fmt:formatDate pattern="${dateformat}" value="${cartData.expectedShipDate}" />
      <!-- AAOL-6138 changes date format changed end -->
      </span>
    </div>
  </div>
  <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
    <label class="pull-left boldtext form-consignment-label-select"><spring:message code="shipping.instructions" /></label> <span> <span>${cartData.shippingInstructions}</span>
    </span>
  </div>
</div>

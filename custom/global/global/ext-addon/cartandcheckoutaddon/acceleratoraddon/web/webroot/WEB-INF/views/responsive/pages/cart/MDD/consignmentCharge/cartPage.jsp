<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="consignmentChargeCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentCharge"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<!-- consignmentCharge/cartPage.jsp -->
<template:page pageTitle="${pageTitle}">
  <spring:theme text="Your Shopping Cart" var="title" code="cart.page.title" />
  <c:url value="/home" var="homePageUrl" />
  <c:url value="/cart/checkout" var="checkoutUrl" />
  <input type="hidden" value="${canValidateCart}" id="canValidateCart">
  <input type="hidden" value="${displayBatchModeAlert}" id="displayBatchModeAlert">
  <c:if test="${empty cartData.entries}">
    <cart:emptyCart />
  </c:if>
  <c:if test="${ not empty cartData.entries}">
    <consignmentChargeCart:cartEntries />
  </c:if>
</template:page>
<!-- Modal dropship account detail pop up-->
<div id="dropShipAccountholder"></div>
<!-- Add to cart Modal pop-up to identify  contract or non contract start-->
<div id="contractPopuppage">
  <!-- Modal -->
  <div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
    <div class="modal-dialog modalcls">
      <div class="modal-content popup">
        <div class="modal-header">
          <button type="button" class="close clsBtn" data-dismiss="modal">
            <spring:message code="account.change.popup.close" />
          </button>
          <h4 class="modal-title selectTitle">
            <spring:message code="contract.page.addprod" />
          </h4>
        </div>
        <div class="modal-body">
          <div class="panel panel-danger">
            <div class="panel-heading">
              <h4 class="panel-title">
                <table class="contract-popup-table">
                  <tr>
                    <td><div class="glyphicon glyphicon-ok"></div></td>
                    <td><div class="info-text">
                        <spring:message code="contract.page.infotext" />
                      </div></td>
                  </tr>
                </table>
              </h4>
            </div>
          </div>
          <div>
            <spring:message code="contract.page.msg" />
          </div>
          <div class="continueques">
            <spring:message code="contract.page.continue" />
          </div>
        </div>
        <div class="modal-footer ftrcls">
          <a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart0"><spring:message code="cart.common.cancel" /></a>
          <button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart0">
            <spring:message code="contract.page.accept" />
          </button>
        </div>
      </div>
    </div>
  </div>
  <!-- Add to cart Modal pop-up to identify  contract or non contract end-->
</div>
<div id="replacement-line-item-holder" style="display: none;"></div>
<commonTags:changeAddressDiv />
<cart:saveAsTemplateDiv />

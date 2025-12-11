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
<%@ taglib prefix="consignmentReturnCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentReturn"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<!-- standard/checkoutConfirmationPage.jsp -->
<c:url value="/home" var="homePageURL" />
<template:page pageTitle="${pageTitle}">
<!-- AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!-- AAOL-6138 changes end -->
  <div id="ordercompletePage" class="orderReviewpage">
    <div class="row">
      <div class="col-lg-12 col-md-12">
        <div class="row">
          <div class="col-lg-7 col-md-7">
            <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
          </div>
        </div>
        <div class="row headingTxt content">
          <div class="col-lg-10 col-md-10 col-sm-8 col-xs-12">
            <spring:message code="cart.review.orderComplete" />
          </div>
          <div class="col-lg-2 col-md-2 col-sm-4 col-xs-12">
            <div class="float-right-to-none">
              <a href="${homePageURL}"><button type="button" class="btn btnclsnormal">
                  <spring:message code="cart.confirmation.done" />
                </button></a>
            </div>
          </div>
        </div>
        <div class="panel-group">
          <div class="panel panel-success">
            <div class="panel-heading">
              <span><span class="ok-icon glyphicon glyphicon-ok"></span> <strong><spring:message
                    code="consignment.return.confirm.thankYou" /></strong> <spring:message code="consignment.return.confirm.order.submit" /></span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--review start here for address and billing address sub total of the review   -->
    <c:url value="/order-history/order/${orderData.orderNumber}" var="orderDetailUrl" />
    <div class="row table-padding">
      <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
        <c:choose>
          <c:when test="${splitOrder}">
            <div class="subtext boldtext"></div>
            <hr>
          </c:when>
          <c:otherwise>
            <div class="order-column-x-100">
              <p class="subhead boldtext text-uppercase">
                <spring:message code="cart.confirmation.orderNumber" />
              </P>
              <div class="subtext boldtext">
                <a href="${orderDetailUrl}">${orderData.orderNumber}</a>
              </div>
            </div>
          </c:otherwise>
        </c:choose>
        <div class="order-column-x-100">
          <hr>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="cart.common.orderType" />
          </P>
          <div class="subtext boldtext">
            <spring:message code="cart.common.orderType.${orderData.orderType}" />
          </div>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="customer.po.number" />
          </P>
          <div class="subtext boldtext">${orderData.purchaseOrderNumber}</div>
        </div>
        <div class="order-column-x-100">
          <hr>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="po.date" />
          </P>
          <div class="subtext boldtext">
            <fmt:formatDate value="${orderData.poDate}" pattern="${dateformat}" />
          </div>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="return.created.date" />
          </P>
          <div class="subtext boldtext">
            <fmt:formatDate value="${orderData.createdOn}" pattern="${dateformat}" />
          </div>
        </div>
        <div class="order-column-x-100">
          <hr>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="stock.user" />
          </P>
          <div class="subtext boldtext">${orderData.stockUser}</div>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="end.user" />
          </P>
          <div class="subtext boldtext">${orderData.endUser}</div>
        </div>
        <div class="order-column-x-100">
          <hr>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="overall.status" />
          </P>
          <div class="subtext boldtext">
            <spring:message code="text.account.order.status.display.open" />
          </div>
        </div>
        <div class="order-column50">
          <p class="subhead boldtext text-uppercase">
            <spring:message code="shipping.instructions" />
          </P>
          <c:choose>
            <c:when test="${empty orderData.shippingInstructions}">
			  <div class="subtext boldtext">
                <spring:message code="orderDetailPage.orderData.notAvailable" />
              </div>            
            </c:when>
            <c:otherwise>
              <div class="subtext boldtext">${orderData.shippingInstructions}</div>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="order-column-x-100">
          <hr>
        </div>
        <!-- ship-to address start here -->
        <p class="subhead boldtext text-uppercase">
          <spring:message code="cart.common.ShipToAdd" />
        </p>
        <div class="subtext boldtext">
          <cart:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" companyName="${orderData.b2bUnitName}" />
        </div>
        <!-- ship - to address ends here -->
        <hr>
        <!-- Billing name and addresss start here -->
        <p class="subhead boldtext text-uppercase">
          <spring:message code="cart.common.BillingAdd" />
        </p>
        <div class="subtext boldtext">
          <cart:deliveryAddress deliveryAddress="${orderData.billingAddress}" companyName="${orderData.b2bUnitName}" />
        </div>
        <!-- Billing name and addresss end here -->
      </div>
    </div>
    <br>
    <c:url value="/checkout/single/downloadOrderConfirmation/${orderData.code}" var="orderConfirmationDownload" />
    <div class="row replishorder-row" style="padding: 20px 0;">
      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"></div>
      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 replishment-download-link" style="margin-top: 10px;">
        <span class="pull-right"><span class="link-txt boldtext"> <spring:message code="cart.confirmation.download" />
        <!-- AAOL-6019 changes start - Removal of Excel Download option -->
        <%-- <a class="tertiarybtn excel" href="${orderConfirmationDownload}?downloadType=EXCEL"> <spring:message code="cart.confirmation.excel" /></a> |  --%>
        <!-- AAOL-6019 changes ends --><a
          class="tertiarybtn pdf" href="${orderConfirmationDownload}?downloadType=PDF"> <spring:message code="cart.confirmation.pdf" /></a> </span>
      </div>
    </div>
    <br>
    <consignmentReturnCart:orderEntries />
  </div>
</template:page>
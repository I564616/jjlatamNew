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
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>

<templateLa:page pageTitle="${pageTitle}">

    <div id="ordercompletePage" class="orderReviewpage">
        <div class="row">
            <div class="col-lg-12 col-md-12">
                <div class="row">
                    <div class="col-lg-6 col-md-6">
                        <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
                    </div>
                    <div class="col-lg-6 col-md-6">
                        <div class="btn-group pull-right">
                            <ul id="breadcrumbs-one">
                                <li><a href="shipping"><spring:message code="cart.review.tabsShipping" /></a></li>
                                <li><a href="paymentContinue"><spring:message code="cart.review.tabsPayment" /></a></li>
                                <li><a href="orderReview"><strong><spring:message code="cart.review.tabsReview" /></strong></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="row headingTxt content">
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderreviewtxt"><spring:message code="cart.review.review"/></div>
                </div>
            </div>
        </div>
        <div id="successMessage_cart" style="display: none;">
            <laCommon:genericMessage messageCode="text.template.saved.successmessage"
                icon="ok" panelClass="success" />
                </div>
                
        <!--review start here for address and billing address sub total of the review   -->
        <div class="row table-padding">
            <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
                <p class="subhead boldtext text-uppercase"><spring:message	code="cart.common.purchaseOrder" /></P>
                <div class="subtext boldtext">${cartData.purchaseOrderNumber}</div>
                <hr>
                <!-- ship-to address start here -->
                <p class="subhead boldtext text-uppercase"><spring:message code="cart.common.ShipToAdd" /></p>
                <cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"  companyName="${cartData.b2bUnitName}"/>
                <hr>
                <!-- ship - to address ends here -->
        </div>
        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 placeorder-btn-holder">
            <button type="button" class="btn btnclsactive pull-right margintop12px placeOrderBtnLatam"><spring:message code="cart.common.placeOrder"/></button>
        </div>
    </div>
    <br>
    <br>
    <standardCartLa:reviewEntries />
    <cartLa:saveAsTemplateDiv/>
    <div class="row bottomrightbuttons">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="pull-right">
                <button type="button" class="btn btnclsnormal savetemplate saveorderastemplatelatam"  disabled="disabled"><spring:message code="cart.common.saveastemplate"/></button>
                <button type="button" class="btn btnclsactive placeorder placeOrderBtnLatam"><spring:message code="cart.common.placeOrder"/></button>
            </div>
        </div>
    </div>
    <lacommon:checkoutForm/>
</templateLa:page>


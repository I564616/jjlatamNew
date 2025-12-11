<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/jnjlaordertemplateaddon/responsive/order"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <c:url value="/order-history/laAddToCart?orderCode=${orderData.code}" var="addToCartFromOrderUrl"/>
    <div class="">
        <div class="row">
            <div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
              <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 headingTxt content"><spring:message code="orderDetailPage.heading"/></div>
        </div>

        <order:orderDetailHeader orderData="${orderData}"/>

		<c:if test="${placeOrderResComUserGrpFlag eq false && placeOrderGroupFlag eq true}" >
            <div class="row">
                <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
                    <div id="orderDetailAddToCartDiv" class="pull-right btnclsactive marginbtm30">
                        <a href="${addToCartFromOrderUrl}" id="orderDetailAddToCart1" orderCode ="${orderData.code}" class="anchorwhiteText" ><spring:message code="orderDetailPage.addOrdertocart"/></a>
                    </div>
                </div>
            </div>
            <c:if test="${not empty accErrorMsgs}">
                <c:forEach items="${accErrorMsgs}" var="msg">
                    <laCommon:genericMessage messageCode="${msg.code}" messagearguments="${msg.attributes}" icon="ban-circle"
                        panelClass="warning" />
                </c:forEach>
            </c:if>
        </c:if>

        <div class="orderDetailsTable d-none d-sm-block">
            <!-- GTS hold message -->
            <c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
                <c:if test="${not empty entry.gtsHold}">
                    <c:if test="${fn:containsIgnoreCase(entry.gtsHold,'Y')}">
                        <div class="rejected">
                            <span class="smallFont">
                                <laCommon:genericMessage catalogId="${entry.catalogId}"
                                    messageCode="order.history.gtsErrorMessage" icon="ban-circle" panelClass="danger" />
                            </span>
                        </div>
                    </c:if>
                </c:if>
            </c:forEach>
            <table id="ordersTable" class="table table-bordered lasorting-table ">
                <thead>
                    <tr>
                        <th style="width: 60px"><spring:message code="orderDetailPage.orderData.item"/></th>
                        <th class="no-sort" style="width: 120px"><spring:message code="orderDetailPage.orderData.product"/></th>
                        <th class="no-sort" style="width: 54px"><spring:message code="orderDetailPage.orderData.quantity"/></th>
                        <th class="no-sort" style="width: 44px"><spring:message code="orderDetailPage.orderData.itemPrice"/></th>
                        <th class="no-sort" style="width: 41px"><spring:message code="orderDetailPage.orderData.Total"/></th>
                        <th class="no-sort" style="width: 70px"><spring:message code="orderDetailPage.orderData.estDeliv"/></th>
                        <th class="no-sort" style="width: 39px"><spring:message code="orderDetailPage.orderData.status"/></th>
                    </tr>
                </thead>
                <c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
                    <c:choose>
                        <c:when test="false">
                            <c:forEach items="${entry.scheduleLines}" var="scheduleLine" varStatus="schLineCounter">
                                <order:orderDetailsItem entry="${entry}" isSplitOrder="true" scheduleLine="${scheduleLine}"  entryIndex="${entryCounter.count}"
                                    schLineIndex="${schLineCounter.count}"   isMddSite="${isMddSite}" orderType="${orderData.orderType}" displayDisputeOption="${orderData.displayDisputeOption}" jnjOrderData="${orderData}"/>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <order:orderDetailsItem entry="${entry}" isSplitOrder="false" isMddSite="${isMddSite}" entryIndex="${entryCounter.count}"  orderType="${orderData.orderType}"
                                displayDisputeOption="${orderData.displayDisputeOption}" jnjOrderData="${orderData}" />
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </table>
        </div>
        <order:totals />

        <!-- Table collapse for mobile device-->

        <div class="Subcontainer d-block d-sm-none">
            <table id="ordersTable" class="table table-bordered tabsize">
                <thead>
                    <tr><th class="text-left">
                            <spring:message code="orderDetailPage.orderData.productnumber"/>
                    </th></tr>
                </thead>
                <c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
                    <tbody>
                        <tr><td class="panel-title text-left">
                            <a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse1" class="ref_no toggle-link mobileacctoggle2 panel-collapsed">
                                <i class="bi bi-plus-lg" style="-webkit-text-stroke: 1.6px;"></i>${entry.product.code}
                            </a>                      
                            <div id="collapse1" class="panel-collapse collapse">
                                <div class="panel-body details">
                                    <p><spring:message code="orderDetailPage.orderData.product"/></p>
                                    <p>
                                        <c:choose>
                                            <c:when test="${empty entry.product.url}">
                                                <h4 title="${entry.product.name}">${entry.product.name}</h4>
                                            </c:when>
                                            <c:otherwise>
                                                <h4 title="${entry.product.name}">${entry.product.name}</h4>
                                            </c:otherwise>
                                        </c:choose>
                                        <br>
                                        <c:if test="${not isMaterialNumPresent}">
                                            ${entry.quantity} (${entry.product.numerator} &nbsp; ${entry.product.baseUnitCode})
                                        </c:if>
                                    </p>
                                    <p><spring:message code="orderDetailPage.orderData.quantity"/></p>
                                </div>
                            </div>
                        </td></tr>
                    </tbody>
                </c:forEach>
            </table>
        </div>
        <c:if test="${placeOrderResComUserGrpFlag eq false && placeOrderGroupFlag eq true}" >
            <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
                <div id="orderDetailAddToCartDiv2" class="pull-right btnclsactive marginbtm30">
                    <a href="${addToCartFromOrderUrl}" id="orderDetailAddToCart2" orderCode ="${orderData.code}" class="anchorwhiteText" >
                        <spring:message code="orderDetailPage.addOrdertocart"/>
                    </a>
                </div>
            </div>
        </c:if>
        <!--Accordian Ends here -->
    </div>
</templateLa:page>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="entry" required="true" type="com.jnj.facades.data.JnjLaOrderEntryData" %>
<%@ attribute name="freeGoodOrderLine" required="false" type="com.jnj.facades.data.JnjOutOrderLine" %>
<%@ attribute name="orderhistoryfreeItem" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showInfoLbl" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showDescLbl" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>

<c:if test="${!orderhistoryfreeItem}">
    <c:if test="${empty freeGoodOrderLine || freeGoodOrderLine eq null}">
        <div class="cartEntriesQtyError">
            <c:if test="${!showDescLbl}">
                <label class="entryMoreInfoMsg text-nowrap">
                    <B><spring:message code="checkOut.orderConfirmation.estimated"/></B>
                    <spring:message code="checkOut.orderConfirmation.deliveryDates"/>:
                    <a href="javascript:;"  class="showMoreInfo" data="${entry.entryNumber}" >&nbsp; <spring:message code="cart.review.item.moreDetail"/></a>
                </label>
            </c:if>
            <div class="cartMoreInfoDesc tipPositionNormal tooltip" id="entryMoreInfoDiv${entry.entryNumber}" style=" width: 200px; z-index: 1000; display: none;">
                <span class="imghArrow">&nbsp;</span>
                <span class="tooltiptext">
                    <c:choose>
                        <c:when  test="${entry.availabilityStatus eq 'notAvailable'}">
                            <span>
                                <spring:message code="cart.review.item.${entry.availabilityStatus}" />
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class='strong'>
                                <c:if test="${'en' ne sessionlanguage}">
                                    <spring:message code="checkOut.orderConfirmation.beforeEstimateds"/>
                                </c:if>
                                <B>
                                    <spring:message code="checkOut.orderConfirmation.estimateds"/>
                                </B>
                                <spring:message code="checkOut.orderConfirmation.deliveryDates"/>
                            </span>
                        </c:otherwise>
                    </c:choose>
                    <div style="overflow:auto;">
                        <div class="deliveryDate-holder">
                            <c:forEach items="${entry.scheduleLines}" var="scheduleLine">
                                <p>
                                    <c:choose>
                                        <c:when test="${not empty scheduleLine.deliveryDate}">
                                            <laFormat:formatDate dateToFormat="${scheduleLine.deliveryDate}" />
                                            -
                                            <spring:message code="cart.review.quantity"/>
                                            &nbsp;
                                            <span class="schedule-quantity">${scheduleLine.quantity}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="txtFont">
                                                <spring:message code="cart.review.deliverydate.unavailable"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </c:forEach>
                        </div>
                        <c:if test="${entry.quantity > entry.confirmedQty}">
                            <span>
                                <spring:message code="cart.review.item.tobeconfirm"/>
                                :
                                ${entry.quantity - entry.confirmedQty}
                            </span>
                        </c:if>
                        <c:if test="${showDescLbl}">
                            <div class="info">
                                <P>
                                    <c:choose>
                                        <c:when test="${fn:length(entry.scheduleLines) eq 0}">
                                            <spring:message code="cart.review.item.${entry.availabilityStatus}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code="cart.review.descriptionMessage1"/>
                                            <B>
                                                <spring:message code="checkOut.orderConfirmation.estimateds"/>
                                            </B>
                                            <spring:message code="cart.review.descriptionMessag2"/>
                                            <br>
                                        </c:otherwise>
                                    </c:choose>
                                </P>
                            </div>
                        </c:if>
                        <c:if test="${showInfoLbl}">
                            <div class="info">
                                <P>
                                    <c:if test="${!showDescLbl && entry.availabilityStatus ne 'notAvailable'}">
                                        <spring:message code="cart.review.descriptionMessage1"/>
                                        <B>
                                            <spring:message code="checkOut.orderConfirmation.estimateds"/>
                                        </B>
                                        <spring:message code="cart.review.descriptionMessag2"/>
                                        <br>
                                    </c:if>
                                    <spring:message code="cart.review.informationMessage"/>
                                </P>
                            </div>
                        </c:if>
                    </div>
                </span>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty freeGoodOrderLine && freeGoodOrderLine ne null}">
        <div class="cartEntriesQtyError">
            <c:if test="${!showDescLbl}">
                <label class="entryMoreInfoMsg text-nowrap">
                    <spring:message code="checkOut.orderConfirmation.view"/>
                    <B>
                        <spring:message code="checkOut.orderConfirmation.estimated"/>
                    </B>
                    <spring:message code="checkOut.orderConfirmation.deliveryDates"/>
                </label>
            </c:if>
            <a href="javascript:;"  class="showMoreInfo" data="${entry.entryNumber}FreeGood" >
                <spring:message code="cart.review.item.moreDetail"/>
            </a>
            <div class="cartMoreInfoDesc tipPositionNormal" id="entryMoreInfoDiv${entry.entryNumber}FreeGood" style=" width: 288px; z-index: 1000; display: none;">
                <span class="imghArrow">&nbsp;</span>
                <c:choose>
                    <c:when  test="${freeGoodOrderLine.availabilityStatus eq 'notAvailable'}">
                        <span>
                            <spring:message code="cart.review.item.${freeGoodOrderLine.availabilityStatus}"/>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class='strong'>
                            <c:if test="${'en' ne sessionlanguage}">
                                <spring:message code="checkOut.orderConfirmation.beforeEstimateds"/>
                            </c:if>
                            <B>
                                <spring:message code="checkOut.orderConfirmation.estimateds"/>
                            </B>
                            <spring:message code="checkOut.orderConfirmation.deliveryDates"/>
                        </span>
                    </c:otherwise>
                </c:choose>
                <div style="overflow:auto;">
                    <c:forEach items="${freeGoodOrderLine.scheduleLines}" var="scheduleLine">
                        <c:if test="${not empty scheduleLine.deliveryDate &&  scheduleLine.quantity > 0 }">
                            <p>
                                <c:choose>
                                    <c:when test="${not empty scheduleLine.deliveryDate}">
                                        <laFormat:formatDate dateToFormat="${scheduleLine.deliveryDate}" />
                                    </c:when>
                                    <c:otherwise>
                                        <span class="txtFont">
                                            <spring:message code="cart.review.deliverydate.unavailable"/>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                                -
                                <spring:message code="cart.review.quantity"/>
                                &nbsp;
                                ${scheduleLine.quantity}
                            </p>
                        </c:if>
                    </c:forEach>
                    <c:if test="${showDescLbl}">
                        <div class="info">
                            <P>
                                <c:choose>
                                    <c:when test="${fn:length(freeGoodOrderLine.scheduleLines) eq 0}">
                                        <spring:message code="cart.review.item.${freeGoodOrderLine.availabilityStatus}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="cart.review.descriptionMessage1"/>
                                        <B>
                                            <spring:message code="checkOut.orderConfirmation.estimateds"/>
                                        </B>
                                        <spring:message code="cart.review.descriptionMessag2"/>
                                        <br>
                                    </c:otherwise>
                                </c:choose>
                            </P>
                        </div>
                    </c:if>
                    <c:if test="${showInfoLbl}">
                        <div class="info">
                            <P>
                                <c:if test="${!showDescLbl && freeGoodOrderLine.availabilityStatus ne 'notAvailable'}">
                                    <spring:message code="cart.review.descriptionMessage1"/>
                                    <B>
                                        <spring:message code="checkOut.orderConfirmation.estimateds"/>
                                    </B>
                                    <spring:message code="cart.review.descriptionMessag2"/>
                                    <br>
                                </c:if>
                                <spring:message code="cart.review.informationMessage"/>
                            </P>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>
</c:if>
<c:if test="${orderhistoryfreeItem}">
    <div class="cartEntriesQtyError">
        <a href="javascript:;"  class="showMoreInfo" data="${entry.entryNumber}FreeGood" >
            <spring:message code="cart.review.item.moreDetail"/>
        </a>
        <div class="cartMoreInfoDesc tipPositionNormal tooltip" id="entryMoreInfoDiv${entry.entryNumber}FreeGood"
            style="width: 200px; z-index: 1000; display: none;">
            <span class="imghArrow">&nbsp;</span>
            <span class="tooltiptext tooltipTextDetails">
            <c:choose>
                <c:when  test="${entry.freeItemsAvailabilityStatus eq 'notAvailable'}">
                    <span>
                        <spring:message code="cart.review.item.${entry.freeItemsAvailabilityStatus}"/>
                    </span>
                </c:when>
                <c:otherwise>
                    <span class='strong'>
                        <c:if test="${'en' ne sessionlanguage}">
                            <spring:message code="checkOut.orderConfirmation.beforeEstimateds"/>
                        </c:if>
                        <B>
                            <spring:message code="checkOut.orderConfirmation.estimateds"/>
                        </B>
                        <spring:message code="checkOut.orderConfirmation.deliveryDates"/>
                    </span>
                </c:otherwise>
            </c:choose>
            <div style="overflow:auto;">
                <c:forEach items="${entry.freeGoodScheduleLines}" var="scheduleLine">
                    <c:if test="${not empty scheduleLine.deliveryDate &&  scheduleLine.quantity > 0 }">
                        <p>
                            <c:choose>
                                <c:when test="${not empty scheduleLine.deliveryDate}">
                                    <laFormat:formatDate dateToFormat="${scheduleLine.deliveryDate}" />
                                </c:when>
                                <c:otherwise>
                                    <span class="txtFont">
                                        <spring:message code="cart.review.deliverydate.unavailable"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                            -
                            <spring:message code="cart.review.quantity"/>
                            &nbsp;${scheduleLine.quantity}
                        </p>
                    </c:if>
                </c:forEach>

                <c:if test="${showDescLbl}">
                    <div class="info">
                        <p>
                            <c:choose>
                                <c:when test="${fn:length(entry.freeGoodScheduleLines) eq 0}">
                                    <spring:message code="cart.review.item.${entry.freeItemsAvailabilityStatus}"/>
                                </c:when>
                                <c:otherwise>
                                    <span>
                                        <spring:message code="cart.review.descriptionMessage1"/>
                                        <b><spring:message code="checkOut.orderConfirmation.estimateds"/></b>
                                        <spring:message code="cart.review.descriptionMessag2"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:if>
                <c:if test="${showInfoLbl}">
                    <div class="info">
                        <p>
                            <c:if test="${!showDescLbl && entry.freeItemsAvailabilityStatus ne 'notAvailable'}">
                                <spring:message code="cart.review.descriptionMessage1"/>
                                <b><spring:message code="checkOut.orderConfirmation.estimateds"/></b>
                                <spring:message code="cart.review.descriptionMessag2"/>
                                <br>
                            </c:if>
                            <spring:message code="cart.review.informationMessage"/>
                        </p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</c:if>
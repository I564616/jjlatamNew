<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="entry" required="true" type="com.jnj.facades.data.JnjLaOrderEntryData" %>

<div class="cartEntriesQtyError">
<input type="hidden" value=<spring:message code="orderHistoryPage.scheduleLines.limit"/> id="endValueData"/>
   <div class="text-nowrap showMoreInfo" data="${entry.entryNumber}">
        <span id="showMoreInfoLink">
        <c:if test="${not empty entry.scheduleLines || (not empty entry.expectedDeliveryDate && not empty entry.quantity)}">
            <c:forEach items="${entry.scheduleLines}" var="scheduleLine">
               <p>
               <c:choose>
                   <c:when test="${not empty scheduleLine.deliveryDate && not empty scheduleLine.quantity}">
                       <laFormat:formatDate dateToFormat="${scheduleLine.deliveryDate}" />
                       -
                       <span class="schedule-quantity">${scheduleLine.quantity}</span>
                       <span class="schedule-quantity">${entry.product.salesUnit}</span>
                   </c:when>

                   <c:when test="${(empty scheduleLine.deliveryDate || empty scheduleLine.quantity) && (not empty entry.expectedDeliveryDate && not empty entry.quantity)}">
                       <laFormat:formatDate dateToFormat="${entry.expectedDeliveryDate}" />
                       -
                       <span class="schedule-quantity">${entry.quantity}</span>
                       <span class="schedule-quantity">${entry.product.salesUnit}</span>
                   </c:when>

                   <c:otherwise>
                       <span class="txtFont">
                           <spring:message code="order.deliverydate.unavailable"/>
                       </span>
                   </c:otherwise>
               </c:choose>
               </p>
            </c:forEach>
            <c:if test="${empty entry.scheduleLines && not empty entry.expectedDeliveryDate && not empty entry.quantity}">
                <laFormat:formatDate dateToFormat="${entry.expectedDeliveryDate}" />
                    -
                <span class="schedule-quantity">${entry.quantity}</span>
                <span class="schedule-quantity">${entry.product.salesUnit}</span>
            </c:if>
        </c:if>
        </span>
   </div>           
</div>

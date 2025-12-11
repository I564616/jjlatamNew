<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true"%>
<%@ attribute name="deliverySch" type="com.jnj.facades.data.JnjDeliveryScheduleData" required="false"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<c:choose>
  <c:when test="${not empty deliverySch}">
    <p class="textLeft"><!-- Modified by Archana for AAOL-5513 -->
      <span class="labelText"><spring:message code="cart.confirmation.estimatedShipDate" /></span> 
      <fmt:formatDate pattern="${dateformat}" value="${deliverySch.materialAvailabilityDate}"
								var="estimatedShipDate" />
      <span class="textBlack">${estimatedShipDate}
      </span>
    </p>
    <p class="textLeft"><!-- Modified by Archana for AAOL-5513 -->
      <span class="labelText"> <spring:message code="cart.confirmation.estimatedDeliveryDate" />
      </span> <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
      <fmt:formatDate pattern="${dateformat}" value="${deliverySch.deliveryDate}"
								var="deliveryDate" />
      <span class="textBlack">${deliveryDate}
      </span>
    </p>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${entry.status ne 'BACKORDERED'}">
        <p class="textLeft">
        <!--  AAOL-6138 changes start  date format changed-->
          <span class="labelText"><spring:message code="cart.confirmation.estimatedShipDate" /></span> <span class="textBlack"> <fmt:formatDate
              value="${entry.shippingDate}" pattern="${dateformat}" />
          </span>
          <!--  AAOL-6138 changes end -->
        </p>
        <p class="textLeft">
        <!--  AAOL-6138 changes start  date format changed-->
          <span class="labelText"> <spring:message code="cart.confirmation.estimatedDeliveryDate" />
          </span> <span class="textBlack"> <fmt:formatDate value="${entry.scheduleLines[0].deliveryDate}" pattern="${dateformat}" />
          </span>
        </p>
      </c:when>
      <c:otherwise>
        <p class="textLeft">
          <span class="labelText"> <spring:message code="cart.confirmation.materialAvailabilityDate" />
          </span> <span class="textBlack"> <fmt:formatDate value="${entry.scheduleLines[0].materialAvailabilityDate}" pattern="${dateformat}" />
          </span>
           <!--  AAOL-6138 changes end -->
        </p>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
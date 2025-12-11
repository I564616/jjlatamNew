<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ attribute name="showChangeTypeLink" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="contractOrderMsg slideDownButton">
              <span class="iconExpandCollapse"></span>               
              <span class="slideDownRight"></span> <span>
              <span class="txtFont"><spring:message code="cart.common.orderType" /></span><b><spring:message code="cart.common.orderType.${cartData.orderType}" /></b></span>
              <c:if test="${showChangeTypeLink && fn:length(orderTypes) gt 1}">
                     <a class="showChagneOrderPopup" href="javascript:;"><spring:message code="cart.common.changeOrderType" /></a>
              </c:if>
              
              <!-- <a href="#">Change Order Type</a> -->
              <span><span class="txtFont">| <spring:message code="header.information.account.number" /></span><b>${cartData.b2bUnitId}</b></span>
              <span><span class="txtFont">|<c:if test="${not empty cartData.b2bUnitGln}"> <spring:message code="header.information.account.gln" />:</c:if></span><b>${cartData.b2bUnitGln}</b></span>
</div>

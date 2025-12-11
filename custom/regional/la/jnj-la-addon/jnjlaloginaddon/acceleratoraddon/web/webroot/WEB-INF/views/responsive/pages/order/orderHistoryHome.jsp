<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<div class="orderHistoryBody">
    <div class="">
        <c:forEach items="${searchPageData.results}" var="order">
            <c:url value="/order-history/order/${order.code}" var="orderDetailUrl" />

            <tr class="orderHistoryRow even gotoOrderHstryDetail">
                <td class="column1">
                    <c:choose>
                        <c:when test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
                            <a href="#" id="updatePoNumber" class="updatePoNumber" orderNumber="${order.code}">
                                <spring:message code="orderHistoryPage.updatedPO" />
                            </a>
                        </c:when>
                        <c:otherwise>
                         ${order.purchaseOrderNumber}
                        </c:otherwise>
                   </c:choose>
                </td>
                <td class="column2 labelText">
                    <a href="${orderDetailUrl}" class="ordernumber">
                        <c:choose>
                            <c:when test="${not empty order.sapOrderNumber}">
                                ${order.sapOrderNumber}
                            </c:when>
                            <c:otherwise>
                                ${order.code}
                            </c:otherwise>
                        </c:choose>
                    </a>
                    <c:if test="${order.surgeonUpdatInd}">
                         <a href="javascript:void(0);"  id="updateSurgeon" class="updateSurgeon"
                            orderNumber="${order.code}" surgeonName="${order.surgeonName}"><spring:message code="orderHistoryPage.updateSurgeon" />
                         </a>
                    </c:if>
                </td>
                <td class="column3">
                    <c:choose>
                        <c:when test="${not empty order.placed}">
							<c:choose>
								<c:when test="${'en' eq sessionLanguage}">
									<fmt:formatDate pattern="MM/dd/yyyy" value="${order.placed}"
										var="orderDate" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${order.placed}"
										var="orderDate" />
								</c:otherwise>
							</c:choose>
							<span class="txtFont">${orderDate}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="txtFont">&nbsp;</span>
                        </c:otherwise>
                    </c:choose>
                </td>
				<c:choose>
					<c:when test="${order.status == 'CREATED'}">
						<spring:message code='order.status.message.created'
							var="orderStatusMessage" />
					</c:when>
					<c:when test="${order.status == 'BEING_PROCESSED'}">
						<spring:message code='order.status.message.beingProcessed'
							var="orderStatusMessage" />
					</c:when>
					<c:when test="${order.status == 'CREDIT_HOLD'}">
						<spring:message code='order.status.message.creditHold'
							var="orderStatusMessage" />
					</c:when>
					<c:when test="${order.status == 'CANCELLED'}">
						<spring:message code='order.status.message.cancelled'
							var="orderStatusMessage" />
					</c:when>
					<c:when test="${order.status == 'COMPLETED'}">
						<spring:message code='order.status.message.completed'
							var="orderStatusMessage" />
					</c:when>
					<c:when test="${order.status == 'IN_PICKING'}">
						<spring:message code='order.status.message.beingPicked'
							var="orderStatusMessage" />
					</c:when>
					<c:otherwise><c:set var="orderStatusMessage" value=""></c:set></c:otherwise>
				</c:choose>
				<td class="column4">
                    <c:choose>
                        <c:when test="${not empty order.statusDisplay}">
                            <span data-toggle="tooltip" data-placement="left" title="${orderStatusMessage}"> <span class="${order.status}"></span>${order.statusDisplay}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="${order.status}">&nbsp;</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="column5">
                    <c:choose>
                        <c:when test="${not empty order.total}">
                            <%-- <span class="txtFont"><spring:message code="order.total" /></span> --%>
                            <format:price priceData="${order.total}"/>
                        </c:when>
                        <c:otherwise>
                            <span class="txtFont">&nbsp;</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </div>
</div>
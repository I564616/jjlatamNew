<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<div class="orderHistoryBody">

	<div class="">
		<c:forEach items="${searchPageData.results}" var="order">

			<c:url value="/order-history/order/${order.code}"
				var="orderDetailUrl" />
			<tr class="orderHistoryRow even gotoOrderHstryDetail">
				<td class="column1 fontHomePage"><c:choose>
						<c:when
							test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
							<a href="#" id="updatePoNumber" class="updatePoNumber"
								orderNumber="${order.code}"> <spring:message
									code="orderHistoryPage.updatePO" />
							</a>
						</c:when>
						<c:otherwise>
				            	     ${order.purchaseOrderNumber}
				             	       </c:otherwise>
					</c:choose></td>
				<td class="column1 labelText"><a href="${orderDetailUrl}"
					class="ordernumber fontHomePage"> <c:choose>
							<c:when test="${not empty order.sapOrderNumber}">
	                        			${order.sapOrderNumber}
	                        		</c:when>
							<c:otherwise>
	                        			${order.code}
	                        		</c:otherwise>
						</c:choose>
				</a> <c:if test="${order.surgeonUpdatInd}">
			                        		 | <a href="#"
							id="updateSurgeon" class="updateSurgeon fontHomePage"
							orderNumber="${order.code}" surgeonName="${order.surgeonName}"><spring:message
								code="orderHistoryPage.updateSurgeon" /></a>
						<input type="hidden" id="orderNo" value="${order.code}" />
					</c:if></td>

				<td class="column2"><c:choose>
						<c:when test="${not empty order.placed}"><!-- Modified by Archana for AAOL-5513 -->
								<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
							<fmt:formatDate pattern="${dateformat}" value="${order.placed}"
								var="orderDate" />
							<span class="txtFont">${orderDate}</span>
						</c:when>
						<c:otherwise>
							<span class="txtFont">&nbsp;</span>
						</c:otherwise>
					</c:choose></td>
				<td class="column3"><c:choose>
						<c:when test="${not empty order.channel}">
							<%-- <span class="txtFont"><spring:message code="order.channel.${order.channel}" /></span> --%>
							<a href="#">1Zjhjhk452A90</a>
						</c:when>
						<c:otherwise>
							<span class="txtFont"><spring:message
									code="order.channel.other" /></span>
						</c:otherwise>
					</c:choose></td>
				<td class="column4"><c:choose>
						<c:when test="${not empty order.channel}">
							<span class="txtFont"><spring:message
									code="order.channel.${order.channel}" /></span>

						</c:when>
						<c:otherwise>
							<span class="txtFont"><spring:message
									code="order.channel.other" /></span>
						</c:otherwise>
					</c:choose></td>
				<td class="column5"><c:choose>
						<c:when test="${not empty order.statusDisplay}">
							<span class="pendingStatus"></span><span class="home-status-txt">${order.statusDisplay}</span>
								</c:when>
						<c:otherwise>
							<span class="pendingStatus">&nbsp;</span>
						</c:otherwise>
					</c:choose></td>
				<!-- Adding for AAOL-3469 start-->
				<td class="column6 text-right"><c:choose>
						<c:when test="${not empty order.total.formattedValue}">
							<span class="priceList">${order.total.formattedValue}</span>
						</c:when>
						<c:otherwise>
							<span class="priceList">&nbsp;</span>
						</c:otherwise>
					</c:choose></td>
				<!-- Adding for AAOL-3469 end-->

			</tr>
		</c:forEach>
	</div>
</div>
<!-- Update PO Pop-up AAOL-3084-->
<%-- <commonTags:updatePO/> --%>


<!-- <div class="orderHistory-surgeon" id="surgeonPopupHolder"></div> -->
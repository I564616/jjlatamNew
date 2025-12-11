<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>


<script type="text/javascript">
				
	function toggleSort(id) {
		var sortCode="Order Date - Newest to Oldest";
		
		if((id=="orderDate" && $("#orderDate").hasClass("orderDown")) || (id=="orderDate" && !($("#orderDate").hasClass("orderDown")) && !($("#orderDate").hasClass("orderUp")))){
			sortCode="Order Date - oldest to newest";
			$("#orderDate").addClass("orderUp").removeClass("orderDown");
		}else if(id=="orderDate" && $("#orderDate").hasClass("orderUp")){
		    sortCode = "Order Date - Newest to Oldest";
		    $("#orderDate").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#orderDate").removeClass("orderUp").removeClass("orderDown");
		}
	
		if(id=="orderNumber" && $("#orderNumber").hasClass("orderDown")|| (id=="orderNumber" && !($("#orderNumber").hasClass("orderDown")) && !($("#orderNumber").hasClass("orderUp")))){
			sortCode="Order Number - increasing";
			$("#orderNumber").removeClass("orderDown").addClass("orderUp");
		}else if(id=="orderNumber" && $("#orderNumber").hasClass("orderUp")){
		    sortCode = "Order Number - decreasing";
		    $("#orderNumber").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#orderNumber").removeClass("orderUp").removeClass("orderDown");
		}
		
		if(id=="poNumber" && $("#poNumber").hasClass("orderDown")|| (id=="poNumber" && !($("#poNumber").hasClass("orderDown")) && !($("#poNumber").hasClass("orderUp")))){
			sortCode="Order poNumber - increasing";
			$("#poNumber").removeClass("orderDown").addClass("orderUp");
		}else if(id=="poNumber" && $("#poNumber").hasClass("orderUp")){
		    sortCode = "Order poNumber - decreasing";
		    $("#poNumber").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#poNumber").removeClass("orderUp").removeClass("orderDown");
		}
		
		if(id=="channel" && $("#channel").hasClass("orderDown")|| (id=="channel" && !($("#channel").hasClass("orderDown")) && !($("#channel").hasClass("orderUp")))){
			sortCode="Order Channel - increasing";
			$("#channel").removeClass("orderDown").addClass("orderUp");
		}else if(id=="channel" && $("#channel").hasClass("orderUp")){
		    sortCode = "Order Channel - decreasing";
		    $("#channel").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#channel").removeClass("orderUp").removeClass("orderDown");
		}
		
		if(id=="status" && $("#status").hasClass("orderDown")|| (id=="status" && !($("#status").hasClass("orderDown")) && !($("#status").hasClass("orderUp")))){
			sortCode="Order Status - increasing";
			$("#status").removeClass("orderDown").addClass("orderUp");
		}else if(id=="status" && $("#status").hasClass("orderUp")){
		    sortCode = "Order Status - decreasing";
		    $("#status").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#status").removeClass("orderUp").removeClass("orderDown");
		}
		
		if(id=="total" && $("#total").hasClass("orderDown")|| (id=="total" && !($("#total").hasClass("orderDown")) && !($("#total").hasClass("orderUp")))){
			sortCode="Order Total - increasing";
			$("#total").removeClass("orderDown").addClass("orderUp");
		}else if(id=="total" && $("#total").hasClass("orderUp")){
		    sortCode = "Order Total - decreasing";
		    $("#total").removeClass("orderUp").addClass("orderDown");
		}else{
		    $("#total").removeClass("orderUp").removeClass("orderDown");
		}
		
		$.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/home/renderOrderHistory",
			data : {
				"sortCode" : sortCode,
			},
			success : function(data) {
				// Now you have your HTML in "data", do whatever you want with it here in this function   
				$(".ohBlock").html(" ");
				$(".ohBlock").html(data);
			}
		});
	}
</script>

<div id="tables" class="orderstatuspage">
	<div class="row ordertextmargin">
		<div class="col-lg-12 col-md-12 col-xs-12 orderStatus text-left"><spring:message code="text.account.orderHistory.orderStatus"/></div>
	</div>
	<!--  <div class="col-lg-12 col-md-12 col-xs-12 orderStatus">Order Status  <span class="glyphicon glyphicon-question-sign"></span></div> -->
	<!--  mobile slider -->
	
	
	
	<div class="swiper-container">

		<div class="swiper-wrapper">

			<c:forEach items="${searchPageData.results}" var="order">

				<c:url value="/order-history/order/${order.code}"
					var="orderDetailUrl" />
				<div class="swiper-slide mobiletable">


					<table class="table table-bordered">
						<tbody>
							<tr>
								<th><spring:message code="orderHistoryPage.poNomber" /></th>
								<td><c:choose>
										<c:when
											test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
											<a href="#" id="updatePoNumber" class="updatePoNumber"
												orderNumber="${order.code}"> <spring:message
													code="orderHistoryPage.updatedPO" />
											</a>
										</c:when>
										<c:otherwise>
											       	     ${order.purchaseOrderNumber}
												 	       </c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<th><spring:message code="orderHistoryPage.orderNumber" /></th>
								<td><a href="${orderDetailUrl}" class="ordernumber"> <c:choose>
											<c:when test="${not empty order.sapOrderNumber}">
									                        			${order.sapOrderNumber}
									                        		</c:when>
											<c:otherwise>
									                        			${order.code}
									                        		</c:otherwise>
										</c:choose>
								</a>
								<c:if test="${order.surgeonUpdatInd}">
			                        		 | <a href="javascript:void(0);"
											id="updateSurgeon" class="updateSurgeon"
											orderNumber="${order.code}"
											surgeonName="${order.surgeonName}"><spring:message
												code="orderHistoryPage.updateSurgeon" /></a>
									</c:if></td>
							</tr>

							<tr>
								<th><spring:message code="orderHistoryPage.orderDate" /></th>
								<td><c:choose>
										<c:when test="${not empty order.placed}">
											<fmt:formatDate pattern="MM/dd/yyyy" value="${order.placed}"
												var="orderDate" />
											<span class="txtFont">${orderDate}</span>
										</c:when>
										<c:otherwise>
											<span class="txtFont">&nbsp;</span>
										</c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<th><spring:message code="orderHistoryPage.status" /></th>
								<td><c:choose>
										<c:when test="${not empty order.statusDisplay}">
											<span class="pendingStatus"></span>${order.statusDisplay}
																	</c:when>
										<c:otherwise>
											<span class="pendingStatus">&nbsp;</span>
										</c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<th><spring:message code="orderHistoryPage.total" /></th>
								<td><c:choose>
										<c:when test="${not empty order.total}">
											<format:price priceData="${order.total}"/>
										</c:when>
										<c:otherwise>
											<span class="txtFont"><spring:message
													code="order.channel.other" /></span>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</tbody>
					</table>

				</div>


			</c:forEach>
		</div>

<!-- 		Add Pagination -->
		<div class="swiper-pagination"></div>

	</div>




	<!--  mobile slider -->
	<div class="desktoptableparent">
		<table id="ordersTablehomelatam" class="table table-bordered table-striped">
			<thead>
				<tr>
					<th class="no-sort" id="status" onclick="toggleSort(id)"><spring:message
							code="orderHistoryPage.poNomber" />
					<th class="no-sort" id="orderNumber" onclick="toggleSort(id)"><spring:message
							code="orderHistoryPage.orderNumber" /></th>
					<th class="no-sort" id="orderDate" onclick="toggleSort(id)"><spring:message
							code="orderHistoryPage.orderDate" /></th>
					<th class="no-sort" id="status" onclick="toggleSort(id)"><spring:message
							code="orderHistoryPage.status" /></th>
					<th class="no-sort"><spring:message
							code="orderHistoryPage.total" /></th>
				</tr>
			</thead>
			<%-- <fmt:formatDate pattern="MM/dd/yyyy" value="${date}" var="currentDate" /> --%>
			<tbody>
				<jsp:include page="../order/orderHistoryHome.jsp"></jsp:include>
			</tbody>
		</table>
	</div>
	<div class="shomoreandviewall col-xs-12 whitebg d-flex justify-content-end">

		<div class="btnclsactive pull-right">
			<c:url value="/order-history" var="orderHistoryUrl" />
			<a href="${orderHistoryUrl}" style="color: white"><spring:message
					code="homePage.orderHistory.vieworders" /></a>
		</div>
	</div>







</div>



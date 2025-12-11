<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjCartData" required="true" type="com.jnj.facades.data.JnjLaCartData"%>
<%@ attribute name="globalCount" required="true" type="java.lang.String" %>
<div class="row">
    <div class="col-lg-12 col-md-12">
	    <div class=" hidden-xs tableshadow dropShipmentTable" id="dropShipmentTable-${globalCount}">

			<input type="hidden" id="hidden-sessionLang" value="${sessionlanguagePattern}"/>
			<div class="dateselectordropdown" style="display:none">
							
							<c:choose>
								<c:when test="${!checkoutoption}">
								<c:if test="${globalCount==1}">
								<div  class="dateselector">
								<div class="setshipping">
									<spring:message code="cart.shipping.RequestedDeliveryDate" />
								</div> 
								
										<div class="input-group marginleft20">
												<input id="date-picker-head" name="toDate" 
													placeholder="<spring:message code="cart.shipping.SelectDate" />" class="date-picker form-control requestDeliveryDataPicker"
													type="text" readonly value="<fmt:formatDate pattern="${sessionlanguagePattern}" value="${cartData.entries[0].expectedDeliveryDate}" />"> <label for="date-picker-head"
													class="input-group-addon btn"><span
													class="glyphicon glyphicon-calendar"></span> </label>
											</div>
								</div>
									</c:if>	
								</c:when>
							<c:otherwise>
								<div  class="dateselector">
								<select class="shippingMethodSelect"
											data="${cartData.entries[0]}" id="shipping-head-dropdown"
											>
											<option selected="selected" style="display:none;"><spring:message code="cart.common.selectmethod"/></option>
											<c:forEach items="${cartData.entries[0].shippingMethodsList}"
												var="shippingMethod">
												<option
													value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
													<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
													<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
													<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"></c:if>>
													
													${shippingMethod.dispName}
													
													</option>
											</c:forEach>
											
										</select>
										</div>
										</c:otherwise>
									</c:choose>
							
						</div>
				<table id="ordersTable"
					class="table table-bordered table-striped sorting-table"
					data-paging="false" data-info="false">
					<thead>
						<tr>
							<th class="no-sort text-uppercase"><spring:message
									code="cart.shipping.product" /></th>
							<th class="no-sort text-uppercase"><spring:message
									code="cart.shipping.RequestedDeliveryDate" /></th>
                            <c:if test="${displayIndirectCustomerHeader}">
                                <th class="text-left no-sort text-uppercase"><spring:theme
                                        code="text.account.buildorder.indirectCustomer"
                                        text="INDIRECT CUSTOMER" /></th>
                            </c:if>
                            <c:if test="${displayIndirectPayerHeader}">
                                <th class="text-left no-sort text-uppercase"><spring:theme
                                        code="text.account.buildorder.indirectPayer"
                                        text="INDIRECT PAYER" /></th>
                            </c:if>
							<th class="no-sort text-uppercase"><spring:message
									code="cart.shipping.quantity" /></th>
							<th class="no-sort text-uppercase"><spring:message
									code="cart.shipping.unitprice" /></th>
							<th class="no-sort text-uppercase totalTableCell"><spring:message
									code="cart.shipping.total" /></th>
						</tr>
					</thead>
					<tbody>
						
						<c:forEach items="${jnjCartData.entries}" var="entry"
							varStatus="count">
							<tr id="orderentry-${count.count}">
								<td class="text-left"><standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false" />
								    <div class="txtWidth text-left">
								        <c:if test="${!jnjCartData.holdCreditCardFlag  && jnjCartData.partialDelivFlag && showATPFlagMap.get(entry)}">
								            <standardCart:entryMoreInfo entry="${entry}" showInfoLbl="true" showDescLbl="false"/>
								        </c:if>
                                    </div>
								</td>

								<td>
									<div class="floatLeft column6">
										<c:choose>
										<c:when test="${!checkoutoption}">
										<div class="input-group  margintop10">
										<input data="${entry.entryNumber}" id="date-picker-${globalCount}${count.count}" name="toDate"	placeholder="<spring:message code="cart.shipping.SelectDate" />" class="date-picker form-control date-picker-body requestDeliveryDataPicker selectExpectedDeliveryDate"
											type="text" readonly value="<fmt:formatDate pattern="${sessionlanguagePattern}" value="${entry.expectedDeliveryDate}" />"> <label for="date-picker-${globalCount}${count.count}"	class="input-group-addon btn" ><span
											class="glyphicon glyphicon-calendar"></span> </label>
									</div>
									</c:when>
										<c:otherwise>
										<select class="shippingMethodSelect shipping-body-dropdown"
											data="${entry.entryNumber}">
											<c:forEach items="${entry.shippingMethodsList}"
												var="shippingMethod">
												<option
													value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
													<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
													<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
													<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"> </c:if>>
													${shippingMethod.dispName}</option>
											</c:forEach>
											<option selected="selected" style="display:none;"><spring:message code="cart.common.selectmethod"/></option>
										</select>
                                       </c:otherwise>
									</c:choose>
									</div>
								</td>

                                <!-- Checking indirect customer line level flag -->
                                <c:if test="${displayIndirectCustomerLine[count.index]}">
                                    <td class="txtWidth valign-middle">
                                        <p class="descMid" id="indirectCustomerId${count.index}">${entry.indirectCustomer}</p>
                                        <p class="descMid" id="indirectCustomerName${count.index}">${entry.indirectCustomerName}</p>
                                    </td>
                                </c:if>
                                <c:if test="${displayIndirectCustomerHeader eq true && (empty displayIndirectCustomerLine || displayIndirectCustomerLine[count.index] eq false)}">
                                    <td></td>
                                </c:if>

                                <!-- Checking indirect payer line level flag -->
                                <c:if test="${displayIndirectPayerLine[count.index]}">
                                    <td class="txtWidth valign-middle">
                                        <p class="descMid" id="indirectPayerId${count.index}">${entry.indirectPayer}</p>
                                        <p class="descMid" id="indirectPayerName${count.index}" >${entry.indirectPayerName}</p>
                                    </td>
                                </c:if>
                                <c:if test="${displayIndirectPayerHeader eq true && (empty displayIndirectPayerLine || displayIndirectPayerLine[count.index] eq false)}">
                                    <td></td>
                                </c:if>

								<td class="valign-middle">${entry.quantity}&nbsp;${entry.product.salesUnitCode}<br />


								</td>
								<td class="valign-middle" id="basePrice_${entry.entryNumber}">
									<format:price priceData="${entry.basePrice}" />
								</td>
								<td class="valign-middle totalTableCell"
									id="totalPrice_${entry.entryNumber}"><ycommerce:testId
										code="cart_totalProductPrice_label">
										<format:price priceData="${entry.totalPrice}" />
									</ycommerce:testId>
							    </td>

                                <!-- Free item -->
                                <c:if test="${freeGoodsMap ne null}">
                                    <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
                                    <c:if test="${freeGoodsMap[entry.product.code] == valueObject.materialNumber}">
						                <standardCart:bonusItem entry="${entry}"/>
						            </c:if>
						            <c:if test="${freeGoodsMap[entry.product.code] != valueObject.materialNumber}">
						                <standardCart:alternateMaterial entry="${entry}" valueObject="${valueObject}"/>
                                    </c:if>
                                </c:if>
							</tr>
						</c:forEach>

					</tbody>

				</table>
									<div class="row basecontainer">
										
												<table class="total-summary-table">
													<tr>
														<td class="total-summary-label"><spring:message code="cart.shipping.SubTotal"/></td>
														<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.subTotal}" /></td>
													</tr>
													<c:if test="${jnjCartData.totalFreightFees.value > 0}">
													    <tr class="summary-bline">
														    <td class="total-summary-label"><spring:message code="cart.shipping.shipping"/></td>
														    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.totalFreightFees}" /></td>
													    </tr>
													</c:if>
													<c:if test="${jnjCartData.totalFees.value > 0}">
													    <tr>
														    <td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
														    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.totalFees}" /></td>
													    </tr>
													</c:if>
													<c:if test="${jnjCartData.totalTax.value > 0}">
													    <tr>
														    <td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
														    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.totalTax}"/></td>
													    </tr>
													</c:if>
													<tr class="total-price-row">
														<td class="total-summary-label"><spring:message code="cart.shipping.total"/></td>
														<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${jnjCartData.totalGrossPrice}" /></td>
														
													</tr>
												</table>
												
										</div>
											
										</div>
										
									</div>
									
								</div>	              
<!-- 	<div class="row basecontainer"> -->
<%-- 		<standardCart:multiCartTotalItem cartData="${jnjCartData}" /> --%>
<!-- 	</div> -->
	
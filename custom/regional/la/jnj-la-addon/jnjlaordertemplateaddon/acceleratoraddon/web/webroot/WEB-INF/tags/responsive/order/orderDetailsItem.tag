<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="entry" required="true" type="com.jnj.facades.data.JnjGTOrderEntryData"%>
<%@ attribute name="scheduleLine" required="false" type="com.jnj.facades.data.JnjDeliveryScheduleData"%>
<%@ attribute name="isSplitOrder" required="true" type="java.lang.Boolean"%>
<%@ attribute name="entryIndex" required="true" type="java.lang.String"%>
<%@ attribute name="schLineIndex" required="false" type="java.lang.String"%>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean" %>
<%@ attribute name="orderType" required="true" type="java.lang.String" %>
<%@ attribute name="displayDisputeOption" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>
<%@ taglib prefix="orderLa" tagdir="/WEB-INF/tags/addons/jnjlaordertemplateaddon/responsive/order"%>
<%@ attribute name="jnjOrderData" required="true" type="com.jnj.facades.data.JnjGTOrderData" %>


<c:set value="${not (isMddSite && fn:contains('ZDELZRE', orderData.orderType))}" var="displayShipping" />
<c:set value="${(empty entry.materialEntered) ? false : true}" var="isMaterialEntPresent" />
<c:set value="${(empty entry.materialNumber) ? false : true}" var="isMaterialNumPresent" />

<tr>
    <%-- <td>
        <c:url value="${entry.product.url}" var="productUrl"/>
        <a href="${productUrl}">${entry.product.catalogId}</a>
    </td> --%>
   <td>
   	 <c:choose>
	     <c:when test="${placeOrderGroupFlag eq true}" >
        				<c:url value="${entry.product.url}" var="productUrl"/>
				        <a href="${productUrl}">${entry.product.catalogId}</a>
 		</c:when>
	      <c:otherwise>
	     				 ${entry.product.catalogId}
	      </c:otherwise>
	  </c:choose>	
    </td>
    
    <td>
         <div class="column2">
             <c:choose>
                 <c:when test="${isMaterialNumPresent}">
                     <div class="prodImage">
                         <img src="${commonResourcePath}/images/prod_img.png" alt="Product Image" />
                     </div>
                 </c:when>
                 <c:otherwise>
                     <c:url value="${entry.product.url}" var="productUrl"/>
                 </c:otherwise>
             </c:choose>
             <div class="orderProdDesc left">
                 <c:choose>
                     <c:when test="${isMaterialNumPresent}">
                         <b><i>Product: <span class="strong">${entry.materialNumber}</span></i></b>
                     </c:when>
                     <c:otherwise>
                         <input type="hidden" value="${entry.product.saleableInd && entry.salesRepDivisionCompatible}" class="saleableInd"
                            id="${entry.product.code}" />
                         <c:choose>
                             <c:when test="${empty entry.product.url}">
                                 <h4 title="${entry.product.name}">${entry.product.name}</h4>
                             </c:when>
                             <c:otherwise>
                                 <div title="${entry.product.name}"><p>${entry.product.name}</p></div>
                            </c:otherwise>
                         </c:choose>
                         <p class="secondline">
                            <spring:message code="product.uom"/>
                            <span class="strong"> ${entry.product.deliveryUnitCode}</span>
                         </p>
                     </c:otherwise>
                 </c:choose>
                 <c:if test="${not empty entry.product.gtin}">
                    <p>
                        <span class="smallFont"><spring:message code="orderDetailPage.orderData.gtin" /></span>
                        ${entry.product.gtin}
                    </p>
                 </c:if>
                 <c:if test="${not empty entry.contractNumber}">
                    <p>
                        <span class="smallFont"><spring:message code="orderDetailPage.orderData.contract" /></span>
                        ${entry.contractNumber}
                    </p>
                 </c:if>
                 <c:if test="${not empty entry.product.upc}">
                     <p>
                        <span class="smallFont"><spring:message code="product.detail.basic.upc" /></span>
                        ${entry.product.upc}
                     </p>
                 </c:if>
                 <c:if test="${not empty entry.indirectCustomerName && not empty entry.indirectCustomer}">
                     <p>
                        <span class="smallFont"><strong><spring:message code="order.history.indirect.customer" /></strong></span>
                        ${entry.indirectCustomer} - ${entry.indirectCustomerName}
                     </p>
                 </c:if>
                 <c:if test="${not empty entry.indirectPayerName && not empty entry.indirectPayer}">
                     <p>
                        <span class="smallFont"><strong><spring:message code="order.history.indirect.payer" /></strong></span>
                        ${entry.indirectPayer} - ${entry.indirectPayerName}
                     </p>
                 </c:if>
             </div>
         </div>
    </td>
    <td class="text-nowrap">
        <c:if test="${not isMaterialNumPresent}">
            ${entry.quantity}&nbsp;${entry.product.salesUnit}
            <p>
                <spring:message code="product.multiple" />&nbsp;${entry.product.multiplicity}
            </p>
        </c:if>
    </td>
    <td>
        <c:choose>
            <c:when test="${'ZLZ' eq orderType}">
                <span class="txtFont">-</span>
            </c:when>
            <c:when test="${not empty entry.basePrice.formattedValue}">
                ${entry.basePrice.formattedValue}
            </c:when>
            <c:otherwise>
                <span class="txtFont">&nbsp;</span>
            </c:otherwise>
        </c:choose>
    </td>
    <td>
        <c:choose>
            <c:when test="${not empty entry.totalPrice.formattedValue}">
                ${entry.totalPrice.formattedValue}
            </c:when>
            <c:otherwise>
                <span class="txtFont">&nbsp;</span>
            </c:otherwise>
        </c:choose>
    </td>
    <td>
        <c:choose>
            <c:when test="${not empty entry.scheduleLines || not empty entry.expectedDeliveryDate}">
                <c:choose>
                    <c:when test="${'ZLZ' eq orderType || 'BACKORDERED' eq entry.status}">
                        <span class="txtFont"> <spring:message
								code="order.deliverydate.unavailable" />
						</span>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${jnjOrderData.partialDelivFlag && !jnjOrderData.holdCreditCardFlag && showATPFlagMap.get(entry)}">
                                <div class="column2 expectedDeliveryDateCheckout">
                                    <orderLa:orderHistoryMoreInfo entry="${entry}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="column2 expectedDeliveryDateCheckout">
                                    <span class="txtFont"> <spring:message
								code="order.deliverydate.unavailable" />
						        </span>
                                </div>
                            </c:otherwise>
				        </c:choose>
				        <span class="txtFont">&nbsp;</span>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
						<span class="txtFont"> <spring:message
								code="order.deliverydate.unavailable" />
						</span>
					</c:otherwise>
        </c:choose>
    </td>
    <td class="text-nowrap">
        <p><spring:message code="orderDetailPage.orderData.status.${entry.status}" /></p>
    </td>

    <!-- Free item -->
    <c:if test="${entry.freeItem ne null}">
        <tr class="text-nowrap">
            <div class="column3 qtyCart">
                <td></td>
                <td>
                    <div title="${entry.freeItem}">
                        <span class="block">
                            <p><b>${entry.freeItem}</b></p>
                        </span>
                    </div>
                </td>
                <td>
                    <span class="column3 qtyConfirmation">
                        ${fn:substringBefore(entry.freeItemsQuanity, ".")} &nbsp; ${entry.freeItemUnit}
                    </span>
                </td>
                <td>
                    <div class="column4 priceCart">
                		<span class="block">
                			<font style="color: #12C2E9;"><spring:message code="cart.freeitem.message" /></font>
                		</span>
                	</div>
                </td>
                <td><div class="column5 totalPriceCart"></div></td>
                <td>
                    <div class="column6 statusConfirmation">
                        <c:choose>
                            <c:when test="${jnjOrderData.partialDelivFlag && !jnjOrderData.holdCreditCardFlag && showATPFlagMap.get(entry)}">
                                <c:choose>
                                    <c:when test="${fn:length(entry.freeGoodScheduleLines) ne 1}">
                        	            <div class="column2 expectedDeliveryDateCheckout">
                        		            <standardCart:entryMoreInfo entry="${entry}" orderhistoryfreeItem="true" showInfoLbl="false" showDescLbl="true"/>
                        		        </div>
                        	        </c:when>
                        	        <c:otherwise>
                        		        <div class="column2 expectedDeliveryDateCheckout">
                        			        <c:choose>
                        				        <c:when test="${'en' eq sessionlanguage}">
                        				             <c:forEach items="${entry.freeGoodScheduleLines}" var="scheduleLine">
														<c:choose>
															<c:when test="${not empty scheduleLine.deliveryDate}">
																<span class="txtFont">&nbsp;<fmt:formatDate
																		value="${scheduleLine.deliveryDate}"
																		pattern="MM/dd/yyyy" /></span>
															</c:when>
															<c:otherwise>
																<span class="txtFont">&nbsp;<spring:message
																		code="order.deliverydate.unavailable" /></span>
															</c:otherwise>
														</c:choose>
													</c:forEach>
                        				        </c:when>
                        				        <c:otherwise>
                        				            <c:forEach items="${entry.freeGoodScheduleLines}" var="scheduleLine">
														<c:choose>
															<c:when test="${not empty scheduleLine.deliveryDate}">
																<span class="txtFont">&nbsp;<fmt:formatDate
																		value="${scheduleLine.deliveryDate}"
																		pattern="dd/MM/yyyy" /></span>
															</c:when>
															<c:otherwise>
																<span class="txtFont">&nbsp;<spring:message
																		code="order.deliverydate.unavailable" /></span>
															</c:otherwise>
														</c:choose>
													</c:forEach>
                        				        </c:otherwise>
                        			        </c:choose>
                        		        </div>
                        	        </c:otherwise>
                        	    </c:choose>
                        	</c:when>
                        	<c:otherwise>
                                <div class="column2 expectedDeliveryDateCheckout">
									<span class="txtFont"> <spring:message
											code="order.deliverydate.unavailable" />
									</span>
								</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </td>
                <td></td>
            </div>
        </tr>
    </c:if>
</tr>
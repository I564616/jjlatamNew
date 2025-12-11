<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/standard"%>
<%@ attribute name="jnjCartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
<%@ attribute name="globalCount" required="true" type="java.lang.String" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/common"%>
<%@ taglib prefix="international" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/international"%>
<div class="orderDetailPanel sectionBlock validateCheckout reviewOrder">
    <span><span class="txtFont"><spring:message code="cart.common.orderType" /></span><b><spring:message code="cart.common.orderType.${jnjCartData.orderType}" /></b></span>
    <!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
	<div class="orderDetHead noBorderBtm reviewOrder">
		<div class="hcolumn1"><span><spring:message code="cart.validate.lineItem" /></span></div>
	    <div class="hcolumn2"><span><spring:message code="cart.validate.product" /></span> </div>               
	    <div class="hcolumn3"><span><spring:message code="cart.validate.quantity" /></span></div>
	    <div class="hcolumn4"><span><spring:message code="cart.validate.itemPrice" /></span></div>
		<div class="hcolumn5"><span><spring:message code="cart.validate.total" /></span></div>
	    <div class="hcolumn6"><span><spring:message code="cart.validate.shippingMethod" /></span></div>
	</div> 
	<div id="orderDetBody"  class="orderDetBody ">
            <!--Order Derail Row starts-->
          <c:set value="false" var="showIndirectCust"></c:set>
          <c:choose>
				<c:when test="${!checkoutoption}">
             		 <span>
							<input class="iconCalender expectedGlobalShipDate required"   value="<fmt:formatDate value="${cartData.expectedShipDate}" 
							pattern="${dateformat}"/>" id="shippingGlobalDate-${globalCount}" name="expectedGlobalShipDate"  
							type="text" data-msg-required="<spring:message code="cart.consumer.valid.date"/>" readonly="readonly"/>
						</span> 
            </c:when>
           <c:otherwise>
            <c:forEach items="${jnjCartData.entries}" var="entryList"  varStatus="count">
					 <c:if test="${'1' eq count.count}">
						<select class="shippingGlobalMethodSelect" data="${entryList.entryNumber}" id="shippingGlobalMethodSelect-${globalCount}">
							<c:forEach items="${entryList.shippingMethodsList}"
								var="shippingMethod">
								<option value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
									<c:if test="${entryList.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
									<c:if test="${shippingMethod.route eq entryList.defaultRoute}"> selected="selected"</c:if>
									<c:if test="${shippingMethod.route eq 'Standard' && (entryList.shippingMethod eq null || !entryList.selectableShippingMethod)}"> selected="selected"</c:if>>
								${shippingMethod.dispName}</option>
							</c:forEach>
						</select>
					 </c:if>
					</c:forEach> 
           	</c:otherwise>
			</c:choose>
          <c:forEach items="${jnjCartData.entries}" var="entry"  varStatus="count">
          	<c:choose>
              	<c:when test="${count.count  mod 2 == 0}">
              		<c:set var="orderEntriesClass" value="even"> </c:set>
              	</c:when>
           		<c:otherwise>
           			<c:set var="orderEntriesClass" value="odd"> </c:set>
           		</c:otherwise>
            </c:choose>			
            <div id="orderentry-${count.count}" class="orderDetRow ${orderEntriesClass}">
				<div class="floatLeft column1 paddingLeft">
					${count.count}
					<%-- ADD FOR SCHEDULE LINES --%>
					<c:if test="${entry.expandableSchedules}">
						<div class="expendOrderDetailArrow rightArrow">+</div>
					</c:if> 
				</div>
				<div class="floatLeft column2">
					<standardCart:productDescription entry="${entry}" showRemoveLink="false" showStatus="true" />
             	</div>
             	 <div class="floatLeft column3">
						<div class="lbox">
							<p><span>${entry.quantity}</span></p>
							<p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>						
							<p class="log"><span><c:if test="${not empty entry.lotComment}"><label for="logComment2" class="descSmall block"><spring:message code="cart.validate.lotComment"/></label>${entry.lotComment}</c:if></span></p>
						</div>								  
	                </div>
				 <div class="floatLeft column4">                
                    <p><format:price priceData="${entry.basePrice}"/></p>
                </div>
                <div class="floatLeft column5">
                     <p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
                </div>
                <div class="floatLeft column6">
						<c:choose>
							<c:when test="${!checkoutoption}">
		              		 <span>
										<input data="${entry.entryNumber}" class="shippingGlobalDate-${globalCount} iconCalender expectedShipDate required " 
										value="<fmt:formatDate value="${cartData.expectedShipDate}" pattern="${dateformat}"/>" id="shippingDate${entry.entryNumber}" 
										name="distExpectedShippingDate"  type="text" data-msg-required="<spring:message code="cart.consumer.valid.date"/>" readonly="readonly"/>
									</span> 
		              	</c:when>
	              		<c:otherwise>
					<select class="shippingMethodSelect shippingGlobalMethodSelect-${globalCount}" id="shippingMethod${entry.entryNumber}" data="${entry.entryNumber}">
						<c:forEach items="${entry.shippingMethodsList}"
							var="shippingMethod">
							<option
								value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
								<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
								<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
								<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"> selected="selected"</c:if>>
							${shippingMethod.dispName}</option>
						</c:forEach>
					</select>
	              		</c:otherwise>
					</c:choose>
					<div class="marTop20 floatLeft marLeft20">
					<p class="textLeft"><span class="labelText"><spring:message code="cart.confirmation.estimatedShipDate"/></span> <span class="textBlack"><fmt:formatDate value="${entry.scheduleLines[0].shippingDate}" pattern="${dateformat}"/></span></p>
					<p class="textLeft"><span class="labelText"><spring:message code="cart.confirmation.estimatedDeliveryDate"/></span> <span class="textBlack"><fmt:formatDate value="${entry.scheduleLines[0].deliveryDate}" pattern="${dateformat}"/></span></p>
					</div>
             </div>
			 <!--Changes for Bonus Item -->
             <c:if test="${freeGoodsMap ne null}">
					<c:set var="valueObject"
						value="${freeGoodsMap[entry.product.code]}" />
					<c:if test="${not empty valueObject.itemCategory}">
						<div class="orderDetRow ${orderEntriesClass} orderDetRowNoPadding">
						<div
								class="floatLeft column2"
								style="padding-left: 50px">
								<standardCart:productDescription entry="${entry}"
									showRemoveLink="false" showStatus="false" />
							</div>
							<div class="floatLeft expectedDeliveryDateCheckout"></div>
							<div
								class="floatLeft column3a indirectCustDesc indirectCustomerCart"
								style="width: 287px"></div>
								
							<div class="floatLeft column3">
								<div class="lbox">
								<p><span>${valueObject.materialQuantity}</span></p>
								
								</div>			
							</div>
							<div class="floatLeft column4">
								<span class="block"> <font style="color: #12C2E9;"><spring:message
											code="cart.freeitem.message" /></font>
								</span>
							</div>
							<div class="floatLeft column6a totalPriceCart"></div>
						</div>
					</c:if>
				</c:if>
             </div>
	      	 </c:forEach>               
           	<div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
				<div class="txtRight">
					<international:multiCartTotalItem cartData="${jnjCartData}" />
				</div>
			</div>
	</div>
</div>
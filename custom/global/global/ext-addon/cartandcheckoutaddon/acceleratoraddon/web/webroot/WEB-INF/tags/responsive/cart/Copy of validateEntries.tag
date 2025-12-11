<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>

<script type="text/javascript"> // set vars
/*<![CDATA[*/
var cartRemoveItem = true;
/*]]>*/
</script>

 <!--Order Derail Row starts-->


     <div class="orderDetHead noBorderBtm reviewOrder">
   			<div class="hcolumn1"><span>[x] line Items</span></div>
            <div class="hcolumn2"><span>Product</span> </div>               
            <div class="hcolumn3"><span>Quantity</span></div>
            <div class="hcolumn4"><span>Item Price</span></div>
			<div class="hcolumn5"><span>Total</span></div>
            <div class="hcolumn6"><span>Shipping Method</span></div>
       </div>

   <div class="orderDetBody reviewOrder">   	
            <!--Order Derail Row starts-->
       <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">	   
      	 <c:url value="${entry.product.url}" var="productUrl"/>
               	<c:choose>
	              	<c:when test="${count.count  mod 2 == 0}">
	              		<c:set var="orderEntriesClass" value="even"> </c:set>
	              	</c:when>
              		<c:otherwise>
              			<c:set var="orderEntriesClass" value="odd"> </c:set>
              		</c:otherwise>
              	</c:choose>				
	            <div class="orderDetRow ${orderEntriesClass}" id="orderentry-1">
					<div class="floatLeft column1 paddingLeft"> ${count.count}</div>
					<div class="floatLeft column2">
	                    <div class="prodImage">
	                    <!-- If a product is not viewable in the catalog, the product name will not be clickable.  -->
			            <c:choose>
			              	<c:when test="${!entry.product.isProdViewable}">
			              		<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
			              	</c:when>
		              		<c:otherwise>
		              			<a href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="cartIcon"/></a>
		              		</c:otherwise>
		              	</c:choose>
	       				
	                    </div>
	                    <div class="orderProdDesc">
	                    	<h4>
		         				<c:choose>
					              	<c:when test="${!entry.product.isProdViewable}">
						              		${entry.product.name}
					              	</c:when>
				              		<c:otherwise>
						              	<a href="${productUrl}">
											${entry.product.name}
										</a>
				              		</c:otherwise>
				              	</c:choose>
							</h4>
								

						<p class="jnjID">J&J ID#: <span class="strong">${entry.product.code}</span></p>
						<p>GTIN#: ${entry.product.gtin}</p>
						<p class="smallFont">Contract #: ${entry.product.code}</p>
						<c:if test="${entry.updateable}" >
							<ycommerce:testId code="cart_product_removeProduct">
								<p><a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
								Remove Item
									<%-- <spring:theme code="basket.page.removeItem"/> --%>
								</a></p>
							</ycommerce:testId>
                        </c:if>
                        <p class="msgHighlight">${entry.product.hazmatCode}</p>
	                    </div>
	                </div>
	                <div class="floatLeft column3">
						<div class="lbox">
						
							<c:url value="/cart/update" var="cartUpdateFormAction" />
							<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
							<input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
							<input type="hidden" name="productCode" value="${entry.product.code}"/>
							<input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
							<ycommerce:testId code="cart_product_quantity">
								<form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.quantity"/></form:label>
							<p><span >	<form:input disabled="${not entry.updateable}"  type="text" size="3"   maxlength="6" id="quantity${entry.entryNumber}" class="insertInput" path="quantity"/></span></p>
							</ycommerce:testId>                              
                            <p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>
                            <c:if test="${entry.updateable}" >
								<ycommerce:testId code="cart_product_updateQuantity">
									<a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}"><spring:theme code="basket.page.update"/></a>
								</ycommerce:testId>
                            </c:if>
						</form:form>
						<!-- Hide lot/comment if division is part of configured divisions -->
						<c:forEach var="division" items="${divisionList}">
						  <c:if test="${division eq entry.product.division}">
	  							<p class="log"><span >
								<label class="descSmall block" for="LotComment${entry.entryNumber}"> Lot/Comment</label>
								<input type='text' class='insertInput lotCommentInput' title="Enter Code" id="LotComment_${entry.entryNumber}" value="${entry.lotComment}" />
								</span></p>
						  </c:if>
						</c:forEach>

						
	
						
						</div>
	                </div>
	                
	                <div class="floatLeft column4">	                	
							<format:price priceData="${entry.basePrice}"/>							           
	                </div>
	                <div class="floatLeft column5">
							<ycommerce:testId code="cart_totalProductPrice_label">
								<p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
							</ycommerce:testId> 
	                </div>
	                <div class="floatLeft column6">
						<select>
						<option value="">All</option>
						</select>
					</div>
	                
	             </div>
	             <c:if test="${entry.status eq 'Error'}">
	              		<div class="error">
							<p><label:message
								messageCode="cart.review.orderLineLevelError" />
							</p>
						</div>
              	</c:if>
			<c:set var="loadmorePageSize" value="${cartEntriesPageSize}"/>
			<c:set var="remainingLines" value="${fn:length(cartData.entries)-count.count}"/>	
		 </c:forEach>	
	   </div>
	
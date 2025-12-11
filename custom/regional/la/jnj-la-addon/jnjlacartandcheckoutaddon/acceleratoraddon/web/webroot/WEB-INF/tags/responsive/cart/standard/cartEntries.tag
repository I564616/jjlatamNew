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
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>

<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>

<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>

<!--Order Derail Row starts-->
<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" modelAttribute="UpdateMultipleEntriesInCartForm">
 </form:form>
<div class="orderDetailPanel validateStandard">
        	<div class="orderDetHead noBorderBtm reviewOrder minHeight45">
       			<div class="hcolumn1"><span><spring:message code="cart.validate.lineItem"/></span></div>
                <div class="hcolumn2"><span></span> </div>
				<div class="hcolumn3"><span><spring:message code="cart.review.entry.wtg"/></span></div>
               
			<div class="hcolumn4"><ul class="ulCartEntry">
                <li class="textColorWhite"><spring:message code="cart.validate.quantity"/>
                </li><li class="textColorWhite"><a class="cartUpdateAllbtn" id="cartUpdateAllbutton" href="javascript:;">Update All</a></li></ul></div>
                
                <div class="hcolumn5"><span><spring:message code="cart.validate.itemPrice"/></span></div>
				<div class="hcolumn6"><span><spring:message code="cart.validate.total"/></span></div>
               
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
             		
            <div id="orderentry-${count.count}" class="orderDetRow ${orderEntriesClass}">
				<div class="floatLeft column1 paddingLeft"> ${count.count}</div>
				<div class="floatLeft column2">
						<standardCart:productConsumerDescription entry="${entry}" showRemoveLink="true" />
                </div>
				  <div class="floatLeft column3">                
                   <p><spring:message code="cart.validate.weight"/><b>&nbsp;&nbsp;${entry.product.productWeight}</b><b>&nbsp;${entry.weightUOM}</b></p>
                   <p><spring:message code="cart.validate.volume"/><b>&nbsp;&nbsp;${entry.product.productVolume}</b><b>&nbsp;${entry.volumeUOM}</b></p>
                   
                </div>
                           
                
                 <div class="floatLeft column4">
						<div class="lbox">
						
							<c:url value="/cart/update" var="cartUpdateFormAction" />
							<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" modelAttribute="updateQuantityForm${entry.entryNumber}">
							<input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
							<input type="hidden" name="productCode" value="${entry.product.code}"/>
							<input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
							<ycommerce:testId code="cart_product_quantity">
								<form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.quantity"/></form:label>
							<p><span >	<form:input disabled="${not entry.updateable}"  type="text" size="3"   maxlength="6" id="quantity_${entry.entryNumber}" class="insertInput small laQtyUpdateTextBox" entryNumber="${entry.entryNumber}" path="quantity"/></span></p>
							</ycommerce:testId>
                            <p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.baseUnitCode})</label></p>
                            <c:if test="${entry.updateable}" >
								<ycommerce:testId code="cart_product_updateQuantity">
									<a href="javascript:;" class="UpdateQtyLinkCart laUpdateQuantityProduct" id="QuantityProduct_${entry.entryNumber}"><spring:theme code="basket.page.update"/></a>
								</ycommerce:testId>
                            </c:if>
						</form:form>
						<!-- Hide lot/comment if division is part of configured divisions -->
						<c:forEach var="division" items="${divisionList}">
						  <c:if test="${division eq entry.product.mddSpecification.division}">
	  							<p class="log"><span >
								<label class="descSmall block" for="LotComment${entry.entryNumber}"><spring:message code="cart.review.entry.lotComment"/></label>
								<input type='text' class='insertInput lotCommentInput' title="Enter Code" id="LotComment_${entry.entryNumber}" value="${entry.lotComment}" />
								</span></p>
						  </c:if>
						</c:forEach>
						</div>
	                </div>
                
              
                
                <div class="floatLeft column5">                
                    <p><format:price priceData="${entry.basePrice}"/></p>
                </div>
                <div class="floatLeft column6">
                     <p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
                </div>
             </div>
             
           </c:forEach>
            <!--Order Derail Row ends-->
         
            <!--Order Derail Row ends-->            
            </div>
        </div>
   
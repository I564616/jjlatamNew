<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>

<!--Order Derail Row starts-->

<div class="orderDetailPanel validateStandard">
        	<div class="orderDetHead noBorderBtm reviewOrder">
       			<div class="hcolumn1"><span><spring:message code="cart.validate.lineItem" /></span></div>
                <div class="hcolumn2"><span><spring:message code="cart.validate.product" /></span> </div>               
                <div class="hcolumn3"><span><spring:message code="cart.validate.quantity" /></span></div>
                <div class="hcolumn4"><span><spring:message code="cart.validate.itemPrice" /></span></div>
				<div class="hcolumn5"><span><spring:message code="cart.validate.total" /></span></div>
                <div class="hcolumn6"><span><spring:message code="cart.validate.shippingMethod" /></span></div>
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
						<p class="log"><span><c:if test="${not empty entry.lotComment}"><label for="logComment2" class="descSmall block"><spring:message code="cart.review.entry.lotComment"/> </label>${entry.lotComment}</c:if></span></p>
					</div>								  
                </div>
                
                <div class="floatLeft column4">                
                    <p><format:price priceData="${entry.basePrice}"/></p>
                </div>
                <div class="floatLeft column5">
                     <p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
                </div>
				<div class="floatLeft column6">
				<p class="center">${entry.shippingMethod}</p>
				<c:if test="${not entry.expandableSchedules}">
					<div class="marTop20 floatLeft marLeft20">
						<commonTags:entryLevelDates entry="${entry}" />
					</div>
				</c:if>
                </div>
               	<%-- ADD FOR SCHEDULE LINES --%>
	            <c:if test="${entry.expandableSchedules}">
	            	<commonTags:scheduleLines scheduleLineList="${entry.scheduleLines}" count="${count.count}" uom="${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})" productName="${entry.product.name}" jjId="${entry.product.code}"/>
	            </c:if>
             </div>
             
           </c:forEach>
            <!--Order Derail Row ends-->
         
            <!--Order Derail Row ends-->            
            </div>
        </div>
   
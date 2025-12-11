<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ attribute name="orderData" required="true" type="com.jnj.facades.data.JnjGTOrderData" %>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>

<div class="orderDetailPanel validateStandard">
        	<div class="orderDetHead noBorderBtm reviewOrder">
       			<div class="hcolumn1"><span><label:message messageCode="cart.validate.lineItem"/></span></div>
                <div class="hcolumn2"><span></span> </div>
				<div class="hcolumn3"><span><label:message messageCode="cart.review.entry.wtg"/></span></div>
                <div class="hcolumn4"><span><label:message messageCode="cart.validate.quantity"/></span></div>
                
                <div class="hcolumn5"><span><label:message messageCode="cart.validate.itemPrice"/></span></div>
				<div class="hcolumn6"><span><label:message messageCode="cart.validate.total"/></span></div>
               
            </div>

   <div class="orderDetBody reviewOrder"> 
	<c:forEach items="${orderData.entries}" var="entry"  varStatus="count">
      	 <c:url value="${entry.product.url}" var="productUrl"/>
           	<c:choose>
           	<c:when test="${count.count  mod 2 == 0}">
           		<c:set var="orderEntriesClass" value="even"> </c:set>
           	</c:when>
          		<c:otherwise>
          			<c:set var="orderEntriesClass" value="odd"> </c:set>
          		</c:otherwise>
          	</c:choose>
            <c:if test="${count.count == 1}" >
				<div class="cartpageloadmore" id="cartpageloadmore${count.count}" style="display:block">
				</div>
			</c:if>
					
          <div id="orderentry-${count.count}" class="orderDetRow ${orderEntriesClass}">
				<div class="floatLeft column1 paddingLeft"> ${count.count}</div>                
				<div class="floatLeft column2">
                  <standardCart:productConsumerValidation entry="${entry}" showRemoveLink="false" />
                </div>
				  <div class="floatLeft column3">                
                   <p><label:message messageCode="cart.validate.weight"/><b>&nbsp;&nbsp;${entry.product.productWeight}</b><b>&nbsp;${entry.weightUOM}</b></p>
                   <p><label:message messageCode="cart.validate.volume"/><b>&nbsp;&nbsp;${entry.product.productVolume}</b><b>&nbsp;${entry.volumeUOM}</b></p>
                   
                </div>
                           
                
                 <div class="floatLeft column4">
						<div class="lbox">
						
							<p><label>${entry.quantity}</label></p>                             
                            <p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>
                            
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
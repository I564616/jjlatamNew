<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="addToCartLabelKey" required="false"
	type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:choose>
	<c:when test="${empty addToCartLabelKey}">
		<c:set value='cart.common.addToCart' var='addToCartLabelKey' />
	</c:when>
	<c:otherwise>
		<c:set value="${addToCartLabelKey}" var='addToCartLabelKey' />
	</c:otherwise>
</c:choose>
<!--AAOL-6531-->
<div class="sectionBlock">
	<div class=" floatLeft ">
		<c:if test="${not empty validationError}">
			<p class="error">
				<spring:message code="${validationError}" />
			</p>
		</c:if>
			<!--4069 story changes starts-->
		<c:if test="${not empty priceError}">	
		<p class="error">
				<spring:message code="cart.common.zeroPrice.error" />
		</p>		
	</c:if>
		<!--4069 story changes ends-->
	</div>

	
		<form:form name="mltiAddToCartForm" id="mltiAddToCartForm"
			action="javascript:;">
	<div class="row jnjPanelbg">
           <div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
            <div class="enter-product-label"><spring:message code="cart.enterproducts.header" /></div>
            <div class="enter-product-label-disc"><spring:message code="cart.commaseperated.text" /></div>
			</div>
           <div class="col-lg-6 col-md-6 col-sm-5 col-xs-12 align-middle">
				<div class="float-right-to-none">
             <div class="price-txt-width">
              <input type="text" class="form-control" name="productId" id="prodCode" placeholder="<spring:message code='cart.productnum.text' />"></input>
             </div> 
             <div class="price-quantity"> 
              <input type="text" id ="prodQty" class="form-control" placeholder="<spring:message code='product.detail.addToCart.quantity' />"></input>
             </div> 
            </div> 
           </div>
           <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 align-middle-pi-btn">
            <div class="full-width-btns">
             <button class="btn btnclsnormal full-width-btns" id="addToCartForm_2"><spring:message code="cart.quote.addtopriceinquiry"/></button>
					<input type="button" style="" id="errorMultiCart"
						class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn"
						value="<spring:message code='homePage.errorDetails' />" />
						<!-- Error Details Button appears after reload the cart page 556-->
						<input type="hidden" value="${errorDetailMap}" id="errorDetailMSG"/>	
				</div>
			</div>
          </div> 
		  <!-- AAOL-6474-->
			<div class="panel-group" id="noProduct">
				<div class="panel panel-danger">
					<div class="panel-heading">
						<div class="panel-title">
						<span class="glyphicon glyphicon-ban-circle"></span>&nbsp; <spring:message	code="cart.productnum.empty" />
						<!-- <span id="noProduct" style="display:none;color:red"><spring:message	code="cart.productnum.empty" /></span> -->
						</div>
					</div>
				</div>										
			</div>
			
          	<div id="noQty" style="display:none;color:red"><spring:message	code="cart.incorrect.Qty.empty" /></div>    
			<div class="row" style="margin-top: 10px">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 nopad-right">
					<div class="registersucess"
						style="color: #3c763d; font-weight: bold"></div>
				</div>
			</div>
		</form:form>
</div>



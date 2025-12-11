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
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false"
	type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="standardCartLa"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
    <c:set var="lengthOfHeaderErrorList" value="${fn:length(headerErrorMessageList)}" />
    <c:set var="lengthOfLineErrors" value="${fn:length(lineErrorMessageMap)}" />
    <input type="hidden" name="freightType" id="freightType" value="${freightType}"/>
    <input type="hidden" name="countryspecificcode" id="countryspecificcode" value="${countryCode}"/>
    <div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12" style="font-size: 45px;">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
            <c:if test="${not empty freightType}">
            <div id="freightypediv" style="display: none;">
        	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-left: -100px;">
              <input type="radio" name="shipping" id="cif" value="CIF" checked> <span style="font-size: 16px; color: red;"><spring:message code="cart.review.CifRadio" /></span>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-left: -100px;">
              <input type="radio" name="shipping" id="fob" value="FOB"> <span style="font-size: 16px; color: red;"><spring:message code="cart.review.FobRadio" /></span>
            </div>
            </div>
            </c:if>
            <c:choose>
                <c:when test="${hideCheckOut == true}">
                <c:choose>
                 <c:when test="${not empty freightType}">
                  <button type="button" class="btn btnclsactive pull-right shippingPageLatamFreight" style="margin-top: -50px;" disabled="disabled"><spring:message code="cart.validate.continueToShipping"/></button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn btnclsactive pull-right shippingPageLatam" " disabled="disabled"><spring:message code="cart.validate.continueToShipping"/></button>
                 </c:otherwise>
                 </c:choose>
                </c:when>
                <c:otherwise>
                 <c:choose>
                 <c:when test="${not empty freightType}">
                  <button type="button" class="btn btnclsactive pull-right shippingPageLatamFreight" style="margin-top: -50px;"><spring:message code="cart.validate.continueToShipping"/></button>
                  </c:when>
                  <c:otherwise>
                    <button type="button" class="btn btnclsactive pull-right shippingPageLatam" ><spring:message code="cart.validate.continueToShipping"/></button>
                     </c:otherwise>
                     </c:choose>
                </c:otherwise>
            </c:choose>
		</div>
	</div>
	<div id="successMessage_cart" style="display: none;">
	    <div class="panel-group hidden-xs hidden-sm" style="margin: 5px 0 20px 0;">
	        <div class="panel panel-success">
		        <div class="panel-heading">
			        <h4 class="panel-title">
				        <span>
				            <span class="glyphicon glyphicon-ok"></span>&nbsp;
					        <spring:message code="text.template.saved.successmessage" />
				        </span>
			        </h4>
		        </div>
	        </div>
        </div>
    </div>

    <%-- Contract info Message --%>
	<c:if test="${cartData.isContractCart}">
		<lacommon:genericMessage contractNumber="${cartData.contractId}"
		messageCode="cart.common.contract.messageInfo" icon="list-alt" panelClass="warning" />
	</c:if>


	<c:choose>
	    <%-- Order validated --%>
        <c:when test="${validateOrderData.validateOrderResponse}">
            <c:if test="${dropshipmentError}">
                <div class="error">
                    <p style="font-size:13px;color:#b41601">
                        <lacommon:genericMessage messageCode="cart.review.dropshipmentError" icon="ban-circle" panelClass="danger" />
                    </p>
                </div>
            </c:if>
            <c:if test="${not dropshipmentError}">
                <div class="success">
                    <p>
                        <lacommon:genericMessage messageCode="cart.review.orderSuccessfullyValidated" icon="ok" panelClass="success" />
                    </p>
                </div>
            </c:if>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
            <c:set value="" var="message"/>
             <c:forEach items="${cartData.entries}" var="productData">
             <c:if  test="${displaySubstitutes eq true}">
                    <c:if test="${not empty productData.product.productStatusCode && productData.product.productStatusCode == 'D3'}">
                    	<c:set value="D3" var="message"/>
                         
                    </c:if>
                   </c:if> 
            </c:forEach>
            <c:if test="${message eq 'D3'}">
              <h5 class="bg-warning p-2">
                               <spring:message code="cart.product.obselete.warningMessage"/>
                           </h5>
             </c:if>
             
             </div>
        </c:when>

        <%-- Order NOT validated --%>
        <c:otherwise>
            <c:choose>
                <c:when test="${validateOrderData.sapErrorResponse}">
                    <c:choose>
                        <c:when test="${lengthOfHeaderErrorList > 0}">
                            <c:forEach items="${headerErrorMessageList}" var="errorMessage">
                           		 <c:choose>
	                            	<c:when test="${errorMessage =='incorrectShipToSelected'}">
	                            	   <lacommon:genericMessage messageCode="cart.review.invalid.shipto" icon="ban-circle" panelClass="danger" />
	                            	</c:when>
	                            	<c:otherwise>
	                            		<lacommon:genericMessage messageCode="${errorMessage}" icon="ban-circle" panelClass="danger" />
	                            	</c:otherwise>
                                 </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="error">
                                <lacommon:genericMessage messageCode="cart.review.sapCustomizedMessage" icon="ban-circle" panelClass="danger" />
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                            <%-- Line Errors --%>
                        <c:when test="${lengthOfLineErrors > 0}">
                            <c:forEach items ="${lineErrorMessageMap}" var="lineError">
                                <c:forEach items ="${lineError.value}" var="lineErrorMessage">
                                    <lacommon:genericMessage messageCode="${lineError.key} ${lineErrorMessage}" icon="ban-circle" panelClass="danger" />
                                </c:forEach>
                            </c:forEach>
                        </c:when>
                    </c:choose>

                </c:when>
                <c:otherwise>
                    <div class="error">
                        <p>
                            <c:if test="${ not validateOrderData.validateOrderResponse}">
                                <lacommon:genericMessage messageCode="cart.review.orderNotValidated" icon="ban-circle" panelClass="danger" />
                            </c:if>
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

	<div class="mainbody-container">
        <c:choose>
            <c:when test="${splitCart}">
                <c:forEach items="${jnjCartDataList}" var="jnjCartData" varStatus="count">
                    <standardCartLa:multiValidateCartsEntries dropshipmentErrorData="${dropshipmentErrorProductMap}" jnjCartData="${jnjCartData}" varRowCount="${count.count}"  validationPage="true" />
                </c:forEach>
                <standardCartLa:cartTotals />
            </c:when>
            <c:otherwise>
                <standardCartLa:singleValidateCartEntry dropshipmentErrorData="${dropshipmentErrorProductMap}" validationPage="true" />
            </c:otherwise>
        </c:choose>
	</div>
	<cartLa:saveAsTemplateDiv/>
	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
				<button type="button" class="btn btnclsnormal checkout-clear-cart"
					id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button>
			</div>

            <c:choose>
                <c:when test="${not empty freightType}">
                   <div class="float-right-to-none">
				<button type="button" class="btn btnclsnormal templatebtn saveorderastemplatelatam"  disabled="disabled">
					<spring:message code="cart.review.shoppingCart.template" />
				</button>
				<c:choose>
					<c:when test="${hideCheckOut == true}">
						<button type="button" class="btn btnclsactive shippingPageLatamFreight" disabled="disabled"><spring:message code="cart.validate.continueToShipping"/></button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btnclsactive shippingPageLatamFreight"><spring:message code="cart.validate.continueToShipping"/></button>
					</c:otherwise>
				</c:choose>
			</div>
                </c:when>
                <c:otherwise>
                   <div class="float-right-to-none">
				<button type="button" class="btn btnclsnormal templatebtn saveorderastemplatelatam"  disabled="disabled">
					<spring:message code="cart.review.shoppingCart.template" />
				</button>
				<c:choose>
					<c:when test="${hideCheckOut == true}">
						<button type="button" class="btn btnclsactive shippingPageLatam" disabled="disabled"><spring:message code="cart.validate.continueToShipping"/></button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btnclsactive shippingPageLatam"><spring:message code="cart.validate.continueToShipping"/></button>
					</c:otherwise>
				</c:choose>
			</div>
                </c:otherwise>
            </c:choose>
            
			
		</div>
	</div>
</div>




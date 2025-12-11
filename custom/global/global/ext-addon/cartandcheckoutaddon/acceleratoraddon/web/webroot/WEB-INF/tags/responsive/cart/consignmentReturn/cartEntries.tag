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
<%@ taglib prefix="breadcrumb"  tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="returnCart"  tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentReturn"%>
<c:url value="/cart/simulateOrderSecondSAPCall" var="sapSecondCallFormUrl" />
<script type="text/javascript"> // set vars
/*<![CDATA[*/
var cartRemoveItem = true;
/*]]>*/

</script>

<!-- replenish/cartEntries.tag -->
 <form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
 <c:if test="${fn:length(dropShipAccounts) eq 0}">
  <c:set value="strictHide" var="hideSelectDropShip"/>
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>
<c:url value="/cart/changeOrderType" var="changeOrderTypeURL" />
<form:form method="post" action="${changeOrderTypeURL}" id="changeOrderTypeForm"></form:form>
<div id="AddItemsCartpage">
<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
  <div class="row content">
    <div class="col-lg-6 col-md-6 col-sm-5 col-xs-12">
      <spring:message code="cart.review.shoppingCart" />
    </div>
    <div class="col-lg-6 col-md-6 col-sm-7 col-xs-12">
      <div class="float-right-to-none">
      <div class="orderType-holder">
          <span class="orderType-label">
            <spring:message code="cart.common.orderType" />
          </span>
          <div class="orderType-dropdown">
            <select id="changeOrderType" name="orderType" data-width="100%">
              <option value="${cartData.orderType}"> <spring:message code="cart.common.orderType.${cartData.orderType}"></spring:message></option>
              <c:forEach items="${orderTypes}" var="orderType">
                <c:if test="${cartData.orderType ne orderType}">
                  <option value="${orderType}"><spring:message code="cart.common.orderType.${orderType}"></spring:message></option>
                </c:if>
              </c:forEach>
            </select>
          </div>
        </div>
        <!-- No charge Order AAOL-3392 -->
        <c:choose>
          <c:when test="${cartorderType eq 'ZNC' }">
            <button type="button" class="btn btnclsactive cartStep1Saveupdate1NoCharge">
              <spring:message code="cart.review.progressBar.validate" />
            </button>
          </c:when>
          <c:otherwise>
            <button type="button" form="consignmentReturnForm" class="btn btnclsactive consignmentReturnValidate" id="consignmentReturnValidate"><spring:message code="cart.review.progressBar.validate"/></button>
          </c:otherwise>
        </c:choose>
        <!-- No charge Order AAOL-3392 -->
      </div>
    </div>
  </div>
  <c:if test="${not empty validationError}">
    <div class="error">
    <p style="color:red">
        <spring:message code="dropshipment.error.not.found" />
    </p>
    </div>
  </c:if>
<!-- flash message for contract product -->
<div class="panel-group contract-product-show" style="margin-bottom:20px" >
  <div class="panel panel-success">
    <div class="panel-heading">
      <h4 class="panel-title">
      <spring:message code="cart.review.popup.text"/>:&nbsp;<span id="contract_product_msg"></span> 
      </h4> 
    </div>
  </div>
</div>
<c:if test="${not empty sapErrorResponse}">
   <div class="panel-group ">
		<div class="alertBroadcast broadcastMessageContainer panel panel-danger">
			<div class="panel-heading">
				<div class="panel-title">	
					<div class="row">
						<div class=" col-lg-11" >
							<span class="glyphicon glyphicon-ban-circle"></span>
							<spring:message code="cart.sapError.response" />
						</div>
					</div>
				</div>
			</div>
		</div>										
</div>
</c:if>



<div class="mainbody-container" id="consignmentOrderPage">
    <div class="row jnjPanelbg">
      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <div class="text-left">
          <spring:message code="header.information.cart.number"/>: <span>${user.currentB2BUnitID},${user.currentB2BUnitName}</span>
        </div>
      </div>
    </div>
  <input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}"/>
  <input type="hidden" name="defaultChekAddid" id = "defaultChekAddid" value="${defaultChekAddid}">
  <!-- Added for Shipping address Display in Validation page -->
  <commonTags:Addresses/>
  <returnCart:cartPageHeader currentPage="cartPage"/>
  <commonTags:cartErrors/>
  <commonTags:changeAddressDiv/> 
  <commonTags:changeBillToAddress/>
  <!-- No charge Order AAOL-3392 -->
  <%-- <c:if test="${cartorderType eq 'ZNC' }">
    <div class="row jnjPanelbg">
      <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
        <div class="txt-label-inline no-charge-reason">
          Purchase Order# <span class="redStar">*</span>
        </div>
        <div class="txt-box-inline">
          <div class="txt-box-inline special-instr-txt">
            <input id="purchOrder" type="text" class="form-control  ${disabled}" value="${cartData.purchaseOrderNumber}"/>
          </div>
        </div>
        </div>
        <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
          <div class="txt-label-inline no-charge-reason">No Charge Reason Code# <span class="redStar">*</span></div>
          <div class="txt-box-inline no-charge-select">
            <select class="selectnoChargepicker" data-width="100%" id="noChargeReasonCode" name="noChargeReasonCode">
              <option value="">
                  <spring:message code="cart.review.orderInfo.selectReason" />
              </option>
              <c:forEach var="reasonCode" items="${reasonCodeNoCharge}">
                <option value="${reasonCode.key}"   ${reasonCode.key == cartData.reasonCode ? 'selected="selected"' : ''}   >${reasonCode.value}</option>
              </c:forEach>
            </select>
          </div>  
          <div class="registerError " style="color:red"></div>
        </div>
        <div class="registerPOError" style="color: red;vertical-align: middle;"></div>
        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margin-top-row-gap">
          <div class="txt-label-inline dropshipAccntLabel">Drop Ship Account</div>
          <div class="txt-box-inline">
            <input type="text" class="form-control" id="dropShip" value="${cartData.dropShipAccount}">
          </div>
          <div class="txt-box-inline">
          <a href="#"  data-toggle="modal" >
          <span class="changeShipAddLighboxLink" data-url="${dropShipURL}"> 
            <button class="drop-ship-account-list-icon ${hideSelectDropShip}  fa fa-list" id="drop-ship-account-list-icon"></button>
            </span>
            </a>
          </div>
          <div id="errorMsgDiv" style="color: red"></div>
        </div>  
      </div>  
      <div id="dropShipAccountholder"></div>
    </div>
  </c:if> --%>
  <!-- No charge Order AAOL-3392 -->
  
  
  <%-- <div class="row jnjPanelbg">
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">Customer PO Number<sup class="star">*</sup></label>
                        <div class="pull-left form-consignment-input-select">
                          <!-- <input class="form-control"> -->
                           <input type="text" id ="customerPONo"  class="form-control"></input>
                        </div>  
                      </div>                      
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">Stock User<sup class="star">*</sup></label>
                        <select class="form-control form-element form-consignment-select-large" id ="stockUser">
                          <option value="000011142">000011142 </option>                     
                        </select>
                      </div>
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 clearBoth margintop20px">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">End User<sup class="star">*</sup></label>
                        <select class="form-control form-element form-consignment-select-large" id ="endUser">
                          <option value="000011142">000011142 </option>                 
                        </select>
                      </div>                        
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">PO Date<sup class="star">*</sup></label>
                        <div class="input-group form-element form-element-date">
                          <input id="poDate" name="poDate" placeholder="Select date" class="date-picker form-control" type="text">
                          <label for="poDate" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
                        </div>  
                      </div>  
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">Request Delivery Date<sup class="star">*</sup></label>
                        <div class="input-group form-element form-element-date">
                          <input id="requestDelDate" name="requestDelDate" placeholder="Select date" class="date-picker form-control" type="text">
                          <label for="requestDelDate" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
                        </div>  
                      </div>  
                      <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
                        <label class="pull-left boldtext label-lineHeight form-consignment-label-select">Shipping Instructions</label>
                        <textarea class="form-control  form-consignment-input-select" rows="" id="comment"></textarea>
                      </div>
                      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">                       
                        <div class="pull-right margintop20px">
                          <a href="#">Import From Excel</a> | <a href="#">Export To Excel</a>                       
                        </div>
                      </div>
                    </div> --%>
  
  
  <div class="row jnjPanelbg">
  
    <div id="noProduct" style="display:none;color:red"><spring:message  code="cart.productnum.empty" /></div>    
    <div id="noQty" style="display:none;color:red"><spring:message  code="cart.incorrect.Qty.empty" /></div>      
      <form:form name="mltiAddToCartForm" id="mltiAddToCartForm" action="javascript:;">
        <div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
        <div class="enter-product-label"><spring:message code="cart.enterproducts.header" /></div>
        <div class="enter-product-label-disc"><spring:message code="cart.commaseperated.text" /></div>    
      </div>
      <div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle marginProd">
        <%-- <label class="price-no-label"><spring:message
          code="cart.quote.product.number" /></label><input type="text" id ="prodCode" class="form-control price-no-txt"></input><input type="text" id ="prodQty" class="form-control price-noqty-txt"></input>
         --%>
        <div class="float-right-to-none">
                 <div class="price-txt-width">
                   <input type="text" id ="prodCode"  class="form-control" placeholder="Product Numbers"></input>
                 </div> 
                 <div class="price-quantity"> 
                   <input type="text"  id ="prodQty" class="form-control" placeholder="Quantity"></input>
                 </div> 
          </div>  
     </div>
    <div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
      <div class="full-width-btns">
        <input type="button"  class="btn btnclsnormal new-add-tocart" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />">
        <input type="button" style="" id="errorMultiCart" class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn" value="<spring:message code='homePage.errorDetails' />" />
  		<!-- Error Details Button appears after reload the cart page Apac-UAT-556-->
		<input type="hidden" value="${errorDetailMap}" id="errorDetailMSG"/>	
  <!--          <button class="btn btnclsnormal full-width-btns" >Add to Cart</button> -->
  <!--          <button class="btn btnclsnormal" id="error-detail-btn" data-target="#error-detail-popup" data-toggle="modal">Error Details</button> -->
      </div>
    </div>  
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top:8px">
      <div class="registersucess registersucess-style"></div>
    </div>
  </form:form>      
  </div>  
  <!-- Desktop -->
  <div class="hidden-xs ">
    <table id="datatab-desktop" class="table table-bordered table-striped sorting-table error-on-top">
      <thead>
        <tr>
          <th class="no-sort text-uppercase">#</th>     
          <th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
          <th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
             <div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
          </th>
          <%-- <th class="no-sort"><spring:message code="cart.validate.unitPrice"/></th>
          <th class="no-sort multitotal-head paddingleft10px"><spring:message code="cart.review.entry.total"/></th> --%>
           <th class="no-sort text-center text-uppercase est-dateCol"><spring:message code="Estimated.Date"/></th>
          <th class="no-sort text-uppercase"><spring:message code="cart.priceQuote.status"/></th>  
        </tr>
      </thead>
      
      <!-- Change for GTUX_1259  for recently added product to top -->
      
      <tbody id="AddItemsCartpage" >
        <c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
        <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
                  <!-- Soumitra - Added for showing SAP errors received upon validation from validateorder method AAOL-4357 -->
                      <c:if test="${not empty sapErrorResponse}">
                        <div style="color: red;">
                          <c:set var="errorString" value=""></c:set>
                          <c:forEach var="sapErrorforProduct" varStatus="countProductError"
                            items="${sapErrorResponse[cartData.entries[fieldLength - count.count].product.code]}">
                            <c:set var="errorString" value="${errorString},${sapErrorforProduct.id}"></c:set>
                          </c:forEach>
                        </div>
                      </c:if>
        <tr id="orderentry- ${cartData.entries[fieldLength - count.count]}" class="shoppingcartOrderEntryList">
        <td class="vertical-align-top text-center">${count.count}</td>
          <td class="text-left">
            <standardCart:productDescriptionBeforeValidation entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}" showRemoveLink="true"  showStatus="false"  rowcount="${count.count}"/>
          </td>
          <td>
            <div class="cart-update-link">  <a href="javascript:void();" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_update" entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1" ><spring:message code="cart.review.productDesc.updateItem"/></a></div> 
                   <c:url value="/cart/update" var="cartUpdateFormAction" />
              <form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_desktop" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
                <input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}"/>
                <input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}"/>
                <input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}"/>
                <div>
                  <ycommerce:testId code="cart_product_quantity">
                      <form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
                    <form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}"  type="text" 
                     id="quantity${cartData.entries[fieldLength - count.count].entryNumber}" 
                     entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" 
                     class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
                  </ycommerce:testId>
                </div>                              
                  <p class="thirdline"><strong><spring:message code="product.detail.addToCart.unit"/> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit} (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})</p> 
                <ycommerce:testId code="cart_product_removeProduct">
                  <p>
                    <a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
                      class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/>
                    </a>
                  </p>
                </ycommerce:testId> 
                <p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p>
              </form:form><c:if test="${fn:contains(errorString, 'NOQTY')}">
                  <div class="error">
                    <spring:message code="no.stock" />
                  </div>
                </c:if>
            </td> 
          <!-- <td></td>
          <td></td> -->
          <td></td>
          <td></td> 
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div> 
  <div class="sapSecondCallData">
      <form:form action="${sapSecondCallFormUrl}" method="POST"   id="simulateOrderForm" name="simulateOrderForm" commandName="simulateOrderForm">
        <!-- <input style="display:none;" type="hidden"  id="isfirstSAPCall" name="isfirstSAPCall"  />
        <input style="display:none;" type="hidden"  id="isSecondSAPCall" name="isSecondSAPCall"  />
        <input style="display:none;" type="hidden"  id="isRefreshCall" name="isRefreshCall"  /> -->
        <!-- <input   type="text"  id="jnjGTProposedOrderResData" name="jnjGTProposedOrderResData"  />  -->
      </form:form>
    </div>
  
  
  
  
  <%-- <div class="hidden-xs">
                      <table id="datatab-desktop" class="table table-bordered table-striped sorting-table">
                        <thead>
                          <tr>                          
                          <th class="no-sort text-uppercase">#</th>
                          <th class="no-sort text-uppercase quanity-cell">Product code</th>
                          <th class="no-sort text-uppercase quanity-cell">Product Description</th>
                          <th class="no-sort text-uppercase">Quantity</th>
                          <th class="no-sort text-uppercase">UoM</th>
                          <th class="no-sort text-center text-uppercase est-dateCol">Estimated Date</th>
                          <th class="no-sort text-uppercase">Status</th> 
                          </tr>
                        </thead>
                        <tbody>
                        <c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
                        <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
                          <tr>
                            <td class="vertical-align-top text-center">${count.count}</td>
                            <td class="text-center">${entry.product.code}</td>
                            <td class="text-left valign-top">
                              <div class="display-row">
                                <div class="table-cell">
                                  <c:choose>
                                      <c:when test="${!entry.product.isProdViewable || empty productUrl}">
                                        <product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
                                      </c:when>
                                      <c:otherwise>
                                        <a href="javascript:;" class="showProductDeatils anchor-img" data="${productCode}"><product:productPrimaryImage product="${entry.product}" format="cartIcon" /></a>
                                      </c:otherwise>
                                  </c:choose>
                                </div>
                                <div class="table-cell">
                                  <div class="Tablesubtxt">
                                    <p class="firstline">Lactaid<sup>@</sup> Fast Act Caplets, 32 Count</p>                                   
                                    <div>J&J ID#: ${entry.product.code}</div>
                                    <div>GTIN#: ${entry.product.gtin}</div>                                                             
                                  </div>
                                </div>  
                              </div>
                            </td>
                            <td class="quanity-cell">                             
                              <div><input type="text" class="form-control txtWidth" value="${cartData.entries[fieldLength - count.count].quantity}"></div>
                              <!-- <p class="thirdline"><strong>Unit:</strong> Case (1 each)</p> -->
                              <a href="#">Remove</a>
                            </td>
                            <td>DZ</td>
                            <td class="text-center"></td>
                            <td> </td>
                          </tr>
                          </c:forEach>
                        </tbody>
                      </table>
                    </div>
   --%>
  
  
  
  
  
  
  <!-- Error Details Popup Start -->
  <div class="modal fade jnj-popup" id="error-detail-popup" role="dialog">
    <div class="modal-dialog modalcls modal-md">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
          <h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
        </div>
        <form:form method="post" action="javascript:;">
          <div class="modal-body">
            <div class="row">
              <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <div class="panel-group">
                  <div class="panel panel-danger">
                      <div class="panel-heading">
                      <h4 class="panel-title">
                      <span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="homePage.errordetails.addfailed" /></span>
                      </h4>
                      </div>
                  </div>  
                </div>
              </div>
              <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <div class="scroll error-content" style="font-weight:bold">
                </div>
              </div>
            </div>
          </div>
        </form:form>
      </div>
    </div>
  </div>
  <!-- Error Details Popup End -->
  <!-- Table collapse for mobile device-->
  <div class=" Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
    <table id="datatab-mobile" class="table table-bordered table-striped sorting-table error-on-top" >
      <thead>
        <tr>
          <th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
          <th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
            <div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
          </th>
        </tr>
      </thead>
      <tbody>
      <c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
      <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
        <tr>
         <form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_mobile" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
          <td class="text-left">
            <standardCart:productDescriptionBeforeValidation entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}"  showRemoveLink="true" showStatus="false" rowcount="${count.count}"/>
            
            <div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
              <c:url value="/cart/update" var="cartUpdateFormAction" />
              <div class="sub-details-row">
                <%-- <form> --%>
                  <input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}"/>
                  <input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}"/>
                  <input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}"/>
                  <!-- qty -->                             
              </div>
              <p><spring:message code="cart.validate.unitPrice"/></p>
              <p></p>
              <p><spring:message code="cart.review.entry.total"/></p>
              <p></p>
            </div>  
          </td>
          <td>
             <div class="cart-update-link">  <a href="#" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile_update" entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile" ><spring:message code="cart.review.productDesc.updateItem"/></a></div> 
            <ycommerce:testId code="cart_product_quantity">
              <form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
              <form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}"  type="text"  id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile" 
              entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" 
              class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
            </ycommerce:testId> 
            <p><strong><spring:message code="product.detail.addToCart.unit"/> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit} (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})</p> 
            <ycommerce:testId code="cart_product_removeProduct">
              <a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}" class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/></a>
            </ycommerce:testId>
            <p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p> 
          </td>
          </form:form>
        </tr>
      </c:forEach>
        </tbody>
      </table>
  </div>
  <!-- Start - Total Price Summary -->
<%--  <div class="row basecontainer">
    <table class="total-summary-table">
      <tr>
        <td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
        <td class="total-summary-cost totalrps no-right-pad">--</td>
      </tr>
      <tr class="summary-bline">
        <td class="total-summary-label"><spring:message code="cart.review.entry.shipping"/></td>
        <td class="total-summary-cost">--</td>
      </tr>
      <tr class="total-price-row">
        <td class="total-summary-label"><spring:message code="cart.validate.total"/></td>
          <td class="total-summary-cost totalsum no-right-pad">
           <sup class="supmd">--</sup> 
          </td>
        </tr>
      </table>
  </div> --%>
</div>
 <returnCart:cartPageActions/> 
  <!-- End - Total Price Summary -->
  <%-- <div class="row validatebtn">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
      <div class="float-left-to-none">
          <button type="button" class="btn btnclsnormal checkout-clear-cart" id="RemoveCartData"> <spring:message code="cart.payment.clearCart"/></button>
        </div> 
      <div class="float-right-to-none">
          <c:set value="saveorderastemplate" var="classForSaveTemplate"/>
            <button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}"><spring:message code="cart.review.cartPageAction.saveTemplate"/></button>
          <button type="button" class="btn btnclsactive cartStep1Saveupdate1" ><spring:message code="cart.review.progressBar.validate"/></button>
      </div>
    </div>
  </div> --%>
</div>
<!--  Changes Made for PurChase Order Pop Up -->
<div class="modal fade jnj-popup" id="validateOrderDivId-popup" role="dialog">
  <div class="modal-dialog modalcls modal-md" id="validateOrderPOpopup">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
        <h4 class="modal-title"><spring:message code="cart.review.validateOrder"/></h4>
      </div>
      <div class="modal-body">
        <spring:message code="cart.review.poNumber"/>
      </div>
      <div class="modal-footer">
      <c:url value="/cart/validate" var="orderValidateUrl" />
        <a href="#"  class="pull-left"  data-dismiss="modal">Cancel</a>
      <button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" onclick="location.href='${orderValidateUrl}'">Validate Order</button>
      </div>  
    </div>
  </div>
</div>
<!--  Changes Made for PurChase Order Pop Up -->
<!-- AAOL-2405 start-->
<div id="proposed-line-item-holder"></div>
<!-- AAOL-2405 end-->

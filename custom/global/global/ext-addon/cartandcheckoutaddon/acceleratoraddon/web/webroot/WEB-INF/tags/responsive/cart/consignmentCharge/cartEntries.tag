<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%-- <%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%> --%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="consignmentChargeCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentCharge"%>
<c:url value="/cart/simulateOrderSecondSAPCall" var="sapSecondCallFormUrl" />
<script type="text/javascript">
	// set vars
	/*<![CDATA[*/
	var cartRemoveItem = true;
	/*]]>*/
</script>
<!-- consignmentCharge/cartEntries.tag -->
<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" commandName="UpdateMultipleEntriesInCartForm">
</form:form>

  

<c:if test="${fn:length(dropShipAccounts) eq 0}">
  <c:set value="strictHide" var="hideSelectDropShip" />
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL" />
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
          <span class="orderType-label"> <spring:message code="cart.common.orderType" />
          </span>
          <div class="orderType-dropdown">
            <select id="changeOrderType" name="orderType" data-width="100%">
              <option value="${cartData.orderType}">
                <spring:message code="cart.common.orderType.${cartData.orderType}"></spring:message></option>
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
            <button type="button" form="consignmentChargeForm" class="btn btnclsactive consignmentChargeCartValidate"
              id="consignmentChargeCartValidate">
              <spring:message code="cart.review.progressBar.validate" />
            </button>
          </c:otherwise>
        </c:choose>
        <!-- No charge Order AAOL-3392 -->
      </div>
    </div>
  </div>
  <c:if test="${not empty validationError}">
    <div class="error">
      <p style="color: red">
        <spring:message code="dropshipment.error.not.found" />
      </p>
    </div>
  </c:if>
  <!-- flash message for contract product -->
  <div class="panel-group contract-product-show" style="margin-bottom: 20px">
    <div class="panel panel-success">
      <div class="panel-heading">
        <h4 class="panel-title">
          <spring:message code="cart.review.popup.text" />
          :&nbsp;<span id="contract_product_msg"></span>
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
          Account Number: <span>${user.currentB2BUnitID}, ${user.currentB2BUnitName}</span>
        </div>
      </div>
    </div>
    <input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}" /> <input type="hidden"
      name="defaultChekAddid" id="defaultChekAddid" value="${defaultChekAddid}">
    <!-- Added for Shipping address Display in Validation page -->
    <commonTags:Addresses />
    <consignmentChargeCart:cartPageHeader currentPage="cartPage" />
    <commonTags:cartErrors />
    <commonTags:changeAddressDiv />
    <commonTags:changeBillToAddress />
    <div class="row jnjPanelbg">
      <div id="noProduct" style="display: none; color: red">
        <spring:message code="cart.productnum.empty" />
      </div>
      <div id="noQty" style="display: none; color: red">
        <spring:message code="cart.incorrect.Qty.empty" />
      </div>
      <form:form name="mltiAddToCartForm" id="mltiAddToCartForm" action="javascript:;">
        <div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
          <div class="enter-product-label">
            <spring:message code="cart.enterproducts.header" />
          </div>
          <div class="enter-product-label-disc">
            <spring:message code="cart.commaseperated.text" />
          </div>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle marginProd">
          <%-- <label class="price-no-label"><spring:message
          code="cart.quote.product.number" /></label><input type="text" id ="prodCode" class="form-control price-no-txt"></input><input type="text" id ="prodQty" class="form-control price-noqty-txt"></input>
         --%>
          <div class="float-right-to-none">
            <div class="price-txt-width">
              <input type="text" id="prodCode" class="form-control" placeholder="Product Numbers"></input>
            </div>
            <div class="price-quantity">
              <input type="text" id="prodQty" class="form-control" placeholder="Quantity"></input>
            </div>
          </div>
        </div>
        <div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
          <div class="full-width-btns">
            <input type="button" class="btn btnclsnormal new-add-tocart" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />">
            <input type="button" style="" id="errorMultiCart" class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn"
              value="<spring:message code='homePage.errorDetails' />" />
             <!-- Error Details Button appears after reload the cart page Apac-UAT-556-->
			 <input type="hidden" value="${errorDetailMap}" id="errorDetailMSG"/>	
            <!--          <button class="btn btnclsnormal full-width-btns" >Add to Cart</button> -->
            <!--          <button class="btn btnclsnormal" id="error-detail-btn" data-target="#error-detail-popup" data-toggle="modal">Error Details</button> -->
          </div>
        </div>
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top: 8px">
          <div class="registersucess registersucess-style"></div>
        </div>
      </form:form>
    </div>
    <!-- Desktop -->
    <div class="hidden-xs ">
      <table id="datatab-desktop" class="table table-bordered table-striped sorting-table error-on-top consignmentCharge">
        <thead>
          <tr>
            <th class="no-sort text-center" style="padding-left: 8px !important; text-align: center !important"><spring:message
                code="cart.review.entry.number" /></th>
            <th class="no-sort text-left"><spring:message code="cart.validate.product" /></th>
            <th class="no-sort"><spring:message code="cart.review.entry.quantity" />
              <div class="cart-update-all-link">
                <a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message
                    code="cart.review.entry.Updateall" /></a>
              </div></th>
            <th class="no-sort text-center text-uppercase ChargeBatchNo"><spring:message code="cart.validate.batchNo" /></th>
            <th class="no-sort text-center text-uppercase ChargeBatchNo"><spring:message code="cart.validate.serialNo" /></th>
            <th class="no-sort"><spring:message code="cart.validate.unitPrice" /></th>
            <th class="no-sort multitotal-head paddingleft10px"><spring:message code="cart.review.entry.totalPrice" /></th>
          </tr>
        </thead>
        <!-- Change for GTUX_1259  for recently added product to top -->
        <tbody id="AddItemsCartpage">
          <c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
          <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
            <c:if test="${not empty sapErrorResponse}">
              <div style="color: red;">
                <c:set var="errorString" value=""></c:set>
                <c:forEach var="sapErrorforProduct" varStatus="countProductError"
                  items="${sapErrorResponse[cartData.entries[fieldLength - count.count].product.code]}">
                  <c:set var="errorString" value="${errorString},${sapErrorforProduct.id}"></c:set>
                </c:forEach>
              </div>
            </c:if>
            <tr id="orderentry-${cartData.entries[fieldLength - count.count].entryNumber}" class="shoppingcartOrderEntryList">
              <td width="" class="vertical-align-top text-center" style="padding-left: 8px !important">${count.count}<c:choose>
                  <c:when test="${cartData.entries[fieldLength - count.count].quantity>1}">
                    <div>
                      <a class="chargeCopyLine" id="${cartData.entries[fieldLength - count.count].entryNumber}" href="#"><b><spring:message code="consignment.Charge.Copy" /></b></a>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <a class="chargeCopyLine" id="${cartData.entries[fieldLength - count.count].entryNumber}" href="#"
                        style="pointer-events: none; color: #ddd;"><b><spring:message code="consignment.Charge.Copy" /></b></a>
                    </div>
                  </c:otherwise>
                </c:choose> <c:if test="">
                </c:if>
              </td>
              <td class="text-left" width="33%"><consignmentChargeCart:productDescriptionBeforeValidation
                  entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}" showRemoveLink="true" showStatus="false"
                  rowcount="${count.count}" /></td>
              <td>
                <div class="cart-update-link">
                  <a href="javascript:void();" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_update"
                    entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1"><spring:message
                      code="cart.review.productDesc.updateItem" /></a>
                </div> <c:url value="/cart/update" var="cartUpdateFormAction" /> <form:form
                  id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_desktop" action="${cartUpdateFormAction}" method="post"
                  commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
                  <input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}" />
                  <input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}" />
                  <input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}" />
                  <div>
                    <ycommerce:testId code="cart_product_quantity">
                      <form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
                      <form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}" type="text"
                        id="quantity${cartData.entries[fieldLength - count.count].entryNumber}"
                        entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" class=" qtyUpdateTextBox form-control txtWidth"
                        path="quantity" />
                    </ycommerce:testId>
                  </div>
                  <p class="thirdline">
                    <strong><spring:message code="product.detail.addToCart.unit" /> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit}
                    (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})
                  </p>
                  <ycommerce:testId code="cart_product_removeProduct">
                    <p>
                      <a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
                        class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem" /> </a>
                    </p>
                  </ycommerce:testId>
                  <p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p>
                </form:form> <c:if test="${fn:contains(errorString, 'NOQTY')}">
                  <div class="error">
                    <spring:message code="no.stock" />
                  </div>
                </c:if>
              </td>
              <td class=""><c:choose>
                  <c:when test="${not empty consInventoryData}">
                    <c:forEach items="${consInventoryData}" var="consInventory">
                      <c:set var="selectedBatch" value=""></c:set>
                      <c:if test="${cartData.entries[fieldLength - count.count].product.code eq consInventory.productCode}">
                        <c:choose>
                          <c:when test="${consInventory.batchManaged eq '1' && consInventory.serialManaged eq '1'}">
                            <select class="batchNumber selectpicker"
                              id="bn_${cartData.entries[fieldLength - count.count].product.code}_${cartData.entries[fieldLength - count.count].entryNumber}">
                              <c:forEach items="${consInventory.batchSerialMap}" var="entry1" varStatus="count1">
                                <c:choose>
                                  <c:when test="${cartData.entries[fieldLength - count.count].batchNumber eq entry1.key}">
                                    <option value="${entry1.key}" selected="selected">${entry1.key}</option>
                                    <c:set var="selectedBatch" value="${entry1.key}"></c:set>
                                  </c:when>
                                  <c:otherwise>
                                    <option value="${entry1.key}">${entry1.key}</option>
                                  </c:otherwise>
                                </c:choose>
                              </c:forEach>
                            </select>
                            <c:forEach items="${consInventory.batchSerialMap}" var="entry1" varStatus="count1">
                              <input type="hidden" value="${entry1.value}" class="batchSerialMap" name="batchSerialMap" id="${entry1.key}">
                            </c:forEach>
                            <div class="batch-text">
                              <spring:message code="consignment.charge.batchno.not.displayed" />
                            </div>
                            <c:if test="${fn:contains(errorString, 'BATCH_NOT_FOUND')}">
                              <div class="error">
                                <spring:message code="batch.not.found" />
                              </div>
                            </c:if>
                          </c:when>
                          <c:when test="${consInventory.batchManaged eq '1'}">
                            <select class="batchNumber selectpicker"
                              id="bn_${cartData.entries[fieldLength - count.count].product.code}_${cartData.entries[fieldLength - count.count].entryNumber}">
                              <c:forEach items="${consInventory.batchNumbers}" var="entry1" varStatus="count1">
                                <c:choose>
                                  <c:when test="${cartData.entries[fieldLength - count.count].batchNumber eq entry1}">
                                    <option value="${entry1}" selected="selected">${entry1}</option>
                                    <c:set var="selectedBatch" value="${entry1}"></c:set>
                                  </c:when>
                                  <c:otherwise>
                                    <option value="${entry1}">${entry1}</option>
                                  </c:otherwise>
                                </c:choose>
                              </c:forEach>
                            </select>
                            <div class="batch-text">
                              <spring:message code="consignment.charge.batchno.not.displayed" />
                            </div>
                            <c:if test="${fn:contains(errorString, 'BATCH_NOT_FOUND')}">
                              <div class="error">
                                <spring:message code="batch.not.found" />
                              </div>
                            </c:if>
                          </c:when>
                          <c:otherwise>
                            <input class="notBatchManaged" type="hidden">
                          </c:otherwise>
                        </c:choose>
                      </c:if>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                  <input id="bn_${cartData.entries[fieldLength - count.count].entryNumber}" type="hidden" value="${cartData.entries[fieldLength - count.count].batchNumber}">
                ${cartData.entries[fieldLength - count.count].batchNumber}<div class="batch-text">
                      </br>
                      <spring:message code="consignment.Charge.batch.notification" />
                    </div>
                  </c:otherwise>
                </c:choose> <c:if test="${not empty consInventoryData}">
                </c:if></td>
              <td class=""><c:choose>
                  <c:when test="${not empty consInventoryData}">
                    <c:forEach items="${consInventoryData}" var="consInventory">
                      <c:if test="${cartData.entries[fieldLength - count.count].product.code eq consInventory.productCode}">
                        <c:choose>
                          <c:when test="${consInventory.serialManaged eq '1' && consInventory.batchManaged eq '1'}">
                            <select class="serialNumber selectpicker"
                              id="sn_${cartData.entries[fieldLength - count.count].product.code}_${cartData.entries[fieldLength - count.count].entryNumber}">
                              <c:if test="${selectedBatch eq ''}">
                                <c:forEach items="${consInventory.batchSerialMap}" var="entryBatch" end="1">
                                  <c:set var="selectedBatch" value="${entryBatch.key}"></c:set>
                                </c:forEach>
                              </c:if>
                              <c:forEach items="${consInventory.batchSerialMap[selectedBatch]}" var="entry2" varStatus="count2">
                                <c:choose>
                                  <c:when test="${cartData.entries[fieldLength - count.count].serialNumber eq entry2}">
                                    <option value="${entry2}" selected="selected">${entry2}</option>
                                  </c:when>
                                  <c:otherwise>
                                    <option value="${entry2}">${entry2}</option>
                                  </c:otherwise>
                                </c:choose>
                              </c:forEach>
                            </select>
                            <div class="batch-text">
                              <spring:message code="consignment.charge.batchno.not.displayed" />
                            </div>
                            <c:if test="${fn:contains(errorString, 'SERIAL_NOT_FOUND')}">
                              <div class="error">
                                <spring:message code="serial.not.found" />
                              </div>
                            </c:if>
                          </c:when>
                          <c:when test="${consInventory.serialManaged eq '1'}">
                            <select class="serialNumber selectpicker"
                              id="sn_${cartData.entries[fieldLength - count.count].product.code}_${cartData.entries[fieldLength - count.count].entryNumber}">
                              <c:forEach items="${consInventory.serialNumbers}" var="entry2" varStatus="count2">
                                <c:choose>
                                  <c:when test="${cartData.entries[fieldLength - count.count].serialNumber eq entry2}">
                                    <option value="${entry2}" selected="selected">${entry2}</option>
                                  </c:when>
                                  <c:otherwise>
                                    <option value="${entry2}">${entry2}</option>
                                  </c:otherwise>
                                </c:choose>
                              </c:forEach>
                            </select>
                            <div class="batch-text">
                              <spring:message code="consignment.charge.batchno.not.displayed" />
                            </div>
                            <c:if test="${fn:contains(errorString, 'SERIAL_NOT_FOUND')}">
                              <div class="error">
                                <spring:message code="serial.not.found" />
                              </div>
                            </c:if>
                          </c:when>
                          <c:otherwise>
                            <input class="notSerialManaged" type="hidden">
                          </c:otherwise>
                        </c:choose>
                      </c:if>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                  <input id="sn_${cartData.entries[fieldLength - count.count].entryNumber}" type="hidden" value="${cartData.entries[fieldLength - count.count].serialNumber}">
                ${cartData.entries[fieldLength - count.count].serialNumber}<div class="batch-text">
                      </br>
                      <spring:message code="consignment.Charge.serial.notification" />
                    </div>
                  </c:otherwise>
                </c:choose></td>
              <td><c:if test="${fn:contains(errorString, 'PRICING_INCOMPLETE')}">
                  <div class="error" style="margin-top: 10px">
                    <spring:message code="pricing.incomplete" />
                  </div>
                </c:if></td>
              <td><c:if test="${fn:contains(errorString, 'PRICING_INCOMPLETE')}">
                  <div class="error" style="margin-top: 10px">
                    <spring:message code="pricing.incomplete" />
                  </div>
                </c:if></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div class="sapSecondCallData">
      <form:form action="${sapSecondCallFormUrl}" method="POST" id="simulateOrderForm" name="simulateOrderForm" commandName="simulateOrderForm">
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
                        <a href="javascript:;" class="showProductDeatils anchor-img" data="${productCode}"><product:productPrimaryImage product="${entry.product}" 
                        format="cartIcon" /></a>
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
            <button type="button" class="close clsBtn" data-dismiss="modal">
              <spring:message code="cart.review.close" />
            </button>
            <h4 class="modal-title">
              <spring:message code="homePage.errorDetails" />
            </h4>
          </div>
          <form:form method="post" action="javascript:;">
            <div class="modal-body">
              <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                  <div class="panel-group">
                    <div class="panel panel-danger">
                      <div class="panel-heading">
                        <h4 class="panel-title">
                          <span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message
                              code="homePage.errordetails.addfailed" /></span>
                        </h4>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                  <div class="scroll error-content" style="font-weight: bold"></div>
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
      <table id="datatab-mobile" class="table table-bordered table-striped sorting-table error-on-top">
        <thead>
          <tr>
            <th class="no-sort text-left"><spring:message code="cart.validate.product" /></th>
            <th class="no-sort"><spring:message code="cart.review.entry.quantity" />
              <div class="cart-update-all-link">
                <a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message
                    code="cart.review.entry.Updateall" /></a>
              </div></th>
          </tr>
        </thead>
        <tbody>
          <c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
          <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
            <tr>
              <form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_mobile" action="${cartUpdateFormAction}"
                method="post" commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
                <td class="text-left"><consignmentChargeCart:productDescriptionBeforeValidation
                    entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}" showRemoveLink="true" showStatus="false"
                    rowcount="${count.count}" />
                  <div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
                    <c:url value="/cart/update" var="cartUpdateFormAction" />
                    <div class="sub-details-row">
                      <%-- <form> --%>
                      <input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}" /> <input
                        type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}" /> <input type="hidden"
                        name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}" />
                      <!-- qty -->
                    </div>
                    <p>
                      <spring:message code="cart.validate.unitPrice" />
                    </p>
                    <p></p>
                    <p>
                      <spring:message code="cart.review.entry.total" />
                    </p>
                    <p></p>
                  </div></td>
                <td>
                  <div class="cart-update-link">
                    <a href="#" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile_update"
                      entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile"><spring:message
                        code="cart.review.productDesc.updateItem" /></a>
                  </div> <ycommerce:testId code="cart_product_quantity">
                    <form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
                    <form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}" type="text"
                      id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile"
                      entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" class=" qtyUpdateTextBox form-control txtWidth"
                      path="quantity" />
                  </ycommerce:testId>
                  <p>
                    <strong><spring:message code="product.detail.addToCart.unit" /> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit}
                    (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})
                  </p> <ycommerce:testId code="cart_product_removeProduct">
                    <a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
                      class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem" /></a>
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
  <consignmentChargeCart:cartPageActions />
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
        <button type="button" class="close clsBtn" data-dismiss="modal">
          <spring:message code="password.forgotPassword.close" />
        </button>
        <h4 class="modal-title">
          <spring:message code="cart.review.validateOrder" />
        </h4>
      </div>
      <div class="modal-body">
        <spring:message code="cart.review.poNumber" />
      </div>
      <div class="modal-footer">
        <c:url value="/cart/validate" var="orderValidateUrl" />
        <a href="#" class="pull-left" data-dismiss="modal"><spring:message code="login.user.cancel" /></a>
        <button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" onclick="location.href='${orderValidateUrl}'">
          <spring:message code="cart.review.validateOrder" />
        </button>
      </div>
    </div>
  </div>
</div>
<!--  Changes Made for PurChase Order Pop Up -->
<!-- AAOL-2405 start-->
<div id="proposed-line-item-holder"></div>
<!-- AAOL-2405 end-->

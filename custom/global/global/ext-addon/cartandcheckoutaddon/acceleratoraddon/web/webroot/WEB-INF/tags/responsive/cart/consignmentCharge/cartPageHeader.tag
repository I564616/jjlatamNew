<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="currentPage" required="false" type="java.lang.String"%>
<!-- consignmentCharge/cartPageHeader.tag -->
<!-- Hide select shipping if only one shipping addresses are less than 2 -->
<c:if test="${fn:length(shippingAddressess) lt 2}">
  <c:set value="strictHide" var="hideSelectShipping" />
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL" />
<c:url value="/cart/consigmentFillUpAddToCartWithQty" var="consigmentFillUpAddToCartUrl" />
<c:url value="/home/homepageFileUpload" var="uploadUrl" />
<c:url value="/cart/exportToExcel" var="exportToExcelUrl" />
<!-- Hide drop ship selection button if no drop ship account is present -->
<c:if test="${fn:length(dropShipAccounts) eq 0}">
  <%-- <c:set value="strictHide" var="hideSelectDropShip"/> --%>
</c:if>
<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
  <c:set var="addOnVisibilityCss" value="disableTextbox" />
  <c:set var="disabled" value="disabled" />
</c:if>
<div class="row jnjPanelbg">
  <form:form id="consignmentChargeForm" action="cart/updateAll" method="post" commandName="consignmentChargeForm">
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
      <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="reports.order.analysis.customerPo.no" /><span class="redStar">*</span></label>
      <div class="pull-left form-consignment-input-select">
        <!-- <input class="form-control"> -->
        <span> <input id="customerPONo" type="text" name="customerPONo" class="form-control"
          data-msg-required="<spring:message code="cart.common.purchaseOrder.enter" ></spring:message>" />
        </span>
      </div>
      <div class="registerError" id="customerPONoError"></div>
      
    </div>
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
      <span> <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="cart.common.stock.user" /><span class="redStar">*</span></label>
        <select class="form-control form-element form-consignment-select-large" id="stockUser" name="stockUser">
          <option value="000011142">000011142</option>
		  <option value="000011143">000011143 </option>
      </select>
      </span>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 clearBoth margintop20px">
      <span> <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="cart.common.end.user" /><span class="redStar">*</span></label> 
      <select class="form-control form-element form-consignment-select-large" id="endUser" name="endUser">
          <option value="000011142">000011142</option>
		  <option value="000011143">000011143 </option>
      </select>
      </span>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
      <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="cart.common.po.date" /><span class="redStar">*</span></label>
      <div class="input-group form-element form-element-date">
        <input id="poDate" name="poDate" placeholder="Select date" class="date-picker form-control" type="text"
          data-msg-required="<spring:message code="cart.common.poDate.enter" ></spring:message>"> <label for="poDate"
          class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
      </div>
      <div class="registerError" id="poDateError"></div>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
      <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="requested.delivery.date" /><span class="redStar">*</span></label>
      <div class="input-group form-element form-element-date">
        <input id="requestDelDate" name="requestDelDate" placeholder="Select date" class="date-picker form-control" type="text"
          data-msg-required="<spring:message code="cart.common.fillup.date.enter" ></spring:message>"> <label for="requestDelDate"
          class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
      </div>
      <div class="registerError" id="requestDelDateError"></div>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
      <label class="pull-left boldtext label-lineHeight form-consignment-label-select"><spring:message code="shipping.instructions" /></label>
      <textarea class="form-control  form-consignment-input-select" rows="" id="comment" name="comment"></textarea>
    </div>
  </form:form>
  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
    <form:form name="uploadFileFormHome" id="uploadFileFormHome" enctype="multipart/form-data" method="POST" action="${uploadUrl}">
      <div class="row" style="margin:0px">
        <div class="pull-right margintop20px">
          <a href="#" class="cursor-pointer"><label for="uploadmultifilehome" class="cursor-pointer"><spring:message code="cart.from.excel" /></label></a> | <a
            href="${exportToExcelUrl}"><label class="cursor-pointer"><spring:message code="cart.to.excel" /></label></a>
        </div>
       </div> 
      <div class="row" style="margin:0px">
         <div class="pull-right"> 
          <div class="table-row">
            <div class="table-cell">
              <input id='uploadmultifilehome' class="uploadfilehome" type='file' name="uploadmultifilehome" />
            </div>
            <div class="table-cell">
              <div id="placeorderfileSpn" style="display: none; width: 100%; text-align: center">
                <input style="display: inline-block" type="button" class='primarybtn floatRight filenamebutton2 btnclsactive' id="placeorderfile"
                  name="placeorderfile" value="Add to Cart" />
              </div>
            </div>
          </div>
         </div> 
      </div>
    </form:form>
  </div>
</div>

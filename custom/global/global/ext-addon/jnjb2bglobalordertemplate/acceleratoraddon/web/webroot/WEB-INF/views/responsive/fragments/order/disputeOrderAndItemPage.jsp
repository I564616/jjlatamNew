<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/order"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<template:page pageTitle="${pageTitle}">

<c:choose>
	<c:when test="${pageType eq 'DISPUTE_ORDER' }">
		<order:disputeOrderPage />
	</c:when>
	<c:when test="${pageType eq 'DISPUTE_ITEM'}">
		<order:disputeLineItemPage/>
	</c:when>
	<c:when test="${pageType eq 'DISPUTE_INVOICE_ITEM'}">
		<order:disputeInvoiceLineItemPage/>
	</c:when>
</c:choose>


<div class="modal fade jnj-popup-container" id="dispute-success-popup" role="dialog" data-firstLogin='true'>
          <div class="modal-dialog modalcls">
           <div class="modal-content popup">
            <div class="modal-header">
<%--               <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close"/></button> --%>
            </div>
            <div class="modal-body" style="font-size:18px;padding:25px">
            <div>           
            	<div class="panel-success" id="dispute-info-panel">
		              <div class="panel-heading">
			              <h4 class="panel-title">
				              <span class="glyphicon glyphicon-ok"></span>
				              <span id="dispute-success-info"></span>
			              </h4>
		              </div>
	            </div>
	            </div>
	            <div class="panel-danger" id="dispute-error-panel">
		              <div class="panel-heading">
			              <h4 class="panel-title">
				              <span class="glyphicon glyphicon-ban-circle"></span>
				              <span id="dispute-error"></span>
			              </h4>
		              </div>
	            </div>
            </div>
            <div class="modal-footer modal-footer-style">

				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-dismiss="modal" >
					<spring:message code="cart.common.ok" />
				</button>
			</div>           
           <%-- <div class="modal-footer">
           	 <a href="#" id="dispute-cancel" class="modal-cancel pull-left"><spring:message code="dispute.order.cancel"/></a>
             <button class="btn btnclsactive pull-right" id="dispute-ok"><spring:message code="dispute.success.popup.ok"/></button>
           </div> --%>
           </div>
          </div>
       </div>	
</template:page>
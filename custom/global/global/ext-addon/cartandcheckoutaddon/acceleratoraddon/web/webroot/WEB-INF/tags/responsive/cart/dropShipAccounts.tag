<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.changeDropShipAccount.js"></script>
<div class="modal fade jnj-popup" id="dropship-account-popup" role="dialog">
	<div class="modal-dialog modalcls modal-md selectaccountpopupdrop" id="selectaccountpopup">
		<div class="modal-content"> 
			<div class="modal-header">
				<a href="" type="button" class="close clsBtn" data-dismiss="modal" style="font-weight:normal"><spring:message code="popup.close"></spring:message></a>
				<h4 class="modal-title"><spring:message code="cart.noCharge.orderInfo.dropShipAccount"/></h4>
			</div>
			<div class="modal-body">
				<div class="list-group listclass">
					<div id="changeAccountPopup">
					   <ul class="account-ul-list">
					   <c:forEach var="dropShipAccount" items="${dropShipAccounts}">
							<li class="dropShipaccountListPopUp odd-row" id="dropShipaccountListPopUp"> 
								<a class="changeAccountUnit list-group-item anchorcls" href="javascript:;">
									<p class="list-group-item-heading code" id="dropShipAccount1">${dropShipAccount.jnjAddressId}</p>
									<input type="hidden" name="dropShipAccount" id="dropShipAccount" value="${dropShipAccount.jnjAddressId}" />
							    </a>
							   <p class="list-group-item-text descTxt">${dropShipAccount.formattedAddress}</p>
							  
							 </li> 
							</c:forEach>												 
						</ul>
					</div>
				</div>
			</div>
			<div class="modal-footer">
			<a href="#"  class="pull-left clearDropShipToAccountVal"  data-dismiss="modal"><spring:message code="cart.common.cancel"/></a>
				<button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" id="submitChangeDropShipAdd"><spring:message code="cart.common.ok"/></button>
			</div>	
			
		</div>
	</div>
</div> 
<div id="laodingcircle" style="display:none">
    <div class="modal-backdrop in">
     <!-- Modal content-->
       <div class="row panelforcircle">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
         <div class="loadcircle"><img src="${webroot}/_ui/responsive/common/images/ajax-loader.gif"></div>
        </div>
               
       </div>
      </div>        
</div>
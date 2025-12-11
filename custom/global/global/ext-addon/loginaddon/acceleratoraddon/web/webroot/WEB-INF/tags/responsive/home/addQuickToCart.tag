<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal -->
<div class="modal fade" id="quickaddcart-popup" role="dialog"
	data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<form:form name="productQuantityForm" id="productQuantityForm"
			action="javascript:;">
			<div class="modal-content popup">
				<div class="modal-header">
					<button type="button" class="close clsBtn quickClose" data-dismiss="modal"
						id="select-accnt-close"><spring:message code="account.change.popup.close"/></button>
					<h4 class="modal-title selectTitle">
						<spring:message code="homePage.quckCart.heading" />
					</h4>
				</div>
				<div class="modal-body">
					
						<c:forEach begin="1" end="6" step="1" varStatus="status" >
						<div class="row quicart-row-gap">
							<div class="first-col pull-left">

								<span class="prod"> <input type="text"
									class="form-control prod-number" id="productId${status.index}"
									name="productId${status.index}" placeholder="Product Number"></span>
								<span><input type="text" name="quantity${status.index}"
									class="form-control prod-quanity paddingright8px productQty "
									id="quantity${status.index}" placeholder="Quantity" ></span>
							</div>
							<div class="pull-left errMsgHolder" id="errorMsgHolder_${status.count}">
							 <div class="error-msg-red hide" id="errorDiv_${status.count}">															
								<div class="panel-group" style="margin-top:1px">
										<div class="panel panel-danger">
											<div class="panel-heading">
													<h4 class="panel-title">
														<span id="msg_${status.count}"><span class="glyphicon glyphicon-ban-circle quick-msg" ></span></span>																	 
													</h4>
											</div>
										</div>  
								</div>
							</div> 
							</div>
						</div>	
						</c:forEach>
						<input type="hidden" name="numbrOfProductLines"
							id="numberOfProductLines" value="6" />
					

				</div>
				<div class="modal-footer ftrcls">
					<button type="button" class="btn btnclsactive" id="addToCartForm_1"><spring:message code="home.quick.popup.addcart"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<!--End of Modal pop-up-->


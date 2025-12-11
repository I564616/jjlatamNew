<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade" id="quickaddcart-popup" role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<form name="productQuantityForm" id="productQuantityForm" action="javascript:;">
			<div class="modal-content popup">
				<div class="modal-header">
					<h4 class="modal-title selectTitle">
						<spring:message code="homePage.quickCart.title" />
					</h4>
					<a class="accountSelectionCancel"  data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></a>
				</div>
				<div class="modal-body">
                    <c:forEach begin="1" end="6" step="1" varStatus="status" >
                        <div class="row quicart-row-gap">
                            <div class="first-col pull-left">
                                <span class="prod">
                                    <input type="text" class="form-control prod-number-la" id="productId${status.index}"
                                        name="productId${status.index}" placeholder='<spring:message code="homePage.quickCart.productCode"/>'>
                                </span>
                                <span>
                                    <input type="text" name="quantity${status.index}" class="form-control prod-quanity paddingright8px productQty "
                                        id="quantity${status.index}" placeholder='<spring:message code="homePage.quickCart.quantity"/>'>
                                </span>
                            </div>
                            <div class="error-msg-red pull-left hide" id="errorDiv_${status.count}">
                                <div class="panel-group" style="margin-top:1px">
                                    <div class="panel panel-danger">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <span class="glyphicon glyphicon-ban-circle quick-msg"></span>
                                                &nbsp;<spring:message code="homePage.quickCart.errorMsg"/>
                                            </h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="error-msg-red pull-left hide" id="errorResCatDiv_${status.count}">
                                <div class="panel-group" style="margin-top:1px">
                                    <div class="panel panel-danger">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <span class="glyphicon glyphicon-ban-circle quick-msg"></span>
                                                &nbsp;<spring:message code="homePage.quickCart.errorCategoryMsgNoParam"/>
                                            </h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="error-msg-red pull-left hide" id="successDiv_${status.count}">
                                <div class="panel-group" style="margin-top:1px">
                                    <div class="panel panel-success">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <span class="glyphicon glyphicon-ok quick-msg"></span>
                                                &nbsp;<spring:message code="homePage.quickCart.successMsg"/>
                                            </h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="6" />
				</div>
				<div class="modal-footer ftrcls">
					<button type="button" class="btn btnclsactive" id="laQuickAddToCart"><spring:message code="homePage.quickCart.addToCart"/></button>
				</div>
			</div>
		</form>
	</div>
</div>
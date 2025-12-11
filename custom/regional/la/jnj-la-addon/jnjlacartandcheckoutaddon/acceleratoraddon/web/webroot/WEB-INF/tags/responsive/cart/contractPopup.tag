<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div  id="contractPopuppage">
    <!-- Modal -->
    <div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
        <div class="modal-dialog modalcls">

            <div class="modal-content popup">
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
                    <h4 class="modal-title selectTitle"><spring:message code="contract.page.addprod"/></h4>
                </div>
                <div class="modal-body">
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <table class="contract-popup-table">
                                    <tr>
                                        <td><div class="glyphicon glyphicon-ok"></div></td>
                                        <td><div class="info-text"><spring:message code="contract.page.infotext"/></div></td>
                                    </tr>
                                </table>
                            </h4>
                        </div>
                    </div>
                    <div><spring:message code="contract.page.msg"/></div>
                    <div class="continueques"><spring:message code="contract.page.continue"/></div>
                </div>
                <div class="modal-footer ftrcls">
                    <a href="#" class="pull-left canceltxt" data-bs-dismiss="modal" id="cancel-btn-addtocart0"><spring:message code="cart.common.cancel"/></a>
                    <button type="button" class="btn btnclsactive" data-bs-dismiss="modal" id="accept-btn-addtocart0" ><spring:message code="contract.page.accept"/></button>
                </div>
            </div>
        </div>
    </div>
    <!-- Add to cart Modal pop-up to identify  contract or non contract end-->
</div>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="selectindirectpage" class="changeIndirectPopupContainer">
    <div class="modal fade" id="selectindirectcustomerpopup" role="dialog">
        <div class="modal-dialog modalcls">
            <!-- Modal content-->
            <div class="modal-content popup">
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-bs-dismiss="modal" id="select-indirect-close" ><spring:message code="buildOrder.popup.close"/></button>
                    <h4 class="modal-title selectTitle"><spring:message code="buildOrder.select.indirect.customer.title"/></h4>
                </div>
                <div class="modal-body">
                    <div class="form-group searchArea">
                        <input type="text" class="form-control searchBox indirectCustomer"  placeholder="<spring:message code="buildOrder.search.indirect.customer.placeholder"/>" id="changeCustomerNoAjax" >
                        <br/>
                        <br/>
                        <div class="row">
                            <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                                <p id="customerNumber">
                                </p>
                                <p id="customerName">
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer ftrcls">
                    <div id="select-indirect-customer-error" style="color:#b41601;display:inline-block;float:left">
                        <spring:message code="buildOrder.search.indirect.customer.error"/>
                    </div>
                    <button type="button" class="btn btnclsactive" id="change-select-customer-btn"><spring:message code="buildOrder.select.indirect.customer"/></button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="selectindirectpayerpopup" role="dialog">
        <div class="modal-dialog modalcls">
            <!-- Modal content-->
            <div class="modal-content popup">
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-bs-dismiss="modal" id="select-indirect-close" ><spring:message code="buildOrder.popup.close"/></button>
                    <h4 class="modal-title selectTitle"><spring:message code="buildOrder.select.indirect.payer.title"/></h4>
                </div>
                <div class="modal-body">
                    <div class="form-group searchArea">
                        <input type="text" class="form-control searchBox indirectPayer"  placeholder="<spring:message code="buildOrder.search.indirect.payer.placeholder"/>" id="changePayerNoAjax" >
                        <br/>
                        <br/>
                        <div class="row">
                            <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                                <p id="payerNumber">
                                </p>
                                <p id="payerName">
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer ftrcls">
                    <div id="select-indirect-payer-error" style="color:#b41601;display:inline-block;float:left">
                        <spring:message code="buildOrder.search.indirect.payer.error"/>
                    </div>
                    <button type="button" class="btn btnclsactive" id="change-select-payer-btn"><spring:message code="buildOrder.select.indirect.payer"/></button>
                </div>
            </div>
        </div>
    </div>
</div>
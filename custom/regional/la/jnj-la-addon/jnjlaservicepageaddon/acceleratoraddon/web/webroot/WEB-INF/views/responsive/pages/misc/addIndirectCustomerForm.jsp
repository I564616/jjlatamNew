<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <div id="addIndirectCustomerFormPage" >
        <input type="hidden" value="${success}" id="isFormSendSuccessful" />
        <div id="breadcrumb" class="breadcrumb">
            <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
        </div>
        <div class="row content">
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <spring:message code="misc.services.addIndirectCustomer" />
            </div>
        </div>

        <div id="successMessage" style="display: none;">
            <laCommon:genericMessage messageCode="misc.indirectCustomer.success"
                icon="ok" panelClass="success" />
        </div>
        <div id="errorMessage" style="display: none;">
            <laCommon:genericMessage messageCode="misc.indirectCustomer.failure"
                icon="ban-circle" panelClass="danger" />
        </div>

        <div class="addIndirectCustomerPanel jnj-panel mainbody-container col-xs-12 col-sm-12 col-md-12 col-lg-12 " >
            <form id="addIndirectCustomerValidate" action="addIndirectCustomer" method="post" name="addIndirectCustomerValidate">
                <div class="row padding20px ">
                    <div class="col-xs-9 col-sm-9 col-md-9 col-lg-9">
                        <div class="addIndirectCustomerGlobalError registerError" style="display:none;">
                            <label class="error" >
                                <spring:message code="help.page.contactus.reqfields" />.<br />
                            </label>
                        </div>
                        <div class="form-group ">
                            <label for="customerName">
                                <spring:message code="misc.services.customerName" /><span class="redStar">*</span>
                            </label>
                            <input type="text" class="form-control required validationevent" name="customerName" id="customerName"
                                data-msg-required="<spring:message code='profile.phone.required' />"/>
                        </div>
                        <div class="form-group ">
                            <input type="radio" checked="checked" id="radioCnpj" name="governmentIdType"
                                value='<spring:message code="misc.services.cnpj"/>' class="required" />
                            <label for="radioCnpj">
                                <spring:message code="misc.services.cnpj"/><span class="redStar">*</span>
                            </label> &nbsp;

                            <input type="radio" id="radioCpf" name="governmentIdType"
                                value='<spring:message code="misc.services.cpf"/>' class="required" />
                            <label for="radioCpf">
                                <spring:message code="misc.services.cpf"/><span class="redStar">*</span>
                            </label> &nbsp;

                            <input type="radio" id="radioTaxId" name="governmentIdType"
                                value='<spring:message code="misc.services.taxId"/>' class="required" />
                            <label for="radioTaxId">
                                <spring:message code="misc.services.taxId"/><span class="redStar">*</span>
                            </label>

                            <input type="text" name="governmentId" id="governmentId" class = "form-control required idtypevalidation"
                                data-msg-required="<spring:message code='profile.phone.required' />"/>
                        </div>
                        <div class="form-group " style="display:none;" id="cpfDiv">
                            <label for="indicate">
                                <spring:message code="misc.services.physicianOrPatient"/>
                                <span class="redStar">*</span>
                            </label>
                            <input type="text" id="indicate" name="cpfPatientOrPhysicianName" class="form-control required validationevent"
                                data-msg-required="<spring:message code='profile.phone.required' />"/>
                        </div>
                        <div class="form-group " >
                            <label for="customerType">
                                <spring:message code="misc.services.typeOfCustomer"/>
                                <span class="redStar">*</span>
                            </label>&nbsp;
                            <select id="customerType" name="customerType" class="form-control required indirectDropdown  "
                                data-msg-required="<spring:message code='profile.phone.required' />">
                                <option value=""><spring:message code="misc.services.selectOne"/>&nbsp;<spring:message code="misc.services.type"/></option>
                                <c:forEach begin="0" end="${customerTypeCount}" var="count">
                                    <option value='<spring:message code="misc.indirectCustomer.indirectFormCustomerType${count}"/>'>
                                        <spring:message code="misc.indirectCustomer.indirectFormCustomerType${count}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group " >
                            <label for="selectPublic">
                                <spring:message code="misc.services.public"/>&nbsp;
                                <spring:message code="misc.services.slash"/>&nbsp;
                                <spring:message code="misc.services.private"/>
                                <span class="redStar">*</span>
                            </label>&nbsp;
                            <select id="selectPublic" name="publicOrPrivate" class="form-control required indirectDropdown  "
                                data-msg-required="<spring:message code='profile.phone.required' />">
                                <option value=""><spring:message code="misc.services.selectOne"/></option>
                                <option value='<spring:message code="misc.services.public"/>'><spring:message code="misc.services.public"/></option>
                                <option value='<spring:message code="misc.services.private"/>'><spring:message code="misc.services.private"/></option>
                            </select>
                        </div>
                        <div class="form-group " >
                            <label for="selectBid">
                                <spring:message code="misc.services.bid"/>
                                <span class="redStar">*</span>
                            </label>&nbsp;
                            <select id="selectBid" name="bid" class="form-control required indirectDropdown  "
                                data-msg-required="<spring:message code='profile.phone.required' />">
                                <option value=""><spring:message code="misc.services.selectOne"/></option>
                                <option value='<spring:message code="common.label.yes"/>'><spring:message code="common.label.yes"/></option>
                                <option value='<spring:message code="common.label.no"/>'><spring:message code="common.label.no"/></option>
                            </select>
                        </div>
                        <div class="form-group " >
                            <label for="selectCompany">
                                <spring:message code="misc.services.company"/>
                                <span class="redStar">*</span>
                            </label>&nbsp;
                            <select id="selectCompany" name="company" class="form-control required indirectDropdown  "
                                data-msg-required="<spring:message code='profile.phone.required' />">
                                <option value=""><spring:message code="misc.services.selectOne"/></option>
                                <option value='<spring:message code="text.uploadOrder.pharma"/>'><spring:message code="text.uploadOrder.pharma"/></option>
                                <option value='<spring:message code="text.uploadOrder.medical"/>'><spring:message code="text.uploadOrder.medical"/></option>
                            </select>
                        </div>
                        <div class="form-group ">
                            <label for="state">
                                <spring:message code="misc.services.state" />
                            </label>
                            <input type="text" class="form-control validationevent" name="state" id="state" />
                        </div>
                        <div class="form-group ">
                            <label for="city">
                                <spring:message code="misc.services.city" />
                            </label>
                            <input type="text" class="form-control validationevent" name="city" id="city" />
                        </div>
                        <div class="form-group ">
                            <label for="address">
                                <spring:message code="misc.services.address" />
                            </label>
                            <input type="text" class="form-control addressvalidation" name="address" id="address" />
                        </div>
                        <div class="form-group ">
                            <label for="neighborhood">
                                <spring:message code="misc.services.townOrNeighborhood" />
                            </label>
                            <input type="text" class="form-control validationevent" name="neighborhood" id="neighborhood" />
                        </div>
                        <div class="form-group">
                            <label for="zip">
                                <spring:message code="misc.services.zip" />
                            </label>
                            <input type="text" class="form-control idtypevalidation" name="zipCode" id="zip" />
                        </div>
                        <div class="checkbox checkbox-info selectchkbox content inline-element" style="border-bottom: 1px solid #ccc;">
                            <input id="indirectCustomer-agree" class="styled" type="checkbox">
                            <label for="indirectCustomer-agree">
                                <spring:message code="help.page.contactus.help.statement" />
                            </label>
                        </div>
                        <br/>
                        <div>
                            <input type="button" id="indirectCustomerSend"
                                class="primarybtn btnclsactive btn-disabled-style pull-right text-uppercase"
                                value="<spring:message code='misc.indirectCustomer.send' />" disabled/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</templateLa:page>
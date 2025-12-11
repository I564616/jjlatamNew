<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="commonLa" tagdir="/WEB-INF/tags/addons/jnjlaordertemplateaddon/responsive/common" %>
<%@ taglib prefix="cartCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <input type="hidden" name="countryCode" id="countryCode" value="${currentLanguage.isocode}"/>
    <div id="Orderhistorypage" class="lessPadding">

        <div class="row">
            <div class="col-lg-12 col-md-12">
                <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
                <div class="row content">
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <spring:message code="invoiceHistoryPage.heading"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <c:if test="${not empty invoiceErrorMessage}">
                <div class="rejected">
                   <span class="smallFont">
                       <cartCommon:genericMessage messageCode="${invoiceErrorMessage}" icon="ban-circle" panelClass="danger"/>
                   </span>
                </div>
            </c:if>
        </div>

        <!-- FORM -->
        <form id="orderHistoryForm" name="orderHistoryForm" method="GET" onsubmit="return false;">
            <c:url value="/invoice-history" var="baseUrl"/>
            <div id="baseUrl" data-value="${baseUrl}"></div>
            <div class="row jnj-panel mainbody-container">

                <div class="col-lg-12 col-md-12">
                    <!-- ROW 1 -->
                    <div class="row jnj-panel-body">
                        <div class="form-group col-lg-6 col-md-5 col-sm-6 col-xs-12 searchby">
                            <label class="pull-left form-label form-label-select-large">
                                <spring:message code="orderHistoryPage.searchBy"/>
                            </label>
                            <select id="searchby" class="form-control form-element form-element-select-large">
                                <option value="">
                                    <spring:message code="orderHistoryPage.select"/>
                                </option>
                                <c:forEach items="${searchOptions}" var="searchOption">
                                    <option value="${searchOption}">
                                        <spring:message code="search.option.${searchOption}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-lg-4 col-md-5 col-sm-6 col-xs-12">
                            <input type="text" title="Enter Text" placeholder="${searchTitle}" class="form-control rounded-textbox" id="searchByInput"/>
                        </div>
                    </div>

                    <!-- ROW 2 -->
                    <div class="row jnj-panel-body">
                        <div class="form-group col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="row marginbottomipad25px">
                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 start">
                                    <label class="pull-left form-label form-label-date boldtext">
                                        <spring:message code="orderHistoryPage.from"/>
                                    </label>
                                    <div class="input-group form-element form-element-date">
                                        <input id="datePicker1" name="startDate"
                                               placeholder='<spring:message code="orderHistoryPage.search.selectDate" />'
                                               class="date-picker form-control orderHistoryDate" type="text"
                                               value="">
                                        <label for="datePicker1" class="input-group-addon btn input-group-addon btn border-end border-top border-bottom skyBlue" id="datePicker1Icon">
                                            <i class="bi bi-calendar3"></i>
                                        </label>
                                    </div>
                                </div>
                                <p class="pull-right errorMessage d-none" style="color: #B41601; padding-right: 20px;" id="datePicker1Error">
                                    <spring:message code="orderHistoryPage.dateError"/>
                                </p>
                            </div>
                        </div>
                        <div class="form-group col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="row marginbottomipad25px">
                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 end">
                                    <label class="pull-left form-label form-label-date boldtext ipadterritory">
                                        <spring:message code="orderHistoryPage.to"/>
                                    </label>
                                    <div class="input-group form-element form-element-date">
                                        <input id="datePicker2" name="endDate"
                                               placeholder='<spring:message code="orderHistoryPage.search.selectDate" />'
                                               class="date-picker form-control orderHistoryDate" type="text" value="">
                                        <label for="datePicker2" class="input-group-addon btn input-group-addon btn border-end border-top border-bottom skyBlue" id="datePicker2Icon">
                                            <i class="bi bi-calendar3"></i>
                                        </label>
                                    </div>
                                </div>
                                <p class="pull-right errorMessage d-none" style="color: #B41601; padding-right: 20px;" id="datePicker2Error">
                                    <spring:message code="orderHistoryPage.dateError"/>
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- ROW 3 -->
                    <div class="row jnj-panel-footer">
                        <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
                            <commonLa:downloadResultsTag useLinks="true"/>
                        </div>
                        <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
                            <div class="pull-right btn-mobile">
                                <button type="button" class="btn btnclsactive searchbtn pull-right" id="ajaxTableFormSearchButton">
                                    <spring:message code="orderHistoryPage.search"/>
                                </button>
                                <button type="button" class="btn btnclsnormal reset orderHistoryReset" id="ajaxTableFormResetButton">
                                    <spring:message code="orderHistoryPage.reset"/>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </form>

        <div class="row">
            <div class="info notLoadedOrderMessage d-none d-lg-block d-xl-block d-xxl-block">
               <span class="smallFont">
                   <cartCommon:genericMessage messageCode="invoiceHistoryPage.info.orderNotLoaded" panelClass="warning"/>
               </span>
            </div>
        </div>

        <!-- TABLE WRAPPERS -->
        <div class="row">
            <div class="col-lg-12 col-md-12">
                <div id="datatab-desktop-wrapper" class="d-none d-lg-block d-xl-block d-xxl-block  jnj-panel-for-table mainbody-container table-responsive"></div>
            </div>
        </div>
        <div id="datatab-tablet-wrapper" class="d-none d-sm-block d-md-block d-lg-none jnj-panel-for-table mainbody-container"></div>
        <div id="datatab-mobile-wrapper" class="d-block d-sm-none jnj-panel-for-table mainbody-container"></div>

        <div class="row">
            <div class="info notLoadedOrderMessage d-none d-lg-block d-xl-block d-xxl-block">
               <span class="smallFont">
                   <cartCommon:genericMessage messageCode="invoiceHistoryPage.info.orderNotLoaded" panelClass="warning"/>
               </span>
            </div>
        </div>

    </div>
</templateLa:page>
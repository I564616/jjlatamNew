<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <c:url value="/my-account/sellout/selloutUpload" var="selloutUpload"></c:url>
    <input type ="hidden" id="uploadStatus" value="${uploadStatus}"/>
    <input type ="hidden" id="sizeError" value="${sizeError}"/>
    <input type ="hidden" id="boxClosed" value="false"/>

    <section>
        <div class="row jnj-body-padding shipmentContainer" id="jnj-body-content">
            <div class="col-lg-12 col-md-12">
                <div id="Orderhistorypage">
                    <div class="row">
                        <div class="col-lg-12 col-md-12">
                            <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
                            <div class="row content">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                    <spring:message code='text.sellout.report'/>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12" style="padding-top:20px">
                                    <a href="#" >
                                        <button type="button" data-bs-toggle="modal" data-bs-target="#uploadSelloutPopup" class="btn btnclsactive pull-right sellout-button">
                                            <spring:message code='text.sellout.upload.sellout.report'/>
                                        </button>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${uploadStatus ==  'true'}">
                        <div>
                            <lacommon:genericMessage messageCode="text.sellout.uploadSuccess" icon="ok" panelClass="success" />
                        </div>
                    </c:if>
                    <c:if test="${uploadStatus ==  'false'}">
                        <div>
                            <lacommon:genericMessage messageCode="text.sellout.uploadFailed" icon="ban-circle" panelClass="danger" />
                        </div>
                    </c:if>
                    <div id="globalMessages">
                        <common:globalMessages/>
                        <cartLa:cartRestoration/>
                        <cartLa:cartValidation/>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-md-12">
                            <div class="d-none d-sm-block mainbody-container">
                                <table id="datatab-desktop" class="table table-bordered table-striped lasorting-table sellout-table">
                                    <thead>
                                        <tr>
                                            <th class="no-sort">
                                                <spring:message code='text.sellout.document'/>
                                            </th>
                                            <th class="no-sort">
                                                <spring:message code='text.sellout.user'/>
                                            </th>
                                            <th class="no-sort text-uppercase">
                                                <spring:message code='text.sellout.customer'/>
                                            </th>
                                            <th class="no-sort text-uppercase">
                                                <spring:message code='text.sellout.company'/>
                                            </th>
                                            <th class="no-sort text-uppercase sellout-data">
                                                <spring:message code='text.sellout.date'/>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${dataList.results}" var="dataList"	varStatus="loop">
                                            <tr>
                                                <td>
                                                    <a href="#">
                                                        <c:choose>
                                                            <c:when test="${ not empty dataList.docName && '' ne dataList.docName}">
                                                                ${dataList.docName}
                                                            </c:when>
                                                            <c:otherwise>
                                                                &nbsp;
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${ not empty dataList.user && '' ne dataList.user}">
                                                            ${dataList.user}
                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${ not empty dataList.customer && '' ne dataList.customer}">
                                                            ${dataList.customer}
                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${ not empty dataList.company && '' ne dataList.company}">
                                                            ${dataList.company}
                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="sellout-table">
                                                    <c:choose>
                                                        <c:when test="${'en' eq sessionlanguage}">
                                                            ${dataList.date}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${dataList.date}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- Table collapse for mobile device -->
                    <div class="d-xs-block d-lg-none d-md-none d-sm-none jnj-panel-for-table mainbody-container">
                        <table id="datatab-mobile" class="table table-bordered table-striped sorting-table bordernone mobile-table sellout-mobile">
                            <thead>
                                <tr>
                                    <th class="no-sort">
                                        <spring:message code='text.sellout.document'/>
                                    </th>
                                    <th class="no-sort">
                                        <spring:message code='text.sellout.date'/>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${dataList.results}" var="dataList"	varStatus="loop">
                                    <tr>
                                        <td class="vlign-top">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="toggle-link panel-collapsed skyBlue">
                                                <span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
                                                <c:choose>
                                                    <c:when test="${ not empty dataList.docName && '' ne dataList.docName}">
                                                        ${dataList.docName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        &nbsp;
                                                    </c:otherwise>
                                                </c:choose>
                                            </a>
                                            <div id="collapse1" class="panel-collapse collapse">
                                                <div class="panel-body details">
                                                    <div class="sub-details-row">
                                                        <div style="font-family:jnjlabelfont; font-size:10px">
                                                            <spring:message code='text.sellout.user'/>
                                                        </div>
                                                        <div>
                                                            <c:choose>
                                                                <c:when test="${ not empty dataList.user && '' ne dataList.user}">
                                                                    ${dataList.user}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    &nbsp;
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                    <div class="sub-details-row">
                                                        <div style="font-family:jnjlabelfont; font-size:10px">
                                                            <spring:message code='text.sellout.customer'/>
                                                        </div>
                                                        <div>
                                                            <c:choose>
                                                                <c:when test="${ not empty dataList.customer && '' ne dataList.customer}">
                                                                    ${dataList.customer}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    &nbsp;
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                    <div class="sub-details-row">
                                                        <div style="font-family:jnjlabelfont; font-size:10px">
                                                            <spring:message code='text.sellout.company'/>
                                                        </div>
                                                        <div>
                                                            <c:choose>
                                                                <c:when test="${ not empty dataList.company && '' ne dataList.company}">
                                                                    ${dataList.company}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    &nbsp;
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="vlign-top">
                                            <div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${'en' eq sessionlanguage}">
                                                            ${dataList.date}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${dataList.date}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- Accordian for mobile ends here -->
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="float-right-to-none" style="padding-top:30px">
                                <a href="#" >
                                    <button type="button" data-bs-toggle="modal" data-bs-target="#uploadSelloutPopup" class="btn btnclsactive sellout-button-bottom">
                                        <spring:message code='text.sellout.upload.sellout.report'/>
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Modal -->
    <div class="modal fade" id="uploadSelloutPopup" role="dialog">
        <div class="modal-dialog modalcls">
            <!-- Modal content-->
            <div class="modal-content popup">
                <div class="modal-header">
                    <button type="button" class="close clsBtn accountSelectionCancel" data-bs-dismiss="modal">
                        <spring:message code='text.sellout.close'/>
                    </button>
                    <h4 class="modal-title selectTitle">
                        <spring:message code='text.sellout.upload.sellout.report'/>
                    </h4>
                </div>
                <form:form id="uploadForm" enctype="multipart/form-data" method="POST" modelAttribute="uploadForm" action="${selloutUpload}">
                    <div id="FileUpload">
                        <div class="modal-body">
                            <div class="form-group searchArea">
                                <div class="product-box">
                                    <label for="usr" class="subtxt company-name pull-left">
                                        <spring:message code='text.sellout.companypopup'/>
                                    </label>
                                    <select class="selectpicker first" id='companyId' data-width="80%">
                                        <!-- <option>Select</option> -->
                                        <option value="medical">
                                            <spring:message code='text.sellout.select.medical'/>
                                        </option >
                                        <option value="pharma">
                                            <spring:message code='text.sellout.select.pharma'/>
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group listclass">
                                <div class="odd-row">
                                    <div class="list-group-item-text descTxt">
                                        <div class="address-txt boldtext" id="file_name"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row ftrcls">
                            <input id="hddnCompany" type="hidden" name="company" value="Medical" />
                            <button type="button" class="btn btnclsactive pull-right btnstyle btnAlign" id="browse_file">
                                <spring:message code='text.sellout.browsefile'/>
                            </button>
                            <input type="button" class="btn btnclsactive pull-right primarybtn submitUploadButton hidden" value="<spring:message code='text.sellout.upload'/>" id="submitbutton" onclick="return restrictFilesOnlyForSellOut('.txt','.xls','.xlsx');"/>
                            <input id="firstFile" type="file" class="file" name="file" style="display: block;  visibility: hidden; width: 0; height: 0;" />
                           
                            
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    <!--End of Modal pop-up-->
    <script>
        window.scrollTo(0, document.getElementById("scrollPos").value);
    </script>
</templateLa:page>
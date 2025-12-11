<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="commonLa" tagdir="/WEB-INF/tags/addons/jnjlaordertemplateaddon/responsive/common" %>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="cartCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">

   <main id="cls-upload-delivery-dates" class="container-fluid">

       <!-- BREADCRUMB AND TITLE -->
       <div class="row">
           <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
           <div class="row content">
               <div class="col-xs-12">
                   ${pageHeader}
               </div>
           </div>
       </div>

       <!-- MESSAGES -->
       <div class="row">
           <c:if test="${not empty accErrorMsgs}">
               <c:forEach items="${accErrorMsgs}" var="msg">
                   <lacommon:genericMessage messageCode="${msg.code}" messagearguments="${msg.attributes}" icon="ban-circle" panelClass="warning" />
               </c:forEach>
           </c:if>
           <c:if test="${not empty accConfMsgs}">
               <c:forEach items="${accConfMsgs}" var="msg">
                   <lacommon:genericMessage messageCode="${msg.code}" messagearguments="${msg.attributes}" icon="ok" panelClass="success" />
               </c:forEach>
           </c:if>
       </div>

       <!-- FORM -->
       <div class="row mainbody-container px-4 py-3" id="upload-delivery-date">
           <div class="pull-left">
               <div class="text-center" role="button">
                   <img src="${webroot}/_ui/responsive/common/images/upload.png" class="img-circle" width="50" height="50" />
               </div>
               <div class="text-center mt-1 mb-2" role="button">
                   <small>
                       <a class="boldtext">
                           <spring:message code="clsPage.uploadDeliveryDates.upload" />
                       </a>
                   </small>
               </div>
               <div class="text-center">
                   <small>
                       <cms:component uid="instructionsTemplatesLinkDeliveryDates" />
                   </small>
               </div>
               <div class="text-center">
                   <small>
                       <p class="fileName hidden mb-0 mt-4"> - </p>
                   </small>
               </div>
               <div class="text-center">
                   <c:url var="uploadUrl" value="/cls/delivery-dates-files" />
                   <form:form enctype="multipart/form-data" method="POST" action="${uploadUrl}">
                       <input type="file" id="clsuploadEdiFile" name="deliveryDatesFile" class="hidden" />
                       <input type="submit" class="hidden filenamebutton2 btnclsactive" value='<spring:message code="clsPage.uploadDeliveryDates.send" />' >
                   </form:form>
               </div>
           </div>
       </div>

       <!-- TABLE -->
       <div class="row mainbody-container mt-5">
           <table class="table table-bordered table-striped">
               <thead>
               <tr>
                   <th>
                       <spring:message code="clsPage.uploadDeliveryDates.table.uploadedFile"/>
                   </th>
                   <th>
                       <spring:message code="clsPage.uploadDeliveryDates.table.user"/>
                   </th>
                   <th>
                       <spring:message code="clsPage.uploadDeliveryDates.table.date"/>
                   </th>
                   <th>
                       <spring:message code="clsPage.uploadDeliveryDates.table.status"/>
                   </th>
                   <th>
                       <spring:message code="clsPage.uploadDeliveryDates.table.errorFile"/>
                   </th>
               </tr>
               </thead>
               <c:forEach items="${fileList}" var="file">
                   <c:if test="${!file.erased}">
                       <tr>
                           <td class="text-left">
                               <c:url var="url" value="cls/delivery-dates-files/${file.id}" />
                               <a href="${url}">${file.filename}</a>
                           </td>
                           <td>
                               ${file.user.name}
                           </td>
                           <td>
                               <fmt:formatDate pattern="${sessionlanguagePattern}" value="${file.creationDate}" />
                           </td>
                           <td>
                               <spring:message code="clsPage.uploadDeliveryDates.table.statuses.${file.currentStatus}"/>
                           </td>
                           <td>
                               <c:choose>
                                   <c:when test="${file.errorMessage == 'PARTIALLY_PROCESSED'}">
                                       <c:url var="url" value="cls/delivery-dates-files/${file.id}/errors" />
                                       <a href="${url}">
                                           <spring:message code="download"/>
                                       </a>
                                   </c:when>
                                   <c:otherwise>
                                       -
                                   </c:otherwise>
                               </c:choose>
                           </td>
                       </tr>
                   </c:if>
               </c:forEach>
           </table>
       </div>

   </main>

</templateLa:page>
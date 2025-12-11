<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/nav"%>
<%@ taglib prefix="services" tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/services"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">

   <spring:url value="/services/addIndirectCustomer" var="addIndirectCustomer" />
   <spring:url value="/services/addIndirectPayer" var="addIndirectPayer" />
   <spring:url value="/services/consignmentIssue" var="consignmentIssue" />
   <spring:url value="/services/integralServices" var="integralServices" />
   <spring:url value="/services/serviceForm" var="serviceForm" />
   <spring:url value="/services/synthes" var="synthes" />
   <spring:url value="/services/laudo/download" var="downloadLaudo" />
   <spring:url value="/services/laudo/manage" var="manageLaudo" />
   <spring:url value="/services/crossReferenceTable" var="crossReferenceTable" />
   <spring:url value="/services/pharmaPriceList" var="pharmaPriceList" />

   <div id="ServicesPage">
      <div id="breadcrumb" class="breadcrumb">
         <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
      </div>
      <div class="row">
         <div class="col-lg-12 col-md-12 margintop15">
         <div class="row">

            <c:if test="${showAddIndirect ne null && showAddIndirect=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${addIndirectCustomer}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-adduser.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.addIndirectCustomer" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.addIndirectCustomer" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

            <c:if test="${showIndirectPayer ne null && showIndirectPayer=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${addIndirectPayer}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-adduser.png"
                              alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.addIndirectPayer" />
                              </p>
                              <p class="secondline">
                                 <spring:message
                                    code="misc.services.addIndirectPayer" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

		<!-- Hidden the links OCD and Orthopedics to resolve the incident INC000022368077 -->

            <c:if test="${showDownloadPharmaPriceList ne null && showDownloadPharmaPriceList=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${pharmaPriceList}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-cust-acc-pricedownload.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.pharmaPriceList" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.download" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

            <c:if test="${showIntegralServices ne null && showIntegralServices=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${integralServices}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-consign.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.integralServices" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.integralServices" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

            <c:if test="${showManageLaudo ne null && showManageLaudo=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${manageLaudo}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-synthes.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.manageLaudo" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.manageLaudo" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

            <c:if test="${showDownloadLaudo ne null && showDownloadLaudo=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${downloadLaudo}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-synthes.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.downloadLaudo" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.downloadLaudo" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

            <c:if test="${showCrossReferenceTable ne null && showCrossReferenceTable=='true'}">
                  <div class="col-lg-6 col-md-6 service-panel">
                  <a href="${crossReferenceTable}">
                     <div class="block-a row">
                        <div class="col-lg-4 icon-prop">
                           <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-synthes.png" alt="Drop Ship" class="image-prop"></img>
                        </div>
                        <div class="col-lg-8 text-prop">
                           <span class="Tablesubtxt">
                              <p class="firstline">
                                 <spring:message code="misc.services.CrossReferenceTable" />
                              </p>
                              <p class="secondline">
                                 <spring:message code="misc.services.CrossReferenceTable" />
                              </p>
                           </span>
                        </div>
                     </div>
                  </a>
                  </div>
            </c:if>

				<c:if test="${showConsignment ne null && showConsignment=='true'}">
                    <div class="col-lg-6 col-md-6 service-panel">
                        <a href="${consignmentIssue}">
                            <div class="block-a row">
                                <div class="col-lg-4 icon-prop">
                                    <img src="${webroot}/_ui/addons/jnjlaresources/responsive/common/images/icon-serv-consign.png"
                                        alt="Drop Ship" class="image-prop"></img>
                                </div>
                                <div class="col-lg-8 text-prop">
                                    <span class="Tablesubtxt">
                                        <p class="firstline">
                                            <spring:message code="misc.services.consignmentIssue" />
                                        </p>
                                        <p class="secondline">
                                            <spring:message code="misc.services.consignmentIssue" />
                                        </p>
                                    </span>
                                </div>
                            </div>
                        </a>
                    </div>
				</c:if>
            </div>
			</div>
      </div>
   </div>
   <div class="pageBlock ">
      <h1>
         <messageLabel:message messageCode="misc.services.header" />
      </h1>
      <div class="pageBlockDivision myServicesForm sectionBlock">
         <div class="span-6 servicesNavList">
            <nav:servicesNav selected="ServicesLinkComponent" />
         </div>
         <input style="display: none;" id="downloadPriceFile" name="downloadPriceFile" value="${downloadFlag}" />
         <input style="display: none;" id="downloadErrorFlag" name="downloadErrorFlag" value="${downloadErrorFlag}" />
      </div>
   </div>
</templateLa:page>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb" %>
<%@ taglib prefix="resource" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="help" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/help"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<templateLa:page pageTitle="${pageTitle}">

    <div id="help-page">

        <c:set var="newPortalCount" value="${faqCountNewPortal}" />
        <c:set var="placeOrderCount" value="${faqCountPlaceOrder}" />
        <c:set var="orderStatusCount" value="${faqCountOrderStatus}" />
        <c:set var="bidsCount" value="${faqCountBids}" />
        <c:set var="linkCount" value="${countUsefulLinks}" />

        <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />

        <div class="row content">
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <spring:message code="resources.help" />
            </div>
        </div>

        <div class="boxshadow ">

            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow" id="accordionFaq" style="padding-left: 0px; padding-right: 0px">
                <div class="help-accordian panel">
                    <div class="help-accordian-header">
                        <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseFaq" class="ref_no toggle-link-la panel-collapsed">
                            <span class="bi bi-plus help-accordian-icon"></span>
                            <messageLabel:message messageCode="help.faq.header" />
                        </a>
                    </div>
                    <div class="help-accordian-faq-body panel-collapse collapse" id="collapseFaq">

                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordionNewPortal" style="padding-left: 0px; padding-right: 0px">
                            <div class="help-accordian panel">
                                <div class="help-accordian-header">
                                    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseNewPortal" class="ref_no toggle-link-la panel-collapsed">
                                        <span class="bi bi-plus help-accordian-icon"></span>
                                        <messageLabel:message messageCode="help.faq.new.portal" />
                                    </a>
                                </div>
                                <div class="help-accordian-faq-body panel-collapse collapse" id="collapseNewPortal">
                                    <c:forEach begin="1" end="${newPortalCount}" var="count">
                                        <help:helpAccordian qaId="newPortal${count}" />
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordionPlaceOrder" style="padding-left: 0px; padding-right: 0px">
                            <div class="help-accordian panel">
                                <div class="help-accordian-header">
                                    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapsePlaceOrder" class="ref_no toggle-link-la panel-collapsed">
                                        <span class="bi bi-plus help-accordian-icon"></span>
                                        <messageLabel:message messageCode="help.faq.place.order" />
                                    </a>
                                </div>
                                <div class="help-accordian-faq-body panel-collapse collapse" id="collapsePlaceOrder">
                                    <c:forEach begin="1" end="${placeOrderCount}" var="count">
                                        <help:helpAccordian qaId="placeOrder${count}" />
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordionOrderStatus" style="padding-left: 0px; padding-right: 0px">
                            <div class="help-accordian panel">
                                <div class="help-accordian-header">
                                    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseOrderStatus" class="ref_no toggle-link-la panel-collapsed">
                                        <span class="bi bi-plus help-accordian-icon"></span>
                                        <messageLabel:message messageCode="help.faq.order.status" />
                                    </a>
                                </div>
                                <div class="help-accordian-faq-body panel-collapse collapse" id="collapseOrderStatus">
                                    <c:forEach begin="1" end="${orderStatusCount}" var="count">
                                        <help:helpAccordian qaId="orderStatus${count}" />
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordionBids" style="padding-left: 0px; padding-right: 0px">
                            <div class="help-accordian panel">
                                <div class="help-accordian-header">
                                    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseBids" class="ref_no toggle-link-la panel-collapsed">
                                        <span class="bi bi-plus help-accordian-icon"></span>
                                        <messageLabel:message messageCode="help.faq.bids" />
                                    </a>
                                </div>
                                <div class="help-accordian-faq-body panel-collapse collapse" id="collapseBids">
                                    <c:forEach begin="1" end="${bidsCount}" var="count">
                                        <help:helpAccordian qaId="bids${count}" />
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow" id="accordionContactUs" style="padding-left: 0px; padding-right: 0px">
                <div class="help-accordian panel">
                    <div class="help-accordian-header">
                        <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseContactUs" class="ref_no toggle-link-la panel-collapsed">
                            <span class="bi bi-plus help-accordian-icon"></span>
                            <messageLabel:message messageCode="help.faq.contact.us" />
                        </a>
                    </div>
                    <div class="help-accordian-body panel-collapse collapse" id="collapseContactUs">
                        <help:contactUs/>
                    </div>
                </div>
            </div>

            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow" id="accordionUsefulLinks" style="padding-left: 0px; padding-right: 0px">
                <div class="help-accordian panel">
                    <div class="help-accordian-header">
                        <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseUsefulLinks" class="ref_no toggle-link-la panel-collapsed">
                            <span class="bi bi-plus help-accordian-icon"></span>
                            <messageLabel:message messageCode="help.faq.useful.links" />
                        </a>
                    </div>
                    <div class="help-accordian-body panel-collapse collapse" id="collapseUsefulLinks">
                        <c:forEach begin="1" end="${linkCount}" var="count">
                            <messageLabel:message messageCode="help.faq.useful.link${linkCount}" />
                        </c:forEach>
                    </div>
                </div>
            </div>
             <c:choose>
             	<c:when test="${locale == 'es'}">
			             <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow" id="accordionSelfVideos" style="padding-left: 0px; padding-right: 0px">
			                <div class="help-accordian panel">
			                    <div class="help-accordian-header">
			                        <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseSelfVideos" class="ref_no toggle-link-la panel-collapsed">
			                            <span class="bi bi-plus help-accordian-icon"></span>
			                            <messageLabel:message messageCode="help.faq.useful.videos" />
			                        </a>
			                    </div>
			                    <div class="help-accordian-body panel-collapse collapse" id="collapseSelfVideos">
			                            <cms:component uid="selfTrainingVideo1_es" /><br/>
			                            <cms:component uid="selfTrainingVideo2_es" /><br/>
			                            <cms:component uid="selfTrainingVideo3_es" /><br/>
			                            <cms:component uid="selfTrainingVideo4_es" /><br/>
			                            <cms:component uid="selfTrainingVideo5_es" /><br/>
			                            <cms:component uid="selfTrainingVideo6_es" /><br/>
			                            <cms:component uid="selfTrainingVideo7_es" /><br/>
			                            <cms:component uid="selfTrainingVideo8_es" />
			                    </div>
			                </div>
			            </div>
		            </c:when>
		           <c:when test="${countryIsoCode == 'BR' && locale == 'pt'}">
		           		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow" id="accordionSelfVideos" style="padding-left: 0px; padding-right: 0px">
		                <div class="help-accordian panel">
		                    <div class="help-accordian-header">
		                        <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapseSelfVideos" class="ref_no toggle-link-la panel-collapsed">
		                            <span class="bi bi-plus help-accordian-icon"></span>
		                            <messageLabel:message messageCode="help.faq.useful.videos" />
		                        </a>
		                    </div>
		                    <div class="help-accordian-body panel-collapse collapse" id="collapseSelfVideos">
		                            <cms:component uid="selfTrainingVideo1_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo2_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo3_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo4_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo5_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo6_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo7_pt" /><br/>
		                            <cms:component uid="selfTrainingVideo8_pt" />
		                    </div>
		                </div>
		            </div>
		           </c:when>
            </c:choose>
        </div>
    </div>

</templateLa:page>
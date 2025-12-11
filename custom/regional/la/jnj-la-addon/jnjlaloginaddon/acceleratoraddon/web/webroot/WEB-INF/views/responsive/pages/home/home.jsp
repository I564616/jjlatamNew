<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="homeLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/home" %>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>

<templateLa:page pageTitle="${pageTitle}">

    <c:url value="/templates" var="templatesUrl" />

    <div id="tables" class="orderstatuspage">
        <div id="orderHistoryBlock">
            <homeLa:orderHistory />
        </div>
       <c:if test="${placeOrderResComUserGrpFlag eq false && placeOrderGroupFlag eq true}" >
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 placeOrder whitebg placeorderrightborder"  id="placeOrderBlock">
            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 placeordertext">
                <spring:message code="cart.common.placeOrder"/>
            </div>
            <div class="d-flex flex-lg-row flex-md-row flex-sm-row flex-column">
            <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered borderforplaceorder">
                <div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px">
                    <a href="#" class="quickAddtoCartClick">
                    <img src="${webroot}/_ui/responsive/common/images/cart.png" class="img-circle  quickAddtoCartClick" width="50" height="50">
                    </a>
                </div>
                <div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 xplaceordermobalign boldtext dashboardtt padding0">
                    <a href="#" class="quickAddtoCartClick">
                        <p class="marginbtmplaceorder">
                            <spring:message code="home.label.quickAddToCart"/>
                        </p>
                        <p>
                            <spring:message code="home.label.quickAddToCart1"/>
                        </p>
                    </a>
                </div>
            </div>
            <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered borderforplaceorder">
                <homeLa:uploadFile />
            </div>
            <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered borderforplaceorder">
	            <c:choose>
	            	<c:when test="${isoCode eq 'br'}">
	            		<homeLa:webEdiFileupload />
	            	</c:when>
	            <c:otherwise>
	                <div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px">
	                    <a href="${templatesUrl}">
	                        <img src="${webroot}/_ui/responsive/common/images/document.png" class="img-circle " alt="ordertemplate" width="50" height="50">
	                    </a>
	                </div>
	                <div class="col-xs-8 col-md-12 col-sm-12 col-lg-12 xplaceordermobalign boldtext dashboardtt padding0">
	                    <a href="${templatesUrl}">
	                        <p class="marginbtmplaceorder">
	                            <spring:message code="home.label.orderfrom" />
	                        </p>
	                        <p>
	                            <spring:message code="home.label.template" />
	                        </p>
	                    </a>
	                </div>
	            </c:otherwise>
	            </c:choose>
	        </div>
            <c:choose>
                <c:when test="${showContractLink}">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered" >
                        <div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px home_Contract_Link" ><img src="${webroot}/_ui/responsive/common/images/contract.png" class="img-circle" alt="addcontract" width="50" height="50"></div>
                        <div class="col-xs-8 col-md-6 col-sm-6 col-lg-6 placeordermobalign buildanordertext boldtext dashboardtxt padding0 home_Contract_Link" >
                            <p class="marginbtmplaceorder" >
                                <spring:message code="home.label.addfrom" />
                            </p>
                            <p>
                                <spring:message code="home.label.acontract" />
                            </p>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 font8pixel centered">
                        <div class="col-xs-4 col-md-12 col-sm-12 col-lg-12 padding10px">
                            <a href="#">
                                <img src="${webroot}/_ui/responsive/common/images/contract.png" class="img-circle opaque " alt="addcontract" width="50" height="50"></div>
                            </a>
                        <div class="col-xs-8 col-md-6 col-sm-6 col-lg-6 placeordermobalign buildanordertext boldtext dashboardtxt padding0 opaque">
                            <a href="#">
                                <p class="marginbtmplaceorder">
                                    <spring:message code="home.label.addfrom" />
                                </p>
                                <p>
                                    <spring:message code="home.label.acontract" />
                                </p>
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        </div>
        </c:if>
    </div>
	<!-- please read limit popup Modal  -->
	    <div class="modal fade jnj-popup" id="limit-detail-popup" role="dialog" >
        <div class="modal-dialog modalcls modal-md">        
            <div class="modal-content"> 
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
					<p class="modal-title" style="margin-right: 35% !important;font-weight: bold; font-size:28px !important "><spring:message code="homePage.limitwarning" /></p>
                    
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="panel-group">
                                <div class="panel panel-danger">
                                    <div class="panel-heading">
                                        <h4 class="panel-title"  style="font-size:20px !important">
                                            <span>
                                                <span class="glyphicon glyphicon-ban-circle" style="margin-right: 60px !important"></span>
                                              <spring:message code="homePage.limit.warning.details" /> <span id="warningdetails"></span> <span> <spring:message code="homePage.limit.warning.count" /></span>
                                            </span>
                                        </h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            
                                <p  style="margin-left: 25% !important;font-weight: bold; color:red; margin-top:10px; font-size:17px ">
                                  <spring:message code="homePage.limit.allowed.details" /> <span id="countdetails"></span> <span> <spring:message code="homePage.limit.warning.lines" /></span>

                                </p>
                           
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- please read - Modal -->
    <!-- Modal pop up for file upload error start -->
	
    <div class="modal fade jnj-popup" id="error-detail-popup" role="dialog">
    	
        <div class="modal-dialog modalcls modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
                    <h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="panel-group">
                                <div class="panel panel-danger">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <span>
                                                <span class="glyphicon glyphicon-ban-circle"></span>
                                                <spring:message code="home.upload.file.error"/>
                                            </span>
                                        </h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <span id="uploadErrorMsg"></span>
                            <span id="someProductsAdded">
                                <b>
                                    <spring:message code="home.upload.file.some"/>
                                </b>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	
    <!-- Modal pop up for file upload error ends -->

    <cms:pageSlot position="BroadCastContent" var="feature" element="div">
        <cms:component component="${feature}"/>
    </cms:pageSlot>

    <cms:pageSlot position="BroadCastInformationContent" var="feature" element="div">
        <cms:component component="${feature}"/>
    </cms:pageSlot>

    <homeLa:addQuickToCart />

    <cartLa:contractPopup/>

</templateLa:page>

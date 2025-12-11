<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="header"  tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/nav" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/home" %>
<%@ taglib prefix="chatlive" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/genesys" %>
<%@ attribute name="isAdminUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isFirstTimeLogin" required="false" type="java.lang.Boolean" %>
<%
    request.setAttribute("isAdminUser","${adminUser}" ); 
    request.setAttribute("isFirstTimeLogin", true);
%>
<chatlive:chatWidget/>
<script>
    var hjid_Id = ACC.config.hjid;
    (function(h,o,t,j,a,r){
        h.hj=h.hj||function(){(h.hj.q=h.hj.q||[]).push(arguments)};
        h._hjSettings={hjid:hjid_Id,hjsv:5};
        a=o.getElementsByTagName('head')[0];
        r=o.createElement('script');r.async=1;
        r.src=t+h._hjSettings.hjid+j+h._hjSettings.hjsv;
        a.appendChild(r);
    })
    (window,document,'//static.hotjar.com/c/hotjar-','.js?sv=');
</script>

<!--BEGIN QUALTRICS WEBSITE FEEDBACK SNIPPET-->
<script type = 'text/javascript' >
    (function() {
        var g = function(e, h, f, g) {
            this.get = function(a) {
                for (var a = a + "=", c = document.cookie.split(";"), b = 0, e = c.length; b < e; b++) {
                    for (var d = c[b];
                        " " == d.charAt(0);) d = d.substring(1, d.length);
                    if (0 == d.indexOf(a)) return d.substring(a.length, d.length)
                }
                return null
            };
            this.set = function(a, c) {
                var b = "",
                    b = new Date;
                b.setTime(b.getTime() + 6048E5);
                b = "; expires=" + b.toGMTString();
                document.cookie = a + "=" + c + b + "; path=/; "
            };
            this.check = function() {
                var a = this.get(f);
                if (a) a = a.split(":");
                else if (100 != e) "v" == h && (e = Math.random() >= e / 100 ? 0 : 100), a = [h, e, 0], this.set(f, a.join(":"));
                else return !0;
                var c = a[1];
                if (100 == c) return !0;
                switch (a[0]) {
                    case "v":
                        return !1;
                    case "r":
                        return c = a[2] % Math.floor(100 / c), a[2]++, this.set(f, a.join(":")), !c
                }
                return !0
            };
            this.go = function() {
                if (this.check()) {
                    var a = document.createElement("script");
                    a.type = "text/javascript";
                    a.src = g;
                    document.body && document.body.appendChild(a)
                }
            };
            this.start = function() {
                var t = this;
                "complete" !== document.readyState ? window.addEventListener ? window.addEventListener("load", function() {
                    t.go()
                }, !1) : window.attachEvent && window.attachEvent("onload", function() {
                    t.go()
                }) : t.go()
            };
        };
        try {
        	
        	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('jnj.la.qualtrics.qsival')" var="qsival" scope="session"/>
        	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('jnj.la.qualtrics.url')" var="qualtricsUrl" scope="session"/>
        	
            (new g(100, "r", "${qsival}", "${qualtricsUrl}")).start()
        } catch (i) {}
    })();
</script><div id='ZN_5586sDbUEaBikSh'><!--DO NOT REMOVE-CONTENTS PLACED HERE--></div >
<!--END WEBSITE FEEDBACK SNIPPET -->

<input type="hidden" name="browserCompatible"
		value='<spring:message code="browser.compatible.header"/>..<a  href="javascript:void(0)" id=buorgcloseid><spring:message code="browser.compatible.dismiss"/><a/>'
		id="browserCompatible" />
<input type="hidden" name="browserCompatibletest" value='true' id="browserCompatibletest" />
<input type="hidden" value="" id="currAccScreenName" />
	
<button id="hamburg-btn" type="button" class = "navbar-toggle pull-left" data-toggle = "collapse" data-target ="#jnj-menu">								
    <i class="bi bi-list"></i>
</button>

<div class="row" id="jnj-header">
    <div class="col-lg-12 shadow-header" style="
    height: fit-content;
" >
  <div class="row" style="height: fit-content;padding-bottom: 20px;justify-content: start;align-items: center;">
	    <div class="col-lg-7 col-md-7 col-9 col-3 no-padding" id="jnj-logo-holder">
		    <cms:pageSlot position="SiteLogo" var="logo" limit="1">
			    <cms:component component="${logo}" />
			</cms:pageSlot>
		</div>
			<c:if test="${((empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)) && (placeOrderGroupFlag eq true || not isSerialUser)}" >
				<div class="padding-0-tablet col-lg-1 col-md-1 col-3 col-2 pull-right order-sm-2 order-xs-2 order-lg-3 order-md-3">
					<div id="cart-btn-holder" class="pull-right">
			            <cms:pageSlot position="MiniCart" var="cart" limit="1">
	                        <cms:component component="${cart}"/>
                        </cms:pageSlot>
					</div>
				</div>
				<header:searchBox />
		</c:if>
		</div>
	</div>
</div>

<input type="hidden" value="${adminUser}" id="isAdminUserHddn" />
<input type="hidden" value="" id="hddnTempAccountList" autocomplete="off" />
<input type="hidden" name="cloudmersiveAPIURL" id="cloudmersiveAPIURL" value="${cloudmersiveAPIURL}"/>
<input type="hidden" name="cloudmersiveAPIKey" id="cloudmersiveAPIKey" value="${cloudmersiveAPIKey}"/>
	
<div id="selectaccountpage" class="changeAccountPopupContainer">
    <input id="accountNumberOfPages" value="${accountPaginationData.numberOfPages}" type="hidden" />
	<input id="accountSearchTerm" value="${accountSearchTerm}" type="hidden" />
	<input id="currentPage" value="${currentPage}" type="hidden" />
	<!-- Modal -->
	<c:if test="${firstTimeLogin}">
       <account:changeAccountPopup isFirstTimeLogin="true"/>
	</c:if>
	<!--End of Modal pop-up-->
</div>
								
<!-- Added By Vijay for Loading ....Display -->
<div id="laodingcircle" style="display:none">
    <div class="modal-backdrop in">
        <!-- Modal content-->
        <div class="row panelforcircle">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div class="loadcircle"><img src="${webroot}/_ui/responsive/common/images/ajax-loader.gif"></div>
            </div>
        </div>
    </div>
</div>
<div class="row" style="bottom: 0px;position: fixed !important;right: 0px !important;margin-right: 50px!important;z-index: 100;">
   <div class="buttonTable liveChatpopup_hn">
      <table style="height: 45px; border-color: #030303; border-radius: 10px; background-color: #288297; width: 250px;border-collapse: collapse;
               border-spacing: 0;
               opacity: .7;
               border-bottom-left-radius: 0em !important;
               border-bottom-right-radius: 0em !important;    "
            role="button">
         <tbody>
         <tr id="webChatButton" onclick="openLauncher()">
            <td style="padding-left: 20px; padding-top: 20px;" colspan="1"
               rowspan="2"><input type="image"
                              src="${webroot}/_ui/responsive/common/images/chatBtn.png"
                              alt=""
                              width="35" height="27" class="page-1" role="button">
            </td>
            <td style="padding-top: 10px !important; float: left !important;"
               colspan="3" rowspan="2"><span class="livechat"><strong><span
                  style="color: #ffffff;"><span
                  style="float: left; padding-top: 0px !important; font-size: 36px; font-weight: 700; width: 146px; height: 49px;"> live chat</span></span></strong></span>
            </td>
         </tr>
         </tbody>
      </table>
   </div>
</div>


<div class="modal fade jnj-popup" id="malware-detail-popup" role="dialog" >
        <div class="modal-dialog modalcls modal-md">        
            <div class="modal-content"> 
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
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
												<spring:message code="common.malware.error.message" /> </span> 
                                              
                                        </h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                       
                    </div>
                </div>
            </div>
        </div>
    </div>


<div id="privacypolicypopuopholder"></div>
<div id="legalnoticepopupholder"></div>
<div id="termsandconditionsholder"></div>
<div id="contactuspopupholder"></div>
<div id="updateprivacypopupholder"></div>
<!-- product details popup tag -->
<div id="product-details-popupholder"></div>
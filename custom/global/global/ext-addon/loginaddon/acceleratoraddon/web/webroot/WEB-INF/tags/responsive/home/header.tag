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
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>

<input type="hidden" value="GT" id="datepickerformat" /><!-- Modified by Archana for AAOL-5513 -->
<input type="hidden" name="browserCompatible" value='<spring:message code="browser.compatible.header"/>..<a  href="javascript:void(0)" id=buorgcloseid><spring:message code="browser.compatible.dismiss"/><a/>' id="browserCompatible" />
<input type="hidden" name="browserCompatibletest" value='true' id="browserCompatibletest" />
<input type="hidden" name="scrLocation" value='${commonResourcePath}/js/acc.browsercompatible.js' id="scrLocation" />
<c:set var="scrLocation" value='${commonResourcePath}/js/browserCompatible.js'/>







<script>
	var $buoop = {c:2,
			text: document.getElementById("browserCompatible").value
			}; 
	function $buo_f(){ 
	 var e = document.createElement("script"); 
	 e.src = document.getElementById("scrLocation").value;
	 document.body.appendChild(e);
	};
	try {
		document.addEventListener("DOMContentLoaded", $buo_f,false)
		}
	catch(e){
		window.attachEvent("onload", $buo_f)
		}
	
</script>
<style>
.label {
	color: #000000;
}
</style>





<header>
		<div class="row" id="jnj-header">
			<div class="col-lg-4 col-md-4 col-sm-3 col-xs-3 no-padding">
			
			<cms:pageSlot position="SiteLogo" var="logo" limit="1" id="logo"  >
			<cms:component component="${logo}" />
		  </cms:pageSlot>
			</div>
			
			<div class="col-lg-8 col-md-8 col-sm-9  col-xs-9 no-padding" id="header-right-holder">
			<div id="cart-btn-holder" class="pull-right">
			
			
			
				 <button type="button" class="btn btn-default glyphicon glyphicon-shopping-cart">
			
			<cms:pageSlot position="MiniCart" var="cart" limit="1">
	        <cms:component component="${cart}"/>
            </cms:pageSlot>
			</button>
			</div>	
			<header:searchBox/>
			
			
			</div>
		</div>
</header>

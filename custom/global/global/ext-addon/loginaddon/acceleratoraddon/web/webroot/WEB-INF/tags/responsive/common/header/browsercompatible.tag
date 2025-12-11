<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div id="browserCompatibleid" style='display:block;'>
	<div class="lightboxtemplate changeAccountBasketPopUp">
	<h2><spring:message code="browser.compatible.header"/></h2>
    <div class='changeAccountWrapper  marginRight' style='display:block;'>
        <div id=accountpopup class='changeAccountContent changeAccountColorBox '>
        <ul>
        <li class="browsercompatible"><span>
        	<spring:message code="browser.compatible.massageContent"/>
        	</span>
        </li>
        </ul>
        </div>
        <ul>
        <li class="txtCenter">
        	<input type='button' class='secondarybtn' id="browsercompatiblebutton" value='<spring:message code="common.msg.ok"/>' />
        </li>
        </ul>
  </div>
  </div>
</div>


	

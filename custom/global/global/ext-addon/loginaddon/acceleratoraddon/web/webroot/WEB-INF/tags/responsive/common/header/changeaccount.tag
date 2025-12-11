<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="changeAccountMini">
	<div class="lightboxtemplate changeAccountBasketPopUp">
	<h2><spring:message code="change.account.select"/></h2>
    <div class='changeAccountWrapper' style='display:block;'>
    	<input type='text' class='placeholder changeContractSearch' id="changeAccount" value="" placeholder='<spring:message code="change.account.search"/>' />
        <div id=accountpopup class='changeAccountContent changeAccountColorBox'>
        </div>
  </div>
  </div>
</div>


	

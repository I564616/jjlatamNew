 <%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 <%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
 <div class="info">
 <p class="comment"><spring:message code="cart.checkout.orderinfo"/>
 <a href="javascript:void(0)" onclick="javascript:$('#moreInfo').show();javascript:$('#moreInfoLink').hide();" id="moreInfoLink"><spring:message code="cart.common.more.info"/></a></p></div>
 <div id="moreInfo" class="hide">
	<cms:component uid="cut_Off_More_Info"/> 
</div>
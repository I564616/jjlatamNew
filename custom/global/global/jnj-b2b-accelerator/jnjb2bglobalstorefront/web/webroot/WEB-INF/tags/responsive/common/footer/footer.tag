<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<!--<script type="text/javascript" src="${commonResourcePath}/js/acc.privacypolicypopup.js"></script>  --> 
 <script type="text/javascript" src="${commonResourcePath}/js/acc.legalnoticepopup.js"></script>
 <script type="text/javascript" src="${commonResourcePath}/js/acc.termconditionpopup.js"></script>
<footer><div class="row jnj-grey" id="jnj-footer">
<div class="col-lg-12 col-md-12">
	<input type="hidden" name="pdfPopupGenration" id="pdfPopupGenration" value="${pdfPopupGenrationFlg}"/>
<c:choose>
	<c:when test="${not isSerialUser}">
		<cms:pageSlot position="Footer" var="feature" >
			<cms:component component="${feature}"/>
	</cms:pageSlot>
	</c:when>
	<c:otherwise>
		<cms:pageSlot position="FooterSerial" var="feature" >
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</c:otherwise>
</c:choose>

</div></div></footer>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="hide" id="errorDetailsAddToCart">
	<div class="lightboxtemplate" id="errorDetails">
		<form:form method="post" action="javascript:;">
			<h2><spring:message code="homePage.errorDetails"/></h2>
			<div class="sectionBlock body popupButtonWrapper uploadFile">
				<div class="globalError">
					<p><spring:message code="homePage.errordetails.addfailed"/>
						</p>
				</div>
				<div class="scroll">
					<ul>
					</ul>
				</div>
			</div>
		</form:form>
	</div>
</div>



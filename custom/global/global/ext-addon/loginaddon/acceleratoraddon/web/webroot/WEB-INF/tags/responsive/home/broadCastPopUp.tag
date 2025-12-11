<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/responsive/home"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="hide" id="broadCastPopUp">
	<div class="lightboxtemplate" id="errorDetails">
		<form:form method="post" action="javascript:;">
			<div class="sectionBlock body popupButtonWrapper uploadFile">
				<div class="homeAlertMsg hide" id="alert">
					<spring:message code="homePage.broadCast.alert" />
					<p class="comment">
						<span class="bold"></span>
					</p>
				</div>
				<div class="homeInfoMsg hide" id="info">
					<spring:message code="homePage.broadCast.notice" />
					<p class="comment">
						<span class="bold"></span>
					</p>
				</div>
				<div class="homeSuccessMsg hide" id="success">
					<spring:message code="homePage.broadCast.success" />
					<p class="comment">
						<span class="bold"></span>
					</p>
				</div>
			</div>
		</form:form>
	</div>
</div>
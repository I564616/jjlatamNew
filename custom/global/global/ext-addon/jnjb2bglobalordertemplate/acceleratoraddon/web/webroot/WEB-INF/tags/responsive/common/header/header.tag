<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/desktop/common/header" %>
<div id="header">
	<div class="siteLogo">
		<cms:pageSlot position="SiteLogo" var="logo" limit="1">
			<cms:component component="${logo}"/>
		</cms:pageSlot>
	</div>
	<div class="headerContent">
		<div class="userinfo">
			<ul class="welome_msg">
				<li class="mainFontStyle">
					<spring:message code="header.information.welcome"/>,&nbsp;
					<span>
						<sec:authorize ifAnyGranted="ROLE_CUSTOMERGROUP">
							<ycommerce:testId code="header_LoggedUser">${user.firstName}&nbsp;${user.lastName}</ycommerce:testId>
						</sec:authorize>
					</span>
				</li>
			</ul>
			<ul class="customer_account">
				<li class="userLinks">
					<a  class="icon-sprite icon-user" href="<c:url value='/my-account/personalInformation'/>" ><spring:message code="header.information.profile.link"/></a>
					<span>|</span>
					<ycommerce:testId code="header_signOut">
						<a class="icon-sprite icon-logout" href="<c:url value='/logout'/>"><spring:message code="header.information.signout"/></a>
					</ycommerce:testId>
				</li>
			</ul>
		</div>
		<header:searchBox/>
	</div>
	<div class="accountInfo span-24">
		<div class="userAccountInfo">
			<ul>
				<li>
					<spring:message code="header.information.account.number"/>&nbsp;
					<span>
						<c:choose>
							<c:when test="${accountUID ne null}">
								${accountUID}
							</c:when>
							<c:otherwise>
								${user.currentB2BUnitID}
							</c:otherwise>
						</c:choose>
					</span>
				</li>
				<li>
					<spring:message code="header.information.account.name"/>&nbsp;
					<span>
						<c:choose>
							<c:when test="${accountName ne null}">
								${accountName}
							</c:when>
							<c:otherwise>
								${user.currentB2BUnitName}
							</c:otherwise>
						</c:choose>
					</span>
				</li>
				<c:if test="${user.jnjSiteName eq 'MDD' && ((accountGLN ne 'null' && not empty accountGLN) || not empty user.currentB2BUnitGLN )}" >
					<li>
						<spring:message code="header.information.account.gln"/>&nbsp;
						<span>
							<c:choose>
								<c:when test="${accountGLN ne 'null' && not empty accountGLN}">
									${accountGLN}
								</c:when>
								<c:otherwise>
									${user.currentB2BUnitGLN}
								</c:otherwise>
							</c:choose>
						</span>
					</li>
				</c:if>
			</ul>
		</div>
		<c:if test="${user.showChangeAccount eq 'true'}">
			<div class="changeAccount">
				<a href="javascript:;"><spring:message code="header.information.account.change.link"/></a>
			</div>
		</c:if>
		<input type="hidden" value="${user.adminUser}" id="isAdminUserHddn" />
		<account:changeAccountPopup isAdminUser="${user.adminUser}" isFirstTimeLogin="${firstTimeLogin}" />
	</div>
	<div class="navigationWrapper"> 
		<nav:topNavigation/>
	</div>
	<input type="hidden" value="" id="hddnTempAccountList" autocomplete="off" />
</div>
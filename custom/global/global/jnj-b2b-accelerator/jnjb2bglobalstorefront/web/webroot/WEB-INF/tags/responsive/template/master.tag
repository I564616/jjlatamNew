<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/addons/loginaddon/shared/analytics" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- <%@ taglib prefix="templateLogin" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %> --%>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('jnjb2bglobalstorefront.webroot')" var="webroot" scope="session"/>
<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
<head>
    
    <title>${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Accelerator Title'}</title>
<%--     <meta http-equiv="X-UA-Compatible" content="IE=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

		Additional meta tags
		<c:forEach var="metatag" items="${metatags}">
			<c:if test="${not empty metatag.content}" >
				<meta name="${metatag.name}" content="${metatag.content}" />
			</c:if>
		</c:forEach> --%>

		  <meta charset="UTF-8">
  		<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%-- <spring:theme code="img.favIcon" text="/" var="favIconPath"/>
        <link rel="shortcut icon" type="image/x-icon" media="all" href="${originalContextPath}${favIconPath}" /> --%>

    <%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
    <template:styleSheets />

    <%-- Inject any additional CSS required by the page --%>

    <jsp:invoke fragment="pageCss"/>

    <analytics:analytics />

    <template:javaScriptVariables/>

    <%-- j query 1.7.2 --%>
    <%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery-1.7.2.min.js"></script> --%>
  </head>

  <body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${currentLanguage.isocode} pushmenu-push" >
    <input type="hidden" id="analyticsTrackingId" value="${googleAnalyticsTrackingId}" />
    <%-- Start : sanchit.a.kumar :: Modal window for Ajax loading spinner --%>
    <div id="ajaxCallOverlay">
    </div>
    <div id="modal-ui-dialog" class = "ui-dialog" class="ui-dialog">
      <div id="dialog-modal" class="ui-dialog-content">
        <messageLabel:message messageCode="home.loadingAjax"/>
      </div>
    </div>
    <%-- End : sanchit.a.kumar :: Modal window for Ajax loading spinner --%>

    <%-- Inject the page body here --%>
    <jsp:doBody/>


    <%-- Load JavaScript required by the site --%>
    <template:javaScript />

    <%-- Inject any additional JavaScript required by the page --%>
    <jsp:invoke fragment="pageScripts"/>

    <script type="text/javascript">
      /*<![CDATA[*/
      $(function () {
        $('.strength').pstrength({verdicts: ["<spring:message code="password.strength.veryweak" />",
            "<spring:message code="password.strength.weak" />",
            "<spring:message code="password.strength.medium" />",
            "<spring:message code="password.strength.strong" />",
            "<spring:message code="password.strength.verystrong" />"],
          tooShort: "<spring:message code="password.strength.tooshortpwd" />",
          minCharText: "<spring:message code="password.strength.minchartext"/>"});
      });
      /*]]>*/
    </script>

  </body>

  <%-- <template:debugFooter /> --%>
</html>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template" %>

<templateLa:page pageTitle="${pageTitle}">

    <spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
    <c:url value="/home" var="homePageUrl" />
    <c:url value="/cart/checkout" var="checkoutUrl" />

    <input type="hidden" value="${canValidateCart}" id="canValidateCart">
    <input type="hidden" value="${displayBatchModeAlert}" id="displayBatchModeAlert">
    <div id="globalMessages">
        <cart:cartRestoration />
        <cart:cartValidation />
    </div>

    <c:if test="${empty cartData.entries}">
        <cartLa:emptyCart/>
    </c:if>
    <c:if test="${ not empty cartData.entries}">
        <cartLa:cartEntries/>
    </c:if>

    <cartLa:indirectCustomerAndPayerPopup/>
	<cartLa:saveAsTemplateDiv/>
    <cartLa:contractPopup/>

</templateLa:page>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartPage.js"></script>

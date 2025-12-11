<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="password" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/password"%>
<%@ taglib prefix="passwordLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/password"%>



<template:registrationPage pageTitle="${pageTitle}">

    <div>
        <!-- First Light-Box Data for password -->
        <passwordLa:firstTimeResetPassword />
        <!-- Second Light-Box Data for password -->
        <password:successResetPassword />
    </div>

	<div id="privacypolicypopuopholder"></div>
	<div id="termsandconditionsholder"></div>
	<div id="legalnoticepopupholder"></div>
	<div id="contactuspopupholder"></div>

</template:registrationPage>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>
<input type="hidden" value="${user.adminUser}" id="isAdminUserHddn" />
<%-- <account:changeAccountPopup isAdminUser="${user.adminUser}" isFirstTimeLogin="false"/> --%>
<account:changeAccountPopup isAdminUser="${adminUser}" isFirstTimeLogin="false"/>
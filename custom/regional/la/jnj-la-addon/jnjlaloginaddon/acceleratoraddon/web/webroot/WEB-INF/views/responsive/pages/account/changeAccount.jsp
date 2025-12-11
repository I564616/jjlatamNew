<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/home" %>
<input type="hidden" value="${user.adminUser}" id="isAdminUserHddn" />
<account:changeAccountPopup isAdminUser="${adminUser}" isFirstTimeLogin="false"/>
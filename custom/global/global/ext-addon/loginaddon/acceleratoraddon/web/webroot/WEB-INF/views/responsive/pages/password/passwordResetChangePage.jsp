<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="password" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/password"%>

<div class="modal fade jnj-popup forgotpopup " id="forgotPopup" role="dialog">
<div class="modal-dialog modalcls modal-md">
	 <!-- First Light-Box Data for password -->
	<password:emailVerificationResetPassword />
	<!-- Second Light-Box Data for password -->
	<password:questionsVerificationResetPassword />
	
</div>
</div>
<!-- Third Light-Box Data for password --> 
	<password:resetPassword />
	<!-- Fourth Light-Box Data for password -->
	<password:successResetPassword />
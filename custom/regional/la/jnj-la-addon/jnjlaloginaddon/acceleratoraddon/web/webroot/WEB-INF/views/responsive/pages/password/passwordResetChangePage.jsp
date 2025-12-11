<%@ page trimDirectiveWhitespaces="true" %>
                                     <%@ taglib prefix="password" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/password"%>
                                     <%@ taglib prefix="passwordLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/password"%>

                                     <div class="modal fade jnj-popup forgotpopup " id="forgotPopup" role="dialog">
                                     <div class="modal-dialog modalcls modal-md">
                                     	 <!-- First Light-Box Data for password -->
                                     	<passwordLa:emailVerificationResetPassword />
                                     	<!-- Second Light-Box Data for password -->
                                     	<passwordLa:questionsVerificationResetPassword />
                                     	<passwordLa:multimodePasswordResetOptions />
                                        <passwordLa:multimodePasswordResetEmail />
                                        <passwordLa:emailVerificationFailed />
                                        <passwordLa:emailPasswordResetFailed />
                                        <passwordLa:emailPasswordResetFailedForbidden />
                                     </div>
                                     </div>
                                     <!-- Third Light-Box Data for password -->
                                         <passwordLa:resetPassword />
                                         <!-- Fourth Light-Box Data for password -->
                                         <password:successResetPassword />
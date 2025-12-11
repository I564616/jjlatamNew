<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjglobalprofile/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<spring:message code="profile.changePassword.myprofile" />

		
									<div class="modal fade" id="firstloginpopup" role="dialog" data-firstLogin='true' data-backdrop="static" data-keyboard="false">
										<div class="modal-dialog modalcls">
										
											<div class="modal-content popup">
												<div class="modal-header">
													<h4 class="modal-title selectTitle"><spring:message code="updated.privacy.header" /></h4>
												</div>
												<div class="modal-body">
												 <cms:pageSlot position="MainBody" var="feature" >
														<cms:component component="${feature}"/>
													</cms:pageSlot>
													<div class="checkbox checkbox-info">
														<input id="agree-check" class="styled" type="checkbox">
														<label for="agree-check">
															<spring:message code="updated.privacy.checkbox" /> <a href="#" class="privacypolicypopup_hn" title="Privacy Policy"><spring:message code="updated.privact.text" /></a>
														</label>
													</div>
												 <div id="privacyPolicyBlockValidation" class="error" style="display: none"><spring:message code="updated.privacy.accept.text" /></div>									
												</div>			
												
												<div class="modal-footer ftrcls">
													<a href="#" class="pull-left first-login-cancel" id="privacyPolicyUpdateBtnNo"><spring:message code="updated.privacy.cancel.text" /></a>
													<button type="button" class="btn btnclsactive" data-dismiss="modal" id="privacyPolicyUpdateBtnYes"><spring:message code="updated.privacy.submit" /></button>
												</div>
											</div>
										  
										</div>
									</div>
								<!--End of Modal pop-up-->
							
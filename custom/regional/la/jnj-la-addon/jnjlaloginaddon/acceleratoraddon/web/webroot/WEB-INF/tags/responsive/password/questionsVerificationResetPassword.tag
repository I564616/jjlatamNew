<%@ taglib prefix="message" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

				<!-- Modal content-->
				<div class="modal-content"  id='recoverpasswordP2'  style='display:none'>
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='la.popup.close'/></button>
					  <h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword"/></h4>
					</div>
					<form:form id="recoverpasswordFormP2" action="javascript:;" method="post">
                    <input type="hidden"  id="emptyQuestionAnswer" value='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>'/>
					<div class="modal-body row">						
						<div class="col-xs-12">
							<p><spring:message code="password.forgotPassword.secretQuestionsHeader" /></p>
							<input type='hidden' id='blockedUser' data-msg-required='<spring:message code="password.forgotPassword.blockedError"/>' />
			                <input type='hidden' id='secretquestionsError' data-msg-required='<spring:message code="password.forgotPassword.secretquestionsError"/>' />
							<div class="form-group">
							        
								<label for='secq1' class="labelText" ><spring:message code="password.forgotPassword.question1" />
								<sup><spring:message code="password.forgotPassword.star" /></sup> <spring:message code="password.forgotPassword.colon" />
								</label>
								<br>
				           <select id='secq1'  value="" data-width="100%" name='secq1' class='required form-control forgot-popup-form' data-msg-required='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>' >
                       		<option value='0'>
                               	<spring:message code="password.forgotPassword.selectAQuestion" />
                           	</option>
                          	</select>
						<input type='text' id="ans1" name='text' class='required form-control margintop10' data-msg-required='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>' />
						<div class='recPassError marTop10'> </div>
							</div>
							<div class="form-group">
								<label  for='secq2' class="labelText" ><spring:message code="password.forgotPassword.question2" /><sup><spring:message code="password.forgotPassword.star" /></sup><spring:message code="password.forgotPassword.colon" /></label><br>
						<select id='secq2' name='secq2' class='required form-control forgot-popup-form'  data-width="100%" data-msg-required='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>' >
							<option value='0'>
                               	<spring:message code="password.forgotPassword.selectAQuestion" />
                           	</option>
                       	</select>
						<input type='text' id="ans2" name='text2' class='required form-control margintop10'  data-msg-required='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>' />
						<div class='recPassError marTop10'> </div>
							</div>
							<div class="form-group ">
								<label  for='secq3' class="labelText" ><spring:message code="password.forgotPassword.question3" /><sup><spring:message code="password.forgotPassword.star" /></sup><spring:message code="password.forgotPassword.colon" /></label><br>
								<select id='secq3' name='secq3' class='required form-control forgot-popup-form'  data-width="100%" data-msg-required='<spring:message code="password.forgotPassword.emptyQuestionAndAnswer"/>'>
                        	<option value='0'>
                                <spring:message code="password.forgotPassword.selectAQuestion" />
                            </option>
                       	</select>
						<input type='text' id="ans3" name='text3' class='required form-control margintop10' data-msg-required='<spring:message code="password.forgotPassword.secretquestionsError"/>' />
						<div class='recPassError marTop10'> </div>
							</div>
							<div class='recPassError height75'> </div>
						</div>
						<div class="col-xs-12">
							<input id='passnext2' class='secondarybtn secretquesvalidate btn btnclsactive col-xs-2 text-uppercase pull-right no-border' value='<spring:message code="password.forgotPassword.next"/>' type='submit' />
						</div>
					</div>
					</form:form>
				</div>
			




















<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="contactUs" tagdir="/WEB-INF/tags/responsive/contactus"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>

<script type="text/javascript">
    var captchaSiteKey = "${googleCaptchaKey}";
    var otploadCallback = function() {
        captchaContainer = grecaptcha.render('contact_recaptcha_content', {
            'sitekey' : captchaSiteKey,
            'callback' : function(response) {
                if(response!= ' '){
            	    document.getElementById("contactUsFormSubmitLb").disabled=false;
            	    $("#captchaResponse").val(response);
                }
            }
        });
      };
</script>

<input type="hidden" id="captchaResponse" value=""/>
<!-- Contact Us Popup-->
<div class="modal fade jnj-popup" id="contactuspopupNew" role="dialog">
	<div class="modal-dialog modalcls modal-md">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					<spring:message code="help.page.contactus.label" />
				</h4>
				<a type="button" class="close clsBtn" data-bs-dismiss="modal">
					<spring:message code="account.change.popup.close" />
				</a>
			</div>
			<div class="modal-body">
			<form:form modelAttribute="contactUsForm" class="contactUsFormLb" method="POST">
				<div class="contactUsFormPage1Lb">
					<div class="row">
						<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
							<div class="">
								<div>
									<spring:message code="help.page.contactus.head" />
								</div>
								<div class="form-group login-margintop10">
									<label for="email">
									    <spring:message code="help.page.contactus.from" />:
									    <sup class="redStar">*</sup>
									</label>
									<c:choose>
										<c:when test="${not empty nameOfCustomer}">
											<input type="text" class="form-control"
											    name="contactUsFromName" id="contactUsFromNameLb"
												value="${nameOfCustomer}" disabled="disabled" />
										</c:when>
										<c:otherwise>
											<input type="text" name="contactUsFromName"
												class="validationevent form-control" id="contactUsFromNameLb" value=""
												required="required" data-msg-required="Please enter your name"/>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="form-group">
									<label for="email">
									    <spring:message code="help.page.contactus.email" />:
									    <sup class="redStar">*</sup>
									</label>
								    <c:choose>
		                				<c:when test="${not empty emailOfCustomer}">
		                					<input name="contactUsEmail" type="email" class="form-control"
		                					    id="contactUsEmailLb" value="${emailOfCustomer}" disabled="disabled" />
		                				</c:when>
		                				<c:otherwise>
		                				    <input name="contactUsEmail" type="email" class="form-control"
		                				        id="contactUsEmailLb" data-msg-required="Please enter your email"
		                				        value="" required="required"/>
		                	            </c:otherwise>
		                            </c:choose>
								</div>
								<div class="form-group contactmessagebox">
									<label for="email">
										<spring:message	code="help.page.contactus.subject" />:
										<sup class="redStar">*</sup>
									</label>
									<select	class="form-control" id="contactUsSubjectLb"
										name="contactUsSubjectLb" required="required"
										data-msg-required="<spring:message code='help.page.subject.error'/>">
										<option value="">
										    <spring:message code="help.page.issue.selectanyone" />
										</option>
										<c:forEach items="${subjectDropDown}" var="subjectMap">
											<option value="${subjectMap.key}">
											    <spring:message code="${subjectMap.value}" />
											</option>
										</c:forEach>
									</select>
								</div>
						        <div class="form-group">
						            <div id="contactUsOrderNumberLb" class="contactUsOrderNumberLb" style="display: none;">
		                                <label for="contactUsOrderNumberLb">
		                                    <spring:message code="help.page.contactus.ordernumber"/>:
		                                    <sup class="redStar">*</sup>
		                                </label><br>
		                                <input type="number" min="0" class="form-control numeric" id="contactUsOrderNumber" name="contactUsOrderNumberLb"
		                                    data-msg-required="<spring:message code='help.page.order.error'/>" required="required" >
		                            </div>
		                        </div>
				            </div>
			            </div>
			            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
				            <cms:pageSlot position="CallUsPopUp" var="comp">
					            <div class="span-5 contactUsFormText">
						            <cms:component component="${comp}" />
					            </div>
				            </cms:pageSlot>
			            </div>
		                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 login-margintop10">
			                <div class="form-group">
				                <label for="comment">
				                    <spring:message code="help.page.contactus.message" />:
				                </label>
				                <textarea class="validationevent form-control" rows="5" id="contactUsMessage" name="contactUsMessage"
				                    data-msg-required="<spring:message code='help.page.message.error'/>" required="required">
				                </textarea>
			                </div>

							<div class="profileRegBlock"  style="width: 354px;padding: 0px;">
								<div class="captchaInput overFlow">
									<spring:message code="text.register.validation" />
									<span>
									    <spring:message code="register.stepOne.required.star" />
									</span>
									<spring:message code="text.register.colon" />
								    <br>
								    <div id="contact_recaptcha_content" style="width: 440px;"></div>
									<script src="https://www.google.com/recaptcha/api.js?onload=otploadCallback&render=explicit&hl=${googleCaptchaLanguageIso}" async defer></script>
										<input type="hidden" id="contactUsCaptchaId"
											value='<spring:message code="error.text.validation.captcha"/>'/>
										<div class="contactUsErrorCaptcha"></div>
								</div>
							</div>



			                <div class="checkbox checkbox-info selectchkbox inline-element">
                                <input id="contactus-agree" class="styled" type="checkbox">
                                    <label for="contactus-agree">
                                        <spring:message code="help.page.contactus.popup.statement" />
                                    </label>
                            </div>
		                </div>
	                </div>
                </div>

                <div class="modal-footer">
	                <button type="button" class="btnclsactive contactUsSubmitBtn pull-right btn-disabled-style"
	                    id="contactUsFormSubmitLb" disabled>
		                <spring:message code="help.page.contactus.sendbuttoncaps" />
	                </button>
                </div>
            </form:form>

            <div class="contactUsFormPage2Lb">
                <c:choose>
                    <c:when test="${mailSent eq true}">
                        <div>
                           <h3>
                               <spring:message code="help.page.contactus.success.successmessage" />
                           </h3>
                           <p>
                               <spring:message code="help.page.contactus.success.thankyoumessage" />
                           </p>
                           <p>
                               <spring:message code="help.page.contactus.success.responsemessage" />
                           </p>
                           <a class="backToContactUsForm" href="#">
                               <spring:message code="help.page.contactus.success.anothermessage" />
                           </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="errorMessage">
                           <laCommon:genericMessage messageCode="help.page.contactus.error.message" icon="ban-circle" panelClass="danger" />
                           <a class="backToContactUsForm" href="#">
                              <spring:message code="help.page.contactus.success.anothermessage" />
                           </a>
                        </div>
                    </c:otherwise>
                </c:choose>
			</div>
		</div>
	</div>
</div>
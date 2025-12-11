<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>


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

<div class="modal-body">
    <c:url value="/help/contactUs" var="sendEmail"></c:url>
    <form action="${sendEmail}" class="contactUsFormLb" method="POST">
        <div class="row">
            <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                <div class="">
                    <p><spring:message code="help.page.contactus.text" /></p>
                    <div class="size1of1 cell">
                        <div class="cell">
                            <div class="contactUsGlobalError registerError">
                                <label class="error" style="display: none;"><spring:message
                                code="help.page.contactus.reqfields" />.<br />
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label for="contactUsFromNameLb">
                            <spring:message code="help.page.contactus.from" />:<span class="redStar">*</span>
                        </label>
                        <c:choose>
                            <c:when test="${not empty nameOfCustomer}">
                                <input name="contactUsFromName" id="contactUsFromNameLb"
                                    value="${nameOfCustomer}" disabled="disabled"
                                    class="form-control" />
                            </c:when>
                            <c:otherwise>
                                <input name="contactUsFromName" class="required form-control"
                                    id="contactUsFromNameLb"
                                    data-msg-required="Please enter your name" value="" />
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="form-group">
                        <label for="contactUsEmailLb">
                            <spring:message code="help.page.contactus.email" />:<span class="redStar">*</span>
                        </label>
                        <c:choose>
                            <c:when test="${not empty emailOfCustomer}">
                                <input name="contactUsEmail" id="contactUsEmailLb"
                                    value="${emailOfCustomer}" disabled="disabled"
                                    class="form-control" />
                            </c:when>
                            <c:otherwise>
                                <input name="contactUsEmail" class="required form-control"
                                    id="contactUsEmailLb"
                                    data-msg-required="Please enter your email" value="" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group">
                        <div>
                            <label for="contactUsSubjectLb">
                                <spring:message code="help.page.contactus.subject" />:<span class="redStar">*</span>
                            </label>
                        </div>
                        <select class="required" id="contactUsSubjectLb"
                            name="contactUsSubject"
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
                        <div class="registerError"></div>
                    </div>
                    <div id="contactUsOrderNumberLb" class="contactUsOrderNumberLb" style="display: none;">
                        <label for="contactUsOrderNumberLb">
                            <spring:message code="help.page.contactus.ordernumber" />:<span class="redStar">*</span>
                        </label><br>
                        <input class="form-control" id="contactUsOrderNumber" name="contactUsOrderNumberLb"
                            data-msg-required="<spring:message code='help.page.order.error'/>"
                            required="required">
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 margintop10" style="width: 200%">
                            <div class="form-group">
                                <label for="contactUsMessage">
                                    <spring:message code="help.page.contactus.message" />
                                    <span class="redStar">*</span>
                                </label>
                                <textarea rows="5" id="contactUsMessage" class="required form-control validationevent" name="contactUsMessage"
                                    data-msg-required="<spring:message code='help.page.message.error'/>">
                                </textarea>
                            </div>
                        </div>
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
                    <div class="checkbox checkbox-info selectchkbox content contactUsFormText inline-element" style="border-bottom: 1px solid #ccc;">
                        <input id="contactus-agree" class="styled" type="checkbox">
                        <label for="contactus-agree">
                            <spring:message code="help.page.contactus.help.statement" />
                        </label>
                    </div>
                    <div class="modal-footer">
                        <input type="button"
                            class="primarybtn contactUsFormSubmitLb btnclsactive btn-disabled-style contactUsSubmitBtn pull-right text-uppercase"
                            value="<spring:message code='help.page.contactus.submit' />" disabled/>

                    </div>
                    <div class="contactUsFormPage2Lb" id="contactUsFormPage2Lbs"
                        style="display: none;">
                        <div>
                            <h3>
                                <spring:message
                                    code="help.page.contactus.success.successmessage" />
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
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
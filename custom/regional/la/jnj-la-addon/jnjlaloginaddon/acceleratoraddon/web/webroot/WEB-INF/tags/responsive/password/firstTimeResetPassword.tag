<%@ taglib prefix="message" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form id="laProfileChangePassword" method="post" action="javascript:;">

    <h4 class="headingTxt content">
        <c:choose>
            <c:when test="${passwordExpired eq 'true'}">
                <spring:message code="password.forgotPassword.passwordExpired" />
            </c:when>
            <c:otherwise>
                <spring:message code="password.forgotPassword.resetPassword" />
            </c:otherwise>
        </c:choose>
    </h4>
    <div  class="col-lg-12 col-md-12 boxshadow whiteBg" id='recoverpasswordP1' style="padding:20px;">
        <div class="success reset"  style='display:none'>
            <p class="correctAnswer mar0">
                <spring:message code="password.forgotPassword.correctAnswers" />
            </p>
        </div>
        <div class="row">
            <div class="col-sm-12" id="profilepage">
                <p>
                    <c:if test="${passwordExpired eq 'true'}">
                        <spring:message code="password.expired.text"/>
                    </c:if>
                </p>
                <div class="row">
                <div class="col-sm-3">
                    <div>
                        <label for="newPassword">
                            <spring:message code="password.forgotPassword.newPassword" />
                            <sup>
                                <spring:message code="password.forgotPassword.star"/>
                            </sup>
                        </label>
                    </div>
                    <div class='lapwdresetwidgetdiv margintop5' id='laResetPwd' style="width:150px"></div>
                    <input type="hidden" value="${param.email}" id="useremail" />
                    <noscript>
                        <div>
                            <input type="password"  class="form-control newPasswordVal" id="newPassword"
                                   name="newPassword" data-toggle="tooltip" data-placement="right"
                                   data-msg-required='<spring:message code="password.forgotPassword.enterPassword" />'
                                   data-msg-complexity='<spring:message code="profile.password.error"/>'  />
                        </div>
                    </noscript>
                </div>
                <div class="col-sm-3">
                    <spring:message code="profile.changePassword.strength"/>
                </div>
                </div>
                <div class="col-sm-12">
                    <div>
                        <label class="margintop20px">
                            <spring:message code="password.forgotPassword.retypePassword" />
                            <sup class="star">
                                <spring:message code="password.forgotPassword.star"/>
                            </sup>
                        </label>
                    </div>
                    <div>
                        <input type="password"  style="width: 225px" class="form-control pwdWidth" id="checkNewPassword" name="checkNewPassword"
                               data-msg-required='<spring:message code="password.forgotPassword.passwordMismatchError"/>' />
                    </div>
                </div>
                <div class="recPassError"></div>
            </div>
            <input id="complexityInvalid" type="hidden" value='<spring:message code="profile.password.error"/>' />
        </div>
        <br>
        <div class="col-sm-12" >
            <label for="checkAcknowledgement">
                <input type="checkbox"
                       onclick="validateSaveButton(this, 'resetPassword')"
                       id="checkAcknowledgement">
                <spring:message code="profile.page.acknowledgement.bottom" />
            </label>
        </div>
        <div class="modal-footer ftrcls formmargin resetPasswordModalFooter">
            <input class="secondarybtn emailvalidate btn btnclsactive text-uppercase pull-right no-border"
                   id="resetPassword" value='<spring:message code="password.forgotPassword.resetPassword" />' type="submit" />
        </div>
    </div>

</form:form>
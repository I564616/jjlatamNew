<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<template:loginPage pageTitle="${pageTitle}">
<body onload="checkCookie()">



<div class="container" id="loginContainer">
<div class="main row">
<div class="row formContent ">
<div class="jnjlogo">
<cms:pageSlot position="LoginLogo" var="logo" limit="1">
				<cms:component component="${logo}" />
</cms:pageSlot>
</div>
  <p class="displayinlineblk loginTxt"><spring:message code="login.header" /></p>
    <a type="button" class="btn pull-right displayinlineblk signUp " href="#" ><spring:message code="login.user.profile.signup" /></a>
  <p class="displayinlineblk pull-right newLink"><spring:message code="login.user.new" /></p>
  <c:url value="/j_spring_security_check" var="loginActionUrl"/>
  <form:form role="form" method="post" action="${loginActionUrl}" id="loginForm" name="loginForm">
    <div class="form-group">
      <label for="email"><spring:message code="login.user.login"/></label>
        <input type="text" data-msg-required="Username or password incorrect." class="login_img form-control required"  id="j_username" name="j_username" />
    </div>
    <div class="form-group">
      <label for="pwd"><spring:message code="login.user.password"/></label>
      <a href="javascript:;" class="password-forgotten pull-right"><spring:message code="login.user.forgot"/></a>
<input type="password" data-msg-required="Username or password incorrect." class="pass_img form-control required" id="j_password" name="j_password" />
    </div>
     <div class="recPassError"></div>
    <c:if test="${!empty loginError}">
							<div class="registerError" style="color: red"> <!-- Style Added by Vijay-->
								<c:choose>
									<c:when test="${attemptsExceeded ne null}">
										<label for="state" class="error">
											<spring:message code="login.attempts.exceeded" />
										</label>
									</c:when>
									<c:otherwise>
										<label for="state" class="error">
											<spring:message code="${loginError}" />
										</label>
									</c:otherwise>
								</c:choose>
							</div>
	</c:if>
	  <button type="submit" class="btn-md  pull-right submitBtn primarybtn " id="loginButton"><spring:message code="login.header"/></button>
	<div class="checkbox displayinlineblk rememberMe pull-right checkbox-info ">
     
		<input id="saveuserChkBox" class="styled" type="checkbox">
		
		<label for="saveuserChkBox ">
			<spring:message code="login.user.save"/>
		</label>
													
    </div>
  </form:form>
  <div class="privacyLinks pull-right"> 
  
 <!--  <a href="javascript:void(0)" class="privacypolicypopup_hn">Contact Us |</a>
  <a href="javascript:void(0)" class="privacypolicypopup_hn">Privacy Policy |</a>
  <a href="javascript:void(0)" class="legalnoticepopup_hn">Legal Notice |</a>
  <a href="javascript:void(0)" class="termconditionpopup_hn">Terms and conditions</a>  --> 
  
  <a type="button" class="" data-toggle="modal" data-target="#contactuspopup" href="javascript:void(0)">Contact Us |</a>
  <a type="button" class="" data-toggle="modal" data-target="#privacypopup" href="javascript:void(0)">Privacy Policy |</a>
  <a type="button" class="" data-toggle="modal" data-target="#legalnoticepopup" href="javascript:void(0)">Legal Notice |</a>
  <a type="button" class="" data-toggle="modal" data-target="#tncpopup" href="javascript:void(0)">Terms & Conditions</a>
  
  </div>
</div>
	
	<div class="row redBgcolor">
		<div class="col-xs-9">
			<p class="tourText"><spring:message code="login.tour.header"/></p>
					<p class="tourDesc"><spring:message code="login.tour.header.content"/></p>
			</div>
			<div class="col-xs-3 videoIcon">
				<img src="${webroot}/_ui/responsive/common/images/play_btn.png" class="img-circle " alt="videoplay" width="50" height="50" data-toggle="modal" data-target="#video-popup">
				<!-- Trigger the modal with a button -->
			</div>
		</div>
	</div>
</div>
<!-- start - video popup -->
	<!-- Modal -->
		<div class="modal fade jnj-popup" id="video-popup" role="dialog">
			<div class="modal-dialog modalcls modal-lg">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
					  <h4 class="modal-title">Take a tour</h4>
					</div>
					<div class="modal-body">
						<iframe id="cartoonVideo" width="100%" height="500" src="https://www.youtube.com/embed/GXWrDSCl1Jw" frameborder="0" allowfullscreen></iframe>	
					</div>
				</div>
			</div>
		</div>
		<!-- Modal contactus -->
		<div class="modal fade jnj-popup" id="contactuspopup" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
					  <h4 class="modal-title">Contact Us</h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
								<div class="">
									<div>All customers can send us a message using the form below</div>
									<form>
									  <div class="form-group login-margintop10">
										<label for="email">From:<sup>*</sup></label>
										<input type="email" class="form-control" id="email">
									  </div>
									  <div class="form-group">
										<label for="pwd">Email:<sup>*</sup></label>
										<input type="password" class="form-control" id="pwd">
									  </div>
										  <div><label for="pwd">Subject:<sup>*</sup></label></div>
										  <select class="selectpicker" value="">
											  <option>Select a Subject</option>
											  <option>Option 1</option>
											  <option>Option 2</option>
											  <option>Option 3</option>
										</select>
									</form>
								</div>
							</div>
							<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
								<div class="">
									<div>All customers can send us a message using the form below</div>
									
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 login-margintop10">
								<div class="form-group">
									  <label for="comment">Message:</label>
									  <textarea class="form-control" rows="5" id="comment"></textarea>
								</div>
								<p>All customers can send us a message using the form below. All customers can send us a message using the form below. All customers can send us a message using the form below. All customers can send us a message using the form below. All customers can send us a message using the form below. All customers can send us a message using the form below.</p>
								
							</div>							
						</div>
					</div>
					<div class="modal-footer">						
							<button type="submit" class="btnclsactive contactUsSubmitBtn pull-right">SEND MESSAGE</button>						
					</div>
				</div>
			</div>
		</div>
	<!--End of Modal pop-up-->
	<!-- Modal privacypopup -->
		<div class="modal fade jnj-popup" id="privacypopup" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
					  <h4 class="modal-title">Privacy Policy</h4>
					</div>
					<div class="modal-body">
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p> You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
					</div>
				</div>
			</div>
		</div>
	<!--End of Modal privacypopup pop-up-->
	<!-- Modal legalnoticepopup -->
		<div class="modal fade jnj-popup" id="legalnoticepopup" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
					  <h4 class="modal-title">Legal Notice</h4>
					</div>
					<div class="modal-body">
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p> You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
					</div>
				</div>
			</div>
		</div>
	<!--End of Modal legalnoticepopup pop-up-->
	<!-- Modal tncpopup -->
		<div class="modal fade jnj-popup" id="tncpopup" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
					  <h4 class="modal-title">Terms & Conditions</h4>
					</div>
					<div class="modal-body">
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p> You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						<p class="strongtextpopup">Conditions of Site Use </p>
						<p>You can use all Bootstrap plugins purely through the markup API without writing a single line of JavaScript. This is Bootstrap's first-class API and should be your first consideration when using a plugin</p>
						
						
					</div>
				</div>
			</div>
		</div>
	<!--End of Modal pop-up-->
<!-- end - video popup -->
	<!--End of Modal pop-up-->
<!-- end - video popup -->
 <input type="hidden" id="passwordExpiryFlag" value="${passwordExpired}">
		<input type="hidden" id="passwordExpireToken" value="${passwordExpireToken}">
		<input type="hidden" id="helpFlag" value="${helpFlag}">
		<input type="hidden" id="email" value="${email}">
		</body>
		 <script type="text/javascript" src="${commonResourcePath}/js/loginaddon.js"></script> 
</template:loginPage>
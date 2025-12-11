<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- iPad Popup Start -->
<div>
	<div class="iPadSign" id="signaturePad">
		<form:form action="#" name="signatureForm" method="post">
		    <h1>
		    	<spring:message code='tablet.signature.header' />
			</h1>
		   <div class="iPadContent"> 
		    <div class="formInfoContainer">
		        <ul>
		            <li>
	            		<input type="checkbox" class="required" name="signatureTerms" data-msg-required="<spring:message code='tablet.signature.terms.error' />"/>
	            		<spring:message code='tablet.signature.agreement' />
	            		<a href="#" class="termconditionpopup_hn">
	            			<spring:message code='tablet.signature.terms' />
	            		</a>
	            		<div class="registerErrorTablet"></div>
	            	</li>
		        </ul>
		    </div>
		    <div class="formInputContainer">
		    	<div class="size2of5 cell">
		            <label for="fname"><spring:message code='tablet.signature.first.name' /></label>
		            <input id="fname" name="tabletFName" class="required" type="text" data-msg-required="<spring:message code='tablet.signature.first.name.error' />" />
		            <div class="registerErrorTablet"></div>
		        </div>
		        <div class="size2of5 cell">
		            <label for="lname"><spring:message code='tablet.signature.last.name' /></label>
		            <input id="lname" name="tabletLName" class="required" type="text" data-msg-required="<spring:message code='tablet.signature.last.name.error' />" />
		            <div class="registerErrorTablet"></div>
		        </div>
		    </div>
		    <div class="signContainer">
		    <label for="createSignature"><spring:message code='tablet.signature.signature' /></label>
		    <div id="createSignature"></div>
		    <div id="canvasError" class="registerErrorTablet marTop20" style="display: none;">
		    	<label class="error"><spring:message code='tablet.signature.signature.error' /></label>
		    </div>
		    </div>
		   </div>
		    <div class="buttonWrapper sectionBlock">
		        <button type="button" class="tertiarybtn" onclick="parent.$.colorbox.close(); return false;">
		        	<spring:message code='tablet.signature.cancel' />
		        </button>
		    	<button type="button" id="ClearSignature" class="secondarybtn" onclick="$('#createSignature').jSignature('clear')">
		    		<spring:message code='tablet.signature.clear' />
		    	</button>
		        <button type="button" id="submitSignature" class="primarybtn">
		        	<spring:message code='tablet.signature.submit' />
		        </button>
		    </div>
    	</form:form>
	</div>
	<div style="display:none;" id="blankCanvasContainer" >
	</div>
</div>
<!-- iPad Popup End -->
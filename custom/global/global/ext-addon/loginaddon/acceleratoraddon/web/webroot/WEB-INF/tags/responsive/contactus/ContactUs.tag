<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2><spring:message code="help.page.contactus.label"/></h2>
                <div class="contactUsFormBody">
                <c:url value="/help/sendEmail" var="sendEmail" ></c:url>
                <form:form id="contactUsForm" action="${sendEmail}" method="POST" commandName="contactUsForm" name="contactUsForm" class="contactUsForm">
                <div class="contactUsFormPage1">
                <div class="contactUsForm" novalidate="novalidate">
                	<ul>
                		<li>
		                	<div class="size1of1 cell">
								<div class="cell span-8">
									<div class="contactUsGlobalError registerError">
										<label class="error" style="display:none;"><spring:message code="help.page.contactus.reqfields"/><br /></label>
									</div>
								</div>
							</div>
						</li>
                        <li>
                            <div class="size1of1 cell">
                                <span><label for="contactUsFromName"><spring:message code="help.page.contactus.from"/>:</label><br>
                                <form:input path="contactUsFromName" class="required disabled" value="${nameOfCustomer}" id="contactUsFromName" name="contactUsFromName" readonly="true" disabled="true"/>
                                </span>
                                <div class="cell"><div class="registerError"></div></div>
                            </div>
                        </li>
                        <li>
                            <div class="size1of1 cell">
                                <span><label for="contactUsEmail"><spring:message code="help.page.contactus.email"/>:</label><br>
                                <form:input path="contactUsEmail" class="required disabled" value="${emailOfCustomer}" id="contactUsEmail" name="contactUsEmail"  readonly="true" disabled="true"/>
                                </span>
                                <div class="cell"><div class="registerError"></div></div>
                            </div>
                        </li>
                        <li>
                            <div class="size1of1 cell">
                                <span><label for="contactUsSubject" class="getErrorMessage" data="<spring:message code='help.page.subject.error'/>"><spring:message code="help.page.contactus.subject"/>:<span class="redStar">*</span></label><br>
								<form:select path="contactUsSubject" class="contactUsSubject required" id="contactUsSubject" name="contactUsSubject" data-msg-required="">
                                   <form:option value=""><spring:message code="help.page.issue.selectanyone"/></form:option>
                                    <c:forEach items="${subjectDropDown}" var="subjectMap">
                                    	<form:option value="${subjectMap.key}"><spring:message code="${subjectMap.value}"/></form:option>
                                    </c:forEach>
                                </form:select></span>
                                <div class="cell"><div class="registerError"></div></div>
                            </div>  
                        </li>
                        <li style="display: none;">
                            <div class="size1of1 cell">
                               <span><label for="contactUsOrderNumber"  class="getErrorMessage" data="<spring:message code='help.page.order.error'/>"><spring:message code="help.page.contactus.ordernumber"/>:<span class="redStar">*</span></label><br>
                                <form:input path="contactUsOrderNumber" data-msg-required="" class="required" id="contactUsOrderNumber" name="contactUsOrderNumber"/>
                                </span> 
                                <div class="cell"><div class="registerError"></div></div>
                            </div>
                        </li>
                    </ul>       
                <div class="contactUsMessageBox">
                	<ul>
                		<li style="display: block;">
		                	<div class="cell">
		                    <span><label for="contactUsMessageBox"  class="getErrorMessage" data="<spring:message code='help.page.message.error'/>"><spring:message code="help.page.contactus.message"/><span class="redStar">*</span></label><br>
		                    <form:textarea path="contactUsMessage" cols="50" rows="50" id="contactUsMessageBox" class="required" name="contactUsMessage" data-msg-required=""/>
		                    </span>
		                    </div>
		                    <div class="cell"><div class="registerError"></div></div>
                   		</li>
                    </ul>
                </div>
                <p><cms:pageSlot position="ContactUs" var="comp" >
                       		<cms:component component="${comp}" />
	                	</cms:pageSlot></p>
                <span>
                	<input type="button" class="primarybtn contactUsFormSubmit" value='<spring:message code="help.page.contactus.sendbutton"/>'/>
                </span>
                </div>
                </div>
            </form:form>
            <div class="contactUsFormPage2">
                <div>
                    <h3><spring:message code="help.page.contactus.success.successmessage"/></h3>
                    <p><spring:message code="help.page.contactus.success.thankyoumessage"/></p>
                    <p><spring:message code="help.page.contactus.success.responsemessage"/></p>
                    <a class="backToContactUsForm" href="#"><spring:message code="help.page.contactus.success.anothermessage"/></a>
                </div>
            </div>
           </div>
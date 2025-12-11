<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Information (confirmation) messages --%>

<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<c:if test="${not empty accConfMsgs}">	
		<c:forEach items="${accConfMsgs}" var="msg">
			<div class="panel-group">
				<div class="panel panel-success">
					<div class="panel-heading">				
						<span class="glyphicon glyphicon-ok" style="margin-right:5px"></span> <spring:message code="${msg.code}"/>
					</div>
					</div>
			</div>		
		</c:forEach>
</c:if>

<%-- Warning messages --%>
<c:if test="${not empty accInfoMsgs}">	
		<c:forEach items="${accInfoMsgs}" var="msg">
			<div class="info">				
				<div><spring:message code="${msg.code}"/></div>
			</div>
		</c:forEach>	
</c:if>

<%-- Error messages (includes spring validation messages)--%>
<c:if test="${not empty accErrorMsgs}">
	<div class="error">	
		<c:forEach items="${accErrorMsgs}" var="msg">
				<p><spring:message code="${msg.code}" arguments="${msg.attributes}"/></p>
		</c:forEach>
	</div>
</c:if>
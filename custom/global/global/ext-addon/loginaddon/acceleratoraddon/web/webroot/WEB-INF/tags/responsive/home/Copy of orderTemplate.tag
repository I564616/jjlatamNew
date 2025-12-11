<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/responsive/home"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="productBox">
	<h3>Add from a Template</h3>
	<div class="productCode">
		<c:url value="/home/viewTemplate" var="viewTemplateHome" />
		<form:form id="homePageForm" action="${viewTemplateHome}"
			method="POST" commandName="homePageForm">
			<div>
				<label for="selTemplate" class="col"><spring:message code="homePage.orderTemplate.addcontents"/>
				</label>
			</div>
			<div>
				<c:choose>
					<c:when test="${not empty orderTemplates}">
						<form:select class="marBott20" path="template" id="selTemplate">
							<c:forEach var="orderTemplate" items="${orderTemplates}">
								<option value="${orderTemplate.key}">${orderTemplate.value}</option>
							</c:forEach>
						</form:select>
					</c:when>
					<c:otherwise>
						<form:select class="marBott20" path="template" id="selTemplate" disabled="disabled">
							<option value="0">No templates available</option>
						</form:select>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not empty orderTemplates}">
						<p class="marBott10">
							<c:url value="/templates" var="viewAllTemplates" />
							<a href="${viewAllTemplates}"><spring:message code="homePage.orderTemplate.viewdetails"/></a>
						</p>
					</c:when>
					<c:otherwise>
						<div class="info">
							<p style="color:#646464;">
								<spring:message code="homePage.orderTemplate.viewdetails.empty"/>
							</p>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<c:choose>
				<c:when test="${not empty orderTemplates}">
					<div>
						<input type="submit" value="View" class="secondarybtn left">
						<input type="button" id="addToCartForm_3" value="Add to Cart"
							class="primarybtn right">
					</div>
					<div class="showError">
						<input id="errorTemplateCart" class="tertiarybtn homeCartErrors hide" type="button"
							value='<spring:message code="homePage.errorDetails"/>' />
					</div>
				</c:when>
				<c:otherwise>
					<div>
						<input type="submit" value="View" class="tertiarybtn left" disabled="disabled" />
						<input type="button" id="addToCartForm_3" value="Add to Cart" class="tertiarybtn right" />
					</div>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>
</div>

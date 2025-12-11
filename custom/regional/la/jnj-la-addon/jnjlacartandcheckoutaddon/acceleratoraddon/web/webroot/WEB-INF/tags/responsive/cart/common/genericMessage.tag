<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="contractNumber" required="false" type="java.lang.String"%>
<%@ attribute name="catalogId" required="false" type="java.lang.String"%>
<%@ attribute name="messageCode" required="false" type="java.lang.String"%>
<%@ attribute name="icon" required="false" type="java.lang.String"%>
<%@ attribute name="panelClass" required="false" type="java.lang.String"%>
<%@ attribute name="messagearguments" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="panel-group hidden-xs hidden-sm"
	style="margin: 5px 0 20px 0;">
	<div class="panel panel-${panelClass}">
		<div class="panel-heading">
			<h6 class="panel-title">
				<span>  <c:choose>
                            <c:when test="${panelClass eq 'danger'}">
                                 <i class="bi bi-slash-circle"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-check-lg" style="-webkit-text-stroke: 2px;" ></i>
                            </c:otherwise>
                        </c:choose>&nbsp;
					<strong>${catalogId}</strong> <spring:message code="${messageCode}"  arguments="${messagearguments}"/> <strong>${contractNumber}</strong>
				</span>
			</h4>
		</div>
	</div>
</div>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageSize" required="true" type="java.lang.String"%>
<%@ attribute name="totalResults" required="true"
	type="java.lang.String"%>
<%@ attribute name="selectedShowGroup" required="false"
	type="java.lang.String"%>
<%@ attribute name="onClickClass" required="true"
	type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>


<div class="showMoreResult clear ajaxLoad">
	<a href="#" class="loadMore ${onClickClass}" style=""><spring:message code='pagination.show'/>&nbsp;${(selectedShowGroup eq null) ? pageSize:selectedShowGroup} 
			&nbsp;<spring:message code='pagination.more'/></a><span class="fontSmall">
		(showing ${pageSize} out of ${totalResults})</span>
</div>

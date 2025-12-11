<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="mainbody-container">
	<c:choose>
		<c:when test="${splitCart}">
			<c:forEach items="${jnjCartDataList}" var="jnjCartData"
				varStatus="count">
				<standardCartLa:multiReviewEntries jnjCartData="${jnjCartData}"
					globalCount="${count.count}" />
			</c:forEach>
			<div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
				<div class="txtRight">
					<standardCartLa:cartTotals />
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<standardCartLa:singleReviewEntry />
		</c:otherwise>
	</c:choose>
</div>
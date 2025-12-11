<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false"
	type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>

<!--Order Derail Row starts-->

            <!--Order Derail Row starts123@-->
	         	<c:choose>
					<c:when test="${splitCart}">
						<c:forEach items="${jnjCartDataList}" var="jnjCartData" varStatus="count">
							<standardCart:multiCartsEntries jnjCartData="${jnjCartData}" globalCount="${count.count}"/>
						</c:forEach>
						<div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
							<div class="txtRight">
								<standardCart:cartTotals/>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<standardCart:singleCartEntry  globalCount="1" /> 

					</c:otherwise>
				</c:choose>
            <!--Order Derail Row ends-->
           
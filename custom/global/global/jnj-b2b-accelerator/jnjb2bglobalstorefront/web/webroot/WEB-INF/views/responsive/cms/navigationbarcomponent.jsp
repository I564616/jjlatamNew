<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${ycommerce:evaluateRestrictions(component.link)}">
	<c:set value="${component.styleClass} ${dropDownLayout}" var="bannerClasses"/>
	<li class="La ${bannerClasses} <c:if test="${not empty component.navigationNode.children}"> parent</c:if>">
		<c:choose>
			<c:when test="${not empty component.navigationNode.children}">
				<div class="mainMenuReport">
					<i class="fa ${component.uid}" aria-hidden="true"></i>
					<cms:component component="${component.link}" />
				</div>
			</c:when>
			<c:otherwise>
				<i class="fa ${component.uid}" aria-hidden="true"></i>
				<cms:component component="${component.link}" />
			</c:otherwise>
		</c:choose>
		<c:if test="${not empty component.navigationNode.children}">
		<div class="clearBoth">
			<ul class="Lb" style="list-style-type:none;padding-left: 0px;">
				<c:forEach items="${component.navigationNode.children}" var="child">
					<c:if test="${child.visible}">
						<li class="Lb" >
							<span class="nav-submenu-title">${child.title}</span>
							<c:forEach items="${child.links}" step="${component.wrapAfter}" varStatus="i">
								<ul class="Lc subReportsMenu subReportsMenuName ${i.count < 2 ? 'left_col' : 'right_col'}">
									<c:forEach items="${child.links}" var="childlink" begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
										<cms:component component="${childlink}" evaluateRestriction="true" element="li" class="Lc sub-menu-item ${i.count < 2 ? 'left_col' : 'right_col'}"/>
									</c:forEach>
								</ul>
							</c:forEach>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
		</c:if>
	</li>
</c:if>
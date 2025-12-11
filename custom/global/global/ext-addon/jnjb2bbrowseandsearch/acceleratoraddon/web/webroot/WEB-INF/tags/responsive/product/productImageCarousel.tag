<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%@ attribute name="galleryImages" required="true" type="java.util.List" %>


<div class="scroller">
	<ul id="carousel_alternate" class="jcarousel-skin alt">
		<li>
		<c:forEach items="${galleryImages}" var="container" varStatus="varStatus">			
				<span class="thumb">
					<a href="javascript:;">
						<img src="${container.thumbnail.url}" data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${product.name}" />
					</a>						
				</span>			
		</c:forEach>
	</li>
	</ul>
</div>
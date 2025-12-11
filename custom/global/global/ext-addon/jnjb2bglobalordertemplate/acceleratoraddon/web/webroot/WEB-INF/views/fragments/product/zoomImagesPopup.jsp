<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>  

<div class="zoom_lightbox resizeableColorbox">
	<div class="span-4">
		<c:if test="${fn:length(galleryImages) gt 0}">
			<div class="scroller modal">
				<div id="thumbs">
					<ul id="carousel_modal" class="jcarousel-skin modal alt galleriffic_slider thumbs noscript">
						<c:forEach items="${galleryImages}" var="container">
							<li>
								<span class="thumb">
									<a href="#" class="noaction">
										<img src="${container.thumbnail.url}" id="${container.zoom.url}" alt="${product.name}" title="${product.name}">
									</a>
								</span>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>
	</div>
	
	<c:if test="${empty zoomImageUrl}">
		<c:set value="${ycommerce:productImage(product, 'zoom').url}" var="zoomImageUrl"/>
	</c:if>
	<c:if test="${not empty requestParams['mediaUrl'][0]}">
		<c:forEach items="${galleryImages}" var="container">
			<c:if test="${container.product.url eq requestParams['mediaUrl'][0]}">
				<c:set var="zoomImageUrl">${container.zoom.url}</c:set>
			</c:if>
		</c:forEach>
	</c:if>
	
	<div class="xl_image span-16 last">
		<c:if test="${not empty zoomImageUrl}">
			<img src="${zoomImageUrl}" id="xl_image_a" alt="${product.name}" title="${product.name}"/>
		</c:if>
	</div>

</div>

<script type="text/javascript">
	ACC.product.zoomImage()
</script>

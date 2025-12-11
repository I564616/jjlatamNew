<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:url value="p/${product.code}/zoomImages?galleryPosition=POSITION" var="zoomImageUrlTemplate"/>



<script  type="text/javascript">
/*<![CDATA[*/

function initWriteReviewAction() {
	$('#write_review_action_main').click(function(e){
		e.preventDefault();
		$.scrollTo('#prod_tabs', 300, {axis: 'y'});
		$('#reviews').hide();
		$('#write_reviews').show();
		$( "#prod_tabs" ).tabs( "option", "selected", $('#tab_strip').children().size() - 1 );
		$('#reviewForm input[name=headline]').focus();
	});
}

function initBasedOnReviewsAction() {
	$('#based_on_reviews').click( function(e) {
		e.preventDefault();
		$.scrollTo('#prod_tabs', 300, {axis: 'y'});
		$( "#prod_tabs" ).tabs( "option", "selected", $('#tab_strip').children().size() - 1 );
		$('#write_reviews').hide();
		$('#reviews').show();
		$('#read_reviews_action').click();
		
	});
}

function initPageEvents() {
	$(".modal").colorbox();
	$("#carousel_alternate img").click(function() {
		$("#primary_image img").attr("src", $(this).attr("data-primaryimagesrc"));
		$("#zoomLink").attr("href", "${zoomImageUrlTemplate}".replace("POSITION", $(this).attr("data-galleryposition")));
		$("#imageLink").attr("href", "${zoomImageUrlTemplate}".replace("POSITION", $(this).attr("data-galleryposition")));
	});

	initWriteReviewAction();
	initBasedOnReviewsAction();


	$("#Size").change(function () {
		var url = "";
		var selectedIndex = 0;
		$("#Size option:selected").each(function () {
			url = $(this).attr('value');
			selectedIndex = $(this).attr("index");
		});
		if (selectedIndex != 0) {
			window.location.href=url;
		}
	});

	$("#variant").change(function () {
		var url = "";
		var selectedIndex = 0;
		
		$("#variant option:selected").each(function () {
			url = $(this).attr('value');
			selectedIndex = $(this).attr("index");
		});
		if (selectedIndex != 0) {
			window.location.href=url;
		}
	});
}

$(document).ready(function() {
	
	initPageEvents();
	
	setTimeout(function() {
		if($.query.get('tab') == 'writereview') {
			$('#write_review_action_main').click();
		}
		if($.query.get('tab') == 'readreviews') {
			$('#based_on_reviews').click();
		}
	}, 100);
});

/*]]>*/
</script>
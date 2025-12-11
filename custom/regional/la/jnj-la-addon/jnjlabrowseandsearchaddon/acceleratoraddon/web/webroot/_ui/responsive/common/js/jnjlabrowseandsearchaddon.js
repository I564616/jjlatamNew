   $(window).load(function(){
		$("#global-search-txt").attr("autocomplete", "on");
		$( "#global-search-txt" ).autocomplete({
			source: function( request, response ) {
             ACC.autocompleteUrl = ACC.config.contextPath + '/search/autocomplete';
				$.getJSON(
						ACC.autocompleteUrl,
						{
							term : $('#global-search-txt').val()
						},
						function(data) {
							response(data);
						}
					);
			},
			minLength: 3,
			open: function(event, ui) { $(".ui-menu").css("z-index", 10000); },
			close: function(event, ui) { $(".ui-menu").css("z-index", -1); },
			select: function(event, ui) {
				if(ui.item) {
					$('#global-search-txt').val(ui.item.value.trim());
				}
				document.forms['search_form'].submit();
			},
			autoFocus: false

		});
});

$(".showLink").mouseover(function(event) {
    var elementClass ='#'+ $(this).attr('id');
    var elementId ='.'+ $(this).attr('id');

    var left = $(elementClass).position().left - 40;
    var top = $(elementClass).position().top + 40;
    $(elementId).css({top: top,left: left}).show();
});
$(".showLink").mouseout(function() {
     var elementId ='.'+ $(this).attr('id');
     $(elementId).hide();
});

window.onload = function() {
    if (window.jQuery) {
    // jQuery is loaded
        $(window).ready(function(){
            $("#desc1").hide();
            $("#desc3").hide();
            $("#tab_2").addClass("activeTab");
        });
        ($(".page-register").css('overflow-y') === 'auto') ? $('.page-register').css({ 'overflow-y' : ''}) : '';
        ($(".page-helpPage").css('overflow-y') === 'auto') ? $('.page-helpPage').css({ 'overflow-y' : ''}) : '';
        $("#tab_1").click(function(){
            console.log("tab 1");
            $("#desc1").show();
            $("#desc2").hide();
            $("#desc3").hide();
            $("#tab_1").addClass("activeTab");
            $("#tab_2").removeClass("activeTab");
            $("#tab_3").removeClass("activeTab");
        });
        $("#tab_2").click(function(){
            console.log("tab 2");
            $("#desc1").hide();
            $("#desc2").show();
            $("#desc3").hide();
            $("#tab_2").addClass("activeTab");
            $("#tab_1").removeClass("activeTab");
            $("#tab_3").removeClass("activeTab");
        });
        $("#tab_3").click(function(){
            console.log("tab 3");
            $("#desc1").hide();
            $("#desc2").hide();
            $("#desc3").show();
            $("#tab_3").addClass("activeTab");
            $("#tab_2").removeClass("activeTab");
            $("#tab_1").removeClass("activeTab");
        });
    } else {
        console.log("jquery not loaded");
    }
};
//script for qty
   $(window).ready(function(){
   var plpPageSize="#plpPageSize";
   var plpPageNumber="#plpPageNumber";
   var searchSortForm="#searchSortForm";
       $('#decrement-qty').click(function(){
           var qty = $('.ProdTbox').val();
           if(qty > 0){
               qty--;
               $(".ProdTbox").val(qty);
           }
       });
       $('#increment-qty').click(function(){
           console.log("inccc");
           var qty = $('.ProdTbox').val();
           qty++;
           console.log(qty);
           $(".ProdTbox").val(qty);
       });

        var itemsPerPage = parseInt($(plpPageSize).val());
        var pageNumber=0, lastPageNumber=0;
        pageNumber = parseInt($(plpPageNumber).val());
        lastPageNumber = parseInt($("#plpLastPageNumber").val());

        if(pageNumber === 0) {
        $("#custom-pg-btn_previous").addClass("disabled");

        }
        if(pageNumber === lastPageNumber-1) {
           $("#custom-pg-btn_next").addClass("disabled");
        }
        $(".bg-white-pad #lineStatus").val($(plpPageSize).val());



        $(".bg-white-pad #lineStatus").on('change', function(){
            itemsPerPage = $(this).val();
            $(plpPageSize).val(itemsPerPage);
            $(plpPageNumber).val(0);
            $(searchSortForm).submit();
        });

        $("#custom-pg-btn_next").click(function(e){
            if(pageNumber<lastPageNumber-1){
                pageNumber++;
            }
            $(plpPageNumber).val(pageNumber);
            if($(this).hasClass("disabled")){
                return;
            } else {
                $(searchSortForm).submit();
            }
        });
        $("#custom-pg-btn_previous").click(function(e){
            if(pageNumber>0) {
                pageNumber = pageNumber-1;
            }
            $(plpPageNumber).val(pageNumber);
            if($(this).hasClass("disabled")){
                return;
            } else {
                $(searchSortForm).submit();
            }
        });
   });
var imgSrc = 'img src', aHref = 'a href', priceRule = 'priceRule', span = 'span', classText = 'class', divTag = 'div';
var CUSTOMER_ALSO_BOUGHT=$("#products-bought-together-title").val();
var ADD_TO_CART=$("#add-to-cart-text").val();
var desktopHtmlContent = '<div class="relatedProdSection hidden-xs" id="alsoBoughtCarousel"><div class="row">' +
    '<div class="col-lg-12 relatedProductLabel">'+CUSTOMER_ALSO_BOUGHT +'</div><div class="col-lg-11 col-md-11 col-sm-11 hidden-xs hrLine"></div>' +
    '<div class="col-lg-1 col-md-1 col-sm-1 hidden-xs swipePageHolder"><div class="swiper-pagination sp2"></div></div>'+
    '<div class="swiper-container s2 boxshadow">'+
    '<div class="swiper-button-next next-2">></div>'+
    '<div class="swiper-button-prev prev-2"><</div>'+
    '<div class="swiper-wrapper">';
var mobileHtmlContent = '<div class="relatedProdSection hidden-lg hidden-md hidden-sm" id="alsoBoughtCarousel"><div class="row">'+
    '<div class="col-lg-12 relatedProductLabel">'+CUSTOMER_ALSO_BOUGHT+'</div><div class="col-lg-12 col-xs-12 hrLine"></div>'+
    '<div class="swiper-container s4 boxshadow hidden-lg hidden-sm hidden-md">'+
    '<div class="swiper-wrapper">';
var carouselDisplayed = false;
var device;
var appendProductsToCarousel = function(html,deviceType,index,productDet) {
    html+= '<'+divTag+' '+classText+'="imgPlaceHolder">';
    var currProductURL = ACC.config.contextPath + productDet.productURL;
    var kitImageURL = "/store/_ui/responsive/common/images/kit-img.png";
    var productImageURL = productDet.imageURL;
    var unavailableImageURL = "/store/_ui/responsive/common/images/img-not-available.jpg";
    var productKit = productDet.kitType;
    var productCode = productDet.productCode;
    if (productImageURL){
        html+='<'+aHref+'="'+currProductURL+'"><'+imgSrc+'="'+productImageURL+'" '+classText+'="productdetail-img"></img></a>';
    } else if (productKit) {
        html+='<'+aHref+'="'+currProductURL+'"><'+imgSrc+'="'+kitImageURL+'" '+classText+'="productdetail-img"></img></a>';
    } else {
        html+='<'+aHref+'="'+currProductURL+'"><'+imgSrc+'="'+unavailableImageURL+'" '+classText+'="no-preview-img productdetail-img"></img></a>';
    }
    html+='</'+divTag+'><'+divTag+' '+classText+'="imgProductDescrp">'+
            '<p '+classText+'="productCode"><a href="'+currProductURL+'">'+productCode+'</a></p>'+
            '<p '+classText+'="prodName"><a href="'+currProductURL+'">'+productDet.productName+'</a></p></'+divTag+'><'+divTag+' '+classText+'="likeProductHold">';

    html+='<'+divTag+'><'+span+' '+classText+'="btn addToCartBtn addToCartLatamCarousel">'+ADD_TO_CART+'</'+span+'></'+divTag+'></'+divTag+'>';
    return html;
};
var buildSlidesForCarouselDesktop = function(slide,products,desktopHtmlContent, numberOfProductPerSlide){
    for (var carouselProduct=0; carouselProduct<numberOfProductPerSlide; carouselProduct++){
        var currIndex = carouselProduct+(slide*numberOfProductPerSlide);
        if(currIndex === products.length){
            break;
        }
        desktopHtmlContent +='<'+divTag+' '+classText+'="col-lg-3 productHolder">';
        device="Desktop";
        desktopHtmlContent = appendProductsToCarousel(desktopHtmlContent,device,currIndex,products[currIndex]);
        desktopHtmlContent += '</'+divTag+'>';
    }
    return desktopHtmlContent;
};
var buildCarousel = function(products, numberOfProductPerSlide) {
    var numberOfProducts = products.length;
    var slides = Math.ceil(numberOfProducts / numberOfProductPerSlide);
    for (var slide=0; slide<slides; slide++){
        desktopHtmlContent+= '<'+divTag+' '+classText+'="swiper-slide"><'+divTag+' '+classText+'="row productSwiperHolder">';
        desktopHtmlContent = buildSlidesForCarouselDesktop(slide,products,desktopHtmlContent, numberOfProductPerSlide);
        desktopHtmlContent += '</'+divTag+'></'+divTag+'>';
    }
    desktopHtmlContent+='</'+divTag+'></'+divTag+'></'+divTag+'></'+divTag+'>';
    for (var mobileSlide=0; mobileSlide<numberOfProducts; mobileSlide++){
        mobileHtmlContent += '<'+divTag+' '+classText+'="swiper-slide"><'+divTag+' '+classText+'="row productSwiperHolder productSwiperHolderMob">'+
        '<'+divTag+' '+classText+'="col-lg-3 productHolder productHolderMobile">';
        var mobileCurrIndex = mobileSlide;
        device="Mobile";
        mobileHtmlContent=appendProductsToCarousel(mobileHtmlContent,device,mobileCurrIndex, products[mobileCurrIndex]);
        mobileHtmlContent+='</'+divTag+'></'+divTag+'></'+divTag+'>';
    }
    mobileHtmlContent+='</'+divTag+'><'+divTag+' '+classText+'="swiper-button-next next-4">></'+divTag+'>'+
    '<'+divTag+' '+classText+'="swiper-button-prev prev-4"><</'+divTag+'></'+divTag+'></'+divTag+'></'+divTag+'>';
};
function customersWhoAlsoBoughtCarousel(){
    carouselDisplayed = false;
    var screenName = $("#screenName").val();
    var numberOfProductPerSlide = $("#numberOfProductPerSlide").val();
    if(!screenName || screenName !== "productDetailsPage"){
        return;
    }
    var carouselHideFlag=$("#cabCarouselHideFlag").val();
    if(carouselHideFlag && carouselHideFlag === "true"){
            return;
    }
    var productId = $("#product_baseMaterialNumber").val();
    var ajaxURL = ACC.config.contextPath + '/p/getTogetherBoughtProducts?productCode='+productId;
    //Commented out window.scroll because there can be lot of data on the pdp and height calculation can not work
    //$(window).scroll(function () {
        //var windowScrollHeight = $(window).height() + $(window).scrollTop();
        //var documentHeightBeforeFooter = $(document).height() - $("#jnj-footer").outerHeight();

        if (carouselDisplayed) {
            return;
        }

        if(!$("#alsoBoughtCarousel").is(":visible")){
            carouselDisplayed=true;
            $.ajax({
                type: "GET",
                url: ajaxURL,
                headers: {
                    'Content-Type': 'application/json'
                },
                success: function(data){
                    if (!$.trim(data)) {
                        return;
                    }
                    buildCarousel(data, numberOfProductPerSlide);
                    $(".scrollBox").html(desktopHtmlContent);
                    $(".scrollBoxMob").html(mobileHtmlContent);
                    var swiper2 = new Swiper('.s2', {
                        pagination: {
                            el:'.sp2',
                            clickable: true
                        },
                        paginationClickable: true,
                        navigation: {
                            nextEl: '.next-2',
                            prevEl: '.prev-2'
                        }
                    });
                    var mobile_swiper2 = new Swiper('.s4', {
                        navigation: {
                            nextEl: '.next-4',
                            prevEl: '.prev-4'
                        }
                    });
                    var pageHeight = $(document).height();
                    console.log(swiper2 +"-"+mobile_swiper2);
                    $("#jnj-usr-details-menu").css("height",pageHeight);
                    bindCarouselAddToCart();
                }
            });
        }
    //});
}
$(document).ready(customersWhoAlsoBoughtCarousel);
function bindCarouselAddToCart(){
    $(".addToCartLatamCarousel").click(function(){
        ACC.addToCart.submitProductCartFromCarousel($(this));
    });
}

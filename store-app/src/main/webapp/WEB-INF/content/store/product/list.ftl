<#import "../display.ftl" as display/>
<html>
<head>
    <title>Product-产品</title>
    <script src="${absoluteContextPath}/js/jquery/slider.js"></script>
    <script>
        $().ready(function(){
            $('#banners').pbTouchSlider({
                slider_Wrap: '#bannerParent',
                slider_Threshold: 50 ,
                slider_Speed:800 ,
                slider_Drag : false,
                slider_Ease:'linear',
                slider_Breakpoints: {
                    default: {
                        height: $(window).height() - 60
                    },
                    tablet: {
                        media: 1024
                    },
                    smartphone: {
                        height: 200,
                        media: 768
                    }
                }
            });
            $(window).scroll(function(){
                $(".he_border1").each(function() {
                   $(this).css("height", $(this).width() +"px");
                });
                var currentClassifyId;
                $("div[id^='classify_']").each(function(index, element){
                    if ($(this).isVisible()) {
                        currentClassifyId = $(element).attr("id");
                    }
                });
                if (!$("#bannerParent").isVisible()) {
                    $(".side_bar").removeClass("hide");
                    $(".side_bar a.side_bar_active").removeClass("side_bar_active");
                    $(".side_bar a[for='" + currentClassifyId + "']").addClass("side_bar_active");
                } else {
                    $(".side_bar").addClass("hide");
                }
            });
            function checkLi() {
                if ($("#banners .o-slider--item").attr("data-id")) {
                    var $bannerUl = $('#bannerParent .o-slider-pagination');
                    var $lastActive = $bannerUl.children("li.isActive");
                    if ($lastActive.index() == $bannerUl.children("li").length - 1) {
                        $bannerUl.children("li:first").click();
                    } else {
                        $lastActive.next().click();
                    }
                }
                setTimeout(checkLi, 5000);
            }
            setTimeout(checkLi, 5000);
        });

        function scrollToDiv(id) {
            $('body').animate({
                scrollTop: $("#" + id).offset().top - 80
            }, 300);
        }

        $.fn.isVisible = function() {
            var currentTop = $(window).scrollTop();
            var eleTop = $(this).offset().top;
            return !((currentTop > (eleTop + $(this).outerHeight()))
            || ((currentTop + $(window).height())< eleTop));
        }
    </script>
</head>
<body>
<div id="productList">
    <@display.banner banners!/>
</div>
<div class="main">
    <section class="dzen_section_DD product" style="padding-top: 30px;padding-bottom: 0px;">
            <#list classifies as classify>
                <#if classify.products?? && classify.products?size gt 0>
                <div style="width: 100%;" class="<#if classify_index % 2 != 0>products-even</#if>">
                <div class="dzen_container ">
                    <div id="classify_${classify.id?c}" class="cur " style="padding-top: 30px;">
                        <header>
                            <div class="dzen_container">
                                <h4>${classify.name!""}</h4>
                            </div>
                        </header>
                        <div class="dzen_column_DD_span12">
                            <#list classify.products as product>
                                <div class="dzen_column_DD_span3 news-cont">
                                    <div class="dzen_column_DD_span12 news-img he_border1">
                                        <a class="thumb-big">
                                            <img class="he_border1_img" src="<@display.pictureUrl product.cover!''/>" alt="">
                                        </a>
                                        <div class="he_border1_caption">
                                            <a class="he_border1_caption_a" href="${absoluteContextPath}/product/${product.id?c}" target="_blank"></a>
                                        </div>
                                        <div class="Product-state state-${product.tag!""}"><@display.enum ProductTag.values() product.tag!""/></div>
                                    </div>
                                    <div class="dzen_column_DD_span12">
                                        <div class=" news-cuolumn ">
                                            <h4>${product.name!""}</h4>
                                            <p class="dzen_follow_us dzen-text"><#if product.introduction?? && product.introduction!="">${product.introduction}<#else>&nbsp;</#if></p>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
                </div>
                </#if>
            </#list>

    </section>
</div>
<!--侧边漂浮边栏-->
<div class="side_bar hide">
    <h3>产品分类</h3>
    <#list classifies as classify>
        <#if classify.products?? && classify.products?size gt 0>
            <a href="javascript:void(0)" onclick="scrollToDiv('classify_${classify.id?c}')" for="classify_${classify.id?c}">${classify.name!""}</a>
        </#if>
    </#list>
    <a id="top" href="javascript:void(0)" onclick="$('body').animate({scrollTop:0},300)">返回顶部</a>
</div>
</body>
</html>
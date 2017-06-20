<#import "../display.ftl" as display/>
<html>
<head>
    <title>Product-产品</title>
    <link rel="stylesheet" href="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.min.css" type="text/css" media="all">
    <script src="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.js"></script>
    <script>
        $().ready(function(){
            $('.bxslider').bxSlider({
                displaySlideQty:1,//显示li的个数
                moveSlideQty: 1,//移动li的个数
                captions: false,//自动控制
                auto: true,

            });
            $(window).scroll(function(){
                var currentTop = $(window).scrollTop() + 100;
                var currentClassifyId;
                var first = false;
                $("div[id^='classify_']").each(function(index, element){
                    var top = $(element).offset().top;
                    if (top < currentTop) {
                        first = true;
                        currentClassifyId = $(element).attr("id");
                    }
                });
                if (first) {
                    $(".side_bar").removeClass("hide");
                } else {
                    $(".side_bar").addClass("hide");
                }
                if (currentClassifyId != undefined) {
                    $(".side_bar a.side_bar_active").removeClass("side_bar_active");
                    $(".side_bar a[href='#" + currentClassifyId + "']").addClass("side_bar_active");
                }
            });
        });
    </script>
</head>
<body>
<div id="dz_header_spacer"></div>
<@display.banner banners!/>
<div class="main">
    <section class="dzen_section_DD product">
        <div class="dzen_container">
            <#list classifies as classify>
                <div id="classify_${classify.id?c}" class="cur">
                    <header>
                        <div class="dzen_container">
                            <h4>${classify.name!""}</h4>
                        </div>
                    </header>
                    <div class="dzen_column_DD_span12">
                        <#list classify.products as product>
                            <div class="dzen_column_DD_span3 news-cont">
                                <div class="dzen_column_DD_span12 news-img he_border1">
                                    <a class=" thumb-big">
                                        <img class="he_border1_img" src="<@display.pictureUrl product.cover!''/>" alt="">
                                    </a>
                                    <div class="he_border1_caption">
                                        <a class="he_border1_caption_a" href="${absoluteContextPath}/product/${product.id?c}" target="_blank"></a>
                                    </div>
                                </div>
                                <div class="dzen_column_DD_span12">
                                    <div class=" news-cuolumn ">
                                        <h4>${product.name!""}</h4>
                                        <p class="dzen_follow_us dzen-text">${product.introduction!""}</p>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
        </div>
    </section>
</div>
<!--侧边漂浮边栏-->
<div class="side_bar hide">
    <h3>产品分类</h3>
    <#list classifies as classify>
        <a href="#classify_${classify.id?c}">${classify.name!""}</a>
    </#list>
    <a href="javascript:void(0)" onclick="$('body').animate({scrollTop:0},300)">返回顶部</a>
</div>
</body>
</html>
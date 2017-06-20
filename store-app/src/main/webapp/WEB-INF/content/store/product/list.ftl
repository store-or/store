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
        });
    </script>
</head>
<body>
<div id="dz_header_spacer"></div>
<@display.banner banners!/>
<section class="dzen_section_DD product">
    <div class="dzen_container">
        <#list classifies as classify>
            <header>
                <div class="dzen_container">
                    <h4 id="${classify.id?c}">${classify.name!""}</h4>
                </div>
            </header>
            <div class="dzen_column_DD_span12 ">
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
        </#list>
    </div>
</section>
<!--侧边漂浮边栏-->
<div class="side_bar">
    <ul>
        <h3>产品分类</h3>
        <#list classifies as classify>
            <li><a href="#${classify.id?c}">${classify.name!""}</a></li>
        </#list>
    </ul>
</div>
</body>
</html>
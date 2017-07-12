<#import "display.ftl" as display/>
<html>
<head>
    <title>Home-首页</title>
    <link rel="stylesheet" href="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.min.css" type="text/css" media="all">
    <script src="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.js"></script>
    <script>
        $().ready(function(){
            <#if recommends?? && recommends?size gt 0>
                $('#recommends').bxSlider({
                    slideWidth: 240,
                    minSlides: 2,
                    maxSlides: 3,
                    moveSlides: 3,
                    slideMargin: 30,
                    pager:false
                });
            </#if>
            $('#banners').bxSlider({
                displaySlideQty:1,//显示li的个数
                moveSlideQty: 1,//移动li的个数
                captions: false,//自动控制
                auto: true
            });
        });
    </script>
</head>
<body>
<@display.banner banners!/>
<section id="our_work" class="dzen_section_DD recommends">
    <header class="recommendHeader">
        <div class="dzen_container">
            <h3>Products</h3>
            <h5>精品推荐</h5>
        </div>
    </header>
    <ul class="bxslider" style="height:150px;" id="recommends">
        <#if recommends?? && recommends?size gt 0>
            <#list recommends as recommend>
                <li>
                    <a href="${absoluteContextPath}/product/${recommend.product.id?c}">
                        <img src="<@display.pictureUrl recommend.product.cover!''/>"/>
                        <p class="productName"><strong>${recommend.product.name}</strong></p>
                        <#if recommend.product.introduction?? && recommend.product.introduction!="">
                            <p class="introduction"><span>${recommend.product.introduction!""}</span></p>
                        </#if>
                    </a>
                </li>
            </#list>
        </#if>
    </ul>
    <div class="dzen_column_DD_span12 center_aligned" style="padding-top: 40px;"> <button class="btn-default btn-big" onclick="location.href='${absoluteContextPath}/product/index'">查看全部</button></div>
</section>
<footer id="dz_main_footer">
    <div id="footer_columns">
        <div class="row">
            <div class="span4 clearfix contact-left">
                <div class="widget">
                    <h3>Contact</h3>
                    <h6>联系我们</h6>
                    <div class="contact_info_widget">
                        <p class="first">订购热线：${CompanyConfig.phone!""}</p>
                        <p>地址：${CompanyConfig.address!""}</p>
                        <p>
                            <a href="${CompanyConfig.tmall!""}" class="margin-right-5"><img class="icon" src="${absoluteContextPath}/img/tmall.png" alt=""/></a>
                            <a href="javascript:void(0)" class="wx-code margin-right-5">
                                <img class="icon" src="${absoluteContextPath}/img/wechat.png" alt=""/>
                                <img src="<@display.pictureUrl CompanyConfig.wechat!''/>" class="qrcode"/>
                            </a>
                            <a href="${CompanyConfig.weibo!""}" class="margin-right-5"><img class="icon" src="${absoluteContextPath}/img/weibo.png" alt=""/></a>
                        </p>
                    </div>
                </div>
            </div>
            <div class="span8 clearfix">
                <div class="widget rpwe_widget">
                    <div class="floatL" style="width: 45%;">
                        <h3>About Us</h3>
                        <h6>关于我们</h6>
                        <img src="<@display.pictureUrl CompanyConfig.aboutUs!''/>" alt=""/>
                    </div>
                    <div class="rpwe-block floatL" style="width: 55%;">
                        <div id="about-info"><#if CompanyConfig.introduction?? && CompanyConfig.introduction?length gt 195>${CompanyConfig.introduction?substring(0, 192)}...<#else>${CompanyConfig.introduction!""}</#if></div>
                        <a href="${absoluteContextPath}/about/index" class="more-link">了解更多</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>
</body>
</html>
<#import "display.ftl" as display/>
<html>
<head>
    <title>Home-首页</title>
    <script src="${absoluteContextPath}/js/jquery/slider.js"></script>
    <script>
        $().ready(function(){
            $('#banners').pbTouchSlider({
                slider_Wrap: '#bannerParent',
                slider_Threshold: 50 ,
                slider_Speed:400 ,
                slider_Drag : false,
                slider_Ease:'linear',
                slider_Breakpoints: {
                    default: {
                        height: 300
                    },
                    tablet: {
                        height: 300,
                        media: 1024
                    },
                    smartphone: {
                        height: 200,
                        media: 768
                    }
                }
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
    <div class="container-box">
        <div class="carousel-example">
            <!-- FLEXIBLE BOOTSTRAP CAROUSEL -->
            <div id="simple-content-carousel" class="carousel flexible slide" data-ride="carousel" data-interval="5000" data-wrap="true">
                <#if recommends?? && recommends?size gt 0>
                    <div class="items">
                        <#list recommends as recommend>
                            <div class="flex-item" onclick="location.href='${absoluteContextPath}/product/${recommend.product.id?c}'">
                                <img class="img-responsive" src="<@display.pictureUrl recommend.product.cover!''/>"/>
                                <h4>${recommend.product.name}</h4>
                                <p>${recommend.product.introduction!"&nbsp;"}</p>
                            </div>
                        </#list>
                    </div>
                    <div class="carousel-inner" role="listbox"></div>
                </#if>
            </div>
        </div>
    </div>
    <div class="dzen_column_DD_span12 center_aligned"> <button class="btn-default btn-big" onclick="location.href='${absoluteContextPath}/product/index'">查看全部</button></div>
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
            <div class="span8 clearfix contact-right">
                <div class="widget">
                    <div class="floatL widget-l">
                        <h3>About Us</h3>
                        <h6>关于我们</h6>
                        <img src="<@display.pictureUrl CompanyConfig.aboutUs!''/>" alt=""/>
                    </div>
                    <div class="rpwe-block floatL widget-r">
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
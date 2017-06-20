<#import "display.ftl" as display/>
<html>
<head>
    <title>Home-首页</title>
    <link rel="stylesheet" href="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.min.css" type="text/css" media="all">
    <script src="${absoluteContextPath}/js/jquery/bxslider/jquery.bxslider.js"></script>
    <script>
        $().ready(function(){
            <#if recommends?? && recommends?size gt 0>
                $('.slider-view').bxSlider({
                    slideWidth: 240,
                    minSlides: 2,
                    maxSlides: 3,
                    moveSlides: 1,
                    slideMargin: 30,
                    pager:false
                });
            </#if>
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
<section id="our_work" class="dzen_section_DD">
    <header>
        <div class="dzen_container">
            <h3>Product</h3>
            <h5>精品推荐</h5>
        </div>
    </header>
    <#if recommends?? && recommends?size gt 0>
        <div class="dzen_section_content">
            <div class="dzen_container">
                <div class="dzen_column_DD_span12 " style="width:780px;">
                    <div class="slider-view">
                        <#list recommends as recommend>
                            <div class="slide"><a href="${absoluteContextPath}/product/${recommend.product.id}"><img src="<@display.pictureUrl recommend.product.cover!''/>"><p><strong>${recommend.product.name}</strong><span>${recommend.product.introduction!""}</span></p></a></div>
                        </#list>
                    </div>
                    <div class="dzen_column_DD_span12 center_aligned" style="margin-top:15px;"> <button class="btn-default btn-big" onclick="locacation.href='${absoluteContextPath}/product/list'">查看全部</button></div>
                </div>
            </div>
        </div>
    </#if>

</section>
<footer id="dz_main_footer">
    <div id="footer_columns">
        <div class="row">
            <div class="span4 clearfix contact-left">
                <div class="widget">
                    <h3>CONTACT</h3>
                    <h6>联系我们</h6>
                    <div class="contact_info_widget">
                        <p>   <a href=""><img src="${absoluteContextPath}/img/tmall-big.png" alt=""/></a>
                            <a href=""><img src="${absoluteContextPath}/img/weix-big.png" alt=""/></a>
                            <a href=""><img src="${absoluteContextPath}/img/sina-big.png" alt=""/></a></p>
                        <p>订购热线：0592-5113003</p>
                        <p>地址：Kendall is eighteen now, living every day to it's recorded</p>
                    </div>
                </div>
            </div>
            <div class="span8 clearfix">
                <div class="widget rpwe_widget">
                    <div class="span4 floatL">
                        <h3>ABOUT US</h3>
                        <h6>关于我们</h6>
                        <img src="<@display.pictureUrl CompanyConfig.aboutUs!''/>" alt=""/>
                    </div>
                    <div class="rpwe-block span8 floatL">
                        <img src="<@display.pictureUrl CompanyConfig.contactUs!''/>" alt=""/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>
</body>
</html>
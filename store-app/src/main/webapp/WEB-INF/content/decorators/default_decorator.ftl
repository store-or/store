<!DOCTYPE html>
<#import "../store/display.ftl" as display/>
<html xmlns:sitemesh>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <title><sitemesh:write property="title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <link href="${absoluteContextPath}/css/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery-1.8.2.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/light.js"> </script>
    <sitemesh:write property="head"/>
</head>
<body>
<header id="dz_main_header" class="clearfix">
    <div class="container">
        <div id="logo">
            <a href="${absoluteContextPath}/index">${SystemConfig.company!""}</a>
        </div>
        <nav>
            <ul id="main_menu">
                <li for="index"><a href="${absoluteContextPath}/index"><span>Home</span>首页</a></li>
                <li for="product"><a href="${absoluteContextPath}/product/index"><span>Products</span>全部产品</a></li>
                <li for="about"><a href="${absoluteContextPath}/about/index"><span>${SystemConfig.company!""}'s Story</span>关于${SystemConfig.company!""}</a></li>
                <li for="contact"><a href="${absoluteContextPath}/contact/index"><span>Contact</span>联系我们</a></li>
            </ul>
        </nav>
        <div class="Contact-info">
            <p>
                <img src="${absoluteContextPath}/img/phone.png" alt=""/><b>${CompanyConfig.phone!""}</b>
                <a href="${CompanyConfig.weibo!""}"><img src="${absoluteContextPath}/img/sina.png" alt=""/></a>
                <a href="javascript:void(0)" class="wx-code">
                    <img src="${absoluteContextPath}/img/weix.png" alt=""/>
                    <img src="<@display.pictureUrl CompanyConfig.wechat!''/>" class="qrcode"/>
                </a>
                <a href="${CompanyConfig.tmall!""}"><img src="${absoluteContextPath}/img/tmall.png" alt=""/></a>
            </p>
        </div>
    </div>
</header>
<div style="min-height: 639px;margin-top:60px;">
    <sitemesh:write property="body"/>
</div>
<div id="footer_copyright">
    <div class="container">
        <div class="row">
            <div class="span12 footer_copyright">
                <p>${SystemConfig.copyright!""}</p>
            </div>
        </div>
    </div>
</div>
<script>
    $().ready(function(){
        var url = location.href;
        var path = url.substring(${absoluteContextPath?length});
        var forPath = '';
        if (path == '/' || path == '') {
            forPath = 'index';
        } else {
            forPath = path.split("/")[1];
        }
        $("#main_menu li[for='" + forPath + "']").addClass("current-menu-item");
    });
</script>
</body>

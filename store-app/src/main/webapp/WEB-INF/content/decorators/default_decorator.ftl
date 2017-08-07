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
    <div class="container" style="width: 100%;height:60px">
        <div id="logo">
            <#if SystemConfig.company?? && SystemConfig.company?replace(" ", "") != "">
                <a href="${absoluteContextPath}/index">${SystemConfig.company!"&nbsp;"}</a>
            <#else>
                &nbsp;
            </#if>
        </div>
        <nav>
            <ul id="main_menu">
                <li for="index"><a href="${absoluteContextPath}/index">首页</a></li>
                <li for="product"><a href="${absoluteContextPath}/product/index">全部产品</a></li>
                <li for="about"><a href="${absoluteContextPath}/about/index">关于${SystemConfig.company!""}</a></li>
                <li for="contact"><a href="${absoluteContextPath}/contact/index">联系我们</a></li>
            </ul>
        </nav>
        <div class="Contact-info">
            <p>
                <img src="${absoluteContextPath}/img/phone.png" alt=""/><b>${CompanyConfig.phone!""}</b>
                <a href="${CompanyConfig.tmall!""}" class="margin-right-5"><img src="${absoluteContextPath}/img/tmall.png" alt=""/></a>
                <a href="javascript:void(0)" class="wx-code margin-right-5">
                    <img src="${absoluteContextPath}/img/wechat.png" alt=""/>
                    <img src="<@display.pictureUrl CompanyConfig.wechat!''/>" class="qrcode"/>
                </a>
                <a href="${CompanyConfig.weibo!""}" class="margin-right-5"><img src="${absoluteContextPath}/img/weibo.png" alt=""/></a>
            </p>
        </div>
    </div>
</header>
<div style="min-height: 500px;margin-top:60px;" id="content-body">
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
        var headerHeight = $("#dz_main_header").get(0).offsetHeight;
        var footerHeight = $("#footer_copyright").get(0).offsetHeight;
        var minHeight = $(window).height() - headerHeight - footerHeight + 'px';
        $("#content-body").css("min-height",minHeight);
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

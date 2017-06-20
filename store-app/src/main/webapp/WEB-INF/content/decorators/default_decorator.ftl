<!DOCTYPE html>
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
            <a href="${absoluteContextPath}/index"><img src="${absoluteContextPath}/img/u74.png" alt=""/></a>
        </div>
        <nav>
            <ul id="main_menu">
                <li for="index"><a href="${absoluteContextPath}/index"><span>Home</span>首页</a></li>
                <li for="product"><a href="${absoluteContextPath}/product/index"><span>Products</span>全部产品</a></li>
                <li for="about"><a href="${absoluteContextPath}/about/index"><span>xxx's Story</span>关于我们</a></li>
                <li for="contact"><a href="${absoluteContextPath}/contact/index"><span>Contact</span>联系我们</a></li>
            </ul>
        </nav>
        <div class="Contact-info">
            <p>
                <img src="${absoluteContextPath}/img/phone.png" alt=""/><b>400-988-900</b>
                <a href=""><img src="${absoluteContextPath}/img/tmall.png" alt=""/></a>
                <a href=""><img src="${absoluteContextPath}/img/weix.png" alt=""/></a>
                <a href=""><img src="${absoluteContextPath}/img/sina.png" alt=""/></a>
            </p>
        </div>
    </div>
</header>
<sitemesh:write property="body"/>
<div id="footer_copyright">
    <div class="container">
        <div class="row">
            <div class="span12 footer_copyright">
                <div><img src="${absoluteContextPath}/img/u74.png" alt=""></div>
                <p>©2011-2017 Baicaowei All Rights Reserved. xxxx食品有限公司版权所有 | 浙ICP备14038051号-1</p>
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

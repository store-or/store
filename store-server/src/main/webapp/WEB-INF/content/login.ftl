<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <link href="${absoluteContextPath}/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/layers.min.css" rel="stylesheet" type="text/css">
    <title>商城后台</title>
</head>

<body class="page-login">
<div id="message_login">
    <#include "message.ftl"/>
</div>
<div id="page-signup-bg">
    <div id="box"></div>
    <div class="login-box">
        <a href="${absoluteContextPath}/login" class="logo-name  text-center m-t-xs">商城后台</a>
        <form class="m-t-md" action="${absoluteContextPath}/login" method="post">
            <input type="hidden" value="~!@#$%^*">
            <div class="form-group">
                <input type="text" class="form-control user" placeholder="账号"  name="username" id="username">
            </div>
            <div class="form-group">
                <input type="password" name="password" class="form-control password" placeholder="密码" name="password" id="password">
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">登录</button>
        </form>
        <p class="text-center m-t-xs text-sm">${version!""}</p>
    </div>
</div>
<div class="foot">
    <p>${copyright!""} ${company!""}(${version!""})</p>
</div>
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/light.js"></script>
    <script src='${absoluteContextPath}/js/particles.js' type="text/javascript"></script>
    <script src='${absoluteContextPath}/js/background.js' type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            <#if shiroLoginFailure??>
                <#if shiroLoginFailure?contains('DisabledAccountException')>
                    $.showMessage({message:'用户已被屏蔽,请登录其他用户',parentId:'message_login'});
                <#else>
                    $.showMessage({message:'用户名或密码不正确，请重试.', parentId:'message_login'});
                </#if>
            </#if>
        })
    </script>
</body>
</html>
<!DOCTYPE html>
<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<html xmlns:sitemesh>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <title>store-<sitemesh:write property="title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <link rel="shortcut icon" href="${absoluteContextPath}/img/ott.ico" type="image/x-icon">
    <link href="${absoluteContextPath}/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/layers.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/layers/dark-layer.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/fontawesome/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/line-icons/simple-line-icons.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/js/waves/waves.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/js/pace-flash/pace-theme-flash.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery-1.8.2.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery.form.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/bootstrap/bootstrap.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/bootstrap/jquery.bootstrap-dropdown-on-hover.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/light.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/store/store.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/waves/waves.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/pace-flash/pace.min.js"> </script>
    <script type="text/javascript" src="${absoluteContextPath}/js/layers.min.js"> </script>

    <sitemesh:write property="head"/>
</head>
<body class="compact-menu page-header-fixed  pace-done">
<main class="page-content content-wrap">
    <div class="navbar">
        <#include "menus.ftl"/>
    </div>
    <div id="message_l">
        <#include "../message.ftl"/>
    </div>
    <sitemesh:write property="body"/>
    <#include "../modifySelfPassword.ftl"/>
</main>
</body>
</html>


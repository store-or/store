<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<#macro securityUrl action url>
    <@sec.authorize action="${action}">${absoluteContextPath}${url};</@sec.authorize>
</#macro>
<div class="navbar-inner">
    <div class="logo-box">
        <a href="${absoluteContextPath}/login" class="logo-text"><span><img src="${absoluteContextPath}/img/logo.png"></span></a>
    </div><!-- Logo Box -->
    <div class="topmenu-outer">
        <div class="top-menu" id="top-menu">
            <ul class="nav navbar-nav navbar-left" id="main-menu">
            <@sec.authorize anyAction="product-list;classify-list;">
                <!-- url=/cms/x1/x2 for= x1-x2 多个的时候,x1-x2,x11-x22, -->
                <li class="dropdown" for=",video-list,entry-list,classify-list,provider-list,star-list," hasChild>
                    <a href="javascript:void(0)" data-toggle="dropdown" class="dropdown-toggle waves-effect waves-button "  >
                        产品管理<i class="fa fa-angle-down m-l-xxs"></i>
                    </a>
                    <ul class="dropdown-menu  animated" role="menu" style="min-width:120px;" data-animation="fadeInUp">
                        <@sec.authorize action="product-list">
                            <li role="presentation"  class="no-link"><a href="${absoluteContextPath}/product/list">产品管理</a></li>
                        </@sec.authorize>
                        <@sec.authorize action="classify-list" >
                            <li role="presentation" class="no-link"><a href="${absoluteContextPath}/classify/list">分类管理</a></li>
                        </@sec.authorize>
                    </ul>
                </li>
            </@sec.authorize>
            <@sec.authorize anyAction="company-config,recommend-list,banner-list">
                <li class="dropdown" for=",companyConfig-config,recommend-list,banner-list," hasChild>
                    <a href="javascript:void(0)" data-toggle="dropdown" class="dropdown-toggle waves-effect waves-button "  >
                        前端编排<i class="fa fa-angle-down m-l-xxs"></i>
                    </a>
                    <ul class="dropdown-menu  animated" role="menu" style="min-width:120px;" data-animation="fadeInUp">
                        <@sec.authorize action="banner-list" >
                            <li role="presentation" class="no-link"><a href="${absoluteContextPath}/banner/list">banner管理</a></li>
                        </@sec.authorize>
                        <@sec.authorize action="recommend-list" >
                            <li role="presentation" class="no-link"><a href="${absoluteContextPath}/recommend/list">精品推荐</a></li>
                        </@sec.authorize>
                        <@sec.authorize action="menu-list" >
                            <li role="presentation" class="no-link"><a href="${absoluteContextPath}/menu/list">品牌介绍</a></li>
                        </@sec.authorize>
                        <@sec.authorize action="company-config" >
                            <li role="presentation" class="no-link"><a href="${absoluteContextPath}/companyConfig/config">页面配置</a></li>
                        </@sec.authorize>
                    </ul>
                </li>
            </@sec.authorize>
            <@sec.authorize anyAction="user-list;role-operate;resource-operate;param-list;cpchn-list;group-list">
                <li for=",privilege-user,privilege-role,group-list,param-list,cpchn-list," hasChild>
                    <a class="waves-effect waves-button" urls="<@securityUrl "user-list" "/privilege/user/list"/><@securityUrl "role-operate" "/privilege/role/list"/><@securityUrl "group-list" "/group/list"/><@securityUrl "param-list" "/param/list"/><@securityUrl "cpchn-list" "/cpchn/list"/>" href="javascript:void(0)" onclick="selectRootMenu(this)">系统管理</a>
                </li>
            </@sec.authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" >
                        <span class="user-name">hi,${principal.trueName}！<i class="fa fa-angle-down"></i></span>
                    </a>
                    <ul class="dropdown-menu  animated" role="menu"  data-animation="fadeInUp">
                    <#if !principal.superuser>
                        <li role="presentation"><a href="javascript:void(0)" onclick="toModifySelfPassword()"><i class="icon-lock"></i>修改密码</a></li>
                    </#if>
                        <li role="presentation"><a href="${absoluteContextPath}/logout">退出</a></li>
                    </ul>
                </li>
            </ul><!-- Nav -->
        </div><!-- Top Menu -->
    </div>
</div>

<script type="text/javascript">
    $().ready(function() {
        $("#top-menu").bootstrapDropdownOnHover();
        var path = window.location.href;
        var paths = path.split("/");
        if (paths.length <= 5 || paths[5] == '') {
            return;
        }
        var index = paths[5].indexOf('?');
        if (index != -1) {
            paths[5] = paths[5].substring(0, index);
        }
        index = paths[5].indexOf('#')
        if (index != -1 ) {
            paths[5] = paths[5].substring(0, index);
        }

        var p = paths[4] + "-" + paths[5];
        var mainMenu = $("#main-menu li[for*='," + p + ",']");
        mainMenu.addClass("head-active");
        if (mainMenu.attr("hasChild") != undefined) {
            var childMenu = $("#child-menu li[for='" + p + "']");
            childMenu.addClass("open");
        }
    });
    function selectRootMenu(obj){
        var url = $(obj).attr("urls").split(";")[0];
        location.href = url;
    }

    function toModifySelfPassword() {
        $('#pop_changePassword_div').modal({show: 'show',backdrop:'static'});
    }
</script>
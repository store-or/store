<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>品牌介绍</title>
    <script type="text/javascript">
        $().ready(function() {
            <#if menuId??>
                var $menu = $("#layout-menu-" + ${menuId?c});
                $menu.click();
                return;
            </#if>
            <#if menus?? && menus?size gt 0>
                $("#layout-menus ul li:first").click();
            </#if>
        });

        function showContent(obj, menuId) {
            var menu = $(obj);
            if (menu.hasClass("active-line")) {
                return;
            }
            var lastMenu = menu.parent().children(".active-line");
            lastMenu.html("<a class='text-hidden'  href='javascript:void(0)' >" + lastMenu.text() + "</a>").removeClass("active-line");
            menu.html(menu.text()).addClass("active-line");
            ajaxContent(menuId);
        }

        function ajaxContent(menuId) {
            $.customAjax({
                url:'${absoluteContextPath}/menu/ajaxContent',
                type:'post',
                data:{
                    menuId: menuId
                },
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        $('#menuContent').html(data.content.data);
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function toEditMenu(menuId){
            var data={};
            var url = '${absoluteContextPath}/menu/toAdd';
            if(!!menuId){
                var url = '${absoluteContextPath}/menu/toModify';
                data.menuId = menuId;
            }
            $.customAjax({
                url: url,
                type:'post',
                data: data,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        $('#menuContent').html(data.content.data);
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function delElement(menuId) {
            $.customAjax({
                url:'${absoluteContextPath}/menu/del',
                type:'post',
                data:{
                    menuId: menuId
                },
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        window.location.href = '${absoluteContextPath}/menu/list';
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }
    </script>
</head>
<path>
    <li>前端编排</li>
    <li>品牌介绍</li>
</path>
<body class="compact-menu page-header-fixed  pace-done">
<div class="page-inner">
    <div id="main-wrapper" class="panel-white p-h-sm p-v-xs">
        <div class="row m-t-md ">
            <div class="col-md-2 layout-box">
                <a href="javascript:void(0)" class="btn btn-default btn-block" onclick="toEditMenu()">添加菜单</a>
                <div id="layout-menus">
                    <ul class="list-unstyled mailbox-nav">
                    <#if menus??>
                        <#list menus as menu>
                            <li  class="text-hidden"  id="layout-menu-${menu.id?c}" onclick="showContent(this, ${menu.id?c})" title="${(menu.name!"")?xhtml}"><a  class="text-hidden"  href="javascript:void(0)" >${(menu.name!"")?xhtml}</a></li>
                        </#list>
                    </#if>
                    </ul>
                </div>
            </div>
            <div class="col-md-10 layout-content">
                <div class="row la">
                    <div class="col-md-12" id="menuContent">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

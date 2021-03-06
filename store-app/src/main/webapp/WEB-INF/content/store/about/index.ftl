<#import "../display.ftl" as display/>
<html>
<head>
    <title>About Us-关于我们</title>
    <!-- 配置文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/ueditor.parse.min.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            <#if menuList?? && menuList?size gt 0>
                $("#menuDiv ul li:first").click();
            </#if>
        });

        function ajaxContent(obj, menuId) {
            if($(obj).hasClass("widget-active")){
                return;
            }
            $.customAjax({
                url:'${absoluteContextPath}/about/ajaxContent',
                type:'post',
                data:{
                    menuId: menuId
                },
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        $('#menuContentDiv').html(data.content);
                        $(obj).siblings().removeClass("widget-active");
                        $(obj).addClass("widget-active");
                        setTimeout(function(){
                            uParse("#menuContentDiv",{
                                rootPath:"${absoluteContextPath}/js/ueditor/"
                            });
                        }, 10);
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }
    </script>

</head>
<body>
<section class="dzen_section_DD aboutUs">
    <div class="dzen_section_content">
        <div class="dzen_container">
            <div class="sidebar-widget-heading">
                <h3>关于我们</h3>
            </div>
            <div class="dzen_column_DD_span2 about-left">
                <aside class="span4 sidebar sidebar_left">
                    <div class="widget widget_categories" id="menuDiv">
                        <ul>
                            <#if menuList?? && menuList?size gt 0>
                                <#list menuList as menu>
                                    <li onclick="ajaxContent(this, ${menu.id?c})"><a href="javascript:void(0);" title="${(menu.name!"")?xhtml}">${(menu.name!"")?xhtml}</a></li>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                </aside>
            </div>
            <div class="dzen_column_DD_span10 about_us_introducton margin_bottom" id="menuContentDiv"></div>
        </div>
    </div>
</section>
</body>
</html>

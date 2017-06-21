<#import "../display.ftl" as display/>
<html>
<head>
    <!-- 配置文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/ueditor.parse.min.js"></script>
    <script type="text/javascript">
        uParse("#showContent",{
            rootPath:"${absoluteContextPath}/js/ueditor/"
        });
    </script>

    <title>Contact-联系我们</title>
</head>
<body>
<div id="dz_header_spacer"></div>
<section id="our_work" class="dzen_section_DD">
    <div class="dzen_section_content">
        <div class="dzen_container">
            <div class="sidebar-widget-heading">
                <h3>联系我们</h3>
            </div>
            <div id="showContent">
            ${portalParam.value!""}
            </div>
        </div>
    </div>
</section>
</body>
</html>
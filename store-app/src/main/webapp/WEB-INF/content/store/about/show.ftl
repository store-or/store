<!-- 配置文件 -->
<script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/ueditor.parse.min.js"></script>
<script type="text/javascript">
    uParse("#aboutContent",{
        rootPath:"${absoluteContextPath}/js/ueditor/"
    });
</script>

<div>
    <h4>
        <span>${menu.name!""}</span>
    </h4>
    <div class="margin_bottom">
        <div id="aboutContent">
            ${menu.publishContent!""}
        </div>
    </div>
</div>

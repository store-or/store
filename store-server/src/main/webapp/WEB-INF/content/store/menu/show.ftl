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


<span>
${menu.name!""}<#if menu.currentVersion?? && menu.publishVersion?? && menu.currentVersion==menu.publishVersion>已发布<#else>未发布</#if>
</span>
<div style="float:right">
    <a class="m-b-xxs btn btn-primary m-b-xs" data-toggle="modal" data-target="#myModal" onclick="toEditMenu(${menu.id?c})">
        编辑
    </a>
    <#if !menu.publishVersion?? || menu.currentVersion != menu.publishVersion>
    <a id="aPublish" class="m-b-xxs btn m-b-xs btn-primary" data-toggle="modal" data-target="#myModal" onclick="publicVersion(${menu.id?c})">
        发布
    </a>
    </#if>
</div>

<ul class="cd-gallery post drag">
    <div id="showContent">
        ${menu.currentContent!""}
    </div>
</ul>

<script type="text/javascript">
    function publicVersion(menuId) {
        $.customAjax({
            url:'${absoluteContextPath}/menu/publicVersion',
            type:'post',
            data:{
                menuId: menuId
            },
            success:function(json){
                var data = JSON.parse(json);
                if (data.returnCode == 1) {
                    window.location.href = '${absoluteContextPath}/menu/list?menuId='+ menuId;
                } else {
                    $.showMessage({message: data.returnMsg});
                }
            }
        });
    }
</script>
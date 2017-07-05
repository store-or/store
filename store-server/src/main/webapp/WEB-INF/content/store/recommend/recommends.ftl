<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<input type="hidden" class="hide" id="condition" value="${(condition!"")?xhtml}"/>
<ul class="cd-gallery post drag">
<#if recommends??>
    <#list recommends as recommend>
        <li recommendId="${recommend.id?c}" class="wide-c">
            <@sec.authorize action="recommend-del">
                <span class="cd-sale text-right">
                    <button class="btn btn-primary btn-sm" onmousedown="del(${recommend.id?c}); event.stopPropagation();">删除</button>
                </span>
            </@sec.authorize>
            <a href="javascript:void(0)">
                <ul class="cd-item-wrapper">
                    <li class="selected">
                        <img src="<#if recommend.product.cover?? && !recommend.product.cover?starts_with("http")>${uploadFileContext}/</#if>${recommend.product.cover!}" alt="Preview image">
                    </li>
                </ul>
            </a>
            <div class="cd-item-info">
                <b><a href="javascript:void(0)" title="${(recommend.product.name!"")?xhtml}">${(recommend.product.name!"")?xhtml}</a></b>
            </div>
        </li>
    </#list>
</#if>
</ul>
<@sec.authorize action="recommend-operate">
    <#if recommends??>
    <script>
        $(function(){
            $('.drag').dad({
                callback :function(target) {
                    var $obj = $(target);
                    $.customAjax({
                        url:'${absoluteContextPath}/recommend/modifyIndex',
                        type:'post',
                        data:{
                            id: $obj.attr("recommendId"),
                            index : $obj.index() + 1
                        },
                        success:function(json){
                            var data = JSON.parse(json);
                            if (data.returnCode == 0) {
                                $.showMessage({message: data.returnMsg});
                            }
                        }
                    });
                }
            });
        });
    </script>
    </#if>
</@sec.authorize>
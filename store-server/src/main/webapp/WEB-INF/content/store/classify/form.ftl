<div class="modal-dialog modal-lg" style="width:600px;">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title"><#if classify.id??>
                修改分类
            <#else>
                添加分类
            </#if></h4>
        </div>
        <div class="modal-body">
            <div id="classify_message">
                <#include "../../message.ftl"/>
            </div>
            <form class="form-horizontal" id="classifyForm" method="post" action="${absoluteContextPath}/classify/<#if classify.id??>modify<#else>save</#if>">
                <input type="text" class="hide" />
            <#if classify.id??>
                <input  type="hidden" name="id"  class="form-control" value="${classify.id?c}">
            </#if>
                <!-- / .form-group -->
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label"><span class="text-danger">* </span>分类名称:</label>
                    <div class="col-sm-10">
                        <input id="name" type="text"  name="name"  class="form-control" value="${classify.name!""}" maxlength="256">
                    </div>
                </div>
                <!-- / .form-group -->
            </form>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary"   onclick="modify()">
                <#if classify.id??>确认修改<#else>确认添加</#if>
            </button>
        </div>
    </div>
    <!-- / .modal-content -->
</div>

<script type="text/javascript">
    function modify() {
        $('#classifyForm').customAjaxSubmit({
            success:function(json){
                $('#classifyForm').submitResult(json,{
                    messageParentId: 'classify_message',
                    success:function(data) {
                        $('#pop_classify_div').modal("hide");
                        refresh();
                        if (data.returnCode == 1) {
                            $.showMessage({type:"success",message: data.returnMsg});
                        }else{
                            $.showMessage({type:"error",message: data.returnMsg});
                        }
                    }
                });
            }
        });
    }

</script>

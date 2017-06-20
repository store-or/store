<div class="modal-dialog modal-lg" style="width:600px;">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">
            <#if user.id??>
                修改用户
            <#else>
                添加用户
            </#if>
            </h4>
        </div>
        <div class="modal-body modal-scroll">
            <div id="user_message">
                <#include "../../message.ftl"/>
            </div>
            <form id="userForm" class="form-horizontal" method="post" action="${absoluteContextPath}/privilege/user/<#if user.id??>modify<#else>save</#if>">
                <#if user.id??>
                    <input type="hidden" name="id" value="${user.id?c}">
                <#else>
                    <div class="form-group">
                        <label for="loginName" class="col-sm-2 control-label"><span class='text-danger'>* </span>登录名:</label>
                        <div class="col-sm-10">
                            <input id="loginName" type="text" name="loginName" class="form-control" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label"><span class='text-danger'>* </span>密码:</label>
                        <div class="col-sm-10">
                            <input type="password" style="display:none">
                            <input id="password" type="password" name="password" class="form-control" value="" >
                        </div>
                    </div>
                </#if>
                <div class="form-group">
                    <label for="trueName" class="col-sm-2 control-label"><span class='text-danger'>* </span>真实姓名:</label>
                    <div class="col-sm-10">
                        <input name="trueName" type="text" id="trueName" class="form-control" value="${user.trueName!""}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-sm-2 control-label"><span class='text-danger'>* </span>邮件:</label>

                    <div class="col-sm-10">
                        <input id="email" type="text" name="email"  class="form-control" value="${user.email!""}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="status" class="col-sm-2 control-label">状态:</label>
                    <div class="col-sm-10">
                        <select name="status" id="status" class="form-control">
                            <option value="1" <#if user.status?? && user.status == 1> selected="selected"</#if>>启用</option>
                            <option value="0" <#if user.status?? &&user.status == 0> selected="selected"</#if>>禁用</option>
                        </select>
                    </div>
                </div>
            </form>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" onclick="modify()">
            <#if user.id??>确认修改<#else>确认添加</#if>
            </button>
        </div>
    </div>
</div>
<script type="text/javascript">
    function modify() {
        $('#userForm').customAjaxSubmit({
            success:function(json){
                $('#userForm').submitResult(json,{
                    messageParentId: 'user_message',
                    success:function(data) {
                        if (data.returnCode == 1) {
                            $.showMessage({type: "success", message: data.returnMsg});
                            refresh();
                        }else{
                            $.showMessage({type: "error", message: data.returnMsg});
                        }
                    }
                });

            }
        });
    }

</script>

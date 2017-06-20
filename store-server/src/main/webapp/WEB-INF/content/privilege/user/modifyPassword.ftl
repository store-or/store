<div class="modal-dialog modal-lg" style="width:600px;">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">修改密码</h4>
        </div>
        <div class="modal-body modal-scroll">
            <div id="modifyPasswordMsg">
                <#include "../../message.ftl"/>
            </div>
            <div class="col-sm-10">
                <form id="modifyPasswordForm" class="form-horizontal" action="${absoluteContextPath}/privilege/user/modifyPassword" method="post">
                    <input type="hidden" name="id" value="${id?c}" class="hide"/>
                    <div class="form-group">
                        <label for="password" class="col-sm-4 control-label"><span class='text-danger'>* </span>密码:</label>
                        <div class="col-sm-8">
                            <input type="password" name="password" id="password" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword" class="col-sm-4 control-label""><span class='text-danger'>* </span>确认密码:</label>
                        <div class="col-sm-8">
                            <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer" style="background: none;border-top: none">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" onclick="modifyPassword()">确认修改</button>
        </div>
    </div>
</div>
<script type="text/javascript">
    function modifyPassword(){
        $('#modifyPasswordForm').customAjaxSubmit({
            success:function(json){
                $('#modifyPasswordForm').submitResult(json , {
                    messageParentId: 'modifyPasswordMsg',
                    success:function(){
                        $("#pop_user_div").modal('hide');
                        $.showMessage({message:'修改密码成功',  type: 'success'});
                    }
                });
            }
        });
    }
</script>
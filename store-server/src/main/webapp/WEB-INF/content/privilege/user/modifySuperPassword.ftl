<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>修改密码</title>
    <script type="text/javascript">
        function modifyPassword(){
            $('#modifyPasswordForm').customAjaxSubmit({
                success:function(json){
                    $('#modifyPasswordForm').submitResult(json);
                }
            });
        }
    </script>
</head>
<body>
<div>
    <div class="modal-header">
        <h4 class="modal-title">修改超管密码</h4>
    </div>
    <div class="modal-body modal-scroll">
        <div class="col-sm-8">
            <form id="modifyPasswordForm" class="form-horizontal" action="${absoluteContextPath}/privilege/user/modifySuperPassword" method="post">
                <div class="form-group">
                    <label for="password" class="col-sm-2 control-label"><span class='text-danger'>* </span>密码:</label>
                    <div class="col-sm-10">
                        <input type="password" name="password" id="password" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="col-sm-2 control-label""><span class='text-danger'>* </span>确认密码:</label>
                    <div class="col-sm-10">
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer" style="background: none;border-top: none">
        <button type="button" class="btn btn-primary" onclick="modifyPassword()">确认修改</button>
    </div>
</div>
</body>
</html>
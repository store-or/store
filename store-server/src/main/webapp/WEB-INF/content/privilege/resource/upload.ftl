<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>权限资源上传</title>
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery.form.js"></script>
    <script type="text/javascript">
        function uploadResource(){
            $('#uploadResourceForm').customAjaxSubmit({
                success:function(data){
                    json = revise(data);
                    var msg = JSON.parse(json);
                    $('#uploadResourceForm').submitResult(json);
                    if (msg.returnCode == 0) {
                        $.showMessage({
                            message:msg.returnMsg
                        })
                    }
                }
            });
        }

        function revise(json) {
            var re = /[\{](.*)[\}]/;
            return json.match(re)[0];
        }
    </script>
</head>
<body>
<div class="modal-body modal-scroll">
    <div id="uploadResource_message">
    <#include "../../message.ftl"/>
    </div>
    <form class="form-horizontal"  id="uploadResourceForm" action="${absoluteContextPath}/privilege/resource/upload" method="post">

            <div class="form-group">
                    <label for="file"  class="col-sm-2 control-label" >请选择zip文件:</label>
                    <div class="col-sm-3">
                        <input type="file" class="form-control" name="file" accept=".zip" id="file"/>
                    </div>
                <input class="btn btn-primary" type="button" value="上 传"
                       onclick="uploadResource()">
            </div>
    </form>
</div>
</body>
</html>
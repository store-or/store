<div class="modal-dialog modal-lg" style="width: 800px;">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="">×</button>
            <h4 class="modal-title">权限资源上传</h4>
        </div>
        <div class="modal-body modal-scroll">
            <div id="resource_message">
                <#include "../../message.ftl"/>
            </div>
            <form class="form-horizontal"  id="uploadResourceForm" action="${absoluteContextPath}/privilege/resource/upload" method="post">
                <div class="form-group">
                    <label for="file"  class="col-sm-2 control-label" >请选择zip文件:</label>
                    <div class="col-sm-8">
                        <input type="file" class="form-control" name="file" accept=".zip" id="file"/>
                    </div>
                    <input class="btn btn-primary" type="button" value="上 传"
                           onclick="uploadResource()">
                </div>
                <div class="form-group">
                    <label for="description" class="col-sm-2 control-label">权限资源:</label>
                    <div class="col-sm-10" id="resourceDiv">
                        <table id="action_table"  cellpadding="0" cellspacing="0" border="0"
                               class="table table-striped table-bordered" aria-describedby="jq-datatables-example_info">
                            <tbody>
                            <#list tree as action>
                                <#if action_index gt 0 >
                                    <#if action.parentName != "/">
                                    <tr data-tt-parent-id="${action.parentName}" data-tt-id="${action.name}" class="${(action_index%2==0)?string("gradeA even","gradeU odd")} child-of-tr-${action.parentName}" id="tr-${action.name}" >
                                        <td><input class="check-all child-of-ac_ck_${action.parentName}" id="ac_ck_${action.name}"  type="checkbox" <#if action.assign>checked="true"</#if>  name="action" value="${action.name}"/>${action.description!""}</td>
                                        <td>${action.name!""}</td>
                                    </tr>
                                    <#else>
                                    <tr data-tt-id="${action.name}" class="${(action_index%2==0)?string("gradeA even","gradeU odd")} ignore" id="tr-${action.name}">
                                        <td><input class="check-all" style="float: none"  id="ac_ck_${action.name}"  type="checkbox"  /><span style="color:olive">${action.description!""}</span></td>
                                        <td></td>
                                    </tr>
                                    </#if>
                                </#if>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    function initActionTable(){
        var $action_table = $('#resourceDiv #action_table');
        $action_table.treetable({initialState:'expanded',expandable: true});
        $action_table.lightTable({
            pagination:false
        });
        $action_table.bindCheckbox();
    }

    function uploadResource(){
        $('#uploadResourceForm').customAjaxSubmit({
            success:function(data){
                json = $(data).html();
                var msg = JSON.parse(json);
                $('#uploadResourceForm').submitResult(json, {
                    messageParentId : "resource_message"
                });
                if (msg.returnCode == 1) {
                    $.customAjax({
                        url:'${absoluteContextPath}/privilege/role/listResource',
                        type:'post',
                        success:function(json){
                            var data = JSON.parse(json);
                            if (data.returnCode == 1) {
                                $("#resourceDiv").html(data.content.data);
                                $("#actionDiv").html(data.content.data);
                                initActionTable();
                            } else {
                                $.showMessage({message: data.returnMsg});
                            }
                        }
                    });
                }
            }
        });
    }
    $().ready(function() {
        initActionTable();
    });
</script>

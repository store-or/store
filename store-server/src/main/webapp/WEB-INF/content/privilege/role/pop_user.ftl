<div class="modal-dialog modal-lg">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">
                为&nbsp;${roleName}&nbsp;分配用户
            </h4>
        </div>
        <div class="modal-body modal-scroll">
            <div class="row">
                <div id="user_message">
                    <#include "../../message.ftl"/>
                </div>
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-3 no-p">
                            <button type="button" id="assign" class="btn btn-primary btn-addon m-b-sm" onclick="listUser('assigned');">已分配</button>
                            <button type="button" id="unassign" class="btn btn-primary btn-addon m-b-sm" onclick="listUser('unassign');">未分配</button>
                        </div>
                        <div class="col-md-9 text-right no-p" id="assigned_search_l" >
                            <input type="text" name="search__loginName__like" placeholder="登录名" class="form-control display-inline" style="width:150px;">
                            <input type="text" name="search__trueName__like" placeholder="用户名" class="form-control display-inline" style="width:150px;">
                            <input type="text" name="search__email__like" placeholder="邮箱" class="form-control display-inline" style="width:150px;">
                            <button type="button" class="btn btn-primary btn-addon m-b-sm" onClick="$('#assigned_table').lightTable('search','assigned_search_l');">搜索</button>
                        </div>
                    </div>
                    <div class="panel-body statement-card">
                        <table id="assigned_table"  cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" aria-describedby="jq-datatables-example_info">
                            <thead>
                            <tr class="table_a">
                                <th><input id="checkAll" class="check-all" type="checkbox" onclick="$('#assigned_table').lightTable('bind', $(this).attr('checked'))"/></th>
                                <th name="loginName" >登录名</th>
                                <th name="trueName" >用户名</th>
                                <th name="email" >邮箱</th>
                            </tr>
                            </thead>
                            <tbody id="userTBody">
                                <#list users as user>
                                    <tr>
                                        <td><input class="check-all" type="checkbox" name="userId"  value="${user.id?c}"></td>
                                        <td>${user.loginName}</td>
                                        <td>${user.trueName!""}</td>
                                        <td>${user.email!""}</td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button id="modify" type="button" class="btn btn-primary" onclick="assignUser('assign')">分配</button>
        </div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function () {
        $('#assigned_table').lightTable({
            pagination:false
        });
        listUser('assigned');
    });

    function listUser(type){
        var data = {};
        var url = '${absoluteContextPath}/privilege/role/listUser';
        data.type = type;
        data.roleId = '${roleId?c}';
        $.customAjax({
            url:url,
            type:'post',
            data:data,
            success:function(json){
                var data = JSON.parse(json);
                if (data.returnCode == 1) {
                    $("#userTBody").html(data.content.data);
                    if(type == 'assigned'){
                        $("#assign").attr("disabled", "disabled");
                        $("#unassign").removeAttr("disabled");
                        $("#modify").text("取消分配");
                        $("#modify").attr("onClick", "assignUser('unassign')");
                    }else{
                        $("#unassign").attr("disabled", "disabled");
                        $("#assign").removeAttr("disabled");
                        $("#modify").text("分配");
                        $("#modify").attr("onClick", "assignUser('assign')");
                    }
                    $('#assigned_table').lightTable({
                        pagination:false
                    });
                    $("#checkAll").attr("checked", false);
                } else {
                    $.showMessage({message: data.returnMsg});
                }
            }
        });
    }

    function assignUser(type) {
        var parentId = '#assigned_table';
        var userIds = [];
        $(parentId).find("input[name=userId]:checked").each(function(){
            userIds.push($(this).val());
        });
        if (userIds.length < 1) {
            alertMsg("角色分配用户","请先选择用户");
            return;
        }
        $.customAjax({
            url:'${absoluteContextPath}/privilege/role/assignUser',
            type:'post',
            data:{roleId:'${roleId?c}',type:type,userId: userIds},
            traditional:true,
            success:function(json) {
                var data = JSON.parse(json);
                if(data.returnCode == 1){
                    refreshRole();
                    $.showMessage({type: 'success',parentId:'message_l',message:'分配用户成功!'});
                }else{
                    $.showMessage({parentId:'user_message',message:'分配用户失败，请联系管理员!'});
                }
            }
        });
    }
</script>

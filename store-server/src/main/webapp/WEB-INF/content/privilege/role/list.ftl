<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>角色管理</title>
    <#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <link href="${absoluteContextPath}/js/jquery/jtree/jquery.treeTable.css" rel="stylesheet" type="text/css" />
    <link href="${absoluteContextPath}/js/jquery/jtree/jquery.treetable.theme.default.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jtree/jquery.treetable.js"></script>
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function(){
            initRoleTable();
            $('#search_l').bind('submit', function (e) {
                e.preventDefault();
            });
        });

        function initRoleTable() {
            var $role = $('#table_l_role #role_table');
            $role.lightTable({
                pagination:false
            });
            $role.treetable({initialState:'expanded',expandable: true});
        }

        function popUser(roleId, roleName) {
            $.customAjax({
                url:"${absoluteContextPath}/privilege/role/popUser",
                type:"post",
                data:{roleId:roleId,roleName:roleName},
                success:function (json) {
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        var $pop_user_div = $('#pop_user_div');
                        $pop_user_div.html(data.content.data);
                        $pop_user_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message:data.returnMsg});
                    }
                }
            });
        }

        function popResource(){
            $.customAjax({
                url:"${absoluteContextPath}/privilege/role/popResource",
                type:"post",
                success:function (json) {
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        var $pop_resource_div = $('#pop_resource_div');
                        $pop_resource_div.html(data.content.data);
                        $pop_resource_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message:data.returnMsg});
                    }
                }
            });
        }

        function popRole(id,type, roleName){
            var url = '';
            if (type == 'modify') {
                url = '${absoluteContextPath}/privilege/role/toModify';
            } else {
                url = '${absoluteContextPath}/privilege/role/toAdd';
            }
            $.customAjax({
                url:url,
                type : "post",
                data : {id: id, roleName:roleName},
                success:function(json) {
                    var data = JSON.parse(json);
                    if (data.returnCode == 1){
                        var $pop_role_div = $('#pop_role_div');
                        $pop_role_div.html(data.content.data);
                        $pop_role_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({
                            message:data.returnMsg
                        })
                    }
                }
            });
        }

        function deleteRole(id, name){
            confirmDialog("删除角色", "确认删除" + name + ", 将会同时删除所有子角色!!", function() {
                $.customAjax({
                    url:'${absoluteContextPath}/privilege/role/delete?id=' + id,
                    type:'post',
                    success:function(json){
                        var data =  JSON.parse(json);
                        if(data.returnCode == 1){
                            $.showMessage({message:data.returnMsg,type:'success'});
                            refreshRole();
                        } else {
                            $.showMessage({message:data.returnMsg});
                        }
                    }
                });
            });
        }

        function refreshRole() {
            $.fn.lightRequest({
                url:'${absoluteContextPath}/privilege/role/ajaxList',
                lightId:'role',
                loading:false,
                type:'ajax',
                result:function(data){
                    initRoleTable();
                }
            });
            $("#pop_role_div").modal('hide');
            $("#pop_user_div").modal('hide');
        }

    </script>
</head>
<path><li>系统管理</li><li>角色管理</li></path>
<body>
    <div id="main-wrapper">
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-3 no-p">
                        <@sec.authorize action="role-operate">
                            <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popRole('1', 'new', 'ROLE_SUPERUSER');">添加角色</button>
                        </@sec.authorize>
                        <@sec.authorize action="resource-operate">
                            <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popResource();">权限资源上传</button>
                        </@sec.authorize>
                        </div>
                        <form class="col-md-9 text-right no-p" id="search_l" >
                            <input type="text" name="search__name__like" placeholder="角色" class="form-control display-inline" style="width: 150px;">
                            <button type="submit" class="btn btn-primary btn-addon m-b-sm" onClick="$('#role_table').lightTable('search');">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body statement-card">
                    <@light.table id="role">
                        <table id="role_table" cellpadding="0" cellspacing="0" border="0" class="table-hover display table table-bordered">
                            <thead>
                            <tr>
                                <th style="width: 300px">名称</th>
                                <th style="display: none" name="name"></th>
                                <th style="width: 200px">描述</th>
                                <th style="width: 200px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list tree as role>
                                <tr <#if role.parentId?? >data-tt-parent-id="${role.parentId?c}"</#if> data-tt-id="${role.id?c}">
                                    <td class="text-hidden" title="${role.name!""}">${role.name!""}</td>
                                    <td style="display: none">${role.name!""}</td>
                                    <td class="text-hidden" title="${role.description!""}">${role.description!""}</td>
                                    <td>
                                        <#if role.name != 'ROLE_SUPERUSER'>
                                            <@sec.authorize action="role-operate">
                                                <a href="javascript:void(0)" onclick="popRole('${role.id?c}','new','${role.name}')">添加子角色</a>
                                                &nbsp;|&nbsp;<a href="javascript:void(0)" onclick="popUser('${role.id?c}','${role.name}')">分配用户</a>&nbsp;|&nbsp;
                                                <a href="javascript:void(0)" onclick="popRole('${role.id?c}','modify','${role.name}')">修改</a>&nbsp;|&nbsp;
                                                <a href="javascript:void(0)" onclick="deleteRole('${role.id?c}','${role.name}')">删除角色</a>
                                            </@sec.authorize>
                                        </#if>
                                    </td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </@light.table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade in" tabindex="-1" role="dialog"  id="pop_user_div" aria-hidden="false"></div>
    <div class="modal fade in" tabindex="-1" role="dialog" id="pop_role_div" ria-hidden="false"></div>
    <div class="modal fade in" tabindex="-1" role="dialog" id="pop_resource_div" aria-hidden="false"></div>
</body>
</html>
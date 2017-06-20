<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>用户管理</title>
    <#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <script type="text/javascript">
        $(document).ready(function(){
            initData();
            $('#search_l').bind('submit', function (e) {
                e.preventDefault();
            });
        });
        function initData(){
            $('#user_table').lightTable({
                pageUrl: '${absoluteContextPath}/page',
                pageId: '${page.pageId}',
                pageSize:${page.pageSize}
            });

            Page.registerToPage(function (pageId, pageNo, pageSize) {
                $('#user_table').lightTable('toPage', pageId, pageNo, pageSize);
            }, '${page.pageId}');
        }
        function popAddOrModify(id){
            var data = {};
            var url = '${absoluteContextPath}/privilege/user/';
            if (id != undefined) {
                data.id = id;
                url = url + "/toModify";
            } else {
                url = url + "/toAdd";
            }
            $.customAjax({
                url:url,
                type:'post',
                data:data,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        var $pop_user_div = $('#pop_user_div');
                        $pop_user_div.html(data.content.data);
                        $pop_user_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function toModifyPassword(id){
            $.customAjax({
                url:'${absoluteContextPath}/privilege/user/toModifyPassword',
                type:'post',
                data:{
                    id : id
                },
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        var $pop_user_div = $('#pop_user_div');
                        $pop_user_div.html(data.content.data);
                        $pop_user_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function refresh() {
            $.fn.lightRequest({
                url:'${absoluteContextPath}/privilege/user/ajaxList',
                lightId:'user',
                loading:false,
                type:'ajax',
                result:function(){
                    initData();
                }
            });
            $('#pop_user_div').modal('hide');
        }

        function showConfirm(id){
            confirmDialog("删除用户", "确定删除？", function() {
                deleteData(id);
            });
        }

        function deleteData(id){
            var data = {};
            data.id = id;
            var url = '${absoluteContextPath}/privilege/user/del';
            $.customAjax({
                url:url,
                type:'post',
                data:data,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        refresh();
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function toOperationLog(name) {
            window.location.href = "${absoluteContextPath}/operationLog/list?name=" + name;
        }

    </script>
</head>
<path><li>系统管理</li><li>用户管理</li></path>
<body>
    <div id="main-wrapper">
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-1 no-p">
                            <@sec.authorize action="user-operate">
                                <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popAddOrModify();">添加用户</button>
                            </@sec.authorize>
                        </div>
                        <form class="col-md-11 text-right no-p" id="search_l" >
                            <input type="text" name="search__loginName__like" placeholder="登录名" class="form-control display-inline" style="width: 150px;">
                            <input type="text" name="search__trueName__like" placeholder="用户名" class="form-control display-inline" style="width: 150px;">
                            <input type="text" name="search__email__like" placeholder="邮箱" class="form-control display-inline" style="width: 150px;">
                            <select id="userStatus" name="search__status__eq" class="form-control display-inline" style="width: 150px;">
                                <option value="">请选择状态</option>
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                            <button type="submit" class="btn btn-primary btn-addon m-b-sm" onClick="$('#user_table').lightTable('search');">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body statement-card">
                        <@light.table id="user">
                            <table id="user_table" cellpadding="0" cellspacing="0" border="0" class="table-hover display table table-bordered">
                                <thead>
                                    <tr>
                                        <th name="loginName" class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 150px;">登录名</th>
                                        <th name="trueName" class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 150px;">用户名</th>
                                        <th name="email" class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 100px;">邮箱</th>
                                        <th name="status"class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 100px;">状态</th>
                                        <th class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 200px;">创建时间</th>
                                        <th class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 200px;">最后一次登录时间</th>
                                        <th class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 200px;">操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <#list page.result as user>
                                        <tr>
                                            <td class="text-hidden" title="${user.loginName}">${user.loginName}</td>
                                            <td class="text-hidden" title="${user.trueName!""}">${user.trueName!""}</td>
                                            <td class="text-hidden" title="${user.email!""}">${user.email!""}</td>
                                            <td value="${user.status?c}">${(user.status==1)?string("启用","禁用")}</td>
                                            <td>${(user.createTime)?string("yyyy-MM-dd HH:mm:ss")}</td>
                                            <td>
                                                <#if user.lastLoginTime??>
                                                ${(user.lastLoginTime)?string("yyyy-MM-dd HH:mm:ss")}
                                                <#else>
                                                    未登录
                                                </#if>
                                            </td>
                                            <td class="center">
                                                <@sec.authorize action="user-operate">
                                                    <a href="javascript:void(0)" onclick="popAddOrModify(${user.id?c});">编辑</a>
                                                    <a href="javascript:void(0)" onclick="toModifyPassword(${user.id?c})">修改密码</a>
                                                </@sec.authorize>
                                                <#if currentUser.id != user.id>
                                                    <@sec.authorize action="user-operate">
                                                        <a href="javascript:void(0)" onclick="showConfirm(${user.id?c});">删除</a>
                                                    </@sec.authorize>
                                                </#if>
                                            </td>
                                        </tr>
                                    </#list>
                                </tbody>
                            </table>
                            <div class="table-footer clearfix">
                                <#include "../../page.ftl"/>
                            </div>
                        </@light.table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div aria-hidden="false" id="pop_user_div" class="modal fade" tabindex="-1" role="dialog"></div>
</body>
</html>
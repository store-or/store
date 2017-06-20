<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<div class="modal-dialog modal-lg">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">
            <#if role.id??>
                修改角色：${role.name!""}
            <#else>
                添加角色
            </#if>
            </h4>
        </div>
        <div class="modal-body modal-scroll">
            <div class="col-sm-10">
                <form id="roleForm" class="form-horizontal" method="post" action="${absoluteContextPath}/privilege/role/<#if role.id??>modify<#else>save</#if>">
                    <div id="modify_role_message">
                    <#include "../../message.ftl"/>
                    </div>
                <#if role.id??>
                    <input type="hidden" name="id" value="${role.id?c}">
                <#else>
                    <input type="hidden" name="parentPath" value="${role.parentPath}">
                </#if>
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label"><span class='text-danger'>* </span>名称:</label>
                        <div class="col-sm-10">
                            <input id="name" type="text" name="name" class="form-control" value="${role.name!""}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-sm-2 control-label">描述:</label>
                        <div class="col-sm-10">
                            <input id="description" type="text" name="description" class="form-control" value="${role.description!""}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-sm-2 control-label">权限资源:</label>
                        <div class="col-sm-10" id="actionDiv">
                            <#if 1 < size>
                            <table id="action_table"  cellpadding="0" cellspacing="0" border="0"
                                   class="display table table-bordered table-hover" aria-describedby="jq-datatables-example_info">
                                <tbody>
                                <#list tree as action>
                                    <#if action_index gt 0 >
                                        <#if action.parentName != "/">
                                            <tr data-tt-parent-id="${action.parentName}" data-tt-id="${action.name}" class="child-of-tr-${action.parentName}" id="tr-${action.name}">
                                                <td><input class="check-all child-of-ac_ck_${action.parentName}" id="ac_ck_${action.name}"  type="checkbox" <#if action.assign>checked="true"</#if>  name="action" value="${action.name}"/>${action.description!""}</td>
                                                <td>${action.name!""}</td>
                                            </tr>
                                        <#else>
                                            <tr data-tt-id="${action.name}" class="ignore" id="tr-${action.name}">
                                                <td><input class="check-all" style="float: none"  id="ac_ck_${action.name}"  type="checkbox"  /><span style="color:olive">${action.description!""}</span></td>
                                                <td></td>
                                            </tr>
                                        </#if>
                                    </#if>
                                </#list>
                                </tbody>
                            </table>
                            <#elseif roleId == 1>
                                <@sec.authorize action="resource-operate">
                                    <span class="name_text">暂无权限功能，请先<a href="javascript:void(0)" onclick="popResource();">上传</a></span>
                                </@sec.authorize>
                            <#else>
                                <span class="name_text">请先分配当前角色的权限</span>
                            </#if>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" onclick="modify()"><#if role.id??>确认修改<#else>确认添加</#if></button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $().ready(function(){
        var $action_table = $("#actionDiv #action_table");
        $action_table.lightTable({
            pagination:false
        });
        $action_table.bindCheckbox();
        $action_table.treetable(
                {initialState:'expanded',expandable: true}
        );
    });

    function modify() {
        $('#roleForm').customAjaxSubmit({
            success:function(json){
                $('#roleForm').submitResult(json,{
                    messageParentId:'modify_role_message',
                    success:function(data){
                        refreshRole();
                    }
                })
            }
        })
    }

</script>

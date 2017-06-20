<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<table cellpadding="0" cellspacing="0" border="0" class="display table table-bordered">
    <thead>
    <tr>
        <th name="loginName" class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 800px;">分类名称</th>
        <th class="sorting" tabindex="0" rowspan="1" colspan="1" style="width: 120px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list classifies as classify>
    <tr classifyId="${classify.id?c}">
        <td>${classify.name}</td>
        <td>
            <div class="op-column">
                <@sec.authorize action="classify-operate">
                    <button type="button" onclick="popAddOrModify(${classify.id?c});" class="btn  btn-xs" title="编辑" ><span class="fa fa-pencil"></span> </button>
                </@sec.authorize>
                <@sec.authorize action="classify-del">
                    <button type="button" onclick="del(${classify.id?c});" class="btn  btn-xs" title="删除" ><span class="fa fa-trash-o"></span></button>
                </@sec.authorize>
            </div>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
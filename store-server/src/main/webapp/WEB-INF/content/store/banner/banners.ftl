<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<#import "../../display.ftl" as display/>
<table cellpadding="0" cellspacing="0" border="0" class="display table table-bordered">
    <thead>
    <tr>
        <th name="loginName" style="width: 300px;">名称</th>
        <th style="width: 100px;">图片</th>
        <th style="width: 300px;">链接</th>
        <th style="width: 60px;">banner位</th>
        <th class="sorting" style="width: 120px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list banners as banner>
    <tr bannerId="${banner.id?c}">
        <td>${banner.name}</td>
        <td><img style="max-width: 100px;max-height: 100px;" src="<#if (banner.poster)?index_of('http')==-1>${uploadFileContext}/</#if>${banner.poster}"/></td>
        <td class="text-hidden" title="${(banner.link!"")?xhtml}"><#if banner.link?? && banner.link != ""><a target="blank" href="${(banner.link!"")?xhtml}">${(banner.link!"")?xhtml}</a></#if></td>
        <td><@display.enum BannerType.values() banner.type!/></td>
        <td>
            <div class="op-column">
                <@sec.authorize action="banner-operate">
                    <button type="button" onclick="popAddOrModify(${banner.id?c});" class="btn  btn-xs" title="编辑" ><span class="fa fa-pencil"></span> </button>
                </@sec.authorize>
                <@sec.authorize action="classify-del">
                    <button type="button" onclick="del(${banner.id?c});" class="btn  btn-xs" title="删除" ><span class="fa fa-trash-o"></span></button>
                </@sec.authorize>
            </div>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
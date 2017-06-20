<#import "light.ftl" as light>
<@light.table id=page.pageId!"page">
<input type="hidden" style="display: none" id="pageNo" value="${(page.pageNo!1)?c}" />
<input type="hidden" style="display: none" id="totalCount" value="${(page.totalCount!0)?c}" />
<input type="hidden" style="display: none" id="autoCount" value="${page.autoCount? string("true", "false")}" />
    <#if !page.autoCount && (!page.result?? || page.result?size lte 0)>
        <input type="hidden" style="display: none" id="pageSize" value="${(page.pageSize - 1)?c}" />
    <#else>
        <#if (!page.autoCount && page.totalCount gt 0) || page.autoCount>
        <!-- 没有满足记录、不计算总条数且总条数大于0、计算总条数 -->
        <input type="hidden" style="display: none" id="pageSize" value="${(page.pageSize)?c}" />
            <#include "static_page.ftl"/>
        <#else>
        <input type="hidden" style="display: none" id="pageSize" value="${(page.pageSize - 1)?c}" />
            <#include "dynamic_page.ftl"/>
        </#if>
    </#if>
</@light.table>

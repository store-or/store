<#macro thSortSpan name="" orderBy="" order="">
    <#if orderBy == name && orderBy != "">
        <span class="inline">
            <#if ("," + order + ",")?contains(",asc,")>
                 <em class="arrow-up m-l-xxs"></em>
            <#else>
                 <em class="arrow-down m-l-xxs"></em>
            </#if>
        </span>
    </#if>
</#macro>
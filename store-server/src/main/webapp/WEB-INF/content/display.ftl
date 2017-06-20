<#macro enum list name>
    <#if name??>
        <#list list as tmp>
            <#if tmp==name>${tmp.alias}</#if>
        </#list>
    </#if>
</#macro>
<#macro numberToTime unix=-1 format="yyyy-MM-dd HH:mm:ss">
    <#if unix?? && unix gt 0>${(unix?number_to_datetime)?string(format)}</#if>
</#macro>
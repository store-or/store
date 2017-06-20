<#macro productClassify product>
    <#if product.classifies??><#list product.classifies as classify>${classify.name!""}<#if classify_has_next>，</#if></#list></#if>
</#macro>
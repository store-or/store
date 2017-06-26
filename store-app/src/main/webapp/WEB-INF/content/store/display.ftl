<#macro pictureUrl url="">
    <#if url!="">
        <#if !url?starts_with("http")>${uploadFileContext}</#if>${url}
    </#if>
</#macro>

<#macro banner banners>
    <ul class="bxslider" style="height:500px;">
        <#if banners?? && banners?size gt 0>
            <#list banners as tmp>
                <li>
                    <a href="<#if tmp.link?? && tmp.link!="">${tmp.link}<#else>javascript:void(0)</#if>">
                        <img src="<@pictureUrl tmp.poster!''/>"/>
                    </a>
                </li>
            </#list>
        <#else>
            <li><img /></li>
        </#if>
    </ul>
</#macro>
<#macro enum list name>
    <#if name??>
        <#list list as tmp>
            <#if tmp==name>${tmp.alias}</#if>
        </#list>
    </#if>
</#macro>

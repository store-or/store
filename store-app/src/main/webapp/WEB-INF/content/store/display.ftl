<#macro pictureUrl url="">
    <#if url!="">
        <#if !url?starts_with("http")>${uploadFileContext}</#if>${url}
    </#if>
</#macro>

<#macro banner banners>
    <div class='o-sliderContainer' id="bannerParent" style="margin-top:0;">
        <div class='o-slider' id='banners'>
            <#if banners?? && banners?size gt 0>
                <#list banners as tmp>
                    <div class="o-slider--item" data-image="http://10.8.72.39:8080/store/img/store/20170630165516601.jpg" <#if tmp.link?? && tmp.link!="">onclick="location.href='${tmp.link}'"</#if>></div>
                </#list>
            </#if>
        </div>
    </div>
    <div style="clear: both"></div>
</#macro>
<#macro enum list name>
    <#if name??>
        <#list list as tmp>
            <#if tmp==name>${tmp.alias}</#if>
        </#list>
    </#if>
</#macro>

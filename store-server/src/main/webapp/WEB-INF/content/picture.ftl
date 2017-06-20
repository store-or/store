<#macro picture name="" url="" width=90 height=90>
    <script>
        function select${name}Callback(url){
            $("#${name}Icon").attr("src" , "${uploadFileContext}" + url);
            $("input[name='${name}']").val(url);
        }
    </script>
    <div class="col-lg-9 media">
        <input type="hidden" id="${name}" name="${name}" value="${url!""}" style="display: none"/>
        <div class="pull-img" style="cursor: pointer"  onclick="choosePic('${url!""}' , 'select${name}Callback')">
            <#assign imgUrl="${absoluteContextPath}/img/add_img.png"/>
            <#if url?? && url?length gt 0>
                <#assign imgUrl="${uploadFileContext}${(url)?xhtml}"/>
                <#if url?index_of("http") == 0>
                    <#assign imgUrl="${url}"/>
                <#else>
                    <#assign imgUrl="${uploadFileContext}${url}"/>
                </#if>
            </#if>
            <img src="${imgUrl}" id="${name}Icon" style="width: ${width}px; height: ${height}px">
        </div>
    </div>
</#macro>
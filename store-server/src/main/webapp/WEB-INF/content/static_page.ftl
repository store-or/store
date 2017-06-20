
<#if page.totalPage gt 0 >
    <nav role="navigation">
        <span>
            <ul class="cd-pagination fr " style="float:right">
                <li>总共<span>${page.totalCount?c}</span>记录.&nbsp;${page.pageNo} / ${page.totalPage}</li>
                <#if page.pageNo == 1>
                    <li class="button"><a href="javascript:void(0)" class="disabled">首页</a></li>
                    <li class="button"><a class="disabled" href="javascript:void(0)">上一页</a></li>
                <#else>
                    <li class="button"><a href="javascript:void(0)" onclick="new Page('${page.pageId}',1, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">首页</a></li>
                    <li class="button"><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${(page.pageNo -1)?c }, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">上一页</a></li>
                </#if>
                <#if page.pageNo-1 <=1 && (page.totalPage <= page.pageNo + 1)>
                    <#list 1.. page.totalPage as index>
                        <#if page.pageNo == index>
                            <li><a href="javascript:void(0)" class="current">${index}</a></li>
                        <#else>
                            <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${index?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${index}</a></li>
                        </#if>
                    </#list>
                <#elseif page.pageNo -1 <= 1 && page.totalPage gt page.pageNo + 1>
                    <#list 1.. (page.pageNo + 1) as index>
                        <#if page.pageNo == index>
                            <li><a href="javascript:void(0)" class="current">${index}</a></li>
                        <#else>
                            <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${index?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${index}</a></li>
                        </#if>
                    </#list>
                    <li><span>...</span></li>
                    <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${page.totalPage?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${page.totalPage}</a></li>
                <#elseif page.pageNo -1 gt 1 && page.totalPage <=page.pageNo + 1>
                    <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',1, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">1</a></li>
                    <li><span>...</span></li>
                    <#list (page.pageNo -1).. page.totalPage as index>
                        <#if page.pageNo == index>
                            <li><a class="current" href="javascript:void(0)">${index}</a></li>
                        <#else>
                            <li ><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${index?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${index}</a></li>
                        </#if>
                    </#list>
                <#else>
                    <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',1, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">1</a></li>
                    <li><span>...</span></li>
                    <#list (page.pageNo -1).. (page.pageNo + 1) as index>
                        <#if page.pageNo == index>
                            <li"><a class="current" href="javascript:void(0)">${index}</a></li>
                        <#else>
                            <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${index?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${index}</a></li>
                        </#if>
                    </#list>
                    <li><span>...</span></li>
                    <li><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${page.totalPage?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">${page.totalPage}</a></li>
                </#if>
                <#if page.pageNo == page.totalPage>
                    <li class="button" ><a href="javascript:void(0)" class="disabled">下一页</a></li>
                    <li class="button" ><a href="javascript:void(0)" class="disabled">末页</a></li>
                <#else>
                    <li ><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${(page.pageNo +1)?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">下一页</a></li>
                    <li class="button"><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${page.totalPage?c}, ${page.pageSize?c}, true, false, ${page.totalCount?c}).toPage();">末页</a></li>
                </#if>
            </ul>
            <div class="fr">
                <select class="form-control" onchange="new Page('${page.pageId}',1,$(this).val(), true, false, ${page.totalCount?c}).toPage()" >
                    <option value="5" <#if page.pageSize == 5>selected="true" </#if>>5</option>
                    <option value="10" <#if page.pageSize == 10>selected="true" </#if>>10</option>
                    <option value="15" <#if page.pageSize == 15>selected="true" </#if>>15</option>
                    <option value="20" <#if page.pageSize == 20>selected="true" </#if>>20</option>
                    <option value="30" <#if page.pageSize == 30>selected="true" </#if>>30</option>
                    <option value="50" <#if page.pageSize == 50>selected="true" </#if>>50</option>
                </select>
            </div>

        </span>

    </nav>
</#if>
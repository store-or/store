<nav role="navigation">
    <span>
        <ul class="cd-pagination fr " style="float:right">
            <li>...</li>
            <#if page.pageNo == 1>
                <li class="button"><a class="disabled" href="javascript:void(0)">上一页</a></li>
            <#else>
                <li class="button"><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${(page.pageNo -1)?c }, ${(page.pageSize -1)?c}, true, false, 0).toPage();">上一页</a></li>
            </#if>
            <#if page.result?size gt page.pageSize-1>
                <li ><a href="javascript:void(0)" onclick="new Page('${page.pageId}',${(page.pageNo +1)?c}, ${(page.pageSize - 1)?c}, true, false, 0).toPage();">下一页</a></li>
            <#else>
                <li class="button" ><a href="javascript:void(0)" class="disabled">下一页</a></li>
            </#if>
        </ul>
        <div class="fr">
            <select class="form-control"  onchange="new Page('${page.pageId}',1,parseInt($(this).val()), true, false, 0).toPage()" >
                <option value="5" <#if page.pageSize - 1== 5>selected="true" </#if>>5</option>
                <option value="10" <#if page.pageSize - 1== 10>selected="true" </#if>>10</option>
                <option value="15" <#if page.pageSize - 1== 15>selected="true" </#if>>15</option>
                <option value="20" <#if page.pageSize - 1== 20>selected="true" </#if>>20</option>
                <option value="30" <#if page.pageSize - 1== 30>selected="true" </#if>>30</option>
                <option value="50" <#if page.pageSize - 1 == 50>selected="true" </#if>>50</option>
            </select>
        </div>
    </span>
</nav>
<script>
    $().ready(function() {
        new Page("${page.pageId}", ${page.pageNo?c}, ${(page.pageSize - 1)?c}, false, true, 0).toPage();
    });
</script>
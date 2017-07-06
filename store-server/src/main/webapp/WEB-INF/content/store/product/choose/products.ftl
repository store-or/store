<#import "../../../th_sort_span.ftl" as sortth>
<#import "../productClassify.ftl" as productClassify/>
<#import "../../../display.ftl" as display>
<table id="chooseProduct_table" cellpadding="0" cellspacing="0" border="0" class="table-hover display table table-bordered">
    <thead>
    <th style="width:50px">选择</th>
    <th style="width:150px">产品名称</th>
    <th style="width:100px">分类</th>
    <th style="width:120px" class="sort" title="点击排序"  onclick="theadClick('chooseProductSearch', this, '${page.pageId}', parseInt($('#table_l_${page.pageId} #pageSize').val()));" name="modifyTime">
        更新时间
        <@sortth.thSortSpan "modifyTime" page.orderBy page.order/>
    </th>
    </thead>
    <tbody>
    <#if page.result??>
        <#list page.result as product>
        <tr>
            <td>
                <#if type?? && type==1>
                    <input type="radio" name="chooseProductId" value="${product.id?c}"/>
                <#else>
                    <input type="checkbox" name="chooseProductId" value="${product.id?c}"/>
                </#if>
            </td>
            <td class="text-hidden" title="${(product.name!"")?xhtml}">${(product.name!"")?xhtml}</td>
            <td class="text-hidden" title="<@productClassify.productClassify product/>"><@productClassify.productClassify product/></td>
            <td><@display.numberToTime product.modifyTime!-1/></td>
        </tr>
        </#list>
    </#if>

    </tbody>
</table>
<div class="table-footer clearfix">
    <#include "../../../page.ftl"/>
</div>
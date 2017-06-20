<#import "../../display.ftl" as display>
<#import "../../th_sort_span.ftl" as sortth>
<#import "productClassify.ftl" as productClassify/>
<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<table id="product_table" class="display table table-bordered table-hover" style="width: 100%; cellspacing: 0;">
    <thead>
    <tr>
        <th class="th-sortable" data-toggle="class" name="name"
            style="cursor:pointer; display: inline-blcok;width: 300px;" onclick="theadClick('search_form', this, '${page.pageId}', parseInt($('#table_l_${page.pageId} #pageSize').val()));"
            title="点击排序">
            产品名称
            <@sortth.thSortSpan "name" page.orderBy page.order/>
        </th>
        <th style="width: 200px;">分类</th>
        <th style="width: 80px;">标签</th>
        <th class="th-sortable" data-toggle="class" name="modifyTime"
            style="cursor:pointer; display: inline-blcok;width: 180px;" onclick="theadClick('search_form', this, '${page.pageId}', parseInt($('#table_l_${page.pageId} #pageSize').val()));"
            title="点击排序">
            更新时间
            <@sortth.thSortSpan "modifyTime" page.orderBy page.order/>
        </th>
        <th class="th-sortable" data-toggle="class" name="topTime,createTime"
            style="cursor:pointer; display: inline-blcok;width: 180px;" onclick="theadClick('search_form', this, '${page.pageId}', parseInt($('#table_l_${page.pageId} #pageSize').val()));"
            title="点击排序">
            置顶时间
            <@sortth.thSortSpan "topTime,createTime" page.orderBy page.order/>
        </th>
        <th style="width:100px;">状态</th>
        <th style="width: 300px;">操作</th>
    </tr>
    </thead>
    <tbody>
        <#list page.result as product>
            <tr>
                <td>${product.name!""}</td>
                <td><@productClassify.productClassify product/></td>
                <td><@display.enum ProductTag.values() product.tag!/></td>
                <td><@display.numberToTime product.modifyTime/></td>
                <td><@display.numberToTime product.topTime/></td>
                <td><#if ProductStatus.ON.value.equals(product.status)><em class="put-on" title="上架"></em><#else><em class="put-off" title="下架"></em></#if></td>
                <td>
                    <div class="op-column">
                        <@sec.authorize action="product-operate">
                            <a style="cursor:pointer;" onclick="popAddOrModify(${product.id?c});" href="javascript:void(0)">编辑</a>
                            <span style="color:#999;">|</span>
                            <#if ProductStatus.ON.value.equals(product.status)>
                                <a style="cursor:pointer;" onclick="postRequest(${product.id?c}, 'putOff');" href="javascript:void(0)">下架</a>
                            <#else>
                                <a style="cursor:pointer;" onclick="postRequest(${product.id?c}, 'putOn');" href="javascript:void(0)">上架</a>
                            </#if>
                            <span style="color:#999;">|</span>
                            <#if product.topTime??>
                                <a style="cursor:pointer;" onclick="postRequest(${product.id?c}, 'unSetTop');" href="javascript:void(0)">取消置顶</a>
                            <#else>
                                <a style="cursor:pointer;" onclick="postRequest(${product.id?c}, 'setTop');" href="javascript:void(0)">置顶</a>
                            </#if>
                        </@sec.authorize>
                        <span style="color:#999;">|</span>
                        <a style="cursor:pointer;" href="javascript:void(0)">预览</a>
                        <@sec.authorize action="product-del">
                            <span style="color:#999;">|</span>
                            <a style="cursor:pointer;" onclick="del(${product.id?c});">删除</a>
                        </@sec.authorize>
                    </div>
                </td>
            </tr>
        </#list>
    </tbody>
</table>
<div class="table-footer clearfix">
<#include "../../page.ftl"/>
</div>
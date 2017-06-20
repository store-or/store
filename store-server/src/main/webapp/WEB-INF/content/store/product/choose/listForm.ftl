<#import "../../../light.ftl" as light>
<script>
    $().ready(function(){
        Page.registerToPage(function (pageId, pageNo, pageSize) {
            refreshChooseProducts(pageNo, pageSize);
        }, '${page.pageId}');
        $('#chooseProductSearch').disableAutoSubmit();
    });
    function refreshChooseProducts(pageNo, pageSize) {
        $('#chooseProductSearch').lightRequest({
            prefix:'search__',
            url:'${absoluteContextPath}/product/ajaxChooseProducts',
            lightId:'chooseProduct',
            params:[
                {name : "condition", value : '${(condition!"")}'}
            ],
            pageId : '${page.pageId}',
            pageNo: pageNo ? pageNo : ${page.pageNo?c},
            pageSize: pageSize ? pageSize : ${page.pageSize?c},
            type:'ajax'
        });
    }
</script>
<div class="modal-dialog modal-lg" style="width:850px;">
    <div class="modal-content" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">选择产品</h4>
        </div>
        <div class="modal-body modal-scroll" style="height:550px;">
            <div id="videoLib_message">
            <#include "../../../message.ftl"/>
            </div>
            <div class="row">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <form class="col-md-12 no-p" id="chooseProductSearch">
                            <select class="form-control md-2 display-inline" name="search__classifyIds__like">
                                <option value="">分类</option>
                                <#if classifies??>
                                    <#list classifies as classify>
                                        <option value=",${classify.id?c},">${classify.name}</option>
                                    </#list>
                                </#if>
                            </select>
                            <input type="hidden" id="order_l" class="order_l" value="${page.order!"desc"}"/>
                            <input type="hidden" id="orderBy_l" class="orderBy_l" value="${page.orderBy!"video.modifyTime"}"/>
                            <input placeholder="产品名称" type="text" name="search__name__like" class="form-control display-inline md-2"/>
                            <button type="submit" class="btn btn-primary btn-addon m-b-sm" onclick="new Page('${page.pageId}', 1, parseInt($('#table_l_${page.pageId} #pageSize').val())).toPage();">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body statement-card">
                    <@light.table id="chooseProduct">
                            <#include "products.ftl"/>
                        </@light.table>
                    </div>
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" id="ok">确认</button>
        </div>
    </div>
    <!-- / .modal-content -->
</div>
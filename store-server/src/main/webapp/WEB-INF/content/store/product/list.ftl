<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>产品管理</title>
    <#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <script type="text/javascript">
        $(document).ready(function(){
            $('#search_form').disableAutoSubmit();
            initSearch();
        });
        function initSearch(){
            Page.registerToPage(function (pageId, pageNo, pageSize) {
                refresh(pageNo, pageSize, true);
            }, '${page.pageId}');
        }

        function refresh(pageNo, pageSize, loading) {
            $("#search_form").lightRequest({
                prefix:'search__',
                url:'${absoluteContextPath}/product/ajaxList',
                lightId:'product',
                loading:loading,
                pageId : '${page.pageId}',
                pageNo: pageNo ? pageNo : ${page.pageNo?c},
                pageSize: pageSize ? pageSize : ${page.pageSize?c},
                type:'ajax'
            });
        }

        function popAddOrModify(id){
            window.open("${absoluteContextPath}/product/" + (id == undefined ? 'toAdd' : 'toModify?id='+id), 'blank');
        }

        function postRequest(id, url) {
            var data = {};
            data.id = id;
            $.customAjax({
                url: '${absoluteContextPath}/product/' + url,
                type: 'post',
                data: data,
                success:function(json){
                    var data = JSON.parse(json);
                    toPage('${page.pageId}', false);
                    if (data.returnCode == 1) {
                        $.showMessage({type:"success",message: data.returnMsg});
                    }else{
                        $.showMessage({type:"error",message: data.returnMsg});
                    }

                }
            });
        }

        function del(id) {
            confirmDialog("删除产品", "是否确认删除?", function () {
                var data = {};
                data.id = id;
                $.customAjax({
                    url: '${absoluteContextPath}/classify/del',
                    type: 'post',
                    data: data,
                    success:function(json){
                        var data = JSON.parse(json);
                        toPage('${page.pageId}', true);
                        if (data.returnCode == 1) {
                            $.showMessage({type:"success",message: data.returnMsg});
                        }else{
                            $.showMessage({type:"error",message: data.returnMsg});
                        }

                    }
                });
            });
        }

    </script>
</head>
<path><li>产品管理</li><li>产品管理</li></path>
<body>
<div class="page-inner">
    <div id="main-wrapper">
        <div class="row tab_demo">
            <div class="col-md-12">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-3 no-p">
                            <@sec.authorize action="product-operate">
                                <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popAddOrModify();">添加产品</button>
                            </@sec.authorize>
                        </div>
                        <div class="col-md-9  text-right  no-p">
                            <form id="search_form">
                                <select class="form-control md-2 display-inline" name="search__classifyIds__like">
                                    <option value="">分类</option>
                                    <#if classifies??>
                                        <#list classifies as classify>
                                            <option value=",${classify.id?c},">${classify.name}</option>
                                        </#list>
                                    </#if>
                                </select>
                                <select  class="form-control md-2 display-inline" name="search__tag__eq">
                                    <option value="">标签</option>
                                    <#list ProductTag.values() as tag>
                                        <option value="${tag.name()!""}" >${tag.alias!""}</option>
                                    </#list>
                                </select>
                                <select  class="form-control md-2 display-inline" parse="int" name="search__status__eq">
                                    <option value="">状态</option>
                                    <#list ProductStatus.values() as status>
                                        <option value="${status.value}" >${status.alias}</option>
                                    </#list>
                                </select>
                                <input type="text" name="search__name__like" class="form-control md-2 display-inline" placeholder="产品名称">
                                <input type="hidden" id="order_l" class="order_l" name="order" value="${page.order!""}"/>
                                <input type="hidden" id="orderBy_l" class="orderBy_l" name="orderBy" value="${page.orderBy!""}"/>
                                <button  class="btn btn-primary btn-addon m-b-sm" onclick="new Page('${page.pageId}', 1, $('#table_l_${page.pageId} #pageSize').val()).toPage()">搜索</button>
                            </form>
                        </div>
                    </div>
                    <div class="panel-body statement-card">
                        <@light.table id="product">
                            <#include "products.ftl"/>
                        </@light.table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" id="pop_classify_div" class="modal fade in" tabindex="-1" role="dialog"></div>
</body>
</html>
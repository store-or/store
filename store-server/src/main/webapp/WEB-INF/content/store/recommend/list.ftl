<html xmlns="http://www.w3.org/1999/html">
<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<head>
<meta charset="utf-8">
<title>精品推荐</title>
    <link href="${absoluteContextPath}/css/product-preview-slider/style.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${absoluteContextPath}/js/drag/jquery.dad.css">
    <script type="text/javascript" src="${absoluteContextPath}/js/drag/jquery.dad.min.js"></script>
<script>
    function toAdd() {
        $("#pop_chooseProduct_div").chooseProduct({
            url: "${absoluteContextPath}/product/toChoose",
            data: {
                condition: $("#condition").val()
            },
            success: function(productId) {
                $.customAjax({
                    url: '${absoluteContextPath}/recommend/save',
                    type: 'post',
                    data: {
                        "product.id": productId
                    },
                    success:function(json){
                        var data = JSON.parse(json);
                        refresh();
                        if (data.returnCode == 1) {
                            $.showMessage({type:"success",message: data.returnMsg});
                        }else{
                            $.showMessage({type:"error",message: data.returnMsg});
                        }

                    }
                });
            }
        });
    }

    function refresh() {
        $.customAjax({
            url:'${absoluteContextPath}/recommend/ajaxList',
            type:'get',
            loading:true,
            success:function(json){
                var data = JSON.parse(json);
                if (data.returnCode == 1) {
                    $('#recommends').html(data.content.data);
                } else {
                    $.showMessage({message: data.returnMsg});
                }
            }
        });
    }

    function del(id) {
        confirmDialog("删除推荐", "是否确认删除?", function () {
            var data = {};
            data.id = id;
            $.customAjax({
                url: '${absoluteContextPath}/recommend/del',
                type: 'post',
                data: data,
                success:function(json){
                    var data = JSON.parse(json);
                    refresh();
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
<path>
    <li>前端编排</li>
    <li>精品推荐</li>
</path>
<body class="compact-menu page-header-fixed  pace-done">
<div class="page-inner">
    <div id="main-wrapper" class="panel-white p-h-sm p-v-xs">
        <div>
            <div class="tab-content no-p">
                <div class="row m-t-md ">
                    <div class="col-md-12 layout-content">
                        <div class="row la">
                            <div class="col-md-12">
                                <@sec.authorize action="recommend-operate">
                                    <a class="m-b-xxs btn btn-primary  m-b-xs" data-toggle="modal" data-target="#myModal" onclick="toAdd()">
                                        <span aria-hidden="true" class="icon-plus m-r-xxs"></span>添加推荐
                                    </a>
                                </@sec.authorize>
                                <div id="recommends">
                                    <#include "recommends.ftl"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" id="pop_chooseProduct_div" class="modal fade in" tabindex="-1" role="dialog"></div>
</body>
</html>

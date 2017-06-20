<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>banner管理</title>
    <#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <script type="text/javascript">
        $().ready(function() {
            initBannerTable();
        });
        function initBannerTable() {
            $("#banners table").tableDrag({
                callback : function (row, from, to) {
                    updateIndex($(row).attr("bannerId"), to);
                }
            });
        }

        function updateIndex(id, index) {
            var url = '${absoluteContextPath}/banner/modifyIndex';
            $.customAjax({
                url:url,
                type:'post',
                data:{
                    index: index,
                    id: id
                },
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        refresh();
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function refresh() {
            $.customAjax({
                url:'${absoluteContextPath}/banner/ajaxList',
                type:'get',
                loading:true,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        $('#banners').html(data.content.data);
                        initBannerTable();
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function popAddOrModify(id){
            var data = {};
            var url = '${absoluteContextPath}/banner/';
            if (id != undefined) {
                data.id = id;
                url = url + "/toModify";
            } else {
                url = url + "/toAdd";
            }
            $.customAjax({
                url:url,
                type:'post',
                data:data,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        var $pop_app_div = $('#pop_banner_div');
                        $pop_app_div.html(data.content.data);
                        $pop_app_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }


        function del(id) {
            confirmDialog("删除banner", "是否删除?", function() {
                var data = {};
                data.id = id;
                $.customAjax({
                    url: '${absoluteContextPath}/banner/del',
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
<path><li>前端编排</li><li>banner管理</li></path>
<body>
<div class="page-inner">
    <div id="main-wrapper">
        <div class="row tab_demo">
            <div class="col-md-12">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-1 no-p">
                        <@sec.authorize action="banner-operate">
                            <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popAddOrModify();">添加banner</button>
                        </@sec.authorize>
                        </div>
                    </div>
                    <div class="panel-body statement-card" id="banners">
                        <#include "banners.ftl"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" id="pop_banner_div" class="modal fade in" tabindex="-1" role="dialog"></div>
</body>
</html>
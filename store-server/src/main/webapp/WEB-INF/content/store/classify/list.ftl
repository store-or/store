<#import "../../light.ftl" as light>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>分类管理</title>
    <#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <script type="text/javascript">
        $().ready(function() {
            initClassifyTable();
        });
        function initClassifyTable() {
            $("#classifies table").tableDrag({
                callback : function (row, from, to) {
                    updateIndex($(row).attr("classifyId"), to);
                }
            });
        }

        function updateIndex(id, index) {
            var url = '${absoluteContextPath}/classify/modifyIndex';
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
                url:'${absoluteContextPath}/classify/ajaxList',
                type:'get',
                loading:true,
                success:function(json){
                    var data = JSON.parse(json);
                    if (data.returnCode == 1) {
                        $('#classifies').html(data.content.data);
                        initClassifyTable();
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }

        function popAddOrModify(id){
            var data = {};
            var url = '${absoluteContextPath}/classify/';
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
                        var $pop_app_div = $('#pop_classify_div');
                        $pop_app_div.html(data.content.data);
                        $pop_app_div.modal({show: 'show',backdrop:'static'});
                    } else {
                        $.showMessage({message: data.returnMsg});
                    }
                }
            });
        }


        function del(id) {
            confirmDialog("删除分类", "是否删除?", function() {
                var data = {};
                data.id = id;
                $.customAjax({
                    url: '${absoluteContextPath}/classify/del',
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
<path><li>产品管理</li><li>分类管理</li></path>
<body>
<div class="page-inner">
    <div id="main-wrapper">
        <div class="row tab_demo">
            <div class="col-md-12">
                <div class="panel panel-white">
                    <div class="panel-heading clearfix">
                        <div class="col-md-1 no-p">
                        <@sec.authorize action="classify-operate">
                            <button type="button" class="btn btn-primary btn-addon m-b-sm" onclick="popAddOrModify();">添加分类</button>
                        </@sec.authorize>
                        </div>
                    </div>
                    <div class="panel-body statement-card" id="classifies">
                        <#include "classifies.ftl"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" id="pop_classify_div" class="modal fade in" tabindex="-1" role="dialog"></div>
</body>
</html>
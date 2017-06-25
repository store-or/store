<#import "../light.ftl" as light>
<html>
<head>
    <title>系统参数</title>
<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
    <link href="${absoluteContextPath}/js/jquery/jtree/jquery.treeTable.css" rel="stylesheet" type="text/css" />
    <link href="${absoluteContextPath}/js/jquery/jtree/jquery.treetable.theme.default.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jtree/jquery.treetable.js"></script>
    <script type="text/javascript">
        function openEdit(paramId,paramName) {
            $('#paramId').val(paramId);
            $('#paramName').html(paramName);
            var paramValue = $('#paramValue_'+paramId).html();
            $('#value').val(paramValue);
            $('#edit').modal('show');
        }
        function edit() {
            $('#paramForm').customAjaxSubmit({
                url:'${absoluteContextPath}/param',
                type:'post',
                success:function(json){
                    var data = eval('('+json+')');
                    if (data.returnCode == 1) {
                        var paramId = $('#paramId').val();
                        var paramValue = $('#paramValue').val();
                        $('#paramValue_' + paramId).html(paramValue);
                    }
                    $('#edit').modal('hide');
                    $.showMessage({message:data.returnMsg,type:'success'});
                    window.location.reload();
                }
            })
        }

        $(document).ready(function()  {
            $("#tree_table_1").treetable({
                initialState:'expanded',
                expandable: true
            });
        });

    </script>
</head>
<path><li>系统管理</li><li>系统参数</li></path>
<body>
<iframe src="${SystemConfig.storeFrontUrl!""}/param/reload" class="hide"></iframe>
<div id="main-wrapper">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-white">
                <div class="panel-body statement-card">
                    <table id="tree_table_1" class="table-hover table treeTable table-bordered table-striped">
                        <thead>
                        <tr class="table_a">
                            <th style="width: 150px" name="name">
                                名称
                            </th>
                            <th style="width: 200px">
                                描述
                            </th>
                            <th style="width: 400px">
                                值
                            </th>
                            <th style="width: 100px">
                                &nbsp;
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list paramMap.keySet() as type>
                                <tr id="${type}" class="table_a" data-tt-id="${type}">
                                    <td colspan="4">${type}</td>
                                </tr>
                                <#list paramMap.get(type) as param>
                                    <tr data-tt-parent-id="${type}" data-tt-id="${param.name}">
                                        <td>${param.name!""}</td>
                                        <td>${param.description!""}</td>
                                        <td><span id="paramValue_${param.id?c}">${param.value!""}</span></td>
                                        <td><a href="javascript:void(0)" onclick="openEdit(${param.id?c},'${param.name}')">查看</a> </td>
                                    </tr>
                                </#list>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="edit" aria-hidden="false" class="modal fade in" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 500px;">
        <div class="modal-content" >
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title">修改</h4>
            </div>
            <div class="modal-body">
                <div id="param_message">
                    <#include "../message.ftl"/>
                </div>
                <form class="form-horizontal" id="paramForm" method="post"
                      action="">
                    <input type="hidden" id="paramId" name="id">
                    <div class="form-group">
                        <label for="value" class="col-sm-3 control-label">
                            <span id="paramName"></span>
                        </label>
                        <div class="col-sm-8">
                            <input type="text" id="value" class="form-control" name="value"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="clearfix"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <@sec.authorize action="param-operate">
                    <button type="button" class="btn btn-primary" onclick="edit()">确认修改</button>
                </@sec.authorize>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        function modify() {
            $('#paramForm').customAjaxSubmit({
                success:function(data){
                    var msg = JSON.parse($(data).html());
                    $('#paramForm').submitResult($(data).html());
                    if (msg.returnCode == 0) {
                    }else{
                    }
                }
            });
        }
    </script>
</div>

</body>
</html>


<script type="text/javascript">
    window.UEDITOR_HOME_URL="${absoluteContextPath}/js/ueditor/";
</script>

<script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
<!-- 实例化编辑器 -->
<script type="text/javascript">
    var ue = UE.getEditor('menuEditContainer',{
        autoHeightEnabled: true,
        autoFloatEnabled: true,
        initialFrameHeight:300,
        allowDivTransToP:false,
        toolbars: [
            ['source','fullscreen', 'undo', 'redo', 'bold','cleardoc','fontfamily','fontsize','paragraph','simpleupload','insertimage','wordimage',
                'link','spechars','searchreplace','justifyleft','justifyright','justifycenter','justifyjustify','forecolor',
                'rowspacingtop','rowspacingbottom','directionalityltr','directionalityrtl','imagenone','imageleft','imageright',
                'attachment','imagecenter','lineheight','autotypeset','customstyle','touppercase','tolowercase','inserttable',
                'anchor','bold','indent','italic','underline','strikethrough','subscript','fontborder','superscript',
                'formatmatch','pasteplain','horizontal','removeformat','time','date','unlink','insertrow','insertcol',
                'mergeright','mergedown','deleterow','deletecol','splittorows','splittocols','splittocells','deletecaption','inserttitle',
                'mergecells','deletetable','insertparagraphbeforetable','preview']
        ]
    });
</script>

<ul class="cd-gallery post drag">
    <div id="form_message" class="col-sm-10">
        <#include "../../message.ftl"/>
    </div>
    <form class="form-horizontal" id="menuForm" method="post" action="${absoluteContextPath}/menu/save">
        <input type="hidden" name="type" id="type" value="">
        <#if menu.id??>
            <input type="hidden" name="id" value="${menu.id?c}">
            <input type="hidden" name="name" value="${menu.name!""}">
            <div class="form-group">
                <div class="col-sm-3">
                    <span>
                        <h3>${menu.name!""}
                            <label class="label label-primary" style="font-size: 12px; font-weight: 100;padding:5px 10px;color: white"></label>
                        </h3>
                    </span>
                </div>
            </div>
        <#else>
            <div class="form-group">
                <label for="input-help-block" class="col-sm-2 control-label" style="min-height: 0px;"><span class="txt-impt">*</span>菜单名称:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="name" value="${(menu.name!'')?xhtml}">
                </div>
            </div>
        </#if>
        <div class="form-group">
            <div class="col-sm-12" style="position:relative;z-index:99;">
                <script id="menuEditContainer" name="currentContent" type="text/plain">
                ${menu.currentContent!""}
                </script>
            </div>
        </div>
        <div class="form-group" style="margin-bottom: 0;">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" class="btn btn-primary" onclick="save('PUBLIC')" style="float: right;margin-left: 10px">发布</button>
                <button type="button" class="btn btn-primary" onclick="save('DRAFT')" style="float: right;margin-left: 10px">存为草稿</button>
                <button type="button" class="btn btn-primary" onclick="goback(<#if menu.id??>${menu.id?c}</#if>)" style="float: right;margin-left: 10px">取消</button>
            </div>
        </div>
    </form>
</ul>

<script type="text/javascript">
    function goback(menuId){
        if(!!menuId){
            window.location.href = '${absoluteContextPath}/menu/list?menuId='+ menuId;
        }else{
            window.location.href = '${absoluteContextPath}/menu/list';
        }
    }

    function save(type){
        $('#type').val(type);
        var commitForm = $('#menuForm');
        $(commitForm).customAjaxSubmit({
            success:function(json){
                $(commitForm).submitResult(json,{
                    messageParentId: 'form_message',
                    success:function(data){
                        if(data.returnCode == 1){
                            window.location.href = '${absoluteContextPath}/menu/list?menuId='+ data.content;
                        }
                    }
                });
            }
        });
    }

</script>
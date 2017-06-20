<div class="modal-dialog modal-lg" style="width:600px;">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title"><#if banner.id??>
                修改
            <#else>
                添加
            </#if></h4>
        </div>
        <div class="modal-body">
            <div id="banner_message">
                <#include "../../message.ftl"/>
            </div>
            <form class="form-horizontal" id="bannerForm" method="post" action="${absoluteContextPath}/banner/<#if banner.id??>modify<#else>save</#if>">
                <#if banner.id??>
                    <input  type="hidden" name="id"  class="form-control" value="${banner.id?c}">
                </#if>
                <!-- / .form-group -->
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label"><span class="text-danger">* </span>名称:</label>
                    <div class="col-sm-10">
                        <input id="name" type="text"  name="name"  class="form-control" value="${banner.name!""}" maxlength="256">
                    </div>
                </div>
                <!-- / .form-group -->
                <!-- / .form-group -->
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label"><span class="text-danger">* </span>推荐位:</label>
                    <div class="col-sm-10">
                        <select class="form-control" name="type">
                            <#list BannerType.values() as type>
                                <option value="${type}" <#if banner.type?? && banner.type==type>selected="selected" </#if>>${type.alias}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <!-- / .form-group -->
                <!-- / .form-group -->
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label"><span class="text-danger">* </span>海报:</label>
                    <div class="col-sm-10">
                        <div class="div1" id="poster-div">
                            <#if banner.poster?? && banner.poster != ''>
                                <img src="<#if (banner.poster)?index_of('http')==-1>${uploadFileContext}/</#if>${banner.poster}"/>
                            <#else >
                                <img src="${absoluteContextPath}/img/add-file.jpg"/>
                            </#if>
                            <input type="file" title='选择文件' onchange="uploadPoster(this);" class="inputstyle">
                        </div>
                        <input type="hidden" class="hide" name="poster" value="${(banner.poster!'')?xhtml}">
                    </div>
                </div>
                <!-- / .form-group -->
                <!-- / .form-group -->
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">跳转链接:</label>
                    <div class="col-sm-10">
                        <input id="name" type="text"  name="link"  class="form-control" value="${banner.link!""}" maxlength="1024">
                    </div>
                </div>
                <!-- / .form-group -->
            </form>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary"   onclick="modify()">
                <#if banner.id??>确认修改<#else>确认添加</#if>
            </button>
        </div>
    </div>
    <!-- / .modal-content -->
</div>

<script type="text/javascript">
    function uploadPoster(obj) {
        $(obj).uploadSinglePicture({
            url: '${absoluteContextPath}/file/upload',
            callback: function (img) {
                $("[name='poster']").val(img.url);
                $("#poster-div").find("img").attr("src", img.visitUrl);
            }
        });
    }
    function modify() {
        $('#bannerForm').customAjaxSubmit({
            success:function(json){
                $('#bannerForm').submitResult(json,{
                    messageParentId: 'banner_message',
                    success:function(data) {
                        $('#pop_banner_div').modal("hide");
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

</script>

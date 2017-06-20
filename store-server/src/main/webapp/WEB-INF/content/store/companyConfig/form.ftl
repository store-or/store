<!DOCTYPE html>
<html>
<head>
    <title>页面配置</title>
    <script>
        function uploadWechat(obj) {
            $(obj).upload({
                url: '${absoluteContextPath}/file/upload',
                regular: '^.*\.(jpg|png|bmp|jpeg|gif)$',
                success: function (json) {
                    var img = json.content[0];
                    $("[name='wechat.value']").val(img.url);
                    $("#wechat-div").find("img").attr("src", img.visitUrl);
                }
            });
        }
        function uploadAboutUs(obj) {
            $(obj).uploadSinglePicture({
                url: '${absoluteContextPath}/file/upload',
                callback: function (img) {
                    $("[name='aboutUs.value']").val(img.url);
                    $("#aboutUs-div").find("img").attr("src", img.visitUrl);
                }
            });
        }
        function uploadContactUs(obj) {
            $(obj).uploadSinglePicture({
                url: '${absoluteContextPath}/file/upload',
                callback: function (img) {
                    $("[name='contactUs.value']").val(img.url);
                    $("#contactUs-div").find("img").attr("src", img.visitUrl);
                }
            });
        }

        function save() {
            $('#configForm').customAjaxSubmit({
                loading: true,
                success: function (json) {
                    $('#configForm').submitResult(json, {
                        success: function (data) {
                            if (data.returnCode == 1) {
                                alertMsg("页面参数", "修改成功");
                            } else {
                                $.showMessage({type: "error", message: data.returnMsg});
                            }
                        }
                    });
                }
            });
        }
    </script>
</head>
<path><li>前端编排</li><li>页面配置</li></path>
<body>
<div class="page-inner">
    <div id="main-wrapper">
        <div id="form-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel p-v-sm">
                        <form class="form-horizontal" id="configForm" method="post" action="${absoluteContextPath}/companyConfig/modify">
                            <div class="col-md-8">
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">电话:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="phone.id" value="${config.phone.id?c}" >
                                        <input type="text" maxlength="50" class="form-control" name="phone.value" value="${(config.phone.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">地址:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="address.id" value="${config.address.id?c}" >
                                        <input type="text" maxlength="256" class="form-control" name="address.value" value="${(config.address.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">微信二维码:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="wechat.id" value="${config.wechat.id?c}" >
                                        <div class="div1" id="wechat-div">
                                        <#if config.wechat.value?? && config.wechat.value != ''>
                                            <img src="<#if (config.wechat.value)?index_of('http')==-1>${uploadFileContext}/</#if>${config.wechat.value}"/>
                                        <#else >
                                            <img src="${absoluteContextPath}/img/add-file.jpg"/>
                                        </#if>
                                            <input type="file" title='选择文件' onchange="uploadWechat(this);" class="inputstyle">
                                        </div>
                                        <input type="hidden" class="hide" name="wechat.value" value="${(config.wechat.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">微博地址:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="weibo.id" value="${config.weibo.id?c}" >
                                        <input type="text" maxlength="1024" class="form-control" name="weibo.value" value="${(config.weibo.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">天猫旗舰店地址:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="tmall.id" value="${config.tmall.id?c}" >
                                        <input type="text" maxlength="1024" class="form-control" name="tmall.value" value="${(config.tmall.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">首页-关于我们图片:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="aboutUs.id" value="${config.aboutUs.id?c}" >
                                        <div class="div1" id="aboutUs-div">
                                        <#if config.aboutUs.value?? && config.aboutUs.value != ''>
                                            <img src="<#if (config.aboutUs.value)?index_of('http')==-1>${uploadFileContext}/</#if>${config.aboutUs.value}"/>
                                        <#else >
                                            <img src="${absoluteContextPath}/img/add-file.jpg"/>
                                        </#if>
                                            <input type="file" title='选择文件' onchange="uploadAboutUs(this);" class="inputstyle">
                                        </div>
                                        <input type="hidden" class="hide" name="aboutUs.value" value="${(config.aboutUs.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">首页-关于我们简介:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="introduction.id" value="${config.introduction.id?c}" >
                                        <textarea rows="20" class="form-control" name="introduction.value">${(config.introduction.value!'')?xhtml}</textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">联系我们图片:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="contactUs.id" value="${config.contactUs.id?c}" >
                                        <div class="div1" id="contactUs-div">
                                        <#if config.contactUs.value?? && config.contactUs.value != ''>
                                            <img src="<#if (config.aboutUs.value)?index_of('http')==-1>${uploadFileContext}/</#if>${config.contactUs.value}"/>
                                        <#else >
                                            <img src="${absoluteContextPath}/img/add-file.jpg"/>
                                        </#if>
                                            <input type="file" title='选择文件' onchange="uploadContactUs(this);" class="inputstyle">
                                        </div>
                                        <input type="hidden" class="hide" name="contactUs.value" value="${(config.contactUs.value!'')?xhtml}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="name" class="col-sm-2 control-label">联系我们:</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" class="hide" name="contactUsContent.id" value="${config.contactUsContent.id?c}" >
                                        <textarea rows="20" class="form-control" name="contactUsContent.value">${(config.contactUsContent.value!'')?xhtml}</textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                        <div class="text-center m-b-sm m-t-sm ">
                            <button class="btn btn-primary" onclick="save()">确认修改</button>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
    </div>
</div>
</body>
<!DOCTYPE html>
<html>
<head>
    <title><#if product.id??>编辑产品<#else>添加产品</#if></title>
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/js/select2/select2.min.css">
    <script type="text/javascript" src="${absoluteContextPath}/js/select2/select2.full.min.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style>
        #classify_select_div span.select2-container{
            z-index: 99;
        }
    </style>

    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        window.UEDITOR_HOME_URL="${absoluteContextPath}/js/ueditor/";
        var ue = UE.getEditor('myContainer',{
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

    <script>
        $().ready(function() {
            $("#classify_select").select2({closeOnSelect: false});
        });
        function checkTag(obj) {
            if ($(obj).isTabSelected()) {
                return;
            }
            $(obj).selectTab();
        }

        function uploadCover(obj) {
            $(obj).uploadSinglePicture({
                url: '${absoluteContextPath}/file/upload',
                callback: function (picture) {
                    $("#cover-div").find("[name='cover']").val(picture.url);
                    $("#cover-div").find("img").attr("src", picture.visitUrl);
                }
            });
        }

        function changePicture(obj) {
            var $file = $(obj);
            var $parent = $file.parents("#pictureDiv");
            $file.uploadSinglePicture({
                url: '${absoluteContextPath}/file/upload',
                callback: function (picture) {
                    $parent.find("[name='pictureList']").val(picture.url);
                    $parent.find("img").attr("src", picture.visitUrl);
                }
            });
        }

        function uploadPictures(obj) {
            var $picturesDiv = $("#pictures");
            var len = $picturesDiv.children("#pictureDiv").length;
            console.log(len);
            $(obj).uploadPictures({
                url: '${absoluteContextPath}/file/upload',
                data: {total:4 - len},
                callback: function (pictures) {
                    for (var i = 0; i < pictures.length; i++) {
                        var $temp = $($("#pictureTemplate").prop("outerHTML"));
                        $temp.find("input[type='hide']").val(pictures[i].url);
                        $temp.find("input[type='hide']").attr("name", "pictureList");
                        $temp.find("img").attr("src", pictures[i].visitUrl);
                        $temp.attr("id", "pictureDiv");
                        $temp.removeClass("hide");
                        $temp.appendTo($picturesDiv);
                    }
                    if ((pictures.length + len) >= 4) {
                        $("#addPicture_div").addClass("hide");
                    }
                }
            });
        }

        function save() {
            $('#product_form').customAjaxSubmit({
                success:function(json){
                    console.log(json);
                    $('#product_form').submitResult(json,{
                        success:function(data) {
                            if (data.returnCode == 1) {
                                alertMsg(<#if product.id??>"编辑产品"<#else>"添加产品"</#if>, "成功保存", function() {
                                    location.href="${absoluteContextPath}/product/list";
                                });
                            }else{
                                $.showMessage({type:"error",message: data.returnMsg});
                            }
                        }
                    });
                }
            });
        }
    </script>
</head>
<path><li>产品管理</li><li><a href="${absoluteContextPath}/product/list">产品管理</a></li><li><#if product.id??>编辑产品<#else>添加产品</#if></li></path>
<body>
    <div class="page-inner">
        <div id="main-wrapper">
            <div class="row" >
                <div class="col-md-12">
                    <div class="panel p-v-sm">
                        <form class="form-horizontal" id="product_form" method="post" action="${absoluteContextPath}/product/<#if (product.id)??>modify<#else>save</#if>">
                            <#if product.id??>
                                <input type="hidden" class="hide" name="id" value="${product.id?c}"/>
                            </#if>
                            <div class="form-horizontal"  style="border:1px solid #ddd;">
                                <div class="panel panel-white">
                                    <div class="clearfix form-title">
                                        <h3 class="panel-title">基本信息</h3>
                                    </div>
                                    <div class="panel-body statement-card">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label"><span class="text-danger">*</span>产品名称:</label>
                                                <div class="col-sm-10">
                                                    <input type="text" maxlength="200" class="form-control" name="name" value="${(product.name!'')?xhtml}">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label"><span class="text-danger">*</span>产品分类:</label>
                                                <div class="col-sm-10" id="classify_select_div">
                                                    <select id="classify_select" name="classifyIds"
                                                            class="js-example-tokenizer js-states form-control w-lg"
                                                            multiple="multiple" tabindex="-1">
                                                        <#list classifies as classify>
                                                            <option value="${classify.id?c}" <#if product.classifyIds?? && product.classifyIds?contains("," + classify.id?c + ",")>selected="selected" </#if>>${classify.name}</option>
                                                        </#list>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label">购买链接:</label>
                                                <div class="col-sm-10">
                                                    <input type="text" maxlength="1024" class="form-control" name="link" value="${(product.link!'')?xhtml}">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label">一句话介绍:</label>
                                                <div class="col-sm-10">
                                                    <input type="text" maxlength="256" class="form-control" name="introduction" value="${(product.introduction!'')?xhtml}">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label">推荐标签:</label>
                                                <div class="col-sm-10">
                                                    <#list ProductTag.values() as tag>
                                                        <button type="button" class="btn-sm btn-default <#if ((!product.tag??) && tag_index == 0) || (product.tag?? && product.tag == tag)>btn-primary</#if>" onclick="checkTag(this)" >
                                                            <input type="radio" name="tag" class="hide" <#if ((!product.tag??) && tag_index == 0) || (product.tag?? && product.tag == tag)>checked</#if> value="${tag}">${tag.alias}
                                                        </button>
                                                    </#list>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label"><span class="text-danger">*</span>产品封面:</label>
                                                <div class="col-sm-10" id="cover-div">
                                                    <div class="div1">
                                                        <#if product.cover?? && product.cover != ''>
                                                            <img src="<#if (product.cover)?index_of('http')==-1>${uploadFileContext}/</#if>${product.cover}"/>
                                                        <#else >
                                                            <img src="${absoluteContextPath}/img/add-file.jpg"/>
                                                        </#if>
                                                        <input type="file" title='选择文件' onchange="uploadCover(this);" class="inputstyle">
                                                    </div>
                                                    <input type="hidden" class="hide" name="cover" value="${(product.cover!'')?xhtml}">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label"><span class="text-danger">*</span>产品图片:</label>
                                                <div class="col-sm-10">
                                                    <div id="addPicture_div" style="width: 70px; height: 30px;" class="div1<#if product.pictureList?? && product.pictureList?size gte 4> hide</#if>">
                                                        <input type="file" multiple="multiple" title='选择文件' style="width: 70px; height: 30px;" onchange="uploadPictures(this);" class="inputstyle">
                                                        <button type="button" class="btn-sm btn-default btn-primary">添加图片</button>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label"></label>
                                                <div class="col-sm-10" id="pictures">
                                                    <#if product.pictureList??>
                                                        <#list product.pictureList as picture>
                                                            <div id="pictureDiv">
                                                                <div class="div1">
                                                                    <img src="<#if (picture)?index_of('http')==-1>${uploadFileContext}/</#if>${picture}"/>
                                                                    <input type="file" title='选择文件' onchange="changePicture(this);" class="inputstyle">
                                                                    <div class="file-close" onclick="$(this).parents('#pictureDiv').remove();$('#addPicture_div').removeClass('hide');" >
                                                                        <span aria-hidden="true" class="icon-close text-danger text-md"></span>
                                                                    </div>
                                                                </div>
                                                                <input type="hidden" class="hide" name="pictureList" value="${(picture!'')?xhtml}">
                                                            </div>
                                                        </#list>
                                                    </#if>
                                                </div>
                                            </div>
                                            <div id="pictureTemplate" class="hide">
                                                <div class="div1" >
                                                    <img />
                                                    <input type="file" title='选择文件' onchange="changePicture(this);" class="inputstyle">
                                                    <div class="file-close" onclick="$(this).parents('#pictureDiv').remove();$('#addPicture_div').removeClass('hide');" >
                                                        <span aria-hidden="true" class="icon-close text-danger text-md"></span>
                                                    </div>
                                                </div>
                                                <input type="hidden" class="hide">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label">简介:</label>
                                                <div class="col-sm-10">
                                                    <textarea name="description" rows="5" class="form-control">${product.description!""}</textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="input-help-block" class="col-sm-2 control-label">产品参数:</label>
                                                <div class="col-sm-10">
                                                    <textarea name="detailDO.properties" rows="15" class="form-control"><#if product.detailDO??>${product.detailDO.properties!""}</#if></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-horizontal"  style="border:1px solid #ddd; margin-top:5px;">
                                <div class="panel panel-white">
                                    <div class="clearfix form-title">
                                        <h3 class="panel-title">产品详情</h3>
                                    </div>
                                    <div class="panel-body statement-card">
                                        <div class="col-sm-12" style="position:relative;z-index:99;">
                                            <script id="myContainer" name="detailDO.detail" type="text/plain">
                                                <#if product.detailDO??>${product.detailDO.detail!""}</#if>
                                            </script>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                        <div class="text-center m-b-sm m-t-sm ">
                            <button  class="btn btn-primary" id="addVideoBtn"onclick="save()">保存</button>
                            <button  class="btn btn-default" onclick='location.href="${absoluteContextPath}/product/list"'>返回</button>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
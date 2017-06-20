// 上传文件
$.fn.upload = function(options){
    var defaults = {
        name:'file',
        url:'',
        regular:'',
        data:{},
        messageParentId:'',
        fail: function(data, options){
            $.showMessage({message:data.returnMsg, parentId:options.messageParentId});
        },
        success:function(data){}
    };
    options = $.extend(true, {}, defaults,options);
    var file = $(this);
    if (options.regular && !new RegExp(options.regular).test(this.val())) {
        alertMsg("文件上传","不支持的文件格式，请重新选择!");
        return;
    }
    file.parent().prepend(file.clone(true));
    file.attr("name", options.name);
    var $fileForm = $('<form id="fileForm" action="' + options.url + '" enctype="multipart/form-data"></form>').appendTo(document.body);
    file.appendTo($fileForm);

    $fileForm.customAjaxSubmit({
        data:options.data,
        traditional:true,
        success:function(json){
            $fileForm.remove();
            $fileForm.html("");
            var result = JSON.parse(json);
            if (result.returnCode == 0) {
                options.fail(result, options);
            } else {
                options.success(result);
            }
        },
        error : function() {
            $fileForm.html("");
        }
    });
}

function execute(func) {
    try {
        if (typeof(eval(func)) == "function") {
            eval(func + "()");
        }
    } catch (e) {

    }
}

//特殊处理，去掉<pre>标签
function revise(json) {
    var re = /[\{](.*)[\}]/;
    return json.match(re)[0];
}

//5秒心跳校验锁
function heartbeatCheckLock(absoluteContextPath, options) {
	setTimeout(function(){
		$.customAjax({
	        url: absoluteContextPath + '/client/lock/checkLock',
	        data: options._data,
	        type: "post",
	        success:function(json){
	            var data = JSON.parse(json);
	            if (data.returnCode == 1) {
	                if(data.content) {
	                	heartbeatCheckLock(absoluteContextPath, options);
	                } else {
	                	options._success();
	                }
	            }
	        }
	    });
	}, 5000);
}

$.fn.disableAutoSubmit = function () {
    $(this).bind('submit', function (e) {
        e.preventDefault();
    });
}

function confirmDialog(title, message, okCallback, cancelCallback) {
    var $pop_confirm_div = $("#pop_confirm_div");
    $pop_confirm_div.find("#title").html(title);
    $pop_confirm_div.find("#message").html(message);
    $pop_confirm_div.find("#ok").unbind("click").bind("click", function() {
        $pop_confirm_div.modal("hide");
        if(undefined != okCallback) {
            okCallback.apply();
        }
    });
    $pop_confirm_div.find("#no-ok").unbind("click").bind("click", function() {
        $pop_confirm_div.modal("hide");
        if(undefined != cancelCallback){
            cancelCallback.apply();
        }
    });
    $pop_confirm_div.modal("show");
}

function alertMsg(title, message, func) {
    var $pop_confirm_div = $("#pop_alert_div");
    $pop_confirm_div.find("#title").html(title);
    $pop_confirm_div.find("#message").html(message);
    $pop_confirm_div.modal("show");
    $pop_confirm_div.find("#ok").unbind("click").bind("click", function() {
        $pop_confirm_div.modal("hide");
        if(undefined != func) {
            func.apply();
        }
    });
}

function theadClick(searchId, obj, pageId, pageSize, replace, autoCount, totalCount) {
    var th = $(obj);
    var $search = $("#" + searchId);
    $search.find("#orderBy_l").val(th.attr("name"));
    var $order = $search.find("#order_l");
    var order = $order.val();
    var len = order.split(',').length;
    if((',' + order + ',').indexOf(',asc,') != -1) {
        var newOrder = 'desc';
        for (var i = 1; i < len; i++) {
            newOrder += ',desc';
        }
        $order.attr("value", newOrder);
    } else {
        var newOrder = 'asc';
        for (var i = 1; i < len; i++) {
            newOrder += ',asc';
        }
        $order.attr("value", newOrder);
    }
    new Page(pageId, 1, pageSize, replace, autoCount, totalCount).toPage();
}

function toPage(pageId, drop, replace, autoCount, totalCount) {
    var $page = $("#table_l_" + pageId);
    var pageNo = $page.find("#pageNo").val();
    var pageSize = $page.find("#pageSize").val();
    if (drop) {
        var total = $page.find("#totalCount").val() - 1;
        var count = Math.ceil(total / pageSize);
        if (count < pageNo) {
            pageNo = count;
        }
    }
    new Page(pageId, pageNo, pageSize, replace, autoCount, totalCount).toPage();
}

// 选择产品
$.fn.chooseProduct= function(options){
    var defaults = {
        messageParentId:'message_l',
        url:'',
        data: {},
        success:function(id){}
    };
    options = $.extend(true, {}, defaults,options);
    var productDialog = $(this);
    $.customAjax({
        url: options.url,
        data : options.data,
        type: "get",
        traditional:true,
        loading:true,
        success:function(json){
            var data = JSON.parse(json);
            if (data.returnCode == 1) {
                productDialog.html(data.content.data);
                productDialog.modal('show');
                productDialog.find("#ok").unbind("click").bind("click" , function(){
                    var products = productDialog.find("#chooseProduct_table input[name='chooseProductId']:checked");
                    if (products.length <= 0) {
                        alertMsg("选择产品","请选择产品!");
                        return;
                    }
                    productDialog.modal("hide");
                    options.success($(products[0]).val());
                });
            } else {
                $.showMessage({type:"error",message: data.returnMsg, parentId: options.messageParentId});
            }
        }
    });
}

// callback = function (selectRow, from, to) selectRow为选中的那一行, from起始位置(从1开始)，to结束位置(从1开始)
$.fn.tableDrag = function(options) {
    var table = $(this);
    var tbody = $(this).children('tbody');
    var rows = tbody.children();
    if (tbody.length == 0 || rows.length <= 1) {
        return;
    }
    var selectedRow;

    function rowMouseDown() {
        selectedRow = this;
        table.addClass("dragTable");
        return false;	//防止拖动时选取文本内容，必须和 mousemove 一起使用
    }
    function rowMouseMove() {
        return false;	//防止拖动时选取文本内容，必须和 mousedown 一起使用
    }
    function rowMouseUp() {
        if(selectedRow && selectedRow != this){
            if (options.callback) {
                options.callback(selectedRow, selectedRow.sectionRowIndex + 1, this.sectionRowIndex + 1);
                if(options.frontMove){
                    if(this.sectionRowIndex < selectedRow.sectionRowIndex) {
                        $(this).before($(selectedRow)).removeClass('mouseOver'); //插入
                    }else{
                        $(this).after($(selectedRow)).removeClass('mouseOver'); //插入
                    }
                }
            } else {
                if(this.sectionRowIndex < selectedRow.sectionRowIndex) {
                    $(this).before($(selectedRow)).removeClass('mouseOver'); //插入
                }else{
                    $(this).after($(selectedRow)).removeClass('mouseOver'); //插入
                }
            }
            table.removeClass("dragTable");
            selectedRow = null;
        }
    }
    function rowHoverIn() {
        if(selectedRow && selectedRow != this){
            $(this).addClass('mouseOver');	//区分大小写的，写成 'mouseover' 就不行
        }
    }
    function rowHoverOut() {
        if(selectedRow && selectedRow != this){
            $(this).removeClass('mouseOver');	//区分大小写的，写成 'mouseover' 就不行
        }
    }
    function bodyMouseOver() {
        event.stopPropagation(); //禁止 tbody 的事件传播到外层的 div 中
    }

    function bind() {
        rows.unbind("mousedown", rowMouseDown).bind("mousedown", rowMouseDown);
        rows.unbind("mousemove", rowMouseMove).bind("mousemove", rowMouseMove);
        rows.unbind("mouseup", rowMouseUp).bind("mouseup", rowMouseUp);
        rows.unbind("mouseenter", rowHoverIn).bind("mouseenter", rowHoverIn);
        rows.unbind("mouseout", rowHoverOut).bind("mouseout", rowHoverOut);
        tbody.unbind("mouseover", bodyMouseOver).bind("mouseover", bodyMouseOver);
    }

    function unBind() {
        rows.unbind("mousedown", rowMouseDown);
        rows.unbind("mousemove", rowMouseMove);
        rows.unbind("mouseup", rowMouseUp);
        rows.unbind("mouseenter", rowHover);
        rows.unbind("mouseout", rowHover);
        tbody.unbind("mouseover", bodyMouseOver);
    }
    bind();
}

/**
 * 导出表格，table必须包含tbody，如果想去掉某一列不过滤，需在td和th上面加上class="no-export"
 * @param options
 */
$.fn.exportTable = function(options) {
    var $table = $(this);
    if ($table.children("tbody").children("tr").length <= 0) {
        alertMsg("文件导出", "没有内容可导出");
        return;
    }
    var data = [];
    $.each($table.find("tr"), function(index, tr){
        var line = [];
        if(index == 0) {
            $.each($(tr).find("th"), function(index, th){
                if ($(th).hasClass("no-export")) {
                    return;
                }
                var text = $(th).text();
                line.push(text);
            });
        } else {
            $.each($(tr).find("td"), function(index, td){
                if ($(td).hasClass("no-export")) {
                    return;
                }
                line.push($(td).text());
            });
        }
        data.push(line);
    });
    var defaults = {
        url: '/export/exportExcel',
        fileName: 'file'
    };
    var ops = $.extend(true, {}, defaults, options);
    $("#exportForm").remove();
    var $exportForm = $('<form id="exportForm" action="' + options.url + '" method="post"></form>').appendTo(document.body);
    $('<input type="hidden" name="fileName" value=""/>').val(options.fileName).appendTo($exportForm);
    $('<textarea name="data"></textarea>').val(JSON.stringify(data)).appendTo($exportForm);
    $exportForm.submit();
}

/**
 * 增强版的select2
 * 解决：按照用户点击的顺序来展示选项，但是会导致选项的位置被追加到最后面，新增属性appendOnClick表示点击末尾追加
 *      当超过maximumSelectionLength,如果select2使用了closeOnSelect：false，会导致该属性无效
 * @param options
 */
$.fn.customSelect2 = function(options) {
    var defaults = {
        appendOnClick : false,
        selecteds: []
    }
    var $this = $(this);
    options = $.extend(true, {}, defaults, options);
    if (options.appendOnClick) {
        options.dropdownCssClass = "customSelect2";
        var $options = $this.children("option");
        for (var i = 1; i < $options.length; i++) {
            // 记住option的层级关系
            $($options[i - 1]).attr("before", $($options[i]).attr("value"));
        }
        $.each(options.selecteds, function(index, selected) {
            var $option = $this.children("option[value='" + selected + "']");
            $option.attr("selected", "selected");
            $option.detach();
            $this.append($option);
            $this.trigger("change");
        });
        $this.on("select2:select", function (evt) {
            var element = evt.params.data.element;
            var $element = $(element);
            $element.detach();
            $this.append($element);
            $this.trigger("change");
            var length = $this.children('option:selected').length;
            if (options.maximumSelectionLength && length >= options.maximumSelectionLength) {
                $this.select2("close");
            }
        }).on("select2:unselect", function (evt) {
            var element = evt.params.data.element;
            var $element = $(element);
            if ($element.attr("before")) {
                $element.detach();
                $this.children("option[value='" + $element.attr("before") + "']").before($element);
                $this.trigger("change");
            }
            $this.select2("close");
        });
    }
    $this.select2(options);
}

$.fn.isTabSelected = function() {
    return $(this).children("input[type='radio']").is(":checked");
}
$.fn.selectTab = function() {
    var $this = $(this);
    $this.parent().children(".btn-primary").unSelectTab();
    $this.addClass("btn-primary")
    $this.children("input[type='radio']").attr("checked", "checked");
}
$.fn.unSelectTab = function() {
    var $this = $(this);
    $this.removeClass("btn-primary")
    $this.children("input[type='radio']").removeAttr("checked");
}
$.fn.tabData = function() {
    var $radio = $(this).children("input[type='radio']");
    var data = {};
    data[$radio.attr("name")] = $radio.val();
    return data;
}
$.fn.uploadSinglePicture = function(options) {
    var defaults = {
        url : '',
        callback : function(picture) {}
    }
    options = $.extend(true, {}, defaults, options);
    $(this).upload({
        url: options.url,
        regular: '^.*\.(jpg|png|bmp|jpeg|gif)$',
        success: function (json) {
            options.callback(json.content[0]);
        }
    });
}

$.fn.uploadPictures = function(options) {
    var defaults = {
        url : '',
        data:{},
        callback : function(picture) {}
    }
    options = $.extend(true, {}, defaults, options);
    $(this).upload({
        url: options.url,
        data: options.data,
        regular: '^.*\.(jpg|png|bmp|jpeg|gif)$',
        success: function (json) {
            options.callback(json.content);
        }
    });
}
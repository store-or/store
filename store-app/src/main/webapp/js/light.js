(function($) {
    $.fn.lightRequest = function(options) {
        var defaults = {
            type : 'post',
            url : '',
            params: [],  // [ {name: "a",value: "1"} ]
            pageId:'page',
            pageNo:1,
            pageSize:20,
            totalCount:0,
            autoCount:true,
            order:$(this).find('#order_l').val() != undefined?$(this).find('#order_l').val():'',
            orderBy:$(this).find('#orderBy_l').val()!=undefined?$(this).find('#orderBy_l').val():'',
            lightId:'',
            prefix:'search',
            replace:true,
            async:true,
            messageParentId:'message_l',
            loading:true,
            result:function(data){}
        };

        defaults.id = '#' + $(this).attr('id');
        //排序
        if ($(this).find('.order_l').length < 1) {
            $(this).append('<input type="hidden" class="order_l">');
        }
        if ($(this).find('.orderBy_l').length < 1) {
            $(this).append('<input type="hidden" class="orderBy_l">');
        }
        if (options.orderBy != undefined && options.orderBy == defaults.orderBy) {
            if (options.order == undefined) {
                options.order = (defaults.order == 'asc' ? 'desc' : 'asc');
            }
        } else {
            if (options.orderBy != undefined) {
                options.order = 'asc';
            }
        }
        options = $.extend(true, {}, defaults,options);
        if(!options.autoCount && options.totalCount <= 0){
            options.pageSize++;
        }

        if(options.loading && options.replace){
            $.showLoading();
        }
        if (options.type == 'post') {
            submitLight(options);
        } else if (options.type== 'ajax') {
            options.type = 'post';
            ajaxLight(options);
        }
    };
    $.fn.customAjaxSubmit=function(options){
        var defaults={
            loading:true
        };
        options = $.extend(true, {}, defaults,options);
        var oldSuccess= options.success;
        var oldError = options.error;
        options.success = function(responseText){
            $.closeLoading();
            if(responseText.indexOf("~!@#$%^*")<0){
                oldSuccess(responseText);
            }else{
                window.location.reload();
            }
        };
        options.error = function(responseText) {
            $.closeLoading();
            if (responseText.indexOf("~!@#$%^*") < 0) {
                oldError(responseText)
            }  else {
               window.location.reload();
            }
        };
        if(options.loading){
            $.showLoading();
        }
        $(this).ajaxSubmit(options);
    } ;
    $.customAjax=function(url,option){
        // If url is an object, simulate pre-1.5 signature
        if ( typeof url === "object" ) {
            option = url;
            url = undefined;
        }
        var defaults={
            loading:false
        };
        // Force options to be an object
        option = $.extend(true, {}, defaults,option);
        var oldSuccess = option.success;
        option.success = function(responseText){
            $.closeLoading();
            if(responseText.indexOf("~!@#$%^*")<0){
                oldSuccess(responseText);
            }else{
                window.location.reload();
            }
        };
        if(option.loading){
            $.showLoading();
        }
        $.ajax(url,option);
    };
    $.redirectURL=function(uri){
        var localObj = window.location;
        var contextPath = localObj.pathname.split("/")[1];
        var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
        var url = basePath+uri;
        window.location.href=url;
    }
    $.showLoading=function(options){
        var defaults ={
            title:"加载数据",
            content:"数据加载中，请稍后"
        }
        options = $.extend(true, {}, defaults,options);
        var $loadModal = $("#loadingModal");
        $loadModal.find("#loadingTitle").html(options.title);
        $loadModal.find("#loadingContent").html(options.content);
        $("#loadingModal").show();
        $('<div class="modal-backdrop fade in" id="loadingBackDrop"></div>').appendTo(document.body);
        $(".modal-backdrop:last").css("z-index", parseInt($(".modal-backdrop:last").css("z-index")) + 20);
    }
    $.closeLoading=function(){
        $("#loadingBackDrop").remove();
        $("#loadingModal").hide();
    }
    $.getRandomStr =function(length){
        if(!length){
            length = 8;
        }
        var x="123456789poiuytrewqasdfghjklmnbvcxzQWERTYUIPLKJHGFDSAZXCVBNM";
        var tmp="";
        for(var i=0;i< length;i++) {
            tmp += x.charAt(Math.ceil(Math.random()*100000000)%x.length);
        }
        return tmp;
    }
    function ajaxLight(options){
        var defaults = {
            success:function(json){
                if(json.indexOf("~!@#$%^*")>=0){
                    window.location.reload();
                }
                $.closeLoading();
                var data = JSON.parse(json);
                if (data.returnCode == 1) {
                    if (options.replace) {
                        $('#' + getLightId(options.lightId)).html(data.content.data);
                    } else {
                        var lastToken = Page.toPages.get(options.pageId + "_last");
                        if (lastToken != options.token) {
                            // 不是上一个分页请求的页码
                            return;
                        }
                        Page.toPages.remove(options.pageId + "_last");
                        var $page = $("#" + getLightId(options.pageId));
                        var content = $(data.content.data).find("#" + getLightId(options.pageId)).html();
                        $page.html(content);
                        $page.find("#autoCount").val("false");
                    }
                } else {
                    $.showMessage({message:data.returnMsg,parentId:options.messageParentId});
                }
                if (options.replace) {
                    // 只刷页码的时候，不通知回调
                    options.result(data);
                }
            },
            error:function(data) {
                if (data.status == 0) {
                    // 用户请求取消时，出错不展示
                    return;
                }
                $.showMessage({message:data.status + " " + data.responseText, parentId:options.messageParentId});
            }
        };
        options = $.extend(true, {}, jQuery.ajaxSettings, options,defaults);
        var data = [];
        for (var i = 0; i < options.params.length ;i++) {
            data.push(options.params[i].name + '=' +  options.params[i].value);
        }
        var json = encodeURIComponent(searchJson(options.id,options.prefix));
        if (json != '') {
            data.push('search='+json);
        }
        if (options.order != '' && options.orderBy!= ''){
            data.push('order='+options.order);
            data.push('orderBy='+options.orderBy);
        }
        data.push('pageId='+options.pageId);
        data.push('pageNo='+options.pageNo);
        data.push('pageSize=' + options.pageSize);
        data.push('totalCount=' + options.totalCount);
        data.push('autoCount=' + options.autoCount);
        data.push('lightId='+getLightId(options.lightId));
        options.data = data.join("&");
        $.ajax(options);
    }

    function submitLight(options){
        var createForm = function(){
            //create form
            var form = jQuery('<form  action="'+options.url+'" method="post"></form>');
            if (options.params.length > 0) {
                for (var i = 0 ;i < options.params.length;i++) {
                    jQuery('<input type=\'hidden\' name=\'' + options.params[i].name + '\' value=\'' + options.params[i].value + '\' />').appendTo(form);
                }
            }
            jQuery(form).appendTo('body');
            return form;
        };
        var json = searchJson(options.id,options.prefix);
        if (json != '') {
            options.params.push({name:'search',value:json});
        }
        if (options.order != '' && options.orderBy!= ''){
            options.params.push({name:'order',value:options.order});
            options.params.push({name:'orderBy',value:options.orderBy});
        }
        options.params.push({name:'pageNo',value:options.pageNo});
        options.params.push({name:'pageSize',value:options.pageSize});
        options.params.push({name:'pageId',value:options.pageId});
        var form = createForm();
        form.submit();
        form.remove();
    }

    var getLightId = function(id){
        return "table_l_" + id;
    };
    var lightTableObject = new HashMap();
    $.fn.lightTable = function(options){
        var plugin = lightTableObject.get($(this).attr('id'));
        if (options === 'search') {
            return search.apply(plugin, Array.prototype.splice.call(arguments, 1));
        } else if (options === 'toPage') {
            return toPage.apply(plugin, Array.prototype.splice.call(arguments, 1));
        } else if (options === 'order') {
            return order.apply(plugin, Array.prototype.splice.call(arguments, 1));
        } else if (options === 'bind') {
            return bind.apply(plugin, Array.prototype.splice.call(arguments, 1));
        } else if (options === 'getCurrentPage'){
            return plugin.pageNo;
        }
        plugin = this;
        var defaults = {
            pagination:true,
            pageUrl:'',
            pageId:'page',
            pageSize:20,
            bindClass:'check-all',
            result:function(data){}
        };
        options = $.extend(true, {}, defaults,options);
        plugin.pagination = options.pagination;
        plugin.bindClass = options.bindClass;
        plugin.pageUrl = options.pageUrl;
        plugin.pageId = options.pageId;
        plugin.result = options.result;
        if (!plugin.pagination) {
            plugin.pageSize = Number.MAX_VALUE;
        } else {
            plugin.pageSize = options.pageSize;
        }
        plugin.pageNo = 1;
        plugin.l_data = [];
        function Row(jQuery,id){
            this.jQuery = jQuery;
            this.id = id;
            this.columnData = new HashMap();
        }
        var indexMap = new HashMap();
        var index = 0;
        $(plugin).find('thead > tr:last > th').each(function(){
            var name = $(this).attr('name');
            if (name != undefined) {
                indexMap.put(index, name);
            }
            index++;
        });
        index = 0;
        $(plugin).find('tbody > tr').each(function(){
            if (!$(this).hasClass('ignore')){
                var row = new Row($(this),index);
                if (indexMap.size() > 0) {
                    var columnIndex = 0;
                    $(this).find('td').each(function(){
                        var name = indexMap.get(columnIndex);
                        if (name != undefined && name != null) {
                            var value = $(this).attr('value');
                            if (value != undefined && value != null && value != '') {
                                row.columnData.put(name, value);
                            } else {
                                row.columnData.put(name, $(this).html());
                            }
                        }
                        columnIndex++;
                    })
                }
                plugin.l_data.push(row);
                if (options.pagination && index >= options.pageSize) {
                    row.jQuery.addClass('hide');
                }
            }
            index++;
        });
        lightTableObject.put($(plugin).attr('id'), plugin);
        function search(searchId, prefix) {
            if (searchId == undefined || searchId == null) {
                searchId = 'search_l';
            }
            if (prefix == undefined || prefix == null) {
                prefix = 'search__';
            }
            var search = searchObjects('#'+searchId, prefix);
            var current = 0;
            for (var index = 0; index < this.l_data.length;index++) {
                var row = this.l_data[index];
                var match = true;
                for (var searchIndex = 0 ; searchIndex < search.length;searchIndex++) {
                    var searchObject = search[searchIndex];
                    var value = row.columnData.get(searchObject.propertyName);
                    if(searchObject.propertyName=="serverTypeSearch"){
                        var searchValues = searchObject.value.split(",");
                        for(var i=0;i<searchValues.length;i++){
                            match = false;
                            if (value.toLowerCase().indexOf(searchValues[i].toLowerCase()) != -1) {
                                match = true;
                                break;
                            }
                        }
                    }else{
                        if (searchObject.condition == SearchObject.EQ) {
                            if (value != searchObject.value) {
                                match = false;
                                break;
                            }
                        } else if (searchObject.condition == SearchObject.LIKE) {
                            if (value.toLowerCase().indexOf(searchObject.value.toLowerCase()) == -1) {
                                match = false;
                                break;
                            }
                        } else if (searchObject.condition == SearchObject.GE) {
                            if (value < searchObject.value){
                                match = false;
                                break;
                            }
                        }else if (searchObject.condition == SearchObject.GT) {
                            if (value <= searchObject.value){
                                match = false;
                                break;
                            }
                        }else if (searchObject.condition == SearchObject.LE) {
                            if (value > searchObject.value){
                                match = false;
                                break;
                            }
                        }else if (searchObject.condition == SearchObject.LT) {
                            if (value >= searchObject.value){
                                match = false;
                                break;
                            }
                        }else if(searchObject.condition== SearchObject.IN) {
                            var searchInValue = searchObject.value.split(",");
                            match = false;
                            for(var i=0;i<searchInValue.length;i++){
                                if(searchInValue[i].toLowerCase()==value.toLowerCase()) {
                                    match = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!match){
                    row.jQuery.addClass('notMatch').addClass('hide');
                    $(row.jQuery).find('.' + this.bindClass).attr('checked', false);
                } else {
                    row.jQuery.removeClass('notMatch');
                    if (current < this.pageSize) {
                        row.jQuery.removeClass('hide');
                        current++;
                    } else {
                        row.jQuery.addClass('hide');
                        $(row.jQuery).find('.' + this.bindClass).attr('checked', false);
                        current++;
                    }
                }
            }
            //reset.apply(this);
            plugin.pageNo = 1;
            if (plugin.pagination) {
                Page.reset(this.pageUrl, this.pageId, 1, this.pageSize, current);
            }else{
                plugin.result(current);
            }

        }
        function toPage(pageId, pageNo,pageSize){
            var min = (pageNo-1) * pageSize;
            var max = pageNo * pageSize;
            var current = 0;
            for (var index =0; index < this.l_data.length;index++) {
                var row = this.l_data[index];
                var display = false;
                if (!row.jQuery.hasClass('notMatch')) {
                    if (current >= min && current < max) {
                        row.jQuery.removeClass('hide');
                        display = true;
                        current++;
                    } else {
                        current++;
                    }
                }
                if (!display) {
                    row.jQuery.addClass('hide');
                }
            }
            reset.apply(this);
            plugin.pageNo = pageNo;
            plugin.pageSize = pageSize;
            Page.reset(this.pageUrl, pageId, pageNo, pageSize, current);
        }

        function order(orderBy, order,isNumber) {
            var current = 0;
            this.l_data = this.l_data.sort(function(row1, row2){
                var val1 = row1.columnData.get(orderBy);
                var val2 = row2.columnData.get(orderBy);
                if (order == 'asc') {
                    var isGreater;
                    if(isNumber) {
                        isGreater = (val1-0) >= (val2-0);
                    }else{
                        isGreater = val1.toLowerCase() >= val2.toLowerCase();
                    }

                    if(isGreater){return 1;}else{return -1};
                } else {
                    var isLess;
                    if(isNumber) {
                        isLess = (val1-0) < (val2-0);
                    }else {
                        isLess = val1.toLowerCase() < val2.toLowerCase();
                    }
                    if(isLess){return 1;}else{return -1};
                }
            });
            var tbody = $(this).find('tbody');
            for (var index = 0; index < this.l_data.length;index++) {
                var row = this.l_data[index];
                var detach = $(row.jQuery).detach();
                $(tbody).append(detach);
                if (!$(detach).hasClass('notMatch')) {
                    if (current < this.pageSize) {
                        $(detach).removeClass('hide');
                    } else {
                        $(detach).addClass('hide');
                    }
                    current++;
                }
            }
            //reset.apply(this);
            plugin.pageNo = 1;
            if (plugin.pagination) {
                Page.reset(this.pageUrl, this.pageId, 1, this.pageSize, current);
            }
        }

        function bind(val) {
            if (val) {
                for (var index = 0; index < this.l_data.length;index++) {
                    var row = this.l_data[index];
                    if (!$(row.jQuery).hasClass('hide')) {
                        $(row.jQuery).find('.' + this.bindClass).attr('checked', true);
                    }
                }
            } else {
                reset.apply(this);
            }

        }

        function reset() {
            $(this).find('.' + this.bindClass).attr('checked', false);
        }
    };

    $.submitFormData = function(options){
        var defaults = {
            url:'',
            loading:true,
            target:'_self',
            method:'post',
            params:[],
            search:'',
            prefix:'search'
        };
        options = $.extend(true, {}, defaults,options);
        if(options.loading){
            $.showLoading();
        }
        var createForm = function(){
            var form = jQuery('<form  action="'+options.url+'" method="'+options.method + '" target="' + options.target +'" ></form>');
            if (options.params.length > 0) {
                for (var i = 0 ;i < options.params.length;i++) {
                    jQuery('<input type=\'hidden\' name=\'' + options.params[i].name + '\' value=\'' + options.params[i].value + '\' />').appendTo(form);
                }
            }
            jQuery(form).appendTo('body');
            return form;
        };
        if(options.search !=""){
            var json = searchJson("#"+options.search,options.prefix);
            if (json != '') {
                options.params.push({name:'search',value:json});
            }
        }
        var form = createForm();
        form.submit();
        form.remove();
    };

    var searchObjects = function(id,prefix){
        if ($(id) != undefined) {
            var objects=[];
            $(id).find('[name^='+prefix+']').each(function(){
                if ($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio') {
                    if (! ($(this).is(':checked'))) {
                        return;
                    }
                }
                var name = $(this).attr('name');
                var split = name.split('__');
                var key = split[1];
                if (split.length >= 2) {
                    var condition = split[2];
                }
                var val = $(this).val();
                if ($(this).attr("untrim") == undefined) {
                    val = $.trim(val);
                }
                var resultVal = [];
                if ( val != '' && val != null && val != undefined) {
                    if(val.constructor != Array){
                        resultVal[0] = val;
                    }else{
                        resultVal = val;
                    }
                    if(condition==SearchObject.IN) {
                        objects.push(new SearchObject(key, condition, val.toString(),'string'));
                    }else{
                        for(var i = 0; i < resultVal.length; i++){
                            var parse = $(this).attr('parse');
                            if (parse == 'int' || parse == 'long' || parse == 'short' || parse == 'integer') {
                                val = parseInt(resultVal[i]);
                            } else if (parse == 'float' || parse == 'double') {
                                val = parseFloat(resultVal[i]);
                            } else if (parse == 'boolean'){
                                val = resultVal[i];
                            } else {
                                if (parse == undefined || parse.length < 1) {
                                    parse = 'string' ;
                                }
                                val = resultVal[i];
                                if(condition!=''&&condition!=null&&condition!=undefined&&condition.indexOf("like")>-1){
                                    if(val.indexOf("\\")>-1){
                                        val = (val+"").replace("\\","\\\\");
                                    }
                                    if(val.indexOf("%")>-1){
                                        val = (val+"").replace("%","\\%");
                                    }
                                }
                            }
                            objects.push(new SearchObject(key, condition, val,parse));
                        }
                    }
                }
            });
            return objects;
        }
        return [];
    };

    searchJson = function(id,prefix){
        return JSON.stringify(searchObjects(id, prefix));
    };

    $.fn.submitResult = function(data,options){
        var defaults = {
            messageParentId: 'message_l',
            css:'',
            opacity:'slow',
            delay:15000,
            replace:false,
            lightId:'',
            mode:'plain',
            showSuccess:false,
            autoHide:true,
            success:function(data){}
        };
        options = $.extend(true, {}, defaults, options);
        var result = JSON.parse(data);
        $(this).find('.form-group').removeClass('has-error');
        $(this).find('.help-block').remove();
        $(this).find('.blur-form-error').removeClass('blur-form-error');
        if (result.returnCode == 0) {
            if (Object.prototype.toString.apply(result.content) === '[object Array]') {
                var showMessage = '';
                for (var i = 0 ;i < result.content.length;i++) {
                    for (var property in result.content[i]){
                        if (property != undefined && result.content[i].hasOwnProperty(property)) {
                            var input = $(this).find('[name="'+property+'"]');
                            if (input.length > 0) {
                                var parent = $(input).parents('.form-group');
                                if (parent != undefined && parent.length > 0) {
                                    $(parent).addClass('has-error');
                                    var span = jQuery('<p class="help-block">' + result.content[i][property]+'</p> ');
                                    $(input).parent().append(span);
                                } else {
                                    $(input).addClass('blur-form-error');
                                    if (showMessage != '') {
                                        showMessage = showMessage + '<br/>' + result.content[i][property];
                                    }else {
                                        showMessage = result.content[i][property];
                                    }
                                }
                            } else {
                                if (showMessage != '') {
                                    showMessage = showMessage + '<br/>' + result.content[i][property];
                                }else {
                                    showMessage = result.content[i][property];
                                }
                            }
                        }
                    }
                }
                if (showMessage != '') {
                    $.showMessage({message:showMessage,parentId:options.messageParentId,autoHide:options.autoHide});
                }
                $(this).find(".has-error.form-group input[name]:first").focus();
            } else {
                $.showMessage({message:result.returnMsg, parentId:options.messageParentId,mode:options.mode,autoHide:options.autoHide});
            }
        } else {
            if (options.showSuccess) {
                if (options.replace) {
                    $('#' + getLightId(options.lightId)).html(result.content.data);
                } else {
                    $.showMessage({type:'success',message:result.returnMsg,parentId:options.messageParentId,mode:options.mode});
                }
            }

            options.success(result);
        }
    };

    $.fn.pieChart = function(data,highOptions) {
        var defaults = {
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return '<b>'+ this.point.name +'</b>: '+ this.y + ' ' + defaults.unit ;
                        }
                    }
                }
            }
        };
        var options = $.extend(true, {}, defaults, highOptions);
        return dChart(this, 'pie', data, options);

    };

    $.fn.lineChart = function(data, highOptions){
        return dChart(this,'line', data,  highOptions);
    };

    $.fn.barChart = function(data,highOptions){
        return dChart(this,'bar', data,  highOptions);
    };

    $.fn.columnChart = function(data,highOptions){
        return dChart(this,'column', data,  highOptions);
    };

    $.fn.splineChart = function(data,highOptions) {
        return dChart(this, 'spline', data,  highOptions);
    };

    function dChart(id,type,data,highOptions){
        var tmpHighOptions = {
            title:{
                text:data.title
            },
            xAxis:{
            }
        };
        tmpHighOptions.chart = {
            defaultSeriesType: type,
            renderTo:$(id).attr('id')
        };
        var series = [];
        if (data.categories) {
            var hashMap = new HashMap();
            var categories = [];

            for (var j = 0 ; j< data.rows.length;j++) {
                var obj = data.rows[j];
                if (obj.points.length > 0){
                    categories.push(obj.name);
                    for (var k = 0 ;k < obj.points.length ;k++) {
                        var p = hashMap.get(obj.points[k].x);
                        if (p == undefined) {
                            hashMap.put(obj.points[k].x, [obj.points[k].y]);
                        } else {
                            p.push(obj.points[k].y);
                        }
                    }
                }
            }
            var keys = hashMap.keys();
            for (j = 0 ;j< keys.length;j++){
                series.push({name:keys[j], data:hashMap.get(keys[j])});
            }
            tmpHighOptions.series = series;
            tmpHighOptions.xAxis.categories = categories;
        } else {
            for (var i = 0 ;i < data.rows.length;i++) {
                obj = data.rows[i];
                series.push({name:obj.name, data:obj.points});
            }
            tmpHighOptions.series = series;
        }

        $.extend(true, tmpHighOptions, highOptions);
        return new Highcharts.Chart(tmpHighOptions);
    }

    $.showMessage = function(options) {
        var defaults = {
            type: 'error',/*success,info,error*/
            message:'',
            messageId:'',
            parentId:'message_l',
            opacity:'slow',
            delay :3000,
            autoHide:true,
            append:false,/*显示信息时，是否清空之前的信息*/
            mode:'plain' /*plain,modal,alert*/
        };
        options = $.extend(true, {}, defaults,options);
        var loader;
        if (options.messageId != '') {
            loader = $('#' + options.messageId);
        } else {
            if (options.type == 'error') {
                loader = $('#' + options.parentId).find('.error_message');
            } else if (options.type == 'success') {
                loader = $('#' + options.parentId).find('.success_message')
            } else {
                loader = $('#' + options.parentId).find('.info_message')
            }
        }
        if (options.mode == 'alert')  {
            if (!options.append) {
                $(loader).html(options.message);
            } else {
                $(loader).append(options.message);
            }
            $(loader).prepend('<a class="close" data-dismiss="alert" href="#">&times;</a>');
            $(loader).fadeIn(options.opacity, null);
        } else if (options.mode == 'modal'){
            var $message_l_content = $('#message_l_content');
            if (!options.append) {
                $message_l_content.html('');
            }
            var clone = $(loader).clone();
            $(clone).html(options.message);
            $(clone).css('display', '');
            $(clone).appendTo($message_l_content);
            $('#message_l_modal').modal({backdrop:'static',show:'show'});
        } else {
            if (!options.append) {
                $(loader).children('span').html(options.message);
            } else {
                $(loader).children('span').append(options.message);
            }
            $(loader).removeClass("hide");
            $(loader).fadeIn(options.opacity, null);
            if (options.autoHide) {
                setTimeout(function(){$(loader).fadeOut('fast',null);}, options.delay);
            }
        }
    };

    $.fn.bindCheckbox = function(options){
        var defaults = {
            className: 'check-all',
            childPrefix:'child-of-'
        };
        options = $.extend(true, {}, defaults,options);
        var plugin = this;
        $(plugin).find('input[type=checkbox].'+options.className).each(function(){
            $(this).bind('change',function(){
                if ($(this).is(':checked')){
                    $(plugin).find('.' + options.childPrefix + $(this).attr('id')).each(function(){
                        $(this).attr('checked', true);
                        $(this).change();
                    })
                } else {
                    $(plugin).find('.' + options.childPrefix + $(this).attr('id')).each(function(){
                        $(this).attr('checked', false);
                        $(this).change();
                    })
                }
            })
        });
    };
})(jQuery);
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
function Page(pageId, pageNo,pageSize, replace, autoCount, totalCount) {
    this.pageId = pageId;
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.replace = replace;
    this.autoCount = autoCount;
    this.totalCount = totalCount;
}

/**
 * default submit search
 */
Page.toPages = new HashMap();
Page.registerToPage = function(toPage,id){
    Page.toPages.put(id,toPage);
};
Page.reset = function(url, pageId, pageNo, pageSize, totalCount) {
    $('#page').lightRequest({
        url:url,
        type:'ajax',
        params:[
            {name:'totalCount', value:totalCount}
        ],
        pageSize:pageSize,
        pageNo:pageNo,
        pageId:pageId,
        async:false,
        lightId:'page',
        loading:false

    });
};
Page.prototype.toPage = function(){
    var func = Page.toPages.get(this.pageId);
    // 加入token，防止由于请求慢的时候，后续的错误页码替换了前面的页码
    var token = new Date().getTime();
    Page.toPages.put(this.pageId + "_last", token);
    func(this.pageId, this.pageNo, this.pageSize, this.replace, this.autoCount, this.totalCount, token);
};
function SearchObject(key,condition,value,type) {
    this.propertyName = key;
    this.condition = condition;
    this.value = value;
    this.type = type;
}

SearchObject.EQ = "eq";
SearchObject.GE = 'ge';
SearchObject.GT = 'gt';
SearchObject.LE = 'le';
SearchObject.LT = 'lt';
SearchObject.LIKE = 'like';
SearchObject.IN = 'in';

function HashMap() {
    var obj = new Object();
    this.put = function(key,value) {
        obj[key] = value;
    } ;
    this.get = function (key) {
        if (obj.hasOwnProperty(key)) {
            return obj[key];
        } else {
            return null;
        }
    };
    this.contains = function(key){
        return obj.hasOwnProperty(key)
    };
    this.remove = function(key) {
        if (obj.hasOwnProperty(key)) {
            var value = obj[key];
            delete obj[key];
            return value;
        } else {
            return null;
        }
    } ;
    this.size = function(){
        return Object.getOwnPropertyNames(obj).length;
    } ;
    this.keys = function() {
        return Object.getOwnPropertyNames(obj);
    };
    this.removeAll = function() {
        var properties = this.keys();
        for (var i = 0 ;i < properties.length;i++) {
            this.remove(properties[i]);
        }
    };


}

function HashSet(){
    var present = new Object();
    var hashMap = new HashMap();
    this.put = function(key) {
        if (hashMap.contains(key)) {
            return false;
        } else {
            hashMap.put(key, present);
            return true;
        }
    };
    this.contains = function(key) {
        return hashMap.contains(key);
    };
    this.remove = function(key) {
        hashMap.remove(key);
    };
    this.size = function() {
        return hashMap.size();
    };
    this.getAll = function(){
        return hashMap.keys();
    };
    this.removeAll = function() {
        hashMap.removeAll();
    };

}
/**
 * 初始化多级的modal，解决出现多级对话框时，当弹出二级对话框，再关掉，会导致出现滚动条
 * @param options
 */
$.fn.rootModal = function(options) {
    $(this).unbind('hidden.bs.modal', close).bind('hidden.bs.modal', close)
        .unbind('shown.bs.modal', open).bind('shown.bs.modal', open);

    function open() {
        $("body").addClass("modal-open-custom");
    }

    function close() {
        $("body").removeClass("modal-open-custom");
    }

    return $(this).modal(options);
}

$(function () {
    $(window).resize(function () {
        $('.modal').each(function () {
            var left = ($(window).width() - $(this).outerWidth()) / 2;
            var top = ($(window).height() - $(this).outerHeight()) / 2 * 0.4;
            var maxHeight = $(window).height()-1.7*top;
            $(this).css({
                position: 'fixed',
                left: left < 0 ? 0 : left,
                top: top < 0 ? 20 : top,
                margin: 0,
                maxHeight: maxHeight
            });
            if ($(this).outerHeight() > $(window).height()) {
                $(this).css({
                    height: $(window).height() - 40,
                    overflowY: 'auto'
                });
            } else {
                $(this).css({
                    height: 'auto',
                    overflowY: 'auto'
                });
                if ($(this).outerHeight() > $(window).height()) {
                    left = ($(window).width() - $(this).outerWidth()) / 2;
                    top = ($(window).height() - $(this).outerHeight()) / 2 * 0.4;
                    $(this).css({
                        position: 'fixed',
                        left: left < 0 ? 0 : left,
                        top: top < 0 ? 20 : top,
                        margin: 0,
                        overflowY: 'auto'
                    });
                    $(this).css({
                        height: $(window).height() - 40
                    });
                }
            }
        });
    });
    // 最初运行函数
    $(window).resize();
});

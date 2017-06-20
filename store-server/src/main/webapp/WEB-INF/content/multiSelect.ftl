<#macro mulitSelect optionList  panelId inputName canModify="true">
    <style>
        .control_btn{cursor:default;width:170px !important; padding-right:30px !important;background:url(../../img/combo_arrow.jpg) 175px -1px no-repeat;}
    </style>
    <script type="text/javascript" charset="utf-8">

        <#--document.onclick = function(){-->
            <#--if($(${panelId}).css('display') == 'none'){-->
<#--//                alert("进来了")-->
                <#--$(${panelId}).hide();-->
            <#--}-->
            <#--&lt;#&ndash;$(${panelId}).hide();&ndash;&gt;-->
        <#--}-->
         var index;
        var values={};
        function selectOption(name, id) {
            if ($('#checkbox_' + id).attr('checked') == undefined) {
                $('#checkbox_' + id).attr('checked', true);
                var value = '';
                for (var item in values) {
                    value += values[item] + ',';
                }
                $('#svId').attr('value', value.replace(/\s+/g, "") + name.replace(/\s+/g, ""));
                values[id] = name;
            } else {
                delete values[id];
                var value = '';
                for (var item in values) {
                    value += values[item] + ',';
                }
                $('#svId').attr('value', value.substring(0, value.length - 1).replace(/\s+/g, ""));
                $('#checkbox_' + id).attr('checked', false);
            }
        }

            <#--function hideALlServiceType(){-->
                <#--if ($(${panelId}).css('display') == 'none') {-->
<#--//                    alert($('#svId').offset().left);-->
                    <#--$(${panelId}).css('left', $('#svId').offset().left);-->
                    <#--$(${panelId}).css('top', $('#svId').offset().top+28);-->
                    <#--$(${panelId}).show();-->
                <#--} else {-->
                    <#--$(${panelId}).hide();-->
                <#--}-->
            <#--}-->

        function addListener(element, e, fn) {
            if (element.addEventListener) {
                element.addEventListener(e, fn, false);
            } else {
                element.attachEvent("on" + e, fn);
            }
        }

        <#if canModify=="true">
        addListener(document, "click",
                function (evt) {
                    var target = evt.srcElement || evt.target;
                    var panelId="${panelId}";
                    if (target == document.getElementById("svId")) {
                        if ($(${panelId}).css('display') == 'none') {
                            $(${panelId}).css('left',target.offsetLeft);
                            $(${panelId}).css('top',target.offsetTop+28);
                            $(${panelId}).show();
                        } else {
                            $(${panelId}).hide();

                        }
                    }
                    else {
                        var downPanel = false;
                        while (target) {
                            downPanel = target ==${panelId};
                            if (downPanel) break;
                            target = target.parentNode;
                        }
                        if (!downPanel) {
                            $(${panelId}).hide();
                        }
                    }
                });
        document.close();
        </#if>
    </script>



    <div class="controls">
        <input type="text" id="svId" onfocus='this.blur()' class="control_btn " >
    </div>

    <div id="${panelId}" class="panel"
        <#--style="position: absolute; z-index: 9002; display: none; width: 200px; left:156px;top: 207px;">-->
        style="position: absolute;  width: 196px;display: none">
        <div title="" style="width: 190px;">
            <#list optionList as option>
                <div onclick="selectOption('${option.name}','${option.id?c}')"
                    class="combobox-item combobox-item-selected">
                    <input id="checkbox_${option.id?c}"
                        onclick="selectOption('${option.name}','${option.id?c}')" type="checkbox" name="${inputName}"
                        style="display:inline-block;vertical-align:middle;" value="${option.id?c}">
                        <span style="display:inline-block;vertical-align:middle;">${option.name}
                        </span>
                </div>
            </#list>
        </div>
    </div>



</#macro>
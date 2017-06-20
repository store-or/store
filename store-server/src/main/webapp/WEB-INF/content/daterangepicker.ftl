<#macro daterangepicker id startName endName label minDate startTime="" endTime="" defaultTimeSpanHtml="" divClass="" opens="right" isCustomAllTime=0 maxDate="new Date()" single="false" callback="changeTimeCallback">
<div id="daterange_${id}" <#if divClass == "">class="form-control" style="margin-right:5px;width:265px;"<#else>class="${divClass}"</#if>>
    <span>${label}</span>
    <i class="fa fa-calendar"></i>
        <span id="${id}_span">
            <#if defaultTimeSpanHtml=="" && isCustomAllTime==0>
                <span style='padding:0 45px;'>所有时间</span>
            <#else>
                ${defaultTimeSpanHtml!""}
            </#if>
        </span>
    <b class="caret"></b>
</div>
<input parse="long" type="hidden" id="${id}_start_time" value="${startTime}" name="${startName}">
<input parse="long" type="hidden" id="${id}_end_time" value="${endTime}" name="${endName}">

<script type="text/javascript" >
    $(document).ready(function() {
        $('#' + 'daterange_${id}').daterangepicker(
                {
                    ranges: {
                        '今天': [new Date(), new Date()],
                        '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                        '过去7天': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                        '过去30天': [moment().subtract(31, 'days'), moment().subtract(1, 'days')]
                        <#if isCustomAllTime == 0>
                            ,
                            '所有时间': [${minDate}, ${maxDate}]
                        </#if>
                    },
                    locale:{
                        applyLabel: '确定',
                        cancelLabel: '取消',
                        fromLabel: '从',
                        toLabel: '到',
                        weekLabel: 'W',
                        customRangeLabel:'自定义',
                        daysOfWeek: ['周日', '周一', '周二', '周三', '周四', '周五','周六'],
                        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
                    },
                showDropdowns:true,
                singleDatePicker: <#if single == "true" >true<#else>false</#if>,
                startDate:<#if !startTime?? || startTime == "" >${minDate}<#else>new Date(${startTime})</#if>,
                    minDate:${minDate},
                endDate:<#if !endTime?? || endTime == "" >${maxDate}<#else>new Date(${endTime})</#if>,
                    maxDate:${maxDate},
                    opens: '${opens}',
                    format: 'YYYY-MM-DD'
                },
                function(start, end) {
                    var startTime = start.toDate().getTime();
                    var minDate = this.minDate.toDate().getTime();
                    var endTime = end.format('YYYY.MM.DD');
                    var maxDate = this.maxDate.format('YYYY.MM.DD');
                    var showTimeOnInit = <#if isCustomAllTime == 0 >false<#else>true</#if>;
                    if(startTime == minDate && endTime == maxDate && !showTimeOnInit){
                        $('#' + '${id}_span').html("<span style='padding:0 45px;'>所有时间</span>");
                        $('#' + '${id}_start_time').val("");
                        $('#' + '${id}_end_time').val("");
                    }else{
                        <#if single == "true" >
                            $('#' + '${id}_span').html(end.format('YYYY.MM.DD'));
                        <#else>
                            $('#' + '${id}_span').html(start.format('YYYY.MM.DD') + ' - ' + end.format('YYYY.MM.DD'));
                        </#if>
                        $('#' + '${id}_start_time').val(start.toDate().getTime());
                        $('#' + '${id}_end_time').val(end.toDate().getTime());
                    }
                    <#if callback??>
                        try {
                            if (typeof eval("${callback}()") == "function") {
                                ${callback}();
                            }
                        } catch(e) {}
                    </#if>

                }
        );
        $("div.daterangepicker_start_input").hide();
        $("div.daterangepicker_end_input").hide();
    });
</script>
</#macro>
<#macro datesinglepicker id timeName label defaultTimeSpanHtml="" curTime="" minDate="">
<style type="text/css">
    .datepacker-cls {
        display:inline-block;
        margin:0px 5px 0 0;
        width: 135px;
        height: 30px;
        padding: 5px 12px;
        font-size: 14px;
        line-height: 1.428571429;
        color: #666;
        vertical-align: middle;
        background-color: #ffffff;
        border: 1px solid #cccccc;
        border-radius: 3px;

        -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
        box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
        -webkit-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
        transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
    }
</style>
<div id="datesingle_${id}" class="datepacker-cls">
    <span>${label}</span>
    <i class="fa fa-calendar"></i>
    <span id="${id}_span">${defaultTimeSpanHtml!""}</span>
    <b class="caret"></b>
</div>
<script type="text/javascript" >
    $(document).ready(function() {
        $('#' + 'datesingle_${id}').daterangepicker(
                {
                    locale:{
                        daysOfWeek: ['周日', '周一', '周二', '周三', '周四', '周五','周六'],
                        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
                    },
                    showDropdowns: true,
                    startDate:new Date(${curTime}),
                    <#if minDate?? && minDate!="">
                    minDate: new Date(${minDate}),
                    </#if>
                    format: 'YYYYMMDD',
                    singleDatePicker:true
                },
                function(start, end) {
                    var startTime = start.startOf('day').toDate().getTime();
                    $('#' + '${timeName}').val(startTime);
                    $('#' + '${id}_span').html(start.format('YYYYMMDD'));
                }
        )
        .on("show.daterangepicker", function(){
            // 由于modal强制获取焦点，去掉该事件监听
            $(document).off('focusin.bs.modal');
        })
        .on("hide.daterangepicker", function(){
            // 还原modal强制获取焦点，去掉该事件监听
            var modalDiv = $(this).parents(".modal.fade.in");
            if (modalDiv.length > 0) {
                modalDiv.modal('show');
            }
        });

    });
</script>
</#macro>
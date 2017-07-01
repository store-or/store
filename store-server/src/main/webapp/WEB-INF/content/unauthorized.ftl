<html>
<head>
    <title>没有权限访问</title>
    <link  href="${absoluteContextPath}/css/ott.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${absoluteContextPath}/css/pages.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery/jquery-1.8.2.min.js"></script>
    <script type="text/javascript">
        function goBack() {
            if ($.browser.mozilla) {
                window.history.go(-1);
            }  else if($.browser.chrome) {
                window.history.go(-2);
            }  else {
                window.history.go(-1);
            }
        }

    </script>
</head>
<body class="page-404">
    <div class="header">
        <div class="demo-logo"><img  src="${absoluteContextPath}/img/error_logo.png" alt="" style="margin-top:-4px;"></div>
        <!-- .logo -->
    </div>
    <div class="demo_error">
        <div class="img_404"> <img src="${absoluteContextPath}/img/error.png" class="img-responsive"> </div>

        <div class="error-text">
            <span class="oops_text"><strong>403</strong></span><br>
            <span class="oops_text">对不起~您无权访问</span>
            <div class="error_web"><a href="javascript:void(0)" onclick="goBack();">返回上一页<<</a></div>
        </div>
    </div>
</body>

</html>
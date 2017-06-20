<%--
  Created by IntelliJ IDEA.
  User: hongxingshi
  Date: 13-1-14
  Time: 下午7:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>访问出错404</title>
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <meta charset="UTF-8">
    <link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/layers.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.2.min.js"></script>
    <!-- 顶部缓存效果 -->
    <%--<link href="<%=request.getContextPath()%>/css/pace-flash/pace-theme-flash.css" rel="stylesheet"/>--%>
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
<body class="page-error">
<main class="page-content">
    <div class="page-inner">
        <div id="main-wrapper">
            <div class="row">
                <div class="col-md-4 center">
                    <div class="panel panel-white" id="js-alerts">
                        <div class="panel-body">
                            <h1 class="text-xxl text-danger text-center">404</h1>
                            <div class="details">
                                <h3>找不到当前页面</h3>
                                <p><a href="javascript:void(0)" onclick="goBack();">返回上一页<<</a></p>
                            </div>

                        </div>
                    </div>
                </div>
            </div><!-- Row -->
        </div><!-- Main Wrapper -->
    </div><!-- Page Inner -->
</main><!-- Page Content -->
<!-- Javascripts -->
<%--<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.2.min.js"></script>
<script src="<%=request.getContextPath()%>/js/pace-flash/pace.min.js"></script>--%>
<!-- 顶部缓冲效果 -->
</body>
</html>
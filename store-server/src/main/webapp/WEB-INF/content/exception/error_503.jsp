<%--
  Created by IntelliJ IDEA.
  User: hongxingshi
  Date: 13-1-14
  Time: 下午7:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" import="org.slf4j.Logger" pageEncoding="utf-8" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%! Logger logger = LoggerFactory.getLogger(getClass());%>
<%
    logger.error(exception.getMessage(),exception);
%>
<html>
<head>
    <title>访问出错503</title>
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/img/ott.ico" type="image/x-icon">
    <link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/layers.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.2.min.js"></script>
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
                            <h1 class="text-xxl text-danger text-center">503</h1>
                            <div class="details">
                                <h3>系统正在维护中...</h3>
                                <p><a href="javascript:void(0)" onclick="goBack();">返回上一页<<</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- Row -->
        </div><!-- Main Wrapper -->
    </div><!-- Page Inner -->
</main><!-- Page Content -->
</body>
</html>
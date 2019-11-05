<%--
  Created by IntelliJ IDEA.
  User: chentao
  Date: 2019/11/4
  Time: 6:11 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>欢迎光临</title>
</head>
<body>
<h3>传统的文件上传方式测试</h3>
<form action="hello/upload" method="post" enctype="multipart/form-data">
    选择文件: <input type="file" name="upload"/><br>
    <input type="submit" value="上传文件"/>
</form>
<br>
<h3>springMVC的文件上传测试</h3>
<form action="hello/uploadFileBySpring" method="post" enctype="multipart/form-data">
    选择文件: <input type="file" name="upload"/><br>
    <input type="submit" value="上传文件"/>
</form>
<br>
<a href="hello/exceptionTest">异常处理类测试</a>
<br>
<a href="hello/testInterceptor">拦截器测试</a>
</body>
</html>

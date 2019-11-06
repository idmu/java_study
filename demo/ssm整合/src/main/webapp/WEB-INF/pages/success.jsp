<%--
  Created by IntelliJ IDEA.
  User: chentao
  Date: 2019/11/5
  Time: 6:23 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>查询所有用户信息</h3>
<br>
<c:forEach items="${list}" var="account">
    ${account.name}
</c:forEach>
</body>
</html>

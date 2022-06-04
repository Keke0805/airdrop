<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022/5/26
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="static java.lang.String.*" %>
<html>
<head>
    <title>共享剪贴板</title>
</head>
<body>
<%
    request.setCharacterEncoding("utf-8");
    String content = request.getParameter("content");
//    out.print(content);
    if(content != null){
        application.setAttribute("clipBoard", content);
    }
    response.sendRedirect("index.jsp");
%>

</body>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Spitter</title>
    <link rel="stylesheet" type="text/css"
          href="<c:url value="/resources/style.css" />">
</head>
<body>
<h1>Register</h1>
<%--可以看到，这个JSP非常基础。它的HTML表单域中记录用户的名字、姓氏、用户名以及密码，然后还包含一个提交表单的按钮。--%>
<%--需要注意的是：这里的\<form\>标签中并没有设置action属性。在这种情况下，当表单提交时，它会提交到与展现时相同的URL路径上。也就是说，它会提交到“/spitter/register”上。--%>
<%--这就意味着需要在服务器端处理该HTTP POST请求。现在，我们在SpitterController中再添加一个方法来处理这个表单提交。--%>
<form method="POST">
    First Name: <input type="text" name="firstName"/><br/>
    Last Name: <input type="text" name="lastName"/><br/>
    Email: <input type="email" name="email"/><br/>
    Username: <input type="text" name="username"/><br/>
    Password: <input type="password" name="password"/><br/>
    <input type="submit" value="Register"/>
</form>
</body>
</html>

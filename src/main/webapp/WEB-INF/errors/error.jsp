<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h2>Error:</h2>
    <p>${exception.message}</p>
    <p>Caused by: <br />${exception.cause}</p>
</body>
</html>

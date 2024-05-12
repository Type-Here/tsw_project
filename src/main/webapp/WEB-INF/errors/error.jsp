<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
    <link type="text/css" rel="stylesheet" href="css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="css/small.css" />
    <link rel="icon" type="image/x-icon" href="img/favicon.ico">
</head>
<body>
    <h2>Error:</h2>
    <p>${exception.message}</p>
    <p>Caused by: <br />${exception.cause}</p>
</body>
</html>

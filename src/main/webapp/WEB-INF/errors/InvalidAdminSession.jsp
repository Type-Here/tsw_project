<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>OPS...Session Expired - Retrogamer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
</head>
<body class="body_def error">
<div class="error_container">
    <h1 class="text-center">OPS... Sessione Non Valida</h1>
    <div class="error_content">
        <div class="general-display-flex centerized-flex-container margin-top-90">
            <h2 class="text-center">Sessione Admin Scaduta o non Valida</h2>
            <span class="text-center">Accedi Nuovamente per Continuare</span>
            <a class="text-center" href="${pageContext.request.contextPath}/admin-access">Admin Login</a>
        </div>
        <div class="general-display-flex centerized-flex-container margin-top-90">
            <span>Messaggio: ${exception.message}</span>
            <p>Error 500</p>
        </div>
    </div>
    <div class="general-display-flex centerized-flex-container">
        <img width="314" alt="Logo Image" src="${pageContext.request.contextPath}/img/logo.png" />
    </div>
</div>
</body>
</html>

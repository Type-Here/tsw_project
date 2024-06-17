<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>OPS...Qualcosa è andato storto - Retrogamer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <title>Retrogamer: Error</title>
</head>
<body class="body_def error">
<div class="error_container">
    <h1 class="text-center">Oops! Qualcosa è andato storto</h1>
    <div class="error_content">
        <div class="general-display-flex centerized-flex-container margin-top-90">
            <h2 class="text-center">Siamo spiacenti, ma si è verificato un errore.</h2>
            <p class="text-center">
                Non riusciamo a trovare la pagina che stai cercando. Potrebbe essere dovuto a: <br>
                Un errore di battitura nell'URL <br>
                Un link scaduto <br>
                Una pagina che non esiste più <br>
            </p>
            <span class="text-center">Torna alla <a href="${pageContext.request.contextPath}/">Home Page</a></span>
            <span class="text-center">Se il problema persiste, contatta l'amministratore del sito. <strong><a href="mailto:supporto@retrogamer.it">supporto@retrogamer.it</a></strong></span>
        </div>
        <div class="general-display-flex centerized-flex-container margin-top-90">
            <c:if test="${exception != null}">
                <span>Messaggio: ${exception.message}</span>
            </c:if>
        </div>
    </div>
    <div class="general-display-flex centerized-flex-container">
        <img width="314" alt="Logo Image" src="${pageContext.request.contextPath}/img/logo.png" />
    </div>
</div>
</body>
</html>

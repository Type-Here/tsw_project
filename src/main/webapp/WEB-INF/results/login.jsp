<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js" defer></script>
    <title>Retrogamer: Login</title>
</head>

<c:if test="${not empty userlogged}">
    <script>
        window.location.href = "${pageContext.request.contextPath}/user-profile";
    </script>
</c:if>

<body class="body_def">
<div class="central-logo-name">
    <a href="${pageContext.request.contextPath}/"><img class="logo-big" src="${pageContext.request.contextPath}/img/logo.png" alt="logo"/></a>
    <a class="upper-name" href="${pageContext.request.contextPath}/">RETROGAMER</a>
    <h5>EVERYTHING BACK TO LIFE</h5>
</div>
<div class="main centerized-flex-container">
    <div class="log_form">
        <!--TODO password dimenticata -->
        <form id="login" action="${pageContext.request.contextPath}/user-login" method="post">
            <div class="form-row" aria-labelledby="user">
                <label for="user">Username</label>
                <input id="user" type="email" name="email" placeholder="Username" required autofocus size="30" pattern="^[\w]+[\w\.\-]+@[\w\.\-]+\.[\w]+$" /> <!-- Email -->
                <!-- General username pattern="^(?=.*[a-z])(?=.*[A-Z])(?!.*[\s]).{3,}$" -->
            </div>
            <div class="form-row" aria-labelledby="pass">
                <label for="pass">Password</label>
                <input id="pass" type="password" name="password" placeholder="Password" required pattern="^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[\\!£$%&\/\(\)=?'^])(?=.*[0-9]).{8,}$" maxlength="30"/>
            </div>
            <c:if test="${not empty invalidUser}">
                <div class="invalid-credentials">
                    <span>Username o Password errati. Riprovare</span>
                </div>
            </c:if>
            <div class="form-row centerized-row" aria-label="Password dimenticata"><a href="#">Password Dimenticata?</a></div>
            <div class="form-row" aria-label="Accedi o Registrati">
                <input class="default" type="submit" value="Login">
                <a href="${pageContext.request.contextPath}/user-register"><button type="button" class="default alternative" value="register" name="method" formnovalidate>Registrati</button></a>
            </div>
        </form>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>

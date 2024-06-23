<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Login Admin - RetroGamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
</head>
<c:if test="${admin == true and valid == true} ">
    <c:redirect url="/console">User already Logged: Redirect</c:redirect>
</c:if>

<body class="body_def">

<%@include file="../include/header-alternative.jsp"%>

<div class="main centerized-flex-container">
    <div><h2 class="admin-login"><b>ADMIN CONSOLE</b></h2></div>
    <div class="log_form">
        <form action="${pageContext.request.contextPath}/admin-login" method="post">
            <div class="form-row">
                <label for="user">Username</label>
                <input id="user" type="text" name="user" placeholder="Username" required autofocus size="30" pattern="^[a-zA-Z0-9_\-]+$" /> <!-- Email -->
                <!-- General username pattern="^(?=.*[a-z])(?=.*[A-Z])(?!.*[\s]).{3,}$" -->
            </div>
            <div class="form-row">
                <label for="pass">Password</label>
                <input id="pass" type="password" name="pass" placeholder="Password" required pattern="^.{5,}$" maxlength="30"/>
            </div>
            <div class="form-row centerized-row"><a href="#">Password Dimenticata?</a></div>
            <div class="form-row centerized-row">
                <input class="default" type="submit" value="Login">
            </div>
        </form>
    </div>
    <c:if test="${not empty invalidUser}">
        <div class="invalid-credentials">
            <span>${errorMessage}</span>
        </div>
    </c:if>
</div>

<%@ include file="../include/footer.jsp"%>

</body>
</html>

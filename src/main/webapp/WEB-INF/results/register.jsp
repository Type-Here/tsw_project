<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Register - RetroGamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/register.js" defer></script>
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
    <div class="log_form reg_form">
        <!--TODO-->
        <form id="register" action="${pageContext.request.contextPath}/user-register" method="post">
            <fieldset class="access-data">
                <label class="form-row" for="email"><span>Email</span>
                    <input id="email" type="email" name="email" placeholder="Email" required autofocus size="30" pattern="^[\w]+[\w\.\-]+@[\w\.\-]+\.[\w]+$" />
                </label>
                <label class="form-row" for="pass-2"><span>Password</span>
                    <input id="pass-2" type="password" name="password" placeholder="Password" required pattern="^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[!£$%&\/\(\)=?'^])(?=.*[0-9]).{8,}$" maxlength="30"/>
                </label>
            </fieldset>
            <fieldset class="registry-data">
                <label class="form-row" for="name"><span>Nome</span>
                    <input id="name" type="text" name="name" placeholder="Nome" required size="30" pattern="^[a-zA-ZÀ-ÿ' ]{3,}$" />
                </label>
                <label class="form-row" for="surname"><span>Cognome</span>
                    <input id="surname" type="text" name="surname" placeholder="Cognome" required pattern="^[a-zA-ZÀ-ÿ' ]{3,}$" />
                </label>
                <label class="form-row" for="phone"><span>Numero di Telefono</span>
                    <input id="phone" type="tel" name="phone" title="Tel: +3912345678" placeholder="Numero di Telefono: +3901234567" required pattern="\+39[0-9]{8,10}" value="+39"/>
                </label>
                <label class="form-row" for="birth"><span>Data di Nascita</span>
                    <input id="birth" type="date" name="birthdate" placeholder="Data di Nascita" required pattern="(\d{1,2}|\d{4})(-|\/)\d{1,2}(-|\/)(\d{4}|\d{1,2})"/>
                </label>
            </fieldset>
            <fieldset class="address-data">
                <label class="form-row alternative-row" for="road-type"><span>Indirizzo</span>
                    <input class="free-size" id="road-type" type="text" name="road-type" size="5" placeholder="Via" required pattern="^[A-Za-zÀ-ÿ'\- ]{3,15}$" maxlength="15" />
                    <input id="road-name" type="text" name="road-name" placeholder="Giovanni Paolo" required pattern="^[A-Za-zÀ-ÿ'\- ]{3,60}$" maxlength="60" />
                    <input class="free-size" id="road-number" name="road-number" size="3" type="text" maxlength="6" placeholder="1" step="1" required pattern="^[0-9]{1,6}$" />
                </label>
                <label class="form-row" for="city"><span>Citt&agrave;</span>
                    <input id="city" type="text" name="city" placeholder="Città" required pattern="^[a-zA-ZÀ-ÿ'\- ]{3,50}$" maxlength="50" />
                </label>
                <div class="form-row centerized-row">
                    <label class="" for="cap"><span>CAP</span>
                        <input class="free-size" name="cap" size="5" id="cap" type="text" maxlength="5" placeholder="CAP" required pattern="^[0-9]{5}$" />
                    </label>
                    <label class="" for="prov"><span>Prov</span>
                        <input class="free-size" name="province" size="2" id="prov" type="text" maxlength="2" placeholder="RM" required pattern="^[a-zA-Z]{2}$" />
                    </label>
                </div>
            </fieldset>
            <fieldset class="form-row">
                <input class="default" type="submit" value="Register">
                <a href="${pageContext.request.contextPath}/user-login"><button type="button" class="default alternative" value="Login" name="method">Login</button></a>
            </fieldset>
            <div id="tooltip" class="general-display-none"></div>
            <fieldset>
                <c:if test="${not empty invalidUser}">
                    <div class="invalid-credentials">
                        <span>Dati inseriti non corretti</span>
                    </div>
                </c:if>
            </fieldset>
        </form>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>
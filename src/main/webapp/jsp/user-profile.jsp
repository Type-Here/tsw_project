<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/user-profile.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/user-profile.js" defer></script>
    <title>Retrogamer: Profilo Utente</title>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<div class="main_home">

    <div class="utente-container">
        <div class="utente-header"><hr><h1 id="username">Bentornato ${userlogged.firstname}</h1><hr></div>
        <div class="utente-content-wrapper">
            <div class="utente-sidebar">
                <ul class="utente-nav">
                    <li onclick="showContent('section1')">Dati Personali</li>
                    <li onclick="showContent('section2')">I Miei Ordini</li>
                    <li onclick="showContent('section3')">Sicurezza</li>
                    <li onclick="showContent('section4')">Indirizzi Di Spedizione</li>
                    <li onclick="document.getElementById('logout').submit();">Log Out</li>
                </ul>
            </div>

            <form id="logout" action="${pageContext.request.contextPath}/exit-user" method="post" style="display: none;"></form>

            <div class="utente-main-content">
                <div id="section1" class="utente-content-section">
                    <div class="log_form reg_form">
                        <form action="${pageContext.request.contextPath}/modify-user" method="post">
                            <input type="hidden" name="section" value="one">
                            <fieldset class="registry-data">
                                <label class="form-row" for="name"><span>Nome</span>
                                    <input id="name" name="name" type="text" value="${userlogged.firstname}" required size="30" pattern="^[a-zA-Z']{3,}$" />
                                </label>
                                <label class="form-row" for="surname"><span>Cognome</span>
                                    <input id="surname" name="surname" type="text" value="${userlogged.lastname}"  required pattern="^[a-zA-Z']{3,}$" />
                                </label>
                                <label class="form-row" for="phone"><span>Numero di Telefono</span>
                                    <input id="phone"  type="tel" name="phone" value="${userlogged.telephone}" required pattern="+39[0-9]{8,10}"/>
                                </label>
                                <label class="form-row" for="birth"><span>Data di Nascita</span>
                                    <input id="birth" name="birthdate" type="date" value="${userlogged.birth}" required />
                                </label>
                                <label class="form-row" for="email"><span>Email</span>
                                    <input id="email" name="email" type="email" value="${userlogged.email}" required autofocus size="30" pattern="^[\w]+[\w\.\-]+@[\w\.\-]+\.[\w]+$" />
                                </label>
                            </fieldset>
                            <fieldset class="address-data">
                                <label class="" for="road-type"><span>Indirizzo</span>
                                    <input id="address" type="hidden" value="${userlogged.address}">
                                    <input class="free-size" id="road-type" name="road-type" type="text" value="" size="5" required pattern="^{3,}$" />
                                    <input id="road-name" name="road-name" type="text" value="" required pattern="^[a-zA-Z'\s\-]{3,}$" />
                                    <input class="free-size" id="road-number" name="road-number" size="3" value="" type="text" maxlength="6" step="1" required pattern="^[\w]+$" />
                                </label>
                                <label class="form-row" for="city"><span>Citt&agrave;</span>
                                    <input id="city" name="city" type="text" value="${userlogged.city}" required pattern="^[a-zA-Z'\s\-]{3,}$" />
                                </label>
                                <div class="form-row centerized-row">
                                    <label class="" for="cap"><span>CAP</span>
                                        <input class="free-size" size="5" id="cap" name="cap" type="text" maxlength="5" value="${userlogged.CAP}" required pattern="^[0-9]{5}$" />
                                    </label>
                                    <label class="" for="prov"><span>Prov</span>
                                        <input class="free-size" size="2" id="prov" name="province" type="text" maxlength="2" value="${userlogged.prov}" required pattern="^[a-zA-Z]{2}$" />
                                    </label>
                                </div>
                            </fieldset>
                            <fieldset class="form-row">
                                <input class="default" type="submit" value="Applica Modifica">
                            </fieldset>
                        </form>
                    </div>
                </div>
                <div id="section2" class="utente-content-section" style="display: none;">
                    <h2>I Miei Ordini</h2>
                    <div class="log_form reg_form">
                        <form action="${pageContext.request.contextPath}/modify-user">
                            <input type="hidden" name="section" value="two">
                            <fieldset class="access-data">
                                <label class="form-row" for="order-id"><span>Ordine</span>
                                    <input id="order-id" type="text" placeholder="ID Ordine" required pattern="^[\w]+$" />
                                </label>
                            </fieldset>
                            <fieldset class="form-row">
                                <input class="default" type="submit" value="Cerca">
                            </fieldset>
                        </form>
                    </div>
                </div>
                <div id="section3" class="utente-content-section" style="display: none;">
                    <h2>Sicurezza</h2>
                    <div class="log_form reg_form">
                        <form action="${pageContext.request.contextPath}/modify-user" method="post">
                            <input type="hidden" name="section" value="three">
                            <fieldset class="access-data">
                                <label class="form-row" for="pass-old"><span>Password</span>
                                    <input id="pass-old" name="oldpass" type="password" placeholder="Password Vecchia" required pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[&%$£\/\\\(\)=€?\^!]).{8,}$" maxlength="30"/>
                                </label>
                                <label class="form-row" for="pass-new"><span>Password</span>
                                    <input id="pass-new" name="newpass" type="password" placeholder="Password Nuova" required pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[&%$£\/\\\(\)=€?\^!]).{8,}$" maxlength="30"/>
                                </label>
                            </fieldset>
                            <fieldset class="form-row">
                                <input class="default" type="submit" value="Applica">
                            </fieldset>
                        </form>
                    </div>
                </div>
                <div id="section4" class="utente-content-section" style="display: none;">
                    <h2>Indirizzi Di Spedizione</h2>
                    <div class="log_form reg_form">
                        <form action="${pageContext.request.contextPath}/modify-user">
                            <input type="hidden" name="section" value="four">
                            <fieldset class="address-data">
                                <label class="" for="road-type"><span>Indirizzo</span>
                                    <input class="free-size" id="road-type2" type="text" size="5" placeholder="Via" required pattern="^{3,}$" />
                                    <input id="road-name2" type="text" placeholder="Giovanni Paolo" required pattern="^[a-zA-Z'\s\-]{3,}$" />
                                    <input class="free-size" id="road-number2" size="3" type="text" maxlength="6" placeholder="1" step="1" required pattern="^[\w]+$" />
                                </label>
                                <label class="form-row" for="city"><span>Citt&agrave;</span>
                                    <input id="city2" type="text" placeholder="Città" required pattern="^[a-zA-Z'\s\-]{3,}$" />
                                </label>
                                <div class="form-row centerized-row">
                                    <label class="" for="cap"><span>CAP</span>
                                        <input class="free-size" size="5" id="cap2" type="text" maxlength="5" placeholder="CAP" required pattern="^[0-9]{5}$" />
                                    </label>
                                    <label class="" for="prov"><span>Prov</span>
                                        <input class="free-size" size="2" id="prov2" type="text" maxlength="2" placeholder="RM" required pattern="^[a-zA-Z]{2}$" />
                                    </label>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>
<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>
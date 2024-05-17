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
    <script src="${pageContext.request.contextPath}/js/user-profile.js"></script>
    <title>Retrogamer: Profilo Utente</title>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<div class="main_home">

    <div class="utente-container">
        <div class="utente-header"><hr><h1 id="username">Nome Utente</h1><hr></div>
        <div class="utente-content-wrapper">
            <div class="utente-sidebar">
                <ul>
                    <li><a href="#" onclick="showContent('section1')">Profilo</a></li>
                    <li><a href="#" onclick="showContent('section2')">I Miei Ordini</a></li>
                    <li><a href="#" onclick="showContent('section3')">Sicurezza</a></li>
                    <li><a href="#" onclick="showContent('section4')">Indirizzi Di Spedizione</a></li>
                    <li><a href="#" onclick="">Log Out</a></li> //TODO: Aggiungere funzione di logout
                </ul>
            </div>
            <div class="utente-main-content">
                <div id="section1" class="utente-content-section">
                    <h2>Titolo Sezione 1</h2>
                    <p>Contenuto Sezione 1</p>
                </div>
                <div id="section2" class="utente-content-section" style="display: none;">
                    <h2>Titolo Sezione 2</h2>
                    <p>Contenuto Sezione 2</p>
                </div>
                <div id="section3" class="utente-content-section" style="display: none;">
                    <h2>Titolo Sezione 2</h2>
                    <p>Contenuto Sezione 2</p>
                </div>
                <div id="section4" class="utente-content-section" style="display: none;">
                    <h2>Titolo Sezione 2</h2>
                    <p>Contenuto Sezione 2</p>
                </div>
            </div>
        </div>

    </div>

</div>
<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>

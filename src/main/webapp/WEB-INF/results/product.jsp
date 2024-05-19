<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Catalogo - Retrogamer</title>
    <link type="text/css" rel="stylesheet" href="css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="css/small.css" />
    <link rel="icon" type="image/x-icon" href="img/favicon.ico">
    <script src="js/carousel.js"></script>
    <script src="js/overlay.js"></script>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-search.jsp"%>

<div class="upper_body">

    <div class="game-name">
        <h1>${product.name}</h1>
    </div>

    <div class="front-view">
        <div class="img-container-front">
            <img class="prod-front-image" src="${product.metaData.path}${product.metaData.front}" alt="front image" />
        </div>
        <div class="prod-overview">
            <div class="prod-price">
                <div class="tile-text-bottom-price">
                    <c:choose>
                        <c:when test="${empty product.discount or product.discount == 0}">
                            <span class="actual-price">
                                    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.price}"/>&euro;
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="discount">${product.discount}</span>
                            <span class="original-rem-price">${product.price}</span>
                            <span class="actual-price">
                                <c:out value="${product.price - product.price*product.discount/100}"/>
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="prod-add-to-cart">
                <button class="default">Aggiungi al Carrello</button>
                <button class="default yellow">1-Click</button>
            </div>
            <div class="prod-meta-table">
                <table>
                    <tr><th>Piattaforma</th><td>${product.platform}</td></tr>
                    <tr><th>Sviluppatore</th><td>${product.developer}</td></tr>
                </table>
            </div>
            <div class="social-overview">
                <img src="img/icons/facebook.svg" alt="Facebook Share" class="general_icon" />
                <img src="img/icons/share.svg" alt="Share Button" class="general_icon" />
            </div>
        </div>
    </div>

</div>

<div class="main_home">
    <section class="prod-description">
        <div class="prod-desc-internal">
            <h2>${product.name}</h2>
            <h3>Categorie</h3>
            <ul>
                <c:forEach var="cat" items="${product.categoryBeanList}">
                <li>${cat.typename}</li>
                </c:forEach>
            </ul>
            <hr />
            <h3>Descrizione</h3>
            <p>
                ${product.description}
            </p>
            <hr />
            <h3>Requirements</h3>
            <table class="req-table">
                <tr><th>Piattaforma</th><td>${product.platform}</td></tr>
            </table>
        </div>
        <div>

        </div>
    </section>

    <section class="prod-reviews">
        <div class="prod-reviews-internal">
            <div class="prod-review-first-row">
                <h3>Recensioni</h3>
                <span class="review-average">Media Voti: 3/5</span>
            </div>
            <table class="prod-reviews-table">
                <tr>
                    <th>Utente</th>
                    <th>Voto</th>
                    <th>Commento</th>
                    <th>Data</th>
                </tr>
                <tr>
                    <td>Domenico</td>
                    <td><span class="star-on"></span><span class="star-on"></span><span class="star-on"></span><span class="star-on"></span><span class="star-off"></span></td>
                    <td>Meraviglioso.</td>
                    <td>2024-03-12</td>
                </tr>
                <tr>
                    <td>Amorello</td>
                    <td><span class="star-on"></span><span class="star-on"></span><span class="star-off"></span><span class="star-off"></span><span class="star-off"></span></td>
                    <td>C'è poco da dire.</td>
                    <td>2024-03-11</td>
                </tr>
            </table>
        </div>
    </section>

</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>
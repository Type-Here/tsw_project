<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${product.name} - Retrogamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/prod-page.js" defer></script>
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
            <c:if test="${product.type == false}"> <!--Digital-->
                <div class="centerized-flex-container condition-select">
                    <h2 class="text-center">Condizione:</h2>
                    <div class="prod-condition-div">
                        <c:forEach items="${product.conditions}" var="cond">
                            <c:choose>
                                <c:when test="${cond.condition eq 'A'}"> <!--<img src="img/top.png" alt="top quality"/>-->
                                    <button class="prod-condition-button <c:if test="${cond == product.conditions[0]}">active-button</c:if> top-quality" value="${cond.id_cond}">${cond.condition}</button>
                                </c:when>
                                <c:otherwise>
                                    <button class="prod-condition-button <c:if test="${cond == product.conditions[0]}">active-button</c:if>" value="${cond.id_cond}">${cond.condition}</button>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            <div class="prod-price">
                <div class="tile-text-bottom-price">
                    <c:choose>
                        <c:when test="${empty product.discount or product.discount == 0}">
                            <span id="prod-price" class="actual-price">
                                    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.price}"/>&euro;
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="discount">${product.discount}</span>
                            <span class="original-rem-price">${product.price}</span>
                            <span id="prod-price" class="actual-price">
                                <c:set var="price" scope="page" value="${product.price - product.price*product.discount/100}"/>
                                 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${price}"/>&euro;
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="prod-add-to-cart">
                <button id="add-to-cart" class="default">Aggiungi al Carrello</button>
                <button id="one-click" class="default yellow">1-Click</button>
            </div>
            <div class="prod-meta-table">
                <table>
                    <tr><th>Piattaforma</th><td>${product.platform}</td></tr>
                    <tr><th>Sviluppatore</th><td>${product.developer}</td></tr>
                </table>
            </div>
            <div class="social-overview">
                <img src="${pageContext.request.contextPath}/img/icons/facebook.svg" alt="Facebook Share" class="general_icon" />
                <img src="${pageContext.request.contextPath}/img/icons/share.svg" alt="Share Button" class="general_icon" />
            </div>
        </div>
    </div>

</div>

<div class="main_home">
    <section class="prod-description">
        <div class="prod-desc-internal">
            <h1>${product.name}</h1>
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
                <span class="review-average">Media Voti: <fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="1" value="${averageVote}"/>/5</span>
            </div>
            <table class="prod-reviews-table">
                <tr>
                    <th>Utente</th>
                    <th>Voto</th>
                    <th>Commento</th>
                    <th>Data</th>
                </tr>
                <c:choose>
                    <c:when test="${empty reviews}">
                        <tr><td></td><td></td><td><b>Non ci sono ancora recensioni. Sii il primo! </b></td><td></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${reviews}" var="review">
                            <tr>
                                <td>${review.firstname}</td>
                                <td>
                                    <c:forEach begin="1" end="${review.vote}"><span class="star-on"></span></c:forEach><c:forEach begin="${review.vote + 1}" end="5"><span class="star-off"></span></c:forEach>
                                </td>
                                <td>${review.comment}</td>
                                <td>${review.review_date}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
    </section>

</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>
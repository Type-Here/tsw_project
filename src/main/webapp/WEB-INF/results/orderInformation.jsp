<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/orderInformation.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/orderInformation.js" defer></script>
    <title>Retrogamer: Ordine</title>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<div class="main_home">

    <div class="orderContainer">

        <div class="orderTable-container">
            <table class="orderTable">
                <caption>
                    <hr>
                    Ordine n° ${cart.id_cart} <br> <span style="font-size: 1rem; font-weight: normal">Totale: ${cart.total}€</span>
                    <input id="cartID" type="hidden" value="${cart.id_cart}">
                    <hr>
                </caption>
                <tr>
                    <td>Foto</td>
                    <td>Nome Prodotto</td>
                    <td>Prezzo</td>
                    <td>Quantità</td>
                    <td>Recensione</td>
                    <td>Rimborso</td>
                </tr>
                <c:forEach var="item" items="${cartItems}">
                    <c:forEach var="product" items="${products}">
                        <c:set var="found" value="false"/>
                        <c:if test="${product.id_prod == item.id_prod}">
                            <c:forEach var="review" items="${reviews}">
                                <c:if test="${review.id_prod == product.id_prod}">
                                    <c:set var="found" value="true"/>
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td><img class="orderImage" id="prodIMG" src="${product.metaData.path}${product.metaData.front}" alt="${product.name}"></td>
                                <td>${product.name}</td>
                                <td>${item.real_price}</td>
                                <td>${item.quantity}</td>
                                <td>
                                    <button class="openModal" value="${item.id_prod}" <c:if test="${found}"> disabled </c:if>>Recensici</button>
                                </td>
                                <c:choose>
                                    <c:when test="${item.refund == 1}">
                                        <td>
                                            <button onclick="refundCartItem(${item.id_prod}, this)" class="refundButton" value="${item.refund}">Rimborso</button>
                                        </td>
                                    </c:when>
                                    <c:when test="${item.refund == 2}">
                                        <td>In attesa di conferma</td>
                                    </c:when>
                                    <c:when test="${item.refund == 3}">
                                        <td>Rifiutato</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Reimborsato</td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </table>
        </div>
        <dialog class="prodModal">
            <div style="display: flex; justify-content: flex-end"><button class="closeModal">X</button></div>
            <h1>Recensione</h1>
            <form class="modalForm">
                <input type="hidden" name="id_prod" value="">
                <label for="evaluation">Voto</label>
                <div class="recensione-stelle" id="recensione-stelle">
                    <span class="star star-off" data-value="1"></span>
                    <span class="star star-off" data-value="2"></span>
                    <span class="star star-off" data-value="3"></span>
                    <span class="star star-off" data-value="4"></span>
                    <span class="star star-off" data-value="5"></span>
                </div>
                <input type=hidden id="evaluation" name="evaluation" value="0">
                <br>
                <label for="comment">Commento</label>
                <br>
                <textarea class="comment" id="comment" name="comment"></textarea>
                <br>
                <input id="sendReview" type="submit" value="Invia Recensione" formmethod="dialog">
            </form>
        </dialog>
    </div>

</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>
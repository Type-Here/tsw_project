<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Il tuo Carrello - Retrogamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-search.jsp"%>

<div class="upper_body">
    <div class="game-name">
        <h1>Il tuo Carrello</h1>
    </div>
</div>

<div class="main_home">
    <div class="cart_container">

        <section class="cart-prod-list">
            <h2>Ecco la tua lista:</h2>

            <% //NB Remember: cartItems is an HashMap, use item.value to get the bean! %>
            <c:choose>
                <c:when test="${empty cart or cart.cartItems.size == 0}">
                 <div class="cart-item">
                    <h2>Non hai alcun prodotto in Carrello!</h2>
                    <span>Continua gli acquisti e rivisita la pagina per continuare l'ordine</span>
                 </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${cart.cartItems}">
                        <div class="cart-item">
                            <div class="cart-item-upper">
                                <h3>${item.value.id_prod}</h3>
                                <span><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${item.value.real_price}"/>&euro;</span>
                            </div>
                            <div class="cart-item-sumup">
                                <div class="item-img">
                                    <img src="${pageContext.request.contextPath}/img/placeholder2.svg" alt="prod-mini image"/>
                                </div>
                                <ul class="item-data">
                                    <li>Piattaforma: X</li>
                                    <li>Condizione: ${item.value.condition}</li>
                                </ul>
                            </div>
                            <div class="cart-item-operations">
                                <label>Quantità:
                                    <select>
                                        <c:forEach var="i" begin="1" end="${item.value.quantity}">
                                            <option <c:if test="${i == item.value.quantity}">selected</c:if> >${i}</option>
                                        </c:forEach>
                                    </select>
                                </label>
                                <button class="remove-item">
                                    <img src="${pageContext.request.contextPath}/img/icons/trash-can-solid.svg" alt="trash-can">
                                    <span>Rimuovi</span>
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </section>

        <aside class="side-cart-view">
            <div class="cart-overview">
                <div class="cart-overview-title">
                    <c:choose>
                        <c:when test="${empty cart}">
                            <span>Nessun Prodotto in Carrello</span>
                        </c:when>
                        <c:otherwise>
                            <span>Prodotti in Carrello: ${cart.cartItems.size()}</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="discount-code">
                    <h3>Hai un codice sconto?</h3>
                    <label for="input-discount-code">Inseriscilo qui! <br />
                        <input id="input-discount-code" type="text" name="code" />
                        <button id="button-discount-code" class="default alternative">Invia</button>
                    </label>
                </div>
                <div class="cart-overview-price">
                    <div class="subtotal">
                        <h3>Subtotale</h3>
                        <c:set var="subtotal" value="0"/>
                        <c:forEach var="item" items="${cart.cartItems}">
                            <c:set var="subtotal" value="${subtotal + item.value.real_price * item.value.quantity}"/>
                        </c:forEach>
                        <span><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${subtotal}"/>&euro;</span>
                    </div>
                    <div class="total">
                        <h2><b>Totale</b></h2>
                        <span><b>
                            <c:choose>
                                <c:when test="${subtotal == 0 or subtotal >= 100}">
                                    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${subtotal}"/>&euro;
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${subtotal + 15.00}"/>&euro;
                                </c:otherwise>
                            </c:choose>
                            </b></span>
                    </div>
                </div>

                <div class="prod-add-to-cart">
                    <button id="order-button" class="default yellow">Prosegui l'acquisto</button>
                </div>

            </div>
        </aside>
    </div>
    <div class="continue-shopping">
        <button class="default alternative"><a href="${pageContext.request.contextPath}/store">Continua con lo shopping</a></button>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>

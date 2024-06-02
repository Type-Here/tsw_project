<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Ordina - Retrogamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/cart-item.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/order.js" defer></script>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<div class="upper_body">
    <div class="game-name">
        <h1>Effettua l'ordine</h1>
    </div>
</div>

<div class="main_home">
    <div class="cart_container">
        <section class="section-container">

            <section id="address" class="cart-prod-list">
                <div class="order-section-top">
                    <h2>Indirizzo di Spedizione</h2>
                </div>
                <c:forEach var="i" begin="1" end="${addresses.size()}">
                    <div class="cart-item <c:if test="${i-1 == 0}">address-button-selected</c:if> address-button">
                        <input type="hidden" name="add" value="${addresses[i-1].id_add}">
                        <div class="cart-item-upper">
                            <h3>${addresses[i-1].firstname} ${addresses[i-1].lastname}</h3>
                        </div>
                        <div class="cart-item-sumup">
                            <ul class="item-data hide-bullets">
                                <li>${addresses[i-1].address}</li>
                                <li>${addresses[i-1].city} ${addresses[i-1].prov}</li>
                                <li>${addresses[i-1].CAP}</li>
                            </ul>
                        </div>
                    </div>
                </c:forEach>
                    <div id="add-address-btn" class="cart-item address-button">
                        <div class="cart-item-upper">
                            <h3>Aggiungi Indirizzo</h3>
                        </div>
                        <div class="add-address-button cart-item-sumup centerized-flex-container">
                            <span class=>+</span>
                        </div>
                    </div>
            </section>
            
            <section id="payment" class="cart-prod-list">
                <div class="order-section-top">
                    <h2>Imposta il Pagamento</h2>
                </div>
                <div class="log_form gen-form payment-form">
                    <form id="pay">
                        <label class="form-row"><span>Nome e Cognome</span><input type="text" placeholder="Mario Rossi" name="name" title="Nome" pattern="[a-zA-Z&àòèùì' ]{3,50}" required/></label>
                        <label class="form-row"><span>PAN </span><input type="text" name="pan" title="PAN" placeholder="0000 0000 0000 0000" pattern="[0-9]{16}" required/></label>
                        <label class="form-row"><span>CVV/CVV2 </span><input class="free-size" type="text" placeholder="123" name="cvv" title="CVV" pattern="[0-9]{3}" size="3" required/></label>
                        <label class="form-row"><span>Data Scadenza </span><input type="date" name="expire" required></label>
                    </form>
                </div>
            </section>
    
            <section id="cart" class="cart-prod-list">
                <div class="order-section-top">
                    <h2>Il tuo Carrello</h2>
                </div>
                <c:forEach var="item" items="${cart.cartItems}">
                    <div class="cart-item">
                        <div class="cart-item-upper">
                            <h3>${item.value.id_prod}</h3>
                            <span><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${item.value.real_price}"/>&euro;
                            </span>
                        </div>
                        <div class="cart-item-sumup">
                            <div class="item-img">
                                <img src="${pageContext.request.contextPath}/img/placeholder2.svg" alt="prod-mini image"/>
                            </div>
                            <ul class="item-data">
                                <li>Piattaforma: X</li>
                                <li>Condizione: ${item.value.condition}</li>
                                <li>Quantità: ${item.value.quantity}</li>
                            </ul>
                        </div>
                    </div>
                </c:forEach>
            </section>

        </section>

        <aside class="side-cart-view">
            <div class="cart-overview">
                <div class="cart-overview-title">
                    <span>Prodotti in Carrello: ${cart.cartItems.size()}</span>
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
                    <button id="order-button" name="order" value="true" class="default yellow">Ordina</button>
                </div>

            </div>
        </aside>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>
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

            <div class="cart-item">
                <div class="cart-item-upper">
                    <h3>Crash Bandicoot</h3>
                    <span>1.00&euro;</span>
                </div>
                <div class="cart-item-sumup">
                    <div class="item-img">
                        <img src="${pageContext.request.contextPath}/img/placeholder2.svg" alt="prod-mini image"/>
                    </div>
                    <ul class="item-data">
                        <li>Piattaforma: ps1</li>
                        <li>Condizione: A</li>
                    </ul>
                </div>
                <div class="cart-item-operations">
                    <label>Quantità:
                        <select>
                            <option>1</option>
                            <option>2</option>
                        </select>
                    </label>
                    <button class="remove-item">
                        <img src="${pageContext.request.contextPath}/img/icons/trash-can-solid.svg" alt="trash-can">
                        <span>Rimuovi</span>
                    </button>

                </div>

            </div>
            <div class="cart-item">
                <div class="cart-item-upper">
                    <h3>Crash Bandicoot</h3>
                    <span>1.00&euro;</span>
                </div>
                <div class="cart-item-sumup">
                    <div class="item-img">
                        <img src="${pageContext.request.contextPath}/img/placeholder2.svg" alt="prod-mini image"/>
                    </div>
                    <ul class="item-data">
                        <li>Piattaforma: ps1</li>
                        <li>Condizione: A</li>
                    </ul>
                </div>
                <div class="cart-item-operations">
                    <label>Quantità:
                        <select>
                            <option>1</option>
                            <option>2</option>
                        </select>
                    </label>
                    <button class="remove-item">
                        <img src="${pageContext.request.contextPath}/img/icons/trash-can-solid.svg" alt="trash-can">
                        <span>Rimuovi</span>
                    </button>

                </div>

            </div>
        </section>

        <aside class="side-cart-view">
            <div class="cart-overview">
                <div class="cart-overview-title">
                    <span>100 Prodotti in Carrello</span>
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
                        <span>99,99&euro;</span>
                    </div>
                    <div class="total">
                        <h2><b>Totale</b></h2>
                        <span><b>99.99&euro;</b></span>
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

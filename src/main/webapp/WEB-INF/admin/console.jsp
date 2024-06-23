<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Admin Console - Retrogamer</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/js/admin/console-module.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/js/admin/admin-functions.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/js/admin/add-product.js" defer></script>
    <script type="module" src="${pageContext.request.contextPath}/js/admin/prod-catalog.js" defer></script>
</head>
<body class="body_def">

<c:if test="${not empty upload}">
    <div class="info-popup">
        <h3 class="text-center">Info</h3>
        <div class="info-text">
            <c:choose>
                <c:when test="${upload == true}">
                    <span>Inserimento è andato a buon fine</span>
                </c:when>
                <c:otherwise>
                    <span class="error-popup">Inserimento ha provocato un errore, impossibile inserire elemento</span><br />
                    <span>Errore: <c:if test="${not empty error}">${error}</c:if></span>
                </c:otherwise>
            </c:choose>
        </div>
        <button class="default free-size" onclick="hidePopup()">OK</button>
    </div>
    <div class="overlay-popup" onclick="hidePopup()"></div>
</c:if>


<%@include file="/WEB-INF/include/header-admin.jsp"%>

<div class="upper_body">
    <div class="centerized-flex-container margin-v-10">
        <h1 class="text-center">Admin Console</h1>
    </div>
</div>

<div class="main_home">

    <section id="add_prod">
        <div class="section-header">
            <h2 class="text-center">Aggiungi Prodotto</h2>
        </div>
        <div class="log_form add-prod-info">
            <form id="add-form" action="admin/add-prod" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="addProd" />
                <label class="form-row"><span>Nome: </span><input type="text" name="name" required/></label>
                <label class="form-row"><span>Prezzo:</span><input class="price-prod" type="number" min="1" max="100000" maxlength="8" step="0.01" name="price" required pattern="[0-9.,]{1,8}"/></label>
                <div class="form-row">
                    <span>
                        <span>Tipo:</span>
                        <label class="required"><input class="type-prod" type="radio" name="type" value="physic" required/>Fisico</label>
                        <label class="required"><input class="type-prod" type="radio" name="type" value="digital" required/>Digitale</label>
                    </span>
                    <label class="margin-h-10"><span>Piattaforma: </span>
                        <select name="platform" required>
                            <option value="ps1">PS1</option>
                            <option value="N64">N64</option>
                            <option value="ps2">PS2</option>
                            <option value="PC">PC</option>
                            <option value="Atari2600">Atari 2600</option>
                            <option value="C64">C64</option>
                            <option value="GameBoy">GameBoy</option>
                            <option value="GameCube">GameCube</option>
                            <option value="SegaMegaDrive">Sega MD</option>
                        </select>
                    </label>
                </div>
                <label class="form-row"><span>Descrizione:</span><textarea rows="3" cols="40" name="description" placeholder="Aggiungi qui la descrizione..." required></textarea></label>
                <label class="form-row"><span>Immagine Copertina:</span><input type="file" name="front-image" accept="image/*" required /></label>
                <label class="form-row"><span>Immagini Galleria:</span><input type="file" multiple name="gallery-images" accept="image/*" required /></label>
                <label class="form-row"><span>Developer:</span><input type="text" name="developer" pattern="[a-zA-Z0-9\&$'_\(\)\- ]{2,50}" required /> </label>
                <label class="form-row"><span>Key:</span><input class="key-prod" type="text" name="key" pattern="[a-zA-Z0-9]{5,15}" /></label>
                <div class="form-row category-form"><span class="title-row">Categorie:*</span>
                    <c:forEach var="cat" items="${category}">
                        <label><input type="checkbox" class="category-input free-size" name="category" value="${cat.id_cat}">${cat.typename}</label>
                    </c:forEach>
                </div>
                <div class="form-row centerized-row">
                    <label><span>&percnt; Sconto:</span><input class="discount-prod free-size" size="5" maxlength="5" type="number" name="discount" min="0" max="100" pattern="[0-9.,]{1,5}"/></label>
                </div>
                <div class="form-row category-form">
                    <span class="title-row">Condizione:* </span>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="X">Digitale</label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="A"/>A</label>
                        <label class="condition-row">Quantità<input class="quantity-prod free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="B"/>B</label>
                        <label class="condition-row">Quantità<input class="quantity-prod free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="C"/>C</label>
                        <label class="condition-row">Quantità<input class="quantity-prod free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="D"/>D</label>
                        <label class="condition-row">Quantità<input class="quantity-prod free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="E"/>E</label>
                        <label class="condition-row">Quantità<input class="quantity-prod free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                </div>

                <div class="form-row centerized-row">
                    <input id="add-submit" class="default" type="submit" value="Invia" />
                    <input class="default alternative" type="reset" value="Reimposta"/>
                </div>
            </form>
        </div>
    </section>

    <section id="catalog_management" class="prod-reviews">
        <div class="section-header">
            <h2 class="text-center">Gestisci Catalogo</h2>
        </div>
        <div class="catalog-buttons">
            <button id="load-cat-button" class="default alternative">Carica Dati</button>
            <button id="prev-cat-button" disabled class="default alternative general-display-none margin-h-10">Prev</button>
            <button id="next-cat-button" class="default general-display-none margin-h-10">Next</button>
            <div class="search_container">
                <form id="prod-search" class="search-bar">
                    <span id="label-input-product"><img src="${pageContext.request.contextPath}/img/icons/search.svg" alt="Search"/></span>
                    <input aria-labelledby="label-input-product" id="search-input" title="Search By Product Name" type="text" name="search" placeholder="Cerca un prodotto" alt="Cerca" />
                </form>
            </div>
        </div>
        <div class="prod-table prod-reviews-internal">
            <div class="margin-v-10 text-center"><span><b>Nota:</b> Possono essere eliminati solo prodotti non ancora venduti</span></div>
            <table id="admin-prod-table" class="prod-reviews-table">
                <tr id="admin-prod-table-header">
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Prezzo</th>
                    <th>Tipo</th>
                    <th>Piattaforma</th>
                    <th>Sviluppatore</th>
                    <th>Meta</th>
                    <th>Sconto</th>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="table-row-button"><button disabled title="modifica" class="secondary">Modifica</button></td>
                    <td class="table-row-button"><button disabled title="remove" class="secondary attention">&Cross;</button></td>
                </tr>
            </table>
        </div>
    </section>

    <section id="order_management" class="prod-reviews">
        <div class="section-header">
            <h2 class="text-center">Gestisci Ordini</h2>
        </div>
        <div class="catalog-buttons">
            <button id="prev-order-button" disabled class="default alternative general-display-none margin-h-10">Prev</button>
            <button id="next-order-button" class="default general-display-none margin-h-10">Next</button>
            <div class="search_container">
                <form id="order-search" class="search-bar">
                    <span id="label-input-order"><img src="${pageContext.request.contextPath}/img/icons/search.svg" alt="Search"/></span>
                    <input id="search-input-order" aria-labelledby="label-input-order" title="Search Order" type="text" name="search" placeholder="Cerca un ordine" alt="Cerca" />
                </form>
            </div>
        </div>
        <div class="order-filters">
            <button class="active" value="in process">Nuovi</button>
            <button value="shipped">Spediti</button>
            <button value="delivering">In Consegna</button>
            <button value="delivered">Consegnati</button>
            <button value="refunded">Rimborsati</button>
            <button value="canceled">Cancellati</button>
        </div>
        <span class="text-center invalid-credentials general-display-none" id="order_error_message"></span>
        <div class="prod-table prod-reviews-internal">
            <table id="admin-orders-table" class="prod-reviews-table">
                <tr id="admin-orders-table-header">
                    <th>Id Ordine</th>
                    <th>Id Utente</th>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Indirizzo</th>
                    <th>Status</th>
                    <th>Data Ordine</th>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
    </section>

    <section id="users_management" class="prod-reviews">
        <div class="section-header">
            <h2 class="text-center">Gestisci Utenti</h2>
        </div>
        <div class="catalog-buttons">
            <button id="load-users-button" class="default alternative">Carica Dati</button>
            <button id="prev-users-button" disabled class="default alternative general-display-none margin-h-10">Prev</button>
            <button id="next-users-button" class="default general-display-none margin-h-10">Next</button>
            <div class="search_container">
                <form id="users-search" class="search-bar">
                    <span id="label-input-user"><img src="${pageContext.request.contextPath}/img/icons/search.svg" alt="Search"/></span>
                    <input id="search-input-users" aria-labelledby="label-input-user" title="Search User" type="text" name="search" placeholder="Cerca un utente" alt="Cerca" />
                </form>
            </div>
        </div>
        <span class="text-center invalid-credentials general-display-none" id="users_error_message"></span>
        <div class="prod-table prod-reviews-internal">
            <table id="admin-users-table" class="prod-reviews-table">
                <tr id="admin-users-table-header">
                    <th>Id Utente</th>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Telefono</th>
                    <th>Email</th>
                    <th>Indirizzo</th>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
    </section>

    <section id="settings" class="prod-reviews">
        <div class="section-header">
            <h2 class="text-center">Impostazioni Admin</h2>
        </div>

        <div class="margin-v-10">
            <h3 class="text-center margin-v-10">Codici Sconto</h3>
            <div class="margin-v-10 centerized-flex-container discount-div">
                <c:set var="substitute" value="Sostituisci il"/>
                <c:choose>
                    <c:when test="${not empty discountCode}">
                        <c:forEach var="entry" items="${discountCode}" varStatus="eventCount">
                            <div class="space-around-row-flex-container" id="discount-display">
                                <ul class="text-center hide-bullets">
                                    <li class="margin-v-10">Codice Sconto: <span class="green-info">${entry.key}</span></li>
                                    <li>Percentuale Sconto: <span class="discount">${entry.value} </span></li>
                                </ul>
                                <button data-value="${entry.key}" class="remove-discount-button secondary attention">Rimuovi</button>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:set var="substitute" value="Aggiungi un"/>
                        <span class="text-center">Non ci sono Codici Attivi</span>
                    </c:otherwise>
                </c:choose>
                <span class="text-center invalid-credentials general-display-none" id="discount_error_message"></span>
                <span class="text-center">${substitute} Codice:</span>
                <div class="centerized-flex-container log_form">
                    <label class="form-row"><span>Nome: </span><input id="discountName" type="text" name="discountName" required/></label>
                    <label class="form-row"><span>Valore in %: </span><input id="discountValue" type="number" step="0.5" name="discountValue" required/></label>
                    <label class="form-row centerized-row"><button id="add-discountCode-button" class="free-size default alternative" type="submit">Invia</button></label>
                </div>
            </div>
        </div>


        <div class="centerized-flex-container">
            <h3 class="text-center margin-v-10">Cambia Password</h3>
            <div class="log_form admin-change-pwd">
                <form id="pass-change" class="gen-form">
                    <label class="form-row"><span>Vecchia Password</span><input name="old" type="password" required/></label>
                    <label class="form-row"><span>Nuova Password</span><input name="new" type="password" required/></label>
                    <label class="form-row"><span>Conferma Password</span><input name="confirm" type="password" required/></label>
                    <label class="form-row centerized-row"><input class="default free-size" type="submit" name="Cambia"></label>
                </form>
                <div class="margin-h-10 centerized-flex-container"><span class="text-center invalid-credentials general-display-none" id="setting_error_message"></span></div>
            </div>
        </div>
    </section>

</div><!-- End Main_Home Div-->

<%@include file="/WEB-INF/include/footer.jsp"%>
<div class="general-display-none" id="modify-prod-popup">
</div>
</body>
</html>
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
    <script src="${pageContext.request.contextPath}/js/add-product.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/prod-catalog.js" defer></script>
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
                    <span class="error-popup">Inserimento ha provocato un errore, impossibile inserire elemento</span>
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
                <label class="form-row"><span>Prezzo:</span><input type="number" step="0.01" name="price" required pattern="[0-9]+"/></label>
                <fieldset class="form-row">
                    <div>
                        <span>Tipo:</span>
                        <label class="required"><input class="type-prod" type="radio" name="type" value="physic" required/>Fisico</label>
                        <label class="required"><input class="type-prod" type="radio" name="type" value="digital" required/>Digitale</label>
                    </div>
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
                </fieldset>
                <label class="form-row"><span>Descrizione:</span><textarea rows="3" cols="40" name="description" placeholder="Aggiungi qui la descrizione..." required></textarea></label>
                <label class="form-row"><span>Immagine Copertina:</span><input type="file" name="front-image" accept="image/*" required /></label>
                <label class="form-row"><span>Immagini Galleria:</span><input type="file" multiple name="gallery-images" accept="image/*" required /></label>
                <label class="form-row"><span>Developer:</span><input type="text" name="developer" pattern="[a-zA-Z0-9\&$'_\(\)\- ]{2,}" required /> </label>
                <label class="form-row"><span>Key:</span><input type="text" name="key" pattern="[a-zA-Z0-9]{5,15}" /></label>
                <fieldset class="form-row category-form"><span class="title-row">Categorie:*</span>
                    <c:forEach var="cat" items="${category}">
                        <label><input type="checkbox" class="category-input free-size" name="category" value="${cat.id_cat}">${cat.typename}</label>
                    </c:forEach>
                </fieldset>
                <fieldset class="form-row centerized-row">
                    <label><span>&percnt; Sconto:</span><input class="free-size" size="5" type="number" name="discount" min="0" max="100" /></label>
                </fieldset>
                <fieldset class="form-row category-form">
                    <span class="title-row">Condizione:* </span>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="X">Digitale</label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="A"/>A</label>
                        <label class="condition-row">Quantità<input class="free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="B"/>B</label>
                        <label class="condition-row">Quantità<input class="free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="C"/>C</label>
                        <label class="condition-row">Quantità<input class="free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="D"/>D</label>
                        <label class="condition-row">Quantità<input class="free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                    <div class="condition-input form-row flex-start-row bordered-row">
                        <label class="condition-row flex-start-row"><input type="checkbox" name="condition" value="E"/>E</label>
                        <label class="condition-row">Quantità<input class="free-size" min="0" max="100" maxlength="5" size="6" type="number" name="quantity"/></label>
                    </div>
                </fieldset>

                <fieldset class="form-row centerized-row">
                    <input id="add-submit" class="default" type="submit" value="Invia" />
                    <input class="default alternative" type="reset" value="Reimposta"/>
                </fieldset>
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
                    <span><img src="${pageContext.request.contextPath}/img/icons/search.svg" alt="Search"/></span>
                    <label style="display: none" for="search-input">Search</label><input id="search-input" title="Search By Product Name" type="text" name="search" placeholder="Cerca un prodotto" alt="Cerca" />
                </form>
            </div>
        </div>
        <div class="prod-table prod-reviews-internal">
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
    <!-- TODO -->
</div><!-- End Main_Home Div-->

<%@include file="/WEB-INF/include/footer.jsp"%>
<div class="general-display-none" id="modify-prod-popup">
</div>
</body>
</html>
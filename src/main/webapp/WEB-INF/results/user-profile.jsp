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

<div class="main_home" id="home_profile">

    <div class="utente-container">
        <div class="utente-header"><hr><h1 id="username">Bentornato ${userlogged.firstname}</h1><hr></div>
        <div class="utente-content-wrapper">
            <div class="utente-sidebar">
                <ul class="utente-nav" role="menu">
                    <li onclick="showContent('section1')" tabindex="0" role="menuitemradio">Dati Personali</li>
                    <li onclick="showContent('section2')" tabindex="0" role="menuitemradio">I Miei Ordini</li>
                    <li onclick="showContent('section3')" tabindex="0" role="menuitemradio">Sicurezza</li>
                    <li onclick="showContent('section4')" tabindex="0" role="menuitemradio">Indirizzi Di Spedizione</li>
                    <li onclick="document.getElementById('logout').submit();" tabindex="0" role="menuitemradio">Log Out</li>
                </ul>
            </div>

            <form id="logout" action="${pageContext.request.contextPath}/exit-user" method="post" class="general-display-none"></form>

            <div class="utente-main-content">
                <div id="section1" class="utente-content-section">
                    <h2>Dati Personali</h2>
                    <div class="log_form reg_form">
                        <form id="form1" action="${pageContext.request.contextPath}/modify-user" method="post">
                            <input type="hidden" name="section" value="one">
                            <div>
                                <c:if test="${not empty invalidUserUpdate}">
                                    <div class="invalid-credentials">
                                        <span>Dati inseriti non corretti</span>
                                    </div>
                                    <div id="errorUserUpdate" style="display: none;"></div>
                                </c:if>
                            </div>
                            <div class="registry-data">
                                <label class="form-row" for="name"><span>Nome</span>
                                    <input id="name" name="name" type="text" value="${userlogged.firstname}" required size="30" pattern="^[a-zA-ZÀ-ÿ' ]{3,}$" disabled/>
                                </label>
                                <label class="form-row" for="surname"><span>Cognome</span>
                                    <input id="surname" name="surname" type="text" value="${userlogged.lastname}" required pattern="^[a-zA-ZÀ-ÿ' ]{3,}$" />
                                </label>
                                <label class="form-row" for="phone"><span>Numero di Telefono</span>
                                    <input id="phone"  type="tel" name="phone" value="${userlogged.telephone}" required pattern="\+39[0-9]{8,10}"/>
                                </label>
                                <label class="form-row" for="birth"><span>Data di Nascita</span>
                                    <span id="date-format" hidden>Formato data: giorno, mese, anno</span>
                                    <input id="birth" type="date" name="birthdate" value="${userlogged.birth}" required pattern="(\d{1,2}|\d{4})(-|\/)\d{1,2}(-|\/)(\d{4}|\d{1,2})" aria-required="true" aria-describedby="date-format"/>
                                </label>
                                <label class="form-row" for="email"><span>Email</span>
                                    <input id="email" name="email" type="email" value="${userlogged.email}" required autofocus size="30" pattern="^[\w]+[\w\.\-]+@[\w\.\-]+\.[\w]+$" />
                                </label>
                            </div>
                            <div class="address-data">
                                <label class="form-row alternative-row" for="road-type"><span>Indirizzo</span>
                                    <input id="address" type="hidden" value="${userlogged.address}">
                                    <input class="free-size" id="road-type" name="road-type" type="text" value="" size="5" required pattern="^[a-zA-ZÀ-ÿ' ]{3,15}$" maxlength="15" aria-labelledby="road-type"/>
                                    <input class="" id="road-name" name="road-name" type="text" value="" required pattern="^[a-zA-ZÀ-ÿ'\- ]{3,60}$" maxlength="60" aria-labelledby="road-type"/>
                                    <input class="free-size" id="road-number" name="road-number" size="3" value="" type="text" maxlength="6" step="1" required pattern="^[0-9]{1,6}$" aria-labelledby="road-type"/>
                                </label>
                                <label class="form-row" for="city"><span>Citt&agrave;</span>
                                    <input id="city" name="city" type="text" value="${userlogged.city}" required pattern="^[a-zA-ZÀ-ÿ'\- ]{3,50}$" maxlength="50"/>
                                </label>
                                <div class="form-row centerized-row">
                                    <label class="" for="cap"><span>CAP</span>
                                        <input class="free-size" size="5" id="cap" name="cap" type="text" maxlength="5" value="${userlogged.CAP}" required pattern="^[0-9]{5}$" />
                                    </label>
                                    <label class="" for="prov"><span>Prov</span>
                                        <input class="free-size" size="2" id="prov" name="province" type="text" maxlength="2" value="${userlogged.prov}" required pattern="^[a-zA-Z]{2}$" />
                                    </label>
                                </div>
                            </div>
                            <div class="form-row">
                                <input class="default" type="submit" value="Applica Modifica" disabled>
                                <button class="default" type="button" onclick="enableUserModify('form1', this)">Abilita Modifica</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div id="section2" class="utente-content-section" style="display: none">
                    <label id="labelID" for="order-id" style="font-size: 1.5rem">Cerca Ordine per ID</label>
                    <br>
                    <input type="text" id="order-id" name="orderID" placeholder="Search by order id" value="">
                    <div class="utente-tableContainer">
                        <table id="orders-table">
                            <caption>
                                I Miei Ordini
                            </caption>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Status</th>
                                <th>Data Ordine</th>
                                <th>Indirizzo Spedizione</th>
                                <th>Dettagli Ordine</th>
                            </tr>
                        </table>
                    </div>
                </div>
                <div id="section3" class="utente-content-section" style="display: none;">
                    <h2>Sicurezza</h2>
                    <div class="log_form reg_form">
                        <form id="changePassword" action="${pageContext.request.contextPath}/modify-user" method="post">
                            <input type="hidden" name="section" value="three">
                            <div class="access-data">
                                <label class="form-row" for="pass-old"><span>Password Vecchia</span>
                                    <input id="pass-old" name="oldpass" type="password" placeholder="Password Vecchia" required pattern="^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[!£$%&/()=?'^])(?=.*[0-9]).{8,}$" maxlength="30"/>
                                </label>
                                <label class="form-row" for="pass-new"><span>Password Nuova</span>
                                    <input id="pass-new" name="newpass" type="password" placeholder="Password Nuova" required pattern="^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[!£$%&/()=?'^])(?=.*[0-9]).{8,}$" maxlength="30"/>
                                </label>
                                <label class="form-row" for="pass-new-confirm"><span>Conferma Password Nuova</span>
                                    <input id="pass-new-confirm" name="newpassConfirm" type="password" placeholder="Conferma Password Nuova" required pattern="^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[!£$%&/()=?'^])(?=.*[0-9]).{8,}$" maxlength="30"/>
                                </label>
                            </div>
                            <div id="tooltip" class="general-display-none"></div>
                            <div class="form-row">
                                <input id="passSend" class="default" type="submit" value="Applica">
                                <label id="passwordResults"></label>
                            </div>
                        </form>
                    </div>
                </div>
                <div id="section4" class="utente-content-section" style="display: none;">
                    <div class="utente-tableContainer">
                        <table id="addresses-table" class="prod-reviews-table">
                            <caption>
                                Indirizzi Di Spedizione
                            </caption>
                            <tr id="addresses-table-header">
                                <th>Nome</th>
                                <th>Cognome</th>
                                <th>Indirizzo</th>
                                <th>Citta&grave;</th>
                                <th>Provincia</th>
                                <th>Cap</th>
                                <th>Rimuovi</th>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td class="table-row-button"><button onclick="" value="" title="remove" class="secondary attention">&Cross;</button></td>
                            </tr>
                        </table>
                        <span><b>Nota:</b> Non puoi eliminare gli indirizzi che hai già usato per un ordine. <br />Massimo 6 indirizzi</span>
                    </div>

                    <div class="log_form reg_form">
                        <form id="add-address" action="${pageContext.request.contextPath}/modify-user" method="post">
                            <input id="form2" type="hidden" name="section" value="four">
                            <div class="address-data">
                                <label class="form-row alternative-row" for="road-type2"><span>Indirizzo</span>
                                    <input class="free-size" id="road-type2" name="road-type2" type="text" size="5" placeholder="Via"
                                           required maxlength="15"
                                           pattern="^[A-Za-zÀ-ÿ'\- ]{3,15}$" aria-labelledby="road-type2"/>
                                    <input id="road-name2" name="road-name2" type="text" placeholder="Giovanni Paolo" required maxlength="60"
                                           pattern="^[A-Za-zÀ-ÿ''\- ]{3,60}$" aria-labelledby="road-type2"/>
                                    <input class="free-size" id="road-number2" name="road-number2" size="3" type="text" maxlength="6"
                                           placeholder="1" step="1" required pattern="^\d{1,6}$" aria-labelledby="road-type2"/>
                                </label>
                                <label class="form-row" for="city2"><span>Citt&agrave;</span>
                                    <input id="city2" name="city2" type="text" placeholder="Città" required maxlength="50"
                                           pattern="^[a-zA-ZÀ-ÿ'\- ]{3,50}$"/>
                                </label>
                                <div class="form-row centerized-row">
                                    <label class="" for="cap2"><span>CAP</span>
                                        <input class="free-size" size="5" id="cap2" name="cap2" type="text" maxlength="5"
                                               placeholder="CAP" required pattern="^[0-9]{5}$"/>
                                    </label>
                                    <label class="" for="prov2"><span>Prov</span>
                                        <input class="free-size" size="2" id="prov2" name="prov2" type="text" maxlength="2"
                                               placeholder="RM" required pattern="^[a-zA-Z]{2}$"/>
                                    </label>
                                </div>
                            </div>
                            <div class="form-row centerized-row">
                                <input class="default free-size" type="submit" value="Aggiungi Indirizzo">
                                <c:if test="${not empty invalidAddresses}">
                                    <div class="invalid-credentials">
                                        <span>Indirizzo inserito errato</span>
                                    </div>
                                    <div id="errorAddresses" style="display: none;"></div>
                                </c:if>
                            </div>
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
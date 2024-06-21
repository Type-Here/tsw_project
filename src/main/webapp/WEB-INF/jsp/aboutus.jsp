<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Retrogamer: Chi siamo</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/aboutus.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
        <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
        <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    </head>
    <body class="body_def">

        <%@include file="/WEB-INF/include/header-standard.jsp"%>

        <div class="main_home">

            <div class="grid-container">

                <div class="grid-item grid-img1"><img
                        src="${pageContext.request.contextPath}/img/chisiamo/gentechegiocaretro.jpeg"
                        alt="Immagine Storia"></div>

                <div class="grid-item grid-txt1">
                    <h2>Storia</h2>
                    <h3>Retrogamer: un viaggio nel tempo videoludico</h3>
                    <p>
                        Nel cuore del 2010, due amici accomunati da una passione ardente per i videogiochi retro,
                        Manuel e Gianluigi, decisero di trasformare la loro nostalgia in realtà. Nacque così
                        Retrogamer, un negozio che avrebbe catapultato gli appassionati in un'epoca videoludica
                        ormai passata, ma mai dimenticata.
                    </p>
                </div>

                <div class="grid-item grid-txt2">
                    <h2>Chi Siamo</h2>
                    <h3>Un sogno che prende vita</h3>
                    <p>
                        Retrogamer non era solo un negozio, ma un tempio dedicato ai videogiochi del passato.
                        Tra gli scaffali ordinati si potevano trovare rarità come Atari 2600, N64, Commodore64 e Sega
                        Mega Drive
                        accanto a classici indimenticabili come Pac-Man, Super Mario Bros. e Zelda. Manuel e Gianluigi
                        non si limitavano a vendere, ma condividevano la loro passione con i clienti,
                        raccontando aneddoti e curiosità su ogni titolo.
                    </p>
                </div>

                <div class="grid-item grid-img2"><img src="${pageContext.request.contextPath}/img/chisiamo/negozio.jpeg"
                                                      alt="Immagine Chi Siamo"></div>

                <div class="grid-item grid-txt3">
                    <h2>Valori</h2>
                    <h3>Passione</h3>
                    <p>
                        La passione per i videogiochi retro è il motore che ha spinto Manuel e Gianluigi a
                        creare Retrogamer. Ogni prodotto in vendita è selezionato con cura e attenzione,
                        per offrire ai clienti un'esperienza autentica e coinvolgente.
                    </p>
                </div>

                <div class="grid-item grid-txt3">
                    <h2>Obiettivi</h2>
                    <h3>Esperienza unica</h3>
                    <p>
                        Retrogamer si impegna a offrire ai clienti un'esperienza unica e indimenticabile.
                        Ogni prodotto in vendita è selezionato con cura e attenzione, per garantire la massima
                        qualità e autenticità.
                    </p>
                </div>

                <div class="grid-item grid-txt3">
                    <h2>Missione</h2>
                    <h3>Passione condivisa</h3>
                    <p>
                        Manuel e Gianluigi condividono con i clienti la loro passione per i videogiochi retro,
                        raccontando aneddoti e curiosità su ogni titolo. Retrogamer non è solo un negozio, ma
                        un luogo dove la nostalgia si mescola alla realtà.
                    </p>
                </div>

                <div class="grid-container-2">

                    <div class="grid-item grid-txt4">
                        <h2>Contatti</h2>
                        <h3>Resta in contatto con noi</h3>
                        <p>
                            Se hai domande, suggerimenti o richieste, non esitare a contattarci. Siamo sempre
                            disponibili ad ascoltare i nostri clienti e a rispondere alle loro esigenze.
                        </p>
                        <h4>Email</h4>
                        <p>
                            Supporto: <strong><a
                                href="mailto:supporto@retrogamer.it">supporto@retrogamer.it</a></strong>
                            <br>
                            Vendi i tuoi giochi: <strong><a href="mailto:vendituoigiochi@retrogamer.it">vendituoigiochi@retrogamer.it</a></strong>
                            <br>
                            Collaborazioni: <strong><a href="mailto:lavoraconnoi@retrogamer.it">lavoraconnoi@retrogamer.it</a></strong>
                        </p>
                        <h4>Telefono</h4>
                        <p>
                            Assistenza: <strong><a href="tel:0123456789">0123456789</a></strong>
                            <br>
                            Negozio Fisciano: <strong><a href="tel:0123456789">0123456789</a></strong>
                            <br>
                            Negozio Napoli: <strong><a href="tel:0123456789">0123456789</a></strong>
                        </p>
                        <h4>Orari</h4>
                        <p>
                            Lun-Ven: 9:00-18:00
                            <br>
                            Chiuso la Domenica</p>
                        <h4>Indirizzo</h4>
                        <p>
                            Negozio Fisciano: Via Giovanni Paolo II, 132, 84084 Fisciano SA
                            <br>
                            Negozio Napoli: Via Foggia, 2, 80143 Napoli NA
                        </p>
                        <div class="google-map">
                            <iframe src="https://www.google.com/maps/embed?pb=!4v1715694165381!6m8!1m7!1s6DJxZCgVENHSGe_e982cJg!2m2!1d40.77539526666076!2d14.79006064263439!3f192.40696386492903!4f0.4766663975700425!5f0.7820865974627469"
                                    width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy"
                                    referrerpolicy="no-referrer-when-downgrade"></iframe>
                        </div>
                    </div>

                    <div class="grid-item grid-txt4">
                        <h2>Il nostro team</h2>
                        <h3>Conosci il nostro staff</h3>
                        <p>
                            Retrogamer è composto da un team di esperti appassionati di videogiochi retro.
                            Ogni membro del team è specializzato in un'area specifica, per garantire ai clienti
                            un'assistenza professionale e competente.
                        </p>
                        <h4>Manuel & Gianluigi</h4>
                        <p>
                            Manuel e Gianluigi sono i fondatori di Retrogamer. Visionari e appassionati di
                            videogiochi, hanno dato vita al negozio con la loro dedizione e il loro amore per
                            i titoli retrò.
                        </p>
                        <h4>Domenico</h4>
                        <p>
                            Gestisce il negozio di Fisciano con professionalità e competenza, assicurando
                            un'esperienza d'acquisto piacevole a tutti i clienti.
                        </p>
                        <h4>Francesca</h4>
                        <p>
                            Alla guida del negozio di Napoli, Francesca porta avanti la passione per i videogiochi
                            retrò con entusiasmo e conoscenza.
                        </p>
                        <h4>Giuseppe</h4>
                        <p>
                            Esperto e disponibile, Giuseppe si occupa del reparto assistenza, risolvendo problemi
                            tecnici e fornendo supporto ai clienti.
                        </p>
                        <h4>Luca & Martina</h4>
                        <p>
                            Il duo dinamico di Retrogamer, Luca e Martina, si occupano del reparto marketing
                            e comunicazione, promuovendo il negozio e coinvolgendo i clienti con iniziative
                            creative e originali.
                        </p>
                        <br>
                        <p>
                            Oltre a queste figure chiave, il team è composto da altri membri altrettanto appassionati
                            e dedicati, che lavorano insieme per offrire ai clienti un'esperienza unica e
                            indimenticabile.
                            <br><br>
                            Insieme, il nostro team rappresenta una forza di passione, competenza e dedizione,
                            garantendo a tutti gli amanti dei retrogames un luogo accogliente e ricco di tesori
                            da scoprire.
                        </p>
                    </div>

                </div>

            </div>

        </div>

        <%@include file="/WEB-INF/include/footer.jsp"%>

    </body>
</html>

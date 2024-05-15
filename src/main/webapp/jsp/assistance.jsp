<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Retrogamer: Assistenza</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/assistance.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
        <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
        <script src="${pageContext.request.contextPath}/js/assistance.js" defer></script>
        <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    </head>

    <body class="body_def">

        <%@include file="/WEB-INF/include/header-standard.jsp"%>

        <div class="main_home">


            <div class="grid-container-faq">

                <div class="grid-item-faq grid-faq-header" id="q-header">
                    <h1>Domande Frequenti</h1>
                </div>
                <div class="grid-item-faq grid-faq0" id="q-spedizioni">
                    <h1>Spedizioni</h1>
                </div>
                <div class="grid-item-faq grid-faq0" id="q-prodotti">
                    <h1>I nostri prodotti</h1>
                </div>
                <div class="grid-item-faq grid-faq0" id="q-pagamento">
                    <h1>Pagamenti</h1>
                </div>
                <div class="grid-item-faq grid-faq0" id="q-lavora-con-noi">
                    <h1>Lavorare con noi</h1>
                </div>

                <div class="grid-item-faq grid-faq-base" id="q-domande-frequenti">
                    <h2>Domande Frequenti</h2>
                    <p>Qui troverai le risposte alle domande più frequenti. Se non trovi la risposta che cerchi, contattaci.</p>
                </div>

                <div class="grid-item-faq grid-faq1" id="faq-spedizioni">
                    <ul>
                        <li><h2>Quanto tempo ci vuole per ricevere il mio ordine?</h2>
                            <p>
                                Il costo della spedizione è di 5€ per ordini inferiori a 50€, mentre è gratuita per ordini superiori a 50€.
                                I tempi di consegna variano a seconda della destinazione e del metodo di spedizione scelto. In generale,
                                gli ordini vengono evasi entro 24 ore lavorative e consegnati entro 3-5 giorni lavorativi in Italia.
                                Per spedizioni internazionali, i tempi di consegna possono variare da 5 a 10 giorni lavorativi.
                            </p>
                        </li>

                        <li><h2>Quali sono i costi di spedizione?</h2>
                            <p>
                                Il costo della spedizione è di 15€ per ordini inferiori a 100€, mentre è gratuita per ordini superiori a 100€.
                            </p>
                        </li>

                        <li><h2>Posso ritirare l'ordine di persona?</h2>
                            <p>
                                Sì, è possibile ritirare l'ordine di persona presso uno dei nostri negozi a Fisciano o Napoli.
                                In questo caso, non saranno applicate spese di spedizione.
                            </p>
                        </li>
                    </ul>
                </div>
                <div class="grid-item-faq grid-faq1" id="faq-prodotti">
                    <ul>
                        <li><h2>Come posso verificare la disponibilità di un prodotto?</h2>
                            <p>
                                Per verificare la disponibilità di un prodotto, basta visitare la pagina del prodotto e controllare la
                                disponibilità. Se il prodotto è disponibile, sarà possibile procedere con l'acquisto.
                            </p>
                        </li>

                        <li><h2>Offrite una garanzia sui vostri prodotti?</h2>
                            <p>
                                Sì, offriamo una garanzia di 12 mesi su tutti i nostri prodotti. Inoltre è possibile restituirlo entro
                                30 giorni dall'acquisto per ottenere un rimborso o una sostituzione.
                            </p>
                        </li>

                        <li><h2>Quali prodotti vendete?</h2>
                            <p>Vendiamo una vasta gamma di videogiochi retrò per diverse console, tra cui:</p>
                            <ul>
                                <li>Atari 2600</li>
                                <li>Commodore 64</li>
                                <li>GameBoy</li>
                                <li>GameCube</li>
                                <li>Nintendo 64</li>
                                <li>PC</li>
                                <li>PlayStation 1</li>
                                <li>PlayStation 2</li>
                                <li>Sega Mega Drive</li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <div class="grid-item-faq grid-faq1" id="faq-pagamento">
                    <ul>
                        <li><h2>Quali metodi di pagamento accettate?</h2>
                            <p>Accettiamo i seguenti metodi di pagamento:</p>
                            <ul>
                                <li>Carte di credito (Visa, Mastercard, American Express)</li>
                                <li>PayPal</li>
                                <li>PayPal</li>
                                <li>Apple Pay</li>
                                <li>Google Pay</li>
                                <li>Contrassegno (solo per l'Italia)</li>
                            </ul>
                        </li>

                        <li><h2>Il mio pagamento è sicuro?</h2>
                            <p>
                                Sì, la tua transazione è sicura al 100%. Utilizziamo i migliori sistemi di sicurezza per proteggere
                                i tuoi dati personali e finanziari.
                            </p>
                        </li>
                    </ul>
                </div>
                <div class="grid-item-faq grid-faq1" id="faq-lavora-con-noi">
                    <ul>
                        <li><h2>Siete alla ricerca di nuovi collaboratori?</h2>
                            <p>
                                Sì, siamo sempre alla ricerca di nuovi talenti per ampliare il nostro team. Se sei appassionato di
                                videogiochi retrò e hai competenze in ambito commerciale, marketing, sviluppo web o altro, inviaci il
                                tuo curriculum vitae e una lettera di presentazione alla nostra email: <strong><a href="mailto:lavoraconnoi@retrogamer.it">lavoraconnoi@retrogamer.it</a></strong>
                            </p>
                        </li>

                        <li><h2>Come posso proporvi un mio prodotto da vendere?</h2>
                            <p>
                                Se sei un collezionista o uno sviluppatore di videogiochi retrò e hai prodotti da vendere, inviaci
                                una descrizione dettagliata e alcune foto all'indirizzo email: <strong><a href="mailto:vendituoigiochi@retrogamer.it">vendituoigiochi@retrogamer.it</a></strong>
                                Valuteremo la tua proposta e ti contatteremo se siamo interessati.
                            </p>
                        </li>
                    </ul>
                </div>
            </div>

        </div>

        <%@include file="/WEB-INF/include/footer.jsp"%>

    </body>
</html>

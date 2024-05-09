-- Testing Elements

-- 1 Product
insert into retrogamer.products(name, price, type, platform, developer, description, metadata, condition) values
--PS1
('Crash Team Racing', 30.00, 0, 'ps1', 'Naughty Dog', 'Le corse di kart vanno molto di moda sulla Terra. Crash e la sua banda hanno costruito piste nelle loro terre e sono in continua competizione. Sfortunatamente, un alieno malvagio di nome Nitros Oxide proveniente dal pianeta Gasmoxia minaccia il pianeta e sfida i suoi abitanti. Egli afferma di essere il più grande pilota della galassia e si diverte a sfidare gli altri pianeti in un "piccolo gioco" dal nome "Vinca il più veloce". Questa volta tocca alla Terra.', '000001.json','A'),
('Crash Bandicoot', 38.00, 0, 'ps1', 'Naughty Dog', 'In un immaginario arcipelago situato al sud delle coste dell''Australia, il malvagio dottor Neo Cortex ambisce a conquistare il mondo intero; assieme al suo braccio destro Nitrus Brio, egli, mediante un macchinario di loro invenzione, l''Evolvo Ray, capace d''indurre forzatamente una forma d''evoluzione accelerata nei soggetti interessati, crea un potentissimo esercito di animali mutanti, i Cortex Commandos.', '000002.json', 'A'),
('Final Fantasy VII', 70.00, 0, 'ps1', 'Square', 'La trama di Final Fantasy VII ruota attorno alle vicende del protagonista, Cloud Strife, un mercenario che farà parte inizialmente di un''organizzazione di ecoterroristi chiamata AVALANCHE, intenzionata a fermare le malvagie attività della compagnia Shinra. Con il progredire della storia, però, il gruppo di Cloud rimarrà coinvolto in un conflitto di proporzioni inaspettate, arrivando a scontrarsi con il leggendario SOLDIER Sephiroth, l''antagonista principale del videogioco.', '000003.json', 'A'),
('Formula 1 97 Edizione Limitata', 399.99, 0, 'ps1', 'Psygnosis', 'Formula 1 97, chiamato Formula 1 Championship Edition nell''edizione statunitense, è il secondo videogioco basato sulla stagione 1997 di Formula 1. Il gioco ricopre tutti e 22 i piloti e i circuiti inclusi nella rispettiva stagione, compreso il circuito di Estoril, anche se non fa parte del calendario di questa stagione della Formula 1.', '000004.jpeg', 'A'),
('Formula 1 98', 17,90, 0, 'ps1', 'Psygnosis', 'Formula 1 98 è un videogioco basato sulla stagione 1998 di Formula 1. Il gioco ricopre tutti e 22 i piloti partecipanti di questa stagione e di tutti i circuiti. Il gioco ha la licenza ufficiale FIA.', '000005.json', 'B'),
('Gran Turismo', 29.50, 0, 'ps1', 'Polyphony Digital', 'Gran Turismo, conosciuto anche come GT, è un simulatore di guida sviluppato su console della famiglia Sony PlayStation. Sono previste due modalità di gioco: Arcade e Gran Turismo. Nel primo caso il giocatore ha a disposizione un numero piuttosto elevato di vetture che riproducono, in maniera fedele, le caratteristiche di modelli di automobili realmente esistenti. Con queste vetture può partecipare a gare a diversi livelli di difficoltà, sfidare altri giocatori o provare qualunque circuito desideri. Molto più originale è la modalità "Gran Turismo": in questo caso il giocatore è un pilota alle prime armi che, per poter raggiungere il successo deve affrontare, l''una dopo l''altra una serie di prove di abilità.', '000006.json', 'A'),
('Gran Turismo 2', 29.50, 0, 'ps1', 'Polyphony Digital', 'Il gioco è composto da due versioni, Arcade e Simulation (in Italiano "Modalità GT"), che rimangono rispettivamente la modalità per fare velocemente gare con gli amici e quella per intraprendere una realistica carriera da pilota. Ogni modalità ha un CD dedicato, in modo da poter sfruttare al massimo la capacità del CD e poter aumentare la sua bellezza sia come grafica sia come assortimento (superando i 600 modelli) d''auto disponibili.', '000007.json', 'A'),
('Hogs of War', 31.99, 0, 'ps1', 'Infogrames Studios Ltd.', 'Le fazioni mirano alla conquista della Maialustralasia. Il controllo di questa isola-nazione (dalle fattezze di un maiale) è fondamentale per il controllo del pianeta, poiché è ricca di sbobba, elemento cruciale per la vita dei maiali. Il giocatore deve guidare il suo manipolo di maiali-soldato in scontri sempre più duri contro i livelli gestiti dal computer (modalità singolo giocatore) o contro altri giocatori umani o IA nella modalità DeathMatch.', '000008.json', 'A'),
('Resident Evil 2', 49.70, 0, 'ps1', 'Capcom', 'La trama del gioco segue le vicende di due sopravvissuti, Leon e Claire, in fuga da una città degli Stati Uniti d''America medio-occidentali, Raccoon City, in preda a un''epidemia virale causata da un''azienda farmaceutica assetata di denaro, che ne ha trasformato la maggior parte della popolazione in zombi.', '000009.jpeg', 'A'),
('Spider-Man', 14.99, 0, 'ps1', 'Neversoft', 'La vicenda inizia durante un convegno scientifico dove il Dottor Octopus, che ha apparentemente abbandonato la vita malavitosa, svolge un importante esperimento. Tra gli spettatori sono presenti Peter Parker e Eddie Brock. Durante l''esperimento però si presenta un falso Spider-Man che ruba il macchinario di Octopus davanti a tutti e si allontana dopo aver frantumato la macchina fotografica di Eddie Brock. Peter si chiede chi possa essere quell''impostore, mentre Eddie si trasforma in Venom e giura vendetta contro Spider-Man per quello che è successo.', '000010.json', 'C'),

--PS2
('Bully Collector''s Edition', 1499.95, 0, 'ps2', 'Rockstar Vancouver', 'Il giovane quindicenne Jimmy Hopkins, animo ribelle, viene rinchiuso dalla madre, al suo 5º matrimonio, nella Bullworth Academy, scuola privata situata nel New England governata da bande di bulli; qui incontra una varietà di persone, che vanno dall''autoritario preside Crabblesnitch all''erratico Gary Smith. Quest''ultimo lo porterà ad avviare una lotta senza quartiere per diventare il re della scuola, che lo porterà a scontrarsi con i ragazzi del luogo, tutto questo tra avventure e peripezie varie.', '000011.json', 'A'),
('Gran Turismo 4', 18.50, 0, 'ps2', 'Polyphony Digital', 'Rispetto al suo predecessore, Gran Turismo 4 è un prodotto di maggiore qualità tecnica sotto diversi punti di vista; inoltre sono stati aggiunti nuovi tracciati. Il gioco supporta anche il rapporto d''aspetto 16:9 in 1080i. Il numero di vetture è enorme, grazie al fatto dell''aggiunta, oltre ai nuovi modelli, di macchine d''epoca (tra cui la Benz Patent Motorwagen, vettura del 1886 della Benz, allora non ancora unitasi con la Daimler oppure la Ford Model T del 1915 o ancora la Fiat 500 e l''Autobianchi A112) per un totale che supera le 700 vetture disponibili. All''interno del gioco sono presenti 51 tracciati,', '000012.json', 'A'),
('Grand Theft Auto San Andreas', 19.90, 0, 'ps2', 'Rockstar North', 'Los Santos, 1992, Carl Johnson, detto "CJ", è un gangster afroamericano membro delle Grove Street Families, una gang di strada situata a Ganton, un quartiere di Los Santos. Nel 1987, a seguito della morte del fratello minore Brian in circostanze non chiarite mentre questi era sotto la sua responsabilità,', '000013.json', 'A'),
('Harry Potter e La Camera Dei Segreti ', 44.20, 0, 'ps2', 'Eurocom', 'Harry Potter, dopo una terribile estate in compagnia dei pestiferi zii, ritorna ad Hogwarts per iniziare il suo secondo anno. Il problema è che nella scuola alcuni studenti (rigorosamente figli di babbani) vengono trasformati in pietra, e ci sono numerose dicerie su un possibile Erede di Serpeverde. La situazione precipita quando la sua amica Hermione Granger viene pietrificata e la piccola Ginny Weasley viene rapita e portata nella Camera dei Segreti. Harry, come sempre, risolverà la situazione.', '000014.json', 'A'),
('Il Padrino Edizione Limitata', 21.99, 0, 'ps2', 'Visceral Games', 'New York, 1936: Johnny Trapani è un soldato della famiglia Corleone che gestisce un panificio di Little Italy insieme alla moglie Serafina. Un giorno però, il suo negozio viene fatto saltare in aria e lui viene ucciso dagli uomini della famiglia Barrese, in presenza del loro padrino Don Emilio, che lo voleva punire per la sua fedeltà ai Corleone e i suoi buoni rapporti con alcuni pezzi grossi della Famiglia. Suo figlio Aldo, allora dodicenne, è presente all''evento e rimane sconvolto, ma prima che possa commettere qualche sciocchezza viene fermato da Don Vito Corleone in persona che gli promette che un giorno, quando sarà abbastanza grande, potrà vendicarsi.', '000015.json', 'C'),
('James Bond 007- From Russia With Love', 18.77, 0, 'ps2', 'EA Redwood Shores', 'Il gioco è basato sul romanzo A 007, dalla Russia con amore di Ian Fleming e sull''omonimo film. James Bond è chiamato ad aiutare Tatiana Romanova, agente della sicurezza di stato sovietica pronta a disertare, e a recuperare una macchina crittografica, il Lektor. Il gioco ha ricevuto giudizi generalmente positivi dalla critica e dai fan sorpresi di rivedere Sean Connery nei panni di Bond, il quale ha doppiato personalmente il suo ruolo e ha lavorato assieme ai programmatori, supervisionando ogni aspetto della produzione.', '000016.json', 'A'),
('Metal Gear Solid 3 SNAKE EATER', 27.90, 0, 'ps2', 'Konami', 'Primavera 1964. In piena guerra fredda un aereo dell''esercito statunitense sorvola lo spazio aereo sovietico con a bordo alcuni membri dell''unità FOX, tra i quali Naked Snake, Major Zero e Para-Medic. Scopo dell''operazione "Missione Virtuosa" è il salvataggio dello scienziato Nikolai Stepanovich Sokolov: questi infatti era riuscito a progettare e costruire un carro armato potentissimo, lo Shagohod, in grado di lanciare, da qualsiasi terreno e posizione, missili nucleari dall''Unione Sovietica contro gli Stati Uniti.', '000017.json', 'A'),
('Need for Speed Underground 2', 9.99, 0, 'ps2', 'Electronic Arts', 'Il gioco è basato sulle corse di auto clandestine, e consente la modifica di auto di serie per il raggiungimento di prestazioni estreme (tuning), riprendendo la trama di Need for Speed: Underground. Rispetto al predecessore, Need for Speed: Underground 2 fornisce molte nuove caratteristiche, come i nuovi metodi nel selezionare le sfide, la modalità "esplorazione" all''interno di una grande città conosciuta come Bayview', '000018.json', 'C'),
('Ratchet & Clank 2 Fuoco a Volontà', 19.99, 0, 'ps2', 'Insomniac Games', 'A sei mesi dalla sconfitta del presidente Drek, Ratchet e Clank vengono chiamati da Abercrombie Fidzzwiget, il presidente della Megacorp, un''industria di armi e gadget della galassia Bogon, dove è in vendita un animaletto blu creato in laboratorio, il Protopet, all''apparenza tenero, piccolo e pelosetto, ma in realtà tremendo e vorace. I due eroi saranno contattati per recuperare il piccolo animale, rubato da un misterioso ladro mascherato.', '000019.json', 'A'),
('Sly 2: La Banda dei Ladri ', 18.99, 0, 'ps2', 'Sucker Punch', 'Due anni dopo la sconfitta di Clockwerk, Sly e la banda scoprono che le parti del gufo robotico sono ancora funzionanti, e sono custodite al museo di storia naturale del Cairo. Giunti sul luogo, il trio scopre che le parti sono già state rubate. Sly viene intercettato dall''ispettrice Carmelita Fox e dall''agente Neyla: questa si lascia sfuggire che le parti potrebbero essere state rubate da un noto gruppo criminale, la banda Klaww.', '000020.json', 'B'),

--PS1
('Star Wars Dark Forces', 22.70, 0, 'ps1', 'LucasArts', 'Il gioco introduce il personaggio di Kyle Katarn, un ex agente imperiale ora mercenario assoldato dalla Alleanza Ribelle. Dopo la battaglia di Yavin, Kyle viene contattato ancora da Mon Mothma per investigare su un assalto imperiale nella base Tak di Talay eseguito con un tipo di soldato mai visto prima. L''investigazione di Kyle rivela che il progetto dei dark trooper è comandato dal generale Rom Mohc.', '000021.json', 'A'),
('Topolino E Le Sue Avventure', 299.80, 0, 'ps1', 'Traveller''s Tales', 'Mickey Mania, la cui sua difficoltà è selezionabile dalle opzioni sul menù iniziale (tra facile, medio o difficile), si articola in sei livelli divisi da un diverso numero di scene, basati su dei corti e film classici di Topolino, ove egli stesso incontra le sue controparti che apparivano nei cartoni medesimi. Il giocatore lo controlla per farlo proseguire verso la fine di essi, facendogli attraversare ostacoli, affrontare pericoli e, soprattutto, sconfiggere nemici lanciando delle biglie (a volte anche saltando loro sopra).', '000022.json', 'A'),

--N64
('Mario Kart 64', 69.00, 0, 'N64', 'Nintendo EAD', 'n Mario Kart 64 le corse durano sempre 3 giri, e a esse partecipano sempre otto piloti. Il videogioco è progettato per essere giocato da 1 a 4 giocatori (per la prima volta nella serie di Mario Kart), anche se non tutte le modalità sono disponibili quando sono collegati 4 joystick. In Mario Kart 64 sono presenti tre modalità di gioco. Oltre alla modalità "Mario GP", simile a una corsa di Formula 1, vi sono la modalità Sfida (uno contro uno), Battaglia (dove lo scopo non è vincere una gara, ma colpire l''avversario tre volte con le proprie armi) e Prova a Tempo (una sorta di allenamento in cui è possibile correre contro il proprio fantasma per migliorare il proprio tempo).', '000023.json', 'D'),
('Pokemon Puzzle League', 98.99, 0, 'N64', 'Intelligent Systems', 'Pokémon Puzzle League è un videogioco rompicapo crossover tra la serie Pokémon e la serie Puzzle League per Nintendo 64. Il gameplay è simile a quello di Panel de Pon, con gran parte dell''attenzione sulla strategia basata su puzzle nella griglia di gioco. Per avanzare a nuovi livelli, i giocatori devono affrontare gli allenatori e i capipalestra del gioco, simili a quelli presenti in Pokémon Rosso e Blu e Pokémon Giallo.', '000024.json', 'C'),
('The legend of Zelda Ocarina Of Time', 125.50, 0, 'N64', 'Nintendo EAD', 'Link è un ragazzino di otto anni che abita nella foresta dei Kokiri, un popolo di eterni bambini protetti dal Grande Albero Deku e da delle fatine, loro compagne di vita. Link è diverso dai Kokiri in quanto non gli è stata ancora assegnata una fata. Una notte ha un incubo, in cui un uomo in armatura nera insegue a cavallo una ragazza durante una notte tempestosa. Il mattino seguente arriva finalmente la fatina destinata a essere sua compagna, Navi, che lo conduce al cospetto del Grande Albero Deku, il quale a causa di una maledizione sta per morire.', '000025.json', 'C'),

--PC
('Alone In The Dark 2', 190.99, 0, 'PC', 'Infogrames', 'Alone in the Dark 2 è il secondo capitolo della saga di Alone in the Dark. A differenza del primo titolo, la componente horror è stata notevolmente diminuita: ora i nemici principali sono pirati e gangster, sebbene la componente paranormale sia garantita da riti Voodoo che trasformano alcuni nemici in zombi.', '000026.json', 'B'),
('Doom II', 174.80, 0, 'PC', 'id Software', 'Subito dopo gli eventi di Doom, il protagonista senza nome detto doomguy si è dimostrato troppo tenace per le forze dell''Inferno. Dopo essere stato teletrasportato da Phobos e successivamente alla battaglia su Deimos, il Marine ritorna a casa sulla Terra, scoprendo che il pianeta è stato conquistato dalle forze degli inferi.', '000027.json', 'B'),
('Doom', 774.85, 0, 'PC', 'id Software', 'Il giocatore indossa i panni di un marine spaziale, che nella letteratura dedicata al gioco viene indicato come "Flynn Taggart", ma più in generale viene indicato con il termine Doomguy (letteralmente "tizio di Doom"), trasferito su Marte per aver assalito un superiore che gli aveva ordinato di sparare su civili disarmati. Costretto a lavorare per la Union Aerospace Corporation, il protagonista viene inviato ad indagare su un misterioso incidente avvenuto nel corso dello svolgimento di esperimenti militari sul teletrasporto tra le due lune Phobos e Deimos: orde di mostri hanno cominciato ad uscire dai portali di teletrasporto e il satellite Deimos è sparito.', '000028.json', 'A'),

--PS1
('WWF Smackdown!', 54.90, 0, 'ps1', 'Yuke''s', 'La modalità storia principale del gioco contiene tre parti principali, prima con la Pre-Season (per i lottatori creati), ma dopo dieci anni di gioco in-game, i giocatori sono autorizzati a saltarla. Giocando e avanzando nelle modalità stagionali i giocatori ottengono ricompense come sbloccabili o abiti, ma invece di sbloccare nuovi personaggi, i giocatori sbloccano nuove parti del corpo per indossare nuove creazioni, per giocare come quel personaggio "sbloccato".', '000029.json', 'A'),

--PS2
('Street fighter 3rd strike The Limited edition', 500, 0, 'ps2', 'Capcom', 'Rilasciato nel maggio 1999, la terza e ultima edizione di Street Fighter III ha riportato il classico personaggio di Street Fighter II Chun-Li, insieme a quattro nuovi personaggi (Makoto, Remy, Q e Twelve), estendendo il roster selezionabile a 19 personaggi, con Akuma che ora è un personaggio regolare. A tutti i personaggi di ritorno dei precedenti giochi di Street Fighter III sono state date nuove fasi, finali e doppiatori per determinati personaggi, continuando la trama generale da dove si erano lasciati i primi due giochi.', '000030.json', 'A'),

--PC
('F/A-18- Operation Desert Storm', 7.99, 0, 'PC', 'GMX Media', 'Vola con l''F|A-18 Hornet Fighter|Attack Aircraft su uno spettacolare paesaggio iracheno basato su immagini satellitari.', '000031.json', 'A'),
('Fallout 3 Survival Edition', 2090.99, 0, 'PC', 'Bethesda Game Studios', 'Trama. Il gioco si svolge nel 2277 negli Stati Uniti d''America a Washington, circa duecento anni dopo una guerra nucleare che ha trasformato quasi tutta la Terra in una distesa arida e irradiata. Alcuni cittadini trovarono la salvezza in bunker sotterranei chiamati Vault: il personaggio giocante vive nel Vault 101. Inizialmente la trama prende parte nei primi diciannove anni di vita del protagonista con quattro brevi prologhi nel momento della nascita e all''età di uno, dieci e sedici anni; poi il protagonista lascia il Vault 101 alla ricerca del padre, James, uno degli scienziati più rispettati della comunità che è scappato verso il mondo esterno e uscito dal bunker.', '000032.json', 'A'),
('Mafia 1 City of Lost Heaven Special Edition', 249.99, 0, 'PC', 'Illusion Softworks', 'Nel 1930, il tassista Thomas "Tommy" Angelo si ritrova costretto a portare in salvo due membri della famiglia criminale Salieri, Paulie e Sam, da un''imboscata della famiglia Morello. L''aiuto di Tommy viene ricompensato e gli viene offerto un posto all''interno dell''organizzazione di Salieri, che Tommy ancora una volta è costretto ad accettare quando, il giorno seguente, due gangster di Morello gli sfasciano il taxi per vendicarsi. Tommy viene accolto nella famiglia Salieri e inizia ad assisterla nella gestione del racket di Lost Heaven.', '000033.json', 'A'),
('Pacific Storm The Midway Campaign IBM', 90.90, 0, 'PC', 'Simulations Canada', 'Gioco simulativo basato sulla battaglia delle Midway', '000034.json', 'A'),
('Pinball Special Edition Dreams & Fantasies', 30.00, 0, 'PC', 'Digital Illusions', 'Pinball Dreams è un videogioco di simulazione di flipper. Il gioco si compone di quattro tavole, ognuna con un tema specifico, come accade per la quasi totalità dei flipper da sala giochi e dei pachinko giapponesi.', '000035.json', 'A'),
('SimCity 2000 Edizione Speciale', 19.90, 0, 'PC', 'Maxis', 'Il protagonista dei videogiochi della serie è il sindaco di una città, la quale può essere creata da zero o sviluppata partendo da uno degli scenari prestabiliti. Tra i principali cambiamenti rispetto alla versione precedente, in SimCity 2000 spicca la presenza di una grafica tridimensionale isometrica, molto più evoluta rispetto all''unica visuale dall''alto della prima edizione del gioco.', '000036.json', 'A'),
('The elder scrolls Arena', 2500.00, 0, 'PC', 'Bethesda Softworks', 'Anno 399 della 3ª era: l''imperatore Uriel Septim VII è stato imprigionato nella dimensione parallela di Oblivion e il trono è stato usurpato dal suo consigliere Jagar Tharn, il mago-guerriero imperiale. L''unico modo per riportarlo indietro e salvare l''Impero è ritrovare gli otto pezzi del Bastone del Caos sparsi in tutta Tamriel. L''unica che sembra conoscere il modo per sconfiggere l''usurpatore è Ria Silmane, l''apprendista di Jagar Tharn che si rifiuta di seguire la via del tradimento intrapresa dal suo mentore.', '000037.json', 'A'),

--C64
('Farming Simulator Limited Edition C64 Edition', 1800.00, 0, 'C64', 'GIANTS software', 'Farming Simulator è una serie di videogiochi di simulazione di agricoltura e allevamento. Le mappe di gioco sono basate su ambienti americani ed europei. I giocatori sono in grado di coltivare, allevare bestiame e raccogliere il frutto del tempo speso in gioco.', '000038.json', 'A'),
('Bad Street Brawler', 300.00, 0, 'C64', 'Beam Software', 'Il giocatore interpreta il personaggio Duke Davis (il retro della scatola lo chiama Duke Dunnegan), che va da un palco all''altro picchiando i gangster che si mettono sulla sua strada, vestito con una canotta gialla, occhiali da sole e pantaloni gialli. È descritto come un ex punk rocker e l''artista marziale "più cool del mondo".', '000039.json', 'C'),

--GameBoy
('Pokemon Blu', 1350.00, 0, 'GameBoy', 'Game Freak', 'Nei videogiochi il giocatore controlla gli spostamenti del personaggio principale attraverso la regione fittizia di Kanto, alla cattura di creature chiamate Pokémon da allenare e utilizzare nelle sfide contro altri allenatori. Lo scopo del gioco è diventare il campione della Lega Pokémon, sconfiggendo gli otto capipalestra e i quattro allenatori più forti della regione: i Superquattro. Un altro obiettivo è quello di completare il Pokédex, l''enciclopedia fittizia presente all''interno dei giochi.', '000040.json', 'A'),
('Pokemon Rosso Fuoco', 500.00, 0, 'GameBoy', 'Game Freak', 'Il protagonista di Rosso Fuoco e Verde Foglia è un ragazzo che vive in una piccola città chiamata Biancavilla. Dopo che il giocatore si avventura da solo nell''erba alta, una voce lo avverte di fermarsi. Il Professor Oak, un famoso ricercatore di Pokémon, spiega al giocatore che tale erba è l''habitat dei Pokémon selvatici e incontrarli da soli può essere molto pericoloso. Pertanto, porta il giocatore nel suo laboratorio dove questi incontra il nipote di Oak, un altro aspirante Allenatore di Pokémon. Al giocatore e al suo rivale viene chiesto di scegliere un Pokémon per i loro viaggi.', '000041.json', 'A'),
('Pokemon Super Pak Rubino ', 2500.00, 0, 'GameBoy', 'Game Freak', 'Come per gli altri titoli della serie, anche queste due versioni trattano dell''avventura di un giovane (maschio o femmina a seconda della scelta del giocatore) che vuole diventare allenatore di Pokémon. La storia inizia nella regione di Hoenn con il protagonista, figlio (o figlia) del capopalestra di Petalipoli Norman, che si trasferisce con la sua famiglia nella cittadina di Albanova. Qui incontra il professor Birch e lo aiuta a fuggire da un Poochyena selvatico.', '000042.json', 'A'),
('Super Mario Land', 200.00, 0, 'GameBoy', 'Nintendo', 'La storia di Super Mario Land è ambientata nella pacifica regione di Sarasaland, che è suddivisa nei quattro regni di Birabuto, Muda, Easton e Chai. Un giorno, nel regno appare un alieno misterioso di nome Tatanga, che ipnotizza gli abitanti di Sarasaland, inclusa la Principessa Daisy, poi rapita dall''invasore per costringerla a sposarlo. Mario corre a salvarla, viaggiando attraverso le quattro aree geografiche di Sarasaland e sconfiggendo i sottoposti di Tatanga.', '000043.json', 'A'),

--GameCube
('Kirby Air Ride', 90, 0, 'GameCube', 'HAL Laboratory', 'Kirby Air Ride è un gioco spin-off di corse in 3D supporta fino a quattro giocatori in multiplayer. I "Veicoli Air Ride" possono anche volare per un determinato periodo grazie all''utilizzo di apposite rampe presenti nel percorso di gara. Il volo viene a sua volta controllato tramite il Control Stick.', '000044.json', 'A'),
('Medal Of Honor Frontline', 20.00, 0, 'GameCube', 'EA Games', 'Dopo essere stato recuperato dalla corazzata USA Thomas Jefferson, il tenente Patterson viene assegnato ad una compagnia della 1ª divisione di fanteria americana al comando del capitano Black e parteciperà allo sbarco a Omaha Beach. Durante l''avvicinamento alla spiaggia Patterson viene sbalzato in acqua perché il suo mezzo da sbarco viene colpito dall''artiglieria nemica ma riesce a portarsi in salvo dietro la carcassa di un mezzo semidistrutto sulla spiaggia.', '000045.json', 'A'),
('Metroid Prime', 45.90, 0, 'GameCube', 'Retro Studios', 'Tre anni dopo gli eventi del primo capitolo, durante un viaggio sulla sua navetta spaziale, l''ormai popolare cacciatrice di taglie Samus Aran riceve un SOS da parte di un vascello dei Pirati Spaziali, una razza di mercenari alieni con cui aveva più volte avuto a che fare.', '000046.json', 'A'),
('Rayman 3 Hoodlum Havoc', 29.90, 0, 'GameCube', 'Ubisoft Shanghai', 'André, divenuto per cause sconosciute un Lum maligno, acquisisce il potere di trasformare a sua volta altri Lum in Lum neri, che grazie alle arti magice si trasformano in Hoodlum, esseri ricoperti da cappucci. Murfy, testimone di quanto accaduto, avverte Rayman e Globox.', '000047.json', 'A'),
('The Legend of Zelda The Windwaker Edizione Limitata', 120.00, 0, 'GameCube', 'Nintendo EAD', 'Link è un ragazzo di undici anni che vive nella pacifica Isola Primula. Il giorno del suo compleanno sua sorella Aril viene rapita da un enorme volatile (Re Elmaroc) al posto della piratessa Dazel, la quale porta l''eroe alla Fortezza dei Demoni per tentare di salvarla.', '000048.json', 'A'),

--SegaMegaDrive
('California Games', 15.50, 0, 'SegaMegaDrive', 'Epyx', 'Il gioco consiste in sei discipline sportive, che si possono affrontare al completo, o selezionarne solo alcune, o fare allenamento senza punteggio su una. Prima di gareggiare il giocatore sceglie uno sponsor tra diversi marchi del mondo reale, a scopo soltanto decorativo. È disponibile anche la modalità multigiocatore, ma nessuna disciplina è in simultanea.', '000049.json', 'C'),
('Champions World Class Soccer', 20.50, 0, 'SegaMegaDrive', 'Park Place Productions', 'Le modalità di gioco incluse nel gioco sono Exhibition Match (un giocatore o due giocatori) e la modalità Torneo. I progressi attraverso il torneo possono essere salvati tramite una password fornita alla fine di ogni partita. Ci sono anche opzioni per attivare o disattivare falli e fuorigioco, oltre a selezionare la quantità di tempo consentita per la partita da giocare.', '000050.json', 'A'),
('Rocket Knight Adventures', 200.50, 0, 'SegaMegaDrive', 'Konami', 'Le leggende del gioco raccontano di come il primo sovrano, di nome El Zebulos, sconfisse un''orda di invasori provenienti dalla nave Pig Star allontanandoli grazie ad uno speciale sigillo. La chiave di tale sigillo fu protetta per generazioni da un ordine di cavalieri chiamati i Rocket Knight, dotati di armi robotiche e speciali forme di combattimento. Tra questi, si affermo'' Sparkster, orfano di guerra adottato dal leader dei cavalieri Mifune Sanjulo e cresciuto assieme all''ordine.', '000051.json', 'C'),
('Sonic The Hedgehog', 38.80, 0, 'SegaMegaDrive', 'Sonic Team', 'In modo da riuscire a rubare i sei Smeraldi del Caos (chiamati nell''edizione italiana Smeraldi Caotici) e sfruttare il loro potere, l''antagonista Dottor Ivo Robotnik (Dottor Eggman nella versione originale giapponese) ha intrappolato gli animali che abitano a South Island all''interno di robot aggressivi chiamati Badnik e in capsule metalliche. Il giocatore controlla Sonic, che mira a fermare i piani del malvagio scienziato liberando i suoi amici animali e raccogliendo gli smeraldi da solo.', '000052.json', 'A'),
('Street Fighter II', 49.90, 0, 'SegaMegaDrive', 'Capcom', 'Gli eventi del gioco si svolgono nove anni dopo il primo Street Fighter e sette anni dopo la serie Alpha. Dopo aver consolidato il proprio potere all''interno della Shadaloo, il temibile generale M. Bison indice un torneo di arti marziali a livello mondiale: il World Warrior Tournament II, o Street Fighter Tournament II. Il suo scopo è quello di individuare e sconfiggere il lottatore più forte per poi possederne il corpo grazie allo Psycho Power e diventare così un "essere perfetto".', '000053.json', 'A'),
('The Lion King', 29.99, 0, 'SegaMegaDrive', 'Virgin Interactive Entertainment', 'Ne Il re leone, il giocatore controlla per l''appunto Simba attraverso livelli che ricalcano gli eventi della trama originale, vedendolo affrontare ostacoli ed animali della giungla (in special modo le iene) allo scopo di raggiungere la fine. Il numero dei livelli varia a seconda della versione sulla quale si sta giocando e due di essi sono adattamenti di scene che furono scartate durante la lavorazione del film.', '000054.json', 'B'),

--Atari2600
('Atari 2600 32 in 1 Boxed', 75.00, 0, 'Atari2600', 'Atari', 'Collezione di 32 tra i migliori giochi per Atari.', '000055.json', 'D'),
('MS. PAC-MAN', 19.99, 0, 'Atari2600', 'Midway Games', 'I quattro soliti fantasmi (antagonisti del precedente capitolo) sono tornati e vogliono vendicarsi. Pac-Man è stato imprigionato e dovrà essere salvato da Ms. Pac-Man la moglie di Pac-Man, nonché la madre di suo figlio. ', '000056.json', 'C'),
('Pole Position', 25.00, 0, 'Atari2600', 'Namco', 'Pole Position è un videogioco arcade di guida automobilistica. La visuale è in prospettiva tridimensionale, da un punto rialzato alle spalle della propria vettura, ma piuttosto vicino alla stessa. La guida avviene con volante, acceleratore, freno e due marce. È possibile anche il controsterzo.', '000057.json', 'A'),
('Space Invaders', 35.00, 0, 'Atari2600', 'Namco', 'Il giocatore controlla un cannone mobile che si muove orizzontalmente sul fondo dello schermo, e deve abbattere uno a uno gli alieni che piano piano si avvicinano alla Terra. Le tappe di avvicinamento degli alieni al Mondo seguono uno schema univoco, un ampio e ordinato zig-zag che li porta lentamente ma inesorabilmente a raggiungere il fondo dello schermo decretando l''avvenuta invasione e la conseguente fine della partita.', '000058.json', 'C');


-- Credentials
insert into retrogamer.credentials(pass_hash, pass_salt. creation_date) values
('timidone', 'dinosauri','2024-05-12');

-- Users
insert into retrogamer.users(id_client,firstname, lastname, telephone, email, birth, address, city, prov, cap, id_cred) values
(2,'Domenico','Amorelli','+3932010234455','timidone@so.org','2004-05-15','Via Girolamo Savonarola','Sapri','SA','84073',1);
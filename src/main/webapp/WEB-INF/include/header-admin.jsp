<div class="upper_bar">
    <input type="checkbox" id="nav" name="nav" />
    <span class="menu_icon">
                <label for="nav">
                    <img src="img/icons/menu.svg" alt="Menu Button" class="general_icon" />
                    <img src="img/icons/close.svg" alt="Menu Close" class="general_icon close_icon" />
                </label>
            </span>

    <div class="upper-logo-name">
        <a href="#"><img class="logo" src="img/logo.png" alt="logo"/></a>
        <a class="upper-name" href="#">RETROGAMER</a>
    </div>

    <div class="overlay" onclick="overlay_menu()"></div>

    <ul class="nav">
        <li class="this-page"><a href="#">Home</a></li>
        <li><a href="#">Gestione Catalogo</a></li>
        <li><a href="#add_prod">Aggiungi Prodotto</a></li>
        <li><a href="#">Gestione Ordini</a></li>
        <li><a href="#">Gestione Utenti</a></li>
    </ul>
    <div class="upper-bar-right">
        <button class="default" name="action" value="Admin">${user}</button>
        <button class="default alternative margin-h-10" name="action" onclick="location.href='${pageContext.request.contextPath}/exit_admin;'" value="Exit">Exit</button>
    </div>
</div>
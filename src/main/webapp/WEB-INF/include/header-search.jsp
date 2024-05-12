<div class="upper_bar">
    <input type="checkbox" id="nav" name="nav" />
    <span class="menu_icon"><label for="nav"><img src="img/icons/menu.svg" alt="Menu Button" class="general_icon" /><img src="img/icons/close.svg" alt="Menu Close" class="general_icon close_icon" /></label></span>

    <div class="upper-logo-name">
        <a href="${pageContext.request.contextPath}/"><img class="logo" src="img/logo.png" alt="logo"/></a>
        <a class="upper-name" href="${pageContext.request.contextPath}/">RETROGAMER</a>
    </div>

    <div class="overlay" onclick="overlay_menu()"></div>

    <ul class="nav">
        <li class=""><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="#">Catalogo</a></li>
        <li><a href="#">Chi Siamo</a></li>
        <li><a href="#">Assistenza</a></li>
        <li><a href="#">Newsletter</a></li>
    </ul>

    <%@include file="/WEB-INF/include/search-bar.jsp"%>

    <div class="upper-bar-right">
        <span><img src="img/icons/shopping_cart.svg" alt="Carrello" class="general_icon"></span>
        <span><img src="img/icons/account_circle.svg" alt="Accedi o Registrati" class="general_icon"></span>
    </div>
</div>

<div class="upper_bar">
    <input type="checkbox" id="nav" name="nav" />
    <span class="menu_icon"><label for="nav"><img src="${pageContext.request.contextPath}/img/icons/menu.svg" alt="Menu Button" class="general_icon" /><img src="${pageContext.request.contextPath}/img/icons/close.svg" alt="Menu Close" class="general_icon close_icon" /></label></span>

    <div class="upper-logo-name">
        <a href="${pageContext.request.contextPath}/"><img class="logo" src="${pageContext.request.contextPath}/img/logo.png" alt="logo"/></a>
        <a class="upper-name" href="${pageContext.request.contextPath}/">RETROGAMER</a>
    </div>

    <div class="overlay" onclick="overlay_menu()"></div>

    <ul class="nav">
        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/store">Catalogo</a></li>
        <li><a href="${pageContext.request.contextPath}/about-us">Chi Siamo</a></li>
        <li><a href="${pageContext.request.contextPath}/assistance">Assistenza</a></li>
        <li><a href="${pageContext.request.contextPath}/newsletter">Newsletter</a></li>
    </ul>

    <%@include file="/WEB-INF/include/search-bar.jsp"%>

    <div class="upper-bar-right">
        <span><a href="cart">
            <img src="${pageContext.request.contextPath}/img/icons/shopping_cart.svg" alt="Carrello" class="general_icon">
            <c:choose>
                <c:when test="${empty cart or cart.cartItems.size() == 0}">
                    <span id="cart-counter" class="general-display-none">0</span>
                </c:when>
                <c:otherwise>
                    <span id="cart-counter" class="">${cart.cartItems.size()}</span>
                </c:otherwise>
            </c:choose>
            </a>
        </span>
        <c:choose>
            <c:when test="${not empty userlogged}">
                <figure aria-label="user-icon-menu" id="userMenu">
                    <img src="${pageContext.request.contextPath}/img/user/Konqi.png" alt="Area Utente" tabindex="0" role="button" title="Il mio account" class="userLoggedIcon">
                    <div class="general-display-none" id="user-menu-div" tabindex="0" role="menu">
                        <h4>Ciao ${userlogged.firstname}!</h4>
                        <nav>
                            <span role="menuitem"><a href="${pageContext.request.contextPath}/user-profile">Il mio Profilo</a></span>
                            <hr />
                            <span role="menuitem"><a href="${pageContext.request.contextPath}/user-profile">I miei Ordini</a></span>
                            <hr />
                            <span class="logout" role="menuitem"><a href="${pageContext.request.contextPath}/exit-user">Logout</a></span>
                        </nav>
                    </div>
                </figure>
                <script src = "${pageContext.request.contextPath}/js/user-menu.js" rel="text/javascript"></script>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/user-login"><span><img src="${pageContext.request.contextPath}/img/icons/account_circle.svg" alt="Accedi o Registrati" class="general_icon"></span></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

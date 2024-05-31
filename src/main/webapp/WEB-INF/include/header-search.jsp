<div class="upper_bar">
    <input type="checkbox" id="nav" name="nav" />
    <span class="menu_icon"><label for="nav"><img src="${pageContext.request.contextPath}/img/icons/menu.svg" alt="Menu Button" class="general_icon" /><img src="${pageContext.request.contextPath}/img/icons/close.svg" alt="Menu Close" class="general_icon close_icon" /></label></span>

    <div class="upper-logo-name">
        <a href="${pageContext.request.contextPath}/"><img class="logo" src="${pageContext.request.contextPath}/img/logo.png" alt="logo"/></a>
        <a class="upper-name" href="${pageContext.request.contextPath}/">RETROGAMER</a>
    </div>

    <div class="overlay" onclick="overlay_menu()"></div>

    <ul class="nav">
        <li class=""><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/store">Catalogo</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/aboutus.jsp">Chi Siamo</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/assistance.jsp">Assistenza</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/newsletter.jsp">Newsletter</a></li>
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
                <a href="${pageContext.request.contextPath}/user-profile"><span><img src="${pageContext.request.contextPath}/img/icons/account_circle.svg" alt="Area Utente" class="general_icon"></span></a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/jsp/login.jsp"><span><img src="${pageContext.request.contextPath}/img/icons/account_circle.svg" alt="Accedi o Registrati" class="general_icon"></span></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

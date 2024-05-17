<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Retrogamer: Home</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <script src="${pageContext.request.contextPath}/js/filter-bar.js" defer></script>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<aside class="filter-bar">

    <div class="hide-filter-mobile"><span id="hide-filter">Hide</span></div>
    <span class="centerized-flex-container">Filtri(1)</span>
    <div class="drop-down-filter">
        <div class="filter-title">
            <h4>Piattaforma</h4>
            <span class="filter-arrow">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
                    </span>
        </div>
        <hr/>
        <div class="filter-list platform-filter">
            <label><input type="radio" name="platform" value="ps1"/>PS1</label>
            <label><input type="radio" name="platform" value="ps2"/>PS2</label>
            <label><input type="radio" name="platform" value="sega"/>Sega</label>
            <label><input type="radio" name="platform" value="atari"/>Atari</label>
            <label><input type="radio" name="platform" value="ps1"/>PS1</label>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title">
            <h4>Categoria</h4>
            <span class="filter-arrow">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
                    </span>
        </div>
        <hr/>
        <div class="filter-list category-filter">
            <label><input type="checkbox" name="cat" value="ps1"/>PS1</label>
            <label><input type="checkbox" name="cat" value="ps2"/>PS2</label>
            <label><input type="checkbox" name="cat" value="sega"/>Sega</label>
            <label><input type="checkbox" name="cat" value="atari"/>Atari</label>
            <label><input type="checkbox" name="cat" value="ps1"/>PS1</label>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title">
            <h4>Developer</h4>
            <span class="filter-arrow">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
                    </span>
        </div>
        <hr/>
        <div class="filter-list dev-filter">
            <label><input type="radio" name="developer" value="ps1"/>PS1</label>
            <label><input type="radio" name="developer" value="ps2"/>PS2</label>
            <label><input type="radio" name="developer" value="sega"/>Sega</label>
            <label><input type="radio" name="developer" value="atari"/>Atari</label>
            <label><input type="radio" name="developer" value="ps1"/>PS1</label>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title">
            <h4>Prezzo</h4>
            <span class="filter-arrow">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
                    </span>
        </div>
        <hr/>
        <div class="filter-list price-filter">
            <label>Prezzo Minimo:<span class="price-value"></span><input class="price-range" type="range" name="min-price" value="0" min="0" max="3000" /></label>
            <label>Prezzo Massimo:<span class="price-value"></span><input class="price-range" type="range" name="max-price" value="3000" min="0" max="3000" /></label>
        </div>
    </div>
    <div class="filter-buttons">
        <button class="default free-size" id="apply-filter" disabled>Applica</button>
        <button class="free-size" id="remove-filter">&Cross;</button>
    </div>

</aside>

<div class="upper_body">
    <div class="game-name">
        <h1>Il nostro Catalogo</h1>
    </div>
    
    <%@include file="/WEB-INF/include/search-bar.jsp"%>
    
</div>

<div class="main_home">
    <div><button class="default alternative" id="filter-mobile">Filtra</button></div>
    <c:forEach var="prod" items="${products}">
        <div class="tile">
            <div class=" tile-img">
                <a href="${pageContext.request.contextPath}/desc?id_prod=${prod.id_prod}"><img alt="front cover image" src="${pageContext.request.contextPath}/${prod.metaData.path}${prod.metaData.front}"></a>
            </div>
            <div class="tile-text">
                <h3 class="tile-title">${prod.name}</h3>
                <span class="tile-desc">${prod.description}</span>
                <div class="tile-text-bottom">
                    <div class="tile-text-bottom-price">
                        <c:choose>
                            <c:when test="${empty prod.discount or prod.discount == 0}">
                                            <span class="actual-price">
                                                    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod.price}"/>&euro;
                                            </span>
                            </c:when>
                            <c:otherwise>
                                <span class="discount">${prod.discount}</span>
                                <span class="original-rem-price">${prod.price}</span>
                                <span class="actual-price">
                                            <c:out value="${prod.price - prod.price*prod.discount/100}"/>
                                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>
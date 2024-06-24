<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Catalogo - Retrogamer</title>
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
    <div class="space-around-row-flex-container">
        <span id="filter-number">Filtri(0)</span>
        <span id="reset-filter">Reset</span>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title" tabindex="0" role="button">
            <h4>Piattaforma</h4>
            <span class="filter-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12" aria-label="Freccia"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
            </span>
        </div>
        <hr/>
        <div class="filter-list platform-filter">
            <label><input type="radio" name="platform" value="ps1"/>PS1</label>
            <label><input type="radio" name="platform" value="n64"/>N64</label>
            <label><input type="radio" name="platform" value="ps2"/>PS2</label>
            <label><input type="radio" name="platform" value="pc"/>PC</label>
            <label><input type="radio" name="platform" value="atari2600"/>Atari 2600</label>
            <label><input type="radio" name="platform" value="c64"/>C64</label>
            <label><input type="radio" name="platform" value="gameboy"/>Gameboy</label>
            <label><input type="radio" name="platform" value="gamecube"/>GameCube</label>
            <label><input type="radio" name="platform" value="segamegadrive"/>Sega MD</label>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title" tabindex="0" role="button">
            <h4>Categoria</h4>
            <span class="filter-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12" aria-label="Freccia"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
            </span>
        </div>
        <hr/>
        <div class="filter-list category-filter">
            <c:forEach var="cat" items="${category}">
                <label><input type="checkbox" name="cat" value="${cat.id_cat}"/>${cat.typename}</label>
            </c:forEach>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title" tabindex="0" role="button">
            <h4>Developer</h4>
            <span class="filter-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12" aria-label="Freccia"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
            </span>
        </div>
        <hr/>
        <div class="filter-list dev-filter">
            <c:forEach var="dev" items="${developers}">
                <label><input type="radio" name="developer" value="${dev}"/>${dev}</label>
            </c:forEach>
        </div>
    </div>
    <div class="drop-down-filter">
        <div class="filter-title" tabindex="0" role="button">
            <h4>Prezzo</h4>
            <span class="filter-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7.7 12" aria-label="Freccia"><path fill="currentColor" d="M1.7 0L0 1.7 4.3 6 0 10.3 1.7 12l6-6z"></path></svg>
            </span>
        </div>
        <hr/>
        <div class="filter-list price-filter">
            <label>Prezzo Minimo:<span class="price-value"></span><input class="price-range" type="range" name="min-price" value="0" min="0" max="${maxPrice}" /></label>
            <label>Prezzo Massimo:<span class="price-value"></span><input class="price-range" type="range" name="max-price" value=${maxPrice} min="0" max=${maxPrice} /></label>
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
<div class="container">
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
                                    <span class="discount"><fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="1" value="${prod.discount}"/>&percnt;</span>
                                    <span class="original-rem-price">${prod.price}&euro;</span>
                                    <span class="actual-price">
                                            <c:out value="${prod.price - prod.price*prod.discount/100}"/>&euro;
                                                </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <c:if test="${not empty page}">
        <div class="page-number">
            <c:if test="${page > 1}">
                <a id="prev-page" href="${pageContext.request.contextPath}/store?page=${page - 1}">Prev</a>
            </c:if>
            <a id="this-page" href="${pageContext.request.contextPath}/store?page=${page}"><button class="default alternative">${page}</button></a>
            <c:if test="${page < maxPage}">
                <a id="next-page" href="${pageContext.request.contextPath}/store?page=${page + 1}">Next</a>
            </c:if>
        </div>
    </c:if>
</div>

<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>
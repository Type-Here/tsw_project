<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Retrogamer: Home</title>
        <link type="text/css" rel="stylesheet" href="css/style.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="css/medium-size.css" />
        <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="css/widescreen.css" />
        <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="css/small.css" />
        <link rel="icon" type="image/x-icon" href="img/favicon.ico">
        <script src="js/carousel.js"></script>
        <script src="js/overlay.js"></script>
    </head>
    <body class="body_def" onload="prepare_function()">
        
        <%@include file="/WEB-INF/include/header-standard.jsp"%>

        <div class="upper_body">

            <div class="carousel">
                <ul>
                    <li class="carousel-slide active-slide">
                        <img src="img/placeholder.svg">
                    </li>
                    <li class="carousel-slide">
                        <img src="img/placeholder2.svg">
                    </li>
                    <li class="carousel-slide">
                        <img src="img/placeholder.svg">
                    </li>
                    <li class="carousel-slide">
                        <img src="img/placeholder2.svg">
                    </li>
                    <li class="carousel-slide">
                        <img src="img/placeholder.svg">
                    </li>
                </ul>

                <div class="carousel-radio">
                    <input type="radio" name="slides" id="carousel-slide-1" checked onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-2" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-3" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-4" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-5" onclick="carousel_changeimg(this)" />
                </div>

                <ul class="carousel-thumbnails">
                    <li>
                        <label for="carousel-slide-1"><img src="img/placeholder.svg" alt="" class="active-thumbnail"></label>
                    </li>
                    <li>
                        <label for="carousel-slide-2"><img src="img/placeholder2.svg" alt=""></label>
                    </li>
                    <li>
                        <label for="carousel-slide-3"><img src="img/placeholder.svg" alt=""></label>
                    </li>
                    <li>
                        <label for="carousel-slide-4"><img src="img/placeholder2.svg" alt=""></label>
                    </li>
                    <li>
                        <label for="carousel-slide-5"><img src="img/placeholder.svg" alt=""></label>
                    </li>
                </ul>
            </div>
            
            <div class="search_container">
                <form action="#" method="get" class="search-bar">
                    <span><img src="img/icons/search.svg" alt="Search"/></span>
                    <input type="text" name="search" placeholder="Cerca un prodotto" alt="Cerca" />
                </form>
            </div>
        </div>

        <div class="main_home">

            <c:forEach var="prod" items="${products}">
                <div class="tile">
                    <div class=" tile-img">
                        <a href="${prod.id_prod}"><img src="${prod.metaData.path}${prod.metaData.front}"></a>
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
                                            <c:out value="${prod.price - prod.price*prod.discount}"/>
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
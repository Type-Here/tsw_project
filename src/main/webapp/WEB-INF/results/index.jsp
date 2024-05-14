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
                    <c:set var="index" value="1" />
                    <c:forEach var="high" items="${highlights}">
                        <li class="carousel-slide <c:if test="${index == 1}">active-slide</c:if>">
                            <a href="${pageContext.request.contextPath}/desc?id_prod=${high.id_prod}">
                                <img src="${high.metaData.path}${high.metaData.front}" alt="Slide front Image of ${high.name}">
                            </a>
                            <c:set var="index" value="0"/>
                            <div class="carousel-slide-text">
                                <h3>${high.name}</h3>
                            </div>
                        </li>
                    </c:forEach>
                </ul>

                <div class="carousel-radio">
                    <input type="radio" name="slides" id="carousel-slide-1" checked onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-2" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-3" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-4" onclick="carousel_changeimg(this)" />
                    <input type="radio" name="slides" id="carousel-slide-5" onclick="carousel_changeimg(this)" />
                </div>

                <ul class="carousel-thumbnails">
                    <c:set var="index" value="1" />
                    <c:forEach items="${highlights}" var="high">
                        <li>
                            <label for="carousel-slide-<c:out value='${index}'/>"><img src="${high.metaData.path}${high.metaData.front}" alt="Thumbnail front image of${high.name}"  <c:if test="${index == 1}">class="active-thumbnail"</c:if>></label>
                            <c:set var="index" value="${index  + 1}"/>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            
            <%@include file="/WEB-INF/include/search-bar.jsp"%>
        </div>

        <div class="main_home">

            <c:forEach var="prod" items="${products}">
                <div class="tile">
                    <div class=" tile-img">
                        <a href="${pageContext.request.contextPath}/desc?id_prod=${prod.id_prod}"><img alt="front cover image" src="${prod.metaData.path}${prod.metaData.front}"></a>
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
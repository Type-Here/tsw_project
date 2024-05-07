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
        
        <div class="upper_bar">
            <input type="checkbox" id="nav" name="nav" />
            <span class="menu_icon"><label for="nav"><img src="img/icons/menu.svg" alt="Menu Button" class="general_icon" /><img src="img/icons/close.svg" alt="Menu Close" class="general_icon close_icon" /></label></span>
            
            <div class="upper-logo-name">
                <a href="#"><img class="logo" src="img/logo.png" alt="logo"/></a>
                <a class="upper-name" href="#">RETROGAMER</a>
            </div>
            
            <div class="overlay" onclick="overlay_menu()"></div>

            <ul class="nav">
                <li class="this-page"><a href="#">Home</a></li>
                <li><a href="#">Catalogo</a></li>
                <li><a href="#">Chi Siamo</a></li>
                <li><a href="#">Assistenza</a></li>
                <li><a href="#">Newsletter</a></li>
            </ul>
            <div class="upper-bar-right">
                <span><img src="img/icons/shopping_cart.svg" alt="Carrello" class="general_icon"></span>
                <span><img src="img/icons/account_circle.svg" alt="Accedi o Registrati" class="general_icon"></span>
            </div>
        </div>

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
                        <a href="${prod.metaData.link}"><img src="${prod.metaData.images[0]}"></a>
                    </div>
                    <div class="tile-text">
                        <h3 class="tile-title">${prod.name}</h3>
                        <span class="tile-desc">${prod.metaData.description}</span>
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

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">Title</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id tortor non lorem accumsan tincidunt. Morbi auctor felis non arcu aliquam ultricies id ut tortor cwemfioqwp ferjogm ermo vgmwj weèrgwm woeprmgjioò erwopmgjns eiwfn  efcwgwetrwh eewvgr w ertvgrth fine testo</span>
                    <div class="tile-text-bottom"><span class="discount">50%</span><div class="tile-text-bottom-price"><span class="original-rem-price">€100.00</span><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder2.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">Title</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id tortor non lorem accumsan tincidunt. Morbi auctor felis non arcu aliquam ultricies id ut tortor</span>
                    <div class="tile-text-bottom"><span class="discount">50%</span><div class="tile-text-bottom-price"><span class="original-rem-price">€100.00</span><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">Title</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id tortor non lorem accumsan tincidunt. Morbi auctor felis non arcu aliquam ultricies id ut tortor</span>
                    <div class="tile-text-bottom"><span class="discount">50%</span><div class="tile-text-bottom-price"><span class="original-rem-price">€100.00</span><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder2.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">Title</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit.</span>
                    <div class="tile-text-bottom"><div class="tile-text-bottom-price"><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">This is a very long Title, to check for ellipsis overflox rule</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id tortor non lorem accumsan tincidunt. Morbi auctor felis non arcu aliquam ultricies id ut tortor</span>
                    <div class="tile-text-bottom"><div class="tile-text-bottom-price"><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>

            <div class="tile">
                <div class=" tile-img">
                    <a href="#"><img src="img/placeholder2.svg"></a>
                </div>
                <div class="tile-text">
                    <h3 class="tile-title">Title</h3>
                    <span class="tile-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id tortor non lorem accumsan tincidunt. Morbi auctor felis non arcu aliquam ultricies id ut tortor</span>
                    <div class="tile-text-bottom"><div class="tile-text-bottom-price"><span class="actual-price">€50.00</span></div></div>
                </div>
            </div>
        </div>

        <div class="footer">

            <div class="ft-social">
                <img src="img/icons/facebook.svg" alt="Facebook Share" class="general_icon" />
                <img src="img/icons/share.svg" alt="Share Button" class="general_icon" />
            </div>
            <div class="ft-links">
                <a href="#">Chi Siamo</a>
                <a href="#">Contatti</a>
            </div>
            <div class="ft-copy">
                <p>&#169; 2024, RetroGamer. Tutti i diritti riservati.</p>
            </div>
            <div class="ft-terms">
                <a href="#">Termini di Servizio</a>
                <a href="#">Gestione Cookies</a>
                <a href="#">Termini di Privacy</a>
            </div>

        </div>
    </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="newsletter-container">

    <div class="newsletter-header">
        <hr>
        <h1>Nostalgia e brividi Autunnali:</h1>
        <h3>Autunno da brivido: scopri <span style="color: rgb(95, 75, 139); font-weight: bold;"><em>paurosi</em></span> giochi retrò!</h3>
        <hr>
    </div>
    <div class="newsletter-boy">

        <c:forEach var="prod" items="${newsletter}">
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

    <div class="newsletter-footer">
        <hr>
        <h3>Riscalda le tue giornate autunnali con i classici di sempre!<br>
            Sconto del <strong>15%</strong> su tutti i giochi retrò con il codice.</h3>
        <h1 style=" color: #0a78f5">AUTUNNORETRO15</h1>
        <hr>
    </div>
</div>
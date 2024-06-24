<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <div class="page-number">
        <c:if test="${page > 1}">
            <a id="prev-page" href="${pageContext.request.contextPath}/store?page=${page - 1}">Prev</a>
        </c:if>
        <a id="this-page" href="${pageContext.request.contextPath}/store?page=${page}"><button class="default alternative">${page}</button></a>
        <c:if test="${page < maxPage}">
            <a id="next-page" href="${pageContext.request.contextPath}/store?page=${page + 1}">Next</a>
        </c:if>
    </div>
</div>
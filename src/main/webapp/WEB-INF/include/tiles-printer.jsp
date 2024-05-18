<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
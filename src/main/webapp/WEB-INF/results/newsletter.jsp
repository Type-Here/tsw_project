<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/newsletter.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 700px)" href="${pageContext.request.contextPath}/css/medium-size.css" />
    <link type="text/css" rel="stylesheet" media="screen and (min-width: 700px)" href="${pageContext.request.contextPath}/css/widescreen.css" />
    <link type="text/css" rel="stylesheet" media="screen and (max-width: 450px)" href="${pageContext.request.contextPath}/css/small.css" />
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    <script src="${pageContext.request.contextPath}/js/overlay.js"></script>
    <title>Retrogamer: Newsletter</title>
</head>
<body class="body_def">

<%@include file="/WEB-INF/include/header-standard.jsp"%>

<div class="main_home">

    <c:choose>
        <c:when test="${season == 'spring'}">
            <%@include file="/WEB-INF/include/newsletter-spring.jsp" %>
        </c:when>
        <c:when test="${season == 'summer'}">
            <%@include file="/WEB-INF/include/newsletter-summer.jsp" %>
        </c:when>
        <c:when test="${season == 'autumn'}">
            <%@include file="/WEB-INF/include/newsletter-autumn.jsp" %>
        </c:when>
        <c:when test="${season == 'winter'}">
            <%@include file="/WEB-INF/include/newsletter-winter.jsp" %>
        </c:when>
        <c:otherwise>
            <%@include file="/WEB-INF/include/newsletter-normal.jsp" %>
        </c:otherwise>
    </c:choose>

</div>

<%@include file="/WEB-INF/include/footer.jsp"%>

</body>
</html>
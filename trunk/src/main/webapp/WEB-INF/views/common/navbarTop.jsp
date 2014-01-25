<%--
User menu on navbar
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
</head>
<body>
<header id="cdoj-navbar-top">
  <div class="container">
    <nav>
      <ul class="nav navbar-nav navbar-right">
        <!-- Search -->
        <li>
          <div class="input-group input-group-sm" id="cdoj-search">
            <input type="text" class="form-control">

            <div class="input-group-btn">
              <button type="button" class="btn btn-default"
                      tabindex="-1">
                <span id="cdoj-search-icon"><i class="fa fa-search"></i></span>
                <span id="cdoj-search-text">Search</span>
              </button>
            </div>
          </div>
        </li>
        <!-- User -->
        <c:choose>
          <c:when test="${sessionScope.currentUser == null}">
            <li ng-controller="UserController"
                ng-init="$root.hasLogin=false;">
            </li>
          </c:when>
          <c:otherwise>
            <li ng-controller="UserController"
                ng-init="$root.hasLogin=true;
                $root.currentUser={
                  userName: '${sessionScope.currentUser.userName}',
                  type: ${sessionScope.currentUser.type},
                  email: '${sessionScope.currentUser.email}'
                };">
            </li>
          </c:otherwise>
        </c:choose>
      </ul>
    </nav>
  </div>
</header>
</body>
</html>

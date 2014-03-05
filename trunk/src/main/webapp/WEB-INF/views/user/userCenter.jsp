<%--
 User center page
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><c:out value="${targetUserName}"/></title>
</head>
<body>
<div id="cdoj-user-center"
     ng-controller="UserCenterController"
     ng-init="targetUserName='${targetUserName}'">
  <div class="row">
    <div class="col-md-12">
      <div class="media" id="cdoj-user-center-summary">
        <a class="pull-left" href="#">
          <img id="cdoj-user-avatar-large"
               ui-avatar
               email="targetUser.email"
               class="media-object img-thumbnail"/>
        </a>

        <div class="media-body">
          <h2 class="media-heading" ng-bind="targetUser.userName">
          </h2>
          <dl class="dl-horizontal">
            <dt>Nick name</dt>
            <dd ng-bind="targetUser.nickName">
            </dd>
            <dt>School</dt>
            <dd ng-bind="targetUser.school">
            </dd>
            <dt>Department</dt>
            <dd ng-bind="targetUser.department">
            </dd>
              <dt>Student ID</dt>
              <dd ng-bind="targetUser.studentId">
              </dd>
              <dt>Email</dt>
              <dd ng-bind="targetUser.email">
              </dd>
            <dt>Motto</dt>
            <dd ng-bind="targetUser.motto">
            </dd>
            <dt>Last login</dt>
            <dd ui-time
                time="targetUser.lastLogin">
            </dd>
          </dl>
        </div>
      </div>
    </div>
    <div class="col-md-12">
      <h3>Problems</h3>
      <hr/>
        <div ng-repeat="status in problemStatus"
             class="cdoj-label cdoj-user-status-label"
             ng-class="{
               'label-default': status.status == 0,
               'label-success': status.status == 1,
               'label-danger': status.status == 2
             }"
            >
          <a href="/problem/show/{{status.problemId}}" ng-bind="status.problemId"></a>
        </div>
    </div>
  </div>
</div>
</body>
</html>

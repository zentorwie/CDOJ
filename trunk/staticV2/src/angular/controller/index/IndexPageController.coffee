angular.module("cdojV2").controller("IndexPageController", [
  "$rootScope"
  ($rootScope) ->
    $rootScope.$broadcast("pageChangeEvent", "Home")
])
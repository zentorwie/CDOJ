/**
 * Javascript for all page.
 */

var currentUser;

$(function () {
  var $currentUser = $('#currentUser');
  if ($currentUser.length !== 0) {
    currentUser = $currentUser[0].innerHTML;
    $('#userAvatar').setAvatar({
      image: 'http://www.acm.uestc.edu.cn/images/akari_small.jpg',
      size: 120
    });
  }

  $('#cdoj-login-button').setButton({
    callback: function() {
      var info=$('#cdoj-login-form').getFormData();

      jsonPost('/user/login', info, function(data) {
        $('#cdoj-login-form').formValidate({
          result: data,
          onSuccess: function() {
            window.location.reload();
          }
        });
      });
    }
  });
});

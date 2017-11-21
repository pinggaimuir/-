/**
 * Created by gao on 2016/9/16.
 */
// $("document")=function() {
$(document).ready(function () {
    $("#name").blur(function () {
            $.get("CheckNameServlet?name=" + $("#name").val, null, callback);
        }
    );
});
    function callback(data) {
        $("#tip").html(data);
    }

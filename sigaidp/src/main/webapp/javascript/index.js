/**
 * Created by hodrigohamalho@gmail.com on 24/07/14.
 */
$( document ).ready(function() {
    // SEND GET Request to SP
    $.ajax({
        url: "/sigawf/app/inbox",
        type: "GET"
    }).done(function(result) {
        var form = $(result)[1];
        var action = $(form).attr("action");
        var samlRequestName = $(form).find("input").attr("name");
        var samlRequestValue = $(form).find("input").attr("value");
        var samlJson = {};
        samlJson[samlRequestName] = samlRequestValue;

        // SEND POST request to IDP with SAMLRequest param
        $.ajax({
            url: action,
            type: "POST",
            data: samlJson
        }).done(function(result) {
            var form = $(result)[1];
            var action = $(form).attr("action");

            var samlResponseName = $(form).find("input").attr("name");
            var samlResponseValue = $(form).find("input").attr("value");
            var jsonData = {};
            jsonData[samlResponseName] = samlResponseValue;

            // SEND POST request to sales-post with SAMLResponse param
            $.ajax({
                url: action,
                type: "POST",
                data: jsonData
            }).done(function(result) {
                console.log($(result).html());
                debugger;
                //$("#another-sp-content").text(result).html();
            });
        });
    });
});
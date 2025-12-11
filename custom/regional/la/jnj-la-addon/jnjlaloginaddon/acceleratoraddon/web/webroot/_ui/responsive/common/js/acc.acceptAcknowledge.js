$(window).load(function(){

    $("#contactUsSendButton").addClass("disabled");

    $("#chkAcknowledge").change(function(){
        if ($(this).is(':checked')){
            $("#contactUsSendButton").removeClass("disabled");
        }
        else
            $("#contactUsSendButton").addClass("disabled");
    })

})


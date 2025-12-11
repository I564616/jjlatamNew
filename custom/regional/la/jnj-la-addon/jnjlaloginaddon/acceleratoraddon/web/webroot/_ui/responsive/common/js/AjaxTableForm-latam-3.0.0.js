function AjaxTableForm(_ajaxTables) {

    // main attributes
    var ajaxTables = _ajaxTables;

    // form attributes
    var searchBy;
    var searchText;
    var fromDate;
    var toDate;

    // initializes the form, doing first ajax call for each table and preparing download link
    this.init = function(){
        bindEvents();
        configureAjaxTables(this);
        updateLinksAndAjaxTables();
    }

    this.createUrlParamsUsingForm = function(){
        return createUrlParamsUsingForm(); // makes a private function exposed
    }

    function configureAjaxTables(ajaxTableForm){
        $(ajaxTables).each(function(i, ajaxTable){
            ajaxTable.setAjaxTableForm(ajaxTableForm);
        })
    }

    function updateLinksAndAjaxTables(){
        readFormValues();
        updateDownloadLinks();
        updateAjaxTables();
    }

    function updateAjaxTables(){
        $(ajaxTables).each(function(i, ajaxTable){
            ajaxTable.restart();
        })
    }

    function bindEvents(){
        $("#ajaxTableFormResetButton").off().on("click", function(event){
            event.preventDefault();
            onReset();
        });
        $("#ajaxTableFormSearchButton").off().on("click", function(event){
            event.preventDefault();
            onSearch();
        });
        $("#searchByInput").off().on("keydown", function(event){
            onSearchTextKeyDown(event);
        });
    }

    // executed every time the user types a char in searchText field
    function onSearchTextKeyDown(event){
        if (event.keyCode == 13) {
            onSearch();
        }
    }

    // read form values and run search again
    function onSearch(){
        updateLinksAndAjaxTables();
    }

    // restore default values of form
    function onReset(){
        resetSearchBySelector();
        $("#searchByInput").val("");
        $("#datePicker1").val("");
        $("#datePicker2").val("");
        updateLinksAndAjaxTables();
    }

    function resetSearchBySelector(){
        // get text of first option
        var defaultText = $("#searchby option:eq(0)").text().trim();

        // put this text in visible selector
        $("button[data-id=searchby] .filter-option").text(defaultText);

        // clean the hidden selector
        $("#searchby").val("");
    }

    function updateDownloadLinks(){
        updateDownloadLink("#excelDownload", "EXCEL");
        updateDownloadLink("#pdfDownload", "PDF");
    }

    function updateDownloadLink(selector, downloadType){
        var url = $("#baseUrl").data("value") + "/download?downloadType=" + downloadType;
        url += createUrlParamsUsingForm();
        $(selector).attr("href", url);
    }

    function createUrlParamsUsingForm(){
        var params = "";

        // we only add searchBy / searchText if both are valid values
        if (hasValue(searchBy) && hasValue(searchText)) {
            params += addParameter("searchBy", searchBy);
            params += addParameter("searchText", searchText);
        }
        params += addParameter("fromDate", fromDate); // expected format in server: mm/dd/yyyy
        params += addParameter("toDate", toDate); // expected format in server: mm/dd/yyyy

        return params;
    }

    function addParameter(key, value){
        // this condition avoids parameters with empty value
        if (!hasValue(value)) {
            return "";
        }

        return "&" + key + "=" + encodeURIComponent(value);
    }

    function hasValue(value){
        // 0 is a valid value. That's why we are comparing with !== instead of !=
        return value !== null && value !== "";
    }

    function readFormValues(){
        searchBy = $("#searchby").val();
        if (searchBy === "") {
            searchBy = null;
        }
        searchText = $("#searchByInput").val().trim();
        fromDate = $("#datePicker1").val();
        toDate = $("#datePicker2").val();
    }

}

function AjaxTable(_ajaxUrl, _wrapper, _defaultSortColumn, _table, _ajaxCallback) {

    // const
    var logEnabled = false;

    // Helper map to reselect previous choices. The same values are used in "lengthMenu" property of DataTable
    // When we use negative numbers the DataTable plugin will NOT paginate anymore and this is the expected behaviour in AjaxTable
    // We use numbers from -4 to -1 to know with "real" number was selected (10, 25, 50 or 100)
    var pageSizeMap = {"10": "-1", "25": "-2", "50": "-3", "100": "-4"};

    // default values
    var defaultSortColumn = _defaultSortColumn;
    var defaultPageSize = 10;
    var defaultSortAsc = false;
    var firstPage = 0;

    // parameters
    var ajaxUrl = _ajaxUrl;
    var wrapper = _wrapper;
    var ajaxCallback = _ajaxCallback;
    var table = _table;

    // optional attributes, changed by setter
    var ajaxTableForm;

    // other attributes
    var sortColumn;
    var pageSize;
    var currentPage;
    var sortAsc;

    var ajaxInProgress = false;

    // initializes the table, doing first ajax call
    this.init = function(sendInitialAjax){
        log('init');
        loadDefaultValues();
        if (sendInitialAjax) {
            sendAjax();
        }
    }

    // specifies which ajaxFormTable is managing this table
    this.setAjaxTableForm = function(_ajaxTableForm){
        ajaxTableForm = _ajaxTableForm;
    }

    // restart table
    this.restart = function(){
        loadDefaultValues();
        sendAjax();
    }

    function loadDefaultValues(){
        pageSize = defaultPageSize;
        sortAsc = defaultSortAsc;
        sortColumn = defaultSortColumn;
        goToFirstPage();
    }

    function goToFirstPage(){
        currentPage = firstPage;
    }

    function bindEvents(){
        getPageSizeSelector().off().on("change", function(){
            onPageSizeChange($("option:selected", this).text());
        });
        getPreviousButton().off().on("click", function(event){
            event.preventDefault();
            onCurrentPageChange($(this), -1);
        });
        getNextButton().off().on("click", function(event){
            event.preventDefault();
            onCurrentPageChange($(this), +1);
        });
        getSortingCells().off().on("click", function(event){
            $(this).blur();
            onSortingChange($(this).data("columnName"));
        });
    }

    function handlePaginationButtonsStyle(){
        // previous button
        if (currentPage == firstPage) {
            getPreviousButton().addClass("disabled");
        } else {
            getPreviousButton().removeClass("disabled");
        }

        // next button
        if (currentPage == getTotalPages()) {
            getNextButton().addClass("disabled");
        } else {
            getNextButton().removeClass("disabled");
        }
    }

    function handleSortingCellsStyle(){
        // restore default state
        getSortingCells().addClass("sorting").removeClass("sorting_asc").removeClass("sorting_desc").css("font-weight", "normal");
        // change style of current selected cell
        var currentCell = getSortingCells().filter("[data-column-name=" + sortColumn + "]");
        currentCell.css("font-weight", "bold");
        if (sortAsc) {
            currentCell.addClass("sorting_asc");
        } else {
            currentCell.addClass("sorting_desc");
        }
    }

    function getPreviousButton(){
        return $("li.previous", wrapper);
    }

    function getNextButton(){
        return $("li.next", wrapper);
    }

    function getPageSizeSelector(){
        return $("select", wrapper);
    }

    function getSortingCells(){
        return $("th.sorting, th.sorting_asc, th.sorting_desc", wrapper);
    }

    function updateSummaryFields(){
        var elements = $("div.dataTables_info b", wrapper);
        var begin = (pageSize * currentPage) + 1;
        var resultsInCurrentPage = $("tr", wrapper).size() - 1; // -1 because the header is one extra row, so we need to remove it
        var end = (begin - 1) + resultsInCurrentPage;
        elements.eq(0).html(begin);
        elements.eq(1).html(end);
        elements.eq(2).html(readTotalResults());
    }

    // user changed page size
    function onPageSizeChange(_pageSize){
        pageSize = _pageSize;
        goToFirstPage();
        sendAjax();
    }

    // user changed sorting
    function onSortingChange(columnName){
        // if repeated the column, we just change direction
        if (columnName == sortColumn) {
            sortAsc = !sortAsc;
        } else {
            sortAsc = true;
            sortColumn = columnName;
        }
        goToFirstPage();
        handleSortingCellsStyle();
        sendAjax();
    }

    // user changed current page
    function onCurrentPageChange(clicked, diff){
        if (ajaxInProgress || clicked.hasClass("disabled")) {
            return;
        }

        currentPage += diff;
        if (currentPage < 1) {
            goToFirstPage();
        } else if (currentPage > getTotalPages()) {
            currentPage = getTotalPages();
        }
        handlePaginationButtonsStyle();
        sendAjax();
    }

    function createBaseUrl(){
        var url = ajaxUrl;

        url += "?pageSize=" + pageSize;
        url += "&currentPage=" + currentPage;
        url += "&sortColumn=" + sortColumn;
        url += "&sortDirection=" + (sortAsc ? 'asc' : 'desc');
        if (table) {
            url += "&table=" + table;
        }

        return url;
    }

    // send ajax to update table
    function sendAjax() {
        var url = createBaseUrl();

        // if has extra params, add them
        if (ajaxTableForm) {
            var extraParams = ajaxTableForm.createUrlParamsUsingForm();
            if (extraParams) {
                url += extraParams;
            }
        }

        log("Sending ajax to /" + url);
        startProgress();
        var finalUrl = url + " " + wrapper + ">.ajaxTableWrapperContent";
        $(wrapper).load(finalUrl, function(){
            sendAjaxCallback();
        });
    }

    function sendAjaxCallback(){
        ajaxCallback.call();
        fixDataTables();
        handlePaginationButtonsStyle();
        handleSortingCellsStyle();
        bindEvents();
        reselectValues();
        updateSummaryFields();
        checkHiddenMessages();
        stopProgress();
    }

    function checkHiddenMessages() {
        var selector = $("table[data-hidden-message-selector]", wrapper).data("hiddenMessageSelector");
        var showMessage = $("table[data-hidden-message-show]", wrapper).data("hiddenMessageShow");
        if (showMessage) {
            $(selector).removeClass("hide");
        } else {
            $(selector).addClass("hide");
        }
    }

    function startProgress(){
        ajaxInProgress = true;
        loadingCircleShow("show");
        getPageSizeSelector().attr('disabled', true);
    }

    function stopProgress(){
        ajaxInProgress = false;
        loadingCircleShow("hide");
        getPageSizeSelector().attr('disabled', false);
    }

    function getTotalPages(){
        return Math.ceil(readTotalResults()/pageSize) - 1;
    }

    function reselectValues(){
        $("select", wrapper).val(pageSizeMap[pageSize]);
    }

    function fixDataTables(){
        fixDataTable("#datatab-desktop_info", "#datatab-desktop_paginate", '#datatab-desktop_wrapper');
        fixDataTable("#datatab-tablet_info", "#datatab-tablet_paginate", '#datatab-tablet_wrapper');
        fixDataTable("#datatab-mobile_info", "#datatab-mobile_paginate", '#datatab-mobile_wrapper');
    }

    function fixDataTable(info, paginate, wrapper){
          $(info).detach().prependTo(wrapper);
          $(paginate).detach().prependTo(wrapper);
    }

    function readTotalResults(){
        return $("table[data-total-results]", wrapper).data("totalResults");
    }

    function log(message){
        if (logEnabled) {
            console.log("[AjaxTable][" + wrapper + "] " + message);
        }
    }

}

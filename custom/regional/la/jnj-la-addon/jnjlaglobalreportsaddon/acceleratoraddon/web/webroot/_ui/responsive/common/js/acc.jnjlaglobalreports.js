var BOR_qucikSelectionDate = "#BOR-qucikSelectionDate", backOrderReportBlock = '.backOrderReportBlock', LATAM = '.LATAM',
    reports_fromDate = '#datePicker1', reports_toDate = '#datePicker2';
var openOrdersReportFormID = "#openOrdersReportForm";
$(document).ready(function() {
	if(!$(""+backOrderReportBlock+""+LATAM+" "+reports_fromDate+"").val()){
    setBORStartEndDates();
	}
    
    $(""+backOrderReportBlock+""+LATAM+" "+BOR_qucikSelectionDate+"").on('change', function() {
        setBORStartEndDates();
    });
    
    
});
/** this method is used to generate PDF or excel for BACKORDER **/
$("#backOrderReportExcel,#backOrderReportPdf").click(function(){
	var backOrderReportFormId = "#backOrderReportForm";
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$(backOrderReportFormId).attr("action", ACC.config.contextPath +'/reports/backorder/downloadReport');
	$(backOrderReportFormId).submit();
	$(backOrderReportFormId).attr("action", $("#originalFormAction").val());
});
function setBORStartEndDates() {
    var quickSelectDate = $(""+backOrderReportBlock+""+LATAM+" "+BOR_qucikSelectionDate+"").val();
    var dateObj = new Date();
    dateObj.setDate( dateObj.getDate() - parseInt(quickSelectDate));
    $(""+backOrderReportBlock+""+LATAM+" "+reports_fromDate+"").datepicker("setDate", dateObj);
    $(""+backOrderReportBlock+""+LATAM+" "+reports_toDate+"").datepicker("setDate", new Date());
}


/** this method is used to generate PDF or excel for OPEN ORDER REPORT **/
$("#openOrdersReportExcel,#openOrdersReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel") !== -1 ? "EXCEL" : "PDF");
	$(openOrdersReportFormID).attr("action", ACC.config.contextPath +'/reports/openordersreport/download');
	if ($("#hddnAccountsString").val() === '') {
                $("#hddnAccountsString").val($("#accountid").val());
            }
	$(openOrdersReportFormID).submit();
	$(openOrdersReportFormID).attr("action", $("#openOrdersReportFormAction").val());
});
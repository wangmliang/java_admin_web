var list_url = ctxPaths + '/flowExchangeLog/query.ajax?isMarketing=2';
var grid_selector = "#pdcl-grid-table";
var pager_selector = "#pdcl-grid-pager";

$(function() {
    pDCLInit();
});

function pDCLInit() {
    $.ajax({
        url : ctxPaths + '/flowExchangeLog/init.ajax',
        dataType : 'json',
        success : function(data, status) {
            if (data.isCustomer) {
                $(".pdclPartnerShow").css("display", "none");
            } else {
                $(".pdclPartnerShow").css("display", "block");
            }
            pDCLInitStatus();
        }
    });
}

function pDCLInitStatus() {
    // 初始化直充记录列表
    pDCLInitDirectChargeList();
    // 文本框输入限制
    pDCLNumberFormatHandler();
    // 初始化下发数和金额
    pDCLInitCountAndPriceTotal();
}

/**
 * 初始化直充记录列表
 */
function pDCLInitDirectChargeList() {
    jqGrid_init($(grid_selector), pager_selector, {
        url : list_url,
        sortable : false,
        colNames : [ '手机号码', '产品名称', '时间', '下发状态'],
        colModel : [ {
            name : 'mobile',
            index : 'mobile',
            sortable : false,
        }, {
            name : 'flowProductInfo.productName',
            index : 'flowProductInfo.productName',
            sortable : false,
            width : 100,
            formatter : formatProdName
        }, {
            name : 'createTime',
            index : 'createTime',
            sortable : false,
            width : 100
        }, {
            name : 'flag',
            index : 'flag',
            sortable : false,
            width : 100,
            formatter : formatStatus
        }]
    });
    function formatProdName(cellvalue, options, rowObject) {
        return $.htmlspecialchars(cellvalue);
    }
    function formatStatus(cellvalue, options, rowObject) {
        if (cellvalue == "0" || cellvalue == "00") {
            return "兑换成功";
        } else if (cellvalue == "1") {
            return "兑换失败";
        } else {
            return "失败";
        }
    }
    $('#pdcl-seachBtn').on('click', function() {
        $(grid_selector).jqGrid('setGridParam', {
            postData : $('#queryForm').serializeJson(),
            page : 1
        }).trigger("reloadGrid");
        
        // 初始化下发数和金额
        pDCLInitCountAndPriceTotal();
    });
    $('#pdcl-resetBtn').on('click', function() {
        resetForm($('#queryForm'));
    });
}

/**
 * 文本框输入限制
 */
function pDCLNumberFormatHandler() {
    $(".pdclNumberFormat").keypress(function(event) {
        var keyCode = event.which;
        if (keyCode >= 48 && keyCode <= 57)
            return true;
        else
            return false;
    });
}

/**
 * 初始化下发数和金额
 */
function pDCLInitCountAndPriceTotal() {
    var info = {};
    info.mobile = $("#pdcl-mobile").val();
    info.createStartTime = new Date($(".date-picker:first").val());
    info.createEndTime = new Date($(".date-picker:last").val());
    info.account = $("#pdcl-account").val();
    $.ajax({
        url : ctxPaths + '/flowExchangeLog/initCountAndPriceTotal.ajax',
        type : "post",
        contentType : "application/json; charset=UTF-8",
        cache : false,
        dataType : 'json',
        data: JSON.stringify(info),
        success : function(data, status) {
            if (data.errMsg) {
                Q_Alert_Fail(data.errMsg);
            } else {
                $("#pdcl-count").text(data.count);
                $("#pdcl-price-total").text(data.priceTotal);
            }
        }
    });
}
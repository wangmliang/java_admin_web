var list_url = ctxPaths + '/flowExchangeLog/query.ajax?isMarketing=1';
var grid_selector = "#pmil-grid-table";
var pager_selector = "#pmil-grid-pager";
var isCustomer = false;

$(function() {
    pMILInit();
});

function pMILInit() {
    $.ajax({
        url : ctxPaths + '/flowExchangeLog/init.ajax',
        dataType : 'json',
        success : function(data, status) {
            isCustomer = data.isCustomer;
            pMILInitStatus();
        }
    });
}

function pMILInitStatus() {
    // 初始化【客戶名称】下拉列表
    pMILInitCustomerNameDDL()
    // 初始化营销下发记录列表
    pMILInitMarketingIssuedList();
}

/**
 * 初始化【客戶名称】下拉列表
 */
function pMILInitCustomerNameDDL() {
    if (isCustomer) {
        $(".pmilPartnerShow").css("display", "none");
    } else {
        $(".pmilPartnerShow").css("display", "block");
        $.ajax({
            url : ctxPaths + '/orderInfo/selectByPartnerId.ajax',
            dataType : 'json',
            success : function(data, status) {
                if (data.errMsg) {
                    Q_Alert_Fail(data.errMsg);
                } else {
                    // 初始化select2组件
                    initSelect2(data);
                }
            }
        });
    }
}

/**
 * 初始化select2组件
 * @param data
 */
function initSelect2(data) {
    $("#pMIL-customer-select").removeClass().css("width","240px").select2({
        minimumInputLength: 1,
        ajax: {
            url: ctxPaths + '/orderInfo/selectCustomerInfoByName.ajax',
            dataType: 'json',
            data: function (term) {
                return {
                    "customerName": term
                };
            },
            results: function (data) {
                return {
                    results: $.map(data.customerList, function (item) {
                        return {
                            id: item.customerId,
                            text: item.customerName
                        }
                    })
                };
            }
        }
    });
}

/**
 * 初始化营销下发记录列表
 */
function pMILInitMarketingIssuedList() {
    jqGrid_init($(grid_selector), pager_selector, {
        url : list_url,
        sortable : false,
        colNames : [ '手机号码', '流量+卡号', '产品名称', '时间', '状态'],
        colModel : [ {
            name : 'mobile',
            index : 'mobile',
            sortable : false,
        }, {
            name : 'flowCardInfo.cardNo',
            index : 'flowCardInfo.cardNo',
            sortable : false,
            width : 100,
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
    $('#pmil-seachBtn').on('click', function() {
        $(grid_selector).jqGrid('setGridParam', {
            postData : $('#queryForm').serializeJson(),
            page : 1
        }).trigger("reloadGrid");
    });
    $('#pmil-resetBtn').on('click', function() {
        $("#pMIL-customer-select").select2("val", "");
        resetForm($('#queryForm'));
    });
}
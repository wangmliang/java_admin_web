var list_url = ctxPaths + '/orderInfo/query.ajax';
var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var isWYAdmin = false;
var isMarketing = false;

$(function() {
    pOILInitStatus();
    pOILSetListener();
});

/**
 * 状态初始化
 */
function pOILInitStatus() {
    // 初始化【客戶名称】下拉列表
    pOILInitCustomerNameDDL();
    // 输入框限制
    inputNumberFormatHandler();
}

/**
 * 初始化【客戶名称】下拉列表
 */
function pOILInitCustomerNameDDL() {
    $.ajax({
        url : ctxPaths + '/orderInfo/selectByPartnerId.ajax',
        dataType : 'json',
        success : function(data, status) {
            if (data.errMsg) {
                Q_Alert_Fail(data.errMsg);
            } else {
                isWYAdmin = data.isWYAdmin;
                isMarketing = data.isMarketing;

                // 初始化产品订单列表
                pOILInitProdOrderList();
                // 初始化select2组件
                initSelect2();
            }
        }
    });
}

/**
 * 初始化产品订单列表
 */
function pOILInitProdOrderList() {
    jqGrid_init($(grid_selector), pager_selector, {
        url : list_url,
        sortable : true,
        sortname : 'create_time',
        sortorder : 'desc',
        colNames : [ '订单编号', '客户名称', '合作伙伴', '合作类型', '订单类型', '创建时间', '订单状态', '操作'],
        colModel : [ {
            name : 'orderIdStr',
            index : 'orderIdStr',
            sortable : true,
            sortname : 'order_id',
            formatter : formatOrderId
        }, {
            name : 'customerName',
            index : 'customerName',
            sortable : true,
            sortname : 'customer_name',
            width : 100,
            formatter: formatCustomerName
        }, {
            name : 'partnerName',
            index : 'partnerName',
            sortable : true,
            sortname : 'partner_name',
            width : 100,
        }, {
            name : 'partnerTypeDesc',
            index : 'partnerTypeDesc',
            sortable : true,
            sortname : 'partner_type',
            width : 80,
        }, {
            name : 'orderTypeDesc',
            index : 'orderTypeDesc',
            sortable : true,
            sortname : 'order_type',
            width : 100
        }, {
            name : 'createTime',
            index : 'createTime',
            sortable : true,
            sortname : 'create_time',
            width : 100
        }, {
            name : 'statusDesc',
            index : 'statusDesc',
            sortable : true,
            sortname : 'status',
            width : 60
        }, {
            name : 'myac',
            index : '',
            width : 160,
            fixed : true,
            sortable : false,
            resize : false,
            formatter : formatActionButtons,
        }]
    });
    function formatCustomerName(cellvalue, options, rowObject) {
        return $.htmlspecialchars(cellvalue);
    }
    function formatOrderId(cellvalue, options, rowObject) {
        return '<a href="javascript:;" onclick="viewEvent(\'' + cellvalue + '\')">' + cellvalue + '</a>';
    }
    function formatActionButtons(cellvalue, options, rowObject) {
        var btnHtml = "<div>";
        // 需求变更：管理员和合作伙伴都可以改变订单状态 start
        var orderStatusTemp = rowObject['status'];
        var btnTitle = "";
        var changeStatus = 0;
        if (orderStatusTemp == 1) {
            btnTitle = "生效";
            changeStatus = 2;
        } else if (orderStatusTemp == 2) {
            btnTitle = "暂停";
            changeStatus = 3;
        } else if (orderStatusTemp == 3) {
            btnTitle = "生效";
            changeStatus = 2;
        }
        btnHtml += '<button onclick=\"pOILOrderTypeBtnClickHandler('+ changeStatus + ',\'' + rowObject['orderIdStr'] + '\')\" class=\"btn btn-xs btn-warning\" permCheck=\"auth_order_manager_list,changestatus,hidden\" data-rel=\"tooltip\" title=\"' + btnTitle + '\" ><i class=\"ace-icon fa fa-flag bigger-120\"></i></button>';
        // 需求变更：管理员和合作伙伴都可以改变订单状态 end
        
        btnHtml += '<button onclick=\"viewEvent(\'' + rowObject['orderIdStr'] + '\')\" class=\"btn btn-xs btn-success\" permCheck=\"auth_order_manager_list,detail,hidden\" data-rel=\"tooltip\" title=\"详情\" ><i class=\"ace-icon fa  fa-eye bigger-120\"></i></button>';
        if (rowObject.status != 2) {
            btnHtml += '<button onclick=\"pOILModifyBtnClickHandler(\'' + rowObject['orderIdStr'] + '\', '+ rowObject['orderType'] +')\" class=\"btn btn-xs btn-info\" permCheck=\"auth_order_manager_list,modify,hidden\" data-rel=\"tooltip\" title=\"修改\" ><i class=\"ace-icon fa fa-pencil bigger-120\"></i></button>';
        }
        btnHtml += '<button onclick=\"exportEvent(\'' + rowObject['orderIdStr'] + '\')\" class=\"btn btn-xs btn-primary\" data-rel=\"tooltip\" title=\"导出\" ><i class=\"ace-icon fa fa-share-square-o bigger-120\"></i></button>';
        btnHtml += '<button onclick=\"deleteEvent(\'' + rowObject['orderIdStr'] + '\')\"'
        +    ' class=\"btn btn-xs btn-danger\" permCheck=\"auth_order_manager_list,delete,hidden\"'
        +    ' data-rel=\"tooltip\" title=\"删除\" >'
        +      '<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>'
        + '</button>'
        btnHtml += "</div>";
        return btnHtml;
    }

    $('#seachBtn').on('click', function() {
        $(grid_selector).jqGrid('setGridParam', {
            postData : $('#queryForm').serializeJson(),
            page : 1
        }).trigger("reloadGrid");
    });
    $('#resetBtn').on('click', function() {
        $("#pOIL-customer-select").select2("val", "");
        resetForm($('#queryForm'));
    });
}

/**
 * 初始化select2组件
 * @param data
 */
function initSelect2() {
    $("#pOIL-customer-select").removeClass().css("width","220px").css("margin-left","-40px").select2({
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
 * 事件监听器
 */
function pOILSetListener() {
    // 新增订单 按钮点击事件
    pOILAddOrderBtnClickHandler();
    // 导出 按钮点击事件
    pOILExportOrderBtnClickHandler();
    // 价格设置 按钮点击事件
    pOILPriceSetBtnClickHandler();
}

/**
 * 新增订单 按钮点击事件
 */
function pOILAddOrderBtnClickHandler() {
    $('#pOILAddOrderBtn').click(function() {
        if (isWYAdmin || isMarketing) {
            $("#pOILOrderModal").modal({
                backdrop: "static",
                keyboard: false
            });
        } else {
            window.location.href = ctxPaths + '/pages/orderInfo_add.shtml?orderType=1';
        }
    });
}

/**
 * 流量包订单点击事件
 */
function pOILFlowPackageBtnClickHandler() {
    window.location.href = ctxPaths + '/pages/orderInfo_add.shtml?orderType=1';
}

/**
 * 流量加订单点击事件
 */
function pOILFlowPlusBtnClickHandler() {
    window.location.href = ctxPaths + '/pages/orderInfo_add.shtml?orderType=2';
}

/**
 * 修改 按钮点击事件
 */
function pOILModifyBtnClickHandler(id, orderType) {
    window.location.href = ctxPaths + '/pages/orderInfo_add.shtml?orderId=' + id + '&orderType=' + orderType;
};

/**
 * 暂停/生效 按钮点击事件
 */
function pOILOrderTypeBtnClickHandler(status, orderId) {
    $.ajax({
        url: ctxPaths + '/orderInfo/changeStatus.ajax?status=' + status + '&orderId=' + orderId,
        dataType: "json",
        success: function(data) {
            if (data.success == true) {
                $(grid_selector).trigger("reloadGrid");
            } else {
                Q_Alert_Fail(data.message);
            }
        }
    });
}

function viewEvent(id) {
    window.location.href = ctxPaths + '/pages/orderInfo_view.shtml?orderId=' + id;
};

// 导出指定订单
function exportEvent(id) {
    $('#queryForm').exportData({
        url: ctxPaths + '/orderInfo/downLoadOrderByOrderId.ajax',
        data: {'orderId' : id},
        callback: function(data) {},
        failure: function(form, action) {}
    });
};

function deleteEvent(id) {
    Q_Confirm("是否要删除？", function(result) {
        if (result) {
            $.ajax({
                url: ctxPaths + '/orderInfo/delete.ajax?orderId=' + id,
                dataType: "json",
                success: function(data) {
                    if (data.success == true) {
                        $(grid_selector).jqGrid('setGridParam', {
                            page: 1
                        }).trigger("reloadGrid");
                    } else {
                        Q_Alert_Fail(data.message);
                    }
                }
            });
        }
    });
};

/**
 * 导出 按钮点击事件
 */
function pOILExportOrderBtnClickHandler() {
    $('#pOILExportOrderBtn').click(function() {
        var records = $(grid_selector).jqGrid('getGridParam', 'records');
        if (records == 0) {
            Q_Alert_Fail("没有订单记录，无法导出。");
            return;
        }
        $('#queryForm').exportData({
            url: ctxPaths + '/orderInfo/downLoadOrder.ajax',
            callback: function(data) {
//                if (!data.success) {
//                    Q_Alert(data.data.msg, undefined, true, function() {
//                    });
//                }
            },
            failure: function(form, action) {
//                Q_Alert_Fail(rtn.data.msg, undefined, false);
            }
        });
    });
}

/**
 * 价格设置 按钮点击事件
 */
function pOILPriceSetBtnClickHandler() {
    $('#pOILPriceSetBtn').click(function() {
        window.location.href = ctxPaths + '/pages/productPriceSetting.shtml';
    });
}

/**
 * 文本框输入限制
 */
function inputNumberFormatHandler() {
    $(".numberFormat").keypress(function(event) {
        var keyCode = event.which;
        if (keyCode == 8 || keyCode == 0 || keyCode >= 48 && keyCode <= 57)
            return true;
        else
            return false;
    });
}
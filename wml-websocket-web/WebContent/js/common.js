/**
 * webscoket
 */
var websocket;
var WEB_SOCKET_SWF_LOCATION = ctxPaths + "/static/js/sockjs/WebSocketMain.swf";
var WEB_SOCKET_DEBUG = false; // webscoket 日志输出
/**
 * webScoket 初始化
 * @param url ws://xxx(不需要带ws://)
 * @author WML 
 * 2017年9月21日 - 下午2:52:19
 */
var initWebSocket = function(url) {
	// 指定websocket路径
	websocket = new WebSocket("ws://" + url);
    websocket.onmessage = function(event) {
   	 	// 弹出任务完成提醒(待定)
    	var data = JSON.parse(event.data);
    	console.log("socket:" + event.data);
    	if(data.from == 1) { // 升级
    		var arr = data.text.split("-");
    		$("#companyLv").html(arr[0] + "级");
    		$("#companyValue").css("width", arr[1]);
    	} else if(data.from == 2 || data.from == 3) { // 完成任务
    		if($("#task_" + data.to) != undefined && $("#task_" + data.to) != null) {
    			if(data.from == 2) {
    				$("#task_" + data.to).append('<b>(已完成)</b>');	
    			} else {
    				$("#task_" + data.to).remove();
    			}
    			// 判断是否隐藏更多
    			var zhuxian = $(".zhuxian_style").length;
    			if(zhuxian <= 4)
    				$("#zhuxian_a").addClass("hide");
    			if(zhuxian == 0)
    				$("#zhuxian").addClass("hide");
    			var chengjiu = $(".chengjiu_style").length;
    			if(chengjiu <= 4)
    				$("#chengjiun_a").addClass("hide");
    			if(chengjiu == 4)
    				$("#chengjiun").addClass("hide");
    		}
    		$(".prompt_txt_count p").html(data.text);
    	} else {	// 任务引导
    		
    	}
    	websockMessage(data);
    };
}

/**
 * 推送消息处理
 * @param data
 * @author WML
 * 2017年10月13日 - 上午10:58:08
 */
function websockMessage(data) {
	switch(data.flag) {
		case 1:
			$(".prompt_txt_count p").html(data.text);
		  break;
		case 2:
			$(".prompt_txt_count p").html(data.text);
		  break;
		default:
		  break;
	}
}

/**
 * 地址栏获取参数
 * @param param 待匹配参数
 * @author WML 
 * 2017年9月21日 - 下午2:52:19
 */
var getP = function(param) {
    var hrefstr, pos, parastr, para, tempstr;
    hrefstr = window.location.href;
    pos = hrefstr.indexOf("?");
    parastr = hrefstr.substring(pos + 1);
    para = parastr.split("&");
    tempstr = "";
    for (var i = 0; i < para.length; i++) {
        tempstr = para[i];
        pos = tempstr.indexOf("=");
        if (tempstr.substring(0, pos).toLowerCase() == param.toLowerCase()) {
            return tempstr.substring(pos + 1);
        }
    }
    return '';
};

/**
 * QQ表情
 */
var QQFace = {
	/**
	 * QQ表情初始化(需结合replace2Img(str)一起使用)
	 * @param idOrClass 触发元素id或class
	 * @param idText	文本显示位置id
	 * @author WML
	 * 2017年9月21日 - 下午3:25:56
	 */
	init : function(qq, idText){
		$(qq).qqFace({
    		id : 'facebox', 
    		assign : idText, 
    		path : ctxPaths + '/static/js/qqFace/arclist/'	//表情存放的路径
    	});
	},
	/**
	 * 将文本转化为QQ表情
	 * @param str
	 * @returns
	 * @author WML
	 * 2017年9月21日 - 下午3:27:07
	 */
	replace2Img : function(str){
    	str = str.replace(/\</g,'&lt;');
    	str = str.replace(/\>/g,'&gt;');
    	str = str.replace(/\n/g,'<br/>');
    	str = str.replace(/\[em_([0-9]*)\]/g,'<img src="' + ctxPaths + '/static/js/qqFace/arclist/$1.gif" border="0" />');
    	return str;
    }
}

/**
 * 定位{div}位置的最下方
 * @param id	元素id
 * @author WML
 * 2017年9月21日 - 下午3:27:07
 */
var ScrollToBottom = function(id){
	var div = document.getElementById(id);
	div.scrollTop = div.scrollHeight;
}

/********************* 表单处理   start ***************************/
/**
 * 表单验证公用方法
 * @author WML
 * 2017年8月30日 - 下午5:10:50
 */
var TableValidata = {
	/**
	 * 表单验证初始化
	 * @param id 	表单id
	 * @param input submit按钮
	 * @author WML
	 * 2017年8月30日 - 下午5:10:50
	 */
	init_validationEngine : function(id, input){
		// 表单验证
		$(id).validationEngine(
			'attach', {
				showOneMessage: true,
				focusFirstField: true,
//				promptPosition: 'bottomLeft:160, 5',
				promptPosition: 'bottomRight:-100, 5',
				addPromptClass:'formError-white',
				autoHidePrompt:'true',
				autoHideDelay:'5000',
				scroll: false
			}
		);
		// 表单提交验证是否已通过验证
		$(input).click(function(){
			if($(id).validationEngine('validate'))
				$(input).submit();
			else
				return false;
		});
	},
	/**
	 * 关闭弹窗
	 * @param id  		点击关闭元素id或class
	 * @param content	弹窗id
	 * @param mask		遮罩层id
	 * @author WML
	 * 2017年9月21日 - 下午3:53:16
	 */
	close : function(id, content, mask) {
		$(id).click(function(){
			$(content).remove();
			$(mask).hide();	
		});	
	},
	/**
	 * mybatis分页插件
	 * @param id	分页显示位置div
	 * @param total	总数据
	 * @param size  分页条数
	 * @param num 	第几页
	 * @param form	表单对象
	 * @author WML
	 * 2017年9月26日 - 下午1:37:45
	 */
	mybatisPaging : function(id, total, size, num, form) {
		$(id).pagination({
			total : Number(total),
			pageSize : Number(size),
			pageNumber : Number(num),
			layout : [ 'first', 'prev', 'links', 'next', 'last' ],
			onSelectPage : function(pageNumber, pageSize) {
				$("#currentpage").val(pageNumber);
				$("#pagesize").val(pageSize);
				$(form).submit();
			}
		});
	},
	/**
	 * 公用分页
	 * @param id 	分页显示位置id
	 * @param tol	总条数
	 * @param size	每页条数
	 * @param num	第几页
	 * @param form	表单id
	 * @param currentpage 当前页input对象
	 * @author WML
	 * 2017年9月21日 - 下午3:56:15
	 * <!--分页示例-->
		<div class="table_page_count page_margin_top clearfix" id="pageDiv">
			<div class="page_btn_count fr clearfix">
				<a class="two_words_btn can_not_drop">首页</a> 
				<a class="three_words_btn anther_not_drop">上一页</a> 
				<span>至</span> 
				<input type="text" value="1"> 
				<span>页</span> 
				<a class="link_page_btn">跳转</a> 
				<a class="three_words_btn">下一页</a> 
				<a class="two_words_btn">末页</a> 
				<span>1&nbsp;/&nbsp;3&nbsp;页</span>
			</div>
		</div>
	 * 引用： TableValidata.paging('#分页块外层div', '总数', '每页数', '当前页', '表单id', '当前页input对象');
	 */
	paging : function(id, total, pageSize, pageNum, form, currentpage) {
		var onePage = $(id).children().children('a:eq(0)');
		var topPage = $(id).children().children('a:eq(1)');
		var _input = $(id).children().children('input');
		var jump = $(id).children().children('a:eq(2)');
		var nextPage = $(id).children().children('a:eq(3)');
		var lastPage = $(id).children().children('a:eq(4)');
		var pageCode = $(id).children().children('span:eq(2)');
		// 计算总页数(向上取整)
		var page = Math.ceil(parseInt(total) / parseInt(pageSize));
		// 无数据时禁用点击事件
		if(total == 0) {
			// 无数据不显示分页
			$(id).addClass("hide");
			
			onePage.addClass("can_not_drop");
			topPage.addClass("anther_not_drop");
			nextPage.addClass("anther_not_drop");
			lastPage.addClass("can_not_drop");
		} else {
			// 首页、第一页屏蔽
			if(pageNum == 1) {
				onePage.addClass("can_not_drop");
				topPage.addClass("anther_not_drop");
			} else {
				onePage.removeClass("can_not_drop");
				topPage.removeClass("anther_not_drop");
				// 首页
				onePage.bind('click', function() {
					$(currentpage).val(1);
					$(form).submit();
				});
				// 上一页
				topPage.bind('click', function() {
					$(currentpage).val(parseInt(pageNum) - 1);
					$(form).submit();
				});
			}
			// 如果总页数==1 或者当前页==最后一页，尾部屏蔽
			if(page == 1 || pageNum == page) {
				nextPage.addClass("anther_not_drop");
				lastPage.addClass("can_not_drop");
				// 只有一页不显示分页
				if(page == 1)
					$(id).addClass("hide");
			} else {
				// 下一页
				nextPage.bind('click', function() {
					$(currentpage).val(parseInt(pageNum) + 1);
					$(form).submit();
				});
				// 尾页
				lastPage.bind('click', function() {
					$(currentpage).val(page);
					$(form).submit();
				});
			}
		}
		// 显示页数
		pageCode.html(pageNum + "&nbsp;/&nbsp;" + page + "&nbsp;页(共" + total + "条)");
		// 跳页
		if(page > 1) {
			jump.removeClass('anther_not_drop');
			jump.bind('click', function() {
				var cpage = _input.val();
				// 输入非整数 或者 小于0
				if(isNaN(cpage) || cpage <= 0) {
					tip("3", "请输入正确页数");
					return false;
				}
				var p = cpage;
				// 输入值大于最大页数，重置为最大页数
				if(cpage > page) {
					p = page;
				}
				_input.val(p);
				$(currentpage).val(p);
				$(form).submit();
			});
		} else {
			jump.addClass('anther_not_drop');
		}
		_input.val(page);
	},
	/**
	 * 全选反选
	 * @param id		全选反选id属性
	 * @param checkDiv	复选框外层包裹的id或class
	 * @author WML
	 * 2017年8月30日 - 下午5:10:39
	 */ 
	checkAll : function(id, checkDiv) {
		$(id).click(function() {
			if(this.checked){   
		        $(checkDiv + " :checkbox").prop("checked", true);  
		    }else{   
				$(checkDiv + " :checkbox").prop("checked", false);
		    }   
		});
	},
	/**
	 * 复选框点击事件
	 * @param checkId	列表复选框class
	 * @param checkDiv	复选框外层包裹的id或class
	 * @param all		全选反选id属性
	 * @author WML
	 * 2017年8月30日 - 下午5:11:10
	 */
	checkboxClick : function(checkId, checkDiv, all) {
		$(chekcId).click(function() {
			if(this.checked){   
				var chknum = $(checkDiv + " :checkbox").size() - 1;
				var chk = 0;
				$(checkDiv + " :checkbox").each(function () {  
			        if($(this).prop("checked") == true){
			        	chk++;
					}
				});
				if(chk == chknum) {
					$(all).prop("checked", true);
				}
			} else {
				$(all).prop("checked", false);
			}
		});
	},
	/**
	 * 单个删除
	 * @param del	点击class
	 * @param url	删除链接
	 * @author WML
	 * 2017年8月30日 - 下午5:11:56
	 */
	deleteOne : function(del, url) {
		$(del).click(function(){
			var id = $(this).attr("data-id");
			if(confirm("确认删除?")){
				$(this).attr("href", url + id);
			}
		});
	},
	/**
	 * 批量删除
	 * @param del		点击id
	 * @param checkDiv	复选框外城包裹的id或class
	 * @param url		删除链接
	 * @author WML
	 * 2017年8月30日 - 下午5:12:22
	 */
	batchDelete : function(del, checkDiv, url) {
		$(del).click(function() {
			var val = "";
			$(checkDiv + " :checkbox").each(function () {  
		        if($(this).prop("checked") == true){
		        	if($(this).val() != "on"){
		        		val += ($(this).val() + ",");
		        	}
				}
			});
			if(val == "") {
				tip("1", "请选择要删除的数据");
				return false;
			}
			if(confirm("确认删除?")){
				$(this).attr("href", url + val);
			}
		});
	},
	/**
	 * 询问框
	 * @param message	显示信息
	 * @param id		点击确认触发事件
	 * @author WML
	 * 2017年10月11日 - 下午2:50:58
	 */
	confirmShow : function(message, id) {
		if (confirm(message)) {  
            $(id).next().click();
        }  
	}
}
/********************* 表单处理   end ***************************/

/**
 * 计时器
 */
var IntervalCountDown = {
	/**
	 * 工作倒计时(添加data-time以及data-down)
	 * @param id	
	 * @author WML
	 * 2017年10月18日 - 下午3:45:59
	 */	
	init : function(id) {
		$.each($(id), function(key, data) {
			var dataDown = parseInt($(data).attr("data-down"));
			if(dataDown == 0 || dataDown == "") {
				return;
			}
			var interval = setInterval(function() {
				var down = $(data).attr("data-down");
				down = parseInt(down);
				if(down == 0) {
					return;
				}
				down--;
				$(data).attr("data-down", down);
				$(data).html("(时间：" + down + "秒)");
				if(down == 0) {
					$(data).attr("data-down", 0);
					$(data).html("(已完成)");
					clearInterval(interval);
				}
			}, 1000);
		});
	}
}

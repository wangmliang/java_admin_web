/**
 * webscoket
 */
/*var websocket;
var WEB_SOCKET_SWF_LOCATION = ctxPaths + "/static/js/sockjs/WebSocketMain.swf";
var WEB_SOCKET_DEBUG = false; // webscoket 日志输出
*//**
 * webScoket 初始化
 * @param url ws://xxx(不需要带ws://)
 * @author WML 
 * 2017年9月21日 - 下午2:52:19
 *//*
var initWebSocket = function(url) {
	// 指定websocket路径
	websocket = new WebSocket("ws://" + url);
    websocket.onmessage = function(event) {
   		var data = JSON.parse(event.data);
   	 	// 弹出任务完成提醒(待定)
   		
    };
}*/

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
	 * QQ表情初始化(需结合replaceEm(str)一起使用)
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
	replaceImg : function(str){
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
				promptPosition: 'bottomLeft:160, 5',
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
	 * 公用分页
	 * @param id 	分页显示位置id
	 * @param tol	总条数
	 * @param size	每页条数
	 * @param num	第几页
	 * @param form	表单id
	 * @author WML
	 * 2017年9月21日 - 下午3:56:15
	 */
	paging : function(id, tol, size, num, form) {
		$(id).pagination({
			total : Number(tol),
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
	batchDelete : function() {
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
	}
}
/********************* 表单处理   end ***************************/
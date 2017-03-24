/**
 * @fileoverview 卓望数码 jQuery Common Library
 * @description:封装一些系统公用模块
 * @author oCEAn Zhuang (zhuangruhai@aspirecn.com QQ: 153414843)
 * @version 1.0
 * @date 2014-5-28
 */
// 进度条
var Loading = {
	id : '_loading',
	isShow : false,
	show : function() {
		if (!document.getElementById('_loading')) {
			$('body')
					.append(
							'<div class="loading smaller lighter grey" id="'
									+ this.id
									+ '" style="width:150px;"><i class="ace-icon fa fa-spinner fa-spin green bigger-125"></i>&nbsp;正在处理,请稍候...</div>');
			this.$load = $('#' + this.id);
		}
		this.$load.floatDiv({
			show : true
		});
	},
	hide : function() {
		if (this.$load){
			this.$load.floatDiv({
				show : false
			});
		}
	}
};
$(document).ajaxComplete(function(event, xhr, settings){
	Loading.hide();
	Loading.isShow = false;
	try{
		var result = jQuery.parseJSON(xhr.responseText);
		/* 用户会话失效或为登录，重定向至登录页面 */
		if(result && result['returnCode'] && result['returnCode']  != '0' && result['redirectUrl']) {
				window.location.replace(result['redirectUrl']);
		}
	}catch(e){}
});
$(document).ajaxStart(function(event, jqxhr, settings){
	if (!Loading.isShow )return;
	Loading.show();
});
// 为iframe增加loading
try {
	if (parent != self && window.parent && window.parent.document) {
		var iframe = $(window.parent.document).find('iframe');
		if (iframe && iframe.get(0).tagName == 'IFRAME') {
			iframe.bind('load', function() {
				if (!!Loading){
					Loading.hide();
				}
				
			});
			Loading.show();
		}
	}
} catch (e) {
}
function openTab(title,url){
	if (parent != self && typeof parent.initTab == 'function'){
		parent.initTab(title,url);
	}else{
		location.href =  url;
	}
}
// 阻止回退键的默认行为
$(document)
		.bind(
				"keydown",
				function(e) {
					if (e.which == 8) {
						var targetNodeName = '';
						if (e.target && e.target.nodeName) {
							targetNodeName = e.target.nodeName.toLowerCase();
						}
						if ((targetNodeName != 'input'
								&& targetNodeName != 'textarea' && targetNodeName != 'password')
								|| e.target.readOnly == true) {
							return false;
						}
					}
});
var getScript = function(url,params,callback,rtnName){
	setTimeout(function(){
		var scriptBlock = document.createElement("script");
		scriptBlock.type = "text/javascript";
		var name = rtnName;
		if(!name|| name == ""){
			name = "scriptDataRtn";
		}
		var s = url.indexOf('?') < 0 ? '?' : '&';
		var surl = s + "rtnName=" + name;
		if(params){
			for (var p in params){
				surl = addUrlParam(surl,p,params[p]);
			}
		}
		//surl += "&" + params;
		scriptBlock.src = url + surl;
		if(window.navigator.userAgent.indexOf("MSIE")>=1){
			scriptBlock.onreadystatechange = function(){
				if("loaded" == scriptBlock.readyState || "complete" == scriptBlock.readyState){
					setTimeout(function(){
						try{
							if (eval('('+name+')')){
								callback(eval('('+name+')'));
							}
						}catch(e){
							callback();
						}
					},0);
				}
			};
		}else{
			scriptBlock.onload = function(){
				try{
					if (eval('('+name+')')){
						callback(eval('('+name+')'));
					}
				}catch(e){
					callback();
				}
			};
		}
		document.getElementsByTagName("head")[0].appendChild(scriptBlock);				
	},20);
};
var ifmAutoHeight =  function(){
	var url = getCookie('admin-domain');
	if (url){
		url = $.trim(url) + 'ifm_proxy.html';
		url = url + (url.lastIndexOf('?') == -1? '?' : '&') + (new Date()).getTime();
		url = url + (url.lastIndexOf('?') == -1? '?' : '&') + "referrer=" + encodeURIComponent(document.referrer);
		url = url + (url.lastIndexOf('?') == -1? '?' : '&') + "c=" + encodeURIComponent(location.href.split('#')[0]);
		if (!window.frames["ifm_proxy"]){
			$('body').append('<iframe id="ifm_proxy" name="ifm_proxy" src="" width="0" height="0" style="display:none;" ></iframe>');
		}
		var vHeight = document.body.offsetHeight;//ff,chrom
		if(window.attachEvent)vHeight = document.body.scrollHeight;//兼容ie
		var _vHeight = $("#ifm_proxy").data('_initHeight');
		if (_vHeight != vHeight){
			var curl = window.location.href,curls = curl.split('#');
			var src = url +  "#" + (curls.length > 1 ? curls[curls.length-1] : '') + "#" + vHeight;
			document.getElementById("ifm_proxy").src = src;
			$("#ifm_proxy").data('_initHeight',vHeight);
		}
	}
};
(function() {
	try {
		// validator 添加默认提示信息
		$.extend(jQuery.validator.messages, {
			required : "不能为空",
			remote : "请修正该字段",
			email : "请输入正确格式的电子邮件",
			url : "请输入合法的网址",
			date : "请输入合法的日期",
			dateISO : "请输入合法的日期 (ISO).",
			number : "请输入合法的数字",
			digits : "只能输入整数",
			creditcard : "请输入合法的信用卡号",
			equalTo : "请再次输入相同的值",
			accept : "请输入拥有合法后缀名的字符串",
			maxlength : $.validator.format("长度最多是 {0} 的字符串"),
			minlength : $.validator.format("长度最少是 {0} 的字符串"),
			rangelength : $.validator.format("长度介于 {0} 和 {1} 之间的字符串"),
			range : $.validator.format("介于 {0} 和 {1} 之间的值"),
			max : $.validator.format("最大为{0} 的值"),
			min : $.validator.format("最小为{0} 的值")
		});
		// validator 手机校验
		$.validator.addMethod("number_2",function(value,element){
			var lengthStr=value;
			if((value+"").indexOf(".")!=-1){
				lengthStr=value.substring(0,(value+"").indexOf("."));
			var reg = /^\d+\.+\d{1,2}$/;
				if(reg.test(value)){
					if(lengthStr.length>8){
						return false;
					}
					return true;
				}else{
					return false;
				}
			}
			if(lengthStr.length>8){
				return false;
			}
			return true;
		},"最多只能有八位整数和两位小数")
		$.validator.addMethod("mobile", function(value, element) {
			var tel = /^1\d{10}$/;
			return this.optional(element) || (tel.test(value));
		}, "请输入有效的手机号码");
		$.validator.addMethod("password", function(value, element) {
			var reg = /^(?=.*\d)(?=.*[A-Za-z])[0-9a-zA-Z]/;
			return this.optional(element) || (reg.test(value));
		}, "必须同时包含数字和字母");
		$.validator.addMethod("maxlength", function(value, element,param) {
			var len = $.trim(value).replace(/[^\x00-\xff]/g, '..').length;
			return this.optional(element) || (len <=param);
		}, "不能超过{0}个字节");
		$.validator.addMethod("priceValid", function(value, element) {
			var reg = /^\d{1,3}(\.\d{1,2})?$/;   
			return this.optional(element) || (reg.test(value));
		}, "金额最大999.99(2位小数)");
		$.validator.addMethod("isLetterAndNum", function(value, element) {
			var reg = /^[A-Za-z0-9]+$/;   
			return this.optional(element) || (reg.test(value));
		}, "只能数字和字母组合");
		$.validator.addMethod("isNotCn", function(value, element) {
			var reg = /.*[\u4e00-\u9fa5]+.*$/;   
			return !this.optional(element) || (!reg.test(value));
		}, "只能数字和字母组合");
		$.validator.addMethod("mutipTelValid", function(value, element) {
			if(value==null || $.trim(value)==""){
				return true;
			}
			var reg = /\d$/; 
			var mutipTel=$.trim(value);
			var tels=mutipTel.split(" ");
			var len=tels.length;
			for(var i=0;i<len;i++){
				if(!reg.test(tels[i])){
					return false;
				}
			}
			return true;  
		}, "请用空格分割多个电话号码");
		$.validator.addMethod("endTimeBigThanCurrentTimeValid", function(value, element) {
			if(value==null || $.trim(value)==""){
				return false;
			}
			var d1_arr = value.split(" ");
			var day1_arr = d1_arr[0].split("-");
			var hour1_arr = d1_arr[1].split(":");
			var year=parseInt(day1_arr[0]);
			var month=parseInt(day1_arr[1])-1;
			var day=parseInt(day1_arr[2]);
			var hour=parseInt(hour1_arr[0]);
			var minute=parseInt(hour1_arr[1]);
			var second=parseInt(hour1_arr[2]);
			var date1 = new Date(year,month,day,hour,minute,second);
			var nowTime=new Date();			
			if(date1.getTime()<nowTime.getTime()){
				return false;
			}
			return true;
		}, "结束时间不能早于当前系统时间");
		$.validator.addMethod("endDateTimeValid",function(value, element) { 
			var startDateID = $(element).attr('cmpDate'); 
			var d1_arr = $("#" + startDateID).val().split(" ");
			var d2_arr = value.split(" ");
			var day1_arr = d1_arr[0].split("-");
			var day2_arr = d2_arr[0].split("-");
			var hour1_arr = d1_arr[1].split(":");
			var hour2_arr = d2_arr[1].split(":");
			var date1 = new Date(parseInt(day1_arr[0]),parseInt(day1_arr[1]),parseInt(day1_arr[2]),
			                parseInt(hour1_arr[0]),parseInt(hour1_arr[1]),parseInt(hour1_arr[2]));
			var date2 = new Date(parseInt(day2_arr[0]),parseInt(day2_arr[1]),parseInt(day2_arr[2]),
							parseInt(hour2_arr[0]),parseInt(hour2_arr[1]),parseInt(hour2_arr[2]));
			var time1 = date1.getTime();
			var time2 = date2.getTime();			
			return time1>time2?false:true;
		}, "必须晚于开始时间!");
				
		$.validator.addMethod("endDateValid",function(value, element) { 
			var startDateID = $(element).attr('cmpDate'); 
			var d1_arr = $("#" + startDateID).val();
			var d2_arr = value;
			var day1_arr = d1_arr.split("-");
			var day2_arr = d2_arr.split("-"); 
			var date1 = new Date(parseInt(day1_arr[0]),parseInt(day1_arr[1]),parseInt(day1_arr[2]));
			var date2 = new Date(parseInt(day2_arr[0]),parseInt(day2_arr[1]),parseInt(day2_arr[2]));
			var time1 = date1.getTime();
			var time2 = date2.getTime();
			return time1>time2?false:true;			
			
		}, "必须晚于开始日期!"); 
		//validator添加ace样式效果
		$.validator.setDefaults({
			errorElement : 'div',
			errorClass : 'help-block',
			ignore: "",
			focusInvalid : false,
			focusCleanup : false,
			highlight : function(e) {
				$(e).closest('.form-group').removeClass('has-info').addClass(
						'has-error');
			},
			success : function(e) {
				$(e).closest('.form-group').removeClass('has-error');// .addClass('has-info');
				$(e).remove();
			},
			errorPlacement : function(error, element) {
				if (element.is(':checkbox') || element.is(':radio')) {
					var controls = element.closest('div[class*="col-"]');
					if (controls.find(':checkbox,:radio').length > 1)
						controls.append(error);
					else
						error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				} else if (element.is('.select2')) {
					error.insertAfter(element
							.siblings('[class*="select2-container"]:eq(0)'));
				} else if (element.is('.chosen-select')) {
					error.insertAfter(element
							.siblings('[class*="chosen-container"]:eq(0)'));
				}else if(element.is('.chosen-table')){
					var span=element.nextAll('.error_span:eq(0)').eq(0);
					$(span).html(error[0].innerHTML);
				} else
					error.insertAfter(element.parent());
			}

		});
		/**$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
			_title : function(title) {
				var $title = this.options.title || '&nbsp;';
				if (("title_html" in this.options)
						&& this.options.title_html == true) {
					title.html($title);
				} else {
					title.text($title);
				}
			}
		}));**/
		//设置时间datetimepicker控件默认值
		/**$.fn.datetimepicker.defaults = {
				language:  'zh-CN',
				format : 'yyyy-mm-dd hh:ii:ss',
		        weekStart: 1,
		        todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				useSeconds : true,
				initialDate : new Date()
		};
		$.extend($.fn.datepicker.defaults,{
			language: 'zh-CN',
			autoclose: true,
			format: 'yyyy-mm-dd',
			language: 'zh-CN',
			todayBtn: true,
			todayHighlight: true,
			weekStart: 1
		});**/
	} catch (e) {
	}
	var ifmAutoFixCount = 0;
	$(function(){
		var ifmTimer = null;
		function delayIfmAutoHeight(){
			if (ifmTimer || ifmAutoFixCount > 10)return;
			ifmTimer = setTimeout(function(){ifmAutoHeight();ifmTimer=null;ifmAutoFixCount++},500);
		}
		if(window.attachEvent){
			if (!!document.documentMode){
				document.documentElement.attachEvent('onDOMSubtreeModified', delayIfmAutoHeight, false);
			}else{
				document.documentElement.addEventListener('DOMSubtreeModified', delayIfmAutoHeight, false);
			}
			
		}else{
			document.documentElement.addEventListener('DOMNodeInserted', delayIfmAutoHeight, false);
			document.documentElement.addEventListener('DOMNodeRemoved',delayIfmAutoHeight, false);
		}
		try {
			$(window)
					.resize(
							function() {
								delayIfmAutoHeight();
							});
		} catch (e) {
			alert(e);
		}
		setTimeout(function(){
			delayIfmAutoHeight();
		},100);
	});
	$('a[data-action="collapse"]').on('click',function(){
		var $content = $(this).closest('.widget-box').find('.search_tj_bar');
		if (!$content)return;
		if ($content.is(':hidden')){
			$content.slideToggle(function(){ifmAutoHeight();});
			$(this).find('i').removeClass('fa-chevron-down').addClass('fa-chevron-up');
		}else{
			$content.slideToggle(function(){ifmAutoHeight();});
			$(this).find('i').removeClass('fa-chevron-up').addClass('fa-chevron-down');
		}
	});
})();

function jqGrid_init($grid_selector,pager_selector,options){
	$(window).on('resize.jqGrid', function () {
		var pageContentWidth = $(".page-content").width();
		
		var $gbox = $('#gbox_' + $grid_selector.attr('id')).parent();
		var _height = $gbox.find('.ui-jqgrid-bdiv table').height() + 2;
		if (_height > 328){
			$gbox.find('.ui-jqgrid-bdiv ').height($gbox.find('.ui-jqgrid-bdiv table').height() + 2);
		}
		//$grid_selector.jqGrid('setGridWidth', pageContentWidth  - 1);
		$gbox.find('table.ui-jqgrid-htable,table.ui-jqgrid-btable').width(pageContentWidth  - 1);
		$gbox.find('.ui-jqgrid,.ui-jqgrid-view,.ui-jqgrid-hdiv,.ui-jqgrid-bdiv,.ui-jqgrid-pager').width(pageContentWidth);//修复1px引起的横向滚动条
		
   });
   $grid_selector.jqGrid($.extend({
		datatype: "json",
		height: 328,
		//width: pageContentWidth,
		rowNum:10,
		rowList:[10,20,30],
		pager: pager_selector,
		autowidth: true,
		shrinkToFit: true,
		viewrecords: true,
		altRows: true,
		beforeRequest : function(){
			var params = $(this).getGridParam(),url = params['url'],postData =  params['postData'];
			var columnName = postData['sidx'];
			if (columnName){
				var column = $(this).getColProp(columnName);
				var sortName = column['sortname'] || postData['sidx'];
				url = addUrlParam(url,"sortName",sortName);
				url = addUrlParam(url,"order",postData['sord']);
				$(this).setGridParam({"url":url});
			}
			
		},
		//multiselect: true,
		loadComplete : function() {
			$.permCheck.run();
			var table = this;
			setTimeout(function(){
				styleCheckbox(table);
				//updateActionIcons(table);
				updatePagerIcons(table);
				//enableTooltips(table);
			}, 0);
		}
	},options));
   $grid_selector.jqGrid('navGrid',pager_selector,
	   {
		edit:false,add:false,del:false,
		search: false,
		refresh: true,
		refreshicon : 'icon-refresh green'
	   }
	);
    //replace icons with FontAwesome icons like above
	function updatePagerIcons(table) {
		var replacement = 
		{
			'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
			'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
			'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
			'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
		};
		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
			var icon = $(this);
			var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
			
			if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
		});
	}

     function styleCheckbox(table) {
		$(table).find('input:checkbox').addClass('ace')
		.wrap('<label />')
		.after('<span class="lbl align-top" />');

		$('.ui-jqgrid-labels th[id*="_cb"]:first-child')
		.find('input.cbox[type=checkbox]').addClass('ace')
		.wrap('<label />').after('<span class="lbl align-top" />');
	
	}
	
}



//重置form,添加validator样式清除
function resetForm($form, $validator) {
	$form[0].reset();
	if ($validator) {
		$validator.resetForm();
		$form.find('.form-group').removeClass('has-error');
	}
}


var Q_Alert_Fail =  function(content,callback){
	var _content = '<div class="red" id="dialog-message"><i class="ace-icon fa fa-exclamation-triangle red"></i> ' + content + '</div>';
	var isShow = false;
	//if(window.attachEvent)isShow = true;
	var d = bootbox.dialog({
		message: _content,
		closeButton : false,
		show : isShow,
		callback : callback,
		buttons: 			
				{
					"danger" :
					 {
						"label" : "<i class='ace-icon fa fa-check'></i>确定",
						"className" : "btn-sm btn-danger",
						"callback": function() {
							if (typeof callback === 'function'){
								callback.call(this);
							}
						}
					}
				}
	});
	if (!isShow){
		setPositionInIfm($('.modal-dialog'),d);
	}else{
		d.modal("show");
	}
};
var Q_Alert =  function(content,callback){
	var _content = '<div class="green" id="dialog-message"><i class="ace-icon fa fa-check"></i> ' + content + '</div>';
	var isShow = false;
	//if(window.attachEvent)isShow = true;
	var d = bootbox.dialog({
		message: _content,
		closeButton : false,
		show : isShow,
		callback : callback,
		buttons: 			
				{
					"success" :
					 {
						"label" : "<i class='ace-icon fa fa-check'></i>确定",
						"className" : "btn-sm btn-success",
						"callback": function() {
						 if (typeof callback === 'function'){
							 callback.call(this);
						}
					}
				}
			}
	});
	if (!isShow){
		setPositionInIfm($('.modal-dialog'),d);
	}else{
		d.modal("show");
	}
};
var Q_Confirm =  function(content,callback){
	var isShow = false;
	//if(window.attachEvent)isShow = true;
	bootbox.setDefaults({
		locale : 'zh_CN',
		show : isShow,
		animate : true
	});
	var _defaults = {
		message: content,
		buttons: {
		  confirm: {
			 label: "确定",
			 className: "btn-danger btn-sm"
		  },
		  cancel: {
			 label: "取消",
			 className: "btn-sm"
		  }
		},
		callback: callback
	  };
	
	var d = bootbox.confirm(_defaults);
	if (!isShow){
		setPositionInIfm($('.modal-dialog'),d);
	}else{
		d.modal("show");
	}
	
};
var Q_Prompt =  function(content,callback){
	var isShow = false;
	//if(window.attachEvent)isShow = true;
	bootbox.setDefaults({
		locale : 'zh_CN',
		show : isShow,
		animate : true
	});
	var d = bootbox.prompt(content, function(result) {
		if (typeof callback === 'function'){
			callback.call(this,result);
		}
	});
	if (!isShow){
		setPositionInIfm($('.modal-dialog'),d);
	}else{
		d.modal("show");
	}
};

(function() {
	jQuery.extend(jQuery.fn, {
		modal2: function(config) { 
			$(this).modal(config);
			if (!config || config === 'show' || config['show'] != false){
				//$._setPosition($(this).find('.modal-dialog'));
				$(this).drag({handler: 'div.modal-header,div.modal-header > h4',opacity: 0.75});
			}
		}
	});
})(jQuery);
/**
 * 通过button和链接button中设置的权限属性（属性名为permCheck的格式是"[resKey,]operKey[,hidden|disable]"）发起后台鉴权请求，通过后台返回的结果，对按钮或链接进行置灰和隐藏操作
 * @param {Object} url
 * @param {Object} resKey
 * @memberOf {TypeName} 
 */
;
(function( $, undefined ) {
	PermCheck=function(options){
		if(this.initialized && this.initialized === true){
			return;
		}
		this.options = options || {};
		if(this.options.url == undefined){
			this.options.url = (getCookie('admin-domain') || window.ctxPaths || '' )+ "/auth/pageAuth.ajax";
		}
	};
	PermCheck.prototype={
		_setBehavior : function(result){
				//判断是原生button还是链接button
				if ($(this).attr("behavior") == 'delete'){
					if (!result){
						$(this).remove();
					}
				}else{
					if("button" == $(this).attr("type") || "submit" == $(this).attr("type")){
						if($(this).attr("behavior") == 'disable'){
							$(this).attr("disabled",(result?false:true));		
						 }else{
							 if (result){
								 $(this).show();
							 }else{
								 $(this).hide();
							 }
						 }
					}else{
						if (result){
							 $(this).show();
						 }else{
							 $(this).hide();
						 }
					}
				}
				
		},
		run:function(options){
			var _this = this;
			$.extend(_this.options,options);
			var resKeyArray = [];
			var operKeyArray = [];
			var targetObjArray = [];
			var cache = $.data(document.body,'PermCheck') || {};
			//获取权限检查数组
			$('[permCheck]').each(function(){
				var permCheckValue = $(this).attr("permCheck");
				var arr=permCheckValue.split(",");
		        var resKey;
		        var operKey;
		        var targetObj = this;
		        var behavior;
		        if(arr.length==1){
		            resKey=_this.options.resKey;
		            operKey=arr[0];
		            behavior="hidden";
		        }else if(arr.length==3){
		           	resKey=arr[0];
		            operKey=arr[1];
		            behavior=arr[2];
		        }else{
		            if(arr[1]=='hidden' ||arr[1]=='disable'){
		                resKey=_this.options.resKey;
		               	operKey=arr[0];
		                behavior=arr[1];
		            }else{
		                resKey=arr[0];
		               	operKey=arr[1];
		                behavior="hidden";
		            }
		        }
		        var _result = cache[permCheckValue];
		       if (_result != null && _result != 'undefined'){
		    	   _this._setBehavior.call(targetObj, _result);
		       }else{
		    	   resKeyArray.push(resKey);
				   operKeyArray.push(operKey);
				   $(targetObj).attr("behavior", behavior);
				   targetObjArray.push(targetObj);
		       }
			});
			
			
			//定义回调函数
			var cb = function(r){
				if(r.success){
					var data = r.data;
					for(var i=0;i<data.length;i++){
						_this._setBehavior.call(targetObjArray[i], data[i].result);
						cache[$(targetObjArray[i]).attr('permCheck')] = data[i].result;
						/**if(data[i].result==false){
							if($(targetObjArray[i]).attr("behavior")=='disable'){
								_this._setBehavior.call(targetObjArray[i], 'disable');
							}else{
								_this._setBehavior.call(targetObjArray[i]);
							}
						}**/
					}
					$.data(document.body,'PermCheck',cache);
					if (_this.options['callback'] && typeof _this.options.callback == 'function'){
						_this.options.callback.call(this,data);
					}
				}
				
			};
			
			//发请求到后台批量检查权限
			if(resKeyArray.length > 0){
				var resKeys = resKeyArray.join(',');
				var operKeys = operKeyArray.join(',');
				/**$.iframeSubmit({
					url : _this.options.url,
					params : {'resKeys':resKeys,'operKeys':operKeys},
					callback : cb
				});**/
				getScript(_this.options.url,{'resKeys':resKeys,'operKeys':operKeys},cb);
				//$.post(_this.options.url,{'resKeys':resKeys,'operKeys':operKeys},cb);
			}else{
				if (_this.options['callback'] && typeof _this.options.callback == 'function'){
					_this.options.callback.call(this,[]);
				}
			}
			
		}
	};
	
	$.permCheck = new PermCheck(); // singleton instance
	$.permCheck.initialized = true;
	$.permCheck.setDefaults =  function(){
		var values = {};
		if (arguments.length === 2) {
		      // allow passing of single key/value...
		      values[arguments[0]] = arguments[1];
		    } else {
		      // ... and as an object too
		      values = arguments[0];
		    }
		    $.extend(this.options, values);
	};
})(jQuery);



//从plupload-2.1.2/js/i18n/zh_CN.js复制而来
plupload.addI18n({"Stop Upload":"停止上传","Upload URL might be wrong or doesn't exist.":"上传的URL可能是错误的或不存在。","tb":"tb","Size":"大小","Close":"关闭","Init error.":"初始化错误。","Add files to the upload queue and click the start button.":"将文件添加到上传队列，然后点击”开始上传“按钮。","Filename":"文件名","Image format either wrong or not supported.":"图片格式错误或者不支持。","Status":"状态","HTTP Error.":"HTTP 错误。","Start Upload":"开始上传","mb":"mb","kb":"kb","Duplicate file error.":"重复文件错误。","File size error.":"文件大小错误。","N/A":"N/A","gb":"gb","Error: Invalid file extension:":"错误：无效的文件扩展名:","Select files":"选择文件","%s already present in the queue.":"%s 已经在当前队列里。","File: %s":"文件: %s","b":"b","Uploaded %d/%d files":"已上传 %d/%d 个文件","Upload element accepts only %d file(s) at a time. Extra files were stripped.":"每次只接受同时上传 %d 个文件，多余的文件将会被删除。","%d files queued":"%d 个文件加入到队列","File: %s, size: %d, max file size: %d":"文件: %s, 大小: %d, 最大文件大小: %d","Drag files here.":"把文件拖到这里。","Runtime ran out of available memory.":"运行时已消耗所有可用内存。","File count error.":"文件数量错误。","File extension error.":"文件扩展名错误。","Error: File too large:":"错误: 文件太大:","Add Files":"增加文件"});

/**
 * 对Pluploader附件上传插件按照业务需求做简单封装。
 * @class
 * @example
 * html:
 * 		<div id="uploadFileDiv"></div>
 * js: 
 * 		var pluploaderObj = new PlUploaderObj('uploadFileDiv');
 *		pluploaderObj.init({
 *					'btnName' : '上传文件',
 *				'btnWidth' : '100px',
 *				'hiddenName' : 'product.productGroupId',
 *				'attachTypeId' : 'qm_pro_file',
 *				'max_file_size' : '10M'
 *			});
 **/
var PlUploaderObj = function(contenterId) {
	this._contenterId = contenterId;
	this.$content = $('#' + this._contenterId);
	this._uploadFileBtnId = 'uploadBtn_' + this._contenterId;
	this._hiddenId  = 'hidden_' + this._contenterId;
	this._attachGroupId = 'hidden_groupid_' + this._contenterId;
	this._uploadUrl = '';
	this._uploadId = 'uploading_' + this._contenterId;
	this._count = 0;
	//this._uploadFileBtnId = 'uploadFileBtn';
	this._attachFileIds = [];
	this._add_attachFileIds = [];
	this._del_attachFileIds = [];
	//this._attachGroupId = '';
	//this._hiddenId = '';
	this._isInit = false;
	this.uploader = null;
	this._options = {
		btnName: '上传附件',
		btnWidth : '200',
		max_file_size: '3mb',
		max_file: 1,
		mime_types:[],
		addUrl: ctxPaths + '/attachment/add.ajax',
		mutilAddUrl : ctxPaths + '/attachment/add.ajax',
		delUrl: ctxPaths + '/attachment/withdraw.ajax',
		downloadUrl: ctxPaths + '/attachment/download.ajax',
		viewUrl:ctxPaths + '/attachment/view.ajax',
		viewTypeUrl: ctxPaths + '/attachment/viewType.ajax',
		listUrl: ctxPaths + '/attachment/list.ajax',
		attachGroupId : '',
		disTheme: 1,
		imgW : 100,//当disTheme为2有效
		imgH : 100,//当disTheme为2有效
		maxImgW : 0,
		maxImgH : 0,
		hiddenName : '', //附件组ID
		hiddenAttachId : '',//附件ID，多个用逗号隔开，多附件该参数不能为空
		hiddenAttr :null,
		attachTypeId:'',
		isView : false,
		uploadCompleteCallBack : null,
		deleteCompleteCallBack:null,
		initData: []
	};
};
PlUploaderObj.prototype = {
	destroy : function(){
		if (this.uploader){
			delete this.uploader;
		}
		this.$content.empty();
	},
	_disableBrowse: function(flag) { //控制上传按钮启用/禁用以及按钮样式
		var _this = this;
		_this.uploader.disableBrowse(flag);
		if (flag) {
			_this.$uploadFileBtn.removeClass('disabled').addClass('disabled');
		} else {
			_this.$uploadFileBtn.removeClass('disabled').addClass('disabled');
		}
	},
	_checkMaxSize: function() { //检查是否上传的附件总数已达到最大
		var _this = this;
		if (!_this.getSucc()){
			_this.$uploadFileBtn.parent().show();
				if (_this.uploader){
				_this.uploader.destroy();
				}
				_this._initPlUpload();
			return;
		}
		if (_this.getSucc().split(',').length < _this._options.max_file) {
			_this.$uploadFileBtn.parent().show();
				if (_this.uploader){
					_this.uploader.destroy();
				}
				_this._initPlUpload();
		} else {
			_this.$uploadFileBtn.parent('div').hide();
		}
		
	},
	_bindCloseItemEvent: function() { //绑定上传的附件关闭事件
		var _this = this;
		_this.$content.find('.closeitem').unbind().bind("click",function() {
			var o = $(this).parent('div');
			if (o.attr('attachfileid')){
				$.ajaxSubmit(_this._options.delUrl,{'attachFileId':o.attr('attachfileid')},function(_data){
					if (_data && _data.success){
						_this._del_attachFileIds.push(o.attr('attachfileid'));
						o.fadeOut(500, function() {
							o.remove();
							_this._checkMaxSize();
							_this._initHiddenName();
						});
						if(_this._options.deleteCompleteCallBack!=null){
							_this._options.deleteCompleteCallBack();
						}
					}else{
						alert('删除失败，请稍候再试!');
					}
				});
			}else{
				o.fadeOut(500, function() {
					o.remove();
					_this._checkMaxSize();
					_this._initHiddenName();
				});
			}
		});
	},
	_changeParam: function(url, name, value) {
		var newUrl = "";
		var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
		var tmp = name + "=" + value;
		if (url.match(reg) != null) {
			newUrl = url.replace(eval(reg), tmp);
		} else {
			if (url.match("[\?]")) {
				newUrl = url + "&" + tmp;
			} else {
				newUrl = url + "?" + tmp;
			}
		}
		return newUrl;
	},
	_initDatas: function(initData) { //初始化已上传的附件信息
		var _this = this,_options = this._options;
		initData = initData || [];
		if (initData.length > 0){
				for (var i = 0; i < initData.length; i++) {
						if (!initData[i] || initData[i] == null || !initData[i].attachFileId)continue;
						_this.$content.attr('attachGroupId', initData[i].attachGroupId);
						if (_options.disTheme == 2) {
							_this.$content.append('<div class="uploading" attachfileid="' + initData[i].attachFileId + '" style="width:'+(_options.imgW + 10)+'px;height:'+(_options.imgH + 10)+'px;"><img width="'+_options.imgW + 'px" height="'+_options.imgH + 'px" _w="'+_options.imgW+'px" _h="'+_options.imgH+'px" onload="autoResizeImage(this)"  src="' + (_options.viewUrl + '?attachFileId=' + initData[i].attachFileId) + '"/>' + (!_this._options.isView ? '<div  class="closeitem"> <a href="javascript:;"><i class="ace-icon fa fa-times red2"></i></a>':'') + ' </div>');
						} else {
							_this.$content.append('<div class="uploading" attachfileid="' + initData[i].attachFileId + '"><div class="uploadcontent"><div class="uploadfile"><a href="'+(_this._options.downloadUrl + "?attachFileId=" + initData[i].attachFileId) + '" target="_blank" title="' + initData[i].fileName + '">' + initData[i].fileName + '</a></div></div>'+(!_this._options.isView ? '<div  class="closeitem"> <a href="javascript:;"><i class="ace-icon fa fa-times red2"></i></a>':'') + '</div></div>');
						}
				 }
				if (!_options.isView){
					_this._initHiddenName();
					_this._bindCloseItemEvent();
				}
		}
		if (!_options.isView){
			_this._checkMaxSize();
		}
	},
	_initHiddenName : function(){
		if (this._options.max_file > 1){
			this.$hiddenField.val(this.getSucc());	
			$('#' + this._attachGroupId).val(this.getGroupId());
		}else{
			if (this.getSucc()){
				this.$hiddenField.val(this.getGroupId());	
			}else{
				this.$hiddenField.val('');	
			}
		}
		this.$hiddenField.trigger('blur');
		this.$content.attr('attachFiles',this.getAttachFiles());
	},
	_addAttacheTypeParam:function(){
		var params = "attachTypeId="+this._options.attachTypeId;
		if (this._options.max_file > 1){
			params += '&attachGroupId=' + this.getGroupId() + '&attachFileIds=' + this.getSucc();
			this._uploadUrl = this._options.mutilAddUrl;
		}else{
			this._uploadUrl = this._options.addUrl;
		}
		if(this._options.addUrl.indexOf("?") == -1){
	        this._uploadUrl += "?" + params;
	    }else{
	    	this._uploadUrl += "&" + params;
	    }
	},
	_initPlUpload : function(){
		var _this = this;
		_this._addAttacheTypeParam();//byhaomingli
		_this.uploader = new plupload.Uploader({
			runtimes: 'html5,flash,gears,silverlight,html4',
			browse_button: _this._uploadFileBtnId, // you can pass in id...
			container: _this._contenterId, // ... or DOM Element itself
			url: _this._uploadUrl,
			multi_selection: false,
			chunk_size: '1mb',//后台不支持，去掉
			multipart:true,
		    urlstream_upload : true,  
			flash_swf_url: ctxPaths +'/js/plupload-2.1.2/js/Moxie.swf',
			silverlight_xap_url:ctxPaths +'/js/plupload-2.1.2/js//Moxie.xap',
			filters:{max_file_size: _this._options.max_file_size,mime_types: _this._options.mime_types},
			file_data_name:'attachment',
			headers : {'attachTypeId':_this._options.attachTypeId},
			init: {
			PostInit: function() {

			},
			FilesAdded: function(up, files) {
				_this.$uploadFileBtn.parent('div').hide();
				plupload.each(files, function(file) {
					_this.$content.append('<div class="uploading"> <div class="uploadcontent"> <div class="uploadfile"><a href="javascript:;" title="' + file.name + '">' + file.name + '</a></div>    <div data-percent="0%" class="progress progress-mini progress-striped active" style="margin-top:24px;margin-bottom:-2px;clear:both"><div style="width:0%;" class="progress-bar"></div></div>                         </div> <div class="closeitem"> <a href="javascript:;"><i class="ace-icon fa fa-times red2"></i></a> </div></div>');
				});
				_this._bindCloseItemEvent();
				up.refresh();
				_this.uploader.disableBrowse(true);
				_this.uploader.start();
			},

			UploadProgress: function(up, file) {
				_this.$content.find('.progress:last').attr('data-percent',file.percent + '%');
				_this.$content.find('.progress-bar:last').width(file.percent + '%');
			},
			UploadComplete: function(up, file) {
				if(_this._options.uploadCompleteCallBack!=null){
					_this._options.uploadCompleteCallBack();
				}
			},
			FileUploaded: function(up, file, res) {
			 var result = {'success':false};
			 if (res.response){
				try{
					result = eval(("(" + res.response + ")"));
				}catch(err){
					result = {'success':false,'message':'发生未知错误'};
				}
			 }
			if (result.success){
				var _options = _this._options;
				_this._add_attachFileIds.push(result.data.attachFileId);
				_this.$content.find('.progress:last').remove();
				_this.$content.find('.uploading:last').attr('attachfileid', result.data.attachFileId);
				_this.$content.attr('attachGroupId', result.data.attachGroupId);
				_this._initHiddenName();
				up.refresh();
				_this._bindCloseItemEvent();
				_this._checkMaxSize();
				_this.$hiddenField.trigger('blur');
				if (_this.uploader)_this.uploader.disableBrowse(false);
				if (_this._options.disTheme == 2) {
					_this.$content.find('.uploadcontent:last').remove();
					_this.$content.find('.uploading:last').css('width', _options.imgW + 10).css('height', _options.imgH + 10).append('<img style="display:none" _w="'+_options.imgW + 'px" _h="'+_options.imgH + 'px" onload="checkImgMaxWidthAndHeight(this,'+_options.maxImgW+','+_options.maxImgH+',\''+_this._contenterId+'\')" src="'+(_this._options.viewUrl + "?attachFileId=" + result.data.attachFileId) + '"/>');
				}else{
					_this.$content.find('.uploadfile a:last').attr('href',(_this._options.downloadUrl + "?attachFileId=" + result.data.attachFileId)).attr("target","_blank");
				}
				
			}else{
				alert(result.message);
				_this.$content.find('.uploading:last').remove();
				up.refresh();
				if (_this.uploader)_this.uploader.disableBrowse(false);
				_this._checkMaxSize();
			}
			
			
			},
			Error: function(up, err) {
				alert(err.message);
				_this.$content.find('.uploading:last').remove();
				up.refresh();
				if (_this.uploader)_this.uploader.disableBrowse(false);
				_this._checkMaxSize();
			}
		  }
		});
		_this.uploader.init();
	},
	_init : function(){
		var _this = this;
		
		if (_this.$content.data('_init')){
			_this.destroy();
		}
		_this.$content.empty();
		_this.$content.data('_init',true);
		
		var _options = _this._options;
		if (!_options.isView){
			_this.$content.append('<div style="float:left;position: relative;"><button type="button" style="display:inline;width:200px;" id="' + _this._uploadFileBtnId + '" class="btn btn-purple btn-sm">' + _this._options.btnName + '<i class="ace-icon fa fa-cloud-upload icon-on-right bigger-110"></i></button></div>');
			if (_options.max_file > 1){
				_this.$content.append('<input style="display:none" type="text" '+ (_this._options.hiddenAttachId ? 'name="' + _this._options.hiddenAttachId + '"' : '') +' id="' + _this._hiddenId  + '"/><input style="display:none" type="text" '+ (_this._options.hiddenName ? 'name="' + _this._options.hiddenName + '"' : '') +' id="' + _this._attachGroupId  + '"/>');
			}else{
				_this.$content.append('<input style="display:none" type="text" '+ (_this._options.hiddenName ? 'name="' + _this._options.hiddenName + '"' : '') +' id="' + _this._hiddenId  + '"/>');
			}
			if (_this._options.hiddenAttr){
				for (var obj in this._options.hiddenAttr){
					_this.$hiddenField.attr(obj,this._options.hiddenAttr[obj]);
				 }
			}
			_this.$uploadFileBtn = $('#' + _this._uploadFileBtnId);
			_this.$hiddenField = $('#' + _this._hiddenId);
		}
		if (Object.prototype.toString.call(_options.initData) === "[object String]" && _options.initData !==''){
			$.ajaxSubmit(_this._options.listUrl,{attachGroupId:_this._options.initData},function(rtn){
				_this._initDatas(rtn.data);
			});
		}else{
			_this._initDatas( _options.initData);
		}
		
	},
	init: function(ops) {
		var _this = this;
		$.extend(_this._options, ops || {});
		if (_this._options.attachTypeId){
				$.ajaxSubmit(_this._options.viewTypeUrl,{attachTypeId:_this._options.attachTypeId},function(rtn){
					var _config = {};
					if (rtn && rtn.success && rtn.data){
						var _data = rtn.data;
						_config = {
								max_file_size:(_data.singleLimitSize/1024 + 'KB'),
								max_file:_data.limitCount
						};
						if (_data.limitSuffix){
							var _suffix = _data.limitSuffix.replaceAll(';',',');
							$.extend(_config, {mime_types : [{
								title : _suffix,
								extensions : _suffix
							}]});
						}
					}
					$.extend(_this._options, _config);
					_this._init();
				});
		}else{
			_this._init();
		}
	},
	getGroupId: function() {
		return this.$content.attr('attachGroupId')?this.$content.attr('attachGroupId'):'';
	},
	getAttachFiles: function() {
		var succ = [];
		this.$content.find('.uploading').each(function(i) {
			if ($(this).attr('attachfileid')) {
				succ.push($(this).attr('attachfileid') + "|" + $(this).find('.uploadfile a').text());
			}
		});
		return succ.join(',');
	},
	getSucc: function() {
		var succ = [];
		this.$content.find('.uploading').each(function(i) {
			if ($(this).attr('attachfileid')) {
				succ.push($(this).attr('attachfileid'));
			}
		});
		return succ.join(',');
	}
};
/**
* Logo图片附件转换为图片显示效果
*/
;
(function($){
var downLoadAttachUrl = ctxPaths + '/attachment/view.ajax';
$.fn.extend({
	createAttachLogo:function(_options){
	   var _default = {
    		width :100,
    		height:100
	  };
	   
      $.extend(_default,_options||{});
		return this.each(function( j ) {
			var attachFileId = $(this).find('span[isAttachId]').text();
			var attachName = $(this).find('span[isAttachName]').text();
			if($.trim(attachFileId) != '' && $.trim(attachName) != ''){
				var aHtml = '<img src="' + $.appendExtraParams(downLoadAttachUrl + '?attachFileId=' + attachFileId) + '" width="'+_default.width+'px" height="'+_default.height+'px" onload="autoResizeImage(this)"/>';
				$(this).empty().append(aHtml);
			}else{
				//var aHtml = '<img src="images/mobilelogo.gif" width="'+_default.width+'px" height="'+_default.height+'px"/>';
				$(this).empty();
			}
			
		});
	}
});	
	
})(jQuery);

/*
 *创建图片预览，适合多附件
 *htmlId:需要创建下载链接的页面元素ID
 *list:附件数据结果集
 */
 function createAttachLogo($content,list){
	 if(list ==null){
		 return;
	 }
	 var _default = {
	    		width :100,
	    		height:100
	};
	if (Object.prototype.toString.call(list) === "[object String]" && list !==''){
		var listUrl = ctxPaths + '/attachment/list.ajax';
			$.ajaxSubmit(listUrl,{attachGroupId:list},function(rtn){
				_init(rtn.data);
			});
	}else{
		_init(list);
	}
	function _init(data){
		var preUrl = ctxPaths + '/attachment/view.ajax';
	     var links = [];
	     if(data){
	         var flen = data.length;
	         if(flen>0){
	           for(var i=0;i<flen;i++){
	               var item = data[i];
	               var fileId = item.attachFileId;
	               links.push('<img src="' +preUrl + '?attachFileId=' + fileId + '" width="'+_default.width+'px" height="'+_default.height+'px" onload="autoResizeImage(this)"/>&nbsp;');
	            }
	         }
	     }
	     $content.empty().html(links.join(''));
	}
     
}
/*
 *创建附件下载链接，适合多附件
 *htmlId:需要创建下载链接的页面元素ID
 *list:附件数据结果集
 */
 function createDownloadLink($content,data){
	 if(data ==null){
		 return;
	 }
	 if (Object.prototype.toString.call(data) === "[object String]"
			&& data !== '') {
		var listUrl = ctxPaths + '/attachment/list.ajax';
		$.ajaxSubmit(listUrl, {
			attachGroupId : data
		}, function(rtn) {
			_init(rtn.data);
		});
	} else {
		_init(data);
	}
	function _init(list){
		 var preUrl = ctxPaths + '/attachment/download.ajax';
	     var links = [];
	     if(list){
	         var flen = list.length;
	         if(flen>0){
	           for(var i=0;i<flen;i++){
	               var item = list[i];
	               var fileId = item.attachFileId;
	               var fileName = item.fileName;
	               var durl = preUrl+"?attachFileId="+fileId;
	               links.push("<a href='"+durl+"' target='_blank' title='"+fileName+"'>"+fileName+"</a>&nbsp;");
	            }
	         }
	     }
	     $content.empty().html(links.join(''));
	 }
     
}
function autoResizeImage(objImg){
	var img = new Image();
	img.src = objImg.src;
	var w = img.width;
	var h = img.height;
	var Ratio= w/h;
	var _w = $(objImg).attr("width") || 100,_h = $(objImg).attr("height") || 100;
	if (Ratio>1){
		$(objImg).css('width',_w);  //如果图片宽度大于高度，控制宽度，高度自适应缩放
		$(objImg).removeAttr("height");
	}else{
		$(objImg).css('height',_h); //如果图片高度大于宽度，控制高度，宽度自适应缩放
		$(objImg).removeAttr("width");
	}
	objImg.style.display = "block";
}
function checkImgMaxWidthAndHeight(obj,maxImgW,maxImgH,contentId){
	var _w = obj.naturalWidth || $(obj).width(),_h = obj.naturalHeight || $(obj).height();
	if ((maxImgW > 0 && _w != maxImgW) || (maxImgH > 0 && _h != maxImgH)){
		$(obj).closest('.uploading').find('.closeitem').trigger('click');
		alert("上传的图片尺寸必须为" + maxImgW +"*" + maxImgH + "px");
	}else{
		autoResizeImage(obj);
	}
}
function setPositionInIfm($obj,dialog){
	var url = getCookie('admin-domain'),ticket = getCookie('AUTH_TICKET');
	if (url){
		/**try{
			getScript(url + '/auth/ifmStyle.ajax?ticket=' + ticket,null,function(rtn){
				if (rtn && rtn.success && rtn.data){
					var style = rtn.data,winWh = $._getViewPort(),w = $obj.width(),h = $obj.height();
					var l = winWh['width'] / 2 - w / 2 + document.documentElement.scrollLeft;
					var t = (style['winH'] - style['ifmTop'])/ 2 - h / 2  + style['scrollTop'] - style['ifmTop'];
					$obj.css({
						left: l,
						top: t,
						height:138,
						margin:0,
						position: 'absolute'
					});
					dialog.modal("show");
				}else{
					dialog.modal("show");
				}
			});
		}catch(e){
			dialog.modal("show");
		}**/
		window._$obj = $obj;
		window._$dialog = dialog;
		var href = window.location.href,
		firstInd = href.indexOf('//') + 2,
		referrer = href.substring(0,firstInd),
		firstSubStr = href.substring(firstInd,href.length),
		ind = firstSubStr.indexOf('/');
		referrer += firstSubStr.substring(0,ind);
		referrer += ctxPaths + '/ifm_style_bridge_sub.html';
		var src = url + "ifm_style_bridge.html?referrer=" + referrer;
		if (!window.frames["ifm_style_bridge"]){
			$('body').append('<iframe id="ifm_style_bridge" name="ifm_proxy" src="" width="0" height="0" style="display:none;" ></iframe>');
		}
		document.getElementById('ifm_style_bridge').src = src;
	}else{
		dialog.modal("show");
	}
};
function setPositionInIfmCallBack(val){
	if (!val)return;

	var vals = val.split('&'),len = vals.length,style = {};
	for (var i = 0; i < len; i ++){
		var param = vals[i],params = param.split('=');
		if (params.length == 2){
			style[params[0]] = parseInt(params[1],10);
		}
		
	}
	var $obj = window._$obj,dialog = window._$dialog,winWh = $._getViewPort(),w = $obj.width(),h = $obj.height() || 138;
	var l = winWh['width'] / 2 - w / 2 + document.documentElement.scrollLeft;
	var t = (style['winH'] - style['ifmTop'])/ 2 - h / 2  + style['scrollTop'] - style['ifmTop'];
	$obj.css({
		left: l,
		top: t,
		height:138,
		margin:0,
		position: 'absolute'
	});
	dialog.modal("show");
}
function goBack(){
	location.href = document.referrer;
}
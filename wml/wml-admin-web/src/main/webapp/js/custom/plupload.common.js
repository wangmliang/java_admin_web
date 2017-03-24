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
	this._uploadFileBtnId = 'uploadFileBtn';
	this._attachFileIds = [];
	this._add_attachFileIds = [];
	this._del_attachFileIds = [];
	this._attachGroupId = '';
	this._hiddenId = '';
	this._isInit = false;
	this.uploader = null;
	this._options = {
		btnName: '上传附件',
		btnWidth : '100px',
		max_file_size: '3mb',
		max_file: 1,
		mime_types:[],
		addUrl: ctxPaths + '/attachment/add.ajax',
		mutilAddUrl : ctxPaths + '/attachment/addForMulti.ajax',
		delUrl: ctxPaths + '/attachment/withdraw.ajax',
		downloadUrl: ctxPaths + '/attachment/download.ajax',
		viewUrl:ctxPaths + '/attachment/view.ajax',
		viewTypeUrl: ctxPaths + '/attachment/viewType.ajax',
		listUrl: ctxPaths + '/attachment/list.ajax',
		attachGroupId : '',
		disTheme: 1,
		imgW : 100,//当disTheme为2有效
		imgH : 100,//当disTheme为2有效
		hiddenName : '', //附件组ID
		hiddenAttachId : '',//附件ID，多个用逗号隔开，多附件该参数不能为空
		hiddenAttr :null,
		attachTypeId:'',
		isView : false,
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
				this.$hiddenField.trigger('blur');
			}
		}
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
				if (_this._options.disTheme == 2) {
					_this.$content.find('.uploadcontent:last').remove();
					_this.$content.find('.uploading:last').css('width', _options.imgW + 10).css('height', _options.imgH + 10).append('<img width="'+_options.imgW + 'px" height="'+_options.imgH + 'px" _w="'+_options.imgW + 'px" _h="'+_options.imgH + 'px" onload="autoResizeImage(this)" src="'+(_this._options.viewUrl + "?attachFileId=" + result.data.attachFileId) + '"/>');
				}else{
					_this.$content.find('.uploadfile a:last').attr('href',(_this._options.downloadUrl + "?attachFileId=" + result.data.attachFileId)).attr("target","_blank");
				}
				_this.$content.find('.uploading:last').attr('attachfileid', result.data.attachFileId);
				_this.$content.attr('attachGroupId', result.data.attachGroupId);
				_this._initHiddenName();
			}else{
				alert(result.message);
				_this.$content.find('.uploading:last').remove();
			}
			up.refresh();
			_this._bindCloseItemEvent();
			_this._checkMaxSize();
			_this.$hiddenField.trigger('blur');
			if (_this.uploader)_this.uploader.disableBrowse(false);
			
			},
			Error: function(up, err) {
				alert(err.message);
				_this.$content.find('.uploading:last').remove();
				up.refresh();
				if (_this.uploader)_this.uploader.disableBrowse(false);
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
			_this.$content.append('<div  '+(_this._options.btnWidth?'style="float:left;width:'+_this._options.btnWidth+'"':'')+'><button type="button" id="' + _this._uploadFileBtnId + '" class="btn btn-purple btn-sm">' + _this._options.btnName + '<i class="ace-icon fa fa-cloud-upload icon-on-right bigger-110"></i></button></div>');
			if (_options.max_file > 1){
				_this.$content.append('<input type="hidden" '+ (_this._options.hiddenAttachId ? 'name="' + _this._options.hiddenAttachId + '"' : '') +' id="' + _this._hiddenId  + '"/><input type="hidden" '+ (_this._options.hiddenName ? 'name="' + _this._options.hiddenName + '"' : '') +' id="' + _this._attachGroupId  + '"/>');
			}else{
				_this.$content.append('<input type="hidden" '+ (_this._options.hiddenName ? 'name="' + _this._options.hiddenName + '"' : '') +' id="' + _this._hiddenId  + '"/>');
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
/*
 *创建附件下载链接，适合多附件
 *htmlId:需要创建下载链接的页面元素ID
 *list:附件数据结果集
 */
 function createDownloadLink(htmlId,list){
	 if(list ==null){
		 return;
	 }
     var preUrl = ctxPaths + '/attachment/download.ajax';
     var links = [];
     if(list){
         var flen = list.length;
         if(flen>0){
           for(var i=0;i<flen;i++){
               var item = list[i];
               var fileId = item.fileId;
               var fileName = item.fileName;
               var durl = preUrl+"?attachFileId="+fileId;
               links.push("<a href='"+durl+"' target='_blank' title='"+fileName+"'>"+fileName+"</a>&nbsp;");
            }
         }
     }
   $('#'+htmlId).empty().html(links.join(''));
}
function autoResizeImage(objImg){
	var img = new Image();
	img.src = objImg.src;
	var w = img.width;
	var h = img.height;
	var Ratio= w/h;
	var _w = $(objImg).attr("_w") || 100,_h = $(objImg).attr("_h") || 100;
	if (Ratio>1){
		$(objImg).css('width',_w);  //如果图片宽度大于高度，控制宽度，高度自适应缩放
		$(objImg).removeAttr("height");
	}else{
		$(objImg).css('height',_h); //如果图片高度大于宽度，控制高度，宽度自适应缩放
		$(objImg).removeAttr("width");
	}
}
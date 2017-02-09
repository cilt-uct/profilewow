var panelId;
function setPanelId(thisid)
{
  panelId = thisid;
}
function showHideDivBlock(hideDivisionNo, context)
{
  var tmpdiv = hideDivisionNo + "__hide_division_";
  var tmpimg = hideDivisionNo + "__img_hide_division_";
  var divisionNo = getTheElement(tmpdiv);
  var imgNo = getTheElement(tmpimg);
  if(divisionNo)
  {
    if(divisionNo.style.display =="block")
    {
      divisionNo.style.display="none";
      if (imgNo)
      {
        imgNo.src =  "../images/right_arrow.gif";
       }
    }
    else
    {
      divisionNo.style.display="block";
      if(imgNo)
      {
        imgNo.src = "../images/down_arrow.gif";
      }
    }
    if(panelId != null)
    {
      setMainFrameHeight(panelId);
    }
  }
}
function showHideDiv(hideDivisionNo, context)
{
  var tmpdiv = hideDivisionNo + "__hide_division_";
  var tmpimg = hideDivisionNo + "__img_hide_division_";
  var divisionNo = getTheElement(tmpdiv);
  var imgNo = getTheElement(tmpimg);

  if(divisionNo)
  {
    if(divisionNo.style.display =="block" || divisionNo.style.display =="table-row")
    {
      divisionNo.style.display="none";
      if (imgNo)
      {
        imgNo.src = "../images/collapse.gif";
      }
    }
    else
    {
      if(navigator.product == "Gecko")
      {
        divisionNo.style.display="table-row";
      }
      else
      {
        divisionNo.style.display="block";
      }
      if(imgNo)
      {
        imgNo.src = "../images/expand.gif";
      }
    }
  }
}

function getTheElement(thisid)
{

  var thiselm = null;

  if (document.getElementById)
  {
    thiselm = document.getElementById(thisid);
  }
  else if (document.all)
  {
    thiselm = document.all[thisid];
  }
  else if (document.layers)
  {
    thiselm = document.layers[thisid];
  }

  if(thiselm)   
  {
    if(thiselm == null)
    {
      return;
    }
    else
    {
      return thiselm;
    }
  }
}

function check(field)
 {
    for (i = 0; i < field.length; i++) 
    {
        field[i].checked = true;
    }
 }
function unCheck(field)
{
    for (i = 0; i < field.length; i++) 
    {
        field[i].checked = false; 
    }
}

function toggleDisplay(obj) {
	resize();
	$("#" + obj).slideToggle("normal", resize);
	return;    
}

function toggleHide(obj){
	if(obj.innerHTML.match(/hide/i)){
		obj.innerHTML = obj.innerHTML.replace('Hide ', '');
	} else {
		obj.innerHTML = obj.innerHTML.replace(/(<.+>)([^<>]+)/i, "$1 Hide $2");
	}
}
function getScrollDist(obj){
	var curtop = 0;
	if (obj.offsetParent) {
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curtop += obj.offsetTop
		}
	}
	return curtop;
}
function selectDeselectCheckboxes(mainCheckboxId, myForm) {   
	var el = getTheElement(mainCheckboxId);
	var isChecked = el.checked;           
	for ( i = 0; i < myForm.elements.length; i++ ) {
		if (myForm.elements[i].type == 'checkbox' ) {
			myForm.elements[i].checked  = isChecked;                                               
		}
	}
}
function resetMainCheckbox(checkboxId) {
  mainCheckboxEl = getTheElement(checkboxId);
  if (mainCheckboxEl.checked = true) {
  	mainCheckboxEl.checked = false;
  }                                                  
}
// if the containing frame is small, then offsetHeight is pretty good for all but ie/xp.
// ie/xp reports clientHeight == offsetHeight, but has a good scrollHeight
function mySetMainFrameHeight(id)
{
	// run the script only if this window's name matches the id parameter
	// this tells us that the iframe in parent by the name of 'id' is the one who spawned us
	if (typeof window.name != "undefined" && id != window.name) return;

	var frame = parent.document.getElementById(id);
	if (frame)
	{

		var objToResize = (frame.style) ? frame.style : frame;

		var height; 
		
		var scrollH = document.body.scrollHeight;
		var offsetH = document.body.offsetHeight;
		var clientH = document.body.clientHeight;
		var innerDocScrollH = null;

		if (typeof(frame.contentDocument) != 'undefined' || typeof(frame.contentWindow) != 'undefined')
		{
			// very special way to get the height from IE on Windows!
			// note that the above special way of testing for undefined variables is necessary for older browsers
			// (IE 5.5 Mac) to not choke on the undefined variables.
 			var innerDoc = (frame.contentDocument) ? frame.contentDocument : frame.contentWindow.document;
			innerDocScrollH = (innerDoc != null) ? innerDoc.body.scrollHeight : null;
		}

		if (document.all && innerDocScrollH != null)
		{
			// IE on Windows only
			height = innerDocScrollH;
		}
		else
		{
			// every other browser!
			height = offsetH;
		}

		// here we fudge to get a little bigger
		//gsilver: changing this from 50 to 10, and adding extra bottom padding to the portletBody		
		var newHeight = height + 150;
		//contributed patch from hedrick@rutgers.edu (for very long documents)
		if (newHeight > 32760)
		newHeight = 32760;

		// no need to be smaller than...
		//if (height < 200) height = 200;
		objToResize.height=newHeight + "px";
	
		var s = " scrollH: " + scrollH + " offsetH: " + offsetH + " clientH: " + clientH + " innerDocScrollH: " + innerDocScrollH + " Read height: " + height + " Set height to: " + newHeight;

	}
}

function doAjax(messageId, topicId, self){
 	$(self).attr('src', '/library/image/sakai/spinner.gif');
	$.ajax({ type: "GET", url: "dfAjax", data: "action=markMessageAsRead&messageId=" + messageId + "&topicId=" + topicId,
      success: function(msg){
         if(msg.match(/SUCCESS/)){
     		setTimeout(function(){
              $(self).remove();
               $("#" + messageId).parents("tr:first").children("td").each(function(){this.innerHTML = this.innerHTML.replace(/unreadMsg/g, 'bogus'); });
            }, 500);
         } else {
            $(self).remove();
            $("#" + messageId).parents("tr:first").css("backgroundColor", "#ffD0DC");         
         }
      },
      error: function(){
         $(self).remove();
         $("#" + messageId).parents("tr:first").css("backgroundColor", "#ffD0DC");
      }
   });
	//$.ajax({type: "GET", url: location.href, data: ""});
	return false;
}

//Ajax mods to profleWow lovemore.nalube@uct.ac.za

	$(document).ready(function() {
	
	$('input[name=searchText]').on('keyup focusout', function(e) {
		var curValue = $(this).val();
		if (curValue.length < 4) {
			$(this).next().attr('disabled', 'disabled');
		}
		else {
			$(this).next().removeAttr('disabled');
		}
	});
	$('input[name=searchText]').trigger('keyup');
	$('a[rel*=facebox]').facebox();
	
	$('.searchForm').bind('submit', function(){
		$('.searchBtn').trigger('click');
		return false;
	});

		$('.profileImage img').tooltip({ 
		    delay: 0, 
		    showURL: false, 
		    bodyHandler: function() { 
			var fileName = this.src.substring(this.src.lastIndexOf('/') + 1);
			var reg = new RegExp('imageServlet[?]');
			if(reg.test(fileName))
		        return $("<div/>").text("UCT picture"); 
			else if(fileName == 'noimage.gif')
				return $("<div/>").text("Default picture");
			else
			return $("<div/>").text(fileName);
		    } 
		});
	
	$('.searchBtn').bind('click', function(){
		if($('.searchForm div'))
			$('.searchForm div').each(function(){
				$(this).remove();
			});
		if(!/\S/.test($('.searchForm').find('input[type=text]').val())){
			$('.searchForm').find('input[type=text]').focus();
			return false;
		}
		if($('.searchForm').find('input[type=text]').val().length < 4){
			$('.searchForm').append($('<div/>').text('Enter a longer name to start searching.').addClass('alertMessage'));
			$('.searchForm').find('input[type=text]').focus();
			return false;
		}
		if($('.success')){
			$('.success').fadeOut('fast');
		}		
		if($('.alertMessage')){
			$('.alertMessage').fadeOut('fast');
		}
		var elem = $('td[rel*=infoCell]');
		var elemHTML = elem.html();
		var n = 0;	
		var options = {
			beforeSend: function(){
				if($('#infoCell-backup')){}	
				else{
					$('body').append('<div id="infoCell-backup">'+elemHTML+'</div');
					$('#infoCell-backup').hide();					
				}
				$(this).attr('disabled', 'disabled');
				elem.html('<span class="loading">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Searching....</span>');
			},
			success: function(msg){
				frameGrow();
				var temp = document.createElement('html');
				temp.innerHTML = msg;
				var profileEl = temp.querySelector('.Mrphs-sakai-profilewow');
				if (profileEl) {
					$(profileEl)
						.find('.Mrphs-toolTitleNav__link')
						.each(function(i, el) {
							el.remove();
						});
					elem.html(profileEl.innerHTML);
					profile.search.init();
				}
				$(this).removeAttr('disabled');
				return false;	
			}
		}
		
		$('.searchForm').ajaxSubmit(options);
		return false;
	});

		var passOpts = {
			beforeSend: function(){
				//frameGrow();
				//alert($('.passwordForm').find('input[@type=password]').eq(0).attr('name'));
				var msgElem = $('#passwordMsg');		
				msgElem.hide();
				msgElem.removeClass();
				if(!/\S/.test($('.passwordForm').find('input[type=password]').eq(0).val())){
					$('.passwordForm').find('input[type=password]').eq(0).focus();
					msgElem.text('Enter a new password.');
					msgElem.addClass('alertMessage');
					msgElem.show();
					return false;
				}
				
				if($('.passwordForm').find('input[type=password]').eq(0).val() != $('.passwordForm').find('input[type=password]').eq(1).val()){
					$('.passwordForm').find('input[type=password]').focus();
					$('.success').hide();
					msgElem.text('Your passwords do not match.');
					msgElem.addClass('alertMessage');
					msgElem.show();
					return false;
				}

				msgElem.html('<span class="loading">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Processing....</span>');
				msgElem.show();					
				$(this).attr('disabled', 'disabled');
			},
			success: function(msg){
				var msgElem = $('#passwordMsg');
				msgElem.removeClass('alertMessage');
				msgElem.html($(msg).find('.success').html());
				msgElem.addClass('success');
				$(this).removeAttr('disabled');
				$('.passwordForm').find('input[type=password]').eq(0).val('');
				$('.passwordForm').find('input[type=password]').eq(1).val('');
				return false;	
				}
		}
		
		$('.passwordForm').ajaxForm(passOpts);

		$('.profileImage > a').click(function(){
			$('#changepic > a').click();
			return false;
		});
		
		$('#editProfileLink').on('click', function(e){
			e.preventDefault();
			if ($('#errorContainer').length > 0) {
				return;
			}
			if($('#infoCell-backup')){
				$('#infoCell-backup').remove();
			}
			if($('.success')){
				$('.success').fadeOut('fast');
			}
			var target = this.href;
			var elem = $('td[rel*=infoCell]');
			var elemHTML = elem.html();
			$.ajax({
				url: target,
				cache: false,
				beforeSend: function(){
					elem.html('Loading....');
					$('body').append('<div id="infoCell-backup">'+elemHTML+'</div');
					$('#infoCell-backup').hide();
				},
				success: function(msg){
					frameGrow();
					var temp = document.createElement('html');
					temp.innerHTML = msg;
					var profileEl = temp.querySelector('.Mrphs-sakai-profilewow');
					if (profileEl) {
						$(profileEl)
							.find('.Mrphs-toolTitleNav__link')
							.each(function(i, el) {
								$(el).remove();
							});
						elem.html(profileEl.innerHTML);
					}
					else {
						elem.html('Could not load profile editor. Please contact Vula Help team');
					}
					return false;
				}
			});
		});
		

			var picOpt = {
			beforeSend: function(){
				$('.success').each(function(){
					$(this).fadeOut('fast');
				});				
				$('.alertMessage').each(function(){
					$(this).fadeOut('fast');
				});
			},
   		 success: function(msg) {
			$(document).trigger('close.facebox');
			if ($('span.success').length === 0) {
				$('.portletBody').eq(0).prepend('<span class="success">' + $(msg).find('.success').html());
			}
			else {
				$('span.success')
					.css({opacity: 1})
					.slideDown(0);
			}
			 //$('.profileImage img').attr('src', $(msg).find('.profileImage img').attr('src'));
			setTimeout(function() {
				$('.success').animate({opacity: 0}, 500).slideUp('fast');
			}, 5000);
    	  }
 	 };
		
		$('#facebox .image img').tooltip({ 
		    delay: 0, 
		    showURL: false, 
		    bodyHandler: function() { 
			var fileName = this.src.substring(this.src.lastIndexOf('/') + 1);
			var reg = new RegExp('imageServlet[?]');
			if(reg.test(fileName))
		        return $("<div/>").text("UCT picture"); 
			else if(fileName == 'noimage.gif')
				return $("<div/>").text("Default picture");
			else
			return $("<div/>").text(fileName);
		    } 
		});

		$('#facebox').on('click', '.selectImage', function(e){
			var imgLink = $(this).find('img').attr('src');
			$('.profileImage img').attr('src', imgLink);
			//that.attr('class', that.attr('class') + ' imageSelected');
			$('.album').find('input[type*=radio]').each(function(){
				$(this).removeAttr('checked');
			});
			$(this).parent().find('input[type*=radio]').attr('checked', 'checked');
			$('#form').ajaxSubmit(picOpt);
			return false;
		});		
		
		$('#facebox').on('click', '.officialPic', function(){
			$.get('officialpic','',
				function(f){
					var form = $(f).find('form');
					if (form) {
						form.ajaxSubmit({
							success: function(msg){
								$(document).trigger('close.facebox');
								$('.portletBody').prepend('<span class="success">' + $(msg).find('.success').html());
								$('.profileImage img').attr('src', $(msg).find('.profileImage img').attr('src'));
								setTimeout(function() {
									$('.success').slideUp('fast');
								}, 5000);
								//$('.success div').bind('click', function(){
								//	$(this).parent().parent().slideUp('normal');
								}
						});
					}
					else 
					{alert('No form');}
					/*$.post(
						'officialpic',
						params,
						function(msg){
							 $(document).trigger('close.facebox');			 
							 $('.portletBody').prepend('<span class="success">' + $(msg).find('.success').html() + '<a href="#"><div name="closeMsg">close</div></a></span>');
							 $('.profileImage img').attr('src', $(msg).find('.profileImage img').attr('src'));
							 $('.success div').bind('click', function(){
								$(this).parent().parent().slideUp('normal');
							});	*/			
						return false;
						}
					//}
				);
			return false;
		});

		$('#facebox').on('click', '.removePicture', function(){
			$('input[type*=radio]').each(function(){
				$(this).removeAttr('checked');
			});
			$('.profileImage img').attr('src', $(this).find('img').attr('src'));
			$('#form').ajaxSubmit(picOpt);
			return false;
		});


		$('#facebox').on('click', '.selectOfficialImage', function(){
			$('.profileImage img').attr('src', $(this).find('img').attr('src'));
			var opts = {
				success: function(msg){
					$(document).trigger('close.facebox');
					//$('.profileImage img').attr('src', $(msg).find('.activeSelectedImage').attr('src'));
				}
			};
			$('.officialPicForm').ajaxSubmit(opts);
			return false;
		});

		
		function formSubmit(that){
			if ($('input[name*=fileupload1]').val()) {
				var reg = new RegExp("^.+\.(jpg|jpeg|png|gif|bmp)$","i");
	                        if ( reg.test($('input[name*=fileupload1]').val()) ) {
					$('#progress').html('&nbsp;');
					$('#progress').attr('class', 'loading');			
					return true;
        	                } else {
					$('#progress').addClass('alertMessage');
                        	        $('#progress').html('Only images allowed');
					return false;
                        	}
			}
			else {					
				return false;
			}
		}	
	
	}); 	
	
function showUpload(){
		document.getElementById('pictureUploader').style.display = '';
	}
	
function frameGrow(){
	try{
	    	var frame = parent.document.getElementById(window.name);
	  		$(frame).height(parent.document.body.scrollHeight + 270);
	}catch(e){}
}

var profile = (function(){
    return {
        search : {
            init : function(){
                //bind search navigation controls
                $('input[rel=nav]').bind('click', function(){
                    if($('#infoCell-backup')){
                        $('#infoCell-backup').remove();
                    }
                    if($('.success')){
                        $('.success').fadeOut('fast');
                    }
                    var target = $(this).attr('src'),
                    elem = $('td[rel*=infoCell]'),
                    elemHTML = elem.html();
                    $.ajax({
                        url: target,
                        cache: false,
                        beforeSend: function(){
                            elem.html('Loading....');
                            $('body').append('<div id="infoCell-backup">'+elemHTML+'</div');
                            $('#infoCell-backup').hide();
                        },
                        success: function(msg){
         //                   elem.html(msg);
                            return false;
                        }
                    });
                    return false;
                });

                var hide = function(){
                    //$('td[rel*=infoCell]').html($('#infoCell-backup').html());
                    window.location.href = '#top';
                    $('#infoCell-backup').remove();
                    var elem = $('td[rel*=infoCell]');
                        $.ajax({
                            url: window.location.href,
                            cache: false,
                            beforeSend: function(){
                                elem.html('Loading....');
                            },
                            success: function(msg){
                                elem.html($(msg).find('td[rel*=infoCell]').html());
                                return false;
                            }
                        });
                    return false;
                };
                $('input[class*=cancel]').bind('click', hide);
                $('.closeImg').bind('click', hide);
                $('a[rel*=facebox]').facebox();
            }
        }
    };
})($);

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

//fix for double click stack traces in IE - SAK-10625
//add Jquery if necessary
/*if(typeof($) == "undefined"){
   js = document.createElement('script');
   js.setAttribute('language', 'javascript');
   js.setAttribute('type', 'text/javascript');
   js.setAttribute('src','/library/js/jquery.js');
   document.getElementsByTagName('head').item(0).appendChild(js);
//document.write('<script type="text/javascript" src="/library/js/jquery.js"></script>');}
}
js = document.createElement('script');
js.setAttribute('language', 'javascript');
js.setAttribute('type', 'text/javascript');
js.setAttribute('src','/sakai-messageforums-tool/js/sak-10625.js');
document.getElementsByTagName('head').item(0).appendChild(js);*/

//Ajax mods to profleWow lovemore.nalube@uct.ac.za

	$(document).ready(function() { 
	$('a[rel*=facebox]').facebox();
	
	$('.searchForm').bind('submit', function(){
		$('.searchBtn').trigger('click');
		return false;
	});

	
	$('.searchBtn').bind('click', function(){
		if(!/\S/.test($('.searchForm').find('input[@type=text]').val())){
			$('.searchForm').find('input[@type=text]').focus();
			return false;
		}
		if($('.success')){
			$('.success').slideUp('fast');
		}
		var elem = $('td[rel*=infoCell]');
		var elemHTML = elem.html();		
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
				elem.html(msg);
				$(this).removeAttr('disabled');
				return false;	
				}
		}
		
		$('.searchForm').ajaxSubmit(options);
		return false;
	});
	
	/*$('.passwordForm').bind('submit', function(){
		$('.passwordBtn').trigger('click');
		return false;
	});	
	
	$('.passwordBtn').bind('click', function(){*/
		
		var passOpts = {
			beforeSend: function(){
				frameGrow();
				//alert($('.passwordForm').find('input[@type=password]').eq(0).attr('name'));
				var msgElem = $('#passwordMsg');		
				msgElem.hide();
		
				if(!/\S/.test($('.passwordForm').find('input[@type=password]').eq(0).val())){
					$('.passwordForm').find('input[@type=password]').eq(0).focus();
					msgElem.html('Enter a new password.');
					msgElem.addClass('alertMessage');
					msgElem.show();
					return false;
				}
				
				if($('.passwordForm').find('input[@type=password]').eq(0).val() != $('.passwordForm').find('input[@type=password]').eq(1).val()){
					$('.passwordForm').find('input[@type=password]').focus();
					$('.success').hide();
					msgElem.html('Your passwords do not match.');
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
				//msgElem.addClass('success');
				msgElem.html($(msg).find('.success'));
				$(this).removeAttr('disabled');
				$('.passwordForm').find('input[@type=password]').eq(0).val('');
				$('.passwordForm').find('input[@type=password]').eq(1).val('');
				return false;	
				}
		}
		
		$('.passwordForm').ajaxForm(passOpts);

	
	/*
	This method is for the "Edit profile" link on hover
	$('td[rel*=infoCell]').parent().hover(
	function(){$('.editProfileHover').toggle();},
	function(){$('.editProfileHover').toggle();}
	);
	
	
	//$('#hovermenu').hide();
		$('.profileImage > img').click(function(){
			$('#hovermenu').attr('class','hovermenuactive toplink');
			$('#hovermenu').fadeIn('fast');
			$('#hovermenu').hover(
					function(){},
					 function(){
						$('#hovermenu').fadeOut('fast');
					}
				);
		});
		
		
		$('.profileImage').hover(
		function(){
			//$('#hoverlink').removeAttr('class');
			$('#hoverlink').attr('class','hoverlinkactive toplink');
			$('#hoverlink').click(function(){
				
			});
		},
		function(){
				//$('#hoverlink').removeAttr('class');
				$('#hoverlink').attr('class','hoverlink');
			}
		);		
		
		
		$('#changepicOLD').bind('click', function(){
			$('#hovermenu').attr('class','hovermenuactive bottomlink');
			$('#hovermenu').fadeIn('fast');
			$('#hovermenu').hover(
					function(){},
					 function(){
						$('#hovermenu').fadeOut('fast');
					}
				);
		});	*/
		
		
		
		$('.profileImage > a').click(function(){
			$('#changepic > a').click();
			return false;
		});
		
		$('#editProfileLink').bind('click', function(){
			if($('#infoCell-backup')){
				$('#infoCell-backup').remove();
			}
			if($('.success')){
				$('.success').slideUp('fast');
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
				error: function(e){
					elem.html(elemHTML);
					return false;
				},
				success: function(msg){
					frameGrow();
					elem.html(msg);
					return false;
				}
			});
			return false;
		});
		
		
	}); 	
	
function showUpload(){
		document.getElementById('pictureUploader').style.display = '';
	}
	
function frameGrow(){
	    	var frame = parent.document.getElementById(window.name);
	  		$(frame).height(parent.document.body.scrollHeight + 270);
}

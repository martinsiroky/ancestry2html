var longFilter = false;

function printFooterText() {
	document.write("${msg('footer.text','<a href=\'http://sirsi.wz.cz/ancestry2html.php\' target=\'_blank\'>Ancestry2html ${g.generatorVersion}</a>',g.today,'<a href=\'http://ancestry.nethar.com\' target=\'_blank\'>Ancestry ${g.ancestryVersion}</a>')}");	
}

function show(idecko)
{
	$('#'+idecko).show();
}

function hide(idecko)
{
	$('#'+idecko).hide();
}

function filterPeople(array,count,skipIfLong) {
	if (skipIfLong && longFilter) {
		return;
	}

	var start = new Date();
	text=$("#namesFilter").val().toLowerCase();
	if (text.length==0) {
		showAllPeople(array,count);
		return;
	}
	window.document.styleSheets[2].disabled=false;
	j=0;

	for (i=0;i<count;i++) {
		if (array[i*2+1].toLowerCase().indexOf(text)==-1) {
			$("#"+array[i*2]).hide();
		} else {
			j++;
			$("#"+array[i*2]).show();
		}
	}
	$("#resultCount").html("\""+text+"\" (${msg('list.searchCount','"+j+"','"+i+"')})");
	$("h2.firstLetter").each(function() {
		/*var a = $(this).find("a").get(0);
		var aName = $(a).attr("name");*/
		if ($(this).next().find("li").filter(":visible").length==0) {
			$(this).hide();
			/*$("."+aName).hide();
			$(this).prev().hide();*/
		} else {
			$(this).show();
			/*$("."+aName).show();
			$(this).prev().show();*/
		}
	})

	var end = new Date();
	if (end-start>300) {
		$("#search").show();
		longFilter = true;
		if (skipIfLong) {
			showAllPeople(array,count);
		}
	}
}

function showAllPeople(array,count) {
	$("h2.firstLetter").show();
	window.document.styleSheets[2].disabled=true;
	
	for (i=0;i<count;i++) {
		document.getElementById(array[i*2]).style.display="list-item";
	}
}

function Person(id,lastname,maidenname,firstname) {
	this.id = id;
	this.firstname = firstname;
	this.maidenname = maidenname;
	this.lastname = lastname;
}

function checkNames() {
	var otherNamesString = $("#otherNames").val();
	var otherNamesStringArray = otherNamesString.split(";");
	var otherNames = new Array();

  for (i=0;i<otherNamesStringArray.length;i++) {
		var splitted = otherNamesStringArray[i].split(",");

		otherNames[i] = new Person(splitted[0],splitted[1],splitted[2],splitted[3]);
	}

	var myNamesString = $("#namesForCheck").val();
	var myNamesStringArray = myNamesString.split(";");
	var myNames = new Array();

  for (i=0;i<myNamesStringArray.length;i++) {
		var splitted = myNamesStringArray[i].split(",");

		myNames[i] = new Person(splitted[0],splitted[1],splitted[2],splitted[3]);
	}

	//-------------

	for (i=0;i<myNames.length;i++) {
		var family = 0;

		for (j=0;j<otherNames.length;j++) {
			lastNames = myNames[i].lastname == otherNames[j].lastname && myNames[i].lastname!="?";
			maidenNames = myNames[i].maidenname == otherNames[j].maidenname && myNames[i].maidenname!="?";
			if (lastNames || maidenNames || 
				(myNames[i].lastname == otherNames[j].maidenname && myNames[i].lastname!="?") || 
				(myNames[i].maidenname == otherNames[j].lastname && myNames[i].maidenname!="?")) {
				family=Math.max(1,family);
				if (lastNames || maidenNames) {
					family = Math.max(2,family);
					if (lastNames && maidenNames && myNames[i].firstname == otherNames[j].firstname && myNames[i].firstname!="?" ) {
						family = Math.max(3,family);	
					}
				}
			}
		}

		if (family>0) {
			$("#p"+myNames[i].id+" a").show().css("color",'rgb('+(105+family*50)+',0,0)');
		} else {
			$("#p"+myNames[i].id).hide();
		}

		window.document.styleSheets[2].disabled=false;
	}
}

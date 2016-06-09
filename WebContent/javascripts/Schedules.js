
function changeSchedule(inc) {
	var list = document.getElementById("tableschedules");
	var currNum = parseInt(list.attributes["name"].value);
	var listItem = list.getElementsByTagName("table");
	var list2 = document.getElementById("textschedules");
	var listItem2 = list2.getElementsByTagName("table");
	if (currNum + inc >= 0 && currNum + inc < listItem.length) {
		listItem[currNum].style.display = "none";
		listItem[currNum + inc].style.display = "table";
		list.attributes["name"].value = listItem[currNum + inc].id;
		listItem2[currNum].style.display = "none";
		listItem2[currNum + inc].style.display = "table";
		list2.attributes["name"].value = listItem[currNum + inc].id;
	}
}

function switchView(name) {
	var button1 = document.getElementById("view1");
	var button2 = document.getElementById("view2");
	
	if (name == "table") {
		button1.name = "text";
		button2.name = "text";
		document.getElementById("textschedules").style.display = "inline";
		document.getElementById("tableschedules").style.display = "none";
		button1.value = "Table View";
		button2.value = "Table View";
	}
	else {
		button1.name = "table";
		button2.name = "table";
		document.getElementById("tableschedules").style.display = "inline";
		document.getElementById("textschedules").style.display = "none";
		button1.value = "Text View";
		button2.value = "Text View";
	}
}
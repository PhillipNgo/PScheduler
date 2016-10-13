
function selectClass(subj, num) {
	var subjList = document.getElementById("subjects");
	subjList.value = subj;
	displayNums(false);
	var numList = document.getElementById("number");
	numList.value = num;
	addClass();
}


function addClass() {
	var table = document.getElementById("scheduledisplay");
	var subj = document.getElementById("subjects");
	var cName = document.getElementById("number");
	cName = cName.options[cName.selectedIndex].getAttribute("name");
	cName = cName.split("--");
	subj = subj.options[subj.selectedIndex].value;
	var num = document.getElementById("number").value;
	if (num.endsWith("H")){
		num = num.substring(num.length-5, num.length);
	}
	else {
		num = num.substring(num.length-4, num.length);
	}
	
	var name = subj + num;
	if (num == "none" || table.rows.length == 10) {
		return;
	}
	var space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	var row = table.insertRow(table.rows.length);
	row.id = name;
	row.insertCell(0).innerHTML = "<input name=\"" + name + "\"type=\"button\" value=\" X \" onclick=\"removeClass(this.name)\" class=\"xbutton\"></input>"
	row.cells[0].id = name;
	row.insertCell(1).innerHTML = subj + " " + num + space;
	row.insertCell(2).innerHTML = cName[0] + space;
	var select = document.createElement("select");
	select.id = "row" + (table.rows.length-1);
	for (var i = 0; i < cName[2].length; i++) {
		var type = cName[2].substring(i, i+1);
		var option = document.createElement("option");
		option.value = type;
		
		switch (type) {
			case "L": option.innerHTML = "Lecture";
					  break;
			case "B": option.innerHTML = "Lab";
				      break;
			case "C": option.innerHTML = "Recitation";
					  break;
			case "H": option.innerHTML = "Hybrid";
					  break;
			case "E": option.innerHTML = "Emporium";
					  break;
			case "O": option.innerHTML = "Online";
					  break;
			case "I": option.innerHTML = "Independent Study";
				      break;
			default:  option.innerHTML = "bug";
					  break;
		}
		select.add(option);
	}	
	if (cName[2].length > 1) {
		var optionA = document.createElement("option");
		optionA.value = "A";
		optionA.innerHTML = "Any";
		select.add(optionA);
	}
	row.insertCell(3).innerHTML = select.outerHTML + space;
	row.insertCell(4).innerHTML = cName[1] + "C";	
}

function removeClass(id) {
	var listItem = document.getElementById(id);
	listItem.parentNode.removeChild(listItem);
}

function addCRN() {
	var table = document.getElementById("scheduledisplay");
	var crn = document.getElementById("crn").value;
	var space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	if (crn.length != 5 || document.getElementById(crn) != null || table.rows.length == 10) {
		return;
	}
	try {
		parseInt(crn);
	}
	catch (ex) {
		return;
	}
	var row = table.insertRow(table.rows.length);
	row.id = crn;
	row.insertCell(0).innerHTML = "<input name=\"" + crn + "\"type=\"button\" value=\" X \" onclick=\"removeClass(this.name)\" class=\"xbutton\"></input>";
	row.cells[0].id = crn;
	row.insertCell(1).innerHTML = "CRN: " + crn;
}

function displayNums(reset) {
	if (reset) {
		if (confirm("Changing Terms Will Clear the Current Schedule")) {
			document.getElementById("scheduledisplay").innerHTML = "";
		}
		displaySubj();
	}
	var term = document.getElementById("term");
	term = term.options[term.selectedIndex].value;
	var subj = document.getElementById("subjects");
	subj = subj.options[subj.selectedIndex].value;
	var list = document.getElementById("number");
	list.id = "";
	list.style.display = "none";
	list = document.getElementsByName(term + subj)[0];
	list.id = "number";
	list.style.display = "inline";
	list.selectedIndex = 0;	
}

function displaySubj() {
	var term = document.getElementById("term");
	term = term.options[term.selectedIndex].value;
	
	var subj = document.getElementById("subjects");
	
	subj.id = "";
	subj.style.display = "none";
	
	subj = document.getElementsByName(term + "subjects")[0];
	subj.id = "subjects";
	subj.style.display = "inline";
	subj.selectedIndex = 0;
}

function sendSchedule() {
	var send = document.getElementById("form");
	var table = document.getElementById("scheduledisplay");
	if (table.rows.length == 0) {
		alert("No classes have been inputted");
		return;
	}
	
	var select = document.createElement("select");
	select.name = "schedule";
	var option = document.createElement("option");

	var values = "";
	for (var i = 0, row; row = table.rows[i]; i++) {
		var type = row.cells[3].getElementsByTagName("select")[0];
		values = values + type.options[type.selectedIndex].value + row.cells[0].id;
		if (i != table.rows.length-1) {
			values = values + "xx";
		}
	}
	select.hidden = true;
	option.value = values;
	select.add(option);
	select.value = option.value;
	send.appendChild(select);
}
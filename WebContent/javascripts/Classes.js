
function addClass() {
	var table = document.getElementById("scheduledisplay");
	var subj = document.getElementById("subject");
	var cName = document.getElementById("number");
	cName = cName.options[cName.selectedIndex].getAttribute("name");
	cName = cName.split("--");
	subj = subj.options[subj.selectedIndex].value;
	var num = document.getElementById("number").value;
	num = num.substring(num.length-4, num.length);
	var name = subj + num;
	if (num == "none" || table.rows.length == 10) {
		return;
	}
	var space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	var row = table.insertRow(table.rows.length);
	row.id = name + type;
	row.insertCell(0).innerHTML = "<input name=\"" + name + type + "\"type=\"button\" value=\" X \" onclick=\"removeClass(this.name)\" class=\"xbutton\"></input>"
	row.cells[0].id = name;
	row.insertCell(1).innerHTML = subj + " " + num + space;
	row.insertCell(2).innerHTML = cName[0] + space;
	var types = "";
	for (var i = 0; i < cName[2].length; i++) {
		var type = cName[2].substring(i, i+1);
		if (document.getElementById(name + type) != null) {
			continue;
		}
		if (i != 0) {
			types += " + ";
		}
		if (type == "L") {
			types += "Lecture";
		}
		else if (type == "B") {
			types += "Lab";
		}
		else if (type == "C") {
			types += "Recitation";
		}
		else {
			types += "bug";
		}
	}
	row.insertCell(3).innerHTML = types + space;
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
	}
	var term = document.getElementById("term");
	term = term.options[term.selectedIndex].value;
	var subj = document.getElementById("subject");
	subj = subj.options[subject.selectedIndex].value;
	var list = document.getElementById("number");
	list.id = "";
	list.style.display = "none";
	list = document.getElementsByName(term + subj)[0];
	list.id = "number";
	list.style.display = "inline";
	list.selectedIndex = 0;
}

function sendSchedule() {
	var send = document.getElementById("form");
	var select = document.createElement("select");
	select.name = "schedule";
	var option = document.createElement("option");
	
	var table = document.getElementById("scheduledisplay");

	var values = "";
	for (var i = 0, row; row = table.rows[i]; i++) {
		values = values + row.cells[0].id;
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
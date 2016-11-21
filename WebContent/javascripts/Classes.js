function addClass(button) {
	if ($('#schedule tr').length > 12) {
		return;
	}

	var text = button.parent().text().split(" / ");
	var crns = text[3].split(", ");
	var types = text[1].split(", ");
	var profs = text[2].split(", ");
	var name = text[0].split(" - ");
	var html =  "<tr><td>" +
	"<select style='width:70%;margin-left:auto;margin-right:auto' class='form-control' onchange='crnCheck(this)'>";
	if (crns.length > 1) {
		html += "<option value='A'>Any</option>";
	}
	for (var i = 0; i < crns.length; i++) {
		html += "<option value='" + crns[i] + "'>" + crns[i] + "</option>";
	}
	html += 	"</select>" +
	"</td>" +
	"<td style='vertical-align:middle' value='" + (name[0].substring(4, name[0].length)).replace(' ', '') +
	"'>" + name[0].substring(4, name[0].length) + "</td>" +
	"<td style='vertical-align:middle'>" + name[1] + "</td>" +
	"<td>" +
	"<select style='width:75%;margin-left:auto;margin-right:auto' class='form-control'>";
	if (types.length > 1) {
		html += "<option value='A'>Any</option>";
	}
	for (var i = 0; i < types.length; i++) {
		html += "<option value='" + classType(types[i]) + "'>" + types[i] + "</option>";
	};
	html += 	"</select>" +
	"</td>" + 
	"<td>" + 
	"<select style='width:75%;margin-left:auto;margin-right:auto' class='form-control'>";
	if (profs.length > 1) {
		html += "<option value='A'>Any</option>";
	}
	for (var i = 0; i < profs.length; i++) {
		html += "<option value='" + profs[i].replace(/-/g, "_").replace(/ /g, "_") + "'>" + profs[i] + "</option>";
	};
	html += 	"</select>" +
	"</td>" +
	"<td><button class='btn btn-default' type='button' onClick='removeClass(this)'>Remove</button></td></tr>";
	$("#schedule > tbody:last-child").append(html);
}

function addCRN(button) {
	if ($('#schedule tr').length > 12) {
		return;
	}

	var text = button.parent().text().split(" / ");
	var name = text[1].split(" - ");
	button = $("[name='" + name[0].replace(/ /g, '') + "']");
	addClass(button);
	var row = $('#schedule tbody tr:last').children('td');
	$($(row.get(3)).children()[0]).val(classType(text[2]));
	$($(row.get(4)).children()[0]).val(text[3].replace(/ /g, '_'));
	$($(row.get(0)).children()[0]).val(text[0].replace('Add ', ''));
	$($(row.get(3)).children()[0]).prop('disabled', true);
	$($(row.get(4)).children()[0]).prop('disabled', true);
}

function crnCheck(select) {
	var row = $(select).parent().parent().children('td');
	if (select.value !== 'A') {
		var text = $("[name='" + select.value + "']").parent().text().split(" / ");
		$($(row.get(3)).children()[0]).val(classType(text[2]));
		$($(row.get(4)).children()[0]).val(text[3].replace(/ /g, '_'));
		$($(row.get(3)).children()[0]).prop('disabled', true);
		$($(row.get(4)).children()[0]).prop('disabled', true);
	}
	else {
		$($(row.get(3)).children()[0]).prop('disabled', false);
		$($(row.get(4)).children()[0]).prop('disabled', false);
	}
}


function removeClass(button) {
	$(button).parent().parent().remove();
}

function sendData() {
	var send = document.getElementById("form");
	var table = document.getElementById("schedule");
	var select = document.createElement("select")
	select.name = "schedule";
	var option = document.createElement("option");
	var value = "";

	for (var i = 1, row; row = table.rows[i]; i++) {
		var crn = row.cells[0].children[0];
		crn = crn.options[crn.selectedIndex].value;
		if (crn.length == 5) {
			value += row.cells[0].getElementsByTagName("select")[0].value;
		}
		else {
			var type = row.cells[3].children[0];
			type = type.options[type.selectedIndex].value;
			value += type + row.cells[1].getAttribute('value') + '-';
			var prof = row.cells[4].children[0];
			prof = prof.options[prof.selectedIndex].value;
			value += prof;
		}
		if (i != table.rows.length-1) {
			value += "~";
		}
	}

	select.hidden = true;
	option.value = value;
	select.add(option);
	send.appendChild(select)
}

function classType(type) {
	switch (type) {
	case "Lecture": return "L";
	case "Lab": return "B";
	case "Recitation": return "C";
	case "Hybrid": return "H";
	case "Emporium": return "E";
	case "Online": return "O";
	case "Independent Study": return "I";
	case "Research": return "R";
	default:  return "bug";
	}
}

function setParameters() {
	var params = window.location.search.substr(1);
	if (params.length == 0) {
		return
	}
	params = params.split('&');
	var i;
	for (i = 1; i < 8; i++) { 
		var val = params[i].split('=');
		$('[name="' + val[0] + '"]').selectpicker('val', val[1]);
	}
	var days = [];
	for (i; i < params.length; i++) {
		days[i-8] = params[i].split('=')[1];
	}
	$('[name="free"]').selectpicker('val', days);
	
	var classList = params[0].split('=')[1].split('%7E');
	if (classList[0].length != 0) {
		for (i = 0; i < classList.length; i++) {
			var str = classList[i];
			var split = str.split('-');
			var index = 0;
			if (split[0].charAt(split[0].length-1) == 'H') {
				index = 1;
			}
			if (split[0].length == 5 && !isNaN(parseInt(split[0], 10))) {
				$('[name="' + split[0] + '"]').click();
			}
			else {
				$('[name="' + split[0].substring(1) + '"]').click();
				$('#schedule tbody tr:last').children('td'); //continue here
				//selectClass(str.substring(1, str.length-4-index), str.substring(str.length-4-index, str.length));
				$($($('#schedule tbody tr:last').children('td').get(3)).children()[0]).val(split[0].substring(0, 1));
				$($($('#schedule tbody tr:last').children('td').get(4)).children()[0]).val(split[1]);
			}			
		}
	}
}
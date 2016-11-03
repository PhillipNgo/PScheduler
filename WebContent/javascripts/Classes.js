function addClass(button) {
	var text = button.parent().text().split(" / ");
	var crns = text[3].split(", ");
	var types = text[1].split(", ");
	var profs = text[2].split(", ");
	var name = text[0].split(" - ");
	var html =  "<tr><td>" +
					"<select style='width:70%;margin-left:auto;margin-right:auto' class='form-control'>";
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
					"<select style='width:55%;margin-left:auto;margin-right:auto' class='form-control'>";
						if (profs.length > 1) {
							html += "<option value='A'>Any</option>";
						}
						for (var i = 0; i < profs.length; i++) {
							html += "<option value='" + profs[i].replace(/ /g, "11") + "'>" + profs[i] + "</option>";
						};
		html += 	"</select>" +
				"</td>" +
				"<td><button class='btn btn-default' type='button' onClick='removeClass(this)'>Remove</button></td></tr>";
	$("#schedule > tbody:last-child").append(html);
}

function addCRN(button) {
	var text = button.parent().text().split(" / ");
	var name = text[1].split(" - ");
	var html =  "<tr><td>" +
					"<select style='width:70%;margin-left:auto;margin-right:auto' class='form-control'>" +
						"<option value='" + text[0].substring(4, text[0].length) + "'>" + text[0].substring(4, text[0].length) + "</option>" +
					"</select>" +
				"</td>" +
				"<td style='vertical-align:middle'>" + name[0] + "</td>" +
				"<td style='vertical-align:middle'>" + name[1] + "</td>" +
				"<td>" +
					"<select style='width:75%;margin-left:auto;margin-right:auto' class='form-control'>" +
						"<option>" + text[2] + "</option>" +
					"</select>" +
				"</td>" + 
				"<td>" + 
					"<select style='width:55%;margin-left:auto;margin-right:auto' class='form-control'>" +
						"<option>" + text[3] + "</option>" +
					"</select>" +
				"</td>" +
				"<td><button class='btn btn-default' type='button' onClick='removeClass(this)'>Remove</button></td></tr>";
	$("#schedule > tbody:last-child").append(html);
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
			value += type + row.cells[1].getAttribute('value') + 'ZXD';
			var prof = row.cells[4].children[0];
			prof = prof.options[prof.selectedIndex].value;
			value += prof;
		}
		if (i != table.rows.length-1) {
			value += "xx";
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
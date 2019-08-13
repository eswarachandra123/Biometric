// File validation
function validate_fileupload() {
	var uploadField = document.getElementById("file");
	uploadField.onchange = function() {
	    if(this.files[0].size > 3072000){
	       alert("File is too big!");
	       this.value = "";
	    };
	};
}
// Email Validation
function myFunction(email) {
	var space = document.getElementById("feed");
	x = email.value;
	var atpos = x.indexOf("@");
	var dotpos = x.lastIndexOf(".");
	if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length) {
		space.innerHTML = "Invalid Email Address";
		return false;
	}
	space.innerHTML = "";
	return true;
}

function check() {
	if (validateAllinputs()) {
		return true;
	} else {
		return false;
	}

}

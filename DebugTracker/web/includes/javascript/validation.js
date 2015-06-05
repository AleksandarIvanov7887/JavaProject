function isValidName(s) {
    if (!s.contains('--') && new RegExp('^[a-zA-Z\\-\\s]+$').test(s)) {
        return true;
    }

    return false;
}

function isValidTextField(s) {
}

function isValidEmail(s) {
}

function isValidAuthField(s) {
    return new RegExp('^[a-zA-Z0-9_]+$').test(s);
}

function isValidDate(s) {
    var date_arr = s.split('-');
    if (date_arr.length == 3 &&
        (date_arr[0] <= 31 && date_arr > 0)) {
        return false;
    }
}

function isValidItemsRange(s) {
    var dig = parseInt(s);
    return dig == 10 || dig == 20 || dig == 30 || dig == 40 || dig == 50 || dig == 60;

}

function isValidStatus(s) {
}

var a = new RegExp('');
var b = new RegExp('');
var c = new RegExp('[a-zA-Z\\-]+');
var d = new RegExp('');

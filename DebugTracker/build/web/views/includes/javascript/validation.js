// TODO KALIN GEORGIEV

function isValidName(s) {
    if (!s.contains('--') && new RegExp('^[a-zA-Z\\-\\s]+$').test(s)) {
        return true;
    }

    return false;
}

function isValidTextField(s) {
    //[a-zA-Z0-9_]+
    // TODO
//    return new RegExp('^[a-zA-Z0-9\\._\\s\\!,()]+$').test(s);
}

function isValidEmail(s) {
// dunno - from web
}

function isValidAuthField(s) {
//    [a-zA-Z0-9_]+
    return new RegExp('^[a-zA-Z0-9_]+$').test(s);
}

function isValidDate(s) {
    var date_arr = s.split('-');
    if (date_arr.length == 3 &&
        (date_arr[0] <= 31 && date_arr > 0)) {
        return false;
    }

//11-01-2014
}

function isValidItemsRange(s) {
    var dig = parseInt(s);
    return dig == 10 || dig == 20 || dig == 30 || dig == 40 || dig == 50 || dig == 60;

}

function isValidStatus(s) {
    //Open, In Process, Closed
}

var a = new RegExp('');


var b = new RegExp('');


var c = new RegExp('[a-zA-Z\\-]+');
c.test('Daniel')

var d = new RegExp('');

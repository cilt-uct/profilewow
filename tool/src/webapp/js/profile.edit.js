$(document).ready(function() {
    var hide = function() {
        $('td[rel*=infoCell]').html($('#infoCell-backup').html());
        $('#infoCell-backup').remove();
        return false;
    }
    $('input[class*=cancel]').bind('click', hide);
    $('.closeImg').bind('click', hide);
    $('.addItem').on('click', function() {
        $('textarea[name*=editProfileForm-more]').slideToggle('fast');
        return false;
    });

    /**
     * Extending the $.validator ibrary to cater for phone numbers
     */

    $.validator.addMethod("phone", function(phone_number, element) {
        var num = phone_number.split(' ').join('');
        return this.optional(element) || num.length > 9 &&
            ((num.match(/^[0-9]/) || num.match(/^[+]/) || num.match(/^[(]/)) && (num.split('-').join('').split('(').join('').split(')').join('').match(/^[+]?\d+$/)));
    }, "Please enter a valid phone number");

    var container = $('#errorContainer');
    // validate the form when it is submitted
    var validator = $('#edit-form').validate({
        errorContainer: container,
        rules: {
            'editProfileForm-mobile': {
                required: false,
                phone: true
            }
        }

    });

});

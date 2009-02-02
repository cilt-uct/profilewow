$(document).ready(function() {
           var hide = function() {
                $('td[rel*=infoCell]').html($('#infoCell-backup').html());
                $('#infoCell-backup').remove();
                return false;
            }
            $('input[@class*=cancel]').bind('click', hide);
            $('.closeImg').bind('click', hide);
            $('.addItem').bind('click', function() {
                $('textarea[@name*=editProfileForm-more]').slideToggle('fast');
                return false;
            });


            /**
             * Extending the $.validator ibrary to cater for phone numbers
             */

            jQuery.validator.addMethod("phone", function(phone_number, element) {
                return this.optional(element) || phone_number.length > 9 && phone_number.match(/^[+]?\d+/);
            }, "Please enter a valid phone number");


            $('#edit-form').validate({
                rules: {
                    'editProfileForm-mobile': {
                      required: true,
                      phone: true
                    }
                  }

            });

        });
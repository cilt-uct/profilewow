$(document).ready(function() {
            //$('#edit-form').ajaxForm(options);
            var hide = function() {
                $('td[rel*=infoCell]').html($('#infoCell-backup').html());
                $('#infoCell-backup').remove();
                return false;
            }
            $('input[@class*=cancel]').bind('click', hide);
            $('.closeImg').bind('click', hide);
            //$('textarea[@name*=editProfileForm-more]').toggle();
            //$.facebox.setHeader($('.titleHeader'));
            $('.addItem').bind('click', function() {
                $('textarea[@name*=editProfileForm-more]').slideToggle('fast');
                return false;
            });


            /**
             * Extending the $.validator ibrary to cater for phone numbers
             */

            jQuery.validator.addMethod("phone", function(phone_number, element) {
                return this.optional(element) || phone_number.length > 9 && phone_number.match(/^[+]?\d+$/);
            }, "Please specify a valid phone number");


            $('#edit-form').validate({
                rules: {
                    'editProfileForm-mobile': {
                      required: true,
                      phone: true
                    },
                    'editProfileForm-workphone': {
                      required: false,
                      phone: true
                    }
                  }

            });

        });
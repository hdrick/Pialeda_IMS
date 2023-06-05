$(document).ready(function() {
  // Get the first modal and its input fields
  var parentModalId;
  var confirmationModal = $("#confirmationModal");
  var clientName = $("#clientName");
  var answer;
  var OR;
  var CR;
  var IP;
  var CRB;
  var LCR;
//  function storeInputValues() {
//    localStorage.setItem("clientName", clientName.val());
//  }

//  function restoreInputValues() {
//    clientName.val(localStorage.getItem("clientName"));
//  }
  function getModalId(id)
    {
        parentModalId = id;
    }
  function getCondition(condition)
  {
        if(condition == true)
        {
        return answer = condition;
        }
  }
  function invConfirmationExitCR(cond)
  {
      if(cond == true)
      {
          var href="/marketing-collectionreceipt";
          window.location.href = href;
      }
  }

  function invConfirmationExitOR(cond)
  {
      if(cond == true)
      {
          var href="/marketing-officialreceipt";
          window.location.href = href;
      }
  }

   function invConfirmationExitIP(cond)
     {
         if(cond == true)
         {
             var href="/invoice-view";
             window.location.href = href;
         }
     }
   function invConfirmationExitCRB(cond)
    {
        if(cond == true)
        {
            var href="/marketing-invoice";
            window.location.href = href;
        }
    }
    function invConfirmationExitLCR(cond)
        {
            if(cond == true)
            {
                var href="/collection-receipt-list";
                window.location.href = href;
            }
        }
  // Add a click event listener to the add client button
  $(".btn-close").on("click", function() {
        parentModalId = $(this).closest('.modal').attr('id');
        getModalId(parentModalId);
        confirmationModal.css('z-index', '9999');
        confirmationModal.modal('show');
  });

   $("#noBtn").on("click", function() {
       confirmationModal.css('z-index', '0');
       confirmationModal.modal('hide');
       confirmationModal.css('display', 'none');
       getConditionORCR(false);

    });

    $("#yesBtn").on("click", function() {
       confirmationModal.css('z-index', '0');
       confirmationModal.modal('hide');
       confirmationModal.css('display', 'none');

        $('#'+parentModalId).css('display', 'none');
        $('#'+parentModalId).modal('hide');
        $('#'+parentModalId).css('z-index', '10');
        $('#'+parentModalId).find('input').val('');

        if(getCondition(OR) == true)
        {
            invConfirmationExitOR(true)
        }
        if(getCondition(CR) == true)
        {
            invConfirmationExitCR(true)
        }
        if(getCondition(IP) == true)
        {
            invConfirmationExitIP(true);
        }
        if(getCondition(CRB) == true)
        {
            invConfirmationExitCRB(true);
        }
        if(getCondition(LCR) == true)
        {
            invConfirmationExitLCR(true);
        }
    });

    $("#btn-addClient").on("click", function() {

        parentModalId = $(this).attr('data-target');
        $(parentModalId).css('z-index', '9999');
    });

    $("#btn-addSupplier").on("click", function() {

            parentModalId = $(this).attr('data-target');
            $(parentModalId).css('z-index', '9999');
        });
    $("#btn-CR").on("click", function() {
        var clientName = $('#my-client').val();
        var supplierName = $('#my-supplier').val();
        if(clientName == '' && supplierName == '')
        {
            invConfirmationExitCR(true);
        }
        else
        {
            CR = true;
            IP = false;
            OR = false;
            CRB = false;
            LCR = false;
            confirmationModal.css('z-index', '9999');
            confirmationModal.modal('show');
        }
    });

    $("#btn-OR").on("click", function() {
            var clientName = $('#my-client').val();
            var supplierName = $('#my-supplier').val();
            if(clientName == '' && supplierName == '')
            {
                invConfirmationExitOR(true);
            }
            else
            {
                OR = true;
                IP = false;
                CR = false;
                CRB = false;
                LCR = false;
                confirmationModal.css('z-index', '9999');
                confirmationModal.modal('show');
            }
        });

        $("#btn-invoices").on("click", function() {
                var clientName = $('#my-client').val();
                var supplierName = $('#my-supplier').val();

                if(clientName == '' && supplierName == '')
                {
                      invConfirmationExitIP(true);
                }
                else
                {
                    IP = true;
                    OR = false;
                    CR = false;
                    CRB = false;
                    LCR = false;
                    confirmationModal.css('z-index', '9999');
                    confirmationModal.modal('show');
                }
            });

            $("#btn-back").on("click", function() {
                var clientName = $('#my-client').val();
                var supplierName = $('#my-supplier').val();

                if(clientName == '' && supplierName == '')
                {
                    invConfirmationExitCRB(true);
                }
                else
                {
                    CRB = true;
                    IP = false;
                    OR = false;
                    CR = false;
                    LCR = false;
                    confirmationModal.css('z-index', '9999');
                    confirmationModal.modal('show');
                }
            });

            $("#btn-ListCR").on("click", function() {
                var clientName = $('#my-client').val();
                var supplierName = $('#my-supplier').val();

                if(clientName == '' && supplierName == '')
                {
                    invConfirmationExitLCR(true);
                }
                else
                {
                    LCR = true;
                    IP = false;
                    OR = false;
                    CR = false;
                    CRB = false;
                    confirmationModal.css('z-index', '9999');
                    confirmationModal.modal('show');
                }
            });

});

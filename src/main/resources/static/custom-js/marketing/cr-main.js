function getTotalAmount() {
    let inputs = document.getElementsByClassName("myNumber");
    let total = 0;
    for (let i = 0; i < inputs.length; i++) {
      let input = inputs[i];
      if (input.value !== "") {
        let valueWithCommas = input.value; // e.g. "1,200.00"
        let valueWithoutCommas = valueWithCommas.replace(/,/g, ''); // e.g. "1200.00"
        let amount = parseFloat(valueWithoutCommas);

        if (!isNaN(amount)) {
          total += amount;
        }
      }
    }
    return total;
  }

function updateTotalAmount() {
    let inputs = document.getElementsByClassName("myNumber");
    let amountDue = document.getElementById("amt-due");
    let totalSales = document.getElementById("total-sales");
    for (let i = 0; i < inputs.length; i++) {
      let input = inputs[i];
      
      input.addEventListener("input", function() {
        let totalAmount = getTotalAmount();
        amountDue.value = totalAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        totalSales.value = totalAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
      });
    }
  }
  
  

function formatNumber(input) {
    // Get input value without commas
    let value = input.value.replace(/,/g, '');
  
    // Format value with commas and two decimal places
    value = parseFloat(value).toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  
    // Update input value with formatted value
    input.value = value;
  }

  
  function computeEWT(){
    const ewtInput = document.getElementById("ewt");
    const totalInput = document.getElementById("total");
    // Get the input element
    var inputElement = document.getElementById("ewtInput");
    var displayEwt = document.getElementById("ewt-input");
    // Get the value of the input
    var ewtPercentage = inputElement.value/100;

    // let totalAmount = getTotalAmount();
    const totalAmount = document.getElementById("amt-due").value;
    let valueWithCommas = totalAmount; // e.g. "1,200.00"
    let totalAmountNoComma = valueWithCommas.replace(/,/g, ''); // e.g. "1200.00"
    console.log("totalAmount: "+totalAmountNoComma);

    let result = (totalAmountNoComma/1.12)*ewtPercentage;
    let ewt = result;
    
    ewtInput.value = ewt.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    let newTotal = totalAmountNoComma - ewt; 
    totalInput.value = newTotal.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    displayEwt.innerHTML = inputElement.value+"%";

    convertNumberToWords();
  }

  function clearFields(){
    const totalSales = document.getElementById("total-sales");
    const addVatInput = document.getElementById("add-vat");
    const lwTaxInput = document.getElementById("lw-tax");
    const amtDueInput = document.getElementById("amt-due");
    const ewtInput = document.getElementById("ewt");
    const totalInput = document.getElementById("total");

    totalSales.value = null;
    addVatInput.value = null;
    lwTaxInput.value = null;
    amtDueInput.value = null;
    ewtInput.value = null;
    totalInput.value = null;
  }

  function getInvoiceInfo() {
    var invNum = $('#my-invNum').val();
    $.ajax({
        type: "GET",
        url: "/getInvoiceInfo",
        data: {
            invNum: invNum
        },
        success: function(data) {

            $('#inv1').val(data.invoiceNum);
            $('#inv1-amt').val(data.grandTotal);
            $('#amt-due').val(data.grandTotal);
            $('#total').val(data.grandTotal);

            $('#add-vat').val(null);
            $('#lw-tax').val(null);
            $('#ewt').val(null);

            convertNumberToWords();
            

        }
    });
  }
 function convertNumberToWords() {
      // Get the input value
      var amountInput = document.getElementById("total");
      var clientPaymentInput = document.getElementById("clientPayment");
      var amount = parseFloat(amountInput.value);
      // Set the converted value to the sumOf input field
      var sumOfInput = document.getElementById("sumOf");

      // Check for null input
      if (amountInput === null || isNaN(amount)) {
        return sumOfInput.value = "please input in numbers only";
      }

      // Split the amount into whole and decimal parts
      var wholePart = Math.floor(amount);
      var decimalPart = Math.floor((amount % 1) * 100);

      // Convert the whole and decimal parts to words
      var wholePartWords = convertToWords(wholePart);
      var decimalPartWords = convertToWords(decimalPart);



      var output = wholePartWords;
      if (decimalPart > 0) {
        output += " and " + decimalPartWords + " cents";
      }
      // Update the value of clientPayment input field
      clientPaymentInput.value = amount;
      sumOfInput.value = output;
    }
    function convertToWords(number) {
          // Define the arrays for units, tens, and scales
          var units = ['', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine', 'ten',
            'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen',
            'eighteen', 'nineteen'];

          var tens = ['', '', 'twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'];

          var scales = ['', 'thousand', 'million', 'billion'];

          // Convert the number to words
          var words = '';

          if (number === 0) {
            words = 'zero';
          } else {
            var count = 0;

            do {
              var hundreds = number % 1000;

              if (hundreds !== 0) {
                var hundredsWords = convertToWordsLessThanOneThousand(hundreds);
                words = hundredsWords + ' ' + scales[count] + ' ' + words;
              }

              count++;
              number = Math.floor(number / 1000);
            } while (number > 0);
          }

          return words.trim();
        }

//   SUPPLIER AND CLIENT INFO
function getSupplierInfo(){
    var supplierName = $('#my-supplier').val();
    if(supplierName != "")
    {
        $.ajax({
                type: "GET",
                url: "/getSupplierInfo",
                data: {
                    name: supplierName
                },
                success: function(data) {
                    // $('#logo-suppName').text(data.name);

                    //for input and display
                    $('#supp-name').val(data.name);
                    $('#logo-suppName').text(data.name);
                    // $('#supp-name').text(data.name);

                    $('#supp-addrs').val(data.address+", "+data.cityAddress);
                    // $('#supp-addrs').text(data.address+", "+data.cityAddress);

                    $('#supp-tin').val("VAT Reg. TIN "+formatTIN(data.tin));
                    $('#supp-tin-hidden').val(formatTIN(data.tin));
                    // $('#supp-tin').text("VAT Reg. TIN "+data.tin);
                    displayInvoice();
                }
            });
    }
    else
        {
            var select = $('#my-invNum');
            clearFields();
            $('#inv1-amt').val(null);
            $('#supp-name').val(null);
            $('#logo-suppName').text(null);
            // $('#supp-name').text(data.name);
            $('#supp-addrs').val(null);
            // $('#supp-addrs').text(data.address+", "+data.cityAddress);
            $('#supp-tin').val(null);
            $('#supp-tin-hidden').val(null);
            select.children(':not(:first)').remove();
        }


}

function getClientInfo() {
    var clientName = $('#my-client').val();
    if(clientName != "")
    {
        $.ajax({
                type: "GET",
                url: "/getClientInfo",
                data: {
                    name: clientName
                },
                success: function(data) {
                    $('#c-name').val(data.name);

                    $('#tin').val(formatTIN(data.tin));

                    $('#c-address').val(data.address+", "+data.cityAddress);

                    $('#busStyle').val(data.busStyle);
                    displayInvoice();

                }
            });
    }
    else
    {
        var select = $('#my-invNum');
        clearFields();
        $('#inv1-amt').val(null);
        $('#c-name').val(null);
        $('#tin').val(null);
        $('#c-address').val(null);
        $('#busStyle').val(null);
        select.children(':not(:first)').remove();
    }
}
function displayInvoice()
{
    var supplierName = $('#my-supplier').val();
    var clientName = $('#my-client').val();
    var select = $('#my-invNum');

    if(supplierName != "" && clientName != "")
    {
    select.children(':not(:first)').remove();
        $.ajax({
                type: "GET",
                url: "/getCRInvoiceDetails",
                data: {
                    supplier: supplierName,
                    client: clientName
                },
                success: function(data) {
                    $('#inv1-amt').val(null);
                    clearFields();
                    populateSelector(data);
                },
                error: function() {
                  console.log('Error retrieving data');
                }
            });
    }
}

function populateSelector(data) {
  var select = $('#my-invNum');

  $.each(data, function(index, item) {
    var option = $('<option>').val(item.invoiceNum).text(item.invoiceNum);
    select.append(option);
  });

}
function formatTIN(tin) {
    var formattedTIN = tin.toString().replace(/[^0-9]/g, ''); // remove any non-digit characters
    formattedTIN = formattedTIN.replace(/^(\d{3})(\d{3})(\d{3})$/, "$1-$2-$3-000");
    return formattedTIN;
  }
  


  // Get references to the input and h2 elements
var inputElement = document.getElementById("supp-name");
var h2Element = document.getElementById("logo-suppName");

// Add an event listener to the input element
inputElement.addEventListener("input", function() {
  // Set the text content of the h2 element to the input value
  h2Element.textContent = inputElement.value;
});
  



  updateTotalAmount();
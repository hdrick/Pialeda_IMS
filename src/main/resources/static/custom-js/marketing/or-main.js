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
  }

  function computeVat(){
    let totalAmount = getTotalAmount();
    // const totalSales = document.getElementById("total-sales");

    const addVatInput = document.getElementById("add-vat");
    const lwTaxInput = document.getElementById("lw-tax");
    const totalInput = document.getElementById("total");
    const amtDueInput = document.getElementById("amt-due");

    let addVat = 0.12 * totalAmount;
    let lwTax = totalAmount * 0.02;

    let totalResult = (totalAmount + addVat) -lwTax;

    addVatInput.value = addVat.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});;
    lwTaxInput.value = lwTax.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});;
    totalInput.value = totalResult.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});;
    amtDueInput.value = totalResult.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});;
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


//   SUPPLIER AND CLIENT INFO
function getSupplierInfo(){
    var supplierName = $('#my-supplier').val();
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
        }
    });
}

function getClientInfo() {
    var clientName = $('#my-client').val();
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


        }
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
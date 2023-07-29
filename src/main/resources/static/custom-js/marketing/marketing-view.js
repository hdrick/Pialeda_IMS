  $(document).ready(function () {

        $('#invoiceLimitModal').modal('show');

        $('#invoiceCreatedModal').modal('show');
        setTimeout(function() {
            $('#invoiceCreatedModal').modal('hide');
        }, 3000);
    });

function fillModalFields(invId) {
    $.ajax({
        type: "GET",
        url: "/specific-inv",
        data: {
            id: invId
        },
        success: function(data) {
            $('#inv-num').val(data.invoiceNum);
            $('#po-num').val(data.poNum);
            $('#si-created').val(data.saleInvoiceDate);
            $('#order-created').val(data.dateCreated);

            $('#supp-name').val(data.supplierName);
            $('#supp-address').val(data.supplierAddress);
            $('#supp-tin').val(data.supplierTin);



            $('#client-name').val(data.clientName);
            $('#client-bStyle').val(data.clientBusStyle);
            $('#client-terms').val(data.clientTerms);
            $('#client-address').val(data.clientAddress);
            $('#client-tin').val(data.clientTin);

            $('#client-cp').val(data.clientContactPerson);

            // Fill the invoice product table
            var prodList = data.productList;
            var tableBody = $('#product-table tbody');
            tableBody.empty(); // Clear any existing rows in the table
            var totalAmt = 0; // Initialize total amount to 0
            for (var i = 0; i < prodList.length; i++) {
                var prodInfo = prodList[i];
                console.log(prodInfo);
                var row = $('<tr>');
                row.append($('<td contenteditable="true" id="qty" name="qty">').text(prodInfo.qty));
                row.append($('<td contenteditable="true" id="unit" name="unit">').text(prodInfo.unit));
                row.append($('<td contenteditable="true" id="articles" name="articles">').text(prodInfo.articles));
                row.append($('<td contenteditable="true" id="unitP" name="unitP">').text(prodInfo.unitPrice));
                row.append($('<td contenteditable="true" id="amount" name="amount">').text(prodInfo.amount));

                row.append($('<input id="id-input" name="id-input" type="hidden" value="' + prodInfo.id + '">'));
                row.append($('<input id="qty-input" name="qty-input" type="hidden" value="' + prodInfo.qty + '">'));
                row.append($('<input id="unit-input" name="unit-input" type="hidden" value="' + prodInfo.unit + '">'));
                row.append($('<input id="articles-input" name="articles-input" type="hidden" value="' + prodInfo.articles + '">'));
                row.append($('<input id="unitP-input" name="unitP-input" type="hidden" value="' + prodInfo.unitPrice + '">'));
                row.append($('<input id="amount-input" name="amount-input" type="hidden" value="' + prodInfo.amount + '">'));
                tableBody.append(row);
                totalAmt += parseFloat(prodInfo.amount); // Add to total amount
            }
            $('#total-amt').val(totalAmt.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
            $('#addVat').val(data.addVat.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
            $('#amtNetVat').val(data.amountNetOfVat.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
            $('#totalSalesVatInc').val(data.totalSalesVatInc.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input

            // Update the input values when the contenteditable td is changed
            $('#product-table tbody td[contenteditable="true"]').on('input', function() {
                var row = $(this).closest('tr');
                var qty = parseFloat(row.find('td:eq(0)').text());
                var unitPrice = parseFloat(row.find('td:eq(3)').text());
                                var unit = row.find('td:eq(1)').text();
                                var articles = row.find('td:eq(2)').text();
                var amount = qty * unitPrice;
                row.find('td:eq(4)').text(amount.toFixed(2));

                // Update the input values
                row.find('input#qty-input').val(qty);
                row.find('input#unitP-input').val(unitPrice);
                row.find('input#amount-input').val(amount.toFixed(2));
                row.find('input#unit-input').val(unit);
                row.find('input#articles-input').val(articles);

                // Recalculate the total amount
                var totalAmt = 0;
                $('#product-table tbody tr').each(function() {
                    totalAmt += parseFloat($(this).find('td:eq(4)').text());
                });
                $('#total-amt').val(totalAmt.toFixed(2));
                $('#totalSalesVatInc').val(totalAmt.toFixed(2));
                
            });
        }
    });
}


function updateVat(){
  const total = $("#total-amt").val();
  let totalSalesVatInc = 0;
  let totalAmountNetOfVat = 0;
  let addVat = 0;
  let grandTotal = 0;

  totalAmountNetOfVat = total / 1.12;
  addVat = total - totalAmountNetOfVat;
  grandTotal = totalSalesVatInc;

  // $('#total-amt').val(totalAmt.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
  $('#addVat').val(addVat.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
  $('#amtNetVat').val(totalAmountNetOfVat.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2})); // Set the formatted value of the total amount input
  
}

function searchInvoices() {
  // Get the input values for supplier name, client name, and invoice number
  var supplierName = $("#supplierNameInput").val();
  var clientName = $("#clientNameInput").val();
  var invoiceNum = $("#invoiceNumInput").val();

  // Loop through each invoice row in the table and check if it matches the search criteria
  $("#invoicesTable tbody tr").each(function() {
    var row = $(this);
    var rowSupplierName = row.find("td:eq(4)").text().toLowerCase();
    var rowClientName = row.find("td:eq(3)").text().toLowerCase();
    var rowInvoiceNum = row.find("td:eq(1)").text().toLowerCase();

    // If the row matches the search criteria, show it. Otherwise, hide it.
    if (supplierName && clientName && invoiceNum) {
      if (rowSupplierName.includes(supplierName.toLowerCase()) && rowClientName.includes(clientName.toLowerCase()) && rowInvoiceNum.includes(invoiceNum.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (supplierName && clientName) {
      if (rowSupplierName.includes(supplierName.toLowerCase()) && rowClientName.includes(clientName.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (supplierName && invoiceNum) {
      if (rowSupplierName.includes(supplierName.toLowerCase()) && rowInvoiceNum.includes(invoiceNum.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (clientName && invoiceNum) {
      if (rowClientName.includes(clientName.toLowerCase()) && rowInvoiceNum.includes(invoiceNum.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (supplierName) {
      if (rowSupplierName.includes(supplierName.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (clientName) {
      if (rowClientName.includes(clientName.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else if (invoiceNum) {
      if (rowInvoiceNum.includes(invoiceNum.toLowerCase())) {
        row.show();
      } else {
        row.hide();
      }
    } else {
      row.show();
    }
  });
}


function specificInvoiceID(invoiceId) {
  document.getElementById('invoiceIdPlaceholder').textContent = invoiceId;
  // Additional code to handle other modal fields
}

// document.getElementById('btn-duplicate').addEventListener('click', function() {
//   var invoiceId = document.getElementById('invoiceIdPlaceholder').textContent;
//   var supdupInvoiceNumpName = document.getElementById('invoiceNumber').value;
//   var dupPoNum = document.getElementById('poNumber').value;
//   var dupDate = document.getElementById('date').value;
  
//   // Check if any of the input fields are empty
//   if (!supdupInvoiceNumpName || !dupPoNum || !dupDate) {
//     // Display the error message within the modal
//     document.getElementById('errorContainer').textContent = "Please fill in all the required fields.";
//     return;
//   }
  
//   var xhr = new XMLHttpRequest();
//   xhr.open('POST', '/duplicate/' + invoiceId, true);
//   xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
//   xhr.onreadystatechange = function() {
//     if (xhr.readyState === 4 && xhr.status === 200) {
//       // Handle the response from the server
//       var result = xhr.responseText;
//       console.log(result);
      
//       // Perform actions based on the result
//       if (result === "success") {
//         // Redirect or perform other actions
//         alert("Invoice duplicated successfully.");
//       } else if (result === "invoiceNumberFound") {
//         // Handle invoice number found case
//         alert("Invoice number already exists.");
//       } else if (result === "purchaseNumberFound") {
//         // Handle purchase number found case
//         alert("Purchase number already exists.");
//       } else {
//         // Handle other cases or errors
//         alert("An error occurred. Please try again.");
//       }
//     }
//   };
//   var params = 'd-invName=' + encodeURIComponent(supdupInvoiceNumpName) + '&d-poNum=' + encodeURIComponent(dupPoNum) + '&d-date=' + encodeURIComponent(dupDate);
//   xhr.send(params);
// });



document.getElementById('btn-duplicate').addEventListener('click', function() {
  var invoiceId = document.getElementById('invoiceIdPlaceholder').textContent;
  var supdupInvoiceNumpName = document.getElementById('invoiceNumber').value;
  var dupPoNum = document.getElementById('poNumber').value;
  var dupDate = document.getElementById('date').value;
  
  // Check if any of the input fields are empty
  if (!supdupInvoiceNumpName || !dupPoNum || !dupDate) {
    // Display the error message within the modal
    document.getElementById('errorContainer').textContent = "Please fill in all the required fields.";
    return;
  }
  
  var xhr = new XMLHttpRequest();
  xhr.open('POST', '/duplicate/' + invoiceId, true);
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      // Handle the response from the server
      var result = xhr.responseText;
      console.log(result);
      
      // Perform actions based on the result
      if (result === "success") {
        // Display success message
        document.getElementById('successContainer').textContent = "Invoice duplicated successfully.";
        
        // Reload the page after 3 seconds
        setTimeout(function() {
          location.reload();
        }, 3000);
      } else if (result === "invoiceNumberFound") {
        // Display invoice duplicate message
        document.getElementById('invoiceDuplicateContainer').textContent = "Invoice number already exists.";
      } else if (result === "purchaseNumberFound") {
        // Display PO number duplicate message
        document.getElementById('poNumberContainer').textContent = "Purchase number already exists.";
      } else if (result === "supplierLimitExceed"){
        // Display PO number duplicate message
        document.getElementById('suppLimitContainer').textContent = "Unfortunately, you have reached the maximum amount limit and are unable to create a new invoice at this time. Please try again later or contact support for further assistance";
      // Reload the page after 3 seconds
      setTimeout(function() {
        location.reload();
      }, 3000);
      }else {
        // Handle other cases or errors
        alert("An error occurred. Please try again.");
      }
    }
  };
  var params = 'd-invName=' + encodeURIComponent(supdupInvoiceNumpName) + '&d-poNum=' + encodeURIComponent(dupPoNum) + '&d-date=' + encodeURIComponent(dupDate);
  xhr.send(params);
});

















// Call the search function when the search button is clicked
$("#searchButton").click(searchInvoices);





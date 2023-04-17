const invoiceNum = document.getElementById('invoice-number');
const invoiceNumInput = document.getElementById('invoice-number-input');

const btnGen = document.getElementById('btn-Gen');

function generateInvNum() {
    const f1 = "INV-";
    const currentYear = new Date().getFullYear();
    const currentYearLast2Digit = currentYear.toString().slice(-2);

    const now = new Date();
    const monthIndex = now.getMonth();

    const currentMonth = monthIndex + 1;
    const randomNumber = Math.floor(Math.random() * 90000) + 10000;
    const randomNumber2 = Math.floor(Math.random() * 90) + 10;

    const date = now.getDate();
    let currentDay = 0;
    if (date <= 9) {
        currentDay = "0" + date;
    } else {
        currentDay = date;
    }
    const result = f1 + currentYearLast2Digit + currentDay + currentMonth + "-" + randomNumber + randomNumber2;
    invoiceNum.textContent = result;
    invoiceNumInput.value = result;
    generatePONumber();
}

function getClientInfo() {
    const date = new Date();

    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    const formattedDate = `${monthNames[date.getMonth()]} ${padZero(date.getDate())}, ${date.getFullYear()}`;

    function padZero(value) {
        return value < 10 ? `0${value}` : value;
    }
    var clientName = $('#my-client').val();
    $.ajax({
        type: "GET",
        url: "/getClientInfo",
        data: {
            name: clientName
        },
        success: function(data) {
            $('#client-name').text(data.name);
            $('#client-name-input').val(data.name);

            $('#client-tin').text(formatTIN(data.tin));
            $('#client-tin-input').val(data.tin);

            $('#client-address').text(data.address+", "+data.cityAddress);
            $('#client-address-input').val(data.address+", "+data.cityAddress);

            $('#client-conPerson-input').val(data.agent);
            $('#client-conPerson').text(data.agent);

            // $('#client-date').text(formattedDate);
            // $('#client-date-input').val(formattedDate);

            $('#client-busStyle').text(data.busStyle);
            $('#client-busStyle-input').val(data.busStyle);

            $('#client-terms').text(data.terms);
            $('#client-terms-input').val(data.terms);
        }
    });
}

function getSupplierInfo(){
    var supplierName = $('#my-supplier').val();
    $.ajax({
        type: "GET",
        url: "/getSupplierInfo",
        data: {
            name: supplierName
        },
        success: function(data) {
            $('#logo-suppName').text(data.name);

            //for input and display
            $('#supp-name-input').val(data.name);
            $('#supp-name').text(data.name);

            $('#supp-addrs-input').val(data.address+", "+data.cityAddress);
            $('#supp-addrs').text(data.address+", "+data.cityAddress);

            $('#supp-tin-input').val(data.tin);
            $('#supp-tin').text("VAT Reg. TIN "+formatTIN(data.tin));
        }
    });
}
function formatTIN(tin) {
  var formattedTIN = tin.toString().replace(/[^0-9]/g, ''); // remove any non-digit characters
  formattedTIN = formattedTIN.replace(/^(\d{3})(\d{3})(\d{3})$/, "$1-$2-$3-000");
  return formattedTIN;
}

function generatePONumber() {
    const poNum = document.getElementById('client-poNumber');
    const poNumInput = document.getElementById('client-poNumber-input');
    const timestamp = new Date().getTime(); // Get the current timestamp in milliseconds
    const randomNum = Math.floor(Math.random() * 10000); // Generate a random number between 0 and 9999
    const poNumber = `PO-${timestamp}-${randomNum}`; // Combine the timestamp and random number to create the PO number

    // Check if the PO number already exists
    const isDuplicate = Math.random() < 0.0001; // 1 in 10,000 chance of duplication
    if (isDuplicate) {
        console.warn(`Duplicate PO number detected: ${poNumber}`);
    }

    poNum.textContent = poNumber;
    poNumInput.value = poNumber;
}

function checkForDuplicateRows() {
  const rows = document.querySelectorAll('#table-body tr');
  const uniqueRows = new Map(); // store unique rows here
  const duplicateRows = new Set(); // store duplicate rows here

  rows.forEach(row => {
    const cells = row.querySelectorAll('td input');
    const rowValues = [...cells].map(cell => cell.value).join('|'); // combine all input values for this row with '|' separator
    if (uniqueRows.has(rowValues)) { // if this row is already in uniqueRows, it's a duplicate
      duplicateRows.add(row);
      uniqueRows.get(rowValues).forEach(duplicateRow => duplicateRows.add(duplicateRow));
      duplicateRows.add(row);
    } else { // otherwise, add this row to uniqueRows
      uniqueRows.set(rowValues, [row]);
    }
  });

  if (duplicateRows.size > 0) {
    alert('Found duplicate items!');
    // highlight duplicate rows
    duplicateRows.forEach(row => {
      row.querySelectorAll('input').forEach(input => {
        input.classList.add('highlight');
      });
    });
    return true; // indicate that there are duplicate rows
  } else {
    return false; // indicate that there are no duplicate rows
  }
}

const form = document.querySelector('#myForm');

form.addEventListener('submit', event => {
  if (checkForDuplicateRows()) {
    event.preventDefault(); // prevent form submission
    return false;
  } else {
    return true;
  }
});



//////////////////// table
const tableBody = document.getElementById('table-body');
const addRowBtn = document.getElementById('add-row-btn');
const deleteRowBtn = document.getElementById('delete-row-btn');
let rowCounter = 0;

addRowBtn.addEventListener('click', () => {
  if (rowCounter < 40) {
    const newRow = document.createElement('tr');
    const index = rowCounter; // Get the index for this row
    newRow.innerHTML = `
      <td>
        <input name="qty" type="number" class="compute_qty" onfocus="if (this.value == '0') { this.value = ''; }" onblur="if (this.value == '') { this.value = '0'; }" required>
      </td>
      <td>
        <input name="unit" type="text" required>
      </td>
      <td>
        <input name="article" type="text" required>
      </td>
      <td>
        <input name="unitPrice" type="text" class="compute_unit_price" required>
      </td>
      <td>
        <input name="amount" type="text" class="compute_amount" readonly>
      </td>
    `;
    tableBody.appendChild(newRow);
    const inputs = newRow.querySelectorAll('.compute_qty, .compute_unit_price');
    inputs.forEach(input => {
      input.addEventListener('input', () => {
        updateAmount(newRow);
      });
    });
    rowCounter++;
    computeInvoiceTotals();
  } else {
    alert('Maximum row limit reached!');
  }
  
});

function updateAmount(row) {
  const qty = row.querySelector('.compute_qty').value;
  const unitPrice = row.querySelector('.compute_unit_price').value;
  const amountInput = row.querySelector('.compute_amount');

  if (qty && unitPrice && qty > 0 && unitPrice > 0) {
    const amount = qty * unitPrice;
    amountInput.value = amount.toFixed(2);
  } else {
    amountInput.value = '';
  }
  computeInvoiceTotals();
}

function computeInvoiceTotals() {
  const amountInputs = document.querySelectorAll('.compute_amount');
  let totalSalesVatInc = 0;
  let totalAmountNetOfVat = 0;
  let addVat = 0;
  let grandTotal = 0;

  amountInputs.forEach(input => {
    if (input.value !== '') {
      totalSalesVatInc += parseFloat(input.value);
    }
  });

  totalAmountNetOfVat = totalSalesVatInc / 1.12;
  addVat = totalSalesVatInc - totalAmountNetOfVat;
  grandTotal = totalSalesVatInc;

  const totalSaleVatIncSpan = document.getElementById('totalSale-VatIncl');
  const amountNOVSpan = document.getElementById('amount-NOV');
  const addVATSpan = document.getElementById('add-VAT');
  const totalAmountDueSpan = document.getElementById('totalAmount-due');
  const totalSalesVatIncInput = document.getElementById('totalSalesVatInc-input');
  const amountNetOfVatInput = document.getElementById('amountNetOfVat-input');
  const addVatInput = document.getElementById('addVat-input');
  const grandTotalInput = document.getElementById('grandTotal-input');

  totalSaleVatIncSpan.textContent = '₱ ' + totalSalesVatInc.toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
  amountNOVSpan.textContent = '₱ ' + totalAmountNetOfVat.toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
  addVATSpan.textContent = '₱ ' + addVat.toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
  totalAmountDueSpan.textContent = '₱ ' + grandTotal.toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});

  totalSalesVatIncInput.value = totalSalesVatInc.toFixed(2);
  amountNetOfVatInput.value = totalAmountNetOfVat.toFixed(2);
  addVatInput.value = addVat.toFixed(2);
  grandTotalInput.value = grandTotal.toFixed(2);
}
// Delete last row
deleteRowBtn.addEventListener('click', () => {
  const rows = tableBody.getElementsByTagName('tr');
  if (rows.length > 0) {
    rows[rows.length - 1].remove();
    rowCounter--;
  }else {
       alert('At least one row is required!');
     }
  computeInvoiceTotals();
});

const clientBusStyleSpan = document.getElementById('client-busStyle');
const clientBusStyleInput = document.getElementById('client-busStyle-input');

    clientBusStyleSpan.addEventListener('input', function() {
      clientBusStyleInput.value = clientBusStyleSpan.textContent;
    });

const clientTerms = document.getElementById('client-terms');
const clientTermsInput = document.getElementById('client-terms-input');

    clientTerms.addEventListener('input', function() {
      clientTermsInput.value = clientTerms.textContent;
    });

const cashierName = document.getElementById('cashier-name');
const cashierNameInput = document.getElementById('cashier-name-input');
// Set the initial value of the input field to the text content of the cashierName element
cashierNameInput.value = cashierName.textContent;
    cashierName.addEventListener('input', function() {
      cashierNameInput.value = cashierName.textContent;
    });

// generateInvNum();

  // Invoice number pair
  const invoiceSpan = document.getElementById('invoice-number');
  const invoiceInput = document.getElementById('invoice-number-input');
  invoiceInput.value = invoiceSpan.innerText;

  const invoiceObserver = new MutationObserver(function(mutationsList) {
    for(let mutation of mutationsList) {
      if (mutation.type === 'childList') {
        invoiceInput.value = invoiceSpan.innerText;
      }
    }
  });

  invoiceObserver.observe(invoiceSpan, {childList: true});

  // PO number pair
  const poLabel = document.getElementById('client-poNumber');
  const poInput = document.getElementById('client-poNumber-input');
  poInput.value = poLabel.innerText;

  const poObserver = new MutationObserver(function(mutationsList) {
    for(let mutation of mutationsList) {
      if (mutation.type === 'childList') {
        poInput.value = poLabel.innerText;
      }
    }
  });

  poObserver.observe(poLabel, {childList: true});


  


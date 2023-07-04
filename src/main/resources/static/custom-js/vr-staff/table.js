$(function() {

      $('#table-body').on('mouseenter', 'tr', function() {
      var id = $(this).attr('id');
        if(id == "not-found"){}
        else{
            $(this).addClass('hover');}
      }).on('mouseleave', 'tr', function() {
      var id = $(this).attr('id');
        if(id == "not-found"){}
        else{
            $(this).removeClass('hover');}
      });

    $('#table-body').on('click', 'tr', function() {
        var id = $(this).attr('id');
        if(id == "not-found"){}
        else{
            $('tr').removeClass('active');
            $(this).addClass('active');}
    });

    $('tbody').on('click','tr', function() {
    event.preventDefault();
    if ($(this).hasClass('active')) {
        var invoiceNum = $(this).find('td.invoice-num').text();
        var newTab = window.open('', 'invoiceDetails', 'width=800,height=600');
        newTab.location.href = '/vr/user/invoice/invoiceDetails/' + invoiceNum;
      }

    });
var defaultRowCount = 7;
   $('#show-btn').on('click', function() {
       event.preventDefault();
       var supplierName = $('#filterBySupplier').val();
        $(this).hide();
        $('#totalPages').hide();
        $('#pagination ul').hide();
       $.ajax({
           type: "GET",
           url: "/getAllInvoiceBySupplier",
           data: {
               supplierName: supplierName
           },
           success: function(data) {
               var tableBody = $('#table-body');

               // Remove existing data rows
               tableBody.find('.table-row').remove();

               $.each(data, function(index, invoice) {
                   var row = $('<tr>').addClass('table-row');
                   row.append($('<td>').addClass('client-info').text(invoice.clientTin));
                   row.append($('<td>').addClass('client-info').text(invoice.clientName));
                   row.append($('<td>').addClass('client-info').text(invoice.clientAddress));
                   row.append($('<td>').addClass('supplier-info').text(invoice.supplierTin));
                   row.append($('<td>').addClass('supplier-info').text(invoice.supplierName));
                   row.append($('<td>').addClass('supplier-info').text(invoice.supplierAddress));
                   row.append($('<td>').text(invoice.grandTotal));

                   // Convert date format
                   var date = new Date(invoice.dateCreated);
                   var formattedDate = date.toLocaleDateString('en', {
                       month: 'long',
                       day: 'numeric',
                       year: 'numeric'
                   });
                   row.append($('<td>').text(formattedDate));

                   row.append($('<td>').addClass('invoice-num').text(invoice.invoiceNum));

                   tableBody.append(row);
               });

               // Move invoices total and not-found rows to the bottom
               var table = $('#invoice-table');
               var invoicesTotalRow = $('#invoices-total');

               table.append(invoicesTotalRow);

               $('#hide-btn').show();
           }
       });
   });
   $('#hide-btn').on('click', function() {
       var tableBody = $('#table-body');
       var rows = tableBody.find('.table-row');

       // Hide excess rows and show only default rows
       rows.hide();
       rows.slice(0, defaultRowCount).show();
       var table = $('#invoice-table');
        var invoicesTotalRow = $('#invoices-total');
        table.append(invoicesTotalRow);

       $('#show-btn').show(); // Show "Show All" button
       $('#hide-btn').hide(); // Hide "Show Default" button
       $('#totalPages').show();
       $('.pagination').show();
   });

});
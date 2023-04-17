$(function() {
function paginateTable(table, itemsPerPage) {
  // Get table rows and calculate number of pages
  const prevBtn = $("#prev");
  const nextBtn = $("#next");
  var rows = table.find('tbody tr');
  var numRows = rows.length;
  var numPages = Math.ceil(numRows / itemsPerPage);

   $('#page-numbers').empty();

  // Create pager elements
  var pager = $('#page-numbers');
  for (var i = 1; i <= numPages; i++) {
    var li = $('<a class="pager" data-page="' + i + '">' + i + '</a>');
    pager.append(li);
  }

  // Add pager to page


  // Show first page
  rows.hide();
  rows.slice(0, itemsPerPage).show();
  pager.find('a:first-child').addClass('active');


   prevBtn.on('click', function(event) {
      event.preventDefault();
       var pageNum = pager.find('a');
       var pagePrev= pageNum.data('page') - 1;
       var startIndex = (pagePrev - 1) * itemsPerPage;
       var endIndex = startIndex + itemsPerPage;

       // Show/hide rows based on page number
       rows.hide();
       rows.slice(startIndex, endIndex).show();
       pageNum.removeClass('active');
       //$(this).addClass("active");
   });


  // Handle pager clicks
  pager.on('click', 'a', function(event) {
    event.preventDefault();
    var pageNum = $(this).data('page');
    var startIndex = (pageNum - 1) * itemsPerPage;
    var endIndex = startIndex + itemsPerPage;

    // Show/hide rows based on page number
    rows.hide();
    rows.slice(startIndex, endIndex).show();

    // Update active pager item
    pager.find('a').removeClass('active');
    pager.removeClass('active');
    $(this).addClass("active");
  });
}

  $("#search-input").on("keyup", function() {
      var query = $(this).val().toLowerCase();
       event.preventDefault();

        $.ajax({
            url: '/vr/search',
            type: 'GET',
            data: {query: query},
            dataType: 'json',
            success: function(response) {
                var tbody = $('#table-body');
                tbody.empty();

                response.forEach(function(invoice) {
                    var row = $('<tr class="table-row">');
                    row.append($('<td>').text(invoice.clientName));
                    row.append($('<td>').text(invoice.clientAddress));
                    row.append($('<td>').text(invoice.tinNum));
                    row.append($('<td>').text(invoice.supplierName));
                    row.append($('<td>').text(invoice.invoiceNum));
                    row.append($('<td>').text(invoice.date));
                    tbody.append(row);
                });
            paginateTable($('#my-table'), 7);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
            }
        });
    });
 });



// $.ajax({
//             url: "/vr/1 ",
//             type: "GET",
//             dataType: "json",
//             success: function(data) {
//                 // handle the response data
//                 alert(data.clientName);
//                 console.log("Success");
//             },
//             error: function(jqXHR, textStatus, errorThrown) {
//                 console.log("Error: " + textStatus, errorThrown);
//             }
//         });
//
//    function getInvoice(page, size) {
//        $.ajax({
//            type: 'GET',
//            url: '/vr?page=' + page + '&size=' + size,
//            success: function(data) {
//                usersTable.find('tbody').empty();
//                $.each(data.content, function(index, invoice) {
//                    usersTable.find('tbody').append('<tr class="table-row">' +
//                        '<td>' + invoice.clientName + '</td>' +
//                        '<td>' + invoice.clientAddress + '</td>' +
//                        '<td>' + invoice.tinNum + '</td>' +
//                        '<td>' + invoice.supplierName + '</td>' +
//                        '<td>' + invoice.invoiceNum + '</td>' +
//                        '<td>' + invoice.date + '</td>' +
//                    '</tr>');
//                });
//                pagination.twbsPagination({
//                    totalPages: data.totalPages,
//                    startPage: data.number,
//                    visiblePages: 5,
//                    onPageClick: function(event, page) {
//                        currentPage = page - 1;
//                        getInvoice(currentPage, pageSize);
//                    }
//                });
//            }
//        });
//    }

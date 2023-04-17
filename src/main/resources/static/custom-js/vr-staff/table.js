$(function() {

function paginateTable(table, itemsPerPage) {
  // Get table rows and calculate number of pages
  var prevBtn = $("#prev");
  var nextBtn = $("#next");
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

  // Show first page
  rows.hide();
  rows.slice(0, itemsPerPage).show();
  pager.find('a:first-child').addClass('active');

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

    $(this).addClass("active");
  });
  nextBtn.on('click', function(event) {
      event.preventDefault();
      $('#page-numbers').children('a').each(function() {
        if($(this).hasClass('active'))
        {
            var currentPage = $(this).data('page');
            var nextPage = currentPage + 1;
            var startIndex = (nextPage - 1) * itemsPerPage;
            var endIndex = startIndex + itemsPerPage;
            var nextButton =  $('.pager').data('page', nextPage);
            // Show/hide rows based on page number
            rows.hide();
            rows.slice(startIndex, endIndex).show();

            // Update active pager item
            $(this).removeClass('active');
            pager.removeClass('active');
            $('.pager[data-page="'+nextPage+'"]').addClass('active');
        }
      });
    });
}
    paginateTable($('#my-table'), 7);
      $('#table-body').on('mouseenter', 'tr', function() {
        $(this).addClass('hover');
      }).on('mouseleave', 'tr', function() {
        $(this).removeClass('hover');
      });

    $('#table-body').on('click', 'tr', function() {
        $('tr').removeClass('active');
        $(this).addClass('active');
    });

    $('td').on('click', function() {
      setTimeout(function() {
            var newTab = window.open('', 'myNewTab', 'width=800,height=600');
            newTab.location.href = '/vr/user/invoice/getinvoice';
         }, 500);

    });
});
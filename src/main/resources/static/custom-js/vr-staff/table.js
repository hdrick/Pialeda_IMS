$(function() {
      $('#table-body').on('mouseenter', 'tr', function() {
        $(this).addClass('hover');
      }).on('mouseleave', 'tr', function() {
        $(this).removeClass('hover');
      });

    $('#table-body').on('click', 'tr', function() {
        $('tr').removeClass('active');
        $(this).addClass('active');
    });

    $('tbody').on('click','tr', function() {
    event.preventDefault();
    if ($(this).hasClass('active')) {
        var invoiceNum = $(this).find('td:first-child').text();
        var newTab = window.open('', 'invoiceDetails', 'width=800,height=600');
        newTab.location.href = '/vr/user/invoice/invoiceDetails/' + invoiceNum;
      }

    });

});
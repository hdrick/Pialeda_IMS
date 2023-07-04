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

});
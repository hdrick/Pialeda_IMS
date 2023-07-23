(function($) {
  $.saveAs = function(blob, filename) {
    saveAs(blob, filename);
  };
})(jQuery);

$(document).ready(function() {
  $('#download-btn').click(function() {
    // Get the table element
    var table1 = document.getElementById('invoice-table');

    // Create a new table to store visible rows and columns
    var visibleTable = document.createElement('table');
    visibleTable.className = table1.className; // Copy the classes if needed

    // Copy visible rows and columns to the new table
    $(table1).find('tr').each(function() {
      if ($(this).is(':visible')) {
        var newRow = visibleTable.insertRow();
        $(this).find('td, th').each(function() {
          if ($(this).is(':visible')) {
            var newCell = newRow.insertCell();
            newCell.innerHTML = $(this).html();
          }
        });
      }
    });

    // Convert the new table to a worksheet object
    var worksheet1 = XLSX.utils.table_to_sheet(visibleTable);

    // Create a workbook with the worksheet(s)
    var workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet1, 'Invoice');

    // Convert the workbook to a binary string
    var binaryString = XLSX.write(workbook, { bookType: 'xlsx', type: 'binary' });

    // Generate a timestamp as the download name
    var timestamp = new Date().getTime();
    var filename = timestamp + '.xlsx';

    // Save the file
    saveAs(new Blob([s2ab(binaryString)], {type: "application/octet-stream"}), filename);
  });
});

// Utility function to convert a string to an ArrayBuffer
function s2ab(s) {
  var buf = new ArrayBuffer(s.length);
  var view = new Uint8Array(buf);
  for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
  return buf;
}
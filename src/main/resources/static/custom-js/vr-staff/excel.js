(function($) {
  $.saveAs = function(blob, filename) {
    saveAs(blob, filename);
  };
})(jQuery);

$(document).ready(function() {
  $('#download-btn').click(function() {
    // Get the table elements
    var table1 = document.getElementById('invoice-table');
//    var table2 = document.getElementById('receipt-table');

    // Convert the tables to worksheet objects
    var worksheet1 = XLSX.utils.table_to_sheet(table1);

    // Create a workbook with the worksheet(s)
    var workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet1, 'Invoice');

    // Check if table2 is not null
//    if (table2 !== null) {
//      var worksheet2 = XLSX.utils.table_to_sheet(table2);
//      XLSX.utils.book_append_sheet(workbook, worksheet2, 'OR');
//    }

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

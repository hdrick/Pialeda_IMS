(function($) {
  $.saveAs = function(blob, filename) {
    saveAs(blob, filename);
  };
})(jQuery);

$(document).ready(function() {
  $('#download-btn').click(function() {
    // Get the table elements
    var table1 = document.getElementById('my-table');
    var table2 = document.getElementById('my-table-total');

// Check if the tables have data
    if (table1.rows.length === 0 || table2.rows.length === 0) {
      // Handle the case when tables are empty
      alert('Tables are empty. Please populate the tables before downloading.');
      return;
    }
    // Convert the tables to worksheet objects
    var worksheet1 = XLSX.utils.table_to_sheet(table1);
    var worksheet2 = XLSX.utils.table_to_sheet(table2);

    // Create a workbook with the worksheets
    var workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet1, 'Invoice');
    XLSX.utils.book_append_sheet(workbook, worksheet2, 'Total');

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

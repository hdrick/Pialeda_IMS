    (function($) {
  $.saveAs = function(blob, filename) {
    saveAs(blob, filename);
  };
})(jQuery);


  $(document).ready(function() {
  $('#download-btn').click(function() {
    // Get the table element
    var table = document.getElementById('my-table');
    // Get the values of all dynamic parameters from the URL
    var urlParams = new URLSearchParams(window.location.search);
    var params = {};

    // Loop through all parameter names
    urlParams.forEach(function(value, name) {
      // Add the parameter value to the "params" object
      params[name] = value+"_";
    });

    // Convert the table to a worksheet object
    var worksheet = XLSX.utils.table_to_sheet(table);
    // Create a workbook with the worksheet
    var workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
    // Convert the workbook to a binary string
    var binaryString = XLSX.write(workbook, { bookType: 'xlsx', type: 'binary' });

    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth() + 1;
    var year = today.getFullYear();

    var time = today.toLocaleTimeString('en-US', {hour12: false});

    var cleanName = day + '/' + month + '/' + year+'/'+time+'.xlsx';


    // Save the file
    saveAs(new Blob([s2ab(binaryString)], {type:"application/octet-stream"}), cleanName);
  });
});

// Utility function to convert a string to an ArrayBuffer
function s2ab(s) {
  var buf = new ArrayBuffer(s.length);
  var view = new Uint8Array(buf);
  for (var i=0; i<s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
  return buf;
}
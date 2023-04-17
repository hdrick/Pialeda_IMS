        const tableBody = $("#table-body");
        const prevBtn = $("#prev");
        const nextBtn = $("#next");
        const pageNumber = $("#page-number");
        const pageNumbers = $("#page-numbers");

        let currentPage = 1;
        const itemsPerPage = 6;
        const rows = tableBody.find("tr");
        const totalItems = rows.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage);

        const showPage = (page) => {
          // First, hide all the rows
          rows.hide();

          // Then, show only the rows for the current page
          const startIndex = (page - 1) * itemsPerPage;
          const endIndex = page * itemsPerPage - 1;
          rows.slice(startIndex, endIndex + 1).show();

          pageNumber.text(`Page ${page} of ${totalPages}`);

          // Remove the active class from all the page numbers
          pageNumbers.find("a").removeClass("active");

          // Add the active class to the current page number
          pageNumbers.find(`a[data-page=${page}]`).addClass("active");

        };

        prevBtn.click(() => {
          if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
          }
        });

        nextBtn.click(() => {
          if (currentPage < totalPages) {
            currentPage++;
            showPage(currentPage);
          }
        });

        for (let i = 1; i <= totalPages; i++) {
          pageNumbers.append(`<a class="pager" data-page="${i}">${i}</a>`);
        }

        pageNumbers.find("a").click(function() {
          currentPage = $(this).data("page");
          showPage(currentPage);
        });

        showPage(currentPage);
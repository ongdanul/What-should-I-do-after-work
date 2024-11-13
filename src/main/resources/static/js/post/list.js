document.addEventListener("DOMContentLoaded", function () {

    const rows = document.querySelectorAll(".card tr");
    if (rows.length >= 2) {
        const targetRow = rows[rows.length - 1];
        targetRow.style.borderBottom = "none";

        const cells = targetRow.querySelectorAll("td");
        cells.forEach(cell => {
            cell.style.borderBottom = "none";
        });
    }
});
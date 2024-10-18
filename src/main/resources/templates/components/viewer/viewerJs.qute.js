let pageGrid = $("#pageGrid");

function findGutters(rowCol){
    let output = [];
    pageGrid.find(".gutter-"+rowCol).each(function(){
        let curGutter = $(this);

        output.push({
            track: curGutter.data("bs-track"),
            element: this
        })
    });
    return output;
}

Split({
    columnGutters: findGutters("col"),
    rowGutters: findGutters("row")
});

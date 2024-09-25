$(".card-carousel").each(function(i, multipleCardCarouselDiv){
    console.log("Found card carousel: ", multipleCardCarouselDiv);
    let multipleCardCarouselJq = $(multipleCardCarouselDiv);
    if (window.matchMedia("(min-width: 101px)").matches) {
        let carousel = new bootstrap.Carousel(multipleCardCarouselDiv, {
            interval: false,
        });
        let carouselWidth = multipleCardCarouselJq.find(".carousel-inner")[0].scrollWidth;
        let cardWidth = multipleCardCarouselJq.find(".carousel-item").width();
        let scrollPosition = 0;
        multipleCardCarouselJq.find(".carousel-control-next").on("click", function () {
            console.log("Next Clicked")
            if (scrollPosition < carouselWidth - cardWidth * 4) {
                scrollPosition += cardWidth;
                multipleCardCarouselJq.find(".carousel-inner").animate(
                    { scrollLeft: scrollPosition },
                    600
                );
            }
        });
        multipleCardCarouselJq.find(".carousel-control-prev").on("click", function () {
            console.log("Prev Clicked")
            if (scrollPosition > 0) {
                scrollPosition -= cardWidth;
                multipleCardCarouselJq.find(".carousel-inner").animate(
                    { scrollLeft: scrollPosition },
                    600
                );
            }
        });
    } else {
        multipleCardCarouselJq.addClass("slide");
    }
});
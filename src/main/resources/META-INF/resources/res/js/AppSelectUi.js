/**
 * Utilities to handle app selection, navigation, and app bar behavior.
 *
 * Requires Jquery, html for app bar ("subheader-auth.qute.html")
 */
let AppSelectUi = {
	appSelectBar: $("#appSelectBar"),
	filterInput: $("#appSelectBarFilterInput"),
	appsDisplay: $("#appSelectAppsDisplay"),
	appCards: $("#appSelectAppsDisplay .appSelectCard"),
	clearFilterButton: $("#appSelectBarFilterClearInput"),

	/**
	 * Toggles the visibility of the app select bar.
	 */
	toggleAppBar(){
		this.appSelectBar.toggle();
	},

	/**
	 * Opens an app.
	 * @param appRef The reference of the app to open.
	 * @param newWindow If to open in a new window or not.
	 * @param iwcMessage The message to prime the new app window with.
	 */
	openApp(appRef, newWindow = false){
		azideWin.launchApp(appRef, newWindow);
	},

	/**
	 * Filters the visible apps based on what is in the search bar.
	 */
	filterApps(){
		let searchText = this.filterInput.val().toLowerCase().trim();
		console.debug("Filtering based on text: ", searchText);
		// console.debug("Searching through apps: ", this.appCards);

		if(searchText !== ""){
			this.clearFilterButton.prop("disabled", false);
			this.appCards.each(function(){
				let thisJq = $(this);
				let curAppText = thisJq.attr("data-searchText");
				// console.debug("Search text: ", curAppText, thisJq);

				if(curAppText.indexOf(searchText) !== -1){
					thisJq.show();
				} else {
					thisJq.hide();
				}
			});
		} else {
			this.clearFilterButton.prop("disabled", true);
			this.appCards.show();
		}
	},
	clearFilter(){
		this.filterInput.val("");
		this.filterApps();
	}
}
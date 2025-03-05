// ====================================================
// Functions etc for profile viewer/editor
// ====================================================
AzideProfile = {
	toggleFavorite(favSpan, appRef) {
		console.log("Toggling favorite status of ", appRef);
		let favoritedIcon = favSpan.find(".favIconFavorited");
		let notFavoritedIcon = favSpan.find(".favIconNotFavorited");

		if (favoritedIcon.is(":visible")) {
			AzideProfile.deleteFavorite(appRef);

			// Show/Hide correct favorite icon
			favoritedIcon.hide();
			notFavoritedIcon.show();
		} else {
			AzideProfile.addFavorite(appRef);

			// Show/Hide correct favorite icon
			favoritedIcon.show();
			notFavoritedIcon.hide();
		}

	},
	addFavorite(appRef) {
		console.log("Adding favorite status of ", appRef);
		fetch('/api/profile/favorite/' + appRef, {
			method: 'PUT'
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Error adding favorite app: ' + response.statusText);
				}
				console.log("Added favorite status of ", appRef, response);
				return response.json();
			})
			.catch(error => {
				console.error('Error adding favorite app:', error);
				// showNotification('Error adding favorite app: ' + error, 'danger');//TODO:: figure out
			});
	},
	deleteFavorite(appRef) {
		console.log("Removing favorite status of ", appRef);
		fetch('/api/profile/favorite/' + appRef, {
			method: 'DELETE'
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Error deleting favorite app: ' + response.statusText);
				}
				console.log("Deleted favorite status of ", appRef, response);
				return response.json();
			})
			.catch(error => {
				console.error('Error deleting favorite app:', error);
				// showNotification('Error deleting favorite app: ' + error, 'danger');//TODO:: figure out
			});
	}
}

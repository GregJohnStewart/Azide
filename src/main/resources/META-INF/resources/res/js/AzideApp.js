
/**
 */
class AzideApp {
    #iwc;
    #windowName = "";

    /**
     * @param {string|null} appName (optional, but recommended) the name of this app.
     * @param {string} network "Network" in this case refers to the "network" of windows that want to talk to each other. All channels made will be prepended with this to provide the ability to separate from other instances/networks.
     */
    constructor({
                    appName = "azideApp",
                    network = "iwc"
                }) {
        this.#iwc = new Iwc(new IwcConfig({
            appName: appName,
            network: network
        }));

        let azideWindow = window.parent.azideWin;
        console.debug("Azide Window: ", azideWindow);

        if(azideWindow == null) {
            console.info("Azide Window not found.");
        } else {
            this.#windowName = azideWindow.getName();
            console.info("Parent window channel name: ", this.#windowName);
        }
    }

    haveWindow(){
        return this.#windowName != null;
    }

    assertHaveWindow(){
        if(!this.haveWindow()){
            throw "This page does not have a window."
        }
    }

    getWindowChannel(){
        this.assertHaveWindow();
        return this.#iwc.getChannel(this.#windowName);
    }

}

/**
 * These classes define the methodology of communicating between browsing contexts of different apps hosted at the same hostname.
 *
 *
 */


/**
 */
class AzideApp {
    #iwc;

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
    }

}

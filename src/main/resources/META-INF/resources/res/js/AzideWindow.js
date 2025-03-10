/**
 * These classes define the methodology of communicating between browsing contexts of different apps hosted at the same hostname.
 *
 *
 */


/**
 * The main configuration object to inform the setup of the {@link Iwc} object.
 */
class AzideWindow {
    #iwc;

    /**
     * @param {string|null} appName (optional, but recommended) the name of this app.
     * @param {string} network "Network" in this case refers to the "network" of windows that want to talk to each other. All channels made will be prepended with this to provide the ability to separate from other instances/networks.
     * @param {string} allChannel The channel to use to broadcast to all messages in network. Recommend not changing.
     * @param roleCallChannel The channel used to communicate to other windows what windows are open. Recommend not changing.
     * @param nameSeparator The character used to separate channel names. Recommend not changing.
     */
    constructor({
                    appName = "azideWindow",
                    network = "iwc"
                }) {
        this.#iwc = new Iwc(new IwcConfig({
            appName: appName,
            network: network
        }));

        //TODO:: load list of available apps

        this.#iwc.registerHandler(
            this.#iwc.getChannels().getThisWindowChannel(),
            this.#thisWindowMessageHandler.bind(this)
        );
    }

    #thisWindowMessageHandler(message) {
        switch (message.getIntent()) {
            case IwcConstants.intents.launchApp:
                this.launchApp(message.getMessage());
                break;
            default:
                console.warn("Got message with intent that the window doesn't support: ", message);
        }
    }

    launchApp(launching){
        //TODO:: launch app
    }

    getName(){
        return this.#iwc.getThisChannelName();
    }
}

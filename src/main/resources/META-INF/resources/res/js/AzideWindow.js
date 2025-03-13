/**
 * These classes define the methodology of communicating between browsing contexts of different apps hosted at the same hostname.
 *
 * Functionalities:
 *
 * - process window open messages
 * - process window close messages
 *
 */


/**
 * The main configuration object to inform the setup of the {@link Ifc} object.
 */
class AzideWindow {
    #ifc;
    #appPageFrameJq = $("#appframe");

    /**
     * @param {string|null} appName (optional, but recommended) the name of this app.
     * @param {string} network "Network" in this case refers to the "network" of windows that want to talk to each other. All channels made will be prepended with this to provide the ability to separate from other instances/networks.
     * @param {string} allChannel The channel to use to broadcast to all messages in network. Recommend not changing.
     * @param roleCallChannel The channel used to communicate to other windows what windows are open. Recommend not changing.
     * @param nameSeparator The character used to separate channel names. Recommend not changing.
     */
    constructor({
                    appName = "azideWindow",
                    network = "ifc"
                }) {
        console.log("======== Initializing new Azide window frame: ", appName);
        this.#ifc = new Ifc(new IfcConfig({
            appName: appName,
            network: network
        }));

        //TODO:: load list of available apps

        this.#ifc.registerMessageHandler(
            this.#ifc.getChannels().getThisFrameChannel(),
            this.#thisFrameMessageHandler.bind(this)
        );
        console.debug("Finished initializing new Azide window frame: ", this.#ifc.getFrameId());
    }

    #thisFrameMessageHandler(message) {
        switch (message.getIntent()) {
            case IfcConstants.intents.launchApp:
                this.launchApp(message.getMessage());
                break;
            //TODO:: app open check
            //TODO:: app close
            default:
                console.warn("Got message with intent that the frame doesn't support: ", message);
        }
    }

    launchApp(appRef, newWindow = false, iwcMessage = null){
        let params = new URLSearchParams();
        params.set("appRef", appRef);
        if(iwcMessage != null){
            params.set("intent", intent);
            params.set("message", message);
            //TODO:: more?
        }

        let newUri = "/app/viewer?" + params.toString();

        console.log("Navigating to new app: ", appRef, newUri)
        if(newWindow){
            window.open(
                newUri,
                '_blank',
                'menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'
            );
        } else {
            window.location.href = newUri;
        }
    }

    getFrameId(){
        return this.#ifc.getFrameId();
    }

    getAvailableApps(){
        return apps;
    }

    appIsOpen(appRef) {
        //TODO
        return false;
    }

    getOpenAppInstances(appRef){
        let output = [];
        //TODO
        return output;
    }

    getIfc(){
        return this.#ifc;
    }
}

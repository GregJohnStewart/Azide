/**
 * These classes define the methodology of communicating between browsing contexts of different apps hosted at the same hostname.
 *
 *
 */


/**
 * The main configuration object to inform the setup of the {@link Iwc} object.
 */
class IwcConfig {
    #channelNetwork;
    #allChannel;
    #roleCallChannel;
    #appName;
    #windowId;
    #nameSeparator;

    /**
     * @param {string|null} appName (optional, but recommended) the name of this app.
     * @param {string} network "Network" in this case refers to the "network" of windows that want to talk to each other. All channels made will be prepended with this to provide the ability to separate from other instances/networks.
     * @param {string} allChannel The channel to use to broadcast to all messages in network. Recommend not changing.
     * @param roleCallChannel The channel used to communicate to other windows what windows are open. Recommend not changing.
     * @param nameSeparator The character used to separate channel names. Recommend not changing.
     */
    constructor({
                    appName = null,
                    network = "iwc",
                    allChannel = "all",
                    roleCallChannel = "roleCall",
                    nameSeparator = "-"
                }) {
        this.#channelNetwork = network;
        this.#allChannel = allChannel;
        this.#roleCallChannel = roleCallChannel;
        this.#appName = appName;
        this.#nameSeparator = nameSeparator;

        this.#windowId = (
            this.hasAppName() ?
                "" :
                this.#appName + "-"
        ) + crypto.randomUUID();
    }

    getChannelNetwork() {
        return this.#channelNetwork;
    }

    getAllChannel() {
        return this.#allChannel;
    }

    /**
     *
     * @returns {*}
     */
    getRoleCallChannel() {
        return this.#roleCallChannel;
    }

    getAppName() {
        return this.#appName;
    }

    hasAppName() {
        return this.#appName == null || this.#appName === "";
    }

    getWindowId() {
        return this.#windowId;
    }

    getNameSeparator() {
        return this.#nameSeparator;
    }
}

class IwcMessage {
    #id;
    #appName;
    #windowId;
    #sendTime;

    #intent;
    #message;

    /**
     * Creates a new message object.
     *
     * While this is usable, the intent is to use the static {@link create()} method.
     *
     * @param appName
     * @param windowId
     * @param sendTime
     * @param intent
     * @param message
     * @param id
     */
    constructor({
                    appName,
                    windowId,
                    sendTime,
                    intent,
                    message = null,
                    id = crypto.randomUUID()
                }) {

        this.#id = id;
        this.#appName = appName;
        this.#windowId = windowId;
        this.#sendTime = sendTime;
        this.#intent = intent;
        this.#message = message;
    }

    /**
     * Creates a new message to send. This is the preferred method, as opposed to the constructor.
     * @param {IwcConfig} config The configuration used to setup IWC for this window.
     * @param {string|Object} intent The intent to send.
     * @param {string|Object|null} message (Optional) The message to send.
     * @returns {IwcMessage} The fully built and ready to send message.
     */
    static create(config, intent, message = null) {
        return new IwcMessage({
                appName: config.getAppName(),
                windowId: config.getWindowId(),
                sendTime: new Date().getTime(),
                intent: intent,
                message: message
            }
        );
    }

    /**
     * Method to serialize a given message.
     * @param {IwcMessage} message The message to serialize.
     * @returns {string} The serialized message.
     */
    static #serialize(message) {
        console.debug("Serializing message: ", message);
        let plain = {
            id: message.getId(),
            appName: message.getAppName(),
            windowId: message.getWindowId(),
            sendTime: message.getSendTime(),
            intent: message.getIntent(),
            message: message.getMessage()
        };
        console.debug("Plain message: ", plain);
        return JSON.stringify(plain);
    }

    /**
     * Serializes this instance as a string.
     * @returns {string}
     */
    serialize() {
        return IwcMessage.#serialize(this);
    }

    /**
     * Deserializes a message from a string.
     * @param messageStr
     * @returns {IwcMessage}
     */
    static deserialize(messageStr) {
        return new IwcMessage(JSON.parse(messageStr));
    }

    getId() {
        return this.#id;
    }

    getAppName() {
        return this.#appName;
    }

    getWindowId() {
        return this.#windowId;
    }

    getSendTime() {
        return this.#sendTime;
    }

    getIntent() {
        return this.#intent;
    }

    getMessage() {
        return this.#message;
    }
}

/**
 *
 */
class IwcChannels {
    #config;
    #all;
    #roleCall;
    #app;
    #thisWindow;
    /**
     * Set of broadcasters for other apps/windows.
     *
     * Example: {
     *     "appName": BroadcastChannel,
     *     "appName-windowId: BroadcastChannel
     * }
     */
    #others;

    constructor(config) {
        console.log("Setting up broadcast channels.");
        this.#config = config;
        this.#all = new BroadcastChannel(IwcChannels.#formatAllChannelName(this.#config));
        this.#roleCall = new BroadcastChannel(IwcChannels.#formatRoleCallChannelName(this.#config));
        this.#app = new BroadcastChannel(IwcChannels.#formatThisAppChannelName(this.#config));
        this.#thisWindow = new BroadcastChannel(IwcChannels.#formatThisWindowChannelName(this.#config));

        this.#others = {};

        console.log("Done setting up broadcast channels.");
    }

    /**
     * Ensures that the app and window both exist in the set of other
     * @param appName
     * @param windowId
     */
    ensureInOthers(appName, windowId) {
        // Ensure we have other apps in, but not for this app (we already have that)
        if (appName != null && this.#config.getAppName() !== appName && !(appName in this.getOtherChannels())) {
            console.log("Adding " + appName + " app to other channels.");
            this.getOtherChannels()[appName] = new BroadcastChannel(IwcChannels.#formatAppChannelName(this.#config, appName));
        }

        if (!(windowId in this.getOtherChannels())) {
            console.log("Adding " + windowId + " window to other channels.");
            this.getOtherChannels()[windowId] = new BroadcastChannel(IwcChannels.#formatOtherWindowChannelName(this.#config, windowId));
        }
    }

    removeFromOthers(appName, windowId) {
        if (windowId in this.getOtherChannels()) {
            console.log("Removing channel ", windowId);
            let otherWindowChannel = this.getOtherChannels()[windowId];
            delete this.getOtherChannels()[windowId];
            otherWindowChannel.close();
        }

        //if no other windows under app exist, remove other app.
        let otherWindowKeyPrepend = appName;
        if (
            appName != null &&
            !Object.keys(this.getOtherChannels()).some(function (k) {
                return ~k.indexOf(otherWindowKeyPrepend);
            }) &&
            appName in this.getOtherChannels()
        ) {
            console.log("No other windows exist in this app. Removing other app.");
            let otherAppChannel = this.getOtherChannels()[appName];
            delete this.getOtherChannels()[appName];
            otherAppChannel.close();
        }
    }

    appOpen(appName) {
        return this.#config.getAppName() === appName || appName in this.getOtherChannels();
    }

    windowOpen(windowId) {
        return windowId in this.getOtherChannels();
    }

    static #formatAllChannelName(config) {
        return config.getChannelNetwork() + config.getNameSeparator() + config.getAllChannel();
    }

    static #formatRoleCallChannelName(config) {
        return config.getChannelNetwork() + config.getNameSeparator() + config.getRoleCallChannel();
    }

    static #formatAppChannelName(config, appName) {
        return config.getChannelNetwork() + config.getNameSeparator() + appName;
    }

    static #formatThisAppChannelName(config) {
        return IwcChannels.#formatAppChannelName(config, config.getAppName());
    }

    static #formatOtherWindowChannelName(config, windowId) {
        return config.getChannelNetwork() + config.getNameSeparator() + windowId;
    }

    static #formatThisWindowChannelName(config) {
        if (config.hasAppName()) {
            return config.getChannelNetwork() + config.getNameSeparator() + config.getAppName() + config.getNameSeparator() + config.getWindowId();
        } else {
            return config.getChannelNetwork() + config.getNameSeparator() + config.getWindowId();
        }


    }

    /**
     *
     * @returns {BroadcastChannel}
     */
    getAllChannel() {
        return this.#all;
    }

    /**
     *
     * @returns {BroadcastChannel}
     */
    getRoleCallChannel() {
        return this.#roleCall;
    }

    /**
     *
     * @returns {BroadcastChannel}
     */
    getAppChannel() {
        return this.#app;
    }

    /**
     *
     * @returns {BroadcastChannel}
     */
    getThisWindowChannel() {
        return this.#thisWindow;
    }

    /**
     *
     * @returns {Object}
     */
    getOtherChannels() {
        return this.#others;
    }

    getOtherChannel(key) {
        return this.getOtherChannels()[key];
    }

    getOtherWindowChannel(windowId) {
        return this.getOtherChannel(windowId);
    }

    getOtherAppChannel(appName) {
        return this.getOtherChannel(appName);
    }
}

/**
 * The main class that manages an instance of inter-window communication.
 *
 * Automatically handles rolecalls in order for all windows to be aware of every other window.
 *
 * Intended to be used with other Iwc instances on other windows, with the same channel
 *
 * <pre>
 * let iwc = new Iwc(new IwcConfig({ appName: "test" }));
 * </pre>
 */
class Iwc {
    static #roleCallIntentCall = "CALL"
    static #roleCallIntentHere = "HERE"
    static #roleCallIntentExit = "EXIT"

    #config;
    #channels;

    constructor(config) {
        if (!config instanceof IwcConfig) {
            throw "Config must be an instance of IwcConfig.";
        }

        this.#config = config;
        this.#channels = new IwcChannels(this.#config);

        this.registerHandler(this.getChannels().getRoleCallChannel(), this.#roleCallChannelHandler.bind(this));

        window.addEventListener('unload', () => {
            this.close();
        });

        this.sendMessage(this.#channels.getRoleCallChannel(), IwcMessage.create(this.getConfig(), Iwc.#roleCallIntentCall));

        console.log(
            "New IWC is initialized. " +
            "App: " + this.#config.getAppName() + " " +
            "WindowId: " + this.#config.getWindowId()
        );
        console.debug("IWC Config: ", this.#config);
    }

    /**
     * This is the default handler to take in raw messages from te channels.
     *
     * Intended use like:
     * <pre>
     * this.#channels.getRoleCallChannel().onmessage = (event) => this.#msgReceived(event, this.#roleCallChannelHandler);
     * </pre>
     * @param {MessageEvent} event The raw message from the BroadcastChannel
     * @param {function} handler The handler to further handle the deserialized message.
     * @returns {*} What is returned by the handler.
     */
    #msgReceived(event, handler) {
        console.debug("New event received: ", event);
        let message = IwcMessage.deserialize(event.data);

        console.debug("Received a new message on channel '" + event.target.name + "': ", message);

        return handler(message);
    }

    /**
     * registers a handler with a channel, wrapping it in the default handler.
     * @param {BroadcastChannel} channel
     * @param {function} handler
     */
    registerHandler(channel, handler) {
        channel.onmessage = (event) => this.#msgReceived(event, handler);
    }

    /**
     * Handler for role call messages.
     * Broadcasts back out a "HERE" on a "call" message so everyone can know who is open.
     * @param {IwcMessage} message
     */
    #roleCallChannelHandler(message) {
        console.log("Received new role call message: ", message);

        switch (message.getIntent()) {
            case Iwc.#roleCallIntentCall:
                console.log("Calling HERE for rolecall.");
                this.sendMessage(this.getChannels().getRoleCallChannel(), IwcMessage.create(this.getConfig(), Iwc.#roleCallIntentHere));
                this.#channels.ensureInOthers(message.getAppName(), message.getWindowId());
                break;
            case Iwc.#roleCallIntentHere:
                console.debug("Received a HERE from rolecall.");
                this.#channels.ensureInOthers(message.getAppName(), message.getWindowId());
                break;
            case Iwc.#roleCallIntentExit:
                console.debug("Exit message received.");
                this.getChannels().removeFromOthers(message.getAppName(), message.getWindowId());
                break;
            default:
                console.warn("Did not receive a recognized role call related message.");
        }
    }

    /**
     * Sends a message on a given channel.
     * @param {BroadcastChannel} channel
     * @param {IwcMessage} message
     */
    sendMessage(channel, message) {
        console.log("Sending message: ", message);
        channel.postMessage(message.serialize());
    }

    /**
     * Responds to a given message with another message. Determines who to send it to based on the incoming message, sends to specific window.
     * @param {IwcMessage} received The message we are responding to.
     * @param {IwcMessage} message The message we are sending in response.
     */
    respondToMessage(received, message) {
        this.sendMessage(
            this.getChannels().getOtherWindowChannel(received.getWindowId()),
            message
        );
    }

    /**
     * @returns {IwcConfig}
     */
    getConfig() {
        return this.#config;
    }

    /**
     * @returns {IwcChannels}
     */
    getChannels() {
        return this.#channels;
    }

    getChannel(name) {
        return this.getChannels()[name];
    }

    getThisChannelName() {
        return this.getChannels().getThisWindowChannel().name;
    }

    /**
     * Closes out this Iwc Object.
     *
     * Given we expect the whole page to be closing, we don't explicitly close channels, but that might be something to do in the future.
     */
    close() {
        console.log("Closing IWC object.");
        this.sendMessage(this.#channels.getRoleCallChannel(), IwcMessage.create(this.getConfig(), Iwc.#roleCallIntentExit));
        //TODO:: determine if this is adequate, or if we need to do anything else, clean-wise to clear dead windows
    }
}

class IwcConstants {
    intents = {
        launchApp: "LAUNCH_APP"
    }
}
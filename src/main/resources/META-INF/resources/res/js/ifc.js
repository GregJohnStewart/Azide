/**
 * These classes define the methodology of communicating between browsing contexts of different apps hosted at the same hostname.
 *
 * Glossary:
 *
 *  - Window - an open browser tab/window. The intuitive definition.
 *  - Frame - a particular web page context; when displaying an app there are two in one window; the "AzideWindow", and the app within that one
 *  - app name - The name of a running app. Multiple instances of an app can be opened, differentiated by a specific frame id
 *  - frame id - The particular id of a given frame.
 *  - network - the context network all communication happens in. Used to segment communications (if needed)
 *
 */


/**
 * The main configuration object to inform the setup of the {@link Ifc} object.
 */
class IfcConfig {
	#channelNetwork;
	#allChannel;
	#roleCallChannel;
	#appName;
	#frameId;
	#nameSeparator;

	/**
	 * @param {string|null} appName (optional, but recommended) the name of this app.
	 * @param {string} network "Network" in this case refers to the "network" of frames that want to talk to each other. All channels made will be prepended with this to provide the ability to separate from other instances/networks.
	 * @param {string} allChannel The channel to use to broadcast to all messages in network. Recommend not changing.
	 * @param roleCallChannel The channel used to communicate to other frames what frames are open. Recommend not changing.
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

		this.#frameId = (
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

	getFrameId() {
		return this.#frameId;
	}

	getNameSeparator() {
		return this.#nameSeparator;
	}
}

class IfcMessage {
	#id;
	#appName;
	#frameId;
	#sendTime;

	#intent;
	#message;

	/**
	 * Creates a new message object.
	 *
	 * While this is usable, the intent is to use the static {@link create()} method.
	 *
	 * @param appName
	 * @param frameId
	 * @param sendTime
	 * @param intent
	 * @param message
	 * @param id
	 */
	constructor({
					appName,
					frameId,
					sendTime,
					intent,
					message = null,
					id = crypto.randomUUID()
				}) {

		this.#id = id;
		this.#appName = appName;
		this.#frameId = frameId;
		this.#sendTime = sendTime;
		this.#intent = intent;
		this.#message = message;
	}

	/**
	 * Creates a new message to send. This is the preferred method, as opposed to the constructor.
	 * @param {IfcConfig} config The configuration used to setup IWC for this frame.
	 * @param {string|Object} intent The intent to send.
	 * @param {string|Object|null} message (Optional) The message to send.
	 * @returns {IfcMessage} The fully built and ready to send message.
	 */
	static create(config, intent, message = null) {
		return new IfcMessage({
				appName: config.getAppName(),
				frameId: config.getFrameId(),
				sendTime: new Date().getTime(),
				intent: intent,
				message: message
			}
		);
	}

	/**
	 * Method to serialize a given message.
	 * @param {IfcMessage} message The message to serialize.
	 * @returns {string} The serialized message.
	 */
	static #serialize(message) {
		console.debug("Serializing message: ", message);
		let plain = {
			id: message.getId(),
			appName: message.getAppName(),
			frameId: message.getFrameId(),
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
		return IfcMessage.#serialize(this);
	}

	/**
	 * Deserializes a message from a string.
	 * @param messageStr
	 * @returns {IfcMessage}
	 */
	static deserialize(messageStr) {
		return new IfcMessage(JSON.parse(messageStr));
	}

	getId() {
		return this.#id;
	}

	getAppName() {
		return this.#appName;
	}

	getFrameId() {
		return this.#frameId;
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
class IfcChannels {
	#config;
	#all;
	#roleCall;
	#app;
	#thisFrame;
	/**
	 * Set of broadcasters for other apps/frames.
	 *
	 * Example: {
	 *     "appName": BroadcastChannel,
	 *     "appName-frameId": BroadcastChannel
	 * }
	 */
	#others;

	constructor(config) {
		console.log("Setting up broadcast channels.");
		this.#config = config;
		this.#all = new BroadcastChannel(IfcChannels.#formatAllChannelName(this.#config));
		this.#roleCall = new BroadcastChannel(IfcChannels.#formatRoleCallChannelName(this.#config));
		this.#app = new BroadcastChannel(IfcChannels.#formatThisAppChannelName(this.#config));
		this.#thisFrame = new BroadcastChannel(IfcChannels.#formatThisFrameChannelName(this.#config));

		this.#others = {};

		console.log("Done setting up broadcast channels.");
	}

	/**
	 * Ensures that the app and frame both exist in the set of other
	 * @param appName
	 * @param frameId
	 */
	ensureInOthers(appName, frameId) {
		// Ensure we have other apps in, but not for this app (we already have that)
		if (appName != null && this.#config.getAppName() !== appName && !(appName in this.getOtherChannels())) {
			console.log("Adding " + appName + " app to other channels.");
			this.getOtherChannels()[appName] = new BroadcastChannel(IfcChannels.#formatAppChannelName(this.#config, appName));
		}

		if (!(frameId in this.getOtherChannels())) {
			console.log("Adding " + frameId + " frame to other channels.");
			this.getOtherChannels()[frameId] = new BroadcastChannel(IfcChannels.#formatOtherFrameChannelName(this.#config, frameId));
		}
	}

	removeFromOthers(appName, frameId) {
		if (frameId in this.getOtherChannels()) {
			console.log("Removing channel ", frameId);
			let otherFrameChannel = this.getOtherChannels()[frameId];
			delete this.getOtherChannels()[frameId];
			otherFrameChannel.close();
		}

		//if no other frames under app exist, remove other app.
		let otherFrameKeyPrepend = appName;
		if (
			appName != null &&
			!Object.keys(this.getOtherChannels()).some(function (k) {
				return ~k.indexOf(otherFrameKeyPrepend);
			}) &&
			appName in this.getOtherChannels()
		) {
			console.log("No other Frames exist in this app. Removing other app.");
			let otherAppChannel = this.getOtherChannels()[appName];
			delete this.getOtherChannels()[appName];
			otherAppChannel.close();
		}
	}

	appOpen(appName) {
		return this.#config.getAppName() === appName || appName in this.getOtherChannels();
	}

	frameOpen(frameId) {
		return frameId in this.getOtherChannels();
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
		return IfcChannels.#formatAppChannelName(config, config.getAppName());
	}

	static #formatOtherFrameChannelName(config, frameId) {
		return config.getChannelNetwork() + config.getNameSeparator() + frameId;
	}

	static #formatThisFrameChannelName(config) {
		if (config.hasAppName()) {
			return config.getChannelNetwork() + config.getNameSeparator() + config.getAppName() + config.getNameSeparator() + config.getFrameId();
		} else {
			return config.getChannelNetwork() + config.getNameSeparator() + config.getFrameId();
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
	getThisFrameChannel() {
		return this.#thisFrame;
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

	getOtherFrameChannel(frameId) {
		return this.getOtherChannel(frameId);
	}

	getOtherAppChannel(appName) {
		return this.getOtherChannel(appName);
	}
}

/**
 * The main class that manages an instance of inter-frame communication.
 *
 * Automatically handles rolecalls in order for all frames to be aware of every other frame.
 *
 * Intended to be used with other Iwc instances on other frames, with the same channel
 *
 * <pre>
 * let iwc = new Iwc(new IwcConfig({ appName: "test" }));
 * </pre>
 */
class Ifc {
	static #roleCallIntentCall = "CALL"
	static #roleCallIntentHere = "HERE"
	static #roleCallIntentExit = "EXIT"

	#config;
	#channels;

	constructor(config) {
		if (!config instanceof IfcConfig) {
			throw "Config must be an instance of IwcConfig.";
		}

		this.#config = config;
		this.#channels = new IfcChannels(this.#config);

		this.registerHandler(this.getChannels().getRoleCallChannel(), this.#roleCallChannelHandler.bind(this));

		window.addEventListener('unload', () => {
			this.close();
		});

		this.sendMessage(this.#channels.getRoleCallChannel(), IfcMessage.create(this.getConfig(), Ifc.#roleCallIntentCall));

		console.log(
			"New IWC is initialized. " +
			"App: " + this.#config.getAppName() + " " +
			"FrameId: " + this.#config.getFrameId()
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
		let message = IfcMessage.deserialize(event.data);

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
	 * @param {IfcMessage} message
	 */
	#roleCallChannelHandler(message) {
		console.log("Received new role call message: ", message);

		switch (message.getIntent()) {
			case Ifc.#roleCallIntentCall:
				console.log("Calling HERE for rolecall.");
				this.sendMessage(this.getChannels().getRoleCallChannel(), IfcMessage.create(this.getConfig(), Ifc.#roleCallIntentHere));
				this.#channels.ensureInOthers(message.getAppName(), message.getFrameId());
				break;
			case Ifc.#roleCallIntentHere:
				console.debug("Received a HERE from rolecall.");
				this.#channels.ensureInOthers(message.getAppName(), message.getFrameId());
				break;
			case Ifc.#roleCallIntentExit:
				console.debug("Exit message received.");
				this.getChannels().removeFromOthers(message.getAppName(), message.getFrameId());
				break;
			default:
				console.warn("Did not receive a recognized role call related message.");
		}
	}

	/**
	 * Sends a message on a given channel.
	 * @param {BroadcastChannel} channel
	 * @param {IfcMessage} message
	 */
	sendMessage(channel, message) {
		console.log("Sending message: ", message);
		channel.postMessage(message.serialize());
	}

	/**
	 * Responds to a given message with another message. Determines who to send it to based on the incoming message, sends to specific frame.
	 * @param {IfcMessage} received The message we are responding to.
	 * @param {IfcMessage} message The message we are sending in response.
	 */
	respondToMessage(received, message) {
		this.sendMessage(
			this.getChannels().getOtherFrameChannel(received.getFrameId()),
			message
		);
	}

	/**
	 * @returns {IfcConfig}
	 */
	getConfig() {
		return this.#config;
	}

	/**
	 * @returns {IfcChannels}
	 */
	getChannels() {
		return this.#channels;
	}

	getChannel(name) {
		return this.getChannels()[name];
	}

	getThisChannelName() {
		return this.getChannels().getThisFrameChannel().name;
	}

	/**
	 * Closes out this Iwc Object.
	 *
	 * Given we expect the whole page to be closing, we don't explicitly close channels, but that might be something to do in the future.
	 */
	close() {
		console.log("Closing IWC object.");
		this.sendMessage(this.#channels.getRoleCallChannel(), IfcMessage.create(this.getConfig(), Ifc.#roleCallIntentExit));
		//TODO:: determine if this is adequate, or if we need to do anything else, clean-wise to clear dead frames
	}
}

class IfcConstants {
	intents = {
		launchApp: "LAUNCH_APP"
	}
}
package ca.rpgcraft.rpgloottables.license;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class AdvancedLicense {

	private String licenseKey;
	private Plugin plugin;
	private String validationServer;
	private LogType logType = LogType.NORMAL;
	private String securityKey = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";
	private boolean debug = false;

	public AdvancedLicense(String licenseKey, String validationServer, Plugin plugin) {
		this.licenseKey = licenseKey;
		this.plugin = plugin;
		this.validationServer = validationServer;
	}

	public AdvancedLicense setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
		return this;
	}

	public AdvancedLicense setConsoleLog(LogType logType) {
		this.logType = logType;
		return this;
	}

	public AdvancedLicense debug() {
		debug = true;
		return this;
	}

	public boolean register() {
		plugin.getLogger().info("[]==========[Sev License]==========[]");
		plugin.getLogger().info("Connecting to License-Server...");
		try{
			ValidationType vt = isValid();
			if (vt == ValidationType.VALID) {
				plugin.getLogger().info("License valid!");
				plugin.getLogger().info("[]==========[Sev License]==========[]");
				return true;
			} else {
				plugin.getLogger().info("License is NOT valid!");
				plugin.getLogger().info("Failed as a result of " + vt.toString());
				plugin.getLogger().info("Disabling plugin!");
				plugin.getLogger().info("Contact dSevvv on discord!");
				plugin.getLogger().info("[]==========[Sev License]==========[]");

				Bukkit.getScheduler().cancelTasks(plugin);
				Bukkit.getPluginManager().disablePlugin(plugin);
				return false;
			}
		}catch (NumberFormatException ignored){
			plugin.getLogger().info("License is NOT valid!");
			plugin.getLogger().info("Disabling plugin!");
			plugin.getLogger().info("Contact dSevvv on discord!");
			plugin.getLogger().info("[]==========[Sev License]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
	}

	public boolean isValidSimple() {
		return (isValid() == ValidationType.VALID);
	}

	private String requestServer(String v1, String v2) throws IOException {
		URL url = new URL(validationServer + "?v1=" + v1 + "&v2=" + v2 + "&pl=" + plugin.getName());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		if (debug) {
			plugin.getLogger().severe("\nSending 'GET' request to URL : " + url);
			plugin.getLogger().severe("Response Code : " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return response.toString();
		}
	}

	public ValidationType isValid() {
		String rand = toBinary(UUID.randomUUID().toString());
		String sKey = toBinary(securityKey);
		String key = toBinary(licenseKey);

		try {
			String response = requestServer(xor(rand, sKey), xor(rand, key));

			if (response.startsWith("<")) {
				plugin.getLogger().severe("The License-Server returned an invalid response!");
				plugin.getLogger().severe("In most cases this is caused by:");
				plugin.getLogger().severe("1) Your Web-Host injects JS into the page (often caused by free hosts)");
				plugin.getLogger().severe("2) Your ValidationServer-URL is wrong");
				plugin.getLogger().severe("SERVER-RESPONSE: " + (response.length() < 150 || debug ? response : response.substring(0, 150) + "..."));
				return ValidationType.PAGE_ERROR;
			}

			try {
				return ValidationType.valueOf(response);
			} catch (IllegalArgumentException exc) {
				String respRand = xor(xor(response, key), sKey);
				if (rand.substring(0, respRand.length()).equals(respRand))
					return ValidationType.VALID;
				else
					return ValidationType.WRONG_RESPONSE;
			}
		} catch (IOException e) {
			if (debug)
				e.printStackTrace();
			return ValidationType.PAGE_ERROR;
		}
	}

	//
	// Cryptographic
	//

	private static String xor(String s1, String s2) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < (Math.min(s1.length(), s2.length())); i++)
			result.append(Byte.parseByte("" + s1.charAt(i)) ^ Byte.parseByte(s2.charAt(i) + ""));
		return result.toString();
	}

	//
	// Enums
	//

	public enum LogType {
		NORMAL, LOW, NONE;
	}

	public enum ValidationType {
		WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
	}

	//
	// Binary methods
	//

	private String toBinary(String s) {
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}
}

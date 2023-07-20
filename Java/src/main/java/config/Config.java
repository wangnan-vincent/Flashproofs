package config;

import commitment.BouncyCommiter;
import commitment.Commiter;

public class Config {

	private static Config INSTANCE;

	private Commiter commiter;

	private Key<?> key = new BouncyKey("bn128");

	private Config() {
	}

	public void init(Key<?> key) {
		this.key = key;
		if (this.key instanceof BouncyKey) {
			this.commiter = new BouncyCommiter();
		} 
	}

	public static Config getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Config();
		}

		return INSTANCE;
	}

	public Commiter getCommiter() {
		return commiter;
	}

	public Key<?> getKey() {
		return key;
	}
}

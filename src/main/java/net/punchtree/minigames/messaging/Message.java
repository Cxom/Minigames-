package net.punchtree.minigames.messaging;

import java.util.Map;

public class Message {
	
	private static final Language DEFAULT_LANGUAGE = Language.ENGLISH;
	
	private Map<Language, String> translations;
	
	public Message(Map<Language, String> translations) {
		this.translations = translations;
	}
	
	public String get(Object... arguments) {
		return get(DEFAULT_LANGUAGE, arguments);
	}
	
	public String get(Language language, Object... arguments) {
		String translationWithPlaceholders = translations.get(language);
		return String.format(translationWithPlaceholders, arguments);
	}

}

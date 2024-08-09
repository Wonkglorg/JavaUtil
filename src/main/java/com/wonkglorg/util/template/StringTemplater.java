package com.wonkglorg.util.template;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to apply values to a template string
 */
public class StringTemplater {
	private String templateIndicator = "{%s}";
	private Map<String, String> values;

	//todo:jmd add way to add padding to templates or remove parts of the template if the value is
    // empty

	public StringTemplater() {
		values = new HashMap<>();
	}

	public void put(String key, String value) {
		values.put(key, value);
	}

	/**
	 * Sets the template indicator, formatted as "{%s}" where "%s" is the location of the placeholder
	 * variable and the { }
	 * indicate the characters around the placeholder indicate what is considered a placeholder to
	 * look for
	 *
	 * @param templateIndicator
	 */
	public void setTemplateIndicator(String templateIndicator) {
		this.templateIndicator = templateIndicator;
	}

	/**
	 * Applies the values to the template
	 *
	 * @param template The template to apply the values to
	 * @return The template with the modified values
	 */
	public String applyAlt(String template) {
		StringBuilder result = new StringBuilder(template);
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String placeholder = String.format(templateIndicator, entry.getKey());
			int startIndex = result.indexOf(placeholder);
			if (startIndex != -1) {
				int endIndex = startIndex + placeholder.length();
				result.replace(startIndex, endIndex, entry.getValue());
			}
		}
		return result.toString();
	}
}

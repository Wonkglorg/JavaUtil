package com.wonkglorg.util.console;

import static com.wonkglorg.util.string.StringUtils.format;
import static java.lang.System.out;

@SuppressWarnings("unused")
public class ConsoleUtil {
	/**
	 * Prints text to the console
	 *
	 * @param text text to be printed
	 */
	public static void print(Object... text) {
		for (Object t : text) {
			out.print(t);
		}
	}

	/**
	 * Prints the text to the console with a new line
	 *
	 * @param template text to be printed
	 * @param args the values to replace according to
	 * {@link com.wonkglorg.util.string.StringUtils#format(String, Object...)}
	 */
	public static void println(Object... text) {
		if (text.length == 0) {
			out.println();
			return;
		}
		for (Object t : text) {
			out.println(t);
		}
	}

	/**
	 * Prints the text to the console according to {@link #printr(Object)}
	 *
	 * @param template text to be printed
	 * @param args the values to replace according to
	 * {@link com.wonkglorg.util.string.StringUtils#format(String, Object...)}
	 */
	public static void printrFormatted(String template, Object... args) {
		printr(format(template, args));
	}

	/**
	 * Prints the text to the console without a new line
	 *
	 * @param template text to be printed
	 * @param args the values to replace according to
	 * {@link com.wonkglorg.util.string.StringUtils#format(String, Object...)}
	 */
	public static void printlnFormated(String template, Object... args) {
		println(format(template, args));
	}

	public static void printFormated(String template, Object... placeholders) {
		print(format(template), placeholders);
	}

	/**
	 * Sends an updatable message to the console, the message can be updated by sending a new message
	 * using {@link #printr(Object)}
	 * <p>
	 * To resume normal printing without removing this line use a new line character or call
	 * {@link #println(Object...)}
	 * <p>
	 * Only works if this method was used to print the text to update, and does not work if a new
	 * line
	 * has been
	 * sent in between which is not updatable, works by sending a carriage return character to the
	 * console which
	 * puts the cursor at the start of the line and overwrites the text
	 *
	 * @param text text to be printed
	 */
	public static void printr(Object text) {
		out.print("\r" + text);
	}
}

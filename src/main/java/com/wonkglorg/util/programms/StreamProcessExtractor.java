package com.wonkglorg.util.programms;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamProcessExtractor extends Thread {
	private static final Logger log = Logger.getLogger(StreamProcessExtractor.class.getName());
	private final InputStream stream;
	private final StringBuilder buffer;
	private final Consumer<String> handler;

	public StreamProcessExtractor(StringBuilder buffer, InputStream stream,
			Consumer<String> handler) {
		this.stream = stream;
		this.buffer = buffer;
		this.handler = handler;
		this.start();
	}

	@Override
	public void run() {
		try {
			StringBuilder currentLine = new StringBuilder();
			int nextChar;
			while ((nextChar = stream.read()) != -1) {
				buffer.append((char) nextChar);
				if (nextChar == '\r' && handler != null) {
					handler.accept(currentLine.toString());
					currentLine.setLength(0);
					continue;
				}
				currentLine.append((char) nextChar);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}

package com.wonkglorg.util.programms;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class to build a command line program with parameters and execute them
 */
public class ProgrammBuilder {
	private final String programName;

	/**
	 * Constructs a new ProgrammBuilder
	 *
	 * @param processValue The value to use for the program name
	 * @param programName The name of the program
	 */
	public ProgrammBuilder(ProgrammLocation processValue, String programName) {
		if (Objects.requireNonNull(processValue) == ProgrammLocation.ENVIRONMENT) {
			this.programName = System.getenv(programName);
		} else {
			this.programName = programName;
		}
	}

	/**
	 * Append executable name to command
	 *
	 * @param command Command string
	 * @return Command string
	 */
	private String buildCommand(String command) {
		return String.format("%s %s", programName, command);
	}

	/**
	 * Executes the given request for this process
	 */
	public ProgrammResponse execute(ProgrammRequest request) throws Exception {
		String command = buildCommand(request.buildOptions());
		String directory = request.getDirectory();
		Map<String, String> options = request.getOptions();

		Process process;
		int exitCode;
		StringBuilder outBuffer = new StringBuilder(); // stdout
		StringBuilder errBuffer = new StringBuilder(); // stderr
		long startTime = System.nanoTime();

		//if multiple empty lines happen correctly split em up
		String[] split =
				Arrays.stream(command.split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);

		ProcessBuilder processBuilder = new ProcessBuilder(split);

		// Define directory if one is passed
		if (directory != null) {
			processBuilder.directory(new File(directory));
		}

		process = processBuilder.start();

		InputStream outStream = process.getInputStream();
		InputStream errStream = process.getErrorStream();

		var stdOutProcessor = new StreamProcessExtractor(outBuffer, outStream, request.getCallback());
		var stdErrProcessor =
				new StreamProcessExtractor(errBuffer, errStream, request.getErrorCallback());

		try {
			stdOutProcessor.join();
			stdErrProcessor.join();
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			// process exited for some reason
			throw new Exception(e);
		}

		String out = outBuffer.toString();
		String err = errBuffer.toString();

		if (exitCode > 0) {
			throw new Exception(err);
		}

		int elapsedTime = (int) ((System.nanoTime() - startTime) / 1000000);

		return new ProgrammResponse(String.join(" ", command), options, directory, exitCode,
				elapsedTime, out, err);
	}


	public enum ProgrammLocation {
		/**
		 * The program name is taken from the environment variables
		 */
		ENVIRONMENT,

		/**
		 * The program name is taken as a literal
		 */
		PATH;
	}


	/**
	 * Returns a user-friendly string representation
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return programName;
	}
}

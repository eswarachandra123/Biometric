package com.mintutiae.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author srinivas.bammidi
 *
 */
public class BLogger {

	private Logger logger;
	private static BLogger wLogger;
	private ArrayList<String> errorMsg;
	private String loggerName;
	private String fileHandlerPath;

	private BLogger(String loggerName, String fileHandlerPath) throws SecurityException, IOException {

		this.fileHandlerPath = fileHandlerPath;
		this.loggerName = loggerName;
		errorMsg = new ArrayList<>();

		logger = Logger.getLogger(this.loggerName);
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new CFormatter());
		logger.addHandler(handler);

		// create a TXT formatter
		FileHandler fileTxt = new FileHandler(this.fileHandlerPath);
		fileTxt.setFormatter(new CFormatter());
		logger.addHandler(fileTxt);

	}

	public static BLogger setup(String loggerName, String fileHandlerPath) throws SecurityException, IOException {
		if (wLogger == null) {
			wLogger = new BLogger(
					(loggerName != null && loggerName.length() > 0) ? loggerName : Logger.GLOBAL_LOGGER_NAME,
					fileHandlerPath);
		}

		wLogger.clearFileHandler();
		return wLogger;
	}

	private void clearFileHandler() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(fileHandlerPath);
		writer.print("");
		writer.close();
	}

	public void clear() {
		errorMsg.clear();
	}

	public void warn(String msg) {
		logger.warning(msg);
	}

	public void error(String msg) {
		errorMsg.add(msg);
		logger.severe(msg);
	}

	public boolean isAnyValidErrors() {
		return errorMsg.size() > 0;
	}

	public void info(String msg) {
		logger.info(msg);
	}

	private class CFormatter extends SimpleFormatter {
		private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

		@Override
		public synchronized String format(LogRecord lr) {
			return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(), lr.getMessage());
		}
	}
}


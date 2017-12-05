package com.herle.java.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(GenericUtil.class);

	public static void main(String[] args) {

		myLogger.info("To Get Temporary File Location : " + System.getProperty("java.io.tmpdir"));

		String current;
		try {
			current = new java.io.File(".").getCanonicalPath();
			myLogger.info("To Get Current dir :" + current);
		} catch (IOException e) {

			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());

		}

		String currentDir = System.getProperty("user.dir");
		myLogger.info("To Current dir using System:" + currentDir);

		// To Create a folder(directory) in current working directory using java
		new File(System.getProperty("user.dir") + "/folder").mkdir();
	}

}

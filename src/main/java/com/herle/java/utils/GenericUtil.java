package com.herle.java.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(GenericUtil.class);

	public static void main(String[] args) throws UnknownHostException, SocketException {

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

		getCurrentMachineIpAddress();
		getCurrentMachineMACAddress();

		/***
		 * chartSchedulerTask will get all the values from country hashMap and
		 * store it in chartLog file.
		 */

		TimerTask chartSchedulerTask = new GenericUtil.ChartSchedulerTask();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartSchedulerTask, getTomorrowMorning1am(), 1000 * 60 * 60 * 24);

	}

	public static class ChartSchedulerTask extends TimerTask {

		/**
		 * Implements TimerTask's abstract run method.
		 */
		@Override
		public void run() {
			System.out.println("..............................Testing Scheduler...................................");

		}

	}

	private static Date getTomorrowMorning1am() {
		Calendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DATE, 1);

		Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE), 1, 0);

		return result.getTime();
	}

	public static String readPropertyConfigFile(String propertyName) {
		String filename = "config.properties";
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = EmailUtil.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				myLogger.info("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);
			myLogger.info("Property [ " + propertyName + " ] retreieved successfully");

			return prop.getProperty(propertyName);

		} catch (IOException ex) {
			myLogger.info(
					"\n LocalizedMessage : " + ex.getLocalizedMessage() + "\n  		 Message :: " + ex.getMessage()
							+ "\n toString :: " + ex.toString() + "\n:		 StackTrace :: " + ex.getStackTrace());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					myLogger.info("\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: "
							+ e.getMessage() + "\n toString :: " + e.toString() + "\n:		 StackTrace :: "
							+ e.getStackTrace());
				}
			}
		}
		return null;

	}

	public static String getCurrentMachineIpAddress() throws UnknownHostException {

		InetAddress ip = InetAddress.getLocalHost();

		myLogger.info("Current machine IP address : " + ip.getHostAddress());

		return ip.getHostAddress();

	}

	public static String getCurrentMachineMACAddress() throws UnknownHostException, SocketException {

		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}

		myLogger.info("Current machine MAC address : " + sb.toString());

		return sb.toString();
	}

}

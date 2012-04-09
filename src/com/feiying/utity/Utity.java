package com.feiying.utity;

import java.text.DecimalFormat;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Utity {

	public static String htmlCheck(String str) {
		if (str == null) {
			return "";
		}
		str = replaceAllOccurances(str, "<", "&lt;");
		str = replaceAllOccurances(str, ">", "&gt;");
		str = replaceAllOccurances(str, "\"", "&quot");
		return str;
	}

	public static String htmlCheck2(String str) {
		if (str == null || str.equals("")) {
			return "0";
		}
		return str.toString();
	}

	public static String replaceAllOccurances(String str, String from, String to) {
		if (str == null || str.length() == 0) {
			return str;
		} else if (str.length() == 1 && str.equals(from)) {
			return to;
		} else if (str.length() == 1 && !str.equals(from)) {
			return str;
		}
		int j = -1;
		while ((j = str.indexOf(from)) >= 0) {
			str = str.substring(0, j) + (char) 5
					+ str.substring(j + from.length());
		}

		int i = -1;
		while ((i = str.indexOf((char) 5)) >= 0) {
			str = str.substring(0, i) + to + str.substring(i + 1);
		}

		return str;
	}

	public static String getActor(String str) {
		if (str.length() > 8) {
			return str.substring(0, 8);
		} else {
			return str;
		}
	}

	public static String formatToMoney(double money) {
		DecimalFormat nf = new DecimalFormat("###,##0.00");
		return nf.format(money);
	}

	/**
	 * split the text with given split word
	 * 
	 * @param text
	 * @param splitWord
	 * @return
	 */
	public static String[] splitText(String text, String splitWord) {
		Vector<String> sentences = new Vector<String>();
		String[] ret = null;
		text += splitWord;

		if (text != null && !text.equals("")) {
			for (int i = 0, j = 0;;) {
				i = text.indexOf(splitWord, j);

				if (i >= 0 && i > j) {
					String tmp = text.substring(j, i);
					sentences.addElement(tmp);
				}
				if (i < 0 || i == (text.length() - splitWord.length())) {
					break;
				}
				j = i + splitWord.length();
			}

		}

		if (sentences.size() > 0) {
			ret = new String[sentences.size()];
			sentences.copyInto(ret);
		}

		return ret;
	}

	/**
	 * split the text between split1 & split2 by improved algorithm it can deal
	 * with the condition that split1 is the same as split2
	 * 
	 * @param text
	 * @param split1
	 * @param split2
	 * @return
	 */
	public static String[] splitText(String text, String split1, String split2) {
		Vector<String> words = new Vector<String>();
		String[] ret = null;

		int i = 0;
		int j = 0;

		if (null == text || null == split1 || null == split2) {
			return ret;
		}

		do {
			// get the first matched word
			i = text.indexOf(split1, j);
			// get the following matched word
			if ((i + split1.length()) < text.length()) {
				j = text.indexOf(split2, i + split1.length());
			}
			// if the i & j are not out of bound length of text
			if (j > i && j < text.length() && i < text.length()) {

				String tmp = text.substring(i + split1.length(), j);

				words.addElement(tmp);
			}

			j = j + split2.length();
		} while (text.indexOf(split1, j) > 0 && text.indexOf(split2, j) > 0);

		if (words.size() > 0) {
			ret = new String[words.size()];
			words.copyInto(ret);
		}
		return ret;

	}

	public static String trimPhone(String phoneNumber) {
		String trimPhone = phoneNumber;

		if (phoneNumber.startsWith("<") && phoneNumber.endsWith(">")) {
			trimPhone = splitText(phoneNumber, ">", "<")[0];
		}

		return trimPhone;
	}

	/**
	 * 判断是否为手机浏览器
	 * 
	 * @param userAgent
	 * @return
	 */
	public static boolean choose(HttpServletRequest request) {
		String accept = request.getHeader("Accept");
		String userAgent = request.getHeader("User-agent");
		if (accept.indexOf("wap.wml") > 0) {
			return true;
		} else if (userAgent.indexOf("Noki") > -1
				|| // Nokia phones and emulators
				userAgent.indexOf("Eric") > -1
				|| // Ericsson WAP phones and emulators
				userAgent.indexOf("WapI") > -1
				|| // Ericsson WapIDE 2.0
				userAgent.indexOf("MC21") > -1
				|| // Ericsson MC218
				userAgent.indexOf("AUR") > -1
				|| // Ericsson R320
				userAgent.indexOf("R380") > -1
				|| // Ericsson R380
				userAgent.indexOf("UP.B") > -1
				|| // UP.Browser
				userAgent.indexOf("WinW") > -1
				|| // WinWAP browser
				userAgent.indexOf("UPG1") > -1
				|| // UP.SDK 4.0
				userAgent.indexOf("upsi") > -1
				|| // another kind of UP.Browser
				userAgent.indexOf("QWAP") > -1
				|| // unknown QWAPPER browser
				userAgent.indexOf("Jigs") > -1
				|| // unknown JigSaw browser
				userAgent.indexOf("Java") > -1
				|| // unknown Java based browser
				userAgent.indexOf("Alca") > -1
				|| // unknown Alcatel-BE3 browser (UP based)
				userAgent.indexOf("MITS") > -1
				|| // unknown Mitsubishi browser
				userAgent.indexOf("MOT-") > -1
				|| // unknown browser (UP based)
				userAgent.indexOf("My S") > -1
				|| // unknown Ericsson devkit browser
				userAgent.indexOf("WAPJ") > -1
				|| // Virtual WAPJAG www.wapjag.de
				userAgent.indexOf("fetc") > -1
				|| // fetchpage.cgi Perl script from www.wapcab.de
				userAgent.indexOf("ALAV") > -1
				|| // yet another unknown UP based browser
				userAgent.indexOf("Wapa") > -1
				|| // another unknown browser (Web based "Wapalyzer")
				userAgent.indexOf("HTC_") > -1
				|| userAgent.indexOf("iPhone") > -1
				|| userAgent.indexOf("Android") > -1) {
			return true;
		} else {
			return false;
		}
	}

}

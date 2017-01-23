package de.topobyte.githubcli;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kohsuke.github.GitHub;

public class Util
{

	public static GitHub connect() throws IOException
	{
		String authLogin = System.getenv("GITHUB_LOGIN");
		String authToken = System.getenv("GITHUB_OAUTH");
		System.err.println("connection login: " + authLogin);
		System.err.println("connection token: " + authToken);

		GitHub github;
		if (authToken == null) {
			github = GitHub.connectAnonymously();
		} else {
			github = GitHub.connect(authLogin, authToken);
		}

		return github;
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static String format(Date date)
	{
		return dateFormat.format(date);
	}

	public static String padFront(Object object, int length)
	{
		return padFront(object.toString(), length);
	}

	public static String padFront(String string, int length)
	{
		int len = string.length();
		if (len >= length) {
			return string;
		}
		int missing = length - len;
		StringBuilder builder = new StringBuilder(length);
		pad(builder, ' ', missing);
		builder.append(string);
		return builder.toString();
	}

	public static String padTail(Object object, int length)
	{
		return padTail(object.toString(), length);
	}

	public static String padTail(String string, int length)
	{
		int len = string.length();
		if (len >= length) {
			return string;
		}
		int missing = length - len;
		StringBuilder builder = new StringBuilder(length);
		builder.append(string);
		pad(builder, ' ', missing);
		return builder.toString();
	}

	private static void pad(StringBuilder builder, char c, int missing)
	{
		for (int i = 0; i < missing; i++) {
			builder.append(c);
		}
	}

}

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

	public static String pad(Object object, int length)
	{
		return pad(object.toString(), length);
	}

	public static String pad(String string, int length)
	{
		int len = string.length();
		if (len >= length) {
			return string;
		}
		int missing = length - len;
		StringBuilder builder = new StringBuilder(length);
		builder.append(string);
		for (int i = 0; i < missing; i++) {
			builder.append(' ');
		}
		return builder.toString();
	}

}

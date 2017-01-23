package de.topobyte.githubcli;

import java.io.IOException;

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

}

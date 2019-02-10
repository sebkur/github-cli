package org.kohsuke.github;

import java.io.IOException;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Topics
{

	public static GHTopics get(GHRepository repository) throws IOException
	{
		GitHub github = repository.root;
		Requester requester = new Requester(github).method("GET");
		// Set a special header so that the API won't return an error message
		// instead of actual content
		requester.setHeader("Accept",
				"application/vnd.github.mercy-preview+json");
		GHTopics topics = requester.to(
				String.format("/repos/%s/topics", repository.getFullName()),
				GHTopics.class);
		return topics;
	}

	public static void update(GHRepository repository, GHTopics topics)
			throws IOException
	{
		GitHub github = repository.root;
		Requester requester = new Requester(github).method("PUT");
		// Set a special header so that the API won't return an error message
		// instead of actual content
		requester.setHeader("Accept",
				"application/vnd.github.mercy-preview+json");
		requester.with("names", topics.getTopics());
		requester.to(
				String.format("/repos/%s/topics", repository.getFullName()),
				GHTopics.class);
	}

}

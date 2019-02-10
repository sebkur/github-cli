package org.kohsuke.github;

import java.io.IOException;

import de.topobyte.githubcli.Util;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class TestGetTopics
{

	public static void main(String[] args) throws IOException
	{
		System.out.println("Testing topic query");
		GitHub github = Util.connect();

		GHRepository repository = github
				.getRepository("topobyte/jeography-gis");

		GHTopics topics = Topics.get(repository);

		for (String topic : topics.getTopics()) {
			System.out.println(topic);
		}
	}

}

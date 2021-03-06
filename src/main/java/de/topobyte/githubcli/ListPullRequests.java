package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ListPullRequests
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		ListPullRequests task = new ListPullRequests();
		task.execute(name, exe, args);
	}

	private void execute(String exeName, Executable exe, String[] args)
			throws IOException
	{
		if (args.length < 1) {
			exe.printMessage(String.format("Usage: %s <repository>", exeName));
			exe.printMessageAndExitFail("Please specify a repository");
		}
		String repoName = args[0];

		GitHub github = Util.connect();

		GHRepository repo = null;
		try {
			repo = github.getRepository(repoName);
		} catch (FileNotFoundException e) {
			exe.printMessageAndExitFail("Repository not found");
		}
		List<GHPullRequest> pullRequests = repo
				.getPullRequests(GHIssueState.ALL);

		int max = pullRequests.stream().mapToInt(GHPullRequest::getNumber).max()
				.orElse(1);
		int digits = Integer.toString(max).length();

		for (GHPullRequest pullRequest : pullRequests) {
			System.out.println(String.format("%s (%s, %s): %s",
					Util.padFront(pullRequest.getNumber(), digits),
					pullRequest.getNumber(),
					Util.format(pullRequest.getCreatedAt()),
					Util.padTail(pullRequest.getState(), 6),
					pullRequest.getTitle()));
		}
	}

}

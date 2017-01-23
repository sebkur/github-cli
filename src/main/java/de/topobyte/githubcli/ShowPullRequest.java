package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ShowPullRequest
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		ShowPullRequest task = new ShowPullRequest();
		task.execute(name, exe, args);
	}

	private void execute(String exeName, Executable exe, String[] args)
			throws IOException
	{
		if (args.length < 2) {
			exe.printMessage(
					String.format("Usage: %s <repository> <number>", exeName));
			exe.printMessageAndExitFail(
					"Please specify a repository and a pull request number");
		}
		String repoName = args[0];
		String argNumber = args[1];

		int number = 0;
		try {
			number = Integer.parseInt(argNumber);
		} catch (NumberFormatException e) {
			exe.printMessageAndExitFail(
					"Error while parsing pull request number");
		}

		GitHub github = Util.connect();

		GHRepository repo = null;
		try {
			repo = github.getRepository(repoName);
		} catch (FileNotFoundException e) {
			exe.printMessageAndExitFail("Repository not found");
		}

		GHPullRequest pullRequest = null;
		try {
			pullRequest = repo.getPullRequest(number);
		} catch (FileNotFoundException e) {
			exe.printMessageAndExitFail("Pull Request not found");
		}

		GHUser user = pullRequest.getUser();

		System.out.println(
				"================================================================================");
		System.out.println(String.format(pullRequest.getTitle()));
		System.out.println(
				"================================================================================");
		System.out.println(String.format("user: %s (%s)", user.getLogin(),
				user.getName()));
		System.out.println(String.format("state: %s", pullRequest.getState()));
		System.out.println(
				"================================================================================");
		System.out.println(pullRequest.getBody());
	}

}

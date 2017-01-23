package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ListIssues
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		ListIssues task = new ListIssues();
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
		List<GHIssue> issues = repo.getIssues(GHIssueState.ALL);

		int max = issues.stream().mapToInt(GHIssue::getNumber).max().orElse(1);
		int digits = (int) Math.round(Math.log10(max));

		for (GHIssue issue : issues) {
			System.out.println(String.format("%s (%s, %s): %s",
					Util.padFront(issue.getNumber(), digits),
					Util.format(issue.getCreatedAt()),
					Util.padTail(issue.getState(), 6), issue.getTitle()));
		}
	}

}

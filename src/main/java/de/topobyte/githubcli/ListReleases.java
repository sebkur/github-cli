package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ListReleases
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		ListReleases task = new ListReleases();
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
		PagedIterable<GHRelease> releases = repo.listReleases();
		for (GHRelease release : releases) {
			System.out.println(String.format("%s (%s, %s)", release.getName(),
					release.getTagName(), release.getPublished_at()));
		}
	}

}

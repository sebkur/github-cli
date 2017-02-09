package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Fork
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		Fork task = new Fork();
		task.execute(name, exe, args);
	}

	private void execute(String exeName, Executable exe, String[] args)
			throws IOException
	{
		if (args.length < 1) {
			exe.printMessage(String.format(
					"Usage: %s <repository> [<organization>]", exeName));
			exe.printMessageAndExitFail("Please specify a repository");
		}
		String repoName = args[0];
		String organizationName = null;
		if (args.length > 1) {
			organizationName = args[1];
		}

		GitHub github = Util.connect();
		if (github.isAnonymous()) {
			exe.printMessageAndExitFail(
					"Forking does not work in anonymous mode");
		}

		GHRepository repo = null;
		try {
			repo = github.getRepository(repoName);
		} catch (FileNotFoundException e) {
			exe.printMessageAndExitFail("Repository not found");
		}

		if (organizationName == null) {
			repo.fork();
		} else {
			GHOrganization organization = github
					.getOrganization(organizationName);
			repo.forkTo(organization);
		}
	}

}

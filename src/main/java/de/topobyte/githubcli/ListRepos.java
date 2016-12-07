package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

public class ListRepos
{

	public static void main(String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		ListRepos task = new ListRepos();
		task.execute(exe, args);
	}

	private void execute(Executable exe, String[] args) throws IOException
	{
		if (args.length < 1) {
			exe.printMessageAndExitFail("Please specify a username");
		}
		String name = args[0];

		GitHub github = GitHub.connectAnonymously();

		GHUser user = null;
		try {
			user = github.getUser(name);
		} catch (FileNotFoundException e) {
			exe.printMessageAndExitFail("Username not found");
		}
		Map<String, GHRepository> repositories = user.getRepositories();

		List<String> repoNames = new ArrayList<>();
		repoNames.addAll(repositories.keySet());
		Collections.sort(repoNames, String.CASE_INSENSITIVE_ORDER);

		for (String repo : repoNames) {
			System.out.println(repo);
		}
	}

}

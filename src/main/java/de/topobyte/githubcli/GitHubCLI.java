package de.topobyte.githubcli;

import java.io.IOException;

import de.topobyte.executableutils.DelegateTask;
import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

public class GitHubCLI
{

	public static void main(String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		DelegateTask task = new DelegateTask(exe, "hubcli");
		task.add("list-repos", ListRepos.class);

		task.execute(args);
	}

}

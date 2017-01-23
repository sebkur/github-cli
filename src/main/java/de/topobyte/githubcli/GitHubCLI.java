package de.topobyte.githubcli;

import java.io.IOException;

import de.topobyte.executableutils.DelegateTask;
import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GitHubCLI
{

	public static void main(String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		DelegateTask task = new DelegateTask(exe, "hubcli");
		task.addWithName("list-repos", ListRepos.class);
		task.addWithName("list-pull-requests", ListPullRequests.class);
		task.addWithName("show-pull-request", ShowPullRequest.class);
		task.addWithName("list-issues", ListIssues.class);

		task.execute(args);
	}

}

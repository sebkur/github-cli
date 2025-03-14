package de.topobyte.githubcli;

import de.topobyte.githubcli.topics.AddTopic;
import de.topobyte.githubcli.topics.RemoveTopic;
import de.topobyte.githubcli.topics.SetTopics;
import de.topobyte.utilities.apache.commons.cli.commands.ArgumentParser;
import de.topobyte.utilities.apache.commons.cli.commands.ExeRunner;
import de.topobyte.utilities.apache.commons.cli.commands.ExecutionData;
import de.topobyte.utilities.apache.commons.cli.commands.RunnerException;
import de.topobyte.utilities.apache.commons.cli.commands.options.DelegateExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GitHubCLI
{

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			DelegateExeOptions options = new DelegateExeOptions();
			options.addCommand("list-repos", ListRepos.OPTIONS_FACTORY,
					ListRepos.class);
			options.addCommand("list-pull-requests", ListPullRequests.class);
			options.addCommand("show-pull-request", ShowPullRequest.class);
			options.addCommand("list-issues", ListIssues.class);
			options.addCommand("show-issue", ShowIssue.class);
			options.addCommand("fork", Fork.OPTIONS_FACTORY, Fork.class);
			options.addCommand("fork-and-clone", ForkAndClone.OPTIONS_FACTORY,
					ForkAndClone.class);
			options.addCommand("rate-limit", RateLimit.class);
			options.addCommand("search-popular",
					SearchPopularRepositories.OPTIONS_FACTORY,
					SearchPopularRepositories.class);
			options.addCommand("clone", Clone.OPTIONS_FACTORY, Clone.class);
			options.addCommand("clone-for-pr", CloneForPR.OPTIONS_FACTORY,
					CloneForPR.class);
			options.addCommand("repo-info", RepoInfo.OPTIONS_FACTORY,
					RepoInfo.class);
			options.addCommand("add-topic", AddTopic.OPTIONS_FACTORY,
					AddTopic.class);
			options.addCommand("remove-topic", RemoveTopic.OPTIONS_FACTORY,
					RemoveTopic.class);
			options.addCommand("set-topics", SetTopics.OPTIONS_FACTORY,
					SetTopics.class);
			options.addCommand("list-releases", ListReleases.class);

			return options;
		}

	};

	public static void main(String[] args) throws RunnerException
	{
		ExeOptions options = OPTIONS_FACTORY.createOptions();
		ArgumentParser parser = new ArgumentParser("hubcli", options);

		ExecutionData data = parser.parse(args);
		if (data != null) {
			ExeRunner.run(data);
		}
	}

}

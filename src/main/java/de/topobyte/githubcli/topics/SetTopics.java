package de.topobyte.githubcli.topics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTopics;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.Topics;

import com.google.common.base.Joiner;

import de.topobyte.githubcli.Util;
import de.topobyte.utilities.apache.commons.cli.CliTool;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SetTopics
{

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			return new CommonsCliExeOptions(options,
					"[options] <repo> [<topic...>]");
		}

	};

	public static void main(String exeName, CommonsCliArguments arguments)
			throws IOException
	{
		CliTool cli = arguments.getOptions().tool(exeName);

		CommandLine line = arguments.getLine();

		List<String> args = line.getArgList();
		if (args.size() < 1) {
			cli.printMessageAndHelpAndExit("Please specify a repository");
		}
		String repoName = args.get(0);

		GitHub github = Util.connect();

		GHRepository repo = null;
		try {
			repo = github.getRepository(repoName);
		} catch (FileNotFoundException e) {
			cli.printMessageAndExit("Repository not found");
		}

		GHTopics topics = Topics.get(repo);
		topics.clearTopics();
		for (int i = 1; i < args.size(); i++) {
			topics.addTopic(args.get(i));
		}
		if (topics.getTopics().isEmpty()) {
			System.out.println("No topics");
		} else {
			System.out.println(
					"Topics: " + Joiner.on(", ").join(topics.getTopics()));
		}

		Topics.update(repo, topics);
	}

}

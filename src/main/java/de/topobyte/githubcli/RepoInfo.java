package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.utilities.apache.commons.cli.CliTool;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RepoInfo
{

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			return new CommonsCliExeOptions(options, "[options] <repo>");
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

		System.out.println(String.format("Web: %s", repo.getHtmlUrl()));
		System.out.println(String.format("Git: %s", repo.getGitTransportUrl()));
		System.out.println(String.format("SSH: %s", repo.getSshUrl()));
		System.out
				.println(String.format("HTTP: %s", repo.getHttpTransportUrl()));
		System.out.println(String.format("Language: %s", repo.getLanguage()));
	}

}

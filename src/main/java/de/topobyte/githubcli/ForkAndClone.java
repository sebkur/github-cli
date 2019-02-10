package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.utilities.apache.commons.cli.CliTool;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ForkAndClone
{

	private static final String OPTION_ORGANIZATION = "organization";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			OptionHelper.addL(options, OPTION_ORGANIZATION, true, false,
					"An organization to fork to");
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
		String organizationName = line.getOptionValue(OPTION_ORGANIZATION);

		GitHub github = Util.connect();
		if (github.isAnonymous()) {
			cli.printMessageAndExit("Forking does not work in anonymous mode");
		}

		GHRepository repo = null;
		try {
			repo = github.getRepository(repoName);
		} catch (FileNotFoundException e) {
			cli.printMessageAndExit("Repository not found");
		}

		if (organizationName == null) {
			repo.fork();
		} else {
			GHOrganization organization = github
					.getOrganization(organizationName);
			repo.forkTo(organization);
		}

		cli.printMessageAndExit("This task is not completely implemented yet");
		// TODO: implement rest of this
	}

}

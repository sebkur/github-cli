package de.topobyte.githubcli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import de.topobyte.githubcli.git.SshUtil;
import de.topobyte.system.utils.SystemPaths;
import de.topobyte.utilities.apache.commons.cli.CliTool;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Clone
{

	private static final String OPTION_DIR = "dir";
	private static final String OPTION_HTTPS = "https";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			OptionHelper.addL(options, OPTION_DIR, true, false,
					"a directory to clone to");
			OptionHelper.addL(options, OPTION_HTTPS, false, false,
					"use the HTTPS uri for cloning");
			return new CommonsCliExeOptions(options, "[options] <repo>");
		}

	};

	public static void main(String exeName, CommonsCliArguments arguments)
			throws IOException
	{
		CliTool cli = arguments.getOptions().tool(exeName);

		CommandLine line = arguments.getLine();
		boolean useHttps = line.hasOption(OPTION_HTTPS);
		String argDir = line.getOptionValue(OPTION_DIR);

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

		String uri = repo.getSshUrl();
		if (useHttps) {
			uri = repo.getHttpTransportUrl();
		}

		Path pathBase = SystemPaths.CWD;
		if (argDir != null) {
			pathBase = Paths.get(argDir);
		}

		Path path = pathBase.resolve(repoName);
		if (Files.exists(path)) {
			System.out.println("Target directory already exists: " + path);
			System.exit(1);
		}

		if (!useHttps) {
			System.out.println("Initializing SSH agent");
			SshUtil.initSshAgentUsage();
		}

		Path parent = path.getParent();
		System.out.println("Creating parent directory: " + parent);

		File dir = path.toFile();

		System.out.println("Cloning from: " + uri);
		try {
			Git.cloneRepository().setURI(uri).setDirectory(dir).call();
		} catch (TransportException e) {
			if (e.getMessage().equals(uri + ": Auth fail")) {
				cli.printMessageAndExit("Authentication failed (SSH agent?)");
			} else {
				cli.printMessageAndExit(String
						.format("Transport exception: '%s'", e.getMessage()));
			}
		} catch (GitAPIException e) {
			System.out.println("Error while cloning");
			e.printStackTrace();
			System.exit(1);
		}
	}

}

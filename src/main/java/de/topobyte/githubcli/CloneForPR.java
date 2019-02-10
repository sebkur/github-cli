package de.topobyte.githubcli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.URIish;
import org.kohsuke.github.GHMyself;
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
public class CloneForPR
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
		String qualifiedRepoName = args.get(0);

		GitHub github = Util.connect();

		GHRepository repoOrigin = null;
		try {
			repoOrigin = github.getRepository(qualifiedRepoName);
		} catch (FileNotFoundException e) {
			cli.printMessageAndExit(
					"Repository not found: " + qualifiedRepoName);
		}

		String repoName = repoOrigin.getName();
		GHMyself me = github.getMyself();
		String login = me.getLogin();
		String ourQualifiedRepoName = String.format("%s/%s", login, repoName);

		GHRepository repoYours = null;
		try {
			repoYours = github.getRepository(ourQualifiedRepoName);
		} catch (FileNotFoundException e) {
			cli.printMessageAndExit(
					"Repository not found: " + ourQualifiedRepoName);
		}

		String originUri = repoOrigin.getSshUrl();
		if (useHttps) {
			originUri = repoOrigin.getHttpTransportUrl();
		}

		String ourUri = repoYours.getSshUrl();

		Path pathBase = SystemPaths.CWD;
		if (argDir != null) {
			pathBase = Paths.get(argDir);
		}

		Path path = pathBase.resolve(qualifiedRepoName);
		if (Files.exists(path)) {
			System.out.println("Target directory already exists: " + path);
			System.exit(1);
		}

		System.out.println("Initializing SSH agent");
		SshUtil.initSshAgentUsage();

		Path parent = path.getParent();
		System.out.println("Creating parent directory: " + parent);

		File dir = path.toFile();

		System.out.println("Cloning from: " + originUri);
		try {
			Git.cloneRepository().setURI(originUri).setDirectory(dir).call();
		} catch (TransportException e) {
			if (e.getMessage().equals(originUri + ": Auth fail")) {
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

		String ourRemote = login;

		System.out.println("Adding our additional remote");
		Git git = Git.open(dir);
		RemoteAddCommand addRemote = git.remoteAdd();
		addRemote.setName(ourRemote);
		try {
			addRemote.setUri(new URIish(ourUri));
		} catch (URISyntaxException e) {
			cli.printMessageAndExit(
					"Error while setting remote, URI syntax broken: "
							+ e.getMessage());
		}
		try {
			addRemote.call();
		} catch (GitAPIException e) {
			cli.printMessageAndExit(
					"Error while setting remote: " + e.getMessage());
		}

		System.out.println("Fetching our remote");
		FetchCommand fetch = git.fetch().setRemote(ourRemote);
		try {
			fetch.call();
		} catch (GitAPIException e) {
			cli.printMessageAndExit(
					"Error while fetching remote: " + e.getMessage());
		}
	}

}

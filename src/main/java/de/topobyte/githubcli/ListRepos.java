package de.topobyte.githubcli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class ListRepos
{

	private static final String OPTION_SORT_BY_STARS = "sort-by-stars";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			options.addOption(null, OPTION_SORT_BY_STARS, false,
					"Sort results by number of stars");
			return new CommonsCliExeOptions(options, "[options] <username>");
		}

	};

	public static void main(String exename, CommonsCliArguments arguments)
			throws IOException
	{
		CommandLine line = arguments.getLine();
		List<String> args = line.getArgList();

		if (args.size() < 1) {
			System.out.println("Please specify a username");
			arguments.getOptions().usage(exename);
			System.exit(1);
		}
		String name = args.get(0);

		boolean sortByStars = line.hasOption(OPTION_SORT_BY_STARS);

		GitHub github = Util.connect();

		GHUser user = null;
		try {
			user = github.getUser(name);
		} catch (FileNotFoundException e) {
			System.out.println("Username not found");
			System.exit(1);
		}
		Map<String, GHRepository> repositories = user.getRepositories();

		List<GHRepository> repos = new ArrayList<>(repositories.values());

		if (sortByStars) {
			repos.sort(new Comparator<GHRepository>() {

				@Override
				public int compare(GHRepository o1, GHRepository o2)
				{
					int cmp = Integer.compare(o1.getStargazersCount(),
							o2.getStargazersCount());
					if (cmp != 0) {
						return cmp;
					}
					return o1.getName().compareTo(o2.getName());
				}

			});
		}

		List<String> repoNames = new ArrayList<>();
		repoNames.addAll(repositories.keySet());
		Collections.sort(repoNames, String.CASE_INSENSITIVE_ORDER);

		for (GHRepository repository : repos) {
			int stars = repository.getStargazersCount();
			String language = repository.getLanguage();
			System.out.println(String.format("%s (%d*, %s)",
					repository.getName(), stars, language));
		}
	}

}

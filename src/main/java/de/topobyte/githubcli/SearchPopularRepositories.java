package de.topobyte.githubcli;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GHRepositorySearchBuilder.Sort;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;
import org.kohsuke.github.PagedSearchIterable;

import com.google.common.base.Joiner;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SearchPopularRepositories
{

	private static final String OPTION_LANGUAGE = "language";
	private static final String OPTION_MIN_STARS = "min-stars";
	private static final String OPTION_LIMIT = "limit";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			OptionHelper.addL(options, OPTION_LANGUAGE, true, false,
					"the programming language");
			OptionHelper.addL(options, OPTION_MIN_STARS, true, false,
					"the minimum number of stars");
			OptionHelper.addL(options, OPTION_LIMIT, true, false,
					"the maximum number of results");
			return new CommonsCliExeOptions(options, "[options] <query>");
		}

	};

	public static void main(String exename, CommonsCliArguments arguments)
			throws IOException
	{
		CommandLine line = arguments.getLine();
		List<String> args = line.getArgList();
		String query = Joiner.on(" ").join(args);

		if (query.isEmpty() && !line.hasOption(OPTION_MIN_STARS)
				&& !line.hasOption(OPTION_LANGUAGE)) {
			System.out.println("Specify at least one criterion or query");
			arguments.getOptions().usage(exename);
			System.exit(1);
		}

		GitHub github = Util.connect();

		int limit = 10;
		if (line.hasOption(OPTION_LIMIT)) {
			limit = Integer.parseInt(line.getOptionValue(OPTION_LIMIT));
		}

		GHRepositorySearchBuilder searchBuilder = github.searchRepositories();
		if (line.hasOption(OPTION_MIN_STARS)) {
			String minStars = line.getOptionValue(OPTION_MIN_STARS);
			searchBuilder.stars(">" + minStars);
		}
		if (line.hasOption(OPTION_LANGUAGE)) {
			String language = line.getOptionValue(OPTION_LANGUAGE);
			searchBuilder.language(language);
		}
		searchBuilder.sort(Sort.STARS);
		searchBuilder.q(query);
		PagedSearchIterable<GHRepository> results = searchBuilder.list();
		int num = results.getTotalCount();

		System.out.println(String.format("Number of results: %d", num));
		PagedIterator<GHRepository> iterator = results.iterator();
		int i = 0;
		while (iterator.hasNext() && i++ < limit) {
			GHRepository repo = iterator.next();
			System.out.println(String.format("%s %d", repo.getFullName(),
					repo.getStargazersCount()));
		}
	}

}

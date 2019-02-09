package de.topobyte.githubcli;

import java.io.IOException;

import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GitHub;

import de.topobyte.executableutils.Executable;
import de.topobyte.executableutils.SystemOutExecutable;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class RateLimit
{

	public static void main(String name, String[] args) throws IOException
	{
		Executable exe = new SystemOutExecutable();
		RateLimit task = new RateLimit();
		task.execute(name, exe, args);
	}

	private void execute(String exeName, Executable exe, String[] args)
			throws IOException
	{
		GitHub github = Util.connect();
		GHRateLimit rateLimit = github.getRateLimit();

		exe.printMessage(String.format("Limit: %d, remaining: %d",
				rateLimit.limit, rateLimit.remaining));
		exe.printMessage(String.format("Resets: %s", rateLimit.reset));
	}

}

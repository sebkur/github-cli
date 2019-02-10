package org.kohsuke.github;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Topic
 *
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 * @see <a href=
 *      "https://developer.github.com/v3/repos#list-all-topics-for-a-repository">documentation</a>
 */
public class GHTopics
{
	/* package almost final */ GitHub root;

	private List<String> names = new ArrayList<>();

	public List<String> getTopics()
	{
		return Collections.unmodifiableList(names);
	}

	/* package */ GHTopics wrapUp(GitHub root)
	{
		this.root = root;
		wrapUp();
		return this;
	}

	private void wrapUp()
	{
		// nothing
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GHTopics ghTopics = (GHTopics) o;
		return names.equals(ghTopics.names);

	}

	@Override
	public int hashCode()
	{
		return names.hashCode();
	}

	public void addTopic(String topic)
	{
		if (!names.contains(topic)) {
			names.add(topic);
		}
	}

	public void removeTopic(String topic)
	{
		names.remove(topic);
	}

	public void clearTopics()
	{
		names.clear();
	}

}
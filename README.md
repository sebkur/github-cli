This project is an (incomplete) command line tool to make queries to the
[GitHub API](https://developer.github.com/v3/).
It is built on top of this Java library:
[kohsuke/github-api](https://github.com/kohsuke/github-api).

Features include:
* list repositories of a user/organization
* list pull requests for a repository
* show details about a specified pull request

## Build and run

Java 8 is required because we use lambda expressions here and there. Also
Gradle is needed for building the project.

Build the project like this:

    ./gradlew clean create

Then run it:

    ./scripts/hubcli

## Credentials

The program will connect anonymously to the GitHub API by default. To use
an OAuth token, export the `GITHUB_OAUTH` variable with a token that you can
generate in [your settings](https://github.com/settings/tokens). This will
increase the [rate limit](https://developer.github.com/v3/#rate-limiting)
from 60 calls per hour to 5000 (as of January 2017).

You can also specify the `GITHUB_LOGIN` variable in addition to prevent the
underlying library to make an additional call to the API upon initialization
of the connection to retrieve the user that the token belongs to.

### Modifying data

If you actually want to manipulate your GitHub profile and repositories,
you need to grant certain permissions in your [token
settings](https://github.com/settings/tokens).

## Todo

* When `clone` fails, delete the directory with the failed checkout if it
  has been created by us.

## Subcommands

The command line supports a number of subcommands as a first argument:

    ./scripts/hubcli <command>

Here's the list of commands:

    list-repos
    list-pull-requests
    show-pull-request
    list-issues
    show-issue
    fork
    fork-and-clone
    rate-limit
    search-popular
    clone
    clone-for-pr
    repo-info
    add-topic
    remove-topic
    set-topics
    list-releases

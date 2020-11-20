package slack.set.profile;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Command(
        name = "slack-set-profile",
        header = {"", "@|red Slack profile red|@"},
        description = "...",
        mixinStandardHelpOptions = true
)
public class SlackSetProfileCommand implements Runnable {

    private final HttpClient httpClient = HttpClient.newBuilder().build();


    @Option(names = {"-t", "--token"},
            paramLabel = "TOKEN",
            required = true,
            interactive = true,
            arity = "0..1", // optionally interactive
            description = "Slack Oauth token",
            scope = ScopeType.INHERIT)
    String token;

    public static void main(String[] args) {
        System.exit(new CommandLine(new SlackSetProfileCommand())
                            .execute(args));
    }

    public void run() {
    }


    @Command(name = "read-profile", description = "Read slack user profile")
    void readUserProfile() {
        try {
            final HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                               .GET()
                               .uri(URI.create("https://slack.com/api/users.profile.get?pretty=1"))
                               .header("Authorization", "Bearer " + token)
                               .build(),
                    BodyHandlers.ofString()
            );

            System.out.printf("%s", httpResponse);
            System.out.printf("%s", httpResponse.headers());
            System.out.printf("%s", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.err.printf("Error while reaching slack.com/api: %s", e);
        }
    }

    @Command(name = "write-profile", description = "Write slack user profile")
    void writeUserProfile() {
        try {
            final HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                               .POST(BodyPublishers.ofString(
                                       "{\n" +
                                       "    \"profile\": {\n" +
                                       "        \"real_name\": \"bric3\",\n" +
                                       "        \"status_text\": \"graalvm\",\n" +
                                       "        \"status_emoji\": \":allthethings:\",\n" +
                                       "        \"status_expiration\": " + Instant.now().plus(10, ChronoUnit.MINUTES).getEpochSecond() + "\n" +
                                       "    }\n" +
                                       "}"))
                               .uri(URI.create("https://slack.com/api/users.profile.set?pretty=1"))
                               .header("Authorization", "Bearer " + token)
                               .header("Content-Type", "application/json; charset=utf-8")
                               .build(),
                    BodyHandlers.ofString()
            );

            System.out.printf("%s", httpResponse);
            System.out.printf("%s", httpResponse.headers());
            System.out.printf("%s", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.err.printf("Error while reaching slack.com/api: %s", e);
        }
    }
}

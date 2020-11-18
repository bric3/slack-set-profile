package slack.set.profile;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

@Command(
        name = "slack-set-profile",
        header = {"", "@|red Slack profile red|@"},
        description = "...",
        mixinStandardHelpOptions = true
)
public class SlackSetProfileCommand implements Runnable {

    private static final Logger log = LoggerFactory.getLogger("http");
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
        PicocliRunner.run(SlackSetProfileCommand.class, args);
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

            log.info("{}", httpResponse);
            log.info("{}", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            log.error("Error while reaching slack.com/api", e);
        }
    }

    @Command(name = "write-profile", description = "Read slack user profile")
    void writeUserProfile() {
        try {
            final HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                               .uri(URI.create("https://slack.com/api/users.profile.get?pretty=1"))
                               .header("Authorization", "Bearer " + token)
                               .build(),
                    BodyHandlers.ofString()
            );

            log.info("{}", httpResponse);
            log.info("{}", httpResponse.body());
        } catch (IOException | InterruptedException e) {
            log.error("Error while reaching slack.com/api", e);
        }
    }
}

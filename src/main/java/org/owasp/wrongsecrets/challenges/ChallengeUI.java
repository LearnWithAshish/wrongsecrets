package org.owasp.wrongsecrets.challenges;

import lombok.Getter;
import org.owasp.wrongsecrets.RuntimeEnvironment;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Wrapper class to move logic from Thymeleaf to keep logic in code instead of the html file.
 */
@Getter
public class ChallengeUI {

    private static final Pattern challengePattern = Pattern.compile("(\\D+)(\\d+)");

    private final Challenge challenge;
    private final int challengeNumber;
    private final RuntimeEnvironment runtimeEnvironment;

    public ChallengeUI(Challenge challenge, int challengeNumber, RuntimeEnvironment runtimeEnvironment) {
        this.challenge = challenge;
        this.challengeNumber = challengeNumber;
        this.runtimeEnvironment = runtimeEnvironment;
    }

    /**
     * Converts the name of the class into the challenge name.
     * @return String with name of the challenge.
     */
    public String getName() {
        var matchers = challengePattern.matcher(challenge.getClass().getSimpleName());
        if (matchers.matches()) {
            return matchers.group(1) + " " + matchers.group(2);
        }
        return "Unknown";
    }

    /**
     * gives back the number of the challenge.
     * @return int with challenge number.
     */
    public Integer getLink() {
        return challengeNumber;
    }

    /**
     * Returns the tech used for a challenge.
     * @return string with tech.
     */
    public String getTech() {
        return challenge.getTech();
    }

    /**
     * Returns the number of the next challenge (e.g current+1).
     * @return int with next challenge number.
     */
    public Integer next() {
        return challengeNumber + 1;
    }

    /**
     * Returns the number of the previous challenge (e.g current-1).
     * @return int with previous challenge number.
     */
    public Integer previous() {
        return challengeNumber - 1;
    }

    /**
     * Returns filename of the explanation of the challenge.
     * @return String with filename.
     */
    public String getExplanation() {
        return challenge.getExplanation();
    }

    /**
     * Returns filename of the hints for the challenge.
     * @return String with filename.
     */
    public String getHint() {
        List<RuntimeEnvironment.Environment> limitedOnlineEnvs = List.of(RuntimeEnvironment.Environment.HEROKU_DOCKER, RuntimeEnvironment.Environment.FLY_DOCKER, RuntimeEnvironment.Environment.OKTETO_K8S);
        if (limitedOnlineEnvs.contains(runtimeEnvironment.getRuntimeEnvironment()) && challenge.isLimittedWhenOnlineHosted()) {
            return challenge.getHint() + "_limitted";
        }
        return challenge.getHint();
    }

    /**
     * Returns filename of the reasons of the challenge.
     * @return String with filename.
     */
    public String getReason() {
        return challenge.getReason();
    }

    /**
     * String providing the minimal required env. Used in homescreen.
     * @return String with required env.
     */
    public String requiredEnv() {
        return challenge.supportedRuntimeEnvironments().stream()
            .map(Enum::name)
            .limit(1)
            .collect(Collectors.joining());
    }

    /**
     * returns integer with difficulty of the challenge.
     * @return int
     */
    public int difficulty() {
        return challenge.difficulty();
    }

    /**
     * checks whether challenge is enabled based on used runtimemode and CTF enablement.
     * @return boolean true if the challenge can run.
     */
    public boolean isChallengeEnabled() {
        if (runtimeEnvironment.runtimeInCTFMode()) {
            return runtimeEnvironment.canRun(challenge) && challenge.canRunInCTFMode();
        }
        return runtimeEnvironment.canRun(challenge);
    }

    /**
     * returns the list of challengeUIs based on the status sof the runtime.
     * @param challenges actual challenges to be used in app.
     * @param environment the runtime env we are running on as an app.
     * @return list of ChallengeUIs.
     */
    public static List<ChallengeUI> toUI(List<Challenge> challenges, RuntimeEnvironment environment) {
        return challenges.stream()
            .sorted(Comparator.comparingInt(challenge -> Integer.parseInt(challenge.getClass().getSimpleName().replace("Challenge", ""))))
            .map(challenge -> new ChallengeUI(challenge, challenges.indexOf(challenge), environment))
            .toList();
    }
}

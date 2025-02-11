package org.owasp.wrongsecrets.challenges.docker;


import org.owasp.wrongsecrets.RuntimeEnvironment;
import org.owasp.wrongsecrets.ScoreCard;
import org.owasp.wrongsecrets.challenges.Challenge;
import org.owasp.wrongsecrets.challenges.ChallengeTechnology;
import org.owasp.wrongsecrets.challenges.Spoiler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.owasp.wrongsecrets.RuntimeEnvironment.Environment.DOCKER;

/**
 * Challenge to find the hardcoded password in code.
 */
@Component
@Order(1)
public class Challenge1 extends Challenge {

    public Challenge1(ScoreCard scoreCard) {
        super(scoreCard);
    }

    @Override
    public boolean canRunInCTFMode() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spoiler spoiler() {
        return new Spoiler(Constants.password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean answerCorrect(String answer) {
        return Constants.password.equals(answer);
    }

    /**
     * {@inheritDoc}
     */
    public List<RuntimeEnvironment.Environment> supportedRuntimeEnvironments() {
        return List.of(DOCKER);
    }

    /**
     * {@inheritDoc}
     * Difficulty: 1
     */
    @Override
    public int difficulty() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * Git based.
     */
    @Override
    public String getTech() {
        return ChallengeTechnology.Tech.GIT.id;
    }

    @Override
    public boolean isLimittedWhenOnlineHosted() {
        return false;
    }

}

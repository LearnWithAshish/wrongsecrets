
package org.owasp.wrongsecrets.challenges.docker;

import lombok.extern.slf4j.Slf4j;
import org.owasp.wrongsecrets.RuntimeEnvironment;
import org.owasp.wrongsecrets.ScoreCard;
import org.owasp.wrongsecrets.challenges.Challenge;
import org.owasp.wrongsecrets.challenges.ChallengeTechnology;
import org.owasp.wrongsecrets.challenges.Spoiler;
import org.owasp.wrongsecrets.challenges.docker.binaryexecution.BinaryExecutionHelper;
import org.owasp.wrongsecrets.challenges.docker.binaryexecution.MuslDetectorImpl;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.owasp.wrongsecrets.RuntimeEnvironment.Environment.DOCKER;

/**
 * This challenge is about finding a secret hardcoded in a Rust binary.
 */
@Component
@Order(22)
@Slf4j
public class Challenge22 extends Challenge {

    private final BinaryExecutionHelper binaryExecutionHelper;

    public Challenge22(ScoreCard scoreCard) {
        super(scoreCard);
        this.binaryExecutionHelper = new BinaryExecutionHelper(22, new MuslDetectorImpl());
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
        return new Spoiler(binaryExecutionHelper.executeCommand("", "wrongsecrets-rust"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean answerCorrect(String answer) {
        return binaryExecutionHelper.executeCommand(answer, "wrongsecrets-rust").equals("This is correct! Congrats!");
    }

    /**
     * {@inheritDoc}
     */
    public List<RuntimeEnvironment.Environment> supportedRuntimeEnvironments() {
        return List.of(DOCKER);
    }

    /**
     * {@inheritDoc}
     * Difficulty: 5.
     */
    @Override
    public int difficulty() {
        return 5;
    }

    /**
     * {@inheritDoc}
     * Binary based.
     */
    @Override
    public String getTech() {
        return ChallengeTechnology.Tech.BINARY.id;
    }

    @Override
    public boolean isLimittedWhenOnlineHosted() {
        return false;
    }
}

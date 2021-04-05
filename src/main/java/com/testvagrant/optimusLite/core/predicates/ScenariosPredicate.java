package com.testvagrant.optimusLite.core.predicates;


import com.testvagrant.optimusLite.commons.entities.reportParser.ExecutedScenario;
import com.testvagrant.optimusLite.commons.entities.reportParser.Step;

import java.util.function.Predicate;

public class ScenariosPredicate {

    public static Predicate<ExecutedScenario> whenScenarioHasErrorStep() {
        return executedScenario -> executedScenario.getSteps().stream().anyMatch(step -> step.getError_message()!=null);
    }

    public static Predicate<Step> whenStepHasErrorMessage() {
        return step -> step.getError_message()!=null;
    }
}

package com.example.spel_demo;

import com.example.spel_demo.service.VariableComputationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationException;

import java.util.Map;

@SpringBootTest
class VariableComputationServiceTest {

    @Autowired
    private VariableComputationService computationService;

    @Test
    void simpleComputation_succeeds() {

        /* GIVEN */
        final String expression = "#var1 + #var2";
        final Map<String, Object> variables = Map.of("var1", 1, "var2", 2);

        /* WHEN */
        final Object result = computationService.compute(expression, variables);

        /* THEN */
        Assertions.assertEquals(3, result);
    }

    @Test
    void computeValue_missingVariable_fails() {

        /* GIVEN */
        final String expression = "#var1 + #var2";
        final Map<String, Object> variables = Map.of("var1", 1); // var2 is missing

        /* WHEN */
        EvaluationException e = Assertions.assertThrows(EvaluationException.class,
                () -> computationService.compute(expression, variables));

        /* THEN */
        Assertions.assertEquals("Error evaluating expression: EL1030E: The operator 'ADD' is not supported " +
                "between objects of type 'java.lang.Integer' and 'null'", e.getMessage());
    }

    @Test
    void computeValue_invalidExpression_fails() {

        /* GIVEN */
        final String expression = "#var1 +"; // invalid expression
        final Map<String, Object> variables = Map.of("var1", 1);

        /* WHEN */
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> computationService.compute(expression, variables));

        /* THEN */
        Assertions.assertEquals("Invalid expression: Expression [#var1 +] @6: EL1042E: Problem parsing right " +
                "operand", e.getMessage());
    }

    @Test
    void computeCustomFunction_succeeds() {

        /* GIVEN */
        final String expression = "customFunction(#var1)";
        final Map<String, Object> variables = Map.of("var1", 2.0);

        /* WHEN */
        final Object result = computationService.compute(expression, variables);

        /* THEN */
        Assertions.assertEquals(4.0, result);
    }

}

package com.example.spel_demo.service;

import com.example.spel_demo.evaluation.CustomEvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VariableComputationService {

    private final SpelParserConfiguration config = new SpelParserConfiguration(false, false);
    private final SpelExpressionParser parser = new SpelExpressionParser(config);

    /**
     * This method will compute the value of the expression using the variables provided.
     *
     * @param expression The user-provided expression, i.e. the 'synthetic variable'.
     * @param variables  The dynamic variables to be used in the expression.
     * @return The computed value of the expression.
     */
    public Object compute(String expression, Map<String, Object> variables) {
        try {
            // Create a custom context to implement custom functions
            final CustomEvaluationContext customContext = new CustomEvaluationContext();

            // Wrap the custom context in a StandardEvaluationContext and add the dynamic variables
            final StandardEvaluationContext context = new StandardEvaluationContext(customContext);
            context.setVariables(variables);

            // Evaluate the expression.
            //
            // In case we know which return type we expect, we can either manually cast the result or use the overloaded
            // method #parseExpression(expression, expectedReturnType) to get the result in the expected type.
            return parser.parseExpression(expression).getValue(context);
        }
        catch (SpelParseException e) {
            // The expression is not valid
            throw new IllegalArgumentException("Invalid expression: " + e.getMessage(), e);
        }
        catch (SpelEvaluationException e) {
            // Something went wrong while evaluating the expression
            throw new EvaluationException("Error evaluating expression: " + e.getMessage(), e);
        }

    }

}

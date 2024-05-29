package com.example.spel_demo.evaluation;

public class CustomEvaluationContext {


    public CustomEvaluationContext() {
        // We can even inject service dependencies here
    }

    public Double customFunction(Double value) {
        return value * 2; // just a dummy implementation
    }
}

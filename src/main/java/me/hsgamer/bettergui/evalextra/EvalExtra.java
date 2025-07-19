package me.hsgamer.bettergui.evalextra;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expression.ExpressionUtils;
import me.hsgamer.hscore.variable.VariableBundle;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public final class EvalExtra implements Expansion {
    private final ExpressionConfiguration configuration = ExpressionUtils.applyExpressionConfigurationModifier(
            ExpressionConfiguration.builder()
                    .implicitMultiplicationAllowed(false)
                    .structuresAllowed(false)
                    .arraysAllowed(false)
                    .build()
    );
    private final Map<String, Expression> cachedExpressionMap = new ConcurrentHashMap<>();
    private final Set<String> cachedStaticStringMap = new ConcurrentSkipListSet<>();
    private final VariableBundle variableBundle = new VariableBundle();

    private String evaluate(String expression) {
        String skipEvalString = "[skip-eval]";
        if (expression.startsWith(skipEvalString)) {
            return expression.substring(skipEvalString.length());
        }

        if (cachedStaticStringMap.contains(expression)) {
            return expression;
        }

        Expression eval = cachedExpressionMap.computeIfAbsent(expression, key -> new Expression(expression, configuration));
        try {
            return eval.evaluate().getStringValue();
        } catch (Exception e) {
            cachedStaticStringMap.add(expression);
            return expression;
        }
    }

    @Override
    public void onEnable() {
        StringReplacerApplier.getStringReplacers().add(this::evaluate);
        variableBundle.register("eval_", this::evaluate);
    }

    @Override
    public void onDisable() {
        variableBundle.unregisterAll();
    }
}

package me.hsgamer.bettergui.evalextra;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expression.ExpressionUtils;

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
    private final String skipEvalString = "[skip-eval]";
    private final Map<String, Expression> cachedExpressionMap = new ConcurrentHashMap<>();
    private final Set<String> cachedStaticStringMap = new ConcurrentSkipListSet<>();

    @Override
    public void onEnable() {
        StringReplacerApplier.getStringReplacers().add((original) -> {
            if (original.startsWith(skipEvalString)) {
                return original.substring(skipEvalString.length());
            }

            if (cachedStaticStringMap.contains(original)) {
                return original;
            }

            Expression expression = cachedExpressionMap.computeIfAbsent(original, key -> new Expression(original, configuration));
            try {
                return expression.evaluate().getStringValue();
            } catch (Exception e) {
                cachedStaticStringMap.add(original);
                return original;
            }
        });
    }
}

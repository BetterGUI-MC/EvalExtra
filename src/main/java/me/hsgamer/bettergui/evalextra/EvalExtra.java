package me.hsgamer.bettergui.evalextra;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expression.ExpressionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EvalExtra implements Expansion {
    private final ExpressionConfiguration configuration = ExpressionUtils.applyExpressionConfigurationModifier(
            ExpressionConfiguration.builder()
                    .implicitMultiplicationAllowed(false)
                    .structuresAllowed(false)
                    .arraysAllowed(false)
                    .build()
    );
    private final Pattern skipPattern = Pattern.compile("\\[skip-eval]\\s?(.*)", Pattern.CASE_INSENSITIVE);

    @Override
    public void onEnable() {
        StringReplacerApplier.getStringReplacers().add((original) -> {
            Matcher matcher = skipPattern.matcher(original);
            if (matcher.find()) {
                return matcher.group(1);
            }
            try {
                return new Expression(original, configuration).evaluate().getStringValue();
            } catch (Exception e) {
                return original;
            }
        });
    }
}

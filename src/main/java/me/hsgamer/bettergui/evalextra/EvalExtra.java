package me.hsgamer.bettergui.evalextra;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.expression.ExpressionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EvalExtra extends PluginAddon {
    private final ExpressionConfiguration configuration = ExpressionConfiguration.builder()
            .implicitMultiplicationAllowed(false)
            .structuresAllowed(false)
            .arraysAllowed(false)
            .build();
    private final Pattern skipPattern = Pattern.compile("\\[skip-eval]\\s?(.*)", Pattern.CASE_INSENSITIVE);

    @Override
    public void onEnable() {
        StringReplacerApplier.getStringReplacerMap().put("eval-extra", (original, uuid) -> {
            Matcher matcher = skipPattern.matcher(original);
            if (matcher.find()) {
                return matcher.group(1);
            }
            try {
                Expression expression = ExpressionUtils.createExpression(original, () -> configuration);
                return expression.evaluate().getStringValue();
            } catch (Exception e) {
                return original;
            }
        });
    }
}

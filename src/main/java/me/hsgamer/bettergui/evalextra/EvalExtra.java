package me.hsgamer.bettergui.evalextra;

import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.expression.ExpressionUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EvalExtra extends PluginAddon {
    private final Pattern skipPattern = Pattern.compile("\\[skip-eval]\\s?(.*)", Pattern.CASE_INSENSITIVE);

    @Override
    public void onEnable() {
        StringReplacerApplier.getStringReplacerMap().put("eval-extra", (original, uuid) -> {
            Matcher matcher = skipPattern.matcher(original);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return Optional.ofNullable(ExpressionUtils.getResult(original)).map(BigDecimal::toPlainString).orElse(original);
        });
    }
}

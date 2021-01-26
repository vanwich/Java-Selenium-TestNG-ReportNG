package help.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.money.*;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import org.apache.commons.lang3.LocaleUtils;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.javamoney.moneta.function.MonetaryOperators;
import help.config.ClassConfigurator;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;
import help.exceptions.JstrException;
import help.utils.i18n.I18nResolver;

public class Currency {
    /**
     * Locale which will be used for currency initialization. Default value - {@link I18nResolver#defaultLocale}
     * Could be changed by providing tests.%1$s.currency.locale where %1$s short code of the needed Locale according to I18N policy used in isba framework. Example: tests.zh_TW.currency.locale=en_US
     * If tests.%1$s.currency.locale is not defined -> tests.locale property will be used as default
     * If test.locale is not defined -> {@link I18nResolver#defaultLocale}
     */
    private static Locale locale = initializeLocale();
    /**
     * Java Money {@link CurrencyUnit}, initialized according to the {@link Currency#locale}
     */
    @ClassConfigurator.Configurable(byClassName = true)
    private static CurrencyUnit currencyUnit = Monetary.getCurrency(locale);
    /**
     * Default configurable toString representation including Currency sign. e.g. $100, €100, etc.
     */
    @ClassConfigurator.Configurable(byClassName = true)
    private static Function<Currency, String> defaultToString = currency -> {
        MonetaryAmountFormat format;
        if (currency.isNegative()) {
            format = MonetaryFormats.getAmountFormat(
                    AmountFormatQueryBuilder.of(locale)
                            .set(CurrencyStyle.SYMBOL)
                            .set("pattern", "(¤#,##0.00###)")
                            .build());
        } else {
            format = MonetaryFormats.getAmountFormat(
                    AmountFormatQueryBuilder.of(locale)
                            .set(CurrencyStyle.SYMBOL)
                            .set("pattern", "¤#,##0.00###")
                            .build());
        }

        return format.format(currency.value).replace("-", "");
    };
    /**
     * Default configurable toPlainString representation excluding Currency sign. e.g. 100, 100.00, etc.
     */
    @ClassConfigurator.Configurable(byClassName = true)
    private static Function<Currency, String> defaultToPlainString = currency -> {
        MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(locale)
                        .set("pattern", "0.00###")
                        .build());
        return format.format(currency.value);
    };

    static {
        ClassConfigurator configurator = new ClassConfigurator(Currency.class);
        configurator.applyConfiguration();
    }

    /**
     * Rounding mode which will be used for each {@link Currency} object. Used for each invoked method.
     */
    @ClassConfigurator.Configurable
    private RoundingMode rounding = RoundingMode.UP;
    /**
     * Java Money {@link MonetaryAmount} value
     */
    private MonetaryAmount value;

    public Currency() {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = Money.zero(currencyUnit);
    }

    public Currency(MonetaryAmount ma) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = ma;
    }

    public Currency(Supplier<MonetaryAmount> supplier) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = supplier.get();
    }

    public Currency(Object obj) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = toMonetary(String.valueOf(obj));
    }

    public Currency(Currency obj) {
        this.rounding = obj.rounding;
        this.value = Money.zero(currencyUnit).add(obj.value);
    }

    public Currency(Object obj, RoundingMode roundingMode) {
        this.rounding = roundingMode;
        this.value = toMonetary(String.valueOf(obj));
    }

    public Currency(BigDecimal bd, RoundingMode roundingMode) {
        this.rounding = roundingMode;
        this.value = roundToTwo(Money.of(bd, currencyUnit)).with(Monetary.getRounding(
                RoundingQueryBuilder.of().set(roundingMode).build()));
    }

    public Currency(Object obj, MonetaryOperator mo) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = toMonetary(String.valueOf(obj)).with(mo);
    }

    public Currency(BigDecimal bd) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        configurator.applyConfiguration();
        this.value = roundToTwo(Money.of(bd, currencyUnit));
    }


    public MonetaryAmount getValue() {
        return value;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setValue(MonetaryAmount value) {
        this.value = value;
    }

    public Currency add(Currency currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.add(currencyValue.value);
        return new Currency(roundToTwo(newValue.value));
    }

    public Currency add(double currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.add(new Currency(currencyValue).value);
        roundToTwo(newValue.value);
        return newValue;
    }

    public Currency subtract(Currency currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.subtract(currencyValue.value);
        newValue.value = roundToTwo(newValue.value);
        return newValue;
    }

    public Currency subtract(double currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.subtract(new Currency(currencyValue).value);
        newValue.value = roundToTwo(newValue.value);
        return newValue;
    }

    public Currency multiply(Currency currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.multiply(currencyValue.value.getNumber());
        newValue.value = roundToTwo(newValue.value);
        return newValue;
    }

    public Currency multiply(double dValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.multiply(dValue);
        newValue.value = roundToTwo(newValue.value);
        return newValue;
    }

    public Currency divide(Currency currencyValue) {
        Currency newValue = new Currency(this);
        newValue.value = value.divide(currencyValue.value.getNumber());
        newValue.value = roundToTwo(newValue.value);
        return newValue;
    }

    public Currency divide(double dValue) {
        Currency newValue = new Currency(this);
        newValue.value = roundToTwo(value.divide(dValue));
        return newValue;
    }

    public Currency abs() {
        Currency r = new Currency(this);
        r.value = roundToTwo(value.abs());
        return r;
    }

    public Currency negate() {
        Currency r = new Currency(this);
        r.value = roundToTwo(value.negate());
        return r;
    }

    public Currency plus() {
        Currency r = new Currency(this);
        r.value = roundToTwo(value.plus());
        return r;
    }

    public Currency getPercentage(Number percents) {
        Currency newValue = new Currency(this);
        newValue.value = newValue.value.with(MonetaryOperators.percent(percents));
        return newValue;
    }

    public Currency getPercentageWithRoundingToTwo(Number percents) {
        Currency newValue = new Currency(this);
        newValue.value = roundToTwo(newValue.value.with(MonetaryOperators.percent(percents)));
        return newValue;
    }

    public Currency applyConfiguration(String configurationName) {
        ClassConfigurator configurator = new ClassConfigurator(this);
        return (Currency) configurator.applyConfiguration(configurationName);
    }

    public String getSymbol() {
        return java.util.Currency.getInstance(locale).getSymbol(locale);
    }

    public String format(MonetaryAmountFormat customFormat) {
        return customFormat.format(this.value);
    }

    public BigDecimal asBigDecimal() {
        return BigDecimal.valueOf(this.value.getNumber().doubleValue());
    }

    public boolean isNegative() {
        return this.value.isNegative();
    }

    public boolean isPositive() {
        return this.value.isPositive();
    }

    public boolean isZero() {
        return this.value.isZero();
    }

    public boolean lessThan(Currency amount) {
        return this.value.isLessThan(amount.value);
    }

    public boolean moreThan(Currency amount) {
        return this.value.isGreaterThan(amount.value);
    }

    public Currency min(Currency amount) {
        return this.value.isLessThan(amount.value) ? this : amount;
    }

    public Currency max(Currency amount) {
        return this.value.isGreaterThan(amount.value) ? this : amount;
    }

    public String toPlainString() {
        return defaultToPlainString.apply(this);
    }

    @Override
    public String toString() {
        return defaultToString.apply(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Currency other = (Currency) obj;
        if (this.value == null) {
            return other.value == null;
        } else {
            return this.value.equals(other.value);
        }
    }

    /**
     * {@link String} to {@link MonetaryAmount} format
     */
    private MonetaryAmount toMonetary(String sum) {
        BigDecimal temp;
        try {
            temp = new BigDecimal(sum.replace("(", "-").replaceAll("([Ee][^\\d+\\-]+)", "").replaceAll("[^Ee\\d.+\\-]", ""));
        } catch (NumberFormatException nfe) {
            throw new JstrException(String.format("Value '%s' can't be converted to financial value", sum), nfe);
        }
        return roundToTwo(Money.of(temp, currencyUnit));
    }

    /**
     * Rounds {@link BigDecimal} number value to 2 digits
     */
    private MonetaryAmount roundToTwo(MonetaryAmount amt) {
        return amt.with(MonetaryOperators.rounding(rounding, 2));
    }

    private static Locale initializeLocale() {
        Locale currencyLocale;
        if (PropertyProvider.isDefined(String.format(CustomTestProperties.CURRENCY_LOCALE, I18nResolver.locale.toLowerCase()))) {
            String locale = PropertyProvider.getPropertyOrThrow(String.format(CustomTestProperties.CURRENCY_LOCALE, I18nResolver.locale.toLowerCase()));
            currencyLocale = LocaleUtils.toLocale(locale);
        } else {
            currencyLocale = LocaleUtils.toLocale(PropertyProvider.getProperty(CustomTestProperties.TESTS_LOCALE, I18nResolver.defaultLocale));
        }
        return currencyLocale;
    }
}

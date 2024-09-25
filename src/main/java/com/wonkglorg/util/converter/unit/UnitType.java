package com.wonkglorg.util.converter.unit;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Different writing forms of equivalent unit types from the SI units
 */
public enum UnitType {
	YOTTA("Y", "septillion", 24, 1e24),
	ZETTA("Z", "sextillion", 21, 1e21),
	EXA("E", "quintillion", 18, 1e18),
	PETA("P", "quadrillion", 15, 1e15),
	TERA("T", "trillion", 12, 1e12),
	GIGA("G", "billion", 9, 1e9),
	MEGA("M", "million", 6, 1e6),
	KILO("k", "thousand", 3, 1e3),
	HECTO("h", "hundred", 2, 1e2),
	DECA("da", "ten", 1, 1e1),
	DECI("d", "tenth", -1, 1e-1),
	CENTI("c", "hundredth", -2, 1e-2),
	MILLI("m", "thousandth", -3, 1e-3),
	MICRO("μ", "millionth", -6, 1e-6),
	NANO("n", "billionth", -9, 1e-9),
	PICO("p", "trillionth", -12, 1e-12),
	FEMTO("f", "quadrillionth", -15, 1e-15),
	ATTO("a", "quintillionth", -18, 1e-18),
	ZEPTO("z", "sextillionth", -21, 1e-21),
	YOCTO("y", "septillionth", -24, 1e-24);

	private final String symbol;
	private final String languageFactor;
	private final int powerFactor;
	private final double multiplier;

	private static final Map<String, UnitType> SYMBOL_LOOKUP =
			Arrays.stream(values()).collect(Collectors.toMap(UnitType::getSymbol, Function.identity()));
	private static final Map<Integer, UnitType> POWER_LOOKUP = Arrays.stream(values())
			.collect(Collectors.toMap(UnitType::getPowerFactor, Function.identity()));

	UnitType(String symbol, String languageFactor, int powerFactor, double multiplier) {
		this.symbol = symbol;
		this.languageFactor = languageFactor;
		this.powerFactor = powerFactor;
		this.multiplier = multiplier;
	}

	public String getPrefix() {
		return name().toLowerCase();
	}

	/**
	 * @return the multiplier to convert this number to its base non SI form
	 * <p>
	 * Example: Kilo is a multiplier of 1000 so dividing kilo gramm by 1000 returns the base form of
	 * gramm
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * @return the valid SI Symbol for this value
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the power for this number
	 */
	public int getPowerFactor() {
		return powerFactor;
	}

	/**
	 * Language factored values
	 * <p>
	 * Example: Kilo = thousand, giga = billion
	 *
	 * @return
	 */
	public String getLanguageFactor() {
		return languageFactor;
	}

	/**
	 * Converts a value from this unit type to the base unit.
	 *
	 * @param value The value to be converted
	 * @return The value in base units
	 */
	public double convertToBase(double value) {
		return value * multiplier;
	}

	/**
	 * @return the inverse multiplier to convert from the base unit to the SI unit
	 */
	public double getInverseMultiplier() {
		return 1 / multiplier;
	}

	/**
	 * Gets the inverse UnitType of this type based on its power factor
	 *
	 * @example {@link #KILO} -> {@link #MILLI} (power 3 -> power -3)
	 */
	public UnitType getInverseUnitType() {
		return POWER_LOOKUP.get(this.powerFactor * -1);
	}

	/**
	 * Gets the inverse UnitType of the given type based on its power factor
	 *
	 * @example {@link #KILO} -> {@link #MILLI} (power 3 -> power -3)
	 */
	public static UnitType getInverseUnitType(UnitType unitType) {
		return POWER_LOOKUP.get(unitType.powerFactor * -1);
	}

	/**
	 * Gets the {@link UnitType} from its symbol
	 *
	 * @param symbol the symbol to lookup
	 */
	public static UnitType fromSymbol(String symbol) {
		return SYMBOL_LOOKUP.get(symbol);
	}

	/**
	 * Converts a value from the base unit to this unit type.
	 *
	 * @param baseValue The value in base units
	 * @return The value in this unit type
	 */
	public double convertFromBase(double baseValue) {
		return baseValue / multiplier;
	}
}

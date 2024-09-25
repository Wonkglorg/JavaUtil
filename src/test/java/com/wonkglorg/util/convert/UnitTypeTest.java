package com.wonkglorg.util.convert;

import com.wonkglorg.util.converter.unit.UnitType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitTypeTest {
	@Test
	void testConvertToBase() {
		// Positive powers
		assertEquals(1000, UnitType.KILO.convertToBase(1), 0.0001);
		assertEquals(1e6, UnitType.MEGA.convertToBase(1), 0.0001);
		assertEquals(1e9, UnitType.GIGA.convertToBase(1), 0.0001);
		assertEquals(1e12, UnitType.TERA.convertToBase(1), 0.0001);
		assertEquals(1e15, UnitType.PETA.convertToBase(1), 0.0001);
		assertEquals(1e18, UnitType.EXA.convertToBase(1), 0.0001);
		assertEquals(1e21, UnitType.ZETTA.convertToBase(1), 0.0001);
		assertEquals(1e24, UnitType.YOTTA.convertToBase(1), 0.0001);

		// Negative powers
		assertEquals(0.001, UnitType.MILLI.convertToBase(1), 0.0001);
		assertEquals(0.000001, UnitType.MICRO.convertToBase(1), 0.0001);
		assertEquals(1e-9, UnitType.NANO.convertToBase(1), 0.0001);
		assertEquals(1e-12, UnitType.PICO.convertToBase(1), 0.0001);
		assertEquals(1e-15, UnitType.FEMTO.convertToBase(1), 0.0001);
		assertEquals(1e-18, UnitType.ATTO.convertToBase(1), 0.0001);
		assertEquals(1e-21, UnitType.ZEPTO.convertToBase(1), 0.0001);
		assertEquals(1e-24, UnitType.YOCTO.convertToBase(1), 0.0001);

		// Special cases
		assertEquals(10, UnitType.DECA.convertToBase(1), 0.0001);
		assertEquals(100, UnitType.HECTO.convertToBase(1), 0.0001);
		assertEquals(0.1, UnitType.DECI.convertToBase(1), 0.0001);
		assertEquals(0.01, UnitType.CENTI.convertToBase(1), 0.0001);
	}

	@Test
	void testConvertFromBase() {
		// Positive powers
		assertEquals(1, UnitType.KILO.convertFromBase(1000), 0.0001);
		assertEquals(1, UnitType.MEGA.convertFromBase(1e6), 0.0001);
		assertEquals(1, UnitType.GIGA.convertFromBase(1e9), 0.0001);
		assertEquals(1, UnitType.TERA.convertFromBase(1e12), 0.0001);
		assertEquals(1, UnitType.PETA.convertFromBase(1e15), 0.0001);
		assertEquals(1, UnitType.EXA.convertFromBase(1e18), 0.0001);
		assertEquals(1, UnitType.ZETTA.convertFromBase(1e21), 0.0001);
		assertEquals(1, UnitType.YOTTA.convertFromBase(1e24), 0.0001);

		// Negative powers
		assertEquals(1, UnitType.MILLI.convertFromBase(0.001), 0.0001);
		assertEquals(1, UnitType.MICRO.convertFromBase(0.000001), 0.0001);
		assertEquals(1, UnitType.NANO.convertFromBase(1e-9), 0.0001);
		assertEquals(1, UnitType.PICO.convertFromBase(1e-12), 0.0001);
		assertEquals(1, UnitType.FEMTO.convertFromBase(1e-15), 0.0001);
		assertEquals(1, UnitType.ATTO.convertFromBase(1e-18), 0.0001);
		assertEquals(1, UnitType.ZEPTO.convertFromBase(1e-21), 0.0001);
		assertEquals(1, UnitType.YOCTO.convertFromBase(1e-24), 0.0001);

		// Special cases
		assertEquals(1, UnitType.DECA.convertFromBase(10), 0.0001);
		assertEquals(1, UnitType.HECTO.convertFromBase(100), 0.0001);
		assertEquals(1, UnitType.DECI.convertFromBase(0.1), 0.0001);
		assertEquals(1, UnitType.CENTI.convertFromBase(0.01), 0.0001);
	}

	@Test
	void canGetUnitFromSymbol() {
		// Positive powers
		assertEquals(UnitType.YOTTA, UnitType.fromSymbol("Y"));
		assertEquals(UnitType.ZETTA, UnitType.fromSymbol("Z"));
		assertEquals(UnitType.EXA, UnitType.fromSymbol("E"));
		assertEquals(UnitType.PETA, UnitType.fromSymbol("P"));
		assertEquals(UnitType.TERA, UnitType.fromSymbol("T"));
		assertEquals(UnitType.GIGA, UnitType.fromSymbol("G"));
		assertEquals(UnitType.MEGA, UnitType.fromSymbol("M"));
		assertEquals(UnitType.KILO, UnitType.fromSymbol("k"));
		assertEquals(UnitType.HECTO, UnitType.fromSymbol("h"));
		assertEquals(UnitType.DECA, UnitType.fromSymbol("da"));

		// Negative powers
		assertEquals(UnitType.DECI, UnitType.fromSymbol("d"));
		assertEquals(UnitType.CENTI, UnitType.fromSymbol("c"));
		assertEquals(UnitType.MILLI, UnitType.fromSymbol("m"));
		assertEquals(UnitType.MICRO, UnitType.fromSymbol("μ"));
		assertEquals(UnitType.NANO, UnitType.fromSymbol("n"));
		assertEquals(UnitType.PICO, UnitType.fromSymbol("p"));
		assertEquals(UnitType.FEMTO, UnitType.fromSymbol("f"));
		assertEquals(UnitType.ATTO, UnitType.fromSymbol("a"));
		assertEquals(UnitType.ZEPTO, UnitType.fromSymbol("z"));
		assertEquals(UnitType.YOCTO, UnitType.fromSymbol("y"));
	}

	@Test
	void getInverseUnit() {
		assertEquals(UnitType.MILLI, UnitType.KILO.getInverseUnitType());
		assertEquals(UnitType.KILO, UnitType.MILLI.getInverseUnitType());

		assertEquals(UnitType.MICRO, UnitType.MEGA.getInverseUnitType());
		assertEquals(UnitType.MEGA, UnitType.MICRO.getInverseUnitType());

		assertEquals(UnitType.NANO, UnitType.GIGA.getInverseUnitType());
		assertEquals(UnitType.GIGA, UnitType.NANO.getInverseUnitType());

		assertEquals(UnitType.PICO, UnitType.TERA.getInverseUnitType());
		assertEquals(UnitType.TERA, UnitType.PICO.getInverseUnitType());

		assertEquals(UnitType.FEMTO, UnitType.PETA.getInverseUnitType());
		assertEquals(UnitType.PETA, UnitType.FEMTO.getInverseUnitType());

		assertEquals(UnitType.ATTO, UnitType.EXA.getInverseUnitType());
		assertEquals(UnitType.EXA, UnitType.ATTO.getInverseUnitType());

		assertEquals(UnitType.ZEPTO, UnitType.ZETTA.getInverseUnitType());
		assertEquals(UnitType.ZETTA, UnitType.ZEPTO.getInverseUnitType());

		assertEquals(UnitType.YOCTO, UnitType.YOTTA.getInverseUnitType());
		assertEquals(UnitType.YOTTA, UnitType.YOCTO.getInverseUnitType());

		assertEquals(UnitType.CENTI, UnitType.HECTO.getInverseUnitType());
		assertEquals(UnitType.HECTO, UnitType.CENTI.getInverseUnitType());

		assertEquals(UnitType.DECI, UnitType.DECA.getInverseUnitType());
		assertEquals(UnitType.DECA, UnitType.DECI.getInverseUnitType());
	}

	@Test
	void languageFactor(){
		assertEquals("thousand",UnitType.KILO.getLanguageFactor());
	}
}

package com.wonkglorg.util;

import com.wonkglorg.util.collection.CollectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

class CollectionUtilTest {
	private record Department(String name, int memberCount) {
	}

	@Test
	void reductionTest() {
		List<Department> departments = new ArrayList<>();
		departments.add(new Department("IT", 9));
		departments.add(new Department("HR", 9));
		departments.add(new Department("IT", 2));
		departments.add(new Department("FH", 2));

		var reducedDepartments = CollectionUtil.reduceDuplicates(departments, Department::name);

		Assertions.assertEquals(3, reducedDepartments.size());
	}

	@Test
	void reductionTestWithoutValues() {
		List<Department> departments = new ArrayList<>();
		departments.add(new Department("IT", 9));
		departments.add(new Department("HR", 9));
		departments.add(new Department("IT", 2));
		departments.add(new Department("FH", 2));

		var reducedDepartments = CollectionUtil.reduceDuplicates(departments);

		Assertions.assertEquals(4, reducedDepartments.size());
	}

	@Test
	void reductionTestWithMergeFunction() {

		BinaryOperator<Department> reduction = (a, b) -> {
			int combinedCount = a.memberCount + b.memberCount;
			return new Department(a.name, combinedCount);
		};

		List<Department> departments = new ArrayList<>();
		departments.add(new Department("IT", 9));
		departments.add(new Department("HR", 9));
		departments.add(new Department("IT", 2));
		departments.add(new Department("FH", 2));

		var reducedDepartments =
				CollectionUtil.reduceDuplicates(departments, reduction, Department::name);

		Assertions.assertEquals(3, reducedDepartments.size());

		Department itDeparment =
				reducedDepartments.stream().filter(department -> "IT".equalsIgnoreCase(department.name))
						.findFirst().orElse(null);
		Assertions.assertNotNull(itDeparment);
		Assertions.assertEquals(11, itDeparment.memberCount);
	}
}

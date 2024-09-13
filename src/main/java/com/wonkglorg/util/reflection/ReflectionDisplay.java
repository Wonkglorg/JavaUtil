package com.wonkglorg.util.reflection;

import com.wonkglorg.util.string.StringUtils;
import com.wonkglorg.util.template.StringTemplater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.wonkglorg.util.console.ConsoleUtil.println;

/**
 * A class that provides methods to display information about classes
 */
@SuppressWarnings("unused")
public class ReflectionDisplay {
	private ReflectionDisplay() {
	}

	public static void printClassInfo(Class<?> clazz) {
		println(StringUtils.padCenter("Class Info", 70, '-'));
		println(ReflectionDisplay.getClassInfo(clazz));
		println(" ");

		println(StringUtils.padCenter("Constructor Info", 70, '-'));
		ReflectionDisplay.getConstructorInfo(clazz).forEach(System.out::println);
		println(" ");

		println(StringUtils.padCenter("Field Info", 70, '-'));
		ReflectionDisplay.getFieldInfos(clazz).forEach(System.out::println);
		println(" ");

		println(StringUtils.padCenter("Method Info", 70, '-'));
		ReflectionDisplay.getMethodInfos(clazz).forEach(System.out::println);
		println(" ");
		println(StringUtils.padCenter("", 70, '-'));
	}

	public static void printCurrentStack() {
		var entries = getCurrentStack();
		println(StringUtils.padCenter("Stack", 70, '-'));
		println("");
		for (var entry : entries) {
			println(StringUtils.padCenter(entry.getFileName(), 70, '-'));
			println("Class Name:  " + entry.getClassName());
			println("Method Name: " + entry.getMethodName());
			println("Line Index:  " + entry.getLineNumber());
			println("Class loader name: " + entry.getClassLoaderName());
			println("Module name: " + entry.getModuleName());
			println("Module version: " + entry.getModuleVersion());
			println("");
		}
		println(StringUtils.padCenter("", 70, '-'));
	}

	public static List<StackTraceElement> getCurrentStack() {
		List<StackTraceElement> stack = new ArrayList<>();

		StackWalker walker = StackWalker.getInstance();
		walker.forEach(frame -> {
			if (frame.getClassName()
					.equalsIgnoreCase("com.wonkglorg.util.reflection" + ".ReflectionDisplay")) {
				return;
			}
			stack.add(frame.toStackTraceElement());
		});
		return stack;
	}

	/**
	 * Returns a string with information about the class
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the class</li>
	 *     <li>{package} - The package of the class</li>
	 *     <li>{superclass} - The superclass of the class</li>
	 *     <li>{interfaces} - The interfaces implemented by the class</li>
	 *     <li>{annotations} - The annotations of the class</li>
	 *
	 * @param clazz the class to get the information from
	 * @param format the format to use
	 * @return a string with information about the class
	 */
	public static String getClassInfo(Class<?> clazz, String format) {
		StringTemplater templater = new StringTemplater();
		templater.put("name", clazz.getName());
		templater.put("package", clazz.getPackage().getName());
		templater.put("superclass", clazz.getSuperclass().getName());
		templater.put("interfaces",
				Arrays.stream(clazz.getInterfaces()).map(Class::getName).collect(Collectors.joining(", ")));
		templater.put("annotations", getAnnotations(clazz.getDeclaredAnnotations()));
		return templater.applyAlt(format);
	}

	/**
	 * Returns a string with information about the class with the format "Class: {name}\nPackage:
	 * {package}\nSuperclass: {superclass}\nInterfaces: {interfaces}"
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the class</li>
	 *     <li>{package} - The package of the class</li>
	 *     <li>{superclass} - The superclass of the class</li>
	 *     <li>{interfaces} - The interfaces implemented by the class</li>
	 *
	 * @param clazz the class to get the information from
	 * @return a string with information about the class
	 */
	public static String getClassInfo(Class<?> clazz) {
		return getClassInfo(clazz,
				"Class: {name}\nPackage: {package}\nSuperclass: {superclass}\nInterfaces: {interfaces}");
	}

	/**
	 * Returns a list of fields in the class with a custom format.
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the field</li>
	 *     <li>{type} - The simple name of the field type</li>
	 *     <li>{fulltype} - The full name of the field type</li>
	 *     <li>{modifiers} - The modifiers of the field</li>
	 *     <li>{fulltype} - The full name of the field type</li>
	 *     <li>{annotations} - The annotations of the field</li>
	 *
	 * @param clazz the class to get the fields from
	 * @param format the format to use (Valid placeholders: {name}, {type})
	 * @return a list of fields in the class
	 */
	public static List<String> getFieldInfos(Class<?> clazz, String format) {
		List<String> fields = new ArrayList<>();

		for (var field : clazz.getDeclaredFields()) {
			StringTemplater templater = new StringTemplater();
			templater.put("name", field.getName());
			templater.put("type", field.getType().getSimpleName());
			templater.put("fulltype", field.getType().getName());
			templater.put("modifiers", getModifiers(field.getModifiers()));
			templater.put("annotations", getAnnotations(field.getDeclaredAnnotations()));
			fields.add(templater.applyAlt(format));
		}
		return fields;
	}

	/**
	 * Returns a list of fields in the class with the format "{name}({type} {modifiers} {
	 * fulltype})"
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the field</li>
	 *     <li>{type} - The simple name of the field type</li>
	 *     <li>{fulltype} - The full name of the field type</li>
	 *     <li>{modifiers} - The modifiers of the field</li>
	 *     <li>{fulltype} - The full name of the field type</li>
	 *     <li>{annotations} - The annotations of the field</li>
	 *
	 * @param clazz the class to get the fields from
	 * @return a list of fields in the class
	 */
	public static List<String> getFieldInfos(Class<?> clazz) {
		return getFieldInfos(clazz, "{modifiers} {type} {name}");
	}

	/**
	 * Returns a list of methods in the class with a custom format
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the method</li>
	 *     <li>{returntype} - The simple name of the return type</li>
	 *     <li>{fullreturntype} - The full name of the return type</li>
	 *     <li>{parameters} - The simple names of the parameters</li>
	 *     <li>{fullparameters} - The full names of the parameters</li>
	 *     <li>{modifiers} - The modifiers of the method</li>
	 *     <li>{exceptions} - The exceptions thrown by the method</li>
	 *     <li>{annotations} - The annotations of the method</li>
	 *
	 * @param clazz the class to get the methods from
	 * @param format the format to use
	 * @return a list of methods in the class
	 */
	public static List<String> getMethodInfos(Class<?> clazz, String format) {
		List<String> methods = new ArrayList<>();
		for (var method : clazz.getDeclaredMethods()) {
			StringTemplater templater = new StringTemplater();
			templater.put("name", method.getName());
			templater.put("returntype", method.getReturnType().getSimpleName());
			templater.put("fullreturntype", method.getReturnType().getName());
			templater.put("parameters", getParameterTypes(method.getParameterTypes()));
			templater.put("fullparameters", getFullParameterTypes(method.getParameterTypes()));
			templater.put("modifiers", getModifiers(method.getModifiers()));
			templater.put("exceptions", Arrays.stream(method.getExceptionTypes()).map(Class::getName)
					.collect(Collectors.joining(", ")));
			templater.put("annotations", getAnnotations(method.getDeclaredAnnotations()));
			methods.add(templater.applyAlt(format));
		}

		return methods;
	}

	/**
	 * Returns a list of methods in the class with the format "{modifiers} {returntype}
	 * {name}({parameters}) throws {exceptions}"
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the method</li>
	 *     <li>{returntype} - The simple name of the return type</li>
	 *     <li>{fullreturntype} - The full name of the return type</li>
	 *     <li>{parameters} - The simple names of the parameters</li>
	 *     <li>{fullparameters} - The full names of the parameters</li>
	 *     <li>{modifiers} - The modifiers of the method</li>
	 *     <li>{exceptions} - The exceptions thrown by the method</li>
	 *
	 * @param clazz the class to get the methods from
	 * @return a list of methods in the class
	 */
	public static List<String> getMethodInfos(Class<?> clazz) {
		return getMethodInfos(clazz,
				"{modifiers} {returntype} {name}({parameters}) throws {exceptions}");
	}

	/**
	 * Returns a list of constructors in the class with a custom format
	 * <p>
	 * Valid placeholders:
	 * <ul>
	 *     <li>{name} - The name of the constructor</li>
	 *     <li>{parameters} - The simple names of the parameters</li>
	 *     <li>{fullparameters} - The full names of the parameters</li>
	 *     <li>{modifiers} - The modifiers of the constructor</li>
	 *     <li>{annotations} - The annotations of the constructor</li>
	 *
	 * @param clazz the class to get the constructors from
	 * @param format the format to use
	 * @return
	 */
	public static List<String> getConstructorInfo(Class<?> clazz, String format) {
		List<String> constructors = new ArrayList<>();
		for (var constructor : clazz.getDeclaredConstructors()) {
			StringTemplater templater = new StringTemplater();
			templater.put("name", constructor.getName());
			templater.put("parameters", getParameterTypes(constructor.getParameterTypes()));
			templater.put("fullparameters", getFullParameterTypes(constructor.getParameterTypes()));
			templater.put("modifiers", getModifiers(constructor.getModifiers()));
			templater.put("annotations", getAnnotations(constructor.getDeclaredAnnotations()));
			constructors.add(templater.applyAlt(format));
		}
		return constructors;
	}

	public static List<String> getConstructorInfo(Class<?> clazz) {
		return getConstructorInfo(clazz,
				"{modifiers} {name}({parameters}) \nAnnotations: {annotations}");
	}

	private static String getModifiers(int modifiers) {
		return Modifier.getModifiers(modifiers).stream().map(Modifier::getName)
				.collect(Collectors.joining(" "));
	}

	private static String getAnnotations(Object[] annotations) {
		return Arrays.stream(annotations).map(Object::toString).collect(Collectors.joining(", "));
	}

	private static String getParameterTypes(Class<?>[] parameterTypes) {
		return Arrays.stream(parameterTypes).map(Class::getSimpleName)
				.collect(Collectors.joining(", "));
	}

	private static String getFullParameterTypes(Class<?>[] parameterTypes) {
		return Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", "));
	}


}

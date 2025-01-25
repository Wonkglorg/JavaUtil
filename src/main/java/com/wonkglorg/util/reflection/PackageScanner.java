package com.wonkglorg.util.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A utility class for scanning packages for classes.
 */
@SuppressWarnings("unused")
public class PackageScanner{
	
	protected static final String CLASS_FILE_EXTENSION = ".class";
	
	private PackageScanner() {
		// Utility Class
	}
	
	/**
	 * Creates a new instance of {@link JarPackageScanBuilder} with the specified jar file.
	 *
	 * @param jarFile The jar file to scan.
	 * @return {@link JarPackageScanBuilder}.
	 */
	public static JarPackageScanBuilder forJarFile(File jarFile, String packageName) {
		return new JarPackageScanBuilder(jarFile).setPackage(packageName);
	}
	
	/**
	 * Creates a new instance of {@link JarPackageScanBuilder} with the jar file of the class. (Implicitly uses the jar file of the class based on {@link Class#getProtectionDomain()})
	 *
	 * @return {@link JarPackageScanBuilder}.
	 */
	public static JarPackageScanBuilder forJarFile(String packageName) {
		return new JarPackageScanBuilder(new File(Paths.get(PackageScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath())
													   .toUri())).setPackage(packageName);
	}
	
	/**
	 * Creates a new instance of {@link ResourcePackageScanBuilder} with the specified package.
	 *
	 * @param packageName The package to scan.
	 * @return {@link ResourcePackageScanBuilder}.
	 */
	public static ResourcePackageScanBuilder forResource(String packageName) {
		return new ResourcePackageScanBuilder().setPackage(packageName);
	}
	
	@SuppressWarnings("unchecked")
	public abstract static class BasePackageScan<T extends BasePackageScan<?>>{
		/**
		 * Weather to scan sub-packages or just the specified one.
		 */
		protected boolean scanSubPackages = false;
		/**
		 * The predicate to match classes against.
		 * <br>
		 * Example: <code>Object.class::isAssignableFrom</code> to match all classes that have Object as a parent.
		 * <br>
		 * Example: <code>clazz -> clazz.isAnnotationPresent(MyAnnotation.class)</code> to match all classes that have the annotation MyAnnotation.
		 * <br>
		 * Example: <code>clazz -> !clazz.isAnonymousClass() != null</code> to filter out all anonymous classes.
		 */
		protected Predicate<Class<?>> predicate = Object.class::isAssignableFrom;
		/**
		 * The package to scan exactly as found in the code (e.g. "org.example.test").
		 */
		protected String packageName;
		/**
		 * The jar file to scan.
		 */
		protected final File jarFile;
		
		protected BasePackageScan(File jarFile) {
			this.jarFile = jarFile;
		}
		
		/**
		 * @param packageName The {@link #packageName} to scan.
		 * @return {@link T}.
		 */
		public T setPackage(String packageName) {
			this.packageName = packageName;
			return (T) this;
		}
		
		/**
		 * @param predicate The {@link #predicate} to match classes against.
		 * @return {@link T}.
		 */
		public T predicate(Predicate<Class<?>> predicate) {
			this.predicate = predicate;
			return (T) this;
		}
		
		/**
		 * Set {@link #scanSubPackages} to true.
		 *
		 * @return {@link T}.
		 */
		public T deepScan() {
			this.scanSubPackages = true;
			return (T) this;
		}
		
		/**
		 * @param value The value to set {@link #scanSubPackages} to.
		 * @return {@link T}.
		 */
		public T deepScan(boolean value) {
			this.scanSubPackages = value;
			return (T) this;
		}
		
		/**
		 * @return Returns a list of classes found based on the configuration.
		 * @throws IOException If an I/O error occurs.
		 * @throws ClassNotFoundException If a class cannot be found.
		 */
		public abstract List<Class<?>> findClasses() throws IOException, ClassNotFoundException;
	}
	
	/**
	 * A builder for scanning classes in a jar file.
	 */
	public static class JarPackageScanBuilder extends BasePackageScan<JarPackageScanBuilder>{
		
		public JarPackageScanBuilder(File jarFile) {
			super(jarFile);
		}
		
		@Override
		public List<Class<?>> findClasses() throws IOException, ClassNotFoundException {
			List<Class<?>> classes = new ArrayList<>();
			try(JarFile jar = new JarFile(jarFile)){
				Enumeration<JarEntry> entries = jar.entries();
				
				while(entries.hasMoreElements()){
					var entryName = entries.nextElement().getName();
					
					if(!isClassInPackage(entryName)){
						continue;
					}
					
					String className = entryName.replace('/', '.').replace(CLASS_FILE_EXTENSION, "");
					Class<?> clazz = Class.forName(className);
					if(predicate.test(clazz)){
						classes.add(clazz);
					}
				}
			}
			
			return classes;
		}
		
		private boolean isClassInPackage(String entryName) {
			return entryName.endsWith(CLASS_FILE_EXTENSION) &&
				   entryName.startsWith(packageName.replace('.', '/')) &&
				   (scanSubPackages || entryName.lastIndexOf('/') == packageName.length());
		}
	}
	
	/**
	 * A builder for scanning classes in a non jar environment.
	 */
	public static class ResourcePackageScanBuilder extends PackageScanner.BasePackageScan<ResourcePackageScanBuilder>{
		
		public ResourcePackageScanBuilder() {
			super(null);
		}
		
		@Override
		public List<Class<?>> findClasses() throws IOException, ClassNotFoundException {
			List<Class<?>> classes = new ArrayList<>();
			String packagePath = packageName.replace('.', '/');
			Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
			
			while(resources.hasMoreElements()){
				URL resource = resources.nextElement();
				String protocol = resource.getProtocol();
				if("file".equals(protocol)){
					classes.addAll(findClassesInDirectory(resource.getPath(), packageName));
				}
			}
			return classes;
		}
		
		private List<Class<?>> findClassesInDirectory(String directoryPath, String packageName) throws ClassNotFoundException {
			List<Class<?>> classes = new ArrayList<>();
			File directory = new File(directoryPath);
			
			if(directory.exists() && directory.isDirectory()){
				for(File file : Objects.requireNonNull(directory.listFiles())){
					if(file.isDirectory() && scanSubPackages){
						classes.addAll(findClassesInDirectory(file.getAbsolutePath(), packageName + "." + file.getName()));
					} else if(file.getName().endsWith(CLASS_FILE_EXTENSION)){
						String className = packageName + '.' + file.getName().replace(CLASS_FILE_EXTENSION, "");
						Class<?> foundClass = Class.forName(className);
						if(predicate.test(foundClass)){
							classes.add(foundClass);
						}
					}
				}
			}
			return classes;
		}
	}
	
	/**
	 * Create an instance of a class.
	 *
	 * @param clazz The class to create an instance of
	 * @param args The arguments to pass to the constructor
	 * @param <T> The type of the class
	 * @return The created instance
	 */
	public static <T> T createInstance(Class<T> clazz, Object... args) {
		try{
			return clazz.getDeclaredConstructor().newInstance(args);
		} catch(Exception e){
			throw new RuntimeException("Failed to create instance of class " + clazz.getName(), e);
		}
	}
}

package com.wonkglorg.util.converter.date;

import static com.wonkglorg.util.converter.date.DateType.DAY;
import static com.wonkglorg.util.converter.date.DateType.HOUR;
import static com.wonkglorg.util.converter.date.DateType.MICRO;
import static com.wonkglorg.util.converter.date.DateType.MILLI;
import static com.wonkglorg.util.converter.date.DateType.MINUTE;
import static com.wonkglorg.util.converter.date.DateType.MONTH;
import static com.wonkglorg.util.converter.date.DateType.NANO;
import static com.wonkglorg.util.converter.date.DateType.SECOND;
import static com.wonkglorg.util.converter.date.DateType.WEEK;
import static com.wonkglorg.util.converter.date.DateType.YEAR;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DurationFormatter{
	/**
	 * Caches tokenized results that have been requested before
	 */
	private static final Map<String, DurationFormatter> CACHE = new ConcurrentHashMap<>();
	private final List<Token> tokens;
	
	private DurationFormatter(List<Token> tokens) {
		this.tokens = List.copyOf(tokens);
	}
	
	public static DurationFormatter ofPattern(String pattern) {
		if(pattern == null || pattern.isEmpty()){
			throw new IllegalArgumentException("Pattern cannot be null or empty");
		}
		
		return CACHE.computeIfAbsent(pattern, p -> new DurationFormatter(tokenize(p)));
	}
	
	public String format(Duration duration) {
		return format(duration.getSeconds(), duration.getNano());
	}
	
	public String format(long seconds, int nanos) {
		
		StringBuilder out = new StringBuilder();
		
		long remainingSeconds = seconds;
		int remainingNanos = nanos;
		
		for(Token token : tokens){
			
			if(token instanceof LiteralToken literal){
				out.append(literal.text());
				continue;
			}
			
			FieldToken field = (FieldToken) token;
			DateType type = field.type();
			
			long value;
			
			if(type.typeStoredInNanos()){
				int unit = type.getNanoseconds();
				value = remainingNanos / unit;
				remainingNanos %= unit;
			} else {
				long unit = type.getSeconds();
				value = remainingSeconds / unit;
				remainingSeconds %= unit;
			}
			
			out.append(String.format("%0" + field.width() + "d", value));
		}
		
		return out.toString();
	}
	
	private static List<Token> tokenize(String pattern) {
		
		if(pattern == null || pattern.isEmpty()){
			throw new IllegalArgumentException("Pattern cannot be null or empty");
		}
		
		List<Token> tokens = new ArrayList<>();
		StringBuilder literal = new StringBuilder();
		
		boolean quoted = false;
		
		for(int i = 0; i < pattern.length(); ){
			
			char c = pattern.charAt(i);
			
			// Start/end quoted literal
			if(c == '\''){
				
				if(i + 1 < pattern.length() && pattern.charAt(i + 1) == '\''){
					literal.append('\'');
					i += 2;
					continue;
				}
				
				quoted = !quoted;
				i++;
				continue;
			}
			
			if(quoted){
				literal.append(c);
				i++;
				continue;
			}
			
			DateType type = fromPattern(c);
			
			if(type == null){
				literal.append(c);
				i++;
				continue;
			}
			
			if(!literal.isEmpty()){
				tokens.add(new LiteralToken(literal.toString()));
				literal.setLength(0);
			}
			
			int width = 1;
			i++;
			
			while(i < pattern.length() && pattern.charAt(i) == c){
				width++;
				i++;
			}
			
			tokens.add(new FieldToken(type, width));
		}
		
		if(quoted){
			throw new IllegalArgumentException("Unterminated quoted literal in pattern");
		}
		
		if(!literal.isEmpty()){
			tokens.add(new LiteralToken(literal.toString()));
		}
		
		return tokens;
	}
	
	private sealed interface Token permits LiteralToken, FieldToken{}
	
	private record LiteralToken(String text) implements Token{}
	
	private record FieldToken(DateType type, int width) implements Token{}
	
	public static DateType fromPattern(char c) {
		return switch(c) {
			case 'Y' -> YEAR;
			case 'M' -> MONTH;
			case 'W' -> WEEK;
			case 'd' -> DAY;
			case 'H' -> HOUR;
			case 'm' -> MINUTE;
			case 's' -> SECOND;
			case 'S' -> MILLI;
			case 'u' -> MICRO;
			case 'n' -> NANO;
			default -> null;
		};
	}
}
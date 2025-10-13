package com.coffeennec.strings;

public class FennecString {

	
    /**
     * Formats the given template string by replacing placeholders with the provided arguments.
     * The placeholders are represented by "{}".
     *
     * @param template The template string containing placeholders.
     * @param args     The arguments to replace the placeholders in the template.
     * @throws IllegalArgumentException if there are not enough arguments provided for the placeholders.
     */
	public static void printf(String template, Object...args) {
		System.out.printf(format(template, args));
	}
	
    /**
     * Formats the given template string by replacing placeholders with the provided arguments
     * and outputs it to the standard error stream.
     * The placeholders are represented by "{}".
     *
     * @param template The template string containing placeholders.
     * @param args     The arguments to replace the placeholders in the template.
     * @throws IllegalArgumentException if there are not enough arguments provided for the placeholders.
     */
	public static void eprintf(String template, Object...args) {
		System.err.printf(format(template, args));
	}

    /**
     * Replaces placeholders in the template string with the provided arguments.
     * Placeholders are denoted by "{}". 
     * 
     * "\\" is used to escape braces, for example:
     * - "{\\}" will be replaced by "{}".
     * - "{\\\\}" will be replaced by "{\}".
     *
     * @param template The template string containing placeholders.
     * @param args     The arguments to replace the placeholders in the template.
     * @return The formatted string with placeholders replaced by the corresponding arguments.
     * @throws IllegalArgumentException if there are not enough arguments provided for the placeholders.
     */
	public static String format(String template, Object...args) {
		if (template == null) return null;
			
		StringBuilder text = new StringBuilder(template);
		int index = 0;
		
		int placeHolderIndex = text.indexOf("{}");
		
		while (placeHolderIndex != -1) {
			
			if (index >= args.length) {
				throw new IllegalArgumentException("Not enough arguments provided for the template");
			}
				
			text.replace(placeHolderIndex, placeHolderIndex + 2, args[index].toString());
			placeHolderIndex = text.indexOf("{}");
			index++;
		}
		
		String result = text.toString().replaceAll("\\{(\\\\)(\\\\*)}", "\\{$2}");
		
		return result;
	}
	
}

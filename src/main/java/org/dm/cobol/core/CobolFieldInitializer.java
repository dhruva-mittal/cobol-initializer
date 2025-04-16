package org.dm.cobol.core;

import org.dm.cobol.annotation.CobolField;
import org.dm.cobol.annotation.CobolNestedObject;
import org.dm.cobol.enums.CobolFieldType;
import org.dm.cobol.exception.CobolParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for initializing and parsing COBOL-annotated Java objects.
 */
public class CobolFieldInitializer {

    /**
     * Initialize an object's fields based on COBOL data type annotations.
     * Fields are set to appropriate default values based on their COBOL type.
     *
     * @param obj The object to initialize
     * @throws IllegalAccessException If a field cannot be accessed
     */
    public static void initialize(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            
            if (field.isAnnotationPresent(CobolField.class)) {
                CobolField cobolField = field.getAnnotation(CobolField.class);
                field.set(obj, getDefaultValue(cobolField));
            } 
            else if (field.isAnnotationPresent(CobolNestedObject.class)) {
                // Initialize nested objects recursively
                Object nestedObj = field.get(obj);
                
                // Create instance if null
                if (nestedObj == null) {
                    try {
                        nestedObj = field.getType().getDeclaredConstructor().newInstance();
                        field.set(obj, nestedObj);
                    } catch (InstantiationException | NoSuchMethodException | 
                             InvocationTargetException e) {
                        throw new IllegalStateException("Failed to instantiate nested object", e);
                    }
                }
                
                // Recursively initialize this nested object
                initialize(nestedObj);
            }
        }
    }
    
    /**
     * Parse a string record with nested COBOL objects.
     * Extracts values from the string and sets them in the provided object.
     *
     * @param obj The object to populate
     * @param record The record string to parse
     * @param startPos The starting position in the record
     * @throws IllegalAccessException If a field cannot be accessed
     * @throws CobolParseException If the record cannot be parsed correctly
     */
    public static void parseRecord(Object obj, String record, int startPos)
            throws IllegalAccessException, CobolParseException {
        if (record == null) {
            throw new CobolParseException("Record cannot be null");
        }
        
        List<FieldInfo> fieldInfos = new ArrayList<>();
        int currentPos = startPos;
        
        // Process all fields including nested objects
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            
            if (field.isAnnotationPresent(CobolField.class)) {
                // Handle simple COBOL fields
                CobolField cobolField = field.getAnnotation(CobolField.class);
                FieldInfo info = new FieldInfo(field, cobolField);
                
                int length = cobolField.length();
                info.setStartPos(currentPos);
                info.setEndPos(currentPos + length);
                currentPos += length;
                
                fieldInfos.add(info);
            } 
            else if (field.isAnnotationPresent(CobolNestedObject.class)) {
                // Handle nested objects
                try {
                    // Create instance of nested object if not already created
                    Object nestedObj = field.get(obj);
                    if (nestedObj == null) {
                        nestedObj = field.getType().getDeclaredConstructor().newInstance();
                        field.set(obj, nestedObj);
                    }
                    
                    // Parse the nested object fields recursively
                    int nestedLength = parseNestedObjectRecursively(nestedObj, record, currentPos);
                    currentPos += nestedLength;
                    
                } catch (InstantiationException | NoSuchMethodException | 
                         InvocationTargetException e) {
                    throw new CobolParseException("Failed to instantiate nested object", e);
                }
            }
            // If no annotation, skip this field
        }
        
        // Parse simple fields
        parseWithPositions(obj, record, fieldInfos);
    }
    
    /**
     * Parse a nested object recursively and return its total length.
     *
     * @param nestedObj The nested object to populate
     * @param record The record string to parse
     * @param startPos The starting position in the record
     * @return The total length of the nested object
     * @throws IllegalAccessException If a field cannot be accessed
     * @throws CobolParseException If the record cannot be parsed correctly
     */
    private static int parseNestedObjectRecursively(Object nestedObj, String record, int startPos) 
            throws IllegalAccessException, CobolParseException {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        int currentPos = startPos;
        int totalLength = 0;
        
        // Process all fields in the nested object
        for (Field field : nestedObj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            
            if (field.isAnnotationPresent(CobolField.class)) {
                // Handle simple COBOL fields
                CobolField cobolField = field.getAnnotation(CobolField.class);
                FieldInfo info = new FieldInfo(field, cobolField);
                
                int length = cobolField.length();
                info.setStartPos(currentPos);
                info.setEndPos(currentPos + length);
                currentPos += length;
                totalLength += length;
                
                fieldInfos.add(info);
            } 
            else if (field.isAnnotationPresent(CobolNestedObject.class)) {
                // Handle nested objects recursively
                try {
                    // Create instance of nested object if not already created
                    Object innerNestedObj = field.get(nestedObj);
                    if (innerNestedObj == null) {
                        innerNestedObj = field.getType().getDeclaredConstructor().newInstance();
                        field.set(nestedObj, innerNestedObj);
                    }
                    
                    // Recursively parse this nested object
                    int nestedLength = parseNestedObjectRecursively(innerNestedObj, record, currentPos);
                    currentPos += nestedLength;
                    totalLength += nestedLength;
                    
                } catch (InstantiationException | NoSuchMethodException | 
                         InvocationTargetException e) {
                    throw new CobolParseException("Failed to instantiate nested object", e);
                }
            }
            // If no annotation, skip this field
        }
        
        // Parse fields in this nested object
        parseWithPositions(nestedObj, record, fieldInfos);
        
        return totalLength;
    }
    
    /**
     * Parse using the provided field position information.
     *
     * @param obj The object to populate
     * @param record The record string to parse
     * @param fieldInfos The field information list
     * @throws IllegalAccessException If a field cannot be accessed
     * @throws CobolParseException If the record cannot be parsed correctly
     */
    private static void parseWithPositions(Object obj, String record, List<FieldInfo> fieldInfos) 
            throws IllegalAccessException, CobolParseException {
        for (FieldInfo info : fieldInfos) {
            Field field = info.getField();
            int startPos = info.getStartPos();
            int endPos = info.getEndPos();
            
            if (startPos >= 0 && endPos <= record.length() && startPos < endPos) {
                field.setAccessible(true);
                String value = record.substring(startPos, endPos);
                
                // Type conversion based on field type could be added here
                // For now, just setting the string value
                field.set(obj, convertValueForField(value, field, info.getCobolField()));
            } else {
                throw new CobolParseException(
                    "Invalid position range for field " + field.getName() + 
                    ": [" + startPos + "," + endPos + "] with record length " + record.length());
            }
        }
    }
    
    /**
     * Convert a string value to the appropriate type for a field.
     * Currently handles String fields, but could be extended for other types.
     *
     * @param value The string value from the record
     * @param field The field to set
     * @param cobolField The CobolField annotation
     * @return The converted value
     */
    private static Object convertValueForField(String value, Field field, CobolField cobolField) {
        Class<?> fieldType = field.getType();
        
        // For now, just return the string value
        // This could be extended to handle numeric conversions, etc.
        return value;
    }
    
    /**
     * Calculate field positions based on field order and the starting position.
     *
     * @param clazz The class to analyze
     * @param startingPos The starting position
     * @return A list of field information
     */
    public static List<FieldInfo> calculateFieldPositions(Class<?> clazz, int startingPos) {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        int currentPos = startingPos;
        
        // Process fields in declaration order
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CobolField.class)) {
                CobolField cobolField = field.getAnnotation(CobolField.class);
                FieldInfo info = new FieldInfo(field, cobolField);
                
                int length = cobolField.length();
                info.setStartPos(currentPos);
                info.setEndPos(currentPos + length);
                currentPos += length;
                
                fieldInfos.add(info);
            }
        }
        
        return fieldInfos;
    }

    /**
     * Get default value based on COBOL data type.
     *
     * @param cobolField The CobolField annotation
     * @return The default value as a string
     */
    private static String getDefaultValue(CobolField cobolField) {
        CobolFieldType type = cobolField.type();
        int length = cobolField.length();

        switch (type) {
            case ALPHANUMERIC:
                // Alphanumeric - fill with spaces
                return repeatString(" ", length);
                
            case NUMERIC:
            case SIGNED_NUMERIC:
                // Numeric - fill with zeros
                return repeatString("0", length);
                
            case DECIMAL:
                // Handle decimal points
                int scale = cobolField.scale();
                int integerPart = length - scale;
                if (scale > 0 && integerPart > 0) {
                    return repeatString("0", integerPart) + repeatString("0", scale);
                }
                return repeatString("0", length);
                
            default:
                // Default to spaces if type is unknown
                return repeatString(" ", length);
        }
    }

    /**
     * Create a string by repeating a character.
     *
     * @param str The string to repeat
     * @param count The number of times to repeat
     * @return The repeated string
     */
    private static String repeatString(String str, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }
}
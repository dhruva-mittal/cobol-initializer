package org.dm.cobol.core;

import org.dm.cobol.annotation.CobolField;

import java.lang.reflect.Field;

/**
 * Helper class to store field information during processing.
 */
public class FieldInfo {
    private final Field field;
    private final CobolField cobolField;
    private int startPos;
    private int endPos;
    
    /**
     * Creates a new FieldInfo instance.
     * 
     * @param field The Java reflection Field object
     * @param cobolField The CobolField annotation
     */
    public FieldInfo(Field field, CobolField cobolField) {
        this.field = field;
        this.cobolField = cobolField;
        this.startPos = -1;
        this.endPos = -1;
    }
    
    /**
     * Gets the Java field.
     * 
     * @return The field
     */
    public Field getField() {
        return field;
    }
    
    /**
     * Gets the CobolField annotation.
     * 
     * @return The annotation
     */
    public CobolField getCobolField() {
        return cobolField;
    }
    
    /**
     * Gets the starting position in the record.
     * 
     * @return The starting position
     */
    public int getStartPos() {
        return startPos;
    }
    
    /**
     * Sets the starting position in the record.
     * 
     * @param startPos The starting position
     */
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }
    
    /**
     * Gets the ending position in the record.
     * 
     * @return The ending position
     */
    public int getEndPos() {
        return endPos;
    }
    
    /**
     * Sets the ending position in the record.
     * 
     * @param endPos The ending position
     */
    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
}
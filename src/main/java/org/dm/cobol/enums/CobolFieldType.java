package org.dm.cobol.enums;

/**
 * Enumeration of supported COBOL field types.
 * Provides standardized types for the CobolField annotation.
 */
public enum CobolFieldType {
    /**
     * Alphanumeric field (PIC X)
     */
    ALPHANUMERIC("PIC X"),
    
    /**
     * Numeric field (PIC 9)
     */
    NUMERIC("PIC 9"),
    
    /**
     * Signed numeric field (PIC S9)
     */
    SIGNED_NUMERIC("PIC S9"),
    
    /**
     * Decimal field - uses length and scale (PIC 9(m).9(n))
     */
    DECIMAL("PIC 9.9");
    
    private final String cobolNotation;
    
    CobolFieldType(String cobolNotation) {
        this.cobolNotation = cobolNotation;
    }
    
    /**
     * Returns the COBOL notation for this field type
     * 
     * @return The COBOL notation string
     */
    public String getCobolNotation() {
        return cobolNotation;
    }
}
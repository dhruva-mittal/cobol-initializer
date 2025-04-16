package org.dm.cobol.core;

import org.dm.cobol.annotation.CobolField;
import org.dm.cobol.annotation.CobolNestedObject;
import org.dm.cobol.enums.CobolFieldType;
import org.dm.cobol.exception.CobolParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CobolFieldInitializerTest {

    @Test
    void initialize_ShouldSetDefaultValues() throws Exception {
        // Arrange
        TestRecord record = new TestRecord();
        
        // Act
        CobolFieldInitializer.initialize(record);
        
        // Assert
        assertEquals("          ", record.id);
        assertEquals("00000", record.count);
        assertNotNull(record.address);
        assertEquals("                    ", record.address.street);
        assertEquals("          ", record.address.city);
    }
    
    @Test
    void parseRecord_ShouldExtractValues() throws Exception {
        // Arrange
        TestRecord record = new TestRecord();
        String testData = "ABC123    12345Main Street         New York  ";
        
        // Act
        CobolFieldInitializer.parseRecord(record, testData, 0);
        
        // Assert
        assertEquals("ABC123    ", record.id);
        assertEquals("12345", record.count);
        assertEquals("Main Street         ", record.address.street);
        assertEquals("New York  ", record.address.city);
    }
    
    @Test
    void parseRecord_WithInvalidData_ShouldThrowException() {
        // Arrange
        TestRecord record = new TestRecord();
        String testData = "ABC123"; // Too short
        
        // Act & Assert
        assertThrows(CobolParseException.class, () -> {
            CobolFieldInitializer.parseRecord(record, testData, 0);
        });
    }
    
    // Test class for the tests
    static class TestRecord {
        @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 10)
        private String id;
        
        @CobolField(type = CobolFieldType.NUMERIC, length = 5)
        private String count;
        
        @CobolNestedObject
        private Address address;
        
        static class Address {
            @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 20)
            private String street;
            
            @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 10)
            private String city;
        }
    }
}
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
    
    @Test
    void write_ShouldCreateFormattedString() throws Exception {
        // Arrange
        TestRecord record = new TestRecord();
        record.id = "ABC123";
        record.count = "42";
        record.address = new TestRecord.Address();
        record.address.street = "Main Street";
        record.address.city = "New York";
        
        // Act
        String result = CobolFieldInitializer.write(record);
        
        // Assert
        String expected = "ABC123    00042Main Street         New York  ";
        assertEquals(expected, result);
    }
    
    @Test
    void writeAndParse_ShouldRoundTrip() throws Exception {
        // Arrange
        TestRecord original = new TestRecord();
        original.id = "XYZ789";
        original.count = "123";
        original.address = new TestRecord.Address();
        original.address.street = "Oak Avenue";
        original.address.city = "Boston";
        
        // Act
        String serialized = CobolFieldInitializer.write(original);
        TestRecord deserialized = new TestRecord();
        CobolFieldInitializer.parseRecord(deserialized, serialized, 0);
        
        // Assert
        assertEquals("XYZ789    ", deserialized.id);
        assertEquals("00123", deserialized.count);
        assertEquals("Oak Avenue          ", deserialized.address.street);
        assertEquals("Boston    ", deserialized.address.city);
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
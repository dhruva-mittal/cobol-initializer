# COBOL Initializer 🚀

Hey there! Welcome to COBOL Initializer — a Java library that makes working with COBOL data in Java way less painful. Annotate your classes, parse records, and chill. 😎

## What’s This?

COBOL Initializer lets you:

- Annotate Java fields to match COBOL data types
- Auto-initialize your Java objects with COBOL-style defaults
- Parse COBOL record strings into Java objects (yep, even nested ones)
- Basically, make COBOL and Java play nice together

## Get It

### Maven

```xml
<dependency>
    <groupId>org.dm.cobol</groupId>
    <artifactId>cobol-initializer</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'org.dm.cobol:cobol-initializer:0.1.0'
```

## How Do I Use It?

### Quick Example

```java
import org.dm.cobol.annotation.CobolField;
import org.dm.cobol.annotation.CobolNestedObject;
import org.dm.cobol.core.CobolFieldInitializer;
import org.dm.cobol.enums.CobolFieldType;

public class CustomerRecord {
    @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 10)
    private String customerId;

    @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 30)
    private String name;

    @CobolField(type = CobolFieldType.NUMERIC, length = 5)
    private String age;

    @CobolNestedObject
    private Address address;

    // Getters and setters

    public static class Address {
        @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 40)
        private String street;

        @CobolField(type = CobolFieldType.ALPHANUMERIC, length = 20)
        private String city;

        // Getters and setters
    }

    public static void main(String[] args) throws Exception {
        CustomerRecord customer = new CustomerRecord();
        CobolFieldInitializer.initialize(customer);

        // Now your fields are set up with COBOL-style defaults:
        // customerId = "          " (10 spaces)
        // name = "                              " (30 spaces)
        // age = "00000" (5 zeros)
        // address is ready too!
    }
}
```

### Parsing a COBOL Record

```java
CustomerRecord customer = new CustomerRecord();
String record = "ABC123    John Doe                     00042123 Main St                             New York            ";
CobolFieldInitializer.parseRecord(customer, record, 0);
```

## Docs?

Want more details? Check out the [Javadoc](https://dhruva-mittal.github.io/cobol-initializer/).

## Wanna Help?

Contributions are always welcome! Check out [CONTRIBUTING.md](CONTRIBUTING.md) for the vibe and how to get started.

## License

MIT, baby! See the [LICENSE](LICENSE) for the boring legal stuff.

---

Thanks for checking this out. Hope it makes your COBOL-Java life easier! ✨
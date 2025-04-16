# COBOL Initializer 🚀

Hey there! Welcome to COBOL Initializer — a Java library that makes working with COBOL data in Java way less painful.
Annotate your classes, parse records, and chill. 😎

## What's This?

COBOL Initializer lets you:

- Annotate Java fields to match COBOL data types
- Auto-initialize your Java objects with COBOL-style defaults
- Parse COBOL record strings into Java objects (including nested objects)
- Write Java objects back to COBOL-formatted strings
- Calculate field positions based on COBOL specifications
- Basically, make COBOL and Java play nice together

## Get It

### Using JitPack

Wanna use the latest and greatest? JitPack has your back!  
Just add JitPack to your repositories and pull in this library.

#### Step 1: Add JitPack to your build

**Maven:**

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**Gradle:**

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

#### Step 2: Add the dependency

Replace `TAG` with the release/tag/commit you want (e.g. `main` or a release tag):

**Maven:**

```xml

<dependency>
    <groupId>com.github.dhruva-mittal</groupId>
    <artifactId>cobol-initializer</artifactId>
    <version>TAG</version>
</dependency>
```

**Gradle:**

```groovy
implementation 'com.github.dhruva-mittal:cobol-initializer:TAG'
```

> **Tip:** For the latest code, use `main` as the version. For a specific release, use the tag name.

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
CustomerRecord customer=new CustomerRecord();
        String record="ABC123    John Doe                     00042123 Main St                             New York            ";
        CobolFieldInitializer.parseRecord(customer,record,0);
```

### Writing a COBOL Record

```java
CustomerRecord customer=new CustomerRecord();
        customer.setCustomerId("ABC123");
        customer.setName("John Doe");
        customer.setAge("42");
// Set address fields...

// Convert the object to a COBOL-formatted string
        String cobolRecord=CobolFieldInitializer.write(customer);
```

### Supported COBOL Field Types

The library supports several COBOL field types:

| Type             | Description                | Default Initialization          |
|------------------|----------------------------|---------------------------------|
| `ALPHANUMERIC`   | Text fields                | Spaces                          |
| `NUMERIC`        | Whole number fields        | Zeros                           |
| `SIGNED_NUMERIC` | Signed whole number fields | Zeros                           |
| `DECIMAL`        | Fields with decimal points | Zeros (including decimal point) |

Example:

```java
// Alphanumeric field (initialized to spaces)
@CobolField(type = CobolFieldType.ALPHANUMERIC, length = 20)
private String customerName;

// Numeric field (initialized to zeros)
@CobolField(type = CobolFieldType.NUMERIC, length = 6)
private String quantity;

// Decimal field with 2 decimal places
@CobolField(type = CobolFieldType.DECIMAL, length = 8, scale = 2)
private String price;  // Format: XXXXX.XX
```

### Working with Decimal Fields

For decimal fields, you can specify the scale:

```java
@CobolField(type = CobolFieldType.DECIMAL, length = 10, scale = 2)
private String amount;  // Will be formatted as XXXXXXX.XX
```

## Docs?

No Javadocs yet — but the code is pretty chill and self-explanatory. If you get stuck, open an issue or peek at the
examples above!

## Wanna Help?

Contributions are always welcome! Check out [CONTRIBUTING.md](CONTRIBUTING.md) for the vibe and how to get started.

## License

MIT, baby! See the [LICENSE](LICENSE) for the boring legal stuff.

---

Thanks for checking this out. Hope it makes your COBOL-Java life easier! ✨
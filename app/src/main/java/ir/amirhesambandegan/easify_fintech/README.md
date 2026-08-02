# Easify Fintech

A collection of validation and formatting tools for Iranian financial and identification data.

## Key Features

- **Bank Card Tools**: Validate 16-digit card numbers (Luhn algorithm), format display, and detect bank names.
- **SHEBA (IBAN) Validation**: Validate Iranian SHEBA numbers and identify the issuing bank.
- **National ID (Code Melli)**: Validate Iranian National ID numbers using the standard checksum algorithm.
- **Mobile Operator Detection**: Identify Iranian mobile operators (MCI, Irancell, Rightel, etc.) from phone numbers.

## Usage

### 1. Bank Card Utilities

```kotlin
val cardNumber = "6037991234567890"

// Validation
if (cardNumber.isValidCardNumber()) {
    // Format for UI: "6037 9912 3456 7890"
    val formatted = cardNumber.formatCardNumber()
    
    // Get Bank Name: "Bank Melli"
    val bankName = cardNumber.getBankName()
}
```

### 2. SHEBA (IBAN) Utilities

```kotlin
val sheba = "IR120170000000123456789012"

if (sheba.isValidSheba()) {
    // Get Bank Name: "Bank Melli"
    val bankName = sheba.getShebaBankName()
}
```

### 3. National ID Validation

```kotlin
val nationalId = "0012345678"

if (nationalId.isValidNationalId()) {
    // ID is valid
}
```

### 4. Mobile Operator Detection

```kotlin
val phone = "09121234567"
val operator = phone.getMobileOperator() // Returns "MCI"
```

## Extension Functions

- `String.isValidCardNumber(): Boolean`
- `String.formatCardNumber(): String`
- `String.getBankName(): String?`
- `String.isValidSheba(): Boolean`
- `String.getShebaBankName(): String?`
- `String.isValidNationalId(): Boolean`
- `String.getMobileOperator(): String?`

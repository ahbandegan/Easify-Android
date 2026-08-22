package ir.amirhesambandegan.easify_form

/**
 * Represents a single validation rule for a form field.
 *
 * @property errorMessage The message to display if the validation fails.
 * @property check A function that takes the field value as input and returns `true` if valid, `false` otherwise.
 */
class ValidationRule(val errorMessage: String, val check: (String) -> Boolean)

/**
 * A collection of common predefined validation rules.
 */
object Validators {
    /**
     * Requires the field to have a non-blank value.
     *
     * @param msg The error message to display if the field is blank.
     */
    fun required(msg: String = "این فیلد الزامی است") = ValidationRule(msg) { it.isNotBlank() }
    
    /**
     * Requires the field value to be a valid email address format.
     *
     * @param msg The error message to display if the email is invalid.
     */
    fun email(msg: String = "ایمیل نامعتبر است") = ValidationRule(msg) { 
        android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() 
    }
    
    /**
     * Requires the field value to have at least a specified number of characters.
     *
     * @param len The minimum required length.
     * @param msg The error message to display if the field length is less than [len].
     */
    fun minLength(len: Int, msg: String = "حداقل $len کاراکتر نیاز است") = ValidationRule(msg) { 
        it.length >= len 
    }
    
    /**
     * Requires the field value to be a valid Iranian mobile phone number format (e.g., 09123456789).
     *
     * @param msg The error message to display if the phone number is invalid.
     */
    fun phoneIr(msg: String = "شماره موبایل نامعتبر است") = ValidationRule(msg) { 
        it.matches(Regex("^09\\d{9}$")) 
    }
    
    /**
     * Requires the field value to match the value of another field in the form.
     * Useful for fields like "Confirm Password".
     *
     * @param otherFieldKey The key of the other field to compare against.
     * @param formState The overall [FormState] containing the other field.
     * @param msg The error message to display if the values do not match.
     */
    fun match(otherFieldKey: String, formState: FormState, msg: String = "عدم تطابق فیلدها") = ValidationRule(msg) { 
        it == formState.getField(otherFieldKey)?.value 
    }
    
    /**
     * Requires the field value to be a strong password, containing at least one uppercase letter,
     * one lowercase letter, and one digit.
     *
     * @param msg The error message to display if the password is not strong enough.
     */
    fun passwordStrength(msg: String = "رمز عبور باید شامل حروف بزرگ، کوچک و عدد باشد") = ValidationRule(msg) {
        it.any { char -> char.isUpperCase() } && 
        it.any { char -> char.isLowerCase() } && 
        it.any { char -> char.isDigit() }
    }
}

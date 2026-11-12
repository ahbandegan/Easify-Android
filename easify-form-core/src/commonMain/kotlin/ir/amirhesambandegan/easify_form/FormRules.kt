package ir.amirhesambandegan.easify_form

/**
 * Lookup interface implemented by both [CoreFormState] and Compose `FormState`
 * so [Validators.match] works identically in Kotlin Console and Compose UI.
 */
interface FormFieldLookup {
    fun getFieldValue(key: String): String?
}

/**
 * Represents a single validation rule for a form field.
 *
 * @property errorMessage The message to display if the validation fails.
 * @property check A predicate returning `true` when valid, `false` otherwise.
 */
class ValidationRule(val errorMessage: String, val check: (String) -> Boolean)

/**
 * Collection of pure multiplatform predefined validation rules.
 */
object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
    private val URL_REGEX = Regex("^(https?://)?([\\w\\-]+\\.)+[a-zA-Z]{2,}(:\\d{1,5})?(/\\S*)?$")
    private val MOBILE_IR_REGEX = Regex("^09\\d{9}$")

    fun required(msg: String = "این فیلد الزامی است") = ValidationRule(msg) { it.isNotBlank() }

    fun email(msg: String = "ایمیل نامعتبر است") = ValidationRule(msg) { input ->
        EMAIL_REGEX.matches(input.trim())
    }

    fun minLength(len: Int, msg: String = "حداقل $len کاراکتر نیاز است") = ValidationRule(msg) {
        it.length >= len
    }

    fun maxLength(len: Int, msg: String = "حداکثر $len کاراکتر مجاز است") = ValidationRule(msg) {
        it.length <= len
    }

    fun exactLength(len: Int, msg: String = "طول باید دقیقاً $len کاراکتر باشد") = ValidationRule(msg) {
        it.length == len
    }

    fun numeric(msg: String = "فقط عدد مجاز است") = ValidationRule(msg) {
        val norm = InputFilters.normalizeDigits(it)
        norm.isNotEmpty() && norm.all { ch -> ch in '0'..'9' }
    }

    fun phoneIr(msg: String = "شماره موبایل نامعتبر است") = ValidationRule(msg) {
        MOBILE_IR_REGEX.matches(InputFilters.normalizeDigits(it.trim()))
    }

    fun url(msg: String = "آدرس اینترنتی نامعتبر است") = ValidationRule(msg) {
        URL_REGEX.matches(it.trim())
    }

    fun pattern(regex: Regex, msg: String) = ValidationRule(msg) { regex.matches(it) }

    fun nationalCodeIr(msg: String = "کد ملی نامعتبر است") = ValidationRule(msg) { input ->
        val clean = InputFilters.normalizeDigits(input.trim())
        if (clean.length != 10 || !clean.all { it in '0'..'9' }) return@ValidationRule false
        if (clean.all { it == clean[0] }) return@ValidationRule false
        val check = clean[9] - '0'
        var sum = 0
        for (i in 0 until 9) {
            sum += (clean[i] - '0') * (10 - i)
        }
        val rem = sum % 11
        if (rem < 2) check == rem else check == 11 - rem
    }

    fun match(
        otherFieldKey: String,
        formState: FormFieldLookup,
        msg: String = "عدم تطابق فیلدها"
    ) = ValidationRule(msg) {
        it == formState.getFieldValue(otherFieldKey)
    }

    fun passwordStrength(msg: String = "رمز عبور باید شامل حروف بزرگ، کوچک و عدد باشد") = ValidationRule(msg) {
        it.any { char -> char.isUpperCase() } &&
            it.any { char -> char.isLowerCase() } &&
            it.any { char -> char.isDigit() }
    }
}

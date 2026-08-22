package ir.amirhesambandegan.easify_form

class ValidationRule(val errorMessage: String, val check: (String) -> Boolean)

object Validators {
    fun required(msg: String = "این فیلد الزامی است") = ValidationRule(msg) { it.isNotBlank() }
    
    fun email(msg: String = "ایمیل نامعتبر است") = ValidationRule(msg) { 
        android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() 
    }
    
    fun minLength(len: Int, msg: String = "حداقل $len کاراکتر نیاز است") = ValidationRule(msg) { 
        it.length >= len 
    }
    
    fun phoneIr(msg: String = "شماره موبایل نامعتبر است") = ValidationRule(msg) { 
        it.matches(Regex("^09\\d{9}$")) 
    }
    
    fun match(otherFieldKey: String, formState: FormState, msg: String = "عدم تطابق فیلدها") = ValidationRule(msg) { 
        it == formState.getField(otherFieldKey)?.value 
    }
    
    fun passwordStrength(msg: String = "رمز عبور باید شامل حروف بزرگ، کوچک و عدد باشد") = ValidationRule(msg) {
        it.any { char -> char.isUpperCase() } && 
        it.any { char -> char.isLowerCase() } && 
        it.any { char -> char.isDigit() }
    }
}

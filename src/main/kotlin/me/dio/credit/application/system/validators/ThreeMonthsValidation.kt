package me.dio.credit.application.system.validators

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate

class ThreeMonthsValidation : ConstraintValidator <ThreeMonths, LocalDate> {
    override fun isValid(value: LocalDate?, context: ConstraintValidatorContext?): Boolean {
        return value!!.isBefore(LocalDate.now().plusMonths(3))
    }
}

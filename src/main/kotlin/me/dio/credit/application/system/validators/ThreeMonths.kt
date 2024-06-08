package me.dio.credit.application.system.validators

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION
)
@Retention(
    AnnotationRetention.RUNTIME
)
@Constraint(validatedBy = [ThreeMonthsValidation::class])
annotation class ThreeMonths(
    val message: String = "dayFirstInstallment pass the max 3 months post the actually date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)



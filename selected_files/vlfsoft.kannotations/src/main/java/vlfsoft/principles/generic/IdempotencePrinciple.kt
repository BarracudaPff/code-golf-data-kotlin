package vlfsoft.principles.generic
import vlfsoft.patterns.OtherPattern
import java.lang.annotation.*
@OtherPattern
@Inherited
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.EXPRESSION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class IdempotencePrinciple
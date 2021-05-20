package com.joom.lightsaber.processor.model
import io.michaelrocks.grip.mirrors.Type
sealed class Converter {
  object Identity : Converter()
  object Instance : Converter()
  data class Adapter(val adapterType: Type.Object) : Converter()
}
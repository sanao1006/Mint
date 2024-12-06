package me.sanao1006.core.network.converter

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed class ApiResponse<S, T> {
  data class Success<S, T>(val data: S) : ApiResponse<S, T>()
  data class Error<S, T>(val error: T) : ApiResponse<S, T>()

  companion object {
    fun <S, T> success(data: S): ApiResponse<S, T> = Success(data)
    fun <S, T> error(error: T): ApiResponse<S, T> = Error(error)
  }
}

class ApiResponseConverterFactory : Converter.Factory {
  override fun suspendResponseConverter(
    typeData: TypeData,
    ktorfit: Ktorfit
  ): Converter.SuspendResponseConverter<HttpResponse, *>? {
    if (typeData.typeInfo.type == ApiResponse::class) {
      return object : Converter.SuspendResponseConverter<HttpResponse, ApiResponse<*, *>> {
        override suspend fun convert(result: KtorfitResult): ApiResponse<*, *> {
          return when (result) {
            is KtorfitResult.Success -> {
              ApiResponse.success<Any, Nothing>(
                result.response.body(typeData.typeInfo)
              )
            }

            is KtorfitResult.Failure -> {
              ApiResponse.error<Nothing, Any>(result.throwable)
            }
          }
        }
      }
    }
    return null
  }
}

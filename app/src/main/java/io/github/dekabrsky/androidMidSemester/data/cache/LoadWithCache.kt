package io.github.dekabrsky.androidMidSemester.data.cache

import kotlinx.coroutines.CancellationException

/**
 * Свежий кэш отдаём сразу. Иначе идём в сеть; если сеть упала,
 * а в кэше есть устаревшие данные — показываем их вместо ошибки.
 */
suspend fun <T> loadWithCache(
    cached: CachedData<T>?,
    load: suspend () -> T,
    save: suspend (T) -> Unit,
): T {
    if (cached != null && cached.isFresh) return cached.data
    return try {
        load().also { save(it) }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        cached?.data ?: throw e
    }
}

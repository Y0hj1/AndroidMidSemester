package io.github.dekabrsky.androidMidSemester.data.cache

/** @param isFresh false — данные устарели, но годятся, если сеть недоступна */
data class CachedData<T>(
    val data: T,
    val isFresh: Boolean,
)

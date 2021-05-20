package io.gladed.watchable
@Suppress("TooManyFunctions")
interface WatchableList<T> : MutableWatchable<MutableList<T>, ListChange<T>>, ReadOnlyWatchableList<T> {
    suspend fun add(element: T): Boolean = invoke { add(element) }
    suspend fun addAll(elements: Iterable<T>): Boolean = invoke { addAll(elements) }
    suspend fun addAll(elements: Array<T>): Boolean = invoke { addAll(elements) }
    suspend fun addAll(elements: Sequence<T>): Boolean = invoke { addAll(elements) }
    suspend fun remove(element: T) = invoke { remove(element) }
    suspend fun removeAll(elements: Iterable<T>): Boolean = invoke { removeAll(elements) }
    suspend fun removeAll(elements: Array<T>): Boolean = invoke { removeAll(elements) }
    suspend fun removeAll(elements: Sequence<T>): Boolean = invoke { removeAll(elements) }
    suspend fun retainAll(elements: Iterable<T>): Boolean = invoke { retainAll(elements) }
    suspend operator fun plusAssign(element: T) { add(element) }
    suspend operator fun plusAssign(elements: Iterable<T>) { addAll(elements) }
    suspend operator fun plusAssign(elements: Array<T>) { addAll(elements) }
    suspend operator fun plusAssign(elements: Sequence<T>) { addAll(elements) }
    suspend operator fun minusAssign(element: T) { remove(element) }
    suspend operator fun minusAssign(elements: Iterable<T>) { removeAll(elements) }
    suspend operator fun minusAssign(elements: Array<T>) { removeAll(elements) }
    suspend operator fun minusAssign(elements: Sequence<T>) { removeAll(elements) }
    override fun readOnly(): ReadOnlyWatchableList<T>
}
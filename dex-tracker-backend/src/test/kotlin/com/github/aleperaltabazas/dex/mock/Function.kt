package com.github.aleperaltabazas.dex.mock

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Replace
import com.github.aleperaltabazas.dex.storage.Update
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever

fun <T> createQueryMock(res: T, execute: Query.(TypeReference<T>?) -> T): Query {
    val query = mock<Query> { on { this.execute(any()) } doReturn res }

    whenever(query.sort(any())).thenReturn(query)
    whenever(query.offset(any())).thenReturn(query)
    whenever(query.limit(any())).thenReturn(query)
    whenever(query.where(any())).thenReturn(query)

    return query
}

fun <T> createUpdateMock(res: T, execute: Update.() -> T): Update {
    val update = mock<Update> { on { this.execute() } doReturn res }

    whenever(update.add(any(), any())).thenReturn(update)
    whenever(update.set(any(), any())).thenReturn(update)
    whenever(update.where(any())).thenReturn(update)

    return update
}

fun <T> createReplaceMock(res: T, execute: Replace.() -> T): Replace {
    val replace = mock<Replace> { on { this.execute() } doReturn res }

    whenever(replace.set(any())).thenReturn(replace)
    whenever(replace.where(any())).thenReturn(replace)

    return replace
}

package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.dto.dex.DexUpdateDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Replace
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.nhaarman.mockito_kotlin.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import org.bson.Document
import org.mockito.ArgumentMatchers.any
import com.nhaarman.mockito_kotlin.any as anyNotNull

class UsersServiceTest : WordSpec() {
    private val storageMock: Storage = mock {}
    private val sessionMock: SessionService = mock {}
    private val idGenMock: IdGenerator = mock {}
    private val userService = UsersService(
        storage = storageMock,
        sessionService = sessionMock,
        idGenerator = idGenMock,
    )

    override fun beforeEach(testCase: TestCase) {
        reset(storageMock, sessionMock, idGenMock)
    }

    init {
        val user = User(
            userId = "123",
            pokedex = emptyList(),
            mail = "test@test.com",
        )


        "updateUser" should {
            "return the user with the changes applied and run an update" {
                val queryMock: Query = mock {}
                val replaceMock: Replace = mock {}

                whenever(queryMock.findOne(any(TypeReference::class.java))).thenReturn(user)

                whenever(queryMock.where(anyNotNull())).thenReturn(queryMock)

                whenever(replaceMock.set(anyNotNull())).thenReturn(replaceMock)
                whenever(replaceMock.where(anyNotNull())).thenReturn(replaceMock)

                whenever(storageMock.replace(anyNotNull())).thenReturn(replaceMock)
                whenever(storageMock.query(anyNotNull())).thenReturn(queryMock)

                val expected = user.copy(username = "foo")

                val actual = userService.updateUser("123", UpdateUserDTO(username = "foo"))

                actual shouldBe expected

                verify(storageMock).query(eq(Collection.USERS))
                verify(storageMock).replace(eq(Collection.USERS))

                verify(queryMock).where(eq(Document("user_id", user.userId)))

                verify(replaceMock).set(eq(actual))
                verify(replaceMock).where(eq(Document("user_id", user.userId)))
            }

            "reject the changes if there is at least one dex id which the user does not own" {
                val queryMock: Query = mock {}

                whenever(queryMock.findOne(any(TypeReference::class.java))).thenReturn(user)
                whenever(queryMock.where(anyNotNull())).thenReturn(queryMock)
                whenever(storageMock.query(anyNotNull())).thenReturn(queryMock)

                shouldThrow<ForbiddenException> {
                    userService.updateUser(
                        userId = "123",
                        changes = UpdateUserDTO(
                            username = "foo",
                            dex = mapOf("123" to DexUpdateDTO(caught = emptyList())),
                        )
                    )
                }

                verify(storageMock).query(eq(Collection.USERS))
                verify(storageMock, never()).replace(eq(Collection.USERS))

                verify(queryMock).where(eq(Document("user_id", user.userId)))
            }
        }
    }
}

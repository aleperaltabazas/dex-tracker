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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.bson.Document

class UsersServiceTest : WordSpec() {
    private val storageMock: Storage = mockk()
    private val sessionMock: SessionService = mockk()
    private val idGenMock: IdGenerator = mockk()
    private val userService = UsersService(
        storage = storageMock,
        sessionService = sessionMock,
        idGenerator = idGenMock,
    )

    override suspend fun beforeEach(testCase: TestCase) {
        clearMocks(storageMock, sessionMock, idGenMock)
    }

    init {
        val user = User(
            userId = "123",
            pokedex = emptyList(),
            mail = "test@test.com",
        )

        "updateUser" should {
            "return the user with the changes applied and run an update" {
                val queryMock: Query = mockk()
                val replaceMock: Replace = mockk()

                every { queryMock.findOne(any<TypeReference<User>>()) } returns user
                every { queryMock.where(any()) } returns queryMock
                every { replaceMock.set(any()) } returns replaceMock
                every { replaceMock.where(any()) } returns replaceMock
                every { replaceMock.replaceOne() } returns null
                every { storageMock.replace(any()) } returns replaceMock
                every { storageMock.query(any()) } returns queryMock

                val expected = user.copy(username = "foo")
                val actual = userService.updateUser("123", UpdateUserDTO(username = "foo"))

                actual shouldBe expected

                verify { storageMock.query(Collection.USERS) }
                verify { storageMock.replace(Collection.USERS) }
                verify { queryMock.where(Document("user_id", user.userId)) }
                verify { replaceMock.set(expected) }
                verify { replaceMock.where(Document("user_id", user.userId)) }
            }

            "reject the changes if there is at least one dex id which the user does not own" {
                val queryMock: Query = mockk()

                every { queryMock.findOne(any<TypeReference<User>>()) } returns user
                every { queryMock.where(any()) } returns queryMock
                every { storageMock.query(any()) } returns queryMock

                shouldThrow<ForbiddenException> {
                    userService.updateUser(
                        userId = "123",
                        changes = UpdateUserDTO(
                            username = "foo",
                            dex = mapOf("123" to DexUpdateDTO(caught = emptyList())),
                        )
                    )
                }

                verify { storageMock.query(Collection.USERS) }
                verify(exactly = 0) { storageMock.replace(Collection.USERS) }
                verify { queryMock.where(Document("user_id", user.userId)) }
            }
        }
    }
}

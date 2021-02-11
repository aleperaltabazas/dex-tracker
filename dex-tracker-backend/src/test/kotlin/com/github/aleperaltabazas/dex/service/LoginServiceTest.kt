package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.storage.Update
import com.nhaarman.mockito_kotlin.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.bson.Document

class LoginServiceTest : WordSpec() {
    init {
        val usersServiceMock: UsersService = mock {}
        val storageMock: Storage = mock {}
        val loginService = LoginService(
            usersService = usersServiceMock,
            storage = storageMock,
        )

        "login" should {
            val user = User(
                userId = "123",
                pokedex = emptyList(),
                mail = "test@test.com"
            )

            "return the user by mail if it exists" {
                checkAll<String> { mail ->
                    reset(storageMock, usersServiceMock)
                    whenever(usersServiceMock.findUserByMail(any())).thenReturn(user)

                    val expected = user
                    val actual = loginService.login(LoginRequestDTO(mail), "123")

                    actual shouldBe expected

                    verify(usersServiceMock).findUserByMail(eq(mail))
                    verifyZeroInteractions(storageMock)
                    verify(usersServiceMock, never()).createUser(any(), eq(mail))
                }
            }

            "merge the user with the new mail if it does not exist for said mail, but has a session token" {
                checkAll<String> { dexToken ->
                    reset(storageMock, usersServiceMock)
                    whenever(usersServiceMock.findUserByMail(any())).thenReturn(null)
                    whenever(usersServiceMock.findUserByToken(any())).thenReturn(user)

                    val updateMock: Update = mock {}
                    whenever(updateMock.where(any())).thenReturn(updateMock)
                    whenever(updateMock.set(any(), any())).thenReturn(updateMock)
                    whenever(updateMock.add(any(), any())).thenReturn(updateMock)
                    whenever(storageMock.update(any())).thenReturn(updateMock)

                    val expected = user

                    val actual = loginService.login(LoginRequestDTO("test@test.com"), dexToken)

                    actual shouldBe expected

                    verify(usersServiceMock).findUserByMail(eq("test@test.com"))
                    verify(usersServiceMock).findUserByToken(eq(dexToken))
                    verify(updateMock).where(Document("user_id", user.userId))
                    verify(updateMock).set("mail", "test@test.com")
                    verify(updateMock).updateOne()
                    verify(storageMock).update(eq(Collection.USERS))
                }
            }

            "create a new user if token is null" {
                checkAll<String> { mail ->
                    reset(storageMock, usersServiceMock)
                    whenever(usersServiceMock.findUserByMail(any())).thenReturn(null)
                    whenever(usersServiceMock.createUser(anyOrNull(), anyOrNull())).thenReturn(user)

                    val expected = user

                    val actual = loginService.login(LoginRequestDTO(mail), null)

                    actual shouldBe expected

                    verify(usersServiceMock).findUserByMail(eq(mail))
                    verify(usersServiceMock, never()).findUserByToken(any())
                    verify(usersServiceMock).createUser(eq(null), eq(mail))
                    verifyZeroInteractions(storageMock)
                }
            }
        }
    }
}
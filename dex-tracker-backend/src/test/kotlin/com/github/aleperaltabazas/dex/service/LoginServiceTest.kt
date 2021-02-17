package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.nhaarman.mockito_kotlin.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class LoginServiceTest : WordSpec() {
    init {
        val usersServiceMock: UsersService = mock {}
        val sessionServiceMock: SessionService = mock {}
        val loginService = LoginService(
            usersService = usersServiceMock,
            sessionService = sessionServiceMock
        )

        "loginFromToken" should {
            val user = User(
                userId = "123",
                pokedex = emptyList(),
                mail = "test@test.com"
            )

            "try to find the user from the given token" {
                checkAll<String> { token ->
                    reset(usersServiceMock)
                    whenever(usersServiceMock.unsafeFindUserByToken(any())).thenReturn(user)

                    val expected = user
                    val actual = loginService.loginFromToken(dexToken = token)

                    actual shouldBe expected

                    verify(usersServiceMock).unsafeFindUserByToken(eq(token))
                }
            }
        }

        "loginFromMail" should {
            val user = User(
                userId = "123",
                pokedex = emptyList(),
                mail = "test@test.com"
            )

            val login = LoginRequestDTO(
                mail = "test@test.com",
                localDex = emptyList(),
                googleToken = "123",
            )

            "return the user with the given email" {
                checkAll<String> { mail ->
                    reset(usersServiceMock)
                    whenever(usersServiceMock.findUserByMail(any())).thenReturn(user)

                    val expected = user
                    val actual = loginService.loginFromMail(login.copy(mail = mail))

                    actual shouldBe expected

                    actual shouldBe expected

                    verify(usersServiceMock).findUserByMail(eq(mail))
                    verify(usersServiceMock).updateUser(eq(expected.copy(pokedex = expected.pokedex + login.localDex.map { UserDex(it) })))
                }
            }

            "merge the user if none is found for the given email" {
                checkAll<String> { mail ->
                    reset(usersServiceMock)
                    whenever(usersServiceMock.findUserByMail(any())).thenReturn(null)
                    whenever(usersServiceMock.createUser(any(), any())).thenReturn(user)

                    val expected = user
                    val actual = loginService.loginFromMail(login.copy(mail = mail))

                    actual shouldBe expected

                    verify(usersServiceMock).findUserByMail(eq(mail))
                    verify(usersServiceMock).createUser(eq(mail), eq(user.pokedex))
                }
            }
        }
    }
}
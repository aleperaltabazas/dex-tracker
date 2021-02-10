package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.CreateUserDexDTO
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.mock.createQueryMock
import com.github.aleperaltabazas.dex.mock.createUpdateMock
import com.github.aleperaltabazas.dex.model.*
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.storage.Update
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.nhaarman.mockito_kotlin.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.bson.Document

class UsersServiceTest : StringSpec() {
    private val storageMock: Storage = mock {}
    private val pokemonMock: PokemonService = mock {}
    private val idGeneratorMock: IdGenerator = mock {}
    private val hashMock: HashHelper = mock {}
    private val usersService = UsersService(
        storage = storageMock,
        idGenerator = idGeneratorMock,
        hash = hashMock,
        pokemonService = pokemonMock,
    )
    private val user = User(
        userId = "U-123",
        username = null,
        pokedex = emptyList(),
    )
    private val session = Session(token = "123", userId = user.userId)

    override fun beforeEach(testCase: TestCase) {
        reset(storageMock, pokemonMock, idGeneratorMock, hashMock)
    }

    init {
        "findUser" should {
            "find one session and then find one user" {
                val sessionsQueryMock = createQueryMock(session, Query::findOne)
                val usersQueryMock = createQueryMock(user, Query::findOne)

                whenever(storageMock.query(eq(Collection.SESSIONS))).thenReturn(sessionsQueryMock)
                whenever(storageMock.query(eq(Collection.USERS))).thenReturn(usersQueryMock)

                val expected = user
                val actual = usersService.unsafeFindUserByToken("123")

                actual shouldBe expected

                verify(sessionsQueryMock).where(eq(Document("token", "123")))
                verify(usersQueryMock).where(eq(Document("user_id", user.userId)))
            }

            "throw a NotFoundException if no session is found" {
                val sessionsQueryMock = createQueryMock(null, Query::findOne)

                whenever(storageMock.query(eq(Collection.SESSIONS))).thenReturn(sessionsQueryMock)

                shouldThrow<NotFoundException> {
                    usersService.unsafeFindUserByToken("123")
                }

                verify(sessionsQueryMock).where(eq(Document("token", "123")))
            }

            "throw a NotFoundException if no user is found" {
                val sessionsQueryMock = createQueryMock(session, Query::findOne)
                val usersQueryMock = createQueryMock(null, Query::findOne)

                whenever(storageMock.query(eq(Collection.SESSIONS))).thenReturn(sessionsQueryMock)
                whenever(storageMock.query(eq(Collection.USERS))).thenReturn(usersQueryMock)

                shouldThrow<NotFoundException> {
                    usersService.unsafeFindUserByToken("123")

                }

                verify(sessionsQueryMock).where(eq(Document("token", "123")))
                verify(usersQueryMock).where(eq(Document("user_id", user.userId)))
            }
        }

        "createUserDex" should {
            "fetch the user, get the national pokedex and run un update on the users collection" {
                mockFindUser()
                val updateMock = createUpdateMock(null, Update::updateOne)
                whenever(storageMock.update(eq(Collection.USERS))).thenReturn(updateMock)

                whenever(idGeneratorMock.userDexId()).thenReturn("UD-123")
                whenever(pokemonMock.gameNationalPokedex(any())).thenReturn(
                    GamePokedexDTO(
                        pokemon = listOf(
                            DexEntryDTO(
                                name = "bulbasaur",
                                number = 1,
                            ),
                            DexEntryDTO(
                                name = "ivysaur",
                                number = 2,
                            ),
                            DexEntryDTO(
                                name = "venusaur",
                                number = 3,
                            ),
                        ),
                        region = "johto",
                        type = PokedexType.NATIONAL,
                        game = GameDTO(
                            title = "hgss",
                            fullTitle = "HeartGold and SoulSilver",
                            spritePokemon = "ho-oh"
                        )
                    )
                )

                val expected = UserDex(
                    userDexId = "UD-123",
                    game = "hgss",
                    type = PokedexType.NATIONAL,
                    pokemon = listOf(
                        UserDexPokemon(
                            name = "bulbasaur",
                            dexNumber = 1,
                            caught = false,
                        ),
                        UserDexPokemon(
                            name = "ivysaur",
                            dexNumber = 2,
                            caught = false,
                        ),
                        UserDexPokemon(
                            name = "venusaur",
                            dexNumber = 3,
                            caught = false,
                        ),
                    ),
                    region = "johto",
                )

                val actual = usersService.createUserDex(
                    token = "123",
                    CreateUserDexDTO(
                        game = "hgss",
                        type = PokedexType.NATIONAL,
                        name = null,
                    )
                )

                actual shouldBe expected

                verify(updateMock).add("pokedex", expected)
                verify(updateMock).where(Document("user_id", user.userId))
            }
        }
    }

    private fun mockFindUser(user: User = this.user, session: Session = this.session): Query {
        val sessionQuery = createQueryMock(session, Query::findOne)
        val usersQuery = createQueryMock(user, Query::findOne)

        whenever(storageMock.query(eq(Collection.SESSIONS))).thenReturn(sessionQuery)
        whenever(storageMock.query(eq(Collection.USERS))).thenReturn(usersQuery)

        return usersQuery
    }
}
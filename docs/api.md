## API

### Pokedex

GET `/api/v1/pokedex/

```json
[
    {
        "name": "rby-national",
        "displayName": "Red, Blue and Yellow - National",
        "region": "kanto",
        "gen": 1,
        "type": "NATIONAL",
        "entries": [
            {
                "name": "bulbasaur",
                "number": 1
            }
        ]
    }
]
```

### Users

GET `/api/v1/users/:userId`

```json
{
    "userId": "U-123",
    "mail": "test@test.com",
    "pokedex": [
        {
            "userDexId": "UD-123",
            "game": {
                "name": "frlg-national",
                "displayName": "FireRed and LeafGreen - National"
            },
            "type": "NATIONAL",
            "region": "kanto",
            "pokemon": [
                {
                    "name": "bulbasaur",
                    "dexNumber": 1,
                    "caught": true
                }
            ],
            "caught": 1
        }
    ]
}
```

GET `/api/v1/users/:userId/pokedex/:dexId`

```json
{
    "userDexId": "UD-123",
    "game": {
        "name": "frlg-national",
        "displayName": "FireRed and LeafGreen - National"
    },
    "type": "NATIONAL",
    "region": "kanto",
    "pokemon": [
        {
            "name": "bulbasaur",
            "dexNumber": 1,
            "caught": true
        }
    ],
    "caught": 1
}
```

POST `/api/v1/users/:userId/pokedex`

Request body:
```json
{
    "game": "frlg-national",
    "name": "string or null"
}
```

Requires cookie `dex-token` (generated from login) to be sent.

Response:
```json
{
    "userDexId": "UD-123",
    "game": {
        "name": "frlg-national",
        "displayName": "FireRed and LeafGreen - National"
    },
    "type": "NATIONAL",
    "region": "kanto",
    "pokemon": [
        {
            "name": "bulbasaur",
            "dexNumber": 1,
            "caught": true
        }
    ],
    "caught": 1
}
```

PATCH `/api/v1/users/:userId`

Request body:
```json
{
    "username": "string or null",
    "dex": {
        "UD-123": {
            "name": "blah",
            "caught": [
                1
            ]
        },
        "UD-456": {
            "caught": [
                1,
                3
            ]
        }
    }
}
```

Requires cookie `dex-token` (generated from login) to be sent.

Response:
```json
[
    {
        "userId": "U-123",
        "mail": "test@test.com",
        "pokedex": [
            {
                "userDexId": "UD-123",
                "name": "blah",
                "game": {
                    "name": "frlg-national",
                    "displayName": "FireRed and LeafGreen - National"
                },
                "type": "NATIONAL",
                "region": "kanto",
                "pokemon": [
                    {
                        "name": "bulbasaur",
                        "dexNumber": 1,
                        "caught": true
                    }
                ],
                "caught": 1
            },
            {
                "userDexId": "UD-456",
                "name": "blah",
                "game": {
                    "name": "gsc-national",
                    "displayName": "Gold, Silver and Crystal - National"
                },
                "type": "NATIONAL",
                "region": "kanto",
                "pokemon": [
                    {
                        "name": "bulbasaur",
                        "dexNumber": 1,
                        "caught": true
                    },
                    {
                        "name": "ivysaur",
                        "dexNumber": 2,
                        "caught": false
                    },
                    {
                        "name": "venusaur",
                        "dexNumber": 3,
                        "caught": true
                    }
                ],
                "caught": 3
            }
        ]
    }
]
```

### Login

POST `/api/v1/login`

Requires either `dex-token` to be sent or the following request body:
```json
{
    "mail": "test@gmail.com",
    "googleToken": "123"
}
```

Response:
```json
{
    "userId": "U-123",
    "mail": "test@test.com",
    "pokedex": [
        {
            "userDexId": "UD-123",
            "game": {
                "name": "frlg-national",
                "displayName": "FireRed and LeafGreen - National"
            },
            "type": "NATIONAL",
            "region": "kanto",
            "pokemon": [
                {
                    "name": "bulbasaur",
                    "dexNumber": 1,
                    "caught": true
                }
            ],
            "caught": 1
        }
    ]
}
```

POST `/api/v1/logout`

Requires `dex-token` to be sent.

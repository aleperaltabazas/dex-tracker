package com.github.aleperaltabazas.dex.utils

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

open class FileSystemHelper {
    open fun getHomeDirectory(): String = System.getProperty("user.home")

    open fun createDirectoryIfItDoesNotExist(path: String) {
        val file = File(path)

        when {
            file.exists() && file.isDirectory -> throw IllegalArgumentException("File $path already exists as a file")
            !file.exists() -> file.mkdir()
        }
    }

    open fun readFile(path: String): String = File(path).readText()

    open fun doesFileExist(path: String) = File(path).exists()

    open fun createFile(content: String, path: String) = createFileFromInputStream(
        filePath = path,
        input = content.byteInputStream()
    )

    open fun deleteFile(file: File) = Files.delete(file.toPath())

    private fun createFileFromInputStream(filePath: String, input: InputStream): File {
        val pathToFile = Paths.get(filePath)
        Files.copy(input, pathToFile)

        return pathToFile.toFile()
    }
}
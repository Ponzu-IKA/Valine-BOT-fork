package amaiice.valine_bot

import com.akuleshov7.ktoml.file.TomlFileReader
import com.akuleshov7.ktoml.file.TomlFileWriter
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import java.io.File

class Config {
    companion object {
        const val ConfigFileName = "config.toml"
    }
    @Serializable
    data class Toml(val botData: BotDataTable, val auditChannel: AuditTable)

    @Serializable
    data class BotDataTable(val token: String, val guild: Long)

    @Serializable
    data class AuditTable(val message: MessageTable = MessageTable(), val voice: VoiceTable = VoiceTable())

    @Serializable
    data class MessageTable(val update: Long = 0, val delete: Long = 0, val create: Long = 0)

    @Serializable
    data class VoiceTable(val voiceMessage: Long = 0, val connect: Long = 0, val disconnect: Long = 0)

    fun readConfig(): Toml {
        val isExistConfig = File(ConfigFileName).exists()

        if(!isExistConfig)
            initializeConfig()

        return TomlFileReader.decodeFromFile(serializer(), ConfigFileName)
    }

    private fun initializeConfig() {
        println("${ConfigFileName}が見つかりませんでした。生成します。")
        println("BotのTokenを入力してください")
        val token = readln()
        println("GuildIDを入力してください")
        val guildID = readln().toLong()
        TomlFileWriter().encodeToFile(serializer(), Toml(BotDataTable(token,guildID), AuditTable()), ConfigFileName)
        println("生成しました。")
    }
}
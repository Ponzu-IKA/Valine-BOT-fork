package amaiice.valine_bot

import amaiice.valine_bot.GuildAudit.MessageType.*
import dev.kord.cache.api.getEntry
import dev.kord.common.entity.MessageType
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Embed
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.Channel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.event.message.MessageUpdateEvent
import dev.kord.core.live.live
import dev.kord.core.on
import dev.kord.rest.builder.message.MessageBuilder
import io.github.petertrr.diffutils.diff
import io.github.petertrr.diffutils.diffInline
import java.awt.Color
import kotlin.reflect.KType


class GuildAudit(private val kord: Kord) {
    init {
        receiveMessageActions()
    }
    enum class MessageType(val title: String,val color: Color) {
        DELETE("削除", Color.RED),CREATE("送信", Color.CYAN),UPDATE("編集", Color.YELLOW)
    }
    private fun receiveMessageActions() {
        kord.on<MessageCreateEvent> {
            responseMessage(CREATE, message)
        }

        kord.on<MessageDeleteEvent> {

            responseMessage(DELETE, message!!)
        }

        kord.on<MessageUpdateEvent> {
            responseMessage(UPDATE, message.asMessage(), old!!.asMessage())
        }

    }

    private suspend fun responseMessage(messageType: MessageType, message: Message, oldMessage: Message = message) {
        if(message.author?.isBot == true) return
        val channel = kord.getChannelOf<Channel>(Snowflake(
            when(messageType) {
                DELETE -> config.auditChannel.message.delete
                CREATE -> config.auditChannel.message.create
                UPDATE -> config.auditChannel.message.update
            }
        )) as MessageChannelBehavior

        channel.createEmbed {
            title = "メッセージが${messageType.title}されました！"
            thumbnail {url = message.author?.avatar?.cdnUrl?.toUrl().toString() }
            color = dev.kord.common.Color(messageType.color.rgb)
            description = """
                **ユーザー**: ${message.author?.mention} 
                **ユーザーID**: `${message.author?.id}`
                **メッセージ**: ${getMessageLink(message)}
                **メッセージID**: `${message.id}`
                **作成時刻**: ${message.timestamp.discordTimestamp}
                ${when(messageType) {
                DELETE -> "**削除時刻**: ${message.timestamp.discordTimestamp}\n"
                CREATE -> ""
                UPDATE -> "**編集時刻**: ${message.editedTimestamp?.discordTimestamp}\n"
            }}
            """.trimIndent()
            if(messageType == UPDATE)
                field("**メッセージ差分**", value = {"```diff\n${diffDiscord(message.content, oldMessage.content)}```"})
            if(messageType == DELETE)
                field("**削除されたメッセージ**", value = {message.content})
        }
    }
}
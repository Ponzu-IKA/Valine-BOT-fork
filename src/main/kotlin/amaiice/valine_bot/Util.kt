package amaiice.valine_bot

import dev.kord.core.entity.Message
import io.github.petertrr.diffutils.text.DiffRow
import io.github.petertrr.diffutils.text.DiffRowGenerator
import io.github.petertrr.diffutils.text.DiffTagGenerator
import kotlinx.datetime.Instant

fun getMessageLink(message: Message): String {
    val messageData = message.data
    return "https://discord.com/channels/${config.botData.guild}/${messageData.channelId}/${messageData.id}"
}

val Instant.discordTimestamp:String
    get() {
        return "<t:${this.epochSeconds}:F>"
    }

fun diffDiscord(newMessage: String, oldMessage: String): String {
    class MarkdownTag(val string: String): DiffTagGenerator {
        override fun generateClose(tag: DiffRow.Tag): String = string
        override fun generateOpen(tag: DiffRow.Tag): String = string
    }

    val diffRowGenerator = DiffRowGenerator(showInlineDiffs = true, inlineDiffByWord = true, oldTag = MarkdownTag(""), newTag = MarkdownTag(""))
    val rows = diffRowGenerator.generateDiffRows(oldMessage.split("\n"),newMessage.split("\n"))
    val encoded = rows.map {
        if(it.newLine == it.oldLine)
            return@map "  ${it.newLine}"
        else {
            if(it.newLine.isNotEmpty() && it.oldLine.isNotEmpty())
                return@map "+ ${it.newLine}\n- ${it.oldLine}"
            else if(it.newLine.isNotEmpty())
                return@map "+ ${it.newLine}"
            else
                return@map "- ${it.oldLine}"
        }
    }
    return encoded.joinToString("\n")
}
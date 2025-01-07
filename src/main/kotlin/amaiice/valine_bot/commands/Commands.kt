package amaiice.valine_bot.commands

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on

suspend fun commands(kord: Kord, guild: Long) {
    kord.createGuildChatInputCommand(Snowflake(guild), "ping", "pong!")

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        interaction.deferPublicResponse().respond { content = "pong! in ${kord.gateway.averagePing?.inWholeMilliseconds}ms!" }
    }
}
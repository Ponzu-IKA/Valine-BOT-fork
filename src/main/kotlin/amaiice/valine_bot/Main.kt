package amaiice.valine_bot

import amaiice.valine_bot.commands.commands
import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
import dev.kord.cache.map.lruLinkedHashMap
import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

val config = Config().readConfig()

suspend fun main() {
    val kord = Kord(config.botData.token) {
        cache {
            messages {cache, description ->
                MapEntryCache(cache, description, MapLikeCollection.lruLinkedHashMap(2))
            }
        }
    }

    commands(kord, config.botData.guild)
    GuildAudit(kord)

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
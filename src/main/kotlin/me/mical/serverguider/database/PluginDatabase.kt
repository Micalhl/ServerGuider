package me.mical.serverguider.database

import me.mical.serverguider.ConfigReader
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.severe
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.HostSQL
import taboolib.module.database.Table

/**
 * ServerGuider
 * me.mical.serverguider.database.PluginDatabase
 *
 * @author xiaomu
 * @since 2023/2/14 10:18 PM
 */
object PluginDatabase {

    private val host = HostSQL(ConfigReader.config.getConfigurationSection("database") ?: error("no database section"))

    private val table = Table("serverguider_data", host) {
        add("user") {
            type(ColumnTypeSQL.VARCHAR, 255)
        }
        add("name") {
            type(ColumnTypeSQL.TEXT)
        }
    }

    private val dataSource = host.createDataSource()

    @Awake(LifeCycle.ENABLE)
    fun init() {
        try {
            table.workspace(dataSource) { createTable(true) }.run()
            info("已初始化数据库.")
        } catch (ex: Throwable) {
            severe("加载 数据库 时遇到错误(${ex.message}).")
            if (host.user == "root" && host.password == "password" && host.database == "minecraft") {
                severe("请去配置更改默认数据库配置!")
                return
            }
            ex.printStackTrace()
        }
    }

    fun write(user: String, name: String) {
        table.insert(dataSource, "user", "name") {
            value(user, name)
        }
    }

    fun read(user: String, name: String): Boolean {
        return table.find(dataSource) {
            where {
                "user" eq user
                "name" eq name
            }
        }
    }
}
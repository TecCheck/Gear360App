package io.github.teccheck.gear360app.bluetooth2

class Gear360Configs {

    private val map = HashMap<String, Config>()

    fun setConfigs(configs: List<Config>) {
        for (config in configs)
            setConfig(config)
    }

    fun setConfig(config: Config) {
        map[config.name] = config
    }

    fun getConfig(name: String): Config? {
        return map[name]
    }

    data class Config(
        val name: String,
        val defaultValue: String,
        val values: Array<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Config

            if (name != other.name) return false
            if (defaultValue != other.defaultValue) return false
            if (!values.contentEquals(other.values)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + defaultValue.hashCode()
            result = 31 * result + values.contentHashCode()
            return result
        }

        override fun toString(): String {
            return "Config(name='$name', defaultValue='$defaultValue', values=${values.contentToString()})"
        }
    }
}
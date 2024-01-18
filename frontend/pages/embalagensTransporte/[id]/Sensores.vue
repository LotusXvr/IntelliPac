<template>
    <div v-if="embalagem">
        <h2 v-once>Adicionar Sensor a embalagem - {{ embalagem.id }} {{ embalagem.material }}</h2>

        <form @submit.prevent="addSensor">
            <label for="sensores">Sensores:</label>
            <select v-model="sensorForm.sensorId">
                <option v-for="sensor in filteredSensores" :value="sensor.id">
                    {{ sensor.tipo }}
                </option>
            </select>

            <span v-if="!isFormValid" class="error"> ERRO: Escolha o sensor a inserir</span>
            <br />

            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/embalagensTransporte">Back to Embalagens de transporte</nuxt-link>
    </div>

    <div v-if="embalagem">
        <h2>Sensor Das Embalagens:</h2>
        <ul>
            <li v-for="sensor in embalagem.sensores" :key="sensor.idSensor">
                {{ sensor.idSensor }} - {{ sensor.tipo }} ({{ sensor.unidade }})
                <button @click="removerSensor(sensor.id)">Remover</button>
            </li>
        </ul>
    </div>

    <h2>Error messages:</h2>
    {{ messages }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const messages = ref([])

const sensorForm = reactive({
    sensorId: null,
})

const formFeedback = reactive({
    sensorId: "",
})

const { data: embalagem, refresh: refreshEmbalagem } = await useFetch(
    `${api}/embalagensDeTransporte/${id}`
)

const { data: sensores, refresh: refreshSensor } = await useFetch(`${api}/sensores`)

const isFormValid = computed(() => {
    console.log(sensorForm.sensorId)
    return sensorForm.sensorId !== null
})

const addSensor = async () => {
    try {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(sensorForm.sensorId),
        }

        const response = await fetch(
            `${api}/embalagensDeTransporte/${id}/adicionarSensor`,
            requestOptions
        )
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        refreshEmbalagem()
        refreshSensor()
    } catch (error) {
        messages.value.push(error.message)
    }
}

const filteredSensores = computed(() => {
    if (Array.isArray(sensores.value)) {
        const uniqueSensorTipo = new Set()
        return sensores.value.filter((sensor) => {
            for (const embalagemSensor of embalagem.value.sensores) {
                uniqueSensorTipo.add(embalagemSensor.tipo) //Nao podemos adicionar do mesmo tipo
            }
            if (sensor.estado === 0 && !uniqueSensorTipo.has(sensor.tipo)) {
                uniqueSensorTipo.add(sensor.tipo) //Mostra apenas 1 de cada tipo
                return true
            }
            return false
        })
    } else {
        // Handle the case when sensores.value is not an array
        return []
    }
})

const removerSensor = async (idSensor) => {
    try {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(idSensor),
        }

        const response = await fetch(
            `${api}/embalagensDeTransporte/${id}/removerSensor`,
            requestOptions
        )
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        refreshEmbalagem()
        refreshSensor()
    } catch (error) {
        messages.value.push(error.message)
    }
}
</script>

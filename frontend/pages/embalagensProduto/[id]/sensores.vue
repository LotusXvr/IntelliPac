<template>
    <Navbar />
    <div v-if="embalagem">
        <h2 v-once>Adicionar Tipo Sensor a embalagem - {{ embalagem.id }} {{ embalagem.material }}</h2>

        <form @submit.prevent="addSensor">
            <label for="sensores">Sensores:</label>
            <select v-model="sensorForm.id">
                <option v-for="sensor in filteredSensores" :value="sensor.id">
                    {{ sensor.tipo }}
                </option>
            </select>

            <span v-if="!isFormValid" class="error"> ERRO: Escolha o sensor a inserir</span>
            <br />

            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/embalagensProduto">Back to Tipo de Embalagens</nuxt-link>
    </div>

    <div v-if="embalagem">
        <h2>Sensor Das Embalagens:</h2>
        <ul>
            <li v-for="sensor in embalagem.sensoresDTO" :key="sensor.id">
                {{ sensor.id }} - {{ sensor.tipo }} ({{ sensor.unidade }})
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
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authUser = useAuthStore()

const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const messages = ref([])

const sensorForm = reactive({
    id: null,
})

const formFeedback = reactive({
    id: "",
})

const { data: embalagem, refresh: refreshEmbalagem } = await useFetch(
    `${api}/tipoEmbalagens/${id}`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authUser.token } }
)

const { data: sensores, refresh: refreshSensor } = await useFetch(`${api}/tipoSensor`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authUser.token } })

const isFormValid = computed(() => {
    console.log(sensorForm.id)
    return sensorForm.id !== null
})

const addSensor = async () => {
    try {
        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + authUser.token
            },
            body: JSON.stringify(sensorForm.id),
        }

        const response = await fetch(
            `${api}/tipoEmbalagens/${id}/adicionarSensor`,
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
            for (const embalagemSensor of embalagem.value.sensoresDTO) {
                uniqueSensorTipo.add(embalagemSensor.tipo) //Nao podemos adicionar do mesmo tipo
            }
            if (!uniqueSensorTipo.has(sensor.tipo)) {
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
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + authUser.token
            },
            body: JSON.stringify(idSensor),
        }

        const response = await fetch(
            `${api}/tipoEmbalagens/${id}/removerSensor`,
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

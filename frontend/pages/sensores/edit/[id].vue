<template>
    <Navbar />
    <div v-if="sensor">
        <h2 v-once>Editar sensor - {{ sensor.idSensor }} {{ sensor.tipo }}</h2>

        <form @submit.prevent="updateSensor">
            <label for="tipo">Tipo</label>
            <select id="tipo" v-model="sensorForm.tipo" @change="updateUnidades">
                <option v-for="tipoSensor in tipoSensores" :key="tipoSensor.id">{{ tipoSensor.tipo }}
                </option>
            </select>
            <span class="error">{{ formFeedback.tipo }}</span>
            <br />
            <label for="unidade">Unidade</label>
            <input id="unidade" :disabled="true" v-model="sensorForm.unidade" />
            <span class="error">{{ formFeedback.unidade }}</span>
            <br />
            <br />
            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/sensores">Voltar aos sensores</nuxt-link>
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
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
import { useAuthStore } from "~/store/auth-store"

const authStore = useAuthStore()
const sensor = ref(null)
const messages = ref([])

const sensorForm = reactive({
    idSensor: null,
    tipo: null,
    unidade: null
})

const formFeedback = reactive({
    tipo: "",
    unidade: ""
})

const fetchSensor = async () => {
    try {
        const response = await fetch(`${api}/sensores/${id}`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authStore.token } })
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        sensor.value = await response.json()
        sensorForm.idSensor = sensor.value.idSensor
        sensorForm.tipo = sensor.value.tipo
        sensorForm.unidade = sensor.value.unidade
        sensorForm.valor = sensor.value.valor
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isFormValid = computed(() => {
    return true
})

const updateSensor = async () => {
    try {
        const requestOptions = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + authStore.token
            },
            body: JSON.stringify(sensorForm),
        }

        const response = await fetch(`${api}/sensores/${id}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/sensores")
    } catch (error) {
        messages.value.push(error.message)
    }
}

const { data: tipoSensores } = await useFetch(`${api}/tipoSensor`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authStore.token } })

const updateUnidades = () => {
    if (Array.isArray(tipoSensores.value)) {
        const sensorEncontrado = tipoSensores.value.find(sensor => sensor.tipo == sensorForm.tipo)
        sensorForm.unidade = sensorEncontrado.unidade
    }
}

fetchSensor()
</script>

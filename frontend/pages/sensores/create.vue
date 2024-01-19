<template>
    <form @submit.prevent="create">
        <label for="tipo">Tipo</label>
        <select id="tipo" v-model="sensorForm.tipo" @change="updateUnidades">
            <option v-for="tipoSensor in tipoSensores" :key="tipoSensor.id">{{ tipoSensor.tipo }}</option>
        </select>
        <br />
        <label for="unidade">Unidade</label>
        <input id="unidade" :disabled="true" v-model="sensorForm.unidade" />
        <span class="error">{{ formFeedback.unidade }}</span>
        <br />
        <span class="error">{{ formFeedback.valor }}</span>
        <br />
        <button type="submit" :disabled="!isFormValid">Criar sensor</button>
    </form>
    {{ message }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import { ref, reactive, computed } from "vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const sensorForm = reactive({
    tipo: null,
    unidade: null,
})

const formFeedback = reactive({
    tipo: "",
    unidade: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref("")

const isFormValid = computed(() => {
    return true
})

const tipos = ["Temperatura", "Humidade", "Luminosidade", "Pressão"]

async function create() {
    const requestOptions = {
        method: "POST",
        headers: { 
            "Content-Type": "application/json",
            'Authorization': 'Bearer ' + authStore.token
        },
        body: JSON.stringify(sensorForm),
    }

    const { error } = await useFetch(`${api}/sensores`, requestOptions)
    if (!error.value) navigateTo("/sensores")
    message.value = error.value
}

const { data: tipoSensores } = await useFetch(`${api}/tipoSensor`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})

const updateUnidades = () =>{
    if (Array.isArray(tipoSensores.value)) {
        const sensorEncontrado = tipoSensores.value.find(sensor => sensor.tipo == sensorForm.tipo)
        sensorForm.unidade = sensorEncontrado.unidade
    }
}
</script>

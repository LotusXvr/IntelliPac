<template>
    <form @submit.prevent="create">
        <label for="idSensor">id Sensor</label>
        <input id="idSensor" v-model="sensorForm.idSensor" />
        <span class="error">{{ formFeedback.idSensor }}</span>
        <br />
        <label for="tipo">Tipo</label>
        <select id="tipo" v-model="sensorForm.tipo">
            <option v-for="tipo in tipos" :key="tipo" :value="tipo">{{ tipo }}</option>
        </select>
        <span class="error">{{ tipo }}</span>
        <br />
        <label for="unidade">Unidade</label>
        <input id="unidade" v-model="sensorForm.unidade" />
        <span class="error">{{ formFeedback.unidade }}</span>
        <br />
        <label for="valor">Valor</label>
        <input id="valor" v-model="sensorForm.valor" />
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
const sensorForm = reactive({
    idSensor: null,
    tipo: null,
    unidade: null,
    valor: null,
})

const formFeedback = reactive({
    idSensor: "",
    tipo: "",
    unidade: "",
    valor: "",
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
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(sensorForm),
    }

    const { error } = await useFetch(`${api}/sensores`, requestOptions)
    if (!error.value) navigateTo("/sensores")
    message.value = error.value
}
</script>

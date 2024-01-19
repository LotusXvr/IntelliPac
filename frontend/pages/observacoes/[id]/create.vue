<template>
    <Navbar />
    <div v-if="sensor">
        <form @submit.prevent="create">
            <label for="valor">Valor: </label>
            <input id="valor" v-model="observacaoForm.valor" />
            <span v-if="!isValorValid" class="error"> ERRO: {{ formFeedback.valor }}</span>
            <br />

            <div>
                <p>Sensor: ({{ sensor.idSensor }}) {{ sensor.tipo }} - {{ sensor.unidade }}</p>
            </div>
            {{ message }}
            <br />
            <button type="submit" :disabled="!isFormValid">Criar observação</button>
        </form>
    </div>
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
import { ref, reactive, computed } from "vue"
const observacaoForm = reactive({
    sensorId: null,
    valor: null,
})

const formFeedback = reactive({
    valor: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref([])

const route = useRoute()
const id = route.params.id
const sensor = ref(null)

const fetchSensor = async () => {
    try {
        const { data: response } = await useFetch(`${api}/sensores/${id}`, {
            method: "GET",
            headers: { Authorization: "Bearer " + authStore.token },
        })
        if (!response) {
            console.log(response)
            throw new Error(response.statusText)
        }
        console.log(response)
        sensor.value = response.value
        observacaoForm.sensorId = sensor.value.id
        console.log(sensor.value)
    } catch (error) {
        message.value.push(error.message)
    }
}

fetchSensor()

const isValorValid = computed(() => {
    if (observacaoForm.valor === null) {
        formFeedback.valor = "A observação deve ter um valor"
        return false
    }
    if (observacaoForm.valor.length < 1) {
        formFeedback.valor = "A observação deve ter pelo menos 1 caracteres"
        return false
    }
    if (observacaoForm.valor.length > 5) {
        formFeedback.valor = "A observação deve ter no máximo 5 caracteres"
        return false
    }
    formFeedback.valor = ""
    return true
})

const isFormValid = computed(() => {
    return isValorValid.value
})

async function create() {
    console.log(observacaoForm)

    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: "Bearer " + authStore.token },
        body: JSON.stringify(observacaoForm),
    }

    const { error } = await useFetch(`${api}/observacoes`, requestOptions)
    if (!error.value) navigateTo("/sensores")
    message.value = error.value
}
</script>

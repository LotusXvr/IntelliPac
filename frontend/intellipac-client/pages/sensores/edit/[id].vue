<template>
    <div v-if="sensor">
        <h2 v-once>Editar sensor - {{ sensor.idSensor }} {{ sensor.tipo }}</h2>

        <form @submit.prevent="updateSensor">
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
const route = useRoute()
const idSensor = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL

const sensor = ref(null)
const messages = ref([])

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

const fetchSensor = async () => {
    try {
        const response = await fetch(`${api}/sensores/${idSensor}`)
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
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(sensorForm),
        }

        const response = await fetch(`${api}/sensores/${idSensor}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/sensores")
    } catch (error) {
        messages.value.push(error.message)
    }
}


const tipos = ["Temperatura", "Humidade", "Luminosidade", "Pressão"]

fetchSensor()
</script>

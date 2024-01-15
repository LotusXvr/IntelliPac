<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateEncomenda">
            <label for="estado">Estado: </label>
            <!-- select -->
            <select id="estado" v-model="encomendaForm.estado">
                <option :disabled="encomendaForm.estado === 'PENDENTE'" value="pendente">
                    Pendente
                </option>
                <option :disabled="encomendaForm.estado === 'PROCESSAMENTO'" value="processamento">
                    Processamento
                </option>
                <option :disabled="encomendaForm.estado === 'TRANSPORTE'" value="transporte">
                    Transporte
                </option>
                <option :disabled="encomendaForm.estado === 'ENTREGUE'" value="entregue">
                    Entregue
                </option>
                <option :disabled="encomendaForm.estado === 'CANCELADA'" value="cancelada">
                    Cancelada
                </option>
                <option :disabled="encomendaForm.estado === 'DEVOLVIDA'" value="devolvida">
                    Devolvida
                </option>
                <option :disabled="encomendaForm.estado === 'DANIFICADA'" value="danificada">
                    Danificada
                </option>
                <option :disabled="encomendaForm.estado === 'PERDIDA'" value="perdida">
                    Perdida
                </option>
            </select>

            <span v-if="encomendaForm.estado !== null && !isEstadoValid" class="error">
                ERRO: {{ formFeedback.estado }}</span
            >
            <br />

            <br />

            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/encomendas">Back to encomendas</nuxt-link>
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

const encomenda = ref(null)
const messages = ref([])

const encomendaForm = reactive({
    estado: null,
})

const formFeedback = reactive({
    estado: "",
})

const fetchEncomenda = async () => {
    try {
        const response = await fetch(`${api}/encomendas/${id}`)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        encomenda.value = await response.json()
        encomendaForm.estado = encomenda.value.estado
        console.log(encomendaForm.estado)
    } catch (error) {
        messages.value.push(error.message)
    }
}

const estadosPermitidos = [
    "pendente",
    "processamento",
    "transporte",
    "entregue",
    "cancelada",
    "devolvida",
    "danificada",
    "perdida",
]

const isEstadoValid = computed(() => {
    if (!estadosPermitidos.includes(encomendaForm.estado)) {
        formFeedback.estado = "O estado deve ser pendente, processamento, transporte ou entregue"
        return false
    }
    formFeedback.estado = ""
    return true
})

const isFormValid = computed(() => {
    return isEstadoValid.value
})

const updateEncomenda = async () => {
    try {
        const requestOptions = {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(encomendaForm),
        }

        const response = await fetch(`${api}/encomendas/${id}/estado`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/encomendas")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchEncomenda()
</script>

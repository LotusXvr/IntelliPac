<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateEncomenda">
            <div>
                <label for="estado">Estado: </label>
                <select id="estado" v-model="encomendaForm.estado">
                    <option :disabled="estado === 'PENDENTE'" value="pendente">Pendente</option>
                    <option :disabled="estado === 'PROCESSAMENTO'" value="processamento">
                        Processamento
                    </option>
                    <option :disabled="estado === 'TRANSPORTE'" value="transporte">
                        Transporte
                    </option>
                    <option :disabled="estado === 'ENTREGUE'" value="entregue">Entregue</option>
                    <option :disabled="estado === 'CANCELADA'" value="cancelada">Cancelada</option>
                    <option :disabled="estado === 'DEVOLVIDA'" value="devolvida">Devolvida</option>
                    <option :disabled="estado === 'DANIFICADA'" value="danificada">
                        Danificada
                    </option>
                    <option :disabled="estado === 'PERDIDA'" value="perdida">Perdida</option>
                </select>
                <br>
                <span v-if="isGoingToMail" class="info"> Irá ser enviado um mail para {{ encomenda.consumidorFinal }}</span>
                <br>
                <span v-if="!isEstadoSelected" class="error"> ERRO: {{ formFeedback.estado }}</span>
            </div>
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

.info {
    color: blue;
}
</style>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL

const encomenda = ref(null)
const messages = ref([])
const estado = ref(null)

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
        estado.value = encomenda.value.estado
        console.log(encomendaForm.estado + " " + estado.value)
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isEstadoSelected = computed(() => {
    if (encomendaForm.estado === encomenda.value.estado) {
        formFeedback.estado = "O estado deve ser selecionado"
        return false
    }
    formFeedback.estado = ""
    return true
})

const isFormValid = computed(() => {
    return isEstadoSelected.value
})

const isGoingToMail = computed(() => {
    if (encomendaForm.estado == "danificada" || encomendaForm.estado == "perdida") {
        return true
    }
    return false
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

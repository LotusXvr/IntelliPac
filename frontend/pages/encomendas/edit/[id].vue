<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateEncomenda">


            
            Embalagem de Transporte:
            <br/>
            <select v-model="encomendaForm.embalagemTransporte">
                <option value="">--- Please select Embalagem de Transporte ---</option>
                <option
                    v-for="embalagemTransporte in embalagensTransporte"
                    :value="embalagemTransporte.id"
                >
                    {{ embalagemTransporte.material }}
                </option>
            </select>
            <span v-if="encomendaForm.embalagemTransporte !== null && !isEmbalagemSelected" class="error">
                ERRO: {{ formFeedback.embalagemTransporte }}</span
            >
            <br />

            <br/>
            <br/>

            <label for="estado">Estado: </label>
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

const { data: embalagensTransporte } = await useFetch(`${api}/embalagensDeTransporte`)

const encomenda = ref(null)
const messages = ref([])

const encomendaForm = reactive({
    embalagemTransporte: null,
    estado: null,
})

const formFeedback = reactive({
    embalagemTransporte: "",
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


const isEmbalagemSelected = computed(() => {
    if (encomendaForm.embalagemTransporte === "") {
        formFeedback.embalagemTransporte = "A embalagem de transporte deve ser selecionada"
        return false
    }
    formFeedback.embalagemTransporte = ""
    return true
})


const isFormValid = computed(() => {
    return isEmbalagemSelected.value
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

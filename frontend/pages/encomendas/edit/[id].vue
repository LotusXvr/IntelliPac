<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateEncomenda">
            <!-- <div v-if="encomendaForm.estado === 'PENDENTE'">
                Embalagem de Transporte:
                <br />
                <select v-model="encomendaForm.embalagensTransporte">
                    <option
                        v-for="embalagemTransporte in embalagensTransporte"
                        :value="embalagemTransporte.id"
                    >
                        {{ embalagemTransporte.material }}
                    </option>
                </select>
                <span
                    v-if="encomendaForm.embalagensTransporte !== null && !isEmbalagemSelected"
                    class="error"
                >
                    ERRO: {{ formFeedback.embalagensTransporte }}</span
                >
                <br />
                <br />
            </div> -->

            <div v-if="encomendaForm.estado === 'PENDENTE'">
                Embalagens de Transporte:
                <p v-for="embalagem in embalagensTransporte">
                    <input
                        type="checkbox"
                        :value="embalagem.id"
                        v-model="encomendaForm.embalagensTransporte"
                    />
                    {{ embalagem.material }}
                </p>
                <span v-if="!isEmbalagemSelected" class="error">
                    ERRO: {{ formFeedback.embalagensTransporte }}</span
                >
            </div>

            <div
                v-if="
                    encomendaForm.embalagensTransporte.length > 0 &&
                    encomendaForm.estado !== 'PENDENTE'
                "
            >
                <label for="estado">Estado: </label>
                <select id="estado" v-model="encomendaForm.estado">
                    <option :disabled="encomendaForm.estado === 'PENDENTE'" value="pendente">
                        Pendente
                    </option>
                    <option
                        :disabled="encomendaForm.estado === 'PROCESSAMENTO'"
                        value="processamento"
                    >
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
                <span
                    v-if="!isEstadoSelected && encomendaForm.embalagensTransporte.length > 0"
                    class="error"
                >
                    ERRO: {{ formFeedback.estado }}</span
                >
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
    embalagensTransporte: [],
    estado: null,
})

const formFeedback = reactive({
    embalagensTransporte: "",
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
        encomendaForm.embalagensTransporte = encomenda.value.embalagensTransporte
        console.log(encomendaForm.estado)
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isEmbalagemSelected = computed(() => {
    if (encomendaForm.embalagensTransporte.length === 0) {
        formFeedback.embalagensTransporte = "A embalagem de transporte deve ser selecionada"
        return false
    }
    formFeedback.embalagensTransporte = ""
    return true
})

const isEstadoSelected = computed(() => {
    console.log(encomendaForm.estado)
    if (encomendaForm.estado === encomenda.value.estado) {
        formFeedback.estado = "O estado deve ser selecionado"
        return false
    }
    formFeedback.estado = ""
    return true
})

const isFormValid = computed(() => {
    console.log(isEmbalagemSelected.value + " " + isEstadoSelected.value)
    return isEmbalagemSelected.value || isEstadoSelected.value
})

const updateEncomenda = async () => {
    try {
        let endpoint
        let requestBody

        if (isEmbalagemSelected.value && encomendaForm.estado === encomenda.value.estado) {
            endpoint = "embalagensDeTransporte"
            requestBody = {
                embalagensTransporte: encomendaForm.embalagensTransporte.map((id) => ({ id })),
            }
        } else {
            endpoint = "estado"
            requestBody = {
                estado: encomendaForm.estado,
            }
        }

        const requestOptions = {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody),
        }

        const response = await fetch(`${api}/encomendas/${id}/${endpoint}`, requestOptions)
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

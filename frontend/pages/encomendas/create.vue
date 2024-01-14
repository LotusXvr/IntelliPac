<template>
    <form @submit.prevent="create">
        <!-- <label for="consumidorFinal">Cliente: </label>
        <input id="consumidorFinal" v-model="encomendaForm.consumidorFinal" />
        <span class="error">{{ formFeedback.consumidorFinal }}</span> -->
        <div>
            Consumidor Final:
            <select v-model="encomendaForm.consumidorFinal">
                <option value="">--- Please select Consumidor Final ---</option>
                <option v-for="consumidorFinal in consumidoresFinais" :value="consumidorFinal.username">
                    {{ consumidorFinal.username }}
                </option>
            </select>
            <span v-if="encomendaForm.consumidorFinal !== null && !isConsumidorFinalValid" class="error">
                ERRO: {{ formFeedback.consumidorFinal }}</span>
        </div>
        <br />
        <div>
            Operador Logistica:
            <select v-model="encomendaForm.operadorLogistica">
                <option value="">--- Please select Operador ---</option>
                <option v-for="operadorLogistica in operadoresLogistica" :value="operadorLogistica.username">
                    {{ operadorLogistica.username }}
                </option>
            </select>
            <span v-if="encomendaForm.operadorLogistica !== null && !isOperadorValid" class="error">
                ERRO: {{ formFeedback.operadorLogistica }}</span>
        </div>

        <br />
        <button type="submit" :disabled="!isFormValid">Criar encomenda</button>
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
const encomendaForm = reactive({
    consumidorFinal: "",
    operadorLogistica: "",
})

const formFeedback = reactive({
    consumidorFinal: "",
    operadorLogistica: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: operadoresLogistica } = await useFetch(`${api}/operadores`)
const { data: consumidoresFinais } = await useFetch(`${api}/clientes`)
const message = ref("")

const isConsumidorFinalValid = computed(() => {
    if (encomendaForm.consumidorFinal === null || encomendaForm.consumidorFinal === "") {
        formFeedback.consumidorFinal = "Escolha um consumidor final"
        return false
    }
    formFeedback.consumidorFinal = ""
    return true
})

const isOperadorValid = computed(() => {
    if (encomendaForm.operadorLogistica === null || encomendaForm.operadorLogistica === "") {
        formFeedback.operadorLogistica = "Escolha um operador de logistica"
        return false
    }

    formFeedback.operadorLogistica = ""
    return true
})

const isFormValid = computed(() => {
    return isConsumidorFinalValid.value && isOperadorValid.value
})

async function create() {
    console.log(encomendaForm)
    encomendaForm.operadorLogistica = encomendaForm.operadorLogistica

    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(encomendaForm),
    }

    const { error } = await useFetch(`${api}/encomendas`, requestOptions)
    if (!error.value) navigateTo("/encomendas")
    message.value = error.value
}
</script>

<template>
    <form @submit.prevent="create">
        <Navbar />
        <div>
            Consumidor Final:
            <select v-model="encomendaForm.consumidorFinal">
                <option value="">--- Please select Consumidor Final ---</option>
                <option v-for="consumidorFinal in consumidoresFinais" :value="consumidorFinal.username">
                    {{ consumidorFinal.name }}
                </option>
            </select>
            <span v-if="encomendaForm.consumidorFinal !== null && !isConsumidorFinalValid" class="error">
                ERRO: {{ formFeedback.consumidorFinal }}</span>
        </div>
        <br />
        <div>
            Operador Logistica: {{ user.username }}
            <!-- <select v-model="encomendaForm.operadorLogistica">
                <option value="">--- Please select Operador ---</option>
                <option v-for="operadorLogistica in operadoresLogistica" :value="operadorLogistica.username">
                    {{ operadorLogistica.username }}
                </option>
            </select>
            <span v-if="encomendaForm.operadorLogistica !== null && !isOperadorSelected" class="error">
                ERRO: {{ formFeedback.operadorLogistica }}</span> -->
        </div>
        <br />
        <div>
            Produtos:
            <p v-for="produto in produtosCatalogo">
                <input type="checkbox" :value="produto.id" v-model="encomendaForm.produtosCatalogo" />
                {{ produto.nome }}
            <ul> Embalagens:
                <li v-for="embalagem in produto.embalagensACriar">{{ tipoNumeroParaString(embalagem.tipo) }}: {{
                    embalagem.material }}</li>
            </ul>
            </p>
            <span v-if="!isProdutoSelected" class="error">
                ERRO: {{ formFeedback.produtosCatalogo }}</span>
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
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store.js"

const authStore = useAuthStore()
const { user } = authStore

const encomendaForm = reactive({
    consumidorFinal: "",
    operadorLogistica: "",
    produtosCatalogo: [],
    embalagensTransporte: [],
})

const formFeedback = reactive({
    consumidorFinal: "",
    operadorLogistica: "",
    produtosCatalogo: "",
    embalagensTransporte: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: operadoresLogistica } = await useFetch(`${api}/operadores`)
const { data: consumidoresFinais } = await useFetch(`${api}/clientes`)
const { data: produtosCatalogo } = await useFetch(`${api}/produtosCatalogo`)
const message = ref("")

const tipoNumeroParaString = (tipo) => {
    switch (tipo) {
        case 1:
            return "Primária"
        case 2:
            return "Secundária"
        case 3:
            return "Terceária"
        default:
            return "Tipo desconhecido"
    }
}

const isConsumidorSelected = computed(() => {
    if (encomendaForm.consumidorFinal === null || encomendaForm.consumidorFinal === "") {
        formFeedback.consumidorFinal = "Escolha um consumidor final"
        return false
    }

    formFeedback.consumidorFinal = ""
    return true
})

// const isOperadorSelected = computed(() => {
//     if (encomendaForm.operadorLogistica === null || encomendaForm.operadorLogistica === "") {
//         formFeedback.operadorLogistica = "Escolha um operador de logistica"
//         return false
//     }

//     formFeedback.operadorLogistica = ""
//     return true
// })

const isProdutoSelected = computed(() => {
    if (encomendaForm.produtosCatalogo.length === 0) {
        formFeedback.produtosCatalogo = "Escolha pelo menos um produto"
        return false
    }

    formFeedback.produtosCatalogo = ""
    return true
})

const isFormValid = computed(() => {
    return (
        isConsumidorSelected.value &&
        isProdutoSelected.value
    )
})

async function create() {
    console.log(encomendaForm)

    // Map the form data to the desired payload structure
    const requestBody = {
        consumidorFinal: encomendaForm.consumidorFinal,
        //operadorLogistica: encomendaForm.operadorLogistica,
        operadorLogistica: user.username,
        produtos: encomendaForm.produtosCatalogo.map((id) => ({ produtoCatalogoId: id })),
        embalagensTransporte: encomendaForm.embalagensTransporte.map((id) => ({ id })),
    }

    console.log(requestBody)

    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody),
    }

    const { error } = await useFetch(`${api}/encomendas`, requestOptions)
    if (!error.value) navigateTo("/encomendas")
    message.value = error.value
}
</script>

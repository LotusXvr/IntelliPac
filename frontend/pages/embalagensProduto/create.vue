<template>
    <Navbar />
    <form @submit.prevent="create">
        <label for="material">Material: </label>
        <input id="material" v-model="embalagemForm.material" />
        <span class="error" v-if="!isMaterialValid">{{ formFeedback.material }}</span>
        <br />
        <label for="tipo">Tipo De Embalagem: </label>
        <select id="tipo" v-model="embalagemForm.tipo">
            <option value="1">Primaria</option>
            <option value="2">Secundaria</option>
            <option value="3">Terciaria</option>
        </select>
        <br />
        <label for="comprimento">Comprimento: </label>
        <input id="comprimento" v-model="embalagemForm.comprimento" />
        <span class="error" v-if="!isComprimentoValid"> {{ formFeedback.comprimento }}</span>
        <br />

        <label for="largura">Largura: </label>
        <input id="largura" v-model="embalagemForm.largura" />
        <span class="error" v-if="!isLarguraValid">{{ formFeedback.largura }}</span>
        <br />

        <label for="altura">Altura: </label>
        <input id="altura" v-model="embalagemForm.altura" />
        <span class="error" v-if="!isAlturaValid">{{ formFeedback.altura }}</span>

        <br />
        <button type="submit" :disabled="!isFormValid">Criar embalagem</button>
    </form>
    {{ message }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
import { ref, reactive, computed } from "vue"

const authStore = useAuthStore()
const embalagemForm = reactive({
    material: null,
    tipo: null,
    comprimento: null,
    largura: null,
    altura: null,
})

const formFeedback = reactive({
    material: "",
    tipo: "",
    comprimento: "",
    largura: "",
    altura: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref("")

const isMaterialValid = computed(() => {
    if (embalagemForm.material === null) {
        return false
    }
    if (embalagemForm.material.length < 3) {
        formFeedback.material = "ERRO: A embalagem deve ter pelo menos 3 caracteres"
        return false
    }
    if (embalagemForm.material.length > 25) {
        formFeedback.material = "ERRO: A embalagem deve ter no máximo 25 caracteres"
        return false
    }
    formFeedback.material = ""
    return true
})

const isComprimentoValid = computed(() => {
    if (embalagemForm.comprimento === null) {
        return false
    }
    if (embalagemForm.comprimento < 1) {
        formFeedback.comprimento = "ERRO: O comprimento deve ser maior que 0"
        return false
    }
    if (isNaN(embalagemForm.comprimento)) {
        formFeedback.comprimento = "ERRO: O comprimento deve ser um número"
        return false
    }

    formFeedback.comprimento = ""
    return true
})

const isLarguraValid = computed(() => {
    if (embalagemForm.largura === null) {
        return false
    }
    if (embalagemForm.largura < 1) {
        formFeedback.largura = "ERRO: A largura deve ser maior que 0"
        return false
    }
    if (isNaN(embalagemForm.largura)) {
        formFeedback.largura = "ERRO: A largura deve ser um número"
        return false
    }
    formFeedback.largura = ""
    return true
})

const isAlturaValid = computed(() => {
    if (embalagemForm.altura === null) {
        return false
    }
    if (embalagemForm.altura < 1) {
        formFeedback.altura = "ERRO: A altura deve ser maior que 0"
        return false
    }
    if (isNaN(embalagemForm.altura)) {
        formFeedback.altura = "ERRO: A altura deve ser um número"
        return false
    }
    formFeedback.altura = ""
    return true
})

const isTipoValid = computed(() => {
    if (embalagemForm.tipo === null || embalagemForm.tipo<1 || embalagemForm.tipo>3) {
        return false
    }
    return true
})

const isFormValid = computed(() => {
    return (
        isMaterialValid.value &&
        isComprimentoValid.value &&
        isLarguraValid.value &&
        isAlturaValid.value &&
        isTipoValid.value
    )
})

async function create() {

    const requestOptions = {
        method: "POST",
        headers: { 
            "Content-Type": "application/json",
            'Authorization': 'Bearer ' + authStore.token
        },
        body: JSON.stringify(embalagemForm),
    }

    const { error } = await useFetch(`${api}/tipoEmbalagens`, requestOptions)
    if (!error.value) navigateTo("/embalagensProduto")
    message.value = error.value
}
</script>

<template>
    <form @submit.prevent="create">
        <label for="material">Material: </label>
        <input id="material" v-model="embalagemForm.material" />
        <br />
        <label for="altura">Altura: </label>
        <input id="altura" type="number" min="1" v-model="embalagemForm.altura" /> cm
        <br />
        <label for="largura">Largura: </label>
        <input id="largura" type="number" min="1" v-model="embalagemForm.largura" /> cm
        <br />
        <label for="comprimento">Comprimento: </label>
        <input id="comprimento" type="number" min="1" v-model="embalagemForm.comprimento" /> cm
        <span class="error">{{ formFeedback.material }}</span>
        <br />
        <button type="submit" :disabled="!isFormValid">Criar embalagem</button>
    </form>
    <nuxt-link to="/embalagensTransporte">Back to Embalagens de transporte</nuxt-link>
    {{ message }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import { ref, reactive, computed } from "vue"
const embalagemForm = reactive({
    material: null,
    altura : null,
    largura : null,
    comprimento : null
})

const formFeedback = reactive({
    material: "",
    altura : "",
    largura : "",
    comprimento : ""
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref("")

const isMaterialValid = computed(() => {
    if (embalagemForm.material === null) {
        return false
    }
    if (embalagemForm.material.length < 1) {
        formFeedback.material = "A embalagem deve ter pelo menos 1 caracteres"
        return false
    }
    if (embalagemForm.material.length > 25) {
        formFeedback.material = "A embalagem deve ter no máximo 25 caracteres"
        return false
    }
    formFeedback.material = ""
    return true
})

const isAlturaValid = computed(() => {
    if (embalagemForm.altura !== null && !isNaN(embalagemForm.altura) && embalagemForm.altura > 0) {
        formFeedback.altura = "";
        return true;
    }
    formFeedback.altura = "A altura tem de ser maior que 0"
    return false;
})

const isLarguraValid = computed(() => {
    if (embalagemForm.largura !== null && !isNaN(embalagemForm.largura) && embalagemForm.largura > 0) {
        formFeedback.largura = "";
        return true;
    }
    formFeedback.largura = "A largura tem de ser maior que 0"
    return false;
})

const isComprimentoValid = computed(() => {
    if (embalagemForm.comprimento !== null && !isNaN(embalagemForm.comprimento) && embalagemForm.comprimento > 0) {
        formFeedback.comprimento = "";
        return true;
    }
    formFeedback.comprimento = "O comprimento tem de ser maior que 0"
    return false;
})

const isFormValid = computed(() => {
    return isMaterialValid.value && isAlturaValid.value && isLarguraValid.value && isComprimentoValid.value
})

async function create() {
    console.log(embalagemForm)

    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(embalagemForm),
    }

    const { error } = await useFetch(`${api}/embalagensDeTransporte`, requestOptions)
    if (!error.value) navigateTo("/embalagensTransporte")
    message.value = error.value
}
</script>

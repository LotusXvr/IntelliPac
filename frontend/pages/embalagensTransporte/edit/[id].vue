<template>
    <Navbar />
    <div v-if="embalagem">
        <h2 v-once>Editar embalagem - {{ embalagem.material }}</h2>

        <form @submit.prevent="updateEmbalagem">
            <label for="material">Material:</label>
            <input v-model.trim="embalagemForm.material" type="text" />
            <span v-if="embalagemForm.material !== null && !isMaterialValid" class="error">
                ERRO: {{ formFeedback.material }}</span
            >
            <br />
            <label for="altura">Altura: </label>
            <input id="altura" type="number" min="1" v-model="embalagemForm.altura" /> cm
            <span v-if="embalagemForm.altura !== null && !isAlturaValid" class="error">
                ERRO: {{ formFeedback.altura }}</span
            >
            <br />
            <label for="largura">Largura: </label>
            <input id="largura" type="number" min="1" v-model="embalagemForm.largura" /> cm
            <span v-if="embalagemForm.largura !== null && !isLarguraValid" class="error">
                ERRO: {{ formFeedback.largura }}</span
            >
            <br />
            <label for="comprimento">Comprimento: </label>
            <input id="comprimento" type="number" min="1" v-model="embalagemForm.comprimento" /> cm
            <span v-if="embalagemForm.comprimento !== null && !isComprimentoValid" class="error">
                ERRO: {{ formFeedback.comprimento }}</span
            >
            <br />

            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/embalagensTransporte">Back to Embalagens de transporte</nuxt-link>
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
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authUser = useAuthStore()

const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL

const embalagem = ref(null)
const messages = ref([])

const embalagemForm = reactive({
    material: null,
    altura: null,
    largura: null,
    comprimento: null,
})

const formFeedback = reactive({
    material: "",
    altura: "",
    largura: "",
    comprimento: "",
})

const fetchEmbalagens = async () => {
    try {
        const response = await fetch(`${api}/embalagensDeTransporte/${id}`, {
            method: "GET",
            headers: { Authorization: "Bearer " + authUser.token },
        })
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        embalagem.value = await response.json()
        embalagemForm.material = embalagem.value.material
        embalagemForm.altura = embalagem.value.altura
        embalagemForm.largura = embalagem.value.largura
        embalagemForm.comprimento = embalagem.value.comprimento
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isMaterialValid = computed(() => {
    if (embalagemForm.material === null) {
        return false
    }
    if (embalagemForm.material.length < 1) {
        formFeedback.material = "O material deve ter pelo menos 1 caracter"
        return false
    }
    if (embalagemForm.material.length > 25) {
        formFeedback.material = "O material deve ter no máximo 25 caracteres"
        return false
    }
    formFeedback.material = ""
    return true
})

const isAlturaValid = computed(() => {
    if (embalagemForm.altura !== null && !isNaN(embalagemForm.altura) && embalagemForm.altura > 0) {
        formFeedback.altura = ""
        return true
    }
    formFeedback.altura = "A altura tem de ser maior que 0"
    return false
})

const isLarguraValid = computed(() => {
    if (
        embalagemForm.largura !== null &&
        !isNaN(embalagemForm.largura) &&
        embalagemForm.largura > 0
    ) {
        formFeedback.largura = ""
        return true
    }
    formFeedback.largura = "A largura tem de ser maior que 0"
    return false
})

const isComprimentoValid = computed(() => {
    if (
        embalagemForm.comprimento !== null &&
        !isNaN(embalagemForm.comprimento) &&
        embalagemForm.comprimento > 0
    ) {
        formFeedback.comprimento = ""
        return true
    }
    formFeedback.comprimento = "O comprimento tem de ser maior que 0"
    return false
})

const isFormValid = computed(() => {
    return (
        isMaterialValid.value &&
        isAlturaValid.value &&
        isLarguraValid.value &&
        isComprimentoValid.value
    )
})
const updateEmbalagem = async () => {
    try {
        const requestOptions = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + authUser.token,
            },
            body: JSON.stringify(embalagemForm),
        }
        const response = await fetch(`${api}/embalagensDeTransporte/${id}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/embalagensTransporte")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchEmbalagens()
</script>

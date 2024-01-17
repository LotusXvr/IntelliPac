<template>
    <div v-if="embalagem">
        <h2 v-once>Editar embalagem - {{ embalagem.material }}</h2>

        <form @submit.prevent="updateEmbalagem">
            <label for="material">Material:</label>
            <input v-model.trim="embalagemForm.material" type="text" />
            <span v-if="embalagemForm.material !== null && !isMaterialValid" class="error">
                ERRO: {{ formFeedback.material }}</span>
            <br>

            <label for="comprimento">Comprimento: </label>
            <input id="comprimento" v-model.trim="embalagemForm.comprimento" />
            <span class="error" v-if="!isComprimentoValid"> {{ formFeedback.comprimento }}</span>
            <br />

            <label for="largura">Largura: </label>
            <input id="largura" v-model.trim="embalagemForm.largura" />
            <span class="error" v-if="!isLarguraValid">{{ formFeedback.largura }}</span>
            <br />

            <label for="altura">Altura: </label>
            <input id="altura" v-model.trim="embalagemForm.altura" />
            <span class="error" v-if="!isAlturaValid">{{ formFeedback.altura }}</span>

            <br>
            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/embalagens">Back to Embalagens</nuxt-link>
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

const embalagem = ref(null)
const messages = ref([])

const embalagemForm = reactive({
    material: null,
    comprimento: null,
    largura: null,
    altura: null,
})

const formFeedback = reactive({
    material: "",
    comprimento: "",
    largura: "",
    altura: "",
})

const fetchEmbalagens = async () => {
    try {
        const response = await fetch(`${api}/embalagens/${id}`)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        embalagem.value = await response.json()
        embalagemForm.material = embalagem.value.material
        embalagemForm.comprimento = embalagem.value.comprimento
        embalagemForm.largura = embalagem.value.largura
        embalagemForm.altura = embalagem.value.altura

    } catch (error) {
        messages.value.push(error.message)
    }
}

const isMaterialValid = computed(() => {
    if (embalagemForm.material === null) {
        return false
    }
    if (embalagemForm.material.length < 3) {
        formFeedback.material = "O material deve ter pelo menos 3 caracteres"
        return false
    }
    if (embalagemForm.material.length > 25) {
        formFeedback.material = "O material deve ter no máximo 25 caracteres"
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

const isFormValid = computed(() => {
    return isMaterialValid.value && isComprimentoValid.value && isLarguraValid.value && isAlturaValid.value
})

const updateEmbalagem = async () => {
    try {
        const requestOptions = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(embalagemForm),
        }

        const response = await fetch(`${api}/embalagens/${id}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/embalagens")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchEmbalagens()
</script>

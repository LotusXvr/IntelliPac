<template>
    <div v-if="embalagem">
        <h2 v-once>Editar embalagem - {{ embalagem.material }}</h2>

        <form @submit.prevent="updateEmbalagem">
            <label for="material">Material:</label>
            <input v-model.trim="embalagemForm.material" type="text" />
            <span v-if="embalagemForm.material !== null && !isMaterialValid" class="error">
                ERRO: {{ formFeedback.material }}</span
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
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL

const embalagem = ref(null)
const messages = ref([])

const embalagemForm = reactive({
    material: null,
})

const formFeedback = reactive({
    material: "",
})

const fetchEmbalagens = async () => {
    try {
        const response = await fetch(`${api}/embalagens/${id}`)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        embalagem.value = await response.json()
        embalagemForm.material = embalagem.value.material
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

const isFormValid = computed(() => {
    return isMaterialValid.value
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

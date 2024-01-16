<template>
    <form @submit.prevent="create">
        <label for="material">Material: </label>
        <input id="material" v-model="embalagemForm.material" />
        <span class="error">{{ formFeedback.material }}</span>
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
import { ref, reactive, computed } from "vue"
const embalagemForm = reactive({
    material: null,
})

const formFeedback = reactive({
    material: "",
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

const isFormValid = computed(() => {
    return isMaterialValid.value
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

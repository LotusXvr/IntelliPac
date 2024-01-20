<template>
    <Navbar />
    <div v-if="operador">
        <h2 v-once>Editar operador - {{ operador.name }}</h2>

        <form @submit.prevent="updateOperador">
            <label for="name">Nome:</label>
            <input v-model.trim="operadorForm.name" type="text" />
            <span v-if="operadorForm.name !== null && !isNameValid" class="error">
                ERRO: {{ formFeedback.name }}</span
            >
            <br />

            <label for="email">Email: </label>
            <input id="email" type="email" v-model="operadorForm.email" />
            <span class="error" v-if="!isEmailValid">{{ formFeedback.email }}</span>
            <br />
            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/operadores">Voltar aos operadores</nuxt-link>
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
const authStore = useAuthStore()
const route = useRoute()
const username = route.params.username
const config = useRuntimeConfig()
const api = config.public.API_URL

const operador = ref(null)
const messages = ref([])

const operadorForm = reactive({
    name: null,
    email: null,
})

const formFeedback = reactive({
    name: "",
    email: "",
})

const fetchOperador = async () => {
    try {
        const response = await fetch(`${api}/operadores/${username}`, {
            method: "GET",
            headers: { Authorization: "Bearer " + authStore.token },
        })
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        operador.value = await response.json()
        operadorForm.name = operador.value.name
        operadorForm.email = operador.value.email
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isNameValid = computed(() => {
    if (operadorForm.name === null) {
        return false
    }
    if (operadorForm.name.length < 3) {
        formFeedback.name = "O nome deve ter pelo menos 3 caracteres"
        return false
    }
    if (operadorForm.name.length > 20) {
        formFeedback.name = "O nome deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.name = ""
    return true
})

const isEmailValid = computed(() => {
    if (operadorForm.email === null) {
        return false
    }
    if (operadorForm.email.length < 3) {
        formFeedback.email = "O email deve ter pelo menos 3 caracteres"
        return false
    }
    if (operadorForm.email.length > 20) {
        formFeedback.email = "O email deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.email = ""
    return true
})

const isFormValid = computed(() => {
    return isNameValid.value
})

const updateOperador = async () => {
    try {
        const requestOptions = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + authStore.token,
            },
            body: JSON.stringify(operadorForm),
        }

        const response = await fetch(`${api}/operadores/${username}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/operadores")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchOperador()
</script>

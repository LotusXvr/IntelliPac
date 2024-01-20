<template>
    <Navbar />
    <div v-if="cliente">
        <h2 v-once>Editar cliente - {{ cliente.name }}</h2>

        <form @submit.prevent="updateCliente">
            <label for="name">Nome:</label>
            <input v-model.trim="clienteForm.name" type="text" />
            <span v-if="clienteForm.name !== null && !isNameValid" class="error">
                ERRO: {{ formFeedback.name }}</span>
            <br />

            <label for="email">Email: </label>
            <input id="email" type="email" v-model="clienteForm.email" />
            <span class="error" v-if="!isEmailValid">{{ formFeedback.email }}</span>
            <br />
            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/clientes">Voltar aos clientes</nuxt-link>
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

const cliente = ref(null)
const messages = ref([])

const clienteForm = reactive({
    name: null,
    email: null,
})

const formFeedback = reactive({
    name: "",
    email: "",
})

const fetchCliente = async () => {
    try {
        const response = await fetch(`${api}/clientes/${username}`, {
            method: "GET",
            headers: { Authorization: "Bearer " + authStore.token },
        })
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        cliente.value = await response.json()
        clienteForm.name = cliente.value.name
        clienteForm.email = cliente.value.email
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isNameValid = computed(() => {
    if (clienteForm.name === null) {
        return false
    }
    if (clienteForm.name.length < 3) {
        formFeedback.name = "O nome deve ter pelo menos 3 caracteres"
        return false
    }
    if (clienteForm.name.length > 20) {
        formFeedback.name = "O nome deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.name = ""
    return true
})

const isEmailValid = computed(() => {
    if (clienteForm.email === null) {
        return false
    }
    if (clienteForm.email.length < 3) {
        formFeedback.email = "O email deve ter pelo menos 3 caracteres"
        return false
    }
    formFeedback.email = ""
    return true
})

const isFormValid = computed(() => {
    return isNameValid.value
})

const updateCliente = async () => {
    try {
        const requestOptions = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + authStore.token,
            },
            body: JSON.stringify(clienteForm),
        }

        const response = await fetch(`${api}/clientes/${username}`, requestOptions)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/clientes")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchCliente()
</script>

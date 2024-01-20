<template>
    <Navbar />
    <form @submit.prevent="create">
        <label for="name">Nome: </label>
        <input id="name" v-model="operadorForm.name" />
        <span class="error" v-if="!isNameValid">{{ formFeedback.name }}</span>
        <br />

        <label for="username">Username: </label>
        <input id="username" v-model="operadorForm.username" />
        <span class="error" v-if="!isUsernameValid">{{ formFeedback.username }}</span>
        <br />

        <label for="email">Email: </label>
        <input id="email" type="email" v-model="operadorForm.email" />
        <span class="error" v-if="!isEmailValid">{{ formFeedback.email }}</span>
        <br />

        <label for="password">Password: </label>
        <input id="password" type="password" v-model="operadorForm.password" />
        <span class="error" v-if="!isPasswordValid">{{ formFeedback.password }}</span>
        <br />

        <button type="submit" :disabled="!isFormValid">Criar operador</button>
    </form>
    {{ message }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
import Navbar from "~/layouts/nav-bar.vue"
import { ref, reactive, computed } from "vue"
const operadorForm = reactive({
    name: null,
    username: null,
    email: null,
    password: null,
})

const formFeedback = reactive({
    name: "",
    username: "",
    email: "",
    password: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref("")

const isNameValid = computed(() => {
    if (operadorForm.name === null) {
        return false
    }
    if (operadorForm.name.length < 3) {
        formFeedback.name = "ERRO: O nome deve ter pelo menos 3 caracteres"
        return false
    }
    if (operadorForm.name.length > 20) {
        formFeedback.name = "ERRO: O nome deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.name = ""
    return true
})

const isUsernameValid = computed(() => {
    if (operadorForm.username === null) {
        return false
    }
    if (operadorForm.username.length < 3) {
        formFeedback.username = "ERRO: O username deve ter pelo menos 3 caracteres"
        return false
    }
    if (operadorForm.username.length > 20) {
        formFeedback.username = "ERRO: O username deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.username = ""
    return true
})

const isEmailValid = computed(() => {
    if (operadorForm.email === null) {
        return false
    }
    if (operadorForm.email.length < 3) {
        formFeedback.email = "ERRO: O email deve ter pelo menos 3 caracteres"
        return false
    }
    formFeedback.email = ""
    return true
})

const isPasswordValid = computed(() => {
    if (operadorForm.password === null) {
        return false
    }
    if (operadorForm.password.length < 3) {
        formFeedback.password = "ERRO: A password deve ter pelo menos 3 caracteres"
        return false
    }
    if (operadorForm.password.length > 20) {
        formFeedback.password = "ERRO: A password deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.password = ""
    return true
})

const isFormValid = computed(() => {
    return isNameValid.value && isUsernameValid.value && isEmailValid.value && isPasswordValid.value
})

async function create() {
    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: "Bearer " + authStore.token },
        body: JSON.stringify(operadorForm),
    }

    const { error } = await useFetch(`${api}/operadores`, requestOptions)
    if (!error.value) navigateTo("/operadores")
    message.value = error.value
}
</script>

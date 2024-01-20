<template>
    <Navbar />
    <div v-if="cliente">
        <h2>Detalhes de {{ cliente.name }}</h2>
        <p>Username: {{ cliente.username }}</p>
        <p>Email: {{ cliente.email }}</p>
        <p>Encomendas:</p>
        <ul>
            <li v-for="encomenda in cliente.encomendas" :key="encomenda.id">
                {{ encomenda.id }} - {{ encomenda.dataEncomenda }} - {{ encomenda.estado }}
            </li>
        </ul>
    </div>
    <nuxt-link to="/clientes">Voltar aos clientes</nuxt-link>
    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const route = useRoute()
const username = route.params.username
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: cliente, error: clienteErr } = await useFetch(
    `${api}/clientes/${username}`,
    { method: "GET", headers: { Authorization: "Bearer " + authStore.token } }
)
const messages = ref([])
if (clienteErr.value) messages.value.push(clienteErr.value)
</script>

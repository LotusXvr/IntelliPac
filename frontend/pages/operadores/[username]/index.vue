<template>
    <Navbar />
    <div v-if="operador">
        <h2>Detalhes de {{ operador.name }}</h2>
        <p>Username: {{ operador.username }}</p>
        <p>Email: {{ operador.email }}</p>
        <p>Encomendas:</p>
        <ul>
            <li v-for="encomenda in operador.encomendas" :key="encomenda.id">
                {{ encomenda.id }} - {{ encomenda.dataEncomenda }} - {{ encomenda.estado }}
            </li>
        </ul>
    </div>

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
const { data: operador, error: operadorErr } = await useFetch(
    `${api}/operadores/${username}`,
    { method: "GET", headers: { Authorization: "Bearer " + authStore.token } }
)
const messages = ref([])
if (operadorErr.value) messages.value.push(operadorErr.value)
</script>

<template>
    <Navbar />
    <div v-if="observacao">
        <h2>Detalhes de {{ observacao.id }}</h2>
        <p>Id: {{ observacao.id }}</p>
        <p>Timestamp: {{ observacao.timestamp }}</p>
        <p>Valor: {{ observacao.valor }}</p>
        <p>Sensor Id: {{ observacao.sensorId }}</p>
    </div>

    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const {
    data: observacao,
    error: proErr,
} = await useFetch(`${api}/observacoes/${id}`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})
const messages = ref([])
if (proErr.value) messages.value.push(proErr.value)
</script>

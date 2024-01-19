<template>
    <Navbar />
    <div v-if="embalagem">
        <h2>Detalhes de {{ embalagem.id }} {{ embalagem.material }}</h2>
        <p>Id: {{ embalagem.id }}</p>
        <p>Tipo: {{ tipoToString(embalagem.tipo) }}</p>
        <p>Material: {{ embalagem.material }}</p>
        <p>Sensores:</p>
        <ul>
            <li v-for="sensor in embalagem.sensoresDTO" :key="sensor.id">
                {{ sensor.id }} - {{ sensor.tipo }} ({{ sensor.unidade }})
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
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: embalagem, error: proErr } = await useFetch(`${api}/tipoEmbalagens/${id}`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})
const messages = ref([])
const tipoToString = (tipo) => {
    switch (tipo) {
        case 1:
            return "Primaria"
        case 2:
            return "Secundaria"
        case 3:
            return "Terciaria"
        default:
            return "Tipo de embalagem desconhecido"
    }
}

console.log(embalagem.value)
if (proErr.value) messages.value.push(proErr.value)
</script>

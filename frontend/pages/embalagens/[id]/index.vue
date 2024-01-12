<template>
    <div v-if="embalagem">
        <h2>Detalhes de {{ embalagem.id }} {{ embalagem.material }}</h2>
        <p>Id: {{ embalagem.id }}</p>
        <p>Material: {{ embalagem.material }}</p>
        <p>Sensores:</p>
        <ul>
            <li v-for="sensor in embalagem.sensores" :key="sensor.idSensor">
                {{ sensor.idSensor }} - {{ sensor.tipo }} ({{ sensor.unidade }})
            </li>
        </ul>
    </div>

    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: embalagem, error: proErr } = await useFetch(`${api}/embalagens/${id}`)
const messages = ref([])
if (proErr.value) messages.value.push(proErr.value)
</script>

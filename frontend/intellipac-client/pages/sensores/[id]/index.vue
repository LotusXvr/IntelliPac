<template>
    <div v-if="sensor">
        <h2>Detalhes de {{ sensor.idSensor }} {{ sensor.tipo }}</h2>
        <p>Embalagens:</p>
        <ul>
            <li v-for="embalagem in sensor.embagalens" :key="sensor.idSensor">
                {{ embalagem.nome }}
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
const { data: sensor, error: sensorErr } = await useFetch(`${api}/sensores/${id}`)
const messages = ref([])
if (sensorErr.value) messages.value.push(sensorErr.value)
</script>

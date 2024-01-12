<template>
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
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: observacao, error: proErr } = await useFetch(`${api}/observacoes/${id}`)
const messages = ref([])
if (proErr.value) messages.value.push(proErr.value)
</script>

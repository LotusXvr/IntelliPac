<template>
    <div v-if="embalagemDeTransporte">
        <h2>Detalhes de {{ embalagemDeTransporte.id }} {{ embalagemDeTransporte.material }}</h2>
        <p>Id: {{ embalagemDeTransporte.id }}</p>
        <p>Material: {{ embalagemDeTransporte.material }}</p>
        <p>Encomendas:</p>
        <ul>
            <li v-for="encomenda in embalagemDeTransporte.encomendas" :key="encomenda.id">
                <p>Id: {{ encomenda.id }}</p>
                <p>Consumidor Final: {{ encomenda.consumidorFinal }}</p>
                <p>Data da encomenda: {{encomenda.dataEncomenda }} </p>
                <p>Estado: {{encomenda.estado}}</p>

            </li>
        </ul>
        <p>Sensores:</p>
        <ul>
            <li v-for="sensor in embalagemDeTransporte.sensores" :key="sensor.idSensor">
                {{ sensor.idSensor }} - {{ sensor.tipo }} ({{ sensor.unidade }})
            </li>
        </ul>
    </div>
    <nuxt-link to="/embalagensTransporte">Back to Embalagens de transporte</nuxt-link>
    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: embalagemDeTransporte, error: proErr } = await useFetch(`${api}/embalagensDeTransporte/${id}`)
const messages = ref([])
if (proErr.value) messages.value.push(proErr.value)
</script>

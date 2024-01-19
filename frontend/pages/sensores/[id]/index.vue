<template>
    <Navbar />
    <div v-if="sensor">
        <h2>Detalhes de {{ sensor.idSensor }} {{ sensor.tipo }}</h2>

        <p>Id: {{ sensor.id }}</p>
        <p>Id Sensor: {{ sensor.idSensor }}</p>
        <p>Tipo: {{ sensor.tipo }}</p>
        <p>Unidade: {{ sensor.unidade }}</p>

        <p>Embalagens:</p>
        <ul>
            <li v-for="embalagem in sensor.embalagens" :key="embalagem.id">
                <span>{{ embalagem.id }} - {{ embalagem.material }}</span>
            </li>
        </ul>

        <button @click="gerarObservacao(sensor.id)">Gerar Observação</button>

        <div v-if="sensor.observacoes.length > 0">
            <h4>Observações ({{ sensor.observacoes.length }})</h4>
            <canvas ref="chartEl"></canvas>
            <ul>
                <li v-for="observacao in sensor.observacoes" :key="sensor.id">
                    ({{ observacao.timestamp }}) {{ observacao.valor }} {{ sensor.unidade }} - 
                    <nuxt-link :to="`/observacoes/${observacao.id}`">Detalhes</nuxt-link>
                </li>
            </ul>
        </div>
        <div v-else>
            <h4>Nenhuma observação registada</h4>
        </div>
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
const sensor = ref(null)
const observacoes = ref([])
const observacoes_valores = ref([])
const observacoes_timestamps = ref([])
import Chart from "chart.js/auto"

const refreshDetails = () => {
    fetchSensor()
}

const fetchSensor = async () => {
    try {
        sensor.value = null
        observacoes.value = []
        observacoes_valores.value = []
        observacoes_timestamps.value = []
        const { data: response } = await useFetch(`${api}/sensores/${id}`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})
        if (!response) {
            console.log(response)
            throw new Error(response.statusText)
        }
        sensor.value = response.value
        observacoes.value = response.value.observacoes
        // nas observacoes eu quero separar os timestamps e os valores, 1 para cada array
        observacoes.value.forEach((observacao) => {
            observacoes_valores.value.push(observacao.valor)
            observacoes_timestamps.value.push(observacao.timestamp)
        })

        createChart()
    } catch (error) {
        messages.value.push(error.message)
    }
}

const chartCanvas = shallowRef(null)
const chartEl = ref(null)
const createChart = () => {
    let observacoesValores = observacoes_valores.value
    let observacoesTimestamps = observacoes_timestamps.value

    observacoesValores = observacoesValores.reverse()
    observacoesTimestamps = observacoesTimestamps.reverse()

    // Use nextTick to ensure that the DOM has been updated
    nextTick(() => {
        chartCanvas.value = new Chart(chartEl.value.getContext("2d"), {
            type: "line",
            data: {
                labels: observacoesTimestamps,
                datasets: [
                    {
                        label: "Observações",
                        data: observacoesValores,
                        borderColor: "rgba(75, 192, 192, 1)",
                        borderWidth: 2,
                    },
                ],
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
            },
        })
    })
}

const gerarObservacao = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}/gerarObservacao`, {
            method: "POST",
            headers : {
                'Authorization': 'Bearer ' + authStore.token
            }
        })
        if (response.status == 200) {
            refreshDetails()
        }
    } catch (error) {
        console.error(error)
    }
}

fetchSensor()

const messages = ref([])
</script>

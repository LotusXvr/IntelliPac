<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="sensores/create">Criar novo Sensor</nuxt-link>
        <button style="margin-left: 50px" @click="toggleGerarObservacoesAutomaticas">
            Toggle Observacoes Automaticas
        </button>
        {{ on }}
        <h2>Sensores</h2>
        <table>
            <tr>
                <th>id sensor</th>
                <th>Tipo</th>
                <th>Unidade</th>
                <th>Estado</th>
                <th>Embalagens</th>
                <th>Observações</th>
                <th>Ações</th>
            </tr>
            <h4>Sensores Disponiveis</h4>
            <tr v-for="sensor in sensoresDisponiveis">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ estadoToString(sensor.estado) }}</td>
                <td class="center">{{ sensor.embalagens.length }}</td>
                <td class="center">{{ sensor.observacoes.length }}</td>
                <td class="center">
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    <span v-if="!hasEmbalagens(sensor)">
                        |

                        <button @click="deleteProduto(sensor.id)">Excluir</button>
                    </span>
                </td>
            </tr>
            <h4>Sensores em Uso</h4>
            <tr v-for="sensor in sensoresEmUso">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ estadoToString(sensor.estado) }}</td>

                <td>{{ sensor.embalagens.length }}</td>
                <td>{{ sensor.observacoes.length }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    |
                    <nuxt-link :to="'/observacoes/' + sensor.id + '/create'">Adicionar Observação</nuxt-link>
                    |
                    <button @click="gerarObservacao(sensor.id)">Gerar Observação</button>
                </td>
            </tr>
            <h4>Sensores de Produto</h4>
            <tr v-for="sensor in sensoresProduto">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ estadoToString(sensor.estado) }}</td>

                <td>{{ sensor.embalagens.length }}</td>
                <td>{{ sensor.observacoes.length }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    |
                    <nuxt-link :to="'/observacoes/' + sensor.id + '/create'">Adicionar Observação</nuxt-link>
                    |
                    <button @click="gerarObservacao(sensor.id)">Gerar Observação</button>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <button @click.prevent="refreshData">Refresh Data</button> <br />
    <br />
    <h2>Error messages:</h2>
    {{ messages }}
    <nuxt-link to="/">Voltar à Home</nuxt-link>
</template>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"

const authStore = useAuthStore()
const config = useRuntimeConfig()
const api = config.public.API_URL
const messages = ref([])
const { data: sensores, error, refresh } = await useFetch(`${api}/sensores`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authStore.token } })

const sensoresDisponiveis = ref(sensores.value.filter((sensor) => sensor.estado == 0))
const sensoresEmUso = ref(sensores.value.filter((sensor) => sensor.estado == 1))
const sensoresProduto = ref(sensores.value.filter((sensor) => sensor.estado == 2))

const deleteProduto = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}`, {
            method: "DELETE",
            headers: {
                'Authorization': 'Bearer ' + authStore.token
            },
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        messages.value.push(error.message)
    }
}

const hasEmbalagens = (sensor) => {
    console.log(sensor.id + " " + sensor.embalagens.length)
    if (sensor.embalagens.length > 0) {
        return true
    }
    return false
}

const gerarObservacao = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}/gerarObservacao`, {
            method: "POST",
            headers: {
                'Authorization': 'Bearer ' + authStore.token
            }
        })
        if (response.status == 200) {
            refresh()
        }
    } catch (error) {
        messages.value.push(error.message)
    }
}

const estadoToString = (estado) => {
    switch (estado) {
        case 0:
            return "Disponivel"
        case 1:
            return "Em Uso"
        case 2:
            return "Em Produto"
    }
}

let intervalId = ref(null) // Use ref for reactive storage of intervalId

const on = ref(false)

const toggleGerarObservacoesAutomaticas = async () => {
    on.value = !on.value

    // Check if intervalId is already defined before attempting to clear it
    if (intervalId.value) {
        clearInterval(intervalId.value)
    }

    if (on.value) {
        intervalId.value = setInterval(async () => {
            if (!on.value) {
                clearInterval(intervalId.value)
                return
            }

            sensores.value.forEach(async (sensor) => {
                if (sensor.estado == 1 || sensor.estado == 2) {
                    await gerarObservacao(sensor.id)
                }
            })
        }, 3000)
    }
}

// Watch for changes in on.value and clear the interval when it becomes false
watch(on, (newValue) => {
    if (!newValue && intervalId.value) {
        clearInterval(intervalId.value)
    }
})

watch(sensores, (newSensores) => {
    sensoresDisponiveis.value = newSensores.filter((sensor) => sensor.estado == 0)
    sensoresEmUso.value = newSensores.filter((sensor) => sensor.estado == 1)
    sensoresProduto.value = newSensores.filter((sensor) => sensor.estado == 2)
})
</script>

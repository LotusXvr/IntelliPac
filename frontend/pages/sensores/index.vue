<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="sensores/create">Criar novo Sensor</nuxt-link>
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
                <td>{{ sensor.estado }}</td>
                <td>{{ sensor.embalagens.length }}</td>
                <td>{{ sensor.observacoes.length }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    |
                    <button @click="deleteProduto(sensor.id)">Excluir</button>
                </td>
            </tr>
            <h4>Sensores em Uso</h4>
            <tr v-for="sensor in sensoresEmUso">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ sensor.estado }}</td>

                <td>{{ sensor.embalagens.length }}</td>
                <td>{{ sensor.observacoes.length }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    |
                    <button @click="deleteProduto(sensor.id)">Excluir</button>
                    |
                    <nuxt-link :to="'/observacoes/' + sensor.id + '/create'"
                        >Adicionar Observação</nuxt-link
                    >
                    |
                    <button @click="gerarObservacao(sensor.id)">Gerar Observação</button>
                </td>
            </tr>
            <h4>Sensores de Produto</h4>
            <tr v-for="sensor in sensoresProduto">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ sensor.estado }}</td>

                <td>{{ sensor.embalagens.length }}</td>
                <td>{{ sensor.observacoes.length }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.id">Editar</nuxt-link>
                    |
                    <button @click="deleteProduto(sensor.id)">Excluir</button>
                    |
                    <nuxt-link :to="'/observacoes/' + sensor.id + '/create'"
                        >Adicionar Observação</nuxt-link
                    >
                    |
                    <button @click="gerarObservacao(sensor.id)">Gerar Observação</button>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <button @click.prevent="refresh">Refresh Data</button> <br />
    <br />
    <nuxt-link to="/">Voltar à Home</nuxt-link>
</template>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: sensores, error, refresh } = await useFetch(`${api}/sensores`)

const sensoresDisponiveis = ref(sensores.value.filter((sensor) => sensor.estado == 0))
const sensoresEmUso = ref(sensores.value.filter((sensor) => sensor.estado == 1))
const sensoresProduto = ref(sensores.value.filter((sensor) => sensor.estado == 2))

const deleteProduto = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}`, {
            method: "DELETE",
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}

const gerarObservacao = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}/gerarObservacao`, {
            method: "POST",
        })
        if (response.status == 200) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>

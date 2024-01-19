<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="observacoes/create">Criar nova observação</nuxt-link>
        <h2>Observações</h2>
        <table>
            <tr>
                <th>id</th>
                <th>Data de registo</th>
                <th>Valor</th>
                <th>Id do Sensor</th>
            </tr>
            <tr v-for="observacao in observacoes">
                <td>{{ observacao.id }}</td>
                <td>{{ observacao.timestamp }}</td>
                <td>{{ observacao.valor }}</td>
                <td>{{ observacao.sensorId }}</td>

                <td>
                    <nuxt-link :to="`/observacoes/${observacao.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/observacoes/edit/' + observacao.id">Editar</nuxt-link>
                    |
                    <button @click="deleteObservacao(observacao.id)">Excluir</button>
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
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
import Navbar from "~/layouts/nav-bar.vue"
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: observacoes, error, refresh } = await useFetch(`${api}/observacoes`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authStore.token } })

const deleteObservacao = async (id) => {
    try {
        const response = await fetch(`${api}/observacoes/${id}`, {
            method: "DELETE",
<<<<<<< Updated upstream
            headers: { 'Authorization': 'Bearer ' + authStore.token }
=======
            headers: { Authorization: "Bearer " + authStore.token },
>>>>>>> Stashed changes
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>

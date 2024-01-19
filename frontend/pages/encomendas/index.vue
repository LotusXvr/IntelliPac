<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="encomendas/create" v-if="user.role == 'OperadorDeLogistica'"
            >Criar nova Encomenda</nuxt-link
        >
        <h2>Encomendas</h2>
        <table>
            <tr>
                <th>Cliente</th>
                <th>Data Encomenda</th>
                <th>Operador</th>
                <th style="padding-right: 50px">Estado</th>
                <th>actions</th>
            </tr>
            <tr v-for="encomenda in encomendas">
                <td>{{ encomenda.consumidorFinal }}</td>
                <td>{{ encomenda.dataEncomenda }}</td>
                <td>{{ encomenda.operadorLogistica }}</td>
                <td style="padding-right: 50px">{{ encomenda.estado }}</td>
                <td>
                    <nuxt-link :to="`/encomendas/${encomenda.id}`">Detalhes</nuxt-link>
                    |
                    <span v-if="user !== null && user.role != 'Cliente'">
                        <nuxt-link
                            v-if="encomenda.estado !== 'PENDENTE'"
                            :to="'/encomendas/edit/' + encomenda.id + '/estado'"
                            >Editar Estado</nuxt-link
                        >
                        |
                        <nuxt-link
                            v-if="encomenda.estado === 'PENDENTE'"
                            :to="'/encomendas/edit/' + encomenda.id + '/embalagemTransporte'"
                            >Adicionar Embalagem</nuxt-link
                        >
                        <nuxt-link
                            v-if="encomenda.estado === 'PROCESSAMENTO'"
                            :to="'/encomendas/edit/' + encomenda.id + '/embalagemTransporte'"
                            >Alterar Embalagem</nuxt-link
                        >
                        |
                    </span>

                    <button @click="deleteEncomenda(encomenda.id)">Excluir</button>
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
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const { user } = authStore
const config = useRuntimeConfig()
const api = config.public.API_URL
const {
    data: encomendas,
    error,
    refresh,
} = await useFetch(`${api}/encomendas/username/${user.username}`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})

const deleteEncomenda = async (id) => {
    try {
        const response = await fetch(`${api}/encomendas/${id}`, {
            method: "DELETE",
            headers: { Authorization: "Bearer " + authStore.token },
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>

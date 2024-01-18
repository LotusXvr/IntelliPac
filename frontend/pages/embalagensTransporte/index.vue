<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="embalagensTransporte/create">Criar nova Embalagem De Transporte</nuxt-link>
        <h2>Embalagens De Transporte</h2>
        <table>
            <tr>
                <th>Id</th>
                <th>Material</th>
                <th>Altura(cm)</th>
                <th>Largura(cm)</th>
                <th>Comprimento(cm)</th>
                <th>Estado</th>
                <th>Sensores</th>
                <th>actions</th>
            </tr>
            <tr v-for="embalagemDeTransporte in embalagensDeTransporte">
                <td>{{ embalagemDeTransporte.id }}</td>
                <td>{{ embalagemDeTransporte.material }}</td>
                <td>{{ embalagemDeTransporte.altura }}</td>
                <td>{{ embalagemDeTransporte.largura }}</td>
                <td>{{ embalagemDeTransporte.comprimento }}</td>
                <td>{{ estadoToString(embalagemDeTransporte.estado) }}</td>
                <td> {{ embalagemDeTransporte.sensores.length }}</td>
                <td>
                    <nuxt-link :to="`/embalagensTransporte/${embalagemDeTransporte.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/embalagensTransporte/edit/' + embalagemDeTransporte.id">Editar</nuxt-link>
                    |
                    <nuxt-link :to="`/embalagensTransporte/${embalagemDeTransporte.id}` + '/Sensores'">
                        Sensores</nuxt-link>
                    |
                    <button @click="deleteEncomenda(embalagemDeTransporte.id)">Excluir</button>
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
    data: embalagensDeTransporte,
    error,
    refresh,
} = await useFetch(`${api}/embalagensDeTransporte`)

const deleteEncomenda = async (id) => {
    try {
        const response = await fetch(`${api}/embalagensDeTransporte/${id}`, {
            method: "DELETE",
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}

const estadoToString = (estado) => {
    switch (estado) {
        case 0:
            return "Disponivel"
        case 1:
            return "Em Transporte"
        default:
            return "Estado Inválido"
    }
}
</script>

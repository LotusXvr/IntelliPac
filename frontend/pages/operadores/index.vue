<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="operadores/create">Criar um novo operador</nuxt-link>
        <h2>Operadores</h2>
        <table>
            <tr>
                <th>Nome</th>
                <th>Username</th>
                <th>Email</th>
                <th>actions</th>
            </tr>
            <tr v-for="operador in operadores">
                <td>{{ operador.name }}</td>
                <td>{{ operador.username }}</td>
                <td>{{ operador.email }}</td>
                <td>
                    <nuxt-link :to="`/operadores/${operador.username}`">Details</nuxt-link>
                    |
                    <nuxt-link :to="'/operadores/edit/' + operador.username">Editar</nuxt-link>
                    |
                    <button @click="deleteOperador(operador.username)">Excluir</button>
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
const {
    data: operadores,
    error,
    refresh,
} = await useFetch(`${api}/operadores`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})

const deleteOperador = async (username) => {
    try {
        const response = await fetch(`${api}/operadores/${username}`, {
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

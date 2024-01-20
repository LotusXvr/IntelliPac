<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="clientes/create">Criar um novo cliente</nuxt-link>
        <h2>Clientes</h2>
        <table>
            <tr>
                <th>Nome</th>
                <th>Username</th>
                <th>Email</th>
                <th>actions</th>
            </tr>
            <tr v-for="cliente in clientes">
                <td>{{ cliente.name }}</td>
                <td>{{ cliente.username }}</td>
                <td>{{ cliente.email }}</td>
                <td>
                    <nuxt-link :to="`/clientes/${cliente.username}`">Details</nuxt-link>
                    |
                    <nuxt-link :to="'/clientes/edit/' + cliente.username">Editar</nuxt-link>
                    |
                    <button @click="deleteCliente(cliente.username)">Excluir</button>
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
    data: clientes,
    error,
    refresh,
} = await useFetch(`${api}/clientes`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})

const deleteCliente = async (username) => {
    try {
        const response = await fetch(`${api}/clientes/${username}`, {
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

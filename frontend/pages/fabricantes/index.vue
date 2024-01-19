<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="fabricantes/create">Criar um novo fabricante</nuxt-link>
        <h2>Fabricantes</h2>
        <table>
            <tr>
                <th>Nome</th>
                <th>Username</th>
                <th>Email</th>
                <th>actions</th>
            </tr>
            <tr v-for="fabricante in fabricantes">
                <td>{{ fabricante.nome }}</td>
                <td>{{ fabricante.username }}</td>
                <td>{{ fabricante.email }}</td>
                <td>
                    <nuxt-link :to="`/fabricantes/${fabricante.username}`">Details</nuxt-link>
                    |
                    <nuxt-link :to="'/fabricantes/edit/' + fabricante.username">Editar</nuxt-link>
                    |
                    <button @click="deleteFabricante(fabricante.username)">Excluir</button>
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
import Navbar from "~/layouts/nav-bar.vue";
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: fabricantes, error, refresh } = await useFetch(`${api}/fabricantes`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})

const deleteFabricante = async (username) => {
    try {
        const response = await fetch(`${api}/fabricantes/${username}`, {
            method: "DELETE",headers: {'Authorization': 'Bearer ' + authStore.token}
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>

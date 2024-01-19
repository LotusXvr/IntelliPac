<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <h2>Produtos</h2>
        <div v-if="produtos.length === 0">
            <p>Não existem produtos fisicos em transporte ou entregues.</p>
        </div>
        <table v-else>
            <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Fabricante</th>
                <th>Peso</th>
                <th>actions</th>
            </tr>
            <tr v-for=" produto  in  produtos ">
                <td> {{ produto.id }}</td>
                <td>{{ produto.nome }}</td>
                <td>{{ produto.fabricanteUsername }}</td>
                <td>{{ produto.peso }}</td>
                <td style="display: flex; align-items: center;">
                    <nuxt-link :to="`/produtosFisico/${produto.id}`">Detalhes</nuxt-link>
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
import { useAuthStore } from "../store/auth-store.js"
import Navbar from "~/layouts/nav-bar.vue";
const config = useRuntimeConfig()
const authStore = useAuthStore()
const { user } = authStore
const api = config.public.API_URL
const { data: produtos, error, refresh } = await useFetch(`${api}/produtosFisicos/fabricante/${user.username}`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})


</script>
